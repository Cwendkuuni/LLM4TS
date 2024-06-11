package org.junit.runner;

import org.junit.runner.Runner;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

public class TestRunner extends Runner {

    @Override
    public Description getDescription() {
        return new Description("MultivariateNormalMixtureExpectationMaximizationTest");
    }

    @Override
    public void run(RunNotifier notifier) {
        JUnitCore junitCore = new JUnitCore();
        Result result = junitCore.run(MultivariateNormalMixtureExpectationMaximizationTest.class);
        notifier.fireTestFinished(result.getRunnable(), result.getTest());
    }
}