import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupWithNoOptions() {
        List<Option> options = new ArrayList<>();
        Group group = new GroupImpl(options, null, null, 0, 0);

        CommandLine commandLine = new CommandLine();
        Group.parseCommandLine(commandLine, group);

        Assert.assertNull(group.getPreferredName());
        Assert.assertNull(group.getDescription());
        Assert.assertEquals(0, group.getOptions().size());
        Assert.assertEquals(0, group.getAnonymous().size());
        Assert.assertTrue(group.canProcess(commandLine, null));
        Assert.assertFalse(group.canProcess(commandLine, "unknown"));
    }

    @Test
    public void testGroupWithOneOption() {
        List<Option> options = Arrays.asList(
                new OptionBuilder("o")
                        .withLongOpt("option")
                        .withDescription("an option")
                        .build()
        );
        Group group = new GroupImpl(options, "group", null, 0, 1);

        CommandLine commandLine = new CommandLine();
        Group.parseCommandLine(commandLine, group);

        commandLine.parseArgument(0, "o");

        Assert.assertEquals("group", group.getPreferredName());
        Assert.assertNull(group.getDescription());
        Assert.assertEquals(1, group.getOptions().size());
        Assert.assertEquals(0, group.getAnonymous().size());
        Assert.assertTrue(group.canProcess(commandLine, "o"));
        Assert.assertFalse(group.canProcess(commandLine, "unknown"));
    }

    @Test
    public void testGroupWithMultipleOptions() {
        List<Option> options = Arrays.asList(
                new OptionBuilder("o1")
                        .withLongOpt("option1")
                        .withDescription("first option")
                        .build(),
                new OptionBuilder("o2")
                        .withLongOpt("option2")
                        .withDescription("second option")
                        .build()
        );
        Group group = new GroupImpl(options, "group", null, 0, Integer.MAX_VALUE);

        CommandLine commandLine = new CommandLine();
        Group.parseCommandLine(commandLine, group);

        commandLine.parseArgument(0, "o1");
        commandLine.parseArgument(1, "o2");

        Assert.assertEquals("group", group.getPreferredName());
        Assert.assertNull(group.getDescription());
        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertEquals(0, group.getAnonymous().size());
        Assert.assertTrue(group.canProcess(commandLine, "o1"));
        Assert.assertTrue(group.canProcess(commandLine, "o2"));
        Assert.assertFalse(group.canProcess(commandLine, "unknown"));
    }

    @Test
    public void testGroupWithAnonymousArguments() {
        List<Option> options = Arrays.asList(
                new OptionBuilder("g")
                        .withLongOpt("group")
                        .withDescription("group option")
                        .isRequired(false)
                        .build(),
                new OptionBuilder("a")
                        .withLongOpt("argument")
                        .withDescription("an argument")
                        .isRequired(true)
                        .build(),
                new OptionBuilder("a")
                        .withLongOpt("argument")
                        .withDescription("another argument")
                        .isRequired(false)
                        .build()
        );
        Group group = new GroupImpl(options, "group", null, 1, 2);

        CommandLine commandLine = new CommandLine();
        Group.parseCommandLine(commandLine, group);

        commandLine.parseArgument(0, "g");
        commandLine.parseArgument(1, "a1");
        commandLine.parseArgument(2, "a2");

        Assert.assertEquals("group", group.getPreferredName());
        Assert.assertNull(group.getDescription());
        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertEquals(2, group.getAnonymous().size());
        Assert.assertTrue(group.canProcess(commandLine, "g"));
        Assert.assertTrue(group.canProcess(commandLine, "a1"));
        Assert.assertTrue(group.canProcess(commandLine, "a2"));
        Assert.assertFalse(group.canProcess(commandLine, "unknown"));
    }

    @Test
    public void testGroupWithInvalidNumberOfOptions() {
        List<Option> options = Arrays.asList(
                new OptionBuilder("o")
                        .withLongOpt("option")
                        .build()
        );
        Group group = new GroupImpl(options, null, null, 0, 1);

        CommandLine commandLine = new CommandLine();
        Group.parseCommandLine(commandLine, group);

        commandLine.parseArgument(0, "o");
        commandLine.parseArgument(1, "o");

        try {
            group.validate(commandLine);
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals("Unexpected option: o", e.getMessage());
        }
    }

    @Test
    public void testGroupWithMissingRequiredOption() {
        List<Option> options = Arrays.asList(
                new OptionBuilder("o")
                        .withLongOpt("option")
                        .isRequired(true)
                        .build()
        );
        Group group = new GroupImpl(options, null, null, 0, 0);

        CommandLine commandLine = new CommandLine();
        Group.parseCommandLine(commandLine, group);

        try {
            group.validate(commandLine);
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals("Missing required option: option", e.getMessage());
        }
    }

    @Test
    public void testGroupWithDuplicateTriggers() {
        List<Option> options = Arrays.asList(
                new OptionBuilder("o1")
                        .withLongOpt("option1")
                        .withDescription("first option")
                        .build(),
                new OptionBuilder("o1")
                        .withLongOpt("option1")
                        .withDescription("second option")
                        .build()
        );
        Group group = new GroupImpl(options, null, null, 0, 0);

        try {
            group.validate(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Duplicate option names: option1", e.getMessage());
        }
    }

    @Test
    public void testGroupWithMismatchedMinMax() {
        List<Option> options = Arrays.asList(
                new OptionBuilder("o")
                        .withLongOpt("option")
                        .isRequired(true)
                        .build()
        );
        Group group = new GroupImpl(options, null, null, 1, 0);

        try {
            group.validate(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Minimum and maximum number of occurrences do not match: 1 and 0", e.getMessage());
        }
    }

    @Test
    public void testGroupWithHelp() {
        List<Option> options = Arrays.asList(
                new OptionBuilder("g")
                        .withLongOpt("group")
                        .withDescription("group option")
                        .build()
        );
        Group group = new GroupImpl(options, "group", "description", 0, Integer.MAX_VALUE);

        HelpFormatter formatter = new HelpFormatter();

        CommandLine commandLine = new CommandLine();
        Group.parseCommandLine(commandLine, group);
        formatter.printHelp("Test", group.getHelpLines(0, new HashSet<>(), null), true);

        String expectedHelp = "usage: Test [-g]\n" +
                "\n" +
                "options:\n" +
                "  -g,--group\n" +
                "      group option\n";

        Assert.assertEquals(expectedHelp, formatter.getHelpText());
    }

    @Test
    public void testGroupWithHelpExpanded() {
        List<Option> options = Arrays.asList(
                new OptionBuilder("g")
                        .withLongOpt("group")
                        .withDescription("group option")
                        .build()
        );
        Group group = new GroupImpl(options, "group", "description", 0, Integer.MAX_VALUE);

        CommandLine commandLine = new CommandLine();
        Group.parseCommandLine(commandLine, group);

        List<HelpLine> helpLines = group.helpLines(0, new HashSet<>(), null);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", helpLines, true);

        String expectedHelp = "usage: Test [-g]\n" +
                "\n" +
                "options:\n" +
                "  -g,--group\n" +
                "      group option\n";

        Assert.assertEquals(expectedHelp, formatter.getHelpText());
    }
}