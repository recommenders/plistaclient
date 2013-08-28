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
    public List<Long> recommend(final Message _input);

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
