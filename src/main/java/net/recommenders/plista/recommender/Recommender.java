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
package net.recommenders.plista.recommender;

import net.recommenders.plista.client.Message;

import java.util.List;
import java.util.Properties;

/**
 * @author till, alan, alejandro
 *
 */
public interface Recommender {

    /**
     * This method is called by the contest handler at startup
     */
    public void init();

    /**
     * Recommend method called by {@link net.recommenders.plista.client.ChallengeHandler}
     *
     * @param _input
     * @return
     */
    public List<Long> recommend(final Message _input, Integer limit);

    /**
     * Impression Information from the Plista server is send to this method
     *
     * @param _impression
     */
    public void impression(final Message _impression);

    /**
     * Impression Information from the Plista server is send to this method
     *
     * @param _click
     */
    public void click(final Message _click);

    /**
     * Impression Information from the Plista server is send to this method
     *
     * @param _update
     */
    public void update(final Message _update);



    public void setProperties(Properties properties);

}
