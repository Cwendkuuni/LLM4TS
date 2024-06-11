package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;

    @Before
    public void setup() {
        final List<Option> optionsList = new ArrayList<>();

        options = new Options();

        // create a group with 2 options and 1 anonymous argument
        final Group group = new GroupImpl(optionsList, "myGroup", "My Group", 1, 1) {
            @Override
            public void appendUsage(StringBuffer sb, Set<?> settings, Comparator<?> cmp) {
                // do nothing
            }

            @Override
            public List<HelpLine> helpLines(int depth, Set<?> settings, Comparator<?> cmp) {
                // do nothing
                return null;
            }
        };

        final Option option1 = new Option("option1", "option1", true, "description1");
        group.addOption(option1);

        final Option option2 = new Option("option2", "option2", false, "description2");
        group.addOption(option2);

        final Argument arg = new Argument("arg", "description");
        group.addArgument(arg);

        options.addOption(group);
    }

    @Test
    public void testValidUsage() throws ParseException {
        final CommandLine commandLine = new CommandLine("-myGroup=option1 option2 arg");
        options.parse(commandLine);

        assertTrue(commandLine.hasOption("myGroup.option1"));
        assertTrue(commandLine.hasOption("myGroup.option2"));
        assertTrue(commandLine.hasOption("myGroup.arg"));

        assertEquals("option1", commandLine.getOptionValue("myGroup.option1"));
        assertNull(commandLine.getOptionValue("myGroup.option2"));
        assertEquals("", commandLine.getOptionValue("myGroup.arg"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        final CommandLine commandLine = new CommandLine("-myGroup=option1");
        options.parse(commandLine);

        fail("A ParseException should have been thrown");
    }

    @Test(expected = ParseException.class)
    public void testExtraOption() throws ParseException {
        final CommandLine commandLine = new CommandLine("-myGroup=option1 option1");
        options.parse(commandLine);

        fail("A ParseException should have been thrown");
    }

    @Test(expected = ParseException.class)
    public void testInvalidArgument() throws ParseException {
        final CommandLine commandLine = new CommandLine("-myGroup=option1 argInvalid");
        options.parse(commandLine);

        fail("A ParseException should have been thrown");
    }

    @Test(expected = ParseException.class)
    public void testArgumentMissing() throws ParseException {
        final CommandLine commandLine = new CommandLine("-myGroup=option1");
        options.parse(commandLine);

        fail("A ParseException should have been thrown");
    }

    @Test
    public void testHelp() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }
}