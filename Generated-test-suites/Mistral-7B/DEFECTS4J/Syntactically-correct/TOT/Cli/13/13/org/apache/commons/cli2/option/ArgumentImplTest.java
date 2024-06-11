package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ArgumentImplTest {

    private Argument arg;
    private WriteableCommandLine cmdLine;

    @Before
    public void setup() {
        arg = new ArgumentImpl("arg", "An argument test", 1, 1, ' ', ' ', new SimpleValidator(), null, null, 0);
        cmdLine = new CommandLine();
    }

    @Test
    public void testProcessValues() {
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        cmdLine.parseArgument(arg, values.toArray(new String[values.size()]));

        assertEquals(2, cmdLine.getValues(arg).size());
        assertEquals("value1", cmdLine.getValues(arg).get(0));
        assertEquals("value2", cmdLine.getValues(arg).get(1));
    }

    @Test
    public void testProcessValuesWithConsumeRemaining() {
        List values = new ArrayList();
        values.add("--");
        values.add("value1");
        values.add("value2");

        cmdLine.parseArgument(arg, values.toArray(new String[values.size()]));

        assertEquals(2, cmdLine.getValues(arg).size());
        assertEquals("value1", cmdLine.getValues(arg).get(0));
        assertEquals("value2", cmdLine.getValues(arg).get(1));
    }

    @Test
    public void testProcessValuesWithMinimum() {
        List values = new ArrayList();
        cmdLine.parseArgument(arg, values.toArray(new String[values.size()]));

        assertNull(cmdLine.getValues(arg));

        values.add("value1");
        cmdLine.parseArgument(arg, values.toArray(new String[values.size()]));

        assertEquals(1, cmdLine.getValues(arg).size());
        assertEquals("value1", cmdLine.getValues(arg).get(0));
    }

    @Test
    public void testProcessValuesWithMaximum() {
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        values.add("value3");

        cmdLine.parseArgument(arg, values.toArray(new String[values.size()]));

        assertEquals(2, cmdLine.getValues(arg).size());
        assertEquals("value1", cmdLine.getValues(arg).get(0));
        assertEquals("value2", cmdLine.getValues(arg).get(1));

        assertThrows(OptionException.class, () -> cmdLine.parseArgument(arg, "value3".toCharArray()));
    }

    @Test
    public void testProcessValuesWithInitialSeparator() {
        List values = new ArrayList();
        values.add("initial-value");
        values.add("value1");
        values.add("value2");

        cmdLine.parseArgument(arg, values.toArray(new String[values.size()]));

        assertEquals(3, cmdLine.getValues(arg).size());
        assertEquals("initial-value", cmdLine.getValues(arg).get(0));
        assertEquals("value1", cmdLine.getValues(arg).get(1));
        assertEquals("value2", cmdLine.getValues(arg).get(2));
    }

    @Test
    public void testProcessValuesWithSubsequentSeparator() {
        List values = new ArrayList();
        values.add("initial-value");
        values.add("value1-value2");
        values.add("value3");

        cmdLine.parseArgument(arg, values.toArray(new String[values.size()]));

        assertEquals(3, cmdLine.getValues(arg).size());
        assertEquals("initial-value", cmdLine.getValues(arg).get(0));
        assertEquals("value1-value2", cmdLine.getValues(arg).get(1));
        assertEquals("value3", cmdLine.getValues(arg).get(2));
    }

    @Test
    public void testProcessValuesWithValidator() {
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        values.add("invalid-value");

        arg.setValidator(new StringValidator("value"));

        cmdLine.parseArgument(arg, values.toArray(new String[values.size()]));

        assertThrows(InvalidArgumentException.class, () -> cmdLine.parseArgument(arg, "value1".toCharArray()));
        assertThrows(InvalidArgumentException.class, () -> cmdLine.parseArgument(arg, "value2".toCharArray()));
        assertThrows(OptionException.class, () -> cmdLine.parseArgument(arg, "invalid-value".toCharArray()));
    }

    @Test
    public void testProcessValuesWithDefaultValues() {
        cmdLine.setDefaultValues(arg, new String[]{"default-value1", "default-value2"});

        cmdLine.parseNoArgs();

        assertEquals(2, cmdLine.getValues(arg).size());
        assertEquals("default-value1", cmdLine.getValues(arg).get(0));
        assertEquals("default-value2", cmdLine.getValues(arg).get(1));
    }

    @Test
    public void testCanProcess() {
        assertTrue(arg.canProcess(cmdLine, "value1"));
        assertTrue(arg.canProcess(cmdLine, "value1 value2"));
        assertTrue(arg.canProcess(cmdLine, "--"));
        assertFalse(arg.canProcess(cmdLine, "--help"));
    }

    @Test
    public void testValidate() {
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");

        assertNull(cmdLine.getValues(arg));

        cmdLine.parseArgument(arg, values.toArray(new String[values.size()]));

        arg.validate(cmdLine);

        assertEquals(2, cmdLine.getValues(arg).size());
        assertEquals("value1", cmdLine.getValues(arg).get(0));
        assertEquals("value2", cmdLine.getValues(arg).get(1));

        List invalidValues = new ArrayList();
        invalidValues.add("invalid-value1");
        invalidValues.add("invalid-value2");

        assertThrows(InvalidArgumentException.class, () -> arg.validate(cmdLine, invalidValues.toArray(new String[invalidValues.size()])));
    }

    @Test
    public void testAppendUsage() {
        StringBuilder usageStringBuilder = new StringBuilder();
        arg.appendUsage(usageStringBuilder, null, null);

        String usageString = usageStringBuilder.toString();

        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Usage:", arg, true, null, usageString, null);

        String expectedUsage = "arg:<arg>       An argument test\n";
        String printedUsage = helpFormatter.getHelp();

        assertEquals(expectedUsage, printedUsage);
    }
}