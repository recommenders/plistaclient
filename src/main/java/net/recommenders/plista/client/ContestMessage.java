package net.recommenders.plista.client;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

/**
 * Created with IntelliJ IDEA. User: alan Date: 2013-08-28 Time: 20:13 To change
 * this template use File | Settings | File Templates.
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
    public static final Integer RECOMMEND_ID = 9;
    public static final Integer ITEM_TEXT_ID = 10;
    public static final Integer ITEM_RECOMMENDABLE_ID = 11;
    public static final Integer ITEM_TITLE_ID = 12;
    public static final Integer ITEM_URL_ID = 14;
    public static final Integer ITEM_CREATED_ID = 15;
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

    public void setUserID(final Long _userID) {
        valuesByID.put(USER_ID, _userID);
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

    public void setDomainID(final Long id) {
        valuesByID.put(DOMAIN_ID, id);
    }

    /**
     * Getter for TimeStamp. (convenience)
     *
     * @return the timeStamp
     */
    public Long getTimeStamp() {
        return null;
    }

    @Override
    public String getNotificationType() {
        return null;
    }

    /**
     * Getter for text (convenience).
     *
     * @return the text
     */
    public String getItemText() {
        return (String) valuesByID.get(ITEM_TEXT_ID);
    }

    /**
     * Setter for text. (convenience)
     *
     * @param text the text.
     */
    public void setItemText(final String text) {
        valuesByID.put(ITEM_TEXT_ID, text);
    }

    public String getItemTitle() {
        return (String) valuesByID.get(ITEM_TITLE_ID);
    }

    public void setItemTitle(final String text) {
        valuesByID.put(ITEM_TITLE_ID, text);
    }

    public String getItemURL() {
        return (String) valuesByID.get(ITEM_URL_ID);
    }

    public void setItemURL(final String text) {
        valuesByID.put(ITEM_URL_ID, text);
    }

    public Long getItemCreated() {
        return (Long) valuesByID.get(ITEM_CREATED_ID);
    }

    public void setItemCreated(final Long time) {
        valuesByID.put(ITEM_CREATED_ID, time);
    }

    /**
     * Setter for recommendable
     *
     * @param _recommendable
     */
    public void setItemRecommendable(Boolean _recommendable) {
        this.valuesByID.put(ITEM_RECOMMENDABLE_ID, _recommendable);
    }

    /**
     * Getter for recommendable
     *
     * @return is the item recommendable
     */
    public Boolean getItemRecommendable() {
        return (Boolean) this.valuesByID.get(ITEM_RECOMMENDABLE_ID);
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
     * @param numberOfRequestedResults the number of requested results.
     */
    public void setNumberOfRequestedResults(
            final Integer numberOfRequestedResults) {
        valuesByID.put(NUMBER_OF_REQUESTED_RESULTS_ID, numberOfRequestedResults);
    }

    /**
     * Setter for recommend
     *
     * @param _recommend
     */
    public void setDoRecommend(Boolean _recommend) {
        this.valuesByID.put(RECOMMEND_ID, _recommend);
    }

    @Override
    public Boolean doRecommend() {
        return (Boolean) this.valuesByID.get(RECOMMEND_ID);
    }

    @Override
    public Message parseItemUpdate(String _jsonMessageBody) {
        // not available in contest
        return null;
    }

    @Override
    public Message parseRecommendationRequest(String _jsonMessageBody) {
        // not available in contest
        return null;
    }

    @Override
    public Message parseEventNotification(String _jsonMessageBody) {
        try {
            JSONObject jObj = getObject(_jsonMessageBody);
            ContestMessage message = new ContestMessage();

            Long userID = getClientIdFromImpression(jObj);
            message.setUserID(userID);
            Long domain = getDomainId(jObj);
            message.setDomainID(domain);
            Long itemID = getItemIdFromImpression(jObj);
            message.setItemID(itemID);
            String title = getItemTitleFromImpression(jObj);
            message.setItemTitle(title);
            String text = getItemTextFromImpression(jObj);
            message.setItemText(text);
            String url = getItemUrlFromImpression(jObj);
            message.setItemURL(url);
            Boolean recommendable = getItemRecommendableFromImpression(jObj);
            message.setItemRecommendable(recommendable);
            Long created = getItemCreatedFromImpression(jObj);
            message.setItemCreated(created);
            Integer limit = getConfigLimitFromImpression(jObj);
            message.setNumberOfRequestedResults(limit);
            Boolean recommend = getConfigRecommendFromImpression(jObj);
            message.setDoRecommend(recommend);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    // JSON methods
    public static Long getDomainId(JSONObject jObj) {
        try {
            return jObj.getJSONObject("domain").getLong("id");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Long getClientId(JSONObject jObj) {
        try {
            return jObj.getJSONObject("client").getLong("id");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Integer getContextCategoryId(JSONObject jObj) {
        try {
            return jObj.getJSONObject("context").getJSONObject("category").getInt("id");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Integer getConfigTeamId(JSONObject jObj) {
        try {
            return jObj.getJSONObject("config").getJSONObject("team").getInt("id");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Boolean isImpression(JSONObject jObj) {
        try {
            if (jObj.has("msg")) {
                if (jObj.getString("msg").equals("impression")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Boolean isFeedback(JSONObject jObj) {
        try {
            if (jObj.has("msg")) {
                if (jObj.getString("msg").equals("feedback")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    // Impression messages
    public static Integer getImpressionIdFromImpression(JSONObject jObj) {
        try {
            return jObj.getInt("id");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Long getClientIdFromImpression(JSONObject jObj) {
        return getClientId(jObj);
    }

    public static Long getDomainIdFromImpression(JSONObject jObj) {
        return getDomainId(jObj);
    }

    public static Long getItemIdFromImpression(JSONObject jObj) {
        try {
            return jObj.getJSONObject("item").getLong("id");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static String getItemTitleFromImpression(JSONObject jObj) {
        try {
            return jObj.getJSONObject("item").getString("title").replaceAll("\u00ad", "");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static String getItemTextFromImpression(JSONObject jObj) {
        try {
            return jObj.getJSONObject("item").getString("text").replaceAll("\u00ad", "");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static String getItemUrlFromImpression(JSONObject jObj) {
        try {
            return jObj.getJSONObject("item").getString("url");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static String getItemImgFromImpression(JSONObject jObj) {
        try {
            return jObj.getJSONObject("item").getString("img");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Long getItemCreatedFromImpression(JSONObject jObj) {
        try {
            return jObj.getJSONObject("item").getLong("created");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Boolean getItemRecommendableFromImpression(JSONObject jObj) {
        try {
            return jObj.getJSONObject("item").getBoolean("recommendable");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Integer getContextCategoryIdFromImpression(JSONObject jObj) {
        return getContextCategoryId(jObj);
    }

    public static Integer getConfigTeamIdFromImpression(JSONObject jObj) {
        return getConfigTeamId(jObj);
    }

    public static Double getConfigTimeoutFromImpression(JSONObject jObj) {
        try {
            return jObj.getJSONObject("config").getDouble("timeout");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Boolean getConfigRecommendFromImpression(JSONObject jObj) {
        try {
            return jObj.getJSONObject("config").getBoolean("recommend");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Integer getConfigLimitFromImpression(JSONObject jObj) {
        try {
            return jObj.getJSONObject("config").getInt("limit");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    // Feedback messages
    public static Long getClientIdFromFeedback(JSONObject jObj) {
        return getClientId(jObj);
    }

    public static Long getDomainIdFromFeedback(JSONObject jObj) {
        return getDomainId(jObj);
    }

    public static String getSourceIdFromFeedback(JSONObject jObj) {
        try {
            return jObj.getJSONObject("source").getString("id");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static String getTargetIdFromFeedback(JSONObject jObj) {
        try {
            return jObj.getJSONObject("target").getString("id");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Integer getContextCategoryIdFromFeedback(JSONObject jObj) {
        return getContextCategoryId(jObj);
    }

    public static Integer getConfigTeamIdFromFeedback(JSONObject jObj) {
        return getConfigTeamId(jObj);
    }

    // Error messages
    public static String[] getInvalidItemsFromError(JSONObject jObj) {
        try {
            String[] errorItems = null;
            if (jObj.has("error")) {
                String error = jObj.getString("error");
                if (error.contains("invalid items returned:")) {
                    String tmpError = error.replace("invalid items returned:", "");
                    errorItems = tmpError.split(",");
                }
            }
            return errorItems;
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static Long getDomainIdFromError(JSONObject jObj) {
        return getDomainId(jObj);
    }

    public static Integer getTeamId(JSONObject jObj) {
        try {
            return jObj.getJSONObject("team").getInt("id");
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return null;
        }
    }

    public static JSONObject getObject(String json) throws JSONException {
        final JSONObject jObj = new JSONObject(json);
        return jObj;
    }
}
