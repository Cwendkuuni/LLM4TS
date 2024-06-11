import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.Default;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.parser.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Options options;
    private Group group;

    @Before
    public void setup() {
        options = new Options();
        final List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(OptionBuilder.withName("g")
                .withDescription("group option")
                .withMinValues(1)
                .withMaxValues(3)
                .isRequired()
                .create(false));
        groupOptions.add(OptionBuilder.withName("a")
                .withDescription("anonymous option")
                .create(new ArgumentBuilder()
                        .withName("anonymous")
                        .withArgName("anonymous")
                        .withDescription("anonymous argument")
                        .withMinValues(1)
                        .withMaxValues(1)
                        .create()));
        group = new GroupImpl(groupOptions, "group", "group description");
    }

    @Test
    public void testCanProcess() {
        final CommandLine commandLine = new CommandLine();

        // test with valid options
        for (int i = 0; i < 3; i++) {
            commandLine.addOption(group.getOptions().get(0));
            assertTrue(group.canProcess(commandLine, null));
        }

        // test with invalid options
        commandLine.addOption(group.getOptions().get(0));
        commandLine.addOption(group.getOptions().get(0));
        assertFalse(group.canProcess(commandLine, null));

        // test with anonymous argument
        commandLine.addOption(group.getOptions().get(0));
        commandLine.addOption(group.getOptions().get(1));
        assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testProcess() throws OptionException {
        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group, Arrays.asList("g", "a=value"));
        Assert.assertEquals("value", commandLine.getOptionValue("a"));

        final CommandLine commandLine2 = new CommandLine();
        commandLine2.parse(group, Arrays.asList("g", "g", "a=value2"));
        Assert.assertEquals("value2", commandLine2.getOptionValue("a"));
    }

    @Test
    public void testValidate() throws OptionException {
        final CommandLine commandLine = new CommandLine();

        // test with valid options
        for (int i = 0; i < 3; i++) {
            commandLine.addOption(group.getOptions().get(0));
        }
        group.validate(commandLine);

        // test with invalid options
        commandLine.addOption(group.getOptions().get(0));
        commandLine.addOption(group.getOptions().get(0));
        try {
            group.validate(commandLine);
            Assert.fail();
        } catch (OptionException e) {
            // expected
        }

        // test with invalid anonymous argument
        commandLine.addOption(group.getOptions().get(0));
        commandLine.addOption(group.getOptions().get(1));
        commandLine.addOption(group.getOptions().get(1));
        try {
            group.validate(commandLine);
            Assert.fail();
        } catch (OptionException e) {
            // expected
        }

        // test with missing options
        commandLine.addOption(group.getOptions().get(0));
        try {
            group.validate(commandLine);
            Assert.fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("group description", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("-g GROUP OPTIONS\n" +
                "  -a ANONYMOUS ARGUMENT\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final HelpFormatter formatter = new HelpFormatter();
        final List<String> helpLines = group.helpLines(1, Collections.emptySet(), null);
        final List<String> expectedHelpLines = Arrays.asList(
                "",
                "  Usage: command [options]",
                "",
                "  Options:",
                "    -g,--group GROUP\n" +
                "        group option\n" +
                "        (3 times)",
                "    -a,--anonymous ANONYMOUS\n" +
                "        anonymous argument",
                "");
        formatter.printHelp(System.out, "command", group.helpLines(1, Collections.emptySet(), null), true);
        assertEquals(expectedHelpLines, helpLines);
    }

    @Test
    public void testGetOptions() {
        final List<Option> options = group.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.get(0) instanceof Option);
        assertTrue(options.get(1) instanceof Argument);
    }

    @Test
    public void testGetAnonymous() {
        final List<Option> anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
        assertTrue(anonymous.get(0) instanceof Argument);
    }

    @Test
    public void testFindOption() {
        final Option option = group.findOption("g");
        assertNotNull(option);
        assertEquals("group", option.getLongOpt());
        assertEquals("g", option.getOpt());
        assertEquals("group option", option.getDescription());
        assertEquals(Option.UNLIMITED_VALUES, option.getMaxValues());
        assertEquals(Option.REQUIRED, option.isRequired());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        final CommandLine commandLine = new CommandLine();
        group.defaults(commandLine);
        assertFalse(commandLine.hasOption("g"));
        assertFalse(commandLine.hasOption("a"));
    }
}