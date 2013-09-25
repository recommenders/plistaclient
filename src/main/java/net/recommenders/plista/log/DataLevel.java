package net.recommenders.plista.log;

import org.apache.log4j.Level;

/**
 *
 * @author alejandr
 */
public class DataLevel extends Level {

    public static final int DATA_INT = DEBUG_INT - 10;
    public static final Level DATA = new DataLevel(DATA_INT, "DATALEVEL", 3);

    public DataLevel(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }

    public static Level toLevel(String sArg) {
        if (sArg != null && sArg.toUpperCase().equals("DATALEVEL")) {
            return DATA;
        }
        return (Level) toLevel(sArg, Level.DEBUG);
    }

    public static Level toLevel(int val) {
        if (val == DATA_INT) {
            return DATA;
        }
        return (Level) toLevel(val, Level.DEBUG);
    }

    public static Level toLevel(int val, Level defaultLevel) {
        if (val == DATA_INT) {
            return DATA;
        }
        return Level.toLevel(val, defaultLevel);
    }

    public static Level toLevel(String sArg, Level defaultLevel) {
        if (sArg != null && sArg.toUpperCase().equals("DATALEVEL")) {
            return DATA;
        }
        return Level.toLevel(sArg, defaultLevel);
    }
}
