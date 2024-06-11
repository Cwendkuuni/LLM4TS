
package org.junit.runner;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.RunnerBuilder;

public class ExtendedMessageFormatTest {

    public static void main(String[] args) throws Exception {
        RunnerBuilder builder = new RunnerBuilder();
        Runner runner = builder.forClass(ExtendedMessageFormatTest.class).buildRunner();
        runner.run();
    }

    @RunWith(BlockJUnit4ClassRunner.class)
    public static class ExtendedMessageFormatTestSuite {
        // All tests go here
    }
}