
package org.junit.runner;

import org.junit.runner.RunnerException;
import org.junit.runner.notification.RunNotifier;

public class Runner implements Runner {

    @Override
    public Description getDescription() {
        return Description.createTestSuiteDescription("CaverphoneTest");
    }

    @Override
    public void run(RunNotifier notifier) {
        JUnitCore core = new JUnitCore();
        Result result = core.run(CaverphoneTest.class);

        if (result.wasSuccessful()) {
            notifier.addSuccess(result);
        } else {
            notifier.addFailure(result.getFailures());
            notifier.addError(result.getErrors());
        }
    }
}
