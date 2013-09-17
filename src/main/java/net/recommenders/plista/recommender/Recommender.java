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
import net.recommenders.plista.client.Model;

/**
 * @author till, alan, alejandro
 *
 */
public interface Recommender extends Model {

    /**
     * This method is called by the contest handler at startup
     */
    public void init();

    /**
     * Recommend method called by
     * {@link net.recommenders.plista.client.ChallengeHandler}
     *
     * @param _input
     * @return
     */
    public List<Long> recommend(final Message _input, Integer limit);

    public void setProperties(Properties properties);
}
