import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.exceptions.ExclusiveGroupFailedException;
import org.apache.commons.cli2.exceptions.MissingArgumentException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GroupImplTest {

    @Test
    public void testBasicGroupFunctionality() throws Exception {
        final Options testOptions = new Options();

        final GroupImpl group = new GroupImpl(
                List.of(
                        OptionBuilder.withName("g1")
                                .withDescription("Option 1 for Group G")
                                .create(),
                        OptionBuilder.withName("g2")
                                .withDescription("Option 2 for Group G")
                                .create(),
                        OptionBuilder.withName("a1")
                                .withDescription("Anonymous Argument 1")
                                .withArgName("arg")
                                .isRequired(false)
                                .create()
                ),
                "My Group",
                0,
                1);

        testOptions.addOption(group);

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(testOptions, new String[]{"-g", "g1"});

        Assert.assertTrue("Option g1 should be present", cmd.hasOption("g1"));
        Assert.assertFalse("Option g2 should not be present", cmd.hasOption("g2"));
        Assert.assertNull("Anonymous Argument 1 should be null", cmd.getOptionValues("a1"));

        testOptions.addOption(group);

        final CommandLine cmdWithAllOptions = parser.parse(testOptions, new String[]{"-g", "g1", "--g2", "argForA1"});

        Assert.assertTrue("Option g1 should be present", cmd.hasOption("g1"));
        Assert.assertTrue("Option g2 should be present", cmd.hasOption("g2"));
        Assert.assertEquals("Anonymous Argument 1 should be equal to argForA1", "argForA1", cmd.getOptionValues("a1")[0]);
    }

    @Test
    public void testGroupWithMinMax() throws Exception {
        final Options testOptions = new Options();

        final GroupImpl group = new GroupImpl(
                List.of(
                        OptionBuilder.withName("g1")
                                .withDescription("Option 1 for Group G")
                                .create(),
                        OptionBuilder.withName("g2")
                                .withDescription("Option 2 for Group G")
                                .create(),
                        OptionBuilder.withName("a1")
                                .withDescription("Anonymous Argument 1")
                                .withArgName("arg")
                                .isRequired(false)
                                .create()
                ),
                "My Group",
                1,
                1);

        testOptions.addOption(group);

        Assert.assertThrows(MissingArgumentException.class, () -> {
            parser.parse(testOptions, new String[]{"-g"});
        });

        testOptions.addOption(group);

        final CommandLine cmdWithMinOptions = parser.parse(testOptions, new String[]{"-g", "g1", "--g2", "argForA1"});

        Assert.assertTrue("Option g1 should be present", cmd.hasOption("g1"));
        Assert.assertTrue("Option g2 should be present", cmd.hasOption("g2"));
        Assert.assertEquals("Anonymous Argument 1 should be equal to argForA1", "argForA1", cmd.getOptionValues("a1")[0]);

        testOptions.addOption(group);

        Assert.assertThrows(ExclusiveGroupFailedException.class, () -> {
            parser.parse(testOptions, new String[]{"-g", "g1", "--g1", "argForA1"});
        });

        testOptions.addOption(group);

        final CommandLine cmdWithMaxOptions = parser.parse(testOptions, new String[]{"-g", "g1", "g1", "g2"});

        Assert.assertTrue("Option g1 should be present", cmd.hasOption("g1"));
        Assert.assertTrue("Option g2 should be present", cmd.hasOption("g2"));
        Assert.assertEquals("Anonymous Argument 1 should be null", null, cmd.getOptionValues("a1"));
    }

    @Test
    public void testGroupWithBursting() throws Exception {
        final Options testOptions = new Options();

        final GroupImpl group = new GroupImpl(
                List.of(
                        OptionBuilder.withName("g1")
                                .withDescription("Option 1 for Group G")
                                .create(),
                        OptionBuilder.withName("g2")
                                .withDescription("Option 2 for Group G")
                                .create()
                ),
                "My Group",
                0,
                Integer.MAX_VALUE);

        testOptions.addOption(group);

        final CommandLine cmdWithOneOption = parser.parse(testOptions, new String[]{"-g", "g1"});

        Assert.assertTrue("Option g1 should be present", cmd.hasOption("g1"));
        Assert.assertFalse("Option g2 should not be present", cmd.hasOption("g2"));

        testOptions.addOption(group);

        final CommandLine cmdWithTwoOptions = parser.parse(testOptions, new String[]{"-g", "g1", "--g2"});

        Assert.assertTrue("Option g1 should be present", cmd.hasOption("g1"));
        Assert.assertTrue("Option g2 should be present", cmd.hasOption("g2"));

        testOptions.addOption(group);

        final CommandLine cmdWithMoreThanTwoOptions = parser.parse(testOptions, new String[]{"-g", "g1", "g1", "g2"});

        Assert.assertTrue("Option g1 should be present", cmd.hasOption("g1"));
        Assert.assertTrue("Option g2 should be present", cmd.hasOption("g2"));

        testOptions.addOption(group);

        Assert.assertThrows(ExclusiveGroupFailedException.class, () -> {
            parser.parse(testOptions, new String[]{"-g", "g1", "--g1"});
        });
    }

    @Test
    public void testHelp() throws Exception {
        final Options testOptions = new Options();

        final GroupImpl group = new GroupImpl(
                List.of(
                        OptionBuilder.withName("g1")
                                .withDescription("Option 1 for Group G")
                                .create(),
                        OptionBuilder.withName("g2")
                                .withDescription("Option 2 for Group G")
                                .create()
                ),
                "My Group",
                0,
                1);

        testOptions.addOption(group);

        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("test.jar", testOptions, true, true);

        Assert.assertTrue("Help text should contain 'My Group' ", testOptions.getHelpHeader().contains("My Group"));

        testOptions.addOption(group);

        final String helpText = formatter.printHelp("test.jar", testOptions, true, true);

        Assert.assertFalse("Help text should not contain 'My Group' twice", helpText.contains("My Group") && helpText.contains("My Group") && helpText.contains("My Group"));
    }

    @Test
    public void testHelpWithLongOptions() throws Exception {
        final Options testOptions = new Options();

        final GroupImpl group = new GroupImpl(
                List.of(
                        OptionBuilder.withName("longOption1")
                                .withLongOpt("long-option1")
                                .withDescription("Long Option 1 for Group G")
                                .create(),
                        OptionBuilder.withName("longOption2")
                                .withLongOpt("long-option2")
                                .withDescription("Long Option 2 for Group G")
                                .create()
                ),
                "My Group",
                0,
                1);

        testOptions.addOption(group);

        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("test.jar", testOptions, true, true);

        Assert.assertTrue("Help text should contain 'long-option1' ", testOptions.getHelpHeader().contains("long-option1"));

        testOptions.addOption(group);

        final String helpText = formatter.printHelp("test.jar", testOptions, true, true);

        Assert.assertFalse("Help text should not contain 'long-option1' and 'long-option2' twice", helpText.contains("long-option1") && helpText.contains("long-option1") && helpText.contains("long-option2") && helpText.contains("long-option2"));
    }

    @Test
    public void testHelpWithArguements() throws Exception {
        final Options testOptions = new Options();

        final GroupImpl group = new GroupImpl(
                List.of(
                        OptionBuilder.withName("g")
                                .withDescription("Group with argument")
                                .create(),
                        ArgumentBuilder.newLongOpt("arg")
                                .withDescription("An argument")
                                .isRequired(true)
                                .create()
                ),
                "My Group",
                0,
                1);

        testOptions.addOption(group);

        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("test.jar", testOptions, true, true);

        Assert.assertTrue("Help text should contain 'arg' ", testOptions.getHelpHeader().contains("arg"));

        testOptions.addOption(group);

        final String helpText = formatter.printHelp("test.jar", testOptions, true, true);

        Assert.assertFalse("Help text should not contain 'arg' and 'arg' twice", helpText.contains("arg") && helpText.contains("arg"));
    }

    @Test
    public void testHelpWithAnonymousArgument() throws Exception {
        final Options testOptions = new Options();

        final GroupImpl group = new GroupImpl(
                List.of(
                        OptionBuilder.withName("g")
                                .withDescription("Group with anonymous argument")
                                .create(),
                        ArgumentBuilder.newLongOpt("a")
                                .withDescription("An anonymous argument")
                                .isRequired(false)
                                .create()
                ),
                "My Group",
                0,
                1);

        testOptions.addOption(group);

        final HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("test.jar", testOptions, true, true);

        Assert.assertTrue("Help text should contain 'a' ", testOptions.getHelpHeader().contains("a"));

        testOptions.addOption(group);

        final String helpText = formatter.printHelp("test.jar", testOptions, true, true);

        Assert.assertFalse("Help text should not contain 'a' and 'a' twice", helpText.contains("a") && helpText.contains("a"));
    }
}