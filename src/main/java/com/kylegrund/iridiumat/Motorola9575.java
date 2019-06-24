package com.kylegrund.iridiumat;

import com.kylegrund.iridiumat.atcommands.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
     * The function to call to dispose underlying unmanaged resources.
     */
    private final Runnable disposeMethod;

    /**
     * Gets a boolean value indicating whether ISU local echo is enabled.
     */
    private boolean echoEnabled = true;

    /**
     * Initializes a new instance of the Motorola9575 class.
     *
     * @param sendLine    A function which sends the provided String to the associated ISU.
     * @param receiveLine A function which returns a String read from the associated ISU.
     * @param disposeMethod The method to call to dispose unmanaged resources.
     */
    Motorola9575(CheckedConsumer<String, IOException> sendLine, CheckedSupplier<String, IOException> receiveLine, Runnable disposeMethod) throws Exception {
        this.sendLine = sendLine;
        this.receiveLine = receiveLine;
        this.disposeMethod = disposeMethod;

        Map<String, AtCommand> commands = new HashMap<>();

        // add all commands the 9575 radio supports
        CheckedConsumer<Class, Exception> addCommand = (cmd) -> commands.put(
                cmd.getName(),
                (AtCommand)cmd.getConstructor(new Class[] { CheckedFunction.class, Supplier.class }).newInstance((CheckedFunction<CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException>, Map<String, String>, IOException>)this::execute, (Supplier<Boolean>)this::getEchoEnabled));

        // build echo command specially with callback to update echo state in this class so other commands know whether
        // to expect a local echo
        commands.put(Echo.class.getName(), new Echo(this::execute, this::getEchoEnabled, (ena) -> this.echoEnabled = ena));

        // add all other commands compatible with this handset
        addCommand.accept(BatteryCharge.class);
        addCommand.accept(DisplayRegisters.class);
        addCommand.accept(FactoryReset.class);
        addCommand.accept(ModelIdentification.class);
        // addCommand.accept(PowerPhone.class);
        // addCommand.accept(RadioActivity.class);
        addCommand.accept(Reboot.class);

        this.commands = Collections.unmodifiableMap(commands);
    }

    @Override
    public Map<String, AtCommand> getCommands() {
        return this.commands;
    }

    @Override
    public void close() {
        // run the dispose method to clean up unmanaged resources
        this.disposeMethod.run();
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

    /**
     * Gets a boolean value indicating whether ISU local echo is enabled.
     * @return A boolean value indicating whether ISU local echo is enabled.
     */
    private Boolean getEchoEnabled() {
        return this.echoEnabled;
    }
}
