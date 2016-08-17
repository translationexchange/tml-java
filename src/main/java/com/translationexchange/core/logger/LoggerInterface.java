package com.translationexchange.core.logger;

/**
 * Created by ababenko on 8/17/2016.
 */
public interface LoggerInterface {
    /**
     * <p>logException.</p>
     *
     * @param message a {@link String} object.
     * @param ex      a {@link Exception} object.
     */
    void logException(String message, Exception ex);

    /**
     * <p>logException.</p>
     *
     * @param ex a {@link Exception} object.
     */
    void logException(Exception ex);

    /**
     * <p>debug.</p>
     *
     * @param message a {@link Object} object.
     */
    void debug(Object message);

    /**
     * <p>info.</p>
     *
     * @param message a {@link Object} object.
     */
    void info(Object message);

    /**
     * <p>warn.</p>
     *
     * @param message a {@link Object} object.
     */
    void warn(Object message);

    /**
     * <p>error.</p>
     *
     * @param message a {@link Object} object.
     */
    void error(Object message);

    void error(String tag, String message);

    void error(String tag, String message, Throwable e);

    void debug(String tag, String message);

    void info(String tag, String message);

    void warn(String tag, String message);
}
