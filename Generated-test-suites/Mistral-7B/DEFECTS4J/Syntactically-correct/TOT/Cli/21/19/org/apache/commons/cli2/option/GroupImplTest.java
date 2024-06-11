package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.options.GroupOptions;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link GroupImpl} class.
 */
public class GroupImplTest {

    private GroupOptions groupOptions;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(new OptionBuilder("o1")
                .withLongOpt("longOpt1")
                .withDescription("Option 1")
                .withRequiredOption()
                .create());
        options.add(new OptionBuilder("o2")
                .withLongOpt("longOpt2")
                .withDescription("Option 2")
                .withOptionalArg()
                .withArgName("arg2")
                .create());
        groupOptions = new GroupOptions("groupName", "groupDescription", options);
    }

    @Test
    public void testCanProcessWithAvailableOptions() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(groupOptions.getOptions());

        assertTrue(groupOptions.canProcess(commandLine, "o1"));
        assertTrue(groupOptions.canProcess(commandLine, "o2"));
        assertTrue(groupOptions.canProcess(commandLine, "longOpt1"));
        assertTrue(groupOptions.canProcess(commandLine, "longOpt2"));
        assertTrue(groupOptions.canProcess(commandLine, "groupName"));
    }

    @Test
    public void testCanProcessWithInvalidOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(groupOptions.getOptions());

        assertFalse(groupOptions.canProcess(commandLine, "invalidOption"));
    }

    @Test
    public void testCanProcessWithBurstableOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(groupOptions.getOptions());

        commandLine.parseArgument(0, "o1");
        commandLine.parseArgument(0, "o1");

        assertTrue(groupOptions.canProcess(commandLine, "o2"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooFewOptions() throws OptionException {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(groupOptions.getOptions());

        groupOptions.process(commandLine, commandLine.iterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooManyOptions() throws OptionException {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(groupOptions.getOptions());

        commandLine.parseArgument(0, "o1");
        commandLine.parseArgument(0, "o1");
        commandLine.parseArgument(0, "o1");

        groupOptions.process(commandLine, commandLine.iterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidOption() throws OptionException {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(groupOptions.getOptions());

        commandLine.parseArgument(0, "invalidOption");

        groupOptions.process(commandLine, commandLine.iterator());
    }

    @Test
    public void testProcessWithOptionalOptionWithNoArgument() throws OptionException {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(groupOptions.getOptions());

        commandLine.parseArgument(0, "o2");

        Option option = groupOptions.findOption("o2");

        assertEquals("arg2 is not set", null, option.getValue());
    }

    @Test
    public void testProcessWithOptionalOptionWithArgument() throws OptionException {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(groupOptions.getOptions());

        commandLine.parseArgument(0, "o2");
        commandLine.parseArgument(0, "arg2");

        Option option = groupOptions.findOption("o2");

        assertEquals("arg2 is not set", "arg2", option.getValue());
    }

    @Test
    public void testProcessWithRequiredOption() throws OptionException {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(groupOptions.getOptions());

        try {
            groupOptions.process(commandLine, commandLine.iterator());
        } catch (OptionException e) {
            Option option = groupOptions.findOption("o1");
            assertNotNull("Option is not found", option);
            assertEquals("Option should be required", option.isRequired(), true);
        }
    }

    @Test
    public void testValidateWithRequiredOption() throws OptionException {
        CommandLine commandLine = new CommandLine();

        try {
            groupOptions.validate(commandLine);
        } catch (OptionException e) {
            Option option = groupOptions.findOption("o1");
            assertNotNull("Option is not found", option);
            assertEquals("Option should be required", option.isRequired(), true);
        }

        commandLine.parseArgument(0, "o1");

        try {
            groupOptions.validate(commandLine);
            fail("OptionException should be thrown");
        } catch (OptionException e) {
            Option option = groupOptions.findOption("o1");
            assertNotNull("Option is not found", option);
            assertEquals("Option should be required", option.isRequired(), true);
        }
    }

    @Test
    public void testValidateWithOptionalOption() throws OptionException {
        CommandLine commandLine = new CommandLine();

        try {
            groupOptions.validate(commandLine);
        } catch (OptionException e) {
            Option option = groupOptions.findOption("o1");
            assertNotNull("Option is not found", option);
            assertFalse("Option should not be required", option.isRequired());
        }

        commandLine.parseArgument(0, "o1");

        try {
            groupOptions.validate(commandLine);
        } catch (OptionException e) {
            Option option = groupOptions.findOption("o1");
            assertNotNull("Option is not found", option);
            assertFalse("Option should not be required", option.isRequired());
        }

        commandLine.parseArgument(0, "o1");

        try {
            groupOptions.validate(commandLine);
        } catch (OptionException e) {
            Option option = groupOptions.findOption("o1");
            assertNotNull("Option is not found", option);
            assertEquals("Option has too many values", option.getValue().size(), 2);
        }
    }

    @Test
    public void testUsage() {
        HelpFormatter formatter = new HelpFormatter();

        String usage = formatter.printHelp("Test", groupOptions.getOptions(), true);

        System.out.println(usage);

        String[] lines = usage.split("\n");

        assertEquals("Incorrect number of lines in help text", 18, lines.length);
        assertEquals("Incorrect group description", "groupDescription", lines[0]);
        assertEquals("Incorrect group name", "groupName", lines[1]);
        assertEquals("Incorrect option 1 description", "Option 1", lines[3]);
        assertEquals("Incorrect option 1 long opt", "longOpt1", lines[5]);
        assertEquals("Incorrect option 1 is required", "(required,)", lines[7]);
        assertEquals("Incorrect option 1 arg name", "", lines[9]);
        assertEquals("Incorrect option 1 has default value", "", lines[11]);
        assertEquals("Incorrect option 2 description", "Option 2", lines[13]);
        assertEquals("Incorrect option 2 long opt", "longOpt2", lines[15]);
        assertEquals("Incorrect option 2 arg name", "arg2", lines[17]);
        assertEquals("Incorrect option 2 is optional", "[,]", lines[19]);
    }
}