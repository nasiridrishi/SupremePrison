package net.primegames.supremeprison.exception;

import net.primegames.supremeprison.SupremePrisonModule;

public class ModuleNotEnabledException extends Exception {

    public ModuleNotEnabledException(SupremePrisonModule module) {
        super("Module " + module.getName() + " is not enabled");
    }
}
