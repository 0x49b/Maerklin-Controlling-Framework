package de.cortex42.maerklin.framework.Scripting;

import de.cortex42.maerklin.framework.CS2CANCommands;
import de.cortex42.maerklin.framework.FrameworkException;

/**
 * Created by ivo on 21.11.15.
 */
public class ScriptElementSetFunction extends ScriptElement {
    private final int locId;
    private final int function;
    private final int toggle;

    public ScriptElementSetFunction(int locId, int function, int toggle) {
        this.locId = locId;
        this.function = function;
        this.toggle = toggle;
    }

    @Override
    public void executeElement(ScriptContext scriptContext) throws FrameworkException {
        scriptContext.writeCANPacket(
                CS2CANCommands.toggleFunction(locId, function, toggle));
    }
}
