package net.recommenders.plista.client;

import net.recommenders.plista.recommender.Recommender;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Main class for handling the messages of the contest server.
 * 
 * @author till
 * 
 */
public class ContestHandler
                extends AbstractHandler {

    private final static Logger logger = LoggerFactory.getLogger(ContestHandler.class);

    private Recommender recommender;
    private Message message;

    private final int teamID;

    public ContestHandler(final Properties _properties, final Recommender _recommender) {

        try {
            // set the item ID
            this.teamID = Integer.parseInt(_properties.getProperty("plista.teamId"));

            // set propeties
            _recommender.setProperties(_properties);
            
            _recommender.init();
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("TEAM ID property must be set");
        }
        this.recommender = _recommender;
        this.message = null;
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
                response(_response, _breq, null, false);
            }
           /* else {
                String responseText = null;
                final BufferedReader bufferedReader = _breq.getReader();
                // handles all other request from the server: impressions etc.
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    if (_breq.getContentType().equals("application/x-www-form-urlencoded; charset=utf-8")) {
                        line = URLDecoder.decode(line, "utf-8");
                    }
                    final JSONObject jObj = (JSONObject) JSONValue.parse(line);

final JSONObject jObj = (JSONObject) JSONValue.parse(_jsonString);

        String msg = "unknown";
        try {
            msg = jObj.get("msg").toString();
        }
        catch (NullPointerException e) {
            logger.debug(e.getMessage());
        }

                    responseText = handleMessage(line);

                }
                bufferedReader.close();
                response(_response, _breq, responseText, true);

            }*/
            else {

                // handle the normal messages
                String typeMessage = _breq.getParameter("type");
                String bodyMessage = _breq.getParameter("body");

                // we may recode the body message
                if (_breq.getContentType().equals("application/x-www-form-urlencoded; charset=utf-8")) {
                    bodyMessage = URLDecoder.decode(bodyMessage, "utf-8");
                }
                System.out.println(_breq.getParameterMap());

                // delegate the request and create a response message
                // send the response message as text
                String responseText = null;
                responseText = handleMessage(typeMessage, bodyMessage);
                response(_response, _breq, responseText, true);
            }
        }
        else {
            // handles get requests.
            logger.debug("Get request from " + _breq.getRemoteAddr());
            response(_response, _breq, "Visit <h3><a href=\"http://www.recommenders.net\">recommenders.net</a></h3>", true);
        }

    }

    /**
     * Method to handle incoming messages from the server.
     * 
     * @param messageType
     *
     * @param  _jsonMessageBody
     *          the incoming contest server message
     * @return the response to the contest server
     */
    private String handleMessage(final String messageType, final String _jsonMessageBody) {

        String response = null;
        // make string to json object


        // write all data from the server to a file
        logger.info(messageType + "\t" + _jsonMessageBody);

        // parse the type of the event
        final Message item = message.parseEventNotification(_jsonMessageBody);

        // impression refers to articles read by the user
        if (messageType.equals(ContestMessage.MSG_IMPRESSION)) {


            response = handleImpression(item);
            new Thread() {

                public void run() {
                    recommender.impression(item);
                }

            }.start();
            if (response != null) {
                logger.info(response);
            }
            // click refers to recommendations clicked by the user
        }  else if (messageType.equals(ContestMessage.MSG_FEEDBACK)) {
            handleFeedback(item);
        }
        else {
            // Error handling
            logger.info(_jsonMessageBody);
        }

         return response;
    }

    /**
     * Method to handle impression messages.
     * 
     * @param _message
     *            incoming impression message
     * @return answer to the impression message
     */
    private String handleImpression(final Message _message) {
        String response = null;


        final Long client = _message.getUserID();
        final Long domain = _message.getDomainID();
        // some impressions do not have an item id
        Long id = (_message.getItemID() == null ? -1 : _message.getItemID());
        Integer limit = _message.getNumberOfRequestedResults();

        // if the impression is a recommendation request, compute
        // recommendations

        final boolean recommend = _message.doRecommend();
        if (recommend) {
            logger.debug("\nREQUEST: " + _message);

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\"msg\":\"result\",\"items\":[");
            final List<Long> recs = this.recommender.recommend(_message);

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
        logger.debug("RESPOND: " + response);
        return response;
    }

    /**
     * Method to handle feedback messages.
     * 
     * @param _message
     *            incoming feedback message
     * @return answer to the feedback message
     */
    private void handleFeedback(Message _message) {

        logger.debug("! Feedback:" + _message + "!");
        this.recommender.click(_message);

    }

    /**
     * Response handler.
     * 
     * @param _response
     *            {@link javax.servlet.http.HttpServletResponse} object
     * @param _breq
     *            the initial request
     * @param _text
     *            response text
     * @param _b
     *            boolean to set whether the response text should be sent
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
