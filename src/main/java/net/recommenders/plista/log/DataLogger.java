package net.recommenders.plista.log;

import org.apache.log4j.Logger;

/**
 *
 * @author alejandr
 */
public class DataLogger {

    private Logger logger;

    private DataLogger(Class c) {
        logger = Logger.getLogger(c);
    }

    public static DataLogger getLogger(Class c) {
        return new DataLogger(c);
    }

    public void info(String m) {
        logger.info(m);
    }

    public void warn(String m) {
        logger.warn(m);
    }

    public void debug(String m) {
        logger.debug(m);
    }

    public void error(String m) {
        logger.error(m);
    }

    public void fatal(String m) {
        logger.fatal(m);
    }

    public void data(String m) {
        logger.log(DataLevel.DATA, m);
    }
}
