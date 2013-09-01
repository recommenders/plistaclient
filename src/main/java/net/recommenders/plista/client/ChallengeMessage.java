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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An impression object. Should contain userID, itemID, domainID, and timeStamp.
 * Additional values or jSON might also be available.
 *
 * @author andreas, alan, alejandro
 *
 */
public class ChallengeMessage implements Message {

    private final static Logger logger = LoggerFactory.getLogger(ChallengeMessage.class);
    public final static String MSG_UPDATE = "item_update";
    public final static String MSG_REC_REQUEST = "recommendation_request";
    public final static String MSG_EVENT_NOTIFICATION = "event_notification";
    public final static String MSG_NOTIFICATION_IMPRESSION = "impression";
    public final static String MSG_NOTIFICATION_CLICK = "click";
    public final static String MSG_ERROR_NOTIFICATION = "error_notification";
    /**
     * The threadLocal simpleDateFormat
     */
    public static final ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            // the Date pattern used by plista
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        }
    };
    // access keys
    public static final Integer USER_ID = 1;
    public static final Integer ITEM_ID = 2;
    public static final Integer DOMAIN_ID = 3;
    public static final Integer TIMESTAMP_ID = 4;
    public static final Integer NUMBER_OF_REQUESTED_RESULTS_ID = 8;
    public static final Integer ITEM_RECOMMENDABLE_ID = 9;
    public static final Integer ITEM_TEXT_ID = 10;
    public static final Integer NUMBER_OF_ITEM_UPDATES_ID = 11;
    public static final Integer NOTIFICATION_TYPE_ID = 12;
    public static final Integer NUMBER_DISPLAYED_AS_REC_ID = 13;
    public static final Integer LIST_OF_DISPLAYED_RECS_ID = 14;
    public static final Integer ITEM_TITLE_ID = 15;
    public static final Integer ITEM_CREATED_ID = 16;
    public static final Integer ITEM_URL_ID = 17;
    public static final Integer ITEM_CATEGORY_ID = 17;
    public static final Integer DO_RECOMMEND_ID = 20;
    // /////////////////////////////////////////////////////////////////////////////////////////
    /**
     * a hashMap storing the impression properties
     */
    private final Map<Integer, Object> valuesByID = new HashMap<Integer, Object>();

    // /////////////////////////////////////////////////////////////////////////////////////////
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
        return (Long) valuesByID.get(TIMESTAMP_ID);
    }

    public void setTimeStamp(final Long timestamp) {
        valuesByID.put(TIMESTAMP_ID, timestamp);
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

    @Override
    public Long getItemSourceID() {
        // not available
        return null;
    }

    public Long getItemCategory() {
        return (Long) valuesByID.get(ITEM_CATEGORY_ID);
    }

    public void setItemCategory(final Long category) {
        valuesByID.put(ITEM_CATEGORY_ID, category);
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

    @Override
    public String getItemTitle() {
        return (String) valuesByID.get(ITEM_TITLE_ID);
    }

    /**
     * Setter for title. (convenience)
     *
     * @param title the title.
     */
    public void setItemTitle(final String title) {
        valuesByID.put(ITEM_TITLE_ID, title);
    }

    @Override
    public String getItemURL() {
        return (String) valuesByID.get(ITEM_URL_ID);
    }

    /**
     * Setter for title. (convenience)
     *
     * @param title the title.
     */
    public void setItemURL(final String url) {
        valuesByID.put(ITEM_URL_ID, url);
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

    @Override
    public Long getItemCreated() {
        return (Long) valuesByID.get(ITEM_CREATED_ID);
    }

    public void setItemCreated(final Long _itemCreated) {
        valuesByID.put(ITEM_CREATED_ID, _itemCreated);
    }

    @Override
    public Boolean doRecommend() {
        return (Boolean) this.valuesByID.get(DO_RECOMMEND_ID);
    }

    public void setDoRecommend(Boolean _doRecommend) {
        this.valuesByID.put(DO_RECOMMEND_ID, _doRecommend);
    }

    /**
     * Getter for notification type.
     *
     * @return the notificationType
     */
    public String getNotificationType() {
        return (String) valuesByID.get(NOTIFICATION_TYPE_ID);
    }

    /**
     * Setter for notification type.
     *
     * @param _notificationType the notificationType to be set
     */
    public void setNotificationType(final String _notificationType) {
        valuesByID.put(NOTIFICATION_TYPE_ID, _notificationType);
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
        valuesByID
                .put(NUMBER_OF_REQUESTED_RESULTS_ID, numberOfRequestedResults);
    }

    /**
     * Getter for number of Updates
     *
     * @return the number of updates, might null
     */
    public Integer getNumberOfUpdates() {
        return (Integer) this.valuesByID.get(NUMBER_OF_ITEM_UPDATES_ID);
    }

    /**
     * Setter for number of updates
     *
     * @param _numberOfUpdates the value to be set
     */
    public void setNumberOfUpdates(final Integer _numberOfUpdates) {
        this.valuesByID.put(NUMBER_OF_ITEM_UPDATES_ID, _numberOfUpdates);
    }

    /**
     * Increment the number of updates
     */
    public void incNumberOfUpdates() {
        Integer oldValue = getNumberOfUpdates();
        if (oldValue == null) {
            setNumberOfUpdates(1);
        } else {
            setNumberOfUpdates(Integer.valueOf(1 + oldValue.intValue()));
        }
    }

    /**
     * Setter for numberDisplayedAsRec
     *
     * @param _numberDisplayedAsRec
     */
    public void setNumberDisplayedAsRec(final Integer _numberDisplayedAsRec) {
        this.valuesByID.put(NUMBER_DISPLAYED_AS_REC_ID, _numberDisplayedAsRec);
    }

    /**
     * Getter for numberDisplayedAsRec
     *
     * @return how often the item has been displayed as recommendation
     */
    public Integer getNumberDisplayedAsRec() {
        return (Integer) this.valuesByID.get(NUMBER_DISPLAYED_AS_REC_ID);
    }

    /**
     * Increment the NumberDisplayedAsRec
     */
    public void incNumberDisplayedAsRec() {
        Integer oldValue = getNumberDisplayedAsRec();
        if (oldValue == null) {
            setNumberDisplayedAsRec(1);
        } else {
            setNumberDisplayedAsRec(Integer.valueOf(1 + oldValue.intValue()));
        }
    }

    /**
     * Setter for listOfDisplayedRecs
     *
     * @param _listOfDisplayedRecs the list of displayed recommendations
     */
    public void setListOfDisplayedRecs(final List<Long> _listOfDisplayedRecs) {
        this.valuesByID.put(LIST_OF_DISPLAYED_RECS_ID, _listOfDisplayedRecs);
    }

    /**
     * Getter for ListOfDisplayedRecs
     *
     * @return the list of displayed recommendations
     */
    @SuppressWarnings("unchecked")
    public List<Long> getListOfDisplayedRecs() {
        return (List<Long>) this.valuesByID.get(LIST_OF_DISPLAYED_RECS_ID);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Parse the ORP json Messages.
     *
     * @param _jsonMessageBody
     * @return the parsed values encapsulated in a map; null if an error has
     * been detected.
     */
    public Message parseItemUpdate(String _jsonMessageBody) {
        try {
            final JSONObject jsonObj = (JSONObject) JSONValue
                    .parse(_jsonMessageBody);

            String itemID = jsonObj.get("id") + "";
            String domainID = jsonObj.get("domainid") + "";
            String categoryID = jsonObj.get("categoryid") + "";
            String text = jsonObj.get("text") + "";
            String title = jsonObj.get("title") + "";
            String url = jsonObj.get("url") + "";
            String flag = jsonObj.get("flag") + "";
            boolean recommendable = ("0".equals(flag));
            Long timestamp = System.currentTimeMillis();
            try {
                timestamp = Long.valueOf(jsonObj.get("timestamp").toString());
            } catch (Exception e) {
                logger.info("EXCEPTION\tno timestamp found\t" + _jsonMessageBody);
            }

            // parse date, now is default
            String createdAt = jsonObj.get("created_at") + "";
            Long created = System.currentTimeMillis();
            try {
                SimpleDateFormat sdf;
                sdf = ChallengeMessage.sdf.get();
                created = sdf.parse(createdAt).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ChallengeMessage result = new ChallengeMessage();
            result.setItemID(Long.valueOf(itemID));
            result.setDomainID(Long.valueOf(domainID));
            result.setItemCategory(Long.valueOf(categoryID));
            result.setTimeStamp(timestamp);
            result.setItemCreated(created);
            result.setItemText(text);
            result.setItemTitle(title);
            result.setItemURL(url);
            result.setItemRecommendable(recommendable);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parse the ORP json Messages.
     *
     * @param _jsonMessageBody
     * @return the parsed values encapsulated in a map; null if an error has
     * been detected.
     */
    public Message parseRecommendationRequest(String _jsonMessageBody) {
        try {
            final JSONObject jsonObj = (JSONObject) JSONValue.parse(_jsonMessageBody);

            // parse JSON structure to obtain "context.simple"	
            JSONObject jsonObjectContext = (JSONObject) jsonObj.get("context");
            JSONObject jsonObjectContextSimple = (JSONObject) jsonObjectContext.get("simple");

            Long domainID = -3L;
            try {
                domainID = Long.valueOf(jsonObjectContextSimple.get("27").toString());
            } catch (Exception e) {
                logger.info("EXCEPTION\tno domainID found\t" + _jsonMessageBody);
            }
            Long itemID = null;
            try {
                itemID = Long.valueOf(jsonObjectContextSimple.get("25").toString());
            } catch (Exception e) {
                logger.info("EXCEPTION\tno itemID found\t" + _jsonMessageBody);
            }
            Long userID = -2L;
            try {
                userID = Long.valueOf(jsonObjectContextSimple.get("57").toString());
            } catch (Exception e) {
                logger.info("EXCEPTION\tno userID found\t" + _jsonMessageBody);
            }
            Long timestamp = System.currentTimeMillis();
            try {
                timestamp = Long.valueOf(jsonObj.get("timestamp").toString());
            } catch (Exception e) {
                logger.info("EXCEPTION\tno timestamp found\t" + _jsonMessageBody);
            }
            Long limit = 0L;
            try {
                limit = (Long) jsonObj.get("limit");
            } catch (Exception e) {
                logger.info("EXCEPTION\tno limit found\t" + _jsonMessageBody);
            }
            Long category = null;
            try {
                JSONObject jsonObjectContextLists = (JSONObject) jsonObjectContext.get("lists");
                category = Long.valueOf(jsonObjectContextLists.get("11").toString().replace("[", "").replace("]", ""));
            } catch (Exception e) {
                logger.info("EXCEPTION\tno category found\t" + _jsonMessageBody);
            }

            ChallengeMessage result = new ChallengeMessage();
            result.setUserID(userID);
            result.setItemID(itemID);
            result.setDomainID(domainID);
            result.setTimeStamp(timestamp);
            result.setNumberOfRequestedResults(limit.intValue());
            result.setItemCategory(category);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * parse the event notification.
     *
     * @param _jsonMessageBody
     * @return
     */
    public Message parseEventNotification(final String _jsonMessageBody) {
        try {
            final JSONObject jsonObj = (JSONObject) JSONValue.parse(_jsonMessageBody);

            // parse JSON structure to obtain "context.simple"
            JSONObject jsonObjectContext = (JSONObject) jsonObj.get("context");
            JSONObject jsonObjectContextSimple = (JSONObject) jsonObjectContext.get("simple");

            Long domainID = null;
            try {
                domainID = Long.valueOf(jsonObjectContextSimple.get("27").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Long itemID = null;
            try {
                itemID = Long.valueOf(jsonObjectContextSimple.get("25").toString());
            } catch (Exception e) {
                logger.info("EXCEPTION\tinvalid itemID\t" + jsonObjectContextSimple);
                //e.printStackTrace();
            }
            Long userID = -2L;
            try {
                userID = Long.valueOf(jsonObjectContextSimple.get("57").toString());
            } catch (Exception e) {
                // TODO missing userID
                logger.info("EXCEPTION\tmissing userID\t" + jsonObjectContextSimple);
                //e.printStackTrace();
            }
            Long timestamp = System.currentTimeMillis();
            try {
                timestamp = Long.valueOf(jsonObj.get("timestamp").toString());
            } catch (Exception e) {
                logger.info("EXCEPTION\tno timestamp found\t" + _jsonMessageBody);
            }
            Long category = null;
            try {
                JSONObject jsonObjectContextLists = (JSONObject) jsonObjectContext.get("lists");
                category = Long.valueOf(jsonObjectContextLists.get("11").toString().replace("[", "").replace("]", ""));
            } catch (Exception e) {
                logger.info("EXCEPTION\tno category found\t" + _jsonMessageBody);
            }

            // impressionType
            String notificationType = null;
            try {
                notificationType = jsonObj.get("type") + "";
            } catch (Exception e) {
                e.printStackTrace();
            }

            // list of displayed recs
            List<Long> listOfDisplayedRecs = new ArrayList<Long>(6);
            try {
                JSONObject jsonObjectRecs = (JSONObject) jsonObj.get("recs");
                if (jsonObjectRecs == null) {
                    logger.info("EXCEPTION\timpression without recs\t" + jsonObj);
                } else {
                    JSONObject jsonObjectRecsInt = (JSONObject) jsonObjectRecs.get("ints");
                    JSONArray array = (JSONArray) jsonObjectRecsInt.get("3");
                    for (Object arrayEntry : array) {
                        listOfDisplayedRecs.add(Long.valueOf(arrayEntry + ""));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // create the result and return
            ChallengeMessage result = new ChallengeMessage();
            result.setUserID(userID);
            result.setItemID(itemID);
            result.setDomainID(domainID);
            result.setTimeStamp(timestamp);
            result.setNotificationType(notificationType);
            result.setListOfDisplayedRecs(listOfDisplayedRecs);
            result.setItemCategory(category);

            return result;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}
