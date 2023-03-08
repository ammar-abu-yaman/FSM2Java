//
// The contents of this file are subject to the Mozilla Public
// License Version 1.1 (the "License"); you may not use this file
// except in compliance with the License. You may obtain a copy
// of the License at http://www.mozilla.org/MPL/
//
// Software distributed under the License is distributed on an
// "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
// implied. See the License for the specific language governing
// rights and limitations under the License.
//
// The Original Code is  State Machine Compiler(SMC).
//
// The Initial Developer of the Original Code is Charles W. Rapp.
// Portions created by Charles W. Rapp are
// Copyright (C) 2021. Charles W. Rapp.
// All Rights Reserved.
//
// Contributor(s):
//
// statemap.java --
//
//  This package defines the AbstractFSMContext class which
/// contains the common debug output data members and methods.
//

package statemap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author <a href="mailto:rapp@acm.org">Charles W. Rapp</a>
 */

public abstract class AbstractFSMContext
    implements Serializable
{
//---------------------------------------------------------------
// Member data.
//

    //-----------------------------------------------------------
    // Constants.
    //

    /**
     * The state change property name.
     */
    public static final String STATE_PROPERTY = "State";

    /**
     * Use this string value when a variable is not set.
     */
    protected static final String NOT_SET = "";

    /**
     * The SMC version for Java serialization purposes.
     */
    private static final long serialVersionUID = 0x070300L;

    //-----------------------------------------------------------
    // Locals.
    //

    /**
     * The FSM name.
     */
    protected transient String mName;

    /**
     * The current transition name. Used for debugging
     * purposes. Will be en empty string when not in
     * transition.
     */
    protected transient String mTransition;

    /**
     * Specifies debug output destination. Defaults to
     * {@link DebugOutputType#NO_DEBUG_OUTPUT}.
     */
    protected transient DebugOutputType mDebugOutputType;

    /**
     * When {@link #mDebugOutputType} is
     * {@link DebugOutputType#PRINT_STREAM} then write debug
     * output to this stream.
     */
    protected transient PrintStream mDebugStream;

    /**
     * When {@link #mDebugOutputType} is
     * {@link DebugOutputType#JAVA_LOGGING} then write debug
     * output to the Java logging subsystem.
     *
     * @see #mDebugLogLevel
     */
    protected transient Logger mDebugLogger;

    /**
     * Write debug output to {@link #mDebugLogger} using this
     * log level. Defaults to {@link Level#OFF}.
     *
     * @see #mDebugLogger
     */
    protected transient Level mDebugLogLevel;

    // Stores the property change listeners here.
    protected transient PropertyChangeSupport mListeners;

//---------------------------------------------------------------
// Member methods.
//

    //-----------------------------------------------------------
    // Constructors.
    //

    /**
     * Creates a finite state machine context with debug output
     * disabled.
     */
    protected AbstractFSMContext()
    {
        mName = "FSMContext";
        mTransition = NOT_SET;
        mDebugOutputType = DebugOutputType.NO_DEBUG_OUTPUT;
        mDebugStream = null;
        mDebugLogger = null;
        mDebugLogLevel = Level.OFF;
        mListeners = null;
    } // end of AbstractFSMContext()

    //
    // end of Constructors.
    //-----------------------------------------------------------

    //-----------------------------------------------------------
    // Abstract Method Declarations.
    //

    /**
     * Returns previous state name. If there is no previous
     * state then returns an empty string.
     * @return previous state name or an empty string.
     */
    protected abstract String previousStateName();

    //
    // end of Abstract Method Declarations.
    //-----------------------------------------------------------

    //-----------------------------------------------------------
    // Get Methods.
    //

    /**
     * Returns the FSM name.
     * @return the FSM name.
     */
    public final String getName()
    {
        return (mName);
    } // end of getName()// end of getName()

    /**
     * If this FSM is in transition, then returns the transition
     * name. If not in transition, then returns an empty string.
     * @return the current transition name.
     */
    public final String getTransition()
    {
        return (mTransition);
    } // end of getTransition()

    /**
     * Returns current debug logging type.
     * @return debug logging type.
     */
    public final DebugOutputType getDebugOutputType()
    {
        return (mDebugOutputType);
    } // end of getDebugOutputType()

    /**
     * When debug is set to {@code true}, the state machine
     * will print messages to the console.
     * @return {@code true} if debug output is generated.
     */
    public final boolean getDebugFlag()
    {
        return(
            mDebugOutputType != DebugOutputType.NO_DEBUG_OUTPUT);
    } // end of getDebugFlag()

    /**
     * Writes the debug output to this stream.
     * @return the debug output stream.
     */
    public final PrintStream getDebugStream()
    {
        return (mDebugStream);
    } // end of getDebugStream()

    /**
     * Return logger used for outputting FSM debug messages. May
     * return {@code null}.
     * @return debug logger.
     */
    public final Logger getDebugLogger()
    {
        return (mDebugLogger);
    } // end of getDebugLogger()

    /**
     * Returns log level used by debug logger to output debug
     * messages.
     * @return debug log level.
     */
    public final Level getDebugLogLevel()
    {
        return (mDebugLogLevel);
    } // end of getDebugLogLevel()

    //
    // end of Get Methods.
    //-----------------------------------------------------------

    //-----------------------------------------------------------
    // Set Methods.
    //

    /**
     * Sets the FSM name.
     * @param name The finite state machine name.
     */
    public final void setName(final String name)
    {
        if (name != null &&
            name.length() > 0 &&
            !name.equals(mName))
        {
            mName = name;
        }
    } // end of setName(String)

    /**
     * Turns debug logging on or off based on whether
     * {@code flag} is {@code true} or {@code false}. If
     * {@code true} then sets the debug output type based on
     * the output settings. If debug logger is not {@code null},
     * then Java logging is used (preferred). Next if debug
     * stream is not {@code null}, then print stream is used.
     * If neither debug logger or stream is set, then defaults
     * to the {@code System.err} print stream.
     * @param flag turn on debug logging if {@code true} and off
     * if {@code false}.
     */
    @SuppressWarnings({"java:S106"})
    public final void setDebugFlag(final boolean flag)
    {
        if (!flag)
        {
            mDebugOutputType = DebugOutputType.NO_DEBUG_OUTPUT;
        }
        else if (
            mDebugOutputType == DebugOutputType.NO_DEBUG_OUTPUT)
        {
            // Set debug logger type based on the current logger
            // settings.
            // Give preference to Java logging.
            if (mDebugLogger != null)
            {
                mDebugOutputType = DebugOutputType.JAVA_LOGGING;
            }
            else if (mDebugStream != null)
            {
                mDebugOutputType = DebugOutputType.PRINT_STREAM;
            }
            // If enable and there is no debug logger or stream
            // set, then use System.err print stream.
            else
            {
                mDebugOutputType = DebugOutputType.PRINT_STREAM;
                mDebugStream = System.err;
            }
        }
    } // end of setDebugFlag(boolean)

    /**
     * Sets the debug output stream to the given value. If
     * {@code stream} is {@code null} <em>and</em> output type is
     * currently print stream, this will turn off debug output.
     * If {@code stream} is not {@code null} then sets the output
     * stream and converts output type to print stream. This
     * means that if debug output is currently using a Java
     * logger this debug logging will stop.
     * @param stream The debug output stream.
     */
    public final void setDebugStream(final PrintStream stream)
    {
        if (stream == null)
        {
            if (mDebugOutputType == DebugOutputType.PRINT_STREAM)
            {
                mDebugOutputType =
                    DebugOutputType.NO_DEBUG_OUTPUT;
            }

            mDebugStream = null;
        }
        else
        {
            mDebugOutputType = DebugOutputType.PRINT_STREAM;
            mDebugStream = stream;
        }
    } // end of setDebugStream(PrintStream)

    /**
     * Sets debug output logger. If {@code logger} is
     * {@code null} <em>and</em> output type is currently Java
     * logging, then turns off all debug logging. If
     * {@code logger} is not {@code null} then sets output
     * logger and converts output type to Java logging. This
     * means that if debug output is currently using a print
     * stream this debug logging will stop.
     * <p>
     * Debug logging level is set separately via
     * {@link #setDebugLoggerLevel(Level)}.
     * </p>
     * @param logger write FSM debug messages to this logger.
     *
     * @see #setDebugLoggerLevel(Level)
     */
    public final void setDebugLogger(final Logger logger)
    {
        if (logger == null)
        {
            if (mDebugOutputType == DebugOutputType.JAVA_LOGGING)
            {
                mDebugOutputType =
                    DebugOutputType.NO_DEBUG_OUTPUT;
            }

            mDebugLogger = null;
        }
        else
        {
            mDebugOutputType = DebugOutputType.JAVA_LOGGING;
            mDebugLogger = logger;
        }
    } // end of setDebugLogger(Logger, Level)

    /**
     * Sets debug logger level. If {@code logLevel} is
     * {@code null} then logger level is set to
     * {@link Level#OFF}.
     * @param logLevel debug logger level.
     *
     * @see #setDebugLogger(Logger)
     */
    public final void setDebugLoggerLevel(final Level logLevel)
    {
        mDebugLogLevel =
            (logLevel == null ? Level.OFF : logLevel);
    } // end of setDebugLoggerLevel(Level)

    /**
     * Fires the state update property change if-and-only-if
     * the property change is supported.
     * @param previousState left this FSM state.
     * @param nextState going to this FSM state.
     */
    protected final void propertyChange(final Object previousState,
                                        final Object nextState)
    {
        if (mListeners != null)
        {
            mListeners.firePropertyChange(
                STATE_PROPERTY, previousState, nextState);
        }
    } // end of propertyChange(Object, Object)

    //
    // end of Set Methods.
    //-----------------------------------------------------------

    // The following methods allow listeners to watch this
    // finite state machine for state changes.
    // Note: if a transition does not cause a state change,
    // then no state change event is fired.

    /**
     * Turns state property change updates on or off based on
     * whether {@code flag} is {@code true} (on) or {@code false}
     * (off). If turned off after being enabled and property
     * listeners added, then those listeners are lost. If
     * state property update is re-enabled, then listeners must
     * be re-added in order to receive updates.
     * @param flag specifies whether state property change
     * updates are on or off.
     * @throws IllegalStateException
     * if {@code flag} is {@code true} and state property updates
     * are already enabled.
     *
     * @see #addStateChangeListener(PropertyChangeListener)
     * @see #removeStateChangeListener(PropertyChangeListener)
     */
    public final void setPropertyChangeUpdate(final boolean flag)
    {
        if (!flag)
        {
            mListeners = null;
        }
        else if (mListeners != null)
        {
            throw (
                new IllegalStateException(
                    "state property change update already enabled"));
        }
        else
        {

            mListeners = new PropertyChangeSupport(this);
        }
    } // end of setPropertyChangeUpdate(boolean)

    /**
     * Adds a PropertyChangeListener to the listener list. The
     * listener is registered for state property changes only.
     * The same listener may be added more than once. For each
     * state change, the listener will be invoked the number of
     * times it was added. If {@code listener} is {@code null},
     * no exception is thrown and no action is taken.
     * @param l The PropertyChangeListener to be added.
     * @throws IllegalStateException
     * if state property change update has not been enabled.
     *
     * @see #setPropertyChangeUpdate(boolean)
     * @see #removeStateChangeListener(PropertyChangeListener)
     */
    public final void addStateChangeListener(final PropertyChangeListener l)
    {
        if (mListeners == null)
        {
            throw (
                new IllegalStateException(
                    "State property change update not enabled"));
        }

        mListeners.addPropertyChangeListener(STATE_PROPERTY, l);
    } // end of addStateChangeListener(PropertyChangeListener)

    /**
     * Removes a PropertyChangeListener for the state change
     * property. If {@code listener} was added more than once
     * to the same event source, it will be notified one less
     * time after being removed. If {@code listener} is
     * {@code null} or was never added, no exception is thrown
     * and no action is taken. If state property updates is
     * disabled then does nothing.
     * @param l The PropertyChangeListener to be removed.
     *
     * @see #addStateChangeListener(PropertyChangeListener)
     * @see #setPropertyChangeUpdate(boolean)
     */
    public final void removeStateChangeListener(final PropertyChangeListener l)
    {
        if (mListeners != null)
        {
            mListeners.removePropertyChangeListener(
                STATE_PROPERTY, l);
        }
    } // end of removeStateChangeListener(PropertyChangeListener)

    /**
     * Writes FSM debug message to the appropriate destination
     * based on the current debug output settings.
     * @param message FSM debug message.
     */
    protected final void debugOutput(final String message)
    {
        switch (mDebugOutputType)
        {
            case JAVA_LOGGING:
                if (mDebugLogger.isLoggable(mDebugLogLevel))
                {
                    mDebugLogger.log(mDebugLogLevel, message);
                }
                break;

            case PRINT_STREAM:
                mDebugStream.println(message);
                break;

            default:
                // no-op.
        }
    } // end of debugOutput(String)

    /**
     * Write FSM debug exception message and stack trace to the
     * appropriate destination based on the current debug output
     * settings.
     * @param tex output this Java exception.
     */
    protected final void debugOutput(final Throwable tex)
    {
        final String message =
            String.format(
                "Exception in %s.%s transition.",
                previousStateName(),
                mTransition);

        switch (mDebugOutputType)
        {
            case JAVA_LOGGING:
                if (mDebugLogger.isLoggable(mDebugLogLevel))
                {
                    mDebugLogger.log(
                        mDebugLogLevel, message, tex);
                }
                break;

            case PRINT_STREAM:
                mDebugStream.println(message);
                tex.printStackTrace(mDebugStream);
                break;

            default:
                // no-op.
        }
    } // end of debugOutput(Throwable)
} // end of class AbstractFSMContext
