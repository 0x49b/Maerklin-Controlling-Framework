package de.cortex42.maerklin.framework.Scripting;

import de.cortex42.maerklin.framework.FrameworkException;

import java.util.ArrayList;

/**
 * Created by ivo on 20.11.15.
 */
public class ScriptElementParallel extends ScriptElement {
    private final ArrayList<ScriptElementConditionChecker> scriptElementConditionCheckers;

    public ScriptElementParallel(ArrayList<ScriptElementConditionChecker> scriptElementConditionCheckers) {
        this.scriptElementConditionCheckers = scriptElementConditionCheckers;
    }

    @Override
    public void executeElement(final ScriptContext scriptContext) throws FrameworkException {
        final int scriptConditionCheckerCount = scriptElementConditionCheckers.size();
        final FrameworkException[] frameworkExceptions = new FrameworkException[scriptConditionCheckerCount];
        final Thread[] threads = new Thread[scriptConditionCheckerCount];

        for (int i = 0; i < threads.length; i++) {
            final int finalI = i;

            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        scriptElementConditionCheckers.get(finalI).execute(scriptContext);
                    } catch (FrameworkException e) {
                        frameworkExceptions[finalI] = e;
                    }
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join(); //wait for all threads to finish
            } catch (InterruptedException e) {
                throw new FrameworkException(e);
            }
        }

        //check if any thread threw an exception
        for (int i = 0; i < frameworkExceptions.length; i++) {
            if (frameworkExceptions[i] != null) {
                throw new FrameworkException(frameworkExceptions);
            }
        }
    }
}
