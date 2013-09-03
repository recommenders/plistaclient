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

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.recommenders.plista.recommender.Recommender;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * Class to handle the communication with the plista challenge server Functions:
 * - parsing incoming HTTP-Requests - delegating requests according to their
 * types - responding to the plista challenge server
 *
 * @author till, andreas, alan, alejandro
 *
 */
public class ChallengeHandler extends AbstractHandler implements Handler {

    /**
     * Define the default logger
     */
    private final static Logger logger = Logger.getLogger(ChallengeHandler.class);
    /**
     * Define the default recommender, currently not used.
     */
    private Recommender recommender;
    private static final Message MESSAGE_PARSER = new ChallengeMessage();
    private ExecutorService pool;

    /**
     * Constructor, sets some default values.
     *
     * @param _properties
     * @param _recommender
     */
    public ChallengeHandler(final Properties _properties, final Recommender _recommender) {
        this.pool = Executors.newFixedThreadPool(Integer.parseInt(_properties.getProperty("plista.nThreads", "8")));
        this.recommender = _recommender;
        Client.RecommenderInitializer.initRecommender(this, recommender, _properties, new String[]{"data.log"}, "MESSAGE");
    }

    /**
     * Handle incoming messages. This method is called by plista We check the
     * message, and extract the relevant parameter values
     *
     * @see org.eclipse.jetty.server.Handler#handle(java.lang.String,
     * org.eclipse.jetty.server.Request, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    public void handle(String arg0, Request _breq, HttpServletRequest _request, HttpServletResponse _response)
            throws IOException, ServletException {

        // we can only handle POST messages
        if (_breq.getMethod().equals("POST")) {

            if (_breq.getContentLength() < 0) {
                // handles first message from the server - returns OK
//                System.out.println("[INFO] Initial Message with no content received.");
                logger.info("INITIAL_MSG");
                response(_response, _breq, null, false);
            } else {

                // handle the normal messages
                String typeMessage = _breq.getParameter("type");
                String bodyMessage = _breq.getParameter("body");

                // we may recode the body message
                if (_breq.getContentType().equals("application/x-www-form-urlencoded; charset=utf-8")) {
                    bodyMessage = URLDecoder.decode(bodyMessage, "utf-8");
                }
//                System.out.println(_breq.getParameterMap());

                // delegate the request and create a response message
                // send the response message as text
                String responseText = null;
                responseText = handleMessage(typeMessage, bodyMessage);
                response(_response, _breq, responseText, true);
            }
        } else {
            // GET requests are answered by a HTML page
            logger.debug("GET_REQ\t" + _breq.getRemoteAddr());
            response(_response, _breq, "Visit <h3><a href=\"http://www.recommenders.net\">recommenders.net</a></h3>", true);
        }
    }

    public String handleMessage(final String messageType, final String messageBody, final Recommender rec, final boolean doLogging) {
        // define a response object
        String response = null;

        // TODO handle "item_create"

        // in a complex if/switch statement we handle the differentTypes of messages
        if (ChallengeMessage.MSG_UPDATE.equalsIgnoreCase(messageType)) {

            // we extract itemID, domainID, text and the time, create/update
            final Message recommenderItem = MESSAGE_PARSER.parseItemUpdate(messageBody);

            // we mark this information in the article table
            if (recommenderItem.getItemID() != null) {
                rec.update(recommenderItem);
            }

            response = ";item_update successfull";
        } else if (ChallengeMessage.MSG_REC_REQUEST.equalsIgnoreCase(messageType)) {

            // we handle a recommendation request
            try {
                // parse the new recommender request
                Message input = MESSAGE_PARSER.parseRecommendationRequest(messageBody);
                rec.update(input);
                // gather the items to be recommended
                List<Long> resultList = rec.recommend(input, input.getNumberOfRequestedResults());
                if (resultList == null) {
                    response = "[]";
//                    System.out.println("invalid resultList");
                } else {
                    response = resultList.toString();
                }
                response = getRecommendationResultJSON(response);
            } catch (Throwable t) {
                if (doLogging) {
                    logger.error("EXCEPTION\t" + t.getMessage());
                }
            }
        } else if (ChallengeMessage.MSG_EVENT_NOTIFICATION.equalsIgnoreCase(messageType)) {
            // parse the type of the event
            final Message item = MESSAGE_PARSER.parseEventNotification(messageBody);
            final String eventNotificationType = item.getNotificationType();
            // impression refers to articles read by the user
            if (ChallengeMessage.MSG_NOTIFICATION_IMPRESSION.equalsIgnoreCase(eventNotificationType)) {
                if (item.getItemID() != null) {
                    pool.execute(
                            new Thread() {
                        public void run() {
                            rec.impression(item);
                        }
                    });
                    response = "handle impression eventNotification successful";
                }
                // click refers to recommendations clicked by the user
            } else if (ChallengeMessage.MSG_NOTIFICATION_CLICK.equalsIgnoreCase(eventNotificationType)) {
                if (item.getItemID() != null) {
                    pool.execute(
                            new Thread() {
                        public void run() {
                            rec.click(item);
                        }
                    });
                    response = "handle click eventNotification successful";
                }
            } else {
                if (doLogging) {
                    logger.info("UNKNOWN_EVNT\t" + messageType + "\t" + messageBody);
                }
            }

        } else if (ChallengeMessage.MSG_ERROR_NOTIFICATION.equalsIgnoreCase(messageType)) {
            if (doLogging) {
                logger.error("ERROR\t" + messageBody);
            }

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
     * @param messageType the messageType of the incoming contest server message
     * @param _jsonMessageBody the incoming contest server message
     * @return the response to the contest server
     */
    private String handleMessage(final String messageType, final String _jsonMessageBody) {

        // write all data from the server to a file
        logger.fatal("MESSAGE\t" + messageType + "\t" + _jsonMessageBody);

        String response = handleMessage(messageType, _jsonMessageBody, recommender, true);

        logger.fatal("RESPONSE\t" + response);
        return response;
    }

    /**
     * Response handler.
     *
     * @param _response {@link HttpServletResponse} object
     * @param _breq the initial request
     * @param _text response text
     * @param _b boolean to set whether the response text should be sent
     * @throws IOException
     */
    private void response(HttpServletResponse _response, Request _breq, String _text, boolean _b)
            throws IOException {
        // configure response parameter
        _response.setContentType("text/html;charset=utf-8");
        _response.setStatus(HttpServletResponse.SC_OK);
        _breq.setHandled(true);

        if (_text != null && _b) {
            _response.getWriter().println(_text);
            if (_text != null && !_text.startsWith("handle")) {
                logger.info("SEND\t" + _text);
            }
        }
    }

    /**
     * Create a json response object for recommendation requests.
     *
     * @param _itemsIDs a list as string
     * @return json
     */
    public static final String getRecommendationResultJSON(String _itemsIDs) {

        // TODO log invalid results
        if (_itemsIDs == null || _itemsIDs.length() == 0) {
            _itemsIDs = "[]";
        } else if (!_itemsIDs.trim().startsWith("[")) {
            _itemsIDs = "[" + _itemsIDs + "]";
        }
        // build result as JSON according to formal requirements
        String result = "{" + "\"recs\": {" + "\"ints\": {" + "\"3\": " + _itemsIDs + "}" + "}}";

        return result;
    }
}
