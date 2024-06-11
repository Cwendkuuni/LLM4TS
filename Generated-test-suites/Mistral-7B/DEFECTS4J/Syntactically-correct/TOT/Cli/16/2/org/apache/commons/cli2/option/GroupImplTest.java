package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.OptionGroupImpl;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

    private OptionGroup group;
    private Option option1;
    private Option option2;
    private Option option3;
    private Option option4;
    private Argument arg;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(option1 = OptionBuilder.withName("o1").createOption());
        options.add(option2 = OptionBuilder.withName("o2").createOption());
        option2.setArgument(ArgType.STRING.getName());
        options.add(option3 = OptionBuilder.withName("o3").createOption());
        option3.setArgument(ArgType.LONG.getName());
        options.add(option4 = OptionBuilder.withName("o4").createOption());
        option4.setArgument(ArgType.STRING.getName());
        options.add(arg = ArgumentBuilder.withName("a").createArgument());
        group = new OptionGroupImpl("G", "Group", options, null, 1, Integer.MAX_VALUE, null, null);
    }

    @Test
    public void testCanProcess() {
        CommandLine cmd = new CommandLine();
        cmd.parse(group.getParser());

        // test option validation
        cmd.parse(group.getParser(), "-o1 -o1");
        assertTrue(group.canProcess(cmd, "o1"));
        cmd.parse(group.getParser(), "-o1 -o1 o1");
        assertTrue(group.canProcess(cmd, "o1"));
        cmd.parse(group.getParser(), "-o1 o1");
        assertTrue(group.canProcess(cmd, "o1"));
        cmd.parse(group.getParser(), "o1");
        assertTrue(group.canProcess(cmd, "o1"));

        // test argument validation
        cmd.parse(group.getParser(), "--a a");
        assertTrue(group.canProcess(cmd, "a"));
        cmd.parse(group.getParser(), "--a a a");
        assertTrue(group.canProcess(cmd, "a"));
        cmd.parse(group.getParser(), "--a a a a");
        assertTrue(group.canProcess(cmd, "a"));
        cmd.parse(group.getParser(), "--a a");
        assertTrue(group.canProcess(cmd, "a"));

        // test invalid option
        cmd.parse(group.getParser(), "-o5");
        assertFalse(group.canProcess(cmd, "o5"));

        // test invalid argument
        cmd.parse(group.getParser(), "--a");
        assertFalse(group.canProcess(cmd, "a"));

        // test multiple arguments
        cmd.parse(group.getParser(), "--a a1 --a a2 --a a3");
        assertTrue(group.canProcess(cmd, "a"));
        assertTrue(group.canProcess(cmd, "a1"));
        assertTrue(group.canProcess(cmd, "a2"));
        assertTrue(group.canProcess(cmd, "a3"));

        // test options and arguments mixed
        cmd.parse(group.getParser(), "-o1 -a a1 --a a2 -o3 42");
        assertTrue(group.canProcess(cmd, "o1"));
        assertTrue(group.canProcess(cmd, "a"));
        assertTrue(group.canProcess(cmd, "a1"));
        assertTrue(group.canProcess(cmd, "o3"));
    }

    @Test
    public void testProcess() {
        CommandLine cmd = new CommandLine();

        // test option processing
        cmd.parse(group.getParser(), "-G -o1 value1");
        assertEquals("value1", group.getValue("o1"));
        cmd.parse(group.getParser(), "-G -o1 value1 -o1 value2");
        assertEquals("value2", group.getValue("o1"));

        // test argument processing
        cmd.parse(group.getParser(), "--G --a value1");
        assertEquals("value1", group.getValue("a"));
        cmd.parse(group.getParser(), "--G --a value1 value2");
        assertEquals("value2", group.getValue("a"));

        // test multiple options and arguments
        cmd.parse(group.getParser(), "-G -o1 value1 --a value1 --a value2");
        assertEquals("value1", group.getValue("o1"));
        assertEquals("value2", group.getValue("a"));

        // test error: missing option
        try {
            cmd.parse(group.getParser(), "--G");
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("Missing option G", e.getMessage());
        }

        // test error: missing argument
        try {
            cmd.parse(group.getParser(), "-G -o1");
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("The option 'o1' requires an argument.", e.getMessage());
        }

        // test error: multiple values for option
        try {
            cmd.parse(group.getParser(), "-G -o1 value1 -o1 value2");
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("Duplicate option: o1", e.getMessage());
        }

        // test error: invalid argument type
        try {
            cmd.parse(group.getParser(), "-G -o3 123");
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("Invalid argument type: 123 for option: o3", e.getMessage());
        }
    }

    @Test
    public void testValidate() {
        CommandLine cmd = new CommandLine();

        // test validate: no options or arguments
        cmd.parse(group.getParser(), "");
        assertNull(group.getValue("o1"));
        assertNull(group.getValue("a"));

        // test validate: option only
        cmd.parse(group.getParser(), "-G -o1");
        assertEquals("", group.getValue("o1"));

        // test validate: argument only
        cmd.parse(group.getParser(), "--G --a");
        assertNull(group.getValue("o1"));
        assertNull(group.getValue("a"));

        // test validate: option and argument
        cmd.parse(group.getParser(), "-G -o1 value1 --a value2");
        assertEquals("value1", group.getValue("o1"));
        assertEquals("value2", group.getValue("a"));

        // test validate: multiple options and arguments
        cmd.parse(group.getParser(), "-G -o1 value1 --a value1 --a value2");
        assertEquals("value1", group.getValue("o1"));
        assertEquals("value2", group.getValue("a"));

        // test validate: error: missing option
        try {
            cmd.parse(group.getParser(), "--G");
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("Missing option G", e.getMessage());
        }

        // test validate: error: missing argument
        try {
            cmd.parse(group.getParser(), "-G -o1");
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("The option 'o1' requires an argument.", e.getMessage());
        }

        // test validate: error: multiple values for option
        try {
            cmd.parse(group.getParser(), "-G -o1 value1 -o1 value2");
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("Duplicate option: o1", e.getMessage());
        }

        // test validate: error: invalid argument type
        try {
            cmd.parse(group.getParser(), "-G -o3 123");
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("Invalid argument type: 123 for option: o3", e.getMessage());
        }
    }

    @Test
    public void testHelp() {
        final String usage = "G:[options][arguments]";
        final Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.USE_COLONS);
        helpSettings.add(DisplaySetting.SHOW_OPTION_DELIMITER);
        helpSettings.add(DisplaySetting.SHOW_LONG_SYNTAX);
        helpSettings.add(DisplaySetting.SHOW_ARGUMENT_DELIMITER);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(0, usage, true, helpSettings, group, null);

        String expected = "GROUP (G)\n" +
                "options:\n" +
                "  o1 OPTIONAL\n" +
                "      This is option o1\n" +
                "  o2 OPTIONAL\n" +
                "      This is option o2\n" +
                "      Type: [string]\n" +
                "  o3 OPTIONAL\n" +
                "      This is option o3\n" +
                "      Type: [long]\n" +
                "  o4 OPTIONAL\n" +
                "      This is option o4\n" +
                "      Type: [string]\n" +
                "  a OPTIONAL\n" +
                "      This is argument a\n" +
                "      Type: [string]\n";

        String actual = formatter.getHelpText().replaceAll("\n$", "");

        assertEquals(expected, actual);
    }
}