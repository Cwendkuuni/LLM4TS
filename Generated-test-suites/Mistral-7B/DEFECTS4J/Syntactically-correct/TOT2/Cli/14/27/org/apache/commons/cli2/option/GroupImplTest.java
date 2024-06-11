import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.cli2.DefaultParseResult.*;

public class GroupImplTest {

    private Options options;

    @Before
    public void setUp() {
        List<Option> optionsList = new ArrayList<>();

        Option groupOption1 = new OptionBuilder("g1")
                .withLongOpt("group1")
                .withDescription("Description for group1")
                .build();

        Option option1A = new OptionBuilder("o1a")
                .withLongOpt("option1a")
                .withDescription("Description for option1a")
                .isRequired(false)
                .build();

        Option option1B = new OptionBuilder("o1b")
                .withLongOpt("option1b")
                .withDescription("Description for option1b")
                .isRequired(false)
                .build();

        Option option1C = new OptionBuilder("o1c")
                .withLongOpt("option1c")
                .withDescription("Description for option1c")
                .isRequired(false)
                .build();

        Option groupOption2 = new OptionBuilder("g2")
                .withLongOpt("group2")
                .withDescription("Description for group2")
                .build();

        Option option2A = new OptionBuilder("o2a")
                .withLongOpt("option2a")
                .withDescription("Description for option2a")
                .isRequired(false)
                .build();

        Option option2B = new OptionBuilder("o2b")
                .withLongOpt("option2b")
                .withDescription("Description for option2b")
                .isRequired(false)
                .build();

        optionsList.add(groupOption1);
        optionsList.add(option1A);
        optionsList.add(option1B);
        optionsList.add(option1C);
        optionsList.add(groupOption2);

        this.options = new Options(optionsList);
    }

    @Test
    public void testCanProcessBasicUsage() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Arrays.asList(new Option[] { groupOption1 }), null, null, 1, 1);

        Assert.assertTrue(group.canProcess(commandLine, "g1"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWrongGroup() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Arrays.asList(new Option[] { groupOption1 }), null, null, 1, 1);

        Assert.assertFalse(group.canProcess(commandLine, "g2"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWrongOption() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Arrays.asList(new Option[] { groupOption1 }), null, null, 1, 1);

        Assert.assertFalse(group.canProcess(commandLine, "o3"));
    }

    @Test
    public void testCanProcessBurstingOption() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Arrays.asList(new Option[] { groupOption1 }), null, null, 1, 1);

        commandLine.parse(options.parse(null));
        commandLine.parse(Arrays.asList("g1", "o1a", "o1b"));

        Option option1A = group.findOption("o1a");
        Assert.assertTrue(option1A.canProcess(commandLine, "o1a"));

        Option option1B = group.findOption("o1b");
        Assert.assertTrue(option1B.canProcess(commandLine, "o1b"));

        Option option1C = group.findOption("o1c");
        Assert.assertFalse(option1C.canProcess(commandLine, "o1c"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessMissingOption() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Arrays.asList(new Option[] { groupOption1 }), null, null, 1, 1);

        commandLine.parse(options.parse(null));
        commandLine.parse(Arrays.asList("g1"));

        Option option1A = group.findOption("o1a");
        Assert.assertFalse(option1A.canProcess(commandLine, "o1a"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWrongArgument() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 0, 0);
        group.anonymous = Arrays.asList(new Option[] { new Argument("a") });

        commandLine.parse(options.parse(null));
        commandLine.parse(Arrays.asList("g1", "a"));

        Assert.assertFalse(group.canProcess(commandLine, "a"));
    }

    @Test
    public void testCanProcessValidArgument() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 0, 0);
        group.anonymous = Arrays.asList(new Option[] { new Argument("a") });

        commandLine.parse(options.parse(null));
        commandLine.parse(Arrays.asList("g1", "a_value"));

        Assert.assertTrue(group.canProcess(commandLine, "a"));
    }

    @Test(expected = OptionException.class)
    public void testProcessInvalidArgument() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 0, 0);
        group.anonymous = Arrays.asList(new Option[] { new Argument("a") });

        commandLine.parse(options.parse(null));
        commandLine.parse(Arrays.asList("g1", "invalid_value"));

        group.process(commandLine, commandLine.iterator());

        Assert.fail("Should have thrown OptionException");
    }

    @Test
    public void testProcessValidArgument() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Collections.emptyList(), null, null, 0, 0);
        group.anonymous = Arrays.asList(new Option[] { new Argument("a") });

        commandLine.parse(options.parse(null));
        commandLine.parse(Arrays.asList("g1", "valid_value"));

        group.process(commandLine, commandLine.iterator());
    }

    @Test
    public void testValidateBasicUsage() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Arrays.asList(new Option[] { groupOption1 }), null, null, 1, 1);

        commandLine.parse(options.parse(null));
        commandLine.parse(Arrays.asList("g1", "o1a", "o1b"));

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateMissingOption() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Arrays.asList(new Option[] { groupOption1 }), null, null, 1, 1);

        commandLine.parse(options.parse(null));
        commandLine.parse(Arrays.asList("g1"));

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateTooManyOptions() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Arrays.asList(new Option[] { groupOption1 }), null, null, 1, 1);

        commandLine.parse(options.parse(null));
        commandLine.parse(Arrays.asList("g1", "o1a", "o1b", "o1c"));

        group.validate(commandLine);
    }

    @Test
    public void testValidateValidUsage() {
        CommandLine commandLine = new CommandLine();
        GroupImpl group = new GroupImpl(Arrays.asList(new Option[] { groupOption1 }), null, null, 1, 1);

        commandLine.parse(options.parse(null));
        commandLine.parse(Arrays.asList("g1", "o1a", "o1b"));

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testHelpLinesBasicUsage() {
        HelpFormatter formatter = new HelpFormatter();

        GroupImpl group = new GroupImpl(Arrays.asList(new Option[] { groupOption1 }), null, null, 1, 1);

        List<String> lines = group.helpLines(0, null, null);

        formatter.printHelp("test", lines.toArray(new String[0]));
    }

    @Test
    public void testHelpLinesExpandedUsage() {
        HelpFormatter formatter = new HelpFormatter();

        GroupImpl group = new GroupImpl(Arrays.asList(new Option[] { groupOption1, groupOption2 }), null, null, 1, 1);

        List<String> lines = group.helpLines(0, null, null);

        formatter.printHelp("test", lines.toArray(new String[0]));
    }
}