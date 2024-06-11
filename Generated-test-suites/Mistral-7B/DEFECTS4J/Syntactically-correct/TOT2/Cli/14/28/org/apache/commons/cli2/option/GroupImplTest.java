import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupImplWithBasicOptions() throws ParseException {
        final String[] args = new String[]{"--groupOption1", "--groupOption2"};
        final Options options = new Options();
        final Group group = new GroupImpl(Arrays.asList(
                new Option("groupOption1", "an option for the group")),
                "Test Group",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet()
        );
        options.addOptionGroup(group);
        options.addOption(new Option("anotherOption", "another option"));

        final CommandLine cmd = new CommandLine();
        options.parse(cmd, args);

        Assert.assertEquals("groupOption1", cmd.getOptionValues("groupOption1")[0]);
        Assert.assertEquals("groupOption2", cmd.getOptionValues("groupOption2")[0]);
        Assert.assertNull(cmd.getOptionValues("anotherOption"));
    }

    @Test(expected = ParseException.class)
    public void testGroupImplWithTooFewOptions() throws ParseException {
        final String[] args = new String[]{"--groupOption1"};
        final Options options = new Options();
        final Group group = new GroupImpl(Arrays.asList(
                new Option("groupOption1", "an option for the group")),
                "Test Group",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet()
        );
        options.addOptionGroup(group);

        final CommandLine cmd = new CommandLine();
        options.parse(cmd, args);

        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testGroupImplWithTooManyOptions() throws ParseException {
        final String[] args = new String[]{"--groupOption1", "--groupOption1", "--groupOption2"};
        final Options options = new Options();
        final Group group = new GroupImpl(Arrays.asList(
                new Option("groupOption1", "an option for the group")),
                "Test Group",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet()
        );
        options.addOptionGroup(group);

        final CommandLine cmd = new CommandLine();
        options.parse(cmd, args);

        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testGroupImplWithInvalidOption() throws ParseException {
        final String[] args = new String[]{"--invalidOption"};
        final Options options = new Options();
        final Group group = new GroupImpl(Arrays.asList(
                new Option("groupOption1", "an option for the group")),
                "Test Group",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet()
        );
        options.addOptionGroup(group);

        final CommandLine cmd = new CommandLine();
        options.parse(cmd, args);

        Assert.fail();
    }

    @Test
    public void testGroupImplWithAnonymousArgument() throws ParseException {
        final String[] args = new String[]{"--groupOption1", "--groupArgument"};
        final Options options = new Options();
        final Group group = new GroupImpl(Collections.emptyList(),
                "Test Group",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet());
        group.addOption(new Option("groupOption1", "an option for the group"));
        group.addOption(new Option("groupArgument", "an argument for the group")
                .argName("argumentValue"));
        options.addOptionGroup(group);

        final CommandLine cmd = new CommandLine();
        options.parse(cmd, args);

        Assert.assertEquals("argumentValue", cmd.getOptionValues("groupArgument")[0]);
    }

    @Test(expected = ParseException.class)
    public void testGroupImplWithMissingAnonymousArgument() throws ParseException {
        final String[] args = new String[]{"--groupOption1"};
        final Options options = new Options();
        final Group group = new GroupImpl(Collections.emptyList(),
                "Test Group",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet());
        group.addOption(new Option("groupOption1", "an option for the group"));
        group.addOption(new Option("groupArgument", "an argument for the group")
                .argName("argumentValue"));
        options.addOptionGroup(group);

        final CommandLine cmd = new CommandLine();
        options.parse(cmd, args);

        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testGroupImplWithInvalidAnonymousArgument() throws ParseException {
        final String[] args = new String[]{"--groupOption1", "--groupArgumentInvalid"};
        final Options options = new Options();
        final Group group = new GroupImpl(Collections.emptyList(),
                "Test Group",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet());
        group.addOption(new Option("groupOption1", "an option for the group"));
        group.addOption(new Option("groupArgument", "an argument for the group")
                .argName("argumentValue").isRequired(true));
        options.addOptionGroup(group);

        final CommandLine cmd = new CommandLine();
        options.parse(cmd, args);

        Assert.fail();
    }

    @Test
    public void testHelp() throws ParseException {
        final String[] args = new String[]{"--help"};
        final Options options = new Options();
        final Group group = new GroupImpl(Collections.emptyList(),
                "Test Group",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet());
        group.addOption(new Option("groupOption1", "an option for the group"));
        group.addOption(new Option("groupArgument", "an argument for the group")
                .argName("argumentValue"));
        options.addOptionGroup(group);
        options.addOption(new Option("help", "show this help message and exit"));

        final CommandLine cmd = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();
        options.parse(cmd, args);

        formatter.printHelp("Test", options);
    }

    @Test
    public void testHelpWithLongOptions() throws ParseException {
        final String[] args = new String[]{"--long-option-name"};
        final Options options = new Options();
        final Group group = new GroupImpl(Collections.emptyList(),
                "Test Group",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet());
        group.addOption(new Option("groupOption1", "an option for the group"));
        group.addOption(new Option("groupArgument", "an argument for the group")
                .argName("argumentValue"));
        options.addOptionGroup(group);
        options.addOption(new Option("long-option-name", "a long option name")
                .longOpt("longOptionName")
                .desc("A long description for the option"));

        final CommandLine cmd = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();
        options.parse(cmd, args);

        formatter.printHelp("Test", options);
    }

    @Test
    public void testHelpWithBurstedOptions() throws ParseException {
        final String[] args = new String[]{"--groupOption1", "--anotherGroupOption"};
        final Options options = new Options();
        final Group group1 = new GroupImpl(Arrays.asList(
                new Option("groupOption1", "an option for the group1")),
                "Group 1",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet()
        );
        final Group group2 = new GroupImpl(Arrays.asList(
                new Option("anotherGroupOption", "an option for group2")),
                "Group 2",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet()
        );
        group1.addOption(group2);
        options.addOptionGroup(group1);

        final CommandLine cmd = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();
        options.parse(cmd, args);

        formatter.printHelp("Test", options);
    }

    @Test
    public void testHelpWithMixedLongAndShortOptions() throws ParseException {
        final String[] args = new String[]{"--long-option-name", "-s", "--short-option"};
        final Options options = new Options();
        final Group group = new GroupImpl(Collections.emptyList(),
                "Test Group",
                null,
                1,
                1,
                Collections.emptyList(),
                new TreeMap<>(),
                Collections.emptySet());
        group.addOption(new Option("long-option-name", "a long option name")
                .longOpt("longOptionName")
                .desc("A long description for the option"));
        group.addOption(new Option("s", "short option")
                .desc("A short description for the option"));
        options.addOptionGroup(group);
        options.addOption(new Option("short-option", "another short option")
                .shortOpt("o")
                .desc("Another short description for the option"));

        final CommandLine cmd = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();
        options.parse(cmd, args);

        formatter.printHelp("Test", options);
    }
}