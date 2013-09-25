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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.recommenders.plista.log.DataLogger;

/**
 * An impression object. Should contain userID, itemID, domainID, and timeStamp.
 * Additional values or jSON might also be available.
 *
 * @author andreas, alan, alejandro
 *
 */
public class ChallengeMessage implements Message {

    private final static DataLogger logger = DataLogger.getLogger(ChallengeMessage.class);
    public final static String MSG_UPDATE = "item_update";
    public final static String MSG_REC_REQUEST = "recommendation_request";
    public final static String MSG_EVENT_NOTIFICATION = "event_notification";
    public final static String MSG_NOTIFICATION_IMPRESSION = "impression";
    public final static String MSG_NOTIFICATION_IMPRESSION_EMPTY = "impression_empty";
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
    public static final Integer NOTIFICATION_TYPE_ID = 9;
    //
    public static final Integer ITEM_RECOMMENDABLE_ID = 10;
    public static final Integer ITEM_TEXT_ID = 11;
    public static final Integer NUMBER_OF_ITEM_UPDATES_ID = 12;
    public static final Integer ITEM_TITLE_ID = 13;
    public static final Integer ITEM_CREATED_ID = 14;
    public static final Integer ITEM_UPDATED_ID = 15;
    public static final Integer ITEM_URL_ID = 16;
    public static final Integer ITEM_CATEGORY_ID = 17;
    public static final Integer ITEM_CONTENT_ID = 18;
    public static final Integer SOURCE_ITEM_ID = 19;
    //
    public static final Integer NUMBER_DISPLAYED_AS_REC_ID = 30;
    public static final Integer LIST_OF_DISPLAYED_RECS_ID = 31;
    public static final Integer DO_RECOMMEND_ID = 32;
    public static final Integer CONTEST_TEAM_ID = 33;
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

    public void setItemSourceID(final Long _itemID) {
        valuesByID.put(SOURCE_ITEM_ID, _itemID);
    }

