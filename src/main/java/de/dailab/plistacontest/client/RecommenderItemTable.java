/*
Copyright (c) 2013, TU Berlin
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

package de.dailab.plistacontest.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.SetBasedFieldSelector;
/*
* Class to represent the table of items available to be recommended
 */
public class RecommenderItemTable {


	private DirtyRingBuffer<String, Long> table = new DirtyRingBuffer<String, Long>(100);

	/**
	 * Handle the item update, itemID is ignored
	 * @param _item
	 * @return
	 */
	public boolean handleItemUpdate(final RecommenderItem _item) {
		
		// check the item
		if (_item == null || _item.getItemID() == null || _item.getItemID() == 0L || _item.getDomainID() == null) {
			return false;
		}
		
		// add the item to the table
		table.addValueByKey(_item.getDomainID() + "", _item.getItemID());
		return true;
	}

	/**
	 * Return something from the buffer.
	 * @param _currentRequest
	 * @return
	 */
	public List<Long> getLastItems(final RecommenderItem _currentRequest) {

		Integer numberOfRequestedResults = _currentRequest.getNumberOfRequestedResults();
		Long itemID = _currentRequest.getItemID();
		Long domainID = _currentRequest.getDomainID();

		
		// handle invalid values
		if (numberOfRequestedResults == null || numberOfRequestedResults.intValue() < 0 || numberOfRequestedResults.intValue() > 10 || domainID == null) {
			return new ArrayList<Long>(0);
		}
		
		Set<Long> blackListedIDs = new HashSet<Long>();
		blackListedIDs.add(0L);
		blackListedIDs.add(itemID);
		Set<Long> result = table.getValuesByKey(domainID+"", numberOfRequestedResults.intValue(), blackListedIDs);
		
		List<Long> returnResult = new ArrayList<Long>();
		returnResult.addAll(result);
		return returnResult;
	}
}
