package com.kylegrund.iridiumat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Abstract interface representing an Iridium ISU AT command.
 */
public abstract class AtCommand {
    /**
     * The key used by the AtCommand implementations to determine if local echo is enabled.
     */
    protected final String ECHO_ENABLED_KEY = "__echoEnabled";

    /**
     * Sends the provided String to the associated ISU.
     */
    private final CheckedFunction<CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException>, Map<String, String>, IOException> commEndpoint;

    /**
     * Gets a value indicating whether the ISU's local echo is enabled.
     */
    private final Supplier<Boolean> getEchoEnabled;

    /**
     * Initializes a new instance of the AtCommand class.
     * @param commEndpoint The interface used to communicate with the ISU.
     */
    protected AtCommand(CheckedFunction<CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException>, Map<String, String>, IOException> commEndpoint, Supplier<Boolean> getEchoEnabled){
        this.commEndpoint = commEndpoint;
        this.getEchoEnabled = getEchoEnabled;
    }

    /**
     * Gets a mapping of String names to default String values when executing this command.
     * @return A mapping of String names to default String values when executing this command.
     */
    public abstract Map<String, String> getParameters();

    /**
     * Parses a response into a mapping of String names to String values.
     * @param parameters A mapping of String names to String values representing the parameters to use when executing
     *                  this command.
     * @return A mapping of String names to String values representing the results of the command.
     */
    public Map<String, String> executeCommand(Map<String, String> parameters) throws IOException {
        return commEndpoint.accept(this.execute(parameters));
    }

    /**
     * Returns a boolean value indicating whether to expect the ISU to echo commands back to DTE.
     * @return  A boolean value indicating whether to expect the ISU to echo commands back to DTE.
     */
    protected Boolean getEchoEnabled() {
        return this.getEchoEnabled.get();
    }

    /**
     * When implemented in a sub-class, returns a DoubleCheckedFunction which executes the command.
     * @param parameters The parameters for executing the call.
     * @return A DoubleCheckedFunction which executes the command.
     * @throws IOException If there is an exception executing command.
     */
    protected abstract CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException> execute(Map<String, String> parameters) throws IOException;
}