    @Override
    public Long getItemSourceID() {
        return (Long) valuesByID.get(SOURCE_ITEM_ID);
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

    /**
     * Getter for content (convenience).
     *
     * @return the content
     */
    public String getItemContent() {
        return (String) valuesByID.get(ITEM_CONTENT_ID);
    }

    /**
     * Setter for content. (convenience)
     *
     * @param content the content.
     */
    public void setItemContent(final String content) {
        valuesByID.put(ITEM_CONTENT_ID, content);
    }

    @Override
    public String getItemURL() {
        return (String) valuesByID.get(ITEM_URL_ID);
    }

    /**
     * Setter for title. (convenience)
     *
     * @param url the title.
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

    public Long getItemUpdated() {
        return (Long) valuesByID.get(ITEM_UPDATED_ID);
    }

    public void setItemUpdated(final Long _itemUpdated) {
        valuesByID.put(ITEM_UPDATED_ID, _itemUpdated);
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

    @Override
    public List<Long> getRecommendedResults() {
        return getListOfDisplayedRecs();
    }

    public Long getContestTeamID() {
        return (Long) valuesByID.get(CONTEST_TEAM_ID);
    }

    public void setContestTeamID(final Long _teamID) {
        valuesByID.put(CONTEST_TEAM_ID, _teamID);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Parse the ORP json Messages.
     *
     * @param _jsonMessageBody
     * @return the parsed values encapsulated in a map; null if an error has
     * been detected.
     */
    @Override
    public Message parseItemUpdate(final String _jsonMessageBody, boolean doLogging) {
        ChallengeMessage result = new ChallengeMessage();
        try {
            final JSONObject jsonObj = (JSONObject) JSONValue.parse(_jsonMessageBody);
            if (jsonObj == null) {
                if (doLogging) {
                    logger.info("ERROR\tinvalid json object in update\t" + _jsonMessageBody);
                }
            } else {
                Long domainID = null;
                try {
                    domainID = Long.valueOf(jsonObj.get("domainid").toString());
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno domainID found in update\t" + jsonObj);
                    }
                }
                result.setDomainID(domainID);
                Long itemID = null;
                try {
                    itemID = Long.valueOf(jsonObj.get("id").toString());
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno itemID found in update\t" + jsonObj);
                    }
                }
                result.setItemID(itemID);
                Long category = null;
                try {
                    category = Long.parseLong(jsonObj.get("categoryid") + "");
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno category found in update\t" + jsonObj);
                    }
                }
                result.setItemCategory(category);
                String text = null;
                try {
                    text = jsonObj.get("text") + "";
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno text found in update\t" + jsonObj);
                    }
                }
                result.setItemText(text);
                String title = null;
                try {
                    title = jsonObj.get("title") + "";
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno title found in update\t" + jsonObj);
                    }
                }
                result.setItemTitle(title);
                String url = null;
                try {
                    url = jsonObj.get("url") + "";
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno url found in update\t" + jsonObj);
                    }
                }
                result.setItemURL(url);
                Boolean recommendable = null;
                try {
                    String flag = jsonObj.get("flag") + "";
                    recommendable = ("0".equals(flag));
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno recommendable found in update\t" + jsonObj);
                    }
                }
                result.setItemRecommendable(recommendable);
                Long timestamp = null;
                try {
                    timestamp = Long.valueOf(jsonObj.get("timestamp").toString());
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno timestamp found in update\t" + jsonObj);
                    }
                }
                result.setTimeStamp(timestamp);
                Long created = null;
                try {
                    String createdAt = jsonObj.get("created_at") + "";
                    SimpleDateFormat sdf;
                    sdf = ChallengeMessage.sdf.get();
                    created = sdf.parse(createdAt).getTime();
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno created_at found in update\t" + jsonObj);
                    }
                }
                result.setItemCreated(created);
                Long updated = null;
                try {
                    String updatedAt = jsonObj.get("updated_at") + "";
                    SimpleDateFormat sdf;
                    sdf = ChallengeMessage.sdf.get();
                    updated = sdf.parse(updatedAt).getTime();
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno updated_at found in update\t" + jsonObj);
                    }
                }
                result.setItemUpdated(updated);
                // update the correct timestamp (if not present, by default updated or created time)
                if (timestamp == null) {
                    if (updated == null) {
                        if (created != null) {
                            result.setTimeStamp(created);
                        }
                    } else {
                        result.setTimeStamp(updated);
                    }
                }
            }
        } catch (Exception e) {
            if (doLogging) {
                logger.info("EXCEPTION\t" + e.getMessage() + "\t" + _jsonMessageBody);
            }
        }
        return result;
    }

    /**
     * Parse the ORP json Messages.
     *
     * @param _jsonMessageBody
     * @return the parsed values encapsulated in a map; null if an error has
     * been detected.
     */
    @Override
    public Message parseRecommendationRequest(final String _jsonMessageBody, boolean doLogging) {
        ChallengeMessage result = new ChallengeMessage();
        try {
            final JSONObject jsonObj = (JSONObject) JSONValue.parse(_jsonMessageBody);
            if (jsonObj == null) {
                if (doLogging) {
                    logger.info("ERROR\tinvalid json object in event not\t" + _jsonMessageBody);
                }
            } else {
                // parse JSON structure to obtain "context.simple"
                JSONObject jsonObjectContext = (JSONObject) jsonObj.get("context");
                if (jsonObjectContext == null) {
                    if (doLogging) {
                        logger.info("ERROR\tcontext not found in event not\t" + jsonObj);
                    }
                } else {
                    JSONObject jsonObjectContextSimple = (JSONObject) jsonObjectContext.get("simple");
                    if (jsonObjectContextSimple == null) {
                        if (doLogging) {
                            logger.info("ERROR\tcontext.simple not found in event not\t" + jsonObjectContext);
                        }
                    } else {
                        Long domainID = null;
                        try {
                            domainID = Long.valueOf(jsonObjectContextSimple.get("27").toString());
                        } catch (Exception e) {
                            if (doLogging) {
                                logger.info("ERROR\tno domainID found in rec req\t" + jsonObjectContextSimple);
                            }
                        }
                        result.setDomainID(domainID);
                        Long itemID = null;
                        try {
                            itemID = Long.valueOf(jsonObjectContextSimple.get("25").toString());
                        } catch (Exception e) {
                            if (doLogging) {
                                logger.info("ERROR\tno itemID found in rec req\t" + jsonObjectContextSimple);
                            }
                        }
                        result.setItemID(itemID);
                        Long userID = null;
                        try {
                            userID = Long.valueOf(jsonObjectContextSimple.get("57").toString());
                        } catch (Exception e) {
                            if (doLogging) {
                                logger.info("ERROR\tno userID found in rec req\t" + jsonObjectContextSimple);
                            }
                        }
                        result.setUserID(userID);
                        Long teamID = null;
                        try {
                            teamID = Long.valueOf(jsonObjectContextSimple.get("41").toString());
                        } catch (Exception e) {
                            if (doLogging) {
                                logger.info("ERROR\tno contestTeamID found in rec req\t" + jsonObjectContextSimple);
                            }
                        }
                        result.setContestTeamID(teamID);
                    }
                    Long category = null;
                    JSONObject jsonObjectContextLists = (JSONObject) jsonObjectContext.get("lists");
                    if (jsonObjectContextLists == null) {
                        if (doLogging) {
                            logger.info("ERROR\tno lists found in rec req\t" + _jsonMessageBody);
                        }
                    } else {
                        JSONArray categories = (JSONArray) jsonObjectContextLists.get("11");
                        if (categories == null) {
                            if (doLogging) {
                                logger.info("ERROR\tno lists.11 (category) found in rec req\t" + _jsonMessageBody);
                            }
                        } else {
                            for (Object e : categories) {
                                category = Long.parseLong(e + "");
                            }
                        }
                    }
                    result.setItemCategory(category);
                }
                Long timestamp = System.currentTimeMillis();
                try {
                    timestamp = Long.valueOf(jsonObj.get("timestamp").toString());
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno timestamp found in rec req\t" + _jsonMessageBody);
                    }
                }
                result.setTimeStamp(timestamp);
                Long limit = 0L;
                try {
                    limit = (Long) jsonObj.get("limit");
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno limit found in rec req\t" + _jsonMessageBody);
                    }
                }
                result.setNumberOfRequestedResults(limit.intValue());
            }
        } catch (Exception e) {
            if (doLogging) {
                logger.info("EXCEPTION\t" + e.getMessage() + "\t" + _jsonMessageBody);
            }
        }
        return result;
    }

    /**
     * parse the event notification.
     *
     * @param _jsonMessageBody
     * @return
     */
    @Override
    public Message parseEventNotification(final String _jsonMessageBody, boolean doLogging) {
        ChallengeMessage result = new ChallengeMessage();
        try {
            final JSONObject jsonObj = (JSONObject) JSONValue.parse(_jsonMessageBody);

            if (jsonObj == null) {
                if (doLogging) {
                    logger.info("ERROR\tinvalid json object in event not\t" + _jsonMessageBody);
                }
            } else {
                Long timestamp = System.currentTimeMillis();
                try {
                    timestamp = Long.valueOf(jsonObj.get("timestamp").toString());
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno timestamp found in event not\t" + _jsonMessageBody);
                    }
                }
                result.setTimeStamp(timestamp);
                // impressionType
                String notificationType = null;
                try {
                    notificationType = jsonObj.get("type") + "";
                } catch (Exception e) {
                    if (doLogging) {
                        logger.info("ERROR\tno type found in event not\t" + jsonObj);
                    }
                }
                result.setNotificationType(notificationType);
                // parse JSON structure to obtain "context.simple"
                JSONObject jsonObjectContext = (JSONObject) jsonObj.get("context");
                if (jsonObjectContext == null) {
                    if (doLogging) {
                        logger.info("ERROR\tcontext not found in event not\t" + jsonObj);
                    }
                } else {
                    JSONObject jsonObjectContextSimple = (JSONObject) jsonObjectContext.get("simple");
                    if (jsonObjectContextSimple == null) {
                        if (doLogging) {
                            logger.info("ERROR\tcontext.simple not found in event not\t" + jsonObjectContext);
                        }
                    } else {
                        Long domainID = null;
                        try {
                            domainID = Long.valueOf(jsonObjectContextSimple.get("27").toString());
                        } catch (Exception e) {
                            if (doLogging) {
                                logger.info("ERROR\tinvalid domainID in event not\t" + jsonObjectContextSimple);
                            }
                        }
                        result.setDomainID(domainID);
                        Long itemID = null;
                        try {
                            itemID = Long.valueOf(jsonObjectContextSimple.get("25").toString());
                        } catch (Exception e) {
                            if (doLogging) {
                                logger.info("ERROR\tinvalid itemID in event not\t" + jsonObjectContextSimple);
                            }
                        }
                        result.setItemID(itemID);
                        Long userID = null;
                        try {
                            userID = Long.valueOf(jsonObjectContextSimple.get("57").toString());
                        } catch (Exception e) {
                            if (doLogging) {
                                logger.info("ERROR\tmissing userID in event not\t" + jsonObjectContextSimple);
                            }
                        }
                        result.setUserID(userID);
                        Long teamID = null;
                        try {
                            teamID = Long.valueOf(jsonObjectContextSimple.get("41").toString());
                        } catch (Exception e) {
                            if (doLogging) {
                                logger.info("ERROR\tmissing contestTeamID in event not\t" + jsonObjectContextSimple);
                            }
                        }
                        result.setContestTeamID(teamID);
                    }
                    Long category = null;
                    JSONObject jsonObjectContextLists = (JSONObject) jsonObjectContext.get("lists");
                    if (jsonObjectContextLists == null) {
                        if (doLogging) {
                            logger.info("ERROR\tno lists found in event not\t" + _jsonMessageBody);
                        }
                    } else {
                        JSONArray categories = (JSONArray) jsonObjectContextLists.get("11");
                        if (categories == null) {
                            if (doLogging) {
                                logger.info("ERROR\tno lists.11 (category) found in event not\t" + _jsonMessageBody);
                            }
                        } else {
                            for (Object e : categories) {
                                category = Long.parseLong(e + "");
                            }
                        }
                    }
                    result.setItemCategory(category);
                }
                // list of displayed recs
                List<Long> listOfDisplayedRecs = new ArrayList<Long>(6);
                JSONObject jsonObjectRecs = (JSONObject) jsonObj.get("recs");
                if (jsonObjectRecs == null) {
                    if (doLogging) {
                        logger.info("ERROR\timpression without recs in event not\t" + jsonObj);
                    }
                } else {
                    JSONObject jsonObjectRecsInt = (JSONObject) jsonObjectRecs.get("ints");
                    if (jsonObjectRecsInt == null) {
                        if (doLogging) {
                            logger.info("ERROR\timpression without recs.int in event not\t" + jsonObj);
                        }
                    } else {
                        JSONArray array = (JSONArray) jsonObjectRecsInt.get("3");
                        if (array == null) {
                            if (doLogging) {
                                logger.info("ERROR\timpression without recs.int.3 (displayed recs) in event not\t" + jsonObj);
                            }
                        } else {
                            for (Object arrayEntry : array) {
                                listOfDisplayedRecs.add(Long.valueOf(arrayEntry + ""));
                            }
                        }
                    }
                }
                result.setListOfDisplayedRecs(listOfDisplayedRecs);
                result.setNumberOfRequestedResults(listOfDisplayedRecs.size());
                if (MSG_NOTIFICATION_CLICK.equals(notificationType)) {
                    Long source = result.getItemID();
                    Long target = null;
                    if (result.getListOfDisplayedRecs().size() > 0) {
                        target = result.getListOfDisplayedRecs().get(0);
                    }
                    result.setItemID(target);
                    result.setItemSourceID(source);
                }
            }
        } catch (Throwable t) {
            if (doLogging) {
                logger.info("EXCEPTION\t" + t.getMessage() + "\t" + _jsonMessageBody);
            }
        }
        return result;
    }
}
