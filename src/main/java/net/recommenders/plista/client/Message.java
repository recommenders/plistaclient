/*
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

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: alan Date: 2013-08-28 Time: 14:59 To change
 * this template use File | Settings | File Templates.
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

    public Long getItemSourceID();

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
    public String getItemText();

    /**
     * Getter for title (convenience).
     *
     * @return the title
     */
    public String getItemTitle();

    /**
     * Getter for category
     *
     * @return the category
     */
    public Long getItemCategory();

    /**
     * Getter for URL.
     *
     * @return the URL
     */
    public String getItemURL();

    /**
     * Getter for creation time
     *
     * @return the creation time
     */
    public Long getItemCreated();

    /**
     * Getter for recommendable
     *
     * @return is the item recommendable
     */
    public Boolean getItemRecommendable();

    /**
     * Getter for notification type.
     *
     * @return the notificationType
     */
    public String getNotificationType();

    /**
     * Getter for numberOfRequestedResults (convenience).
     *
     * @return the limit
     */
    public Integer getNumberOfRequestedResults();
    
    public List<Long> getRecommendedResults();

    /**
     * Check whether recommend or not
     *
     * @return recommendation or not
     */
    public Boolean doRecommend();
    
    public Long getContestTeamID();

    /**
     * Parse the json Messages.
     *
     * @param _jsonMessageBody
     * @return the parsed values encapsulated in a map; null if an error has
     * been detected.
     */
    public Message parseItemUpdate(final String _jsonMessageBody, boolean doLogging);

    /**
     * Parse the json Messages.
     *
     * @param _jsonMessageBody
     * @return the parsed values encapsulated in a map; null if an error has
     * been detected.
     */
    public Message parseRecommendationRequest(final String _jsonMessageBody, boolean doLogging);

    /**
     * parse the event notification.
     *
     * @param _jsonMessageBody
     * @return
     */
    public Message parseEventNotification(final String _jsonMessageBody, boolean doLogging);
}
