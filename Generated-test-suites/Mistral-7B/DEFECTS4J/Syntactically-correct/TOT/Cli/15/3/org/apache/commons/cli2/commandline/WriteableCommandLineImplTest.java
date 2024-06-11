package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setUp() {
        option = new Option("a", "append", false, "append argument");
    }

    @Test
    public void testAddValue() {
        commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueDuplicate() {
        commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value1");
    }

    @Test
    public void testAddSwitch() {
        commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        commandLine = new WriteableCommandLineImpl(null, Arrays.asList("a"));
        commandLine.addOption(option);

        boolean hasOption = commandLine.hasOption(option);
        assertEquals(true, hasOption);
    }

    @Test
    public void testGetOption() {
        commandLine = new WriteableCommandLineImpl(null, Arrays.asList("a"));
        commandLine.addOption(option);

        Option actualOption = commandLine.getOption("a");
        assertEquals(option, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionInvalidTrigger() {
        commandLine = new WriteableCommandLineImpl(null, Arrays.asList("a"));
        commandLine.addOption(option);

        Option actualOption = commandLine.getOption("b");
    }

    @Test
    public void testToString() {
        commandLine = new WriteableCommandLineImpl(null, Arrays.asList("a", "1", "b", "2"));
        commandLine.addOption(option);

        String actual = commandLine.toString();
        String expected = "a 1 b 2";
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidArgument() throws ParseException {
        commandLine = new WriteableCommandLineImpl(null, Arrays.asList("a", "invalid-argument"));
        commandLine.addOption(option);

        commandLine.parse();
    }

    @Test(expected = ParseException.class)
    public void testParseMissingArgument() throws ParseException {
        commandLine = new WriteableCommandLineImpl(null, Arrays.asList("a"));
        commandLine.addOption(option);

        commandLine.parse();
    }

    @Test
    public void testParseValidArguments() throws ParseException {
        commandLine = new WriteableCommandLineImpl(null, Arrays.asList("a", "value1", "b", "value2"));
        commandLine.addOption(option);

        Map<String, Object> parsed = commandLine.parse();
        Map<String, Object> expected = new HashMap<>();
        expected.put(option.getLongOpt(), Arrays.asList("value1", "value2"));

        assertEquals(expected, parsed);
    }
}