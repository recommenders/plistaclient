/*
 Copyright (c) 2013, TU Berlin
 Copyright (c) 2013, recommenders.net.
 Permission is hereby granted, free of charge, to any person obtaining 
 a copy of this software and associated documentation files (the "Software"),
 to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense,
 and/or sell copies of the Software, and to permit persons to whom the
 Software is furnished to do so, subject to the following conditions:
 The above copyright notice and this permission notice shall be included
 in all copies or substantial portions of the Software.
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 DEALINGS IN THE SOFTWARE.
 */
package net.recommenders.plista.client;

import java.io.BufferedReader;
import net.recommenders.plista.recommender.Recommender;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.recommenders.plista.log.DataLogger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Main class for handling the messages of the contest server.
 *
 * @author till
 *
 */
public class ContestHandler extends AbstractHandler implements Handler {

    private final static DataLogger logger = DataLogger.getLogger(ContestHandler.class);
    private Recommender recommender;
    private static final Message MESSAGE_PARSER = new ContestMessage();
    private final int teamID;
    private ExecutorService pool;

    public ContestHandler(final Properties _properties, final Recommender _recommender) {
        try {
            this.pool = Executors.newFixedThreadPool(Integer.parseInt(_properties.getProperty("plista.nThreads", "8")));
            // set the item ID
            this.teamID = Integer.parseInt(_properties.getProperty("plista.teamId"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("TEAM ID property must be set");
        }
        this.recommender = _recommender;
        Client.RecommenderInitializer.initRecommender(this, recommender, _properties, new String[]{"data.log"}, "MESSAGE");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jetty.server.Handler#handle(java.lang.String, org.eclipse.jetty.server.Request,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(String arg0, Request _breq, HttpServletRequest _request, HttpServletResponse _response)
            throws IOException, ServletException {

        if (_breq.getMethod().equals("POST")) {
            if (_breq.getContentLength() < 0) {
                // handles first message from the server - returns OK
                logger.info("INITIAL_MSG");
                response(_response, _breq, null, false);
            } else {
                String responseText = null;
                final BufferedReader bufferedReader = _breq.getReader();
                // handles all other request from the server: impressions etc.
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    if (_breq.getContentType().equals("application/x-www-form-urlencoded; charset=utf-8")) {
                        line = URLDecoder.decode(line, "utf-8");
                    }
                    sb.append(line);
                }
                bufferedReader.close();

                line = sb.toString();
                final JSONObject jObj = (JSONObject) JSONValue.parse(line);

                String msg = "unknown";
                try {
                    msg = jObj.get("msg").toString();
                } catch (NullPointerException e) {
                    logger.error("EXCEPTION\t" + e.getMessage());
                }

                responseText = handleMessage(msg, line);
                response(_response, _breq, responseText, true);
            }
        } else {
            // handles get requests.
            logger.debug("GET_REQ\t" + _breq.getRemoteAddr());
            response(_response, _breq, "Visit <h3><a href=\"http://www.recommenders.net\">recommenders.net</a></h3>", true);
        }

    }

    public String handleMessage(final String messageType, final String messageBody, final Recommender rec, final boolean doLogging) {
        String response = null;
        // parse the type of the event
        final Message item = MESSAGE_PARSER.parseEventNotification(messageBody, doLogging);

        // impression refers to articles read by the user
        if (messageType.equals(ContestMessage.MSG_IMPRESSION)) {
            response = handleImpression(item);
            pool.execute(
                    new Thread() {
                public void run() {
                    rec.impression(item);
                }
            });
        } else if (messageType.equals(ContestMessage.MSG_FEEDBACK)) {
            // click refers to recommendations clicked by the user
            pool.execute(
                    new Thread() {
                public void run() {
                    rec.click(item);
                }
            });
        } else {
            // Error handling
            if (doLogging) {
                logger.error("UNKNOWN_MSG\t" + messageType + "\t" + messageBody);
            }
        }
        return response;
    }

    /**
     * Method to handle incoming messages from the server.
     *
     * @param messageType
     *
     * @param _jsonMessageBody the incoming contest server message
     * @return the response to the contest server
     */
    private String handleMessage(final String messageType, final String _jsonMessageBody) {

        // write all data from the server to a file
        logger.data("MESSAGE\t" + messageType + "\t" + _jsonMessageBody);

        String response = handleMessage(messageType, _jsonMessageBody, recommender, true);

        logger.data("RESPONSE\t" + response);
        return response;
    }

    /**
     * Method to handle impression messages.
     *
     * @param _message incoming impression message
     * @return answer to the impression message
     */
    private String handleImpression(final Message _message) {
        String response = null;

        final boolean recommend = _message.doRecommend();
        if (recommend) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\"msg\":\"result\",\"items\":[");

            final List<Long> recs = this.recommender.recommend(_message, _message.getNumberOfRequestedResults());

            for (final Iterator<Long> iterator = recs.iterator(); iterator.hasNext();) {
                final Long contestItem = iterator.next();
                stringBuilder.append("{\"id\":\"" + contestItem + "\"}");
                if (iterator.hasNext()) {
                    stringBuilder.append(",");
                }
            }

            stringBuilder.append("],\"team\":{\"id\":" + this.teamID + "},\"version\":\"1.0\"}");
            response = stringBuilder.toString();
        }
        return response;
    }

    /**
     * Response handler.
     *
     * @param _response {@link javax.servlet.http.HttpServletResponse} object
     * @param _breq the initial request
     * @param _text response text
     * @param _b boolean to set whether the response text should be sent
     * @throws java.io.IOException
     */
    private void response(HttpServletResponse _response, Request _breq, String _text, boolean _b)
            throws IOException {

        _response.setContentType("text/html;charset=utf-8");
        _response.setStatus(HttpServletResponse.SC_OK);
        _breq.setHandled(true);

        if (_text != null && _b) {
            _response.getWriter().println(_text);
        }

    }
}
