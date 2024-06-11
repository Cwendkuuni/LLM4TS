package org.junit.runner;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

public class OptionTestRunner extends BlockJUnit4ClassRunner {

    public OptionTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
}