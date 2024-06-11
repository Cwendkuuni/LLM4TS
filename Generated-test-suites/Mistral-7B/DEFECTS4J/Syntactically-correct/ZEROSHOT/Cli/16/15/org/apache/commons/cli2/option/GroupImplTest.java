import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    private CommandLine cmd;
    private Group group;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();

        Option option1 = OptionBuilder.withName("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = OptionBuilder.withName("option2")
                .withDescription("Option 2")
                .create();
        Option option3 = OptionBuilder.withName("option3")
                .withDescription("Option 3")
                .withArgName("ARG")
                .create();
        Argument arg1 = ArgumentBuilder.withName("arg1")
                .withDescription("Argument 1")
                .create();

        options.add(option1);
        options.add(option2);
        options.add(option3);

        group = new GroupImpl(options, "group", "A group of options", -1, -1);
    }

    @Test
    public void testCanProcess() {
        cmd = new CommandLine();

        // can process option1
        cmd.parseArgument(0, "option1");
        assertTrue(group.canProcess(cmd, "option1"));

        // can process option2
        cmd.parseArgument(0, "option2");
        assertTrue(group.canProcess(cmd, "option2"));

        // can process option3
        cmd.parseArgument(0, "ARG");
        assertTrue(group.canProcess(cmd, "ARG"));

        // can process anonymous argument
        cmd.parseArgument(1, "arg1");
        assertTrue(group.canProcess(cmd, "arg1"));

        // cannot process non-existent option
        assertFalse(group.canProcess(cmd, "nonExistentOption"));
    }

    @Test
    public void testGetPrefixes() {
        List<String> prefixes = new ArrayList<>();

        group.getPrefixes().iterator().forEachRemaining(prefixes::add);

        assertEquals("Empty prefixes collection should contain no elements", 0, prefixes.size());

        prefixes.clear();

        Option option1 = OptionBuilder.withName("option1")
                .withDescription("Option 1")
                .withLongOpt("longOption1")
                .create();
        Option option2 = OptionBuilder.withName("option2")
                .withDescription("Option 2")
                .withLongOpt("longOption2")
                .create();
        option1.setOptionParser(OptionParserBuilder.withName("parser")
                .withPrefix("p")
                .create());
        option2.setOptionParser(OptionParserBuilder.withName("parser")
                .withPrefix("p")
                .create());

        group = new GroupImpl(Arrays.asList(option1, option2), "group", "A group of options", -1, -1);

        group.getPrefixes().iterator().forEachRemaining(prefixes::add);

        assertEquals("Prefixes collection should contain the 'p' prefix", 1, prefixes.size());
        assertEquals("Prefixes collection should contain the 'p' prefix", "p", prefixes.get(0));
    }

    @Test
    public void testProcess() {
        cmd = new CommandLine();

        // process option1
        cmd.parseArgument(0, "option1");
        group.process(cmd, cmd.listIterator());
        assertEquals("Option1 value should be set", "option1", cmd.getValue("option1"));

        // process option2
        cmd.parseArgument(0, "option2");
        group.process(cmd, cmd.listIterator());
        assertEquals("Option2 value should be set", "option2", cmd.getValue("option2"));

        // process option3 with value "VALUE"
        cmd.parseArgument(0, "ARG");
        cmd.parseArgument(1, "VALUE");
        group.process(cmd, cmd.listIterator());
        assertEquals("Option3 value should be set", "VALUE", cmd.getValue("option3.ARG"));

        // process anonymous argument
        cmd.parseArgument(1, "arg1");
        group.process(cmd, cmd.listIterator());
        assertEquals("Anonymous argument value should be set", "arg1", cmd.getValue("arg1"));

        // process option that does not belong to the group
        cmd.parseArgument(1, "nonExistentOption");
        group.process(cmd, cmd.listIterator());
        assertNull("Non-existent option value should be null", cmd.getValue("nonExistentOption"));
    }

    @Test
    public void testValidate() throws OptionException {
        cmd = new CommandLine();

        // validate with valid options
        group.validate(cmd);

        // validate with missing options
        cmd.clear();
        cmd.parseArgument(0, "option1");
        try {
            group.validate(cmd);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals("Missing required option: option2", e.getMessage());
        }

        // validate with extra options
        cmd.clear();
        cmd.parseArgument(0, "option1");
        cmd.parseArgument(0, "option2");
        cmd.parseArgument(0, "option3");
        try {
            group.validate(cmd);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals("Too many options: argument list contains 3 elements, but the group only accepts 2", e.getMessage());
        }
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("Group name should be 'group'", "group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Group description should be 'A group of options'", "A group of options", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("Usage should be empty", "", buffer.toString());

        group = new GroupBuilder()
                .withName("group")
                .withDescription("A group of options")
                .create();

        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("Usage should be 'group'\n", "group\n", buffer.toString());

        group = new GroupBuilder()
                .withName("group")
                .withDescription("A group of options")
                .addOption(OptionBuilder.withName("option1")
                        .withDescription("Option 1")
                        .create())
                .create();

        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("Usage should be 'group\noption1'\n", "group\noption1\n", buffer.toString());

        group = new GroupBuilder()
                .withName("group")
                .withDescription("A group of options")
                .addOption(OptionBuilder.withName("option1")
                        .withDescription("Option 1")
                        .create())
                .addOption(OptionBuilder.withName("option2")
                        .withDescription("Option 2")
                        .create())
                .create();

        group.appendUsage(buffer, Collections.emptySet(), null);
        assertEquals("Usage should be 'group\noption1\noption2'\n", "group\noption1\noption2\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<String> helpLines = new ArrayList<>();

        group.helpLines(0, Collections.emptySet(), null).forEach(helpLines::add);

        assertEquals("Help lines should be empty", 0, helpLines.size());

        group = new GroupBuilder()
                .withName("group")
                .withDescription("A group of options")
                .create();

        group.helpLines(0, Collections.emptySet(), null).forEach(helpLines::add);
        assertEquals("Help lines should contain 'group'\n", 1, helpLines.size());
        assertEquals("First help line should be 'group\n'", "group\n", helpLines.get(0));

        group = new GroupBuilder()
                .withName("group")
                .withDescription("A group of options")
                .addOption(OptionBuilder.withName("option1")
                        .withDescription("Option 1")
                        .create())
                .create();

        group.helpLines(0, Collections.emptySet(), null).forEach(helpLines::add);
        assertEquals("Help lines should contain 'group\noption1\n'", 2, helpLines.size());
        assertEquals("First help line should be 'group\n'", "group\n", helpLines.get(0));
        assertEquals("Second help line should be 'option1\n'", "option1\n", helpLines.get(1));

        group = new GroupBuilder()
                .withName("group")
                .withDescription("A group of options")
                .addOption(OptionBuilder.withName("option1")
                        .withDescription("Option 1")
                        .create())
                .addOption(OptionBuilder.withName("option2")
                        .withDescription("Option 2")
                        .create())
                .create();

        group.helpLines(0, Collections.emptySet(), null).forEach(helpLines::add);
        assertEquals("Help lines should contain 'group\noption1\noption2\n'", 3, helpLines.size());
        assertEquals("First help line should be 'group\n'", "group\n", helpLines.get(0));
        assertEquals("Second help line should be 'option1\n'", "option1\n", helpLines.get(1));
        assertEquals("Third help line should be 'option2\n'", "option2\n", helpLines.get(2));
    }

    @Test
    public void testGetOptions() {
        List<Option> options = group.getOptions();

        assertEquals("Number of options should be 3", 3, options.size());
        assertEquals("First option should be 'option1'\n", "option1", options.get(0).getPreferredName());
        assertEquals("Second option should be 'option2'\n", "option2", options.get(1).getPreferredName());
        assertEquals("Third option should be 'option3'\n", "option3", options.get(2).getPreferredName());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = group.getAnonymous();

        assertEquals("Number of anonymous arguments should be 1", 1, anonymous.size());
        assertEquals("Anonymous argument should be 'arg1'\n", "arg1", anonymous.get(0).getPreferredName());
    }

    @Test
    public void testFindOption() {
        Option option1 = group.findOption("option1");
        assertNotNull("Option 'option1' should be found", option1);

        Option option2 = group.findOption("option2");
        assertNotNull("Option 'option2' should be found", option2);

        Option option3 = group.findOption("option3");
        assertNotNull("Option 'option3' should be found", option3);

        Option nonExistentOption = group.findOption("nonExistentOption");
        assertNull("Non-existent option should not be found", nonExistentOption);
    }

    @Test
    public void testGetMinimum() {
        assertEquals("Minimum number of options should be 0", 0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals("Maximum number of options should be -1", -1, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse("Group is not required", group.isRequired());
    }

    @Test
    public void testDefaults() {
        cmd = new CommandLine();

        group.defaults(cmd);

        assertFalse("Option1 should not be set by default", cmd.hasOption("option1"));
        assertFalse("Option2 should not be set by default", cmd.hasOption("option2"));
        assertFalse("Option3 should not be set by default", cmd.hasOption("option3"));
        assertFalse("Anonymous argument should not be set by default", cmd.hasOption("arg1"));
    }
}