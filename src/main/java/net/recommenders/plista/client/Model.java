package net.recommenders.plista.client;

/**
 *
 * @author alejandr
 */
public interface Model {

    /**
     * Impression Information from the Plista server is sent to this method
     *
     * @param _impression
     */
    public void impression(final Message _impression);

    /**
     * Impression Information from the Plista server is sent to this method
     *
     * @param _click
     */
    public void click(final Message _click);

    /**
     * Impression Information from the Plista server is sent to this method
     *
     * @param _update
     */
    public void update(final Message _update);
}
