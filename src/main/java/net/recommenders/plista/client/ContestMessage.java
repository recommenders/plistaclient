package net.recommenders.plista.client;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: alan
 * Date: 2013-08-28
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
public class ContestMessage implements Message {

    private static final Logger logger = Logger.getLogger(ContestMessage.class);

    public final static String MSG_IMPRESSION = "impression";
    public final static String MSG_FEEDBACK = "feedback";

    // access keys
    public static final Integer USER_ID = 1;
    public static final Integer ITEM_ID = 2;
    public static final Integer DOMAIN_ID = 3;
    public static final Integer TIMESTAMP_ID = 4;
    public static final Integer NUMBER_OF_REQUESTED_RESULTS_ID = 8;
    public static final Integer RECOMMENDABLE_ID = 9;
    public static final Integer ITEM_TEXT_ID = 10;
    public static final Integer RECOMMEND_ID = 11;

    //
    private final Map<Integer, Object> valuesByID = new HashMap<Integer, Object>();

    /**
     * Getter for userID. (convenience)
     *
     * @return the userID
     */
    public Long getUserID() {
        return (Long) valuesByID.get(USER_ID);
    }

    /**
     * Getter for itemID. (convenience)
     *
     * @return the itemID
     */
    public Long getItemID() {
        return (Long) valuesByID.get(ITEM_ID);
    }


    /**
     * Setter for itemID. (convenience)
     *
     * @param _itemID the itemID
     */
    public void setItemID(final Long _itemID) {
        valuesByID.put(ITEM_ID, _itemID);
    }

    /**
     * Getter for domainID. (convenience)
     *
     * @return the domainID
     */
    public Long getDomainID() {
        return (Long) valuesByID.get(DOMAIN_ID);
    }

    /**
     * Getter for TimeStamp. (convenience)
     *
     * @return the timeStamp
     */
    public Long getTimeStamp() {
        return (Long) valuesByID.get(TIMESTAMP_ID);
    }

    /**
     * Getter for text (convenience).
     *
     * @return the text
     */
    public String getText() {
        return (String) valuesByID.get(ITEM_TEXT_ID);
    }

    @Override
    public String getNotificationType() {
        return null;
    }

    /**
     * Setter for text. (convenience)
     *
     * @param text
     *            the text.
     */
    public void setText(final String text) {
        valuesByID.put(ITEM_TEXT_ID, text);
    }

    /**
     * Getter for numberOfRequestedResults (convenience).
     *
     * @return the text
     */
    public Integer getNumberOfRequestedResults() {
        return (Integer) valuesByID.get(NUMBER_OF_REQUESTED_RESULTS_ID);
    }

    /**
     * Setter for numberOfRequestedResults. (convenience)
     *
     * @param numberOfRequestedResults
     *            the number of requested results.
     */
    public void setNumberOfRequestedResults(
            final Integer numberOfRequestedResults) {
        valuesByID
                .put(NUMBER_OF_REQUESTED_RESULTS_ID, numberOfRequestedResults);
    }

    /**
     * Setter for recommendable
     * @param _recommendable
     */
    public void setRecommendable(Boolean _recommendable) {
        this.valuesByID.put(RECOMMENDABLE_ID, _recommendable);
    }
    /**
     * Getter for recommendable
     * @return is the item recommendable
     */
    public Boolean getRecommendable() {
        return (Boolean) this.valuesByID.get(RECOMMENDABLE_ID);
    }

    /**
     * Setter for recommend
     * @param _recommend
     */
    public void setRecommend(Boolean _recommend) {
        this.valuesByID.put(RECOMMEND_ID, _recommend);
    }
    @Override
    public Boolean doRecommend() {
        return (Boolean) this.valuesByID.get(RECOMMEND_ID);
    }


    @Override
    public Message parseItemUpdate(String _jsonMessageBody) {
        return null;
    }

    @Override
    public Message parseRecommendationRequest(String _jsonMessageBody) {
        return null;
    }

    @Override
    public Message parseEventNotification(String _jsonMessageBody) {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(_jsonMessageBody);

            Long domain = jObj.getJSONObject("domain").getLong("id");
            Long itemID = jObj.getJSONObject("item").getLong("id");
            String title = jObj.getJSONObject("item").getString("title").replaceAll("\u00ad", "");
            String text =  jObj.getJSONObject("item").getString("text").replaceAll("\u00ad", "");
            String url =  jObj.getJSONObject("item").getString("url");
            Boolean recommendable =  jObj.getJSONObject("item").getBoolean("recommendable");
            Long created = jObj.getJSONObject("item").getLong("created");

            ContestMessage message = new ContestMessage();
            message.setItemID(itemID);
            message.setNumberOfRequestedResults(limit);
            message.setRecommend();
            message.setRecommendable();
            message.setText();
            message.setTitle();
        }  catch (Exception e){
            logger.error(e.getMessage());
        }

        return null;
    }
}
