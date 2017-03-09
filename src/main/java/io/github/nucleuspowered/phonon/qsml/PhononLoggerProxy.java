package io.github.nucleuspowered.phonon.qsml;

import org.slf4j.Logger;
import uk.co.drnaylor.quickstart.LoggerProxy;

public class PhononLoggerProxy implements LoggerProxy {

    private final Logger logger;

    public PhononLoggerProxy(Logger logger) {
        this.logger = logger;
    }

    @Override public void info(String message) {
        this.logger.info(message);
    }

    @Override public void warn(String message) {
        this.logger.warn(message);
    }

    @Override public void error(String message) {
        this.logger.error(message);
    }
}
