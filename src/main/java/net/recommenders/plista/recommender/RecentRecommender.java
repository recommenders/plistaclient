/*
 Copyright (c) 2013, TU Berlin.
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
package net.recommenders.plista.recommender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import net.recommenders.plista.client.Message;


/*
 * Class to represent the table of items available to be recommended
 */
public class RecentRecommender implements Recommender {

    private RecentRingBuffer<Long, Long> table = new RecentRingBuffer<Long, Long>(100);

    /**
     * Handle the item update, itemID is ignored
     *
     * @param _item
     * @return
     */
    public boolean handleItemUpdate(final Message _item) {

        // check the item
        if (_item == null || _item.getItemID() == null || _item.getItemID() == 0L || _item.getDomainID() == null) {
            return false;
        }

        // add the item to the table
        table.addValueByKey(_item.getDomainID(), _item.getItemID());
        return true;
    }

    /**
     * Return something from the buffer.
     *
     * @param _currentRequest
     * @return
     */
    @Override
    public List<Long> recommend(final Message _currentRequest, Integer numberOfRequestedResults) {
        Long itemID = _currentRequest.getItemID();
        Long domainID = _currentRequest.getDomainID();


        // handle invalid values
        if (numberOfRequestedResults == null || domainID == null) {
            return new ArrayList<Long>(0);
        }
        int limit = numberOfRequestedResults.intValue();
        if (limit < 0 || limit > 100) {
            return new ArrayList<Long>(0);
        }

        Set<Long> blackListedIDs = new HashSet<Long>();
        blackListedIDs.add(0L);
        blackListedIDs.add(itemID);
        Set<Long> result = table.getValuesByKey(domainID, limit, blackListedIDs);

        return new ArrayList<Long>(result);
    }

    @Override
    public void init() {
    }

    @Override
    public void impression(Message _impression) {
        update(_impression);
    }

    @Override
    public void click(Message _click) {
    }

    @Override
    public void update(Message _update) {
        handleItemUpdate(_update);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
