package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.apache.commons.cli2.validation.MockValidator;
import org.apache.commons.cli2.writeable.MockWriteableCommandLine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test for ArgumentImpl class.
 */
public class ArgumentImplTest {

    @org.junit.Before
    public void setUp() {
        TestUtils.initMocks();
    }

    @org.junit.After
    public void tearDown() {
        TestUtils.cleanupMocks();
    }

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testArgumentImplConstruction() {
        Argument argument = new ArgumentImpl("argName", "description", 1, 1,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                new MockValidator(), DEFAULT_CONSUME_REMAINING,
                Arrays.asList("defaultValue1"), 0);

        assertEquals("argName", argument.getPreferredName());
        assertEquals("description", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
        assertEquals(false, argument.isSubsequentSplit());
        assertEquals(new MockValidator(), argument.getValidator());
        assertEquals(DEFAULT_CONSUME_REMAINING, argument.getConsumeRemaining());
        assertEquals(Arrays.asList("defaultValue1"), argument.getDefaultValues());
    }

    /**
     * Test processing values.
     */
    @org.junit.Test
    public void testProcessValues() {
        Argument argument = new ArgumentImpl("argName", "description", 1, 1,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                new MockValidator(), DEFAULT_CONSUME_REMAINING,
                Collections.emptyList(), 0);

        CommandLine commandLine = new MockWriteableCommandLine();
        List<String> arguments = Arrays.asList("arg1", "arg2", "--consume");

        argument.processValues(commandLine, arguments.listIterator(), argument);

        assertEquals(2, commandLine.getValues(argument).size());
        assertEquals("arg1", commandLine.getValues(argument).get(0));
        assertEquals("arg2", commandLine.getValues(argument).get(1));
    }

    /**
     * Test can process option.
     */
    @org.junit.Test
    public void testCanProcessOption() {
        Argument argument = new ArgumentImpl("argName", "description", 1, 1,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                new MockValidator(), DEFAULT_CONSUME_REMAINING,
                Collections.emptyList(), 0);

        assertTrue(argument.canProcess(new MockWriteableCommandLine(), "arg1"));
    }

    /**
     * Test validating values.
     */
    @org.junit.Test
    public void testValidate() {
        Argument argument = new ArgumentImpl("argName", "description", 1, 1,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                new MockValidator(), DEFAULT_CONSUME_REMAINING,
                Collections.emptyList(), 0);

        CommandLine commandLine = new MockWriteableCommandLine();
        commandLine.addValue(argument, "arg1");
        commandLine.addValue(argument, "arg2");
        commandLine.addValue(argument, "arg3");

        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            fail("Should not throw an exception.");
        }
    }

    /**
     * Test validating values with invalid argument.
     */
    @org.junit.Test
    public void testValidateWithInvalidArgument() {
        Argument argument = new ArgumentImpl("argName", "description", 1, 1,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                new MockValidator(), DEFAULT_CONSUME_REMAINING,
                Collections.emptyList(), 0);

        CommandLine commandLine = new MockWriteableCommandLine();
        commandLine.addValue(argument, "arg1");
        commandLine.addValue(argument, "arg2");
        commandLine.addValue(argument, "arg3");
        commandLine.addValue(argument, "invalidArg");

        try {
            argument.validate(commandLine);
            fail("Should throw an exception.");
        } catch (OptionException e) {
            assertEquals("argName:arg3 is an invalid value", e.getMessage());
        }
    }

    /**
     * Test processing with consume remaining.
     */
    @org.junit.Test
    public void testProcessWithConsumeRemaining() {
        Argument argument = new ArgumentImpl("argName", "description", 1, 1,
                DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                new MockValidator(), DEFAULT_CONSUME_REMAINING,
                Collections.emptyList(), 0);

        CommandLine commandLine = new MockWriteableCommandLine();
        List<String> arguments = Arrays.asList("arg1", "--consume", "arg3");

        argument.process(commandLine, arguments.listIterator());

        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("arg1", commandLine.getValues(argument).get(0));

        // Consumed arguments should be empty.
        List<String> consumedArguments = commandLine.getValues(null);
        assertTrue(consumedArguments.isEmpty());
    }

    /**
     * Test help lines.
     */
    @org.junit.Test
    public void testHelpLines() {
        Argument argument = OptionBuilder.argument("argName")
                .description("description")
                .required()
                .build();

        HelpFormatter formatter = new HelpFormatter();
        List<HelpLine> helpLines = argument.helpLines(0, null, null);

        String expectedHelp = "argName:\n" +
                "  description\n" +
                "  (required)\n";

        for (HelpLine helpLine : helpLines) {
            assertEquals(expectedHelp, helpLine.getHelp());
        }
    }

}

class MockValidator implements Validator {
    @Override
    public void validate(List values) throws InvalidArgumentException {
        // Do nothing.
    }
}