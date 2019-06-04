package com.kylegrund.iridiumat;

import com.kylegrund.iridiumat.atcommands.DisplayRegisters;
import com.kylegrund.iridiumat.atcommands.ModelIdentification;

import java.io.IOException;
import java.util.*;

public class Motorola9575 extends SubscriberUnit {
    /**
     * The AT commands available for this ISU.
     */
    private final Map<String, AtCommand> commands;

    /**
     * The function which sends a line of text to the remote ISU.
     */
    private final CheckedConsumer<String, IOException> sendLine;

    /**
     * The function which receives a line of text from the ISU.
     */
    private final CheckedSupplier<String, IOException> receiveLine;

    /**
     * Initializes a new instance of the Motorola9575 class.
     *
     * @param sendLine    A function which sends the provided String to the associated ISU.
     * @param receiveLine A function which returns a String read from the associated ISU.
     */
    Motorola9575(CheckedConsumer<String, IOException> sendLine, CheckedSupplier<String, IOException> receiveLine) {
        this.sendLine = sendLine;
        this.receiveLine = receiveLine;

        Map<String, AtCommand> commands = new HashMap<>();

        commands.put(DisplayRegisters.class.getName(), new DisplayRegisters(this::execute));
        commands.put(ModelIdentification.class.getName(), new ModelIdentification(this::execute));

        this.commands = Collections.unmodifiableMap(commands);
    }

    @Override
    public Map<String, AtCommand> getCommands() {
        return this.commands;
    }

    /**
     * Function providing the ability to synchronously access communications when executing commands.
     * @param func The function which actually executes the command.
     * @return The result of executing the command.
     * @throws IOException If an IOException occurs while executing the command.
     */
    private synchronized Map<String, String> execute(CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException> func) throws IOException {
        return func.accept(this.sendLine, this.receiveLine);
    }
}
