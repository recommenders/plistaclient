package net.recommenders.plista.client;

/**
 * Created with IntelliJ IDEA.
 * User: alan
 * Date: 2013-08-28
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public interface Message {


    /**
     * Getter for userID. (convenience)
     *
     * @return the userID
     */
    public Long getUserID();

    /**
     * Getter for itemID. (convenience)
     *
     * @return the itemID
     */
    public Long getItemID();

    /**
     * Getter for domainID. (convenience)
     *
     * @return the domainID
     */
    public Long getDomainID();

    /**
     * Getter for TimeStamp. (convenience)
     *
     * @return the timeStamp
     */
    public Long getTimeStamp();

    /**
     * Getter for text (convenience).
     *
     * @return the text
     */
    public String getText();

    /**
     * Getter for notification type.
     * @return the notificationType
     */
    public String getNotificationType();

    /**
     * Getter for numberOfRequestedResults (convenience).
     *
     * @return the limit
     */
    public Integer getNumberOfRequestedResults();

    /**
     * Getter for recommendable
     * @return is the item recommendable
     */
    public Boolean getRecommendable();

    /**
     * Parse the  json Messages.
     * @param _jsonMessageBody
     * @return the parsed values encapsulated in a map; null if an error has been detected.
     */
    public Message parseItemUpdate(String _jsonMessageBody);

    /**
     * Parse the  json Messages.
     * @param _jsonMessageBody
     * @return the parsed values encapsulated in a map; null if an error has been detected.
     */
    public Message parseRecommendationRequest(String _jsonMessageBody);

    /**
     * parse the event notification.
     * @param _jsonMessageBody
     * @return
     */
    public Message parseEventNotification(final String _jsonMessageBody);



    }

