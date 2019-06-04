package com.kylegrund.iridiumat;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Abstract interface representing an Iridium ISU AT command.
 */
public abstract class AtCommand {
    /**
     * Sends the provided String to the associated ISU.
     */
    private final CheckedFunction<CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException>, Map<String, String>, IOException> commEndpoint;

    /**
     * Initializes a new instance of the AtCommand class.
     * @param commEndpoint The interface used to communicate with the ISU.
     */
    protected AtCommand(CheckedFunction<CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException>, Map<String, String>, IOException> commEndpoint){
        this.commEndpoint = commEndpoint;
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
     * When implemented in a sub-class, returns a DoubleCheckedFunction which executes the command.
     * @param parameters The parameters for executing the call.
     * @return A DoubleCheckedFunction which executes the command.
     * @throws IOException If there is an exception executing command.
     */
    protected abstract CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException> execute(Map<String, String> parameters) throws IOException;
}
