package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private GroupImpl group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();

        Option opt1 = new OptionBuilder("o1")
                .longOpt("option1")
                .desc("This is option 1")
                .build();
        options.add(opt1);

        Option opt2 = new OptionBuilder("o2")
                .longOpt("option2")
                .desc("This is option 2")
                .hasArg(true)
                .argName("ARG")
                .build();
        options.add(opt2);

        Option opt3 = new OptionBuilder("o3")
                .longOpt("option3")
                .desc("This is option 3")
                .isRequired()
                .build();
        options.add(opt3);

        Argument arg1 = new ArgumentBuilder()
                .name("ARG1")
                .usage("ARG1")
                .description("This is argument 1")
                .build();

        Argument arg2 = new ArgumentBuilder()
                .name("ARG2")
                .usage("ARG2")
                .description("This is argument 2")
                .build();

        List<Argument> anonymous = new ArrayList<>();
        anonymous.add(arg1);
        anonymous.add(arg2);

        group = new GroupImpl(options, "MyGroup", "This is my group", 1, 3, true) {
            {
                setAnonymous(anonymous);
            }
        };

        commandLine = new BasicParseResult();
    }

    @Test
    public void testCanProcess() {
        // Test valid option
        assertTrue(group.canProcess(commandLine, "o1"));

        // Test valid option with argument
        assertTrue(group.canProcess(commandLine, "o2 ARG"));

        // Test valid option with argument and another option
        assertTrue(group.canProcess(commandLine, "o2 ARG ORG"));

        // Test valid option with argument and trigger of another option
        assertTrue(group.canProcess(commandLine, "o2 ORG"));

        // Test trigger of required option
        assertTrue(group.canProcess(commandLine, "o3"));

        // Test trigger of required option with argument
        assertFalse(group.canProcess(commandLine, "o3 ARG"));

        // Test trigger of non-existent option
        assertFalse(group.canProcess(commandLine, "NONEXIST"));

        // Test trigger of anonymous argument
        assertTrue(group.canProcess(commandLine, "ARG1"));
        assertTrue(group.canProcess(commandLine, "ARG2"));

        // Test trigger of anonymous argument with value
        assertFalse(group.canProcess(commandLine, "ARG1 VALUE"));
        assertFalse(group.canProcess(commandLine, "ARG2 VALUE"));

        // Test trigger of non-existent anonymous argument
        assertFalse(group.canProcess(commandLine, "ARG3"));
    }

    @Test
    public void testProcess() throws OptionException {
        // Test valid option
        commandLine.parse("--option1");
        assertEquals(1, commandLine.getNumArgs());
        assertEquals("", commandLine.getArgs()[0]);
        assertFalse(commandLine.hasOption("o2"));
        assertTrue(commandLine.hasOption("o1"));

        // Test valid option with argument
        commandLine.parse("--option1=VALUE");
        assertEquals(1, commandLine.getNumArgs());
        assertEquals("VALUE", commandLine.getArgs()[0]);
        assertFalse(commandLine.hasOption("o2"));
        assertTrue(commandLine.hasOption("o1"));

        // Test valid option with argument and another option
        commandLine.parse("--option1=VALUE --option2=ARG");
        assertEquals(1, commandLine.getNumArgs());
        assertEquals("VALUE", commandLine.getArgs()[0]);
        assertTrue(commandLine.hasOption("o2"));
        assertFalse(commandLine.hasOption("o3"));

        // Test trigger of required option
        commandLine.parse("--option3");
        assertEquals(0, commandLine.getNumArgs());
        assertTrue(commandLine.hasOption("o3"));

        // Test trigger of required option with argument
        assertThrows(OptionException.class, () -> commandLine.parse("--option3 ARG"));

        // Test trigger of non-existent option
        assertThrows(OptionException.class, () -> commandLine.parse("--nonexistent"));

        // Test trigger of anonymous argument
        commandLine.parse("--ARG1=VALUE");
        assertEquals(1, commandLine.getNumArgs());
        assertEquals("VALUE", commandLine.getArgs()[0]);
        assertTrue(commandLine.hasOption("o1"));
        assertTrue(commandLine.hasOption("o2"));
        assertTrue(commandLine.hasOption("o3"));
        assertTrue(commandLine.getArgValue("ARG1").isPresent());

        // Test trigger of anonymous argument with value
        assertThrows(IllegalArgumentException.class, () -> commandLine.parse("--ARG1 VALUE ARG"));
    }

    @Test
    public void testValidate() throws OptionException {
        // Test valid command line
        commandLine.parse("--option1 --option2 ARG");
        group.validate(commandLine);

        // Test command line with missing option
        assertFalse(commandLine.hasOption("o1"));
        commandLine.parse("--option2 ARG");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing option: o1", e.getMessage());
        }

        // Test command line with too many options
        commandLine.parse("--option1 --option1 --option2 ARG");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Too many options: 3", e.getMessage());
        }

        // Test command line with missing required option
        commandLine.parse("--option2 ARG");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing option: o3", e.getMessage());
        }

        // Test command line with multiple values for same option
        commandLine.parse("--option2 ARG1 ARG2");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Duplicate option: option2", e.getMessage());
        }

        // Test command line with missing value for argument
        commandLine.parse("--option2 --ARG1");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Missing value for argument: ARG1", e.getMessage());
        }

        // Test command line with extra value for argument
        commandLine.parse("--option2 --ARG1 VALUE ARG2");
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Too many values for argument: ARG1", e.getMessage());
        }
    }

    @Test
    public void testHelp() {
        HelpFormatter formatter = new HelpFormatter();
        group.printHelp("java", formatter);
    }
}