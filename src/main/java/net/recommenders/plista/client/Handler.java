package net.recommenders.plista.client;

import net.recommenders.plista.recommender.Recommender;

/**
 *
 * @author alejandr
 */
public interface Handler {

    public String handleMessage(final String messageType, final String messageBody, final Recommender rec, final boolean doLogging);
}
