import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.builder.ValueBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testValidGroup() {
        // given
        final String[] args = {"-g", "test", "--option1", "value1", "--option2"};
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
                Collections.singletonList(OptionBuilder.withLongOpt("option1")
                        .withDescription("Option 1")
                        .withValueSeparator('=')
                        .isRequired(false)
                        .create("option1"))
                , "test"
                , "Test group"
                , 0
                , 2
                , Collections.emptyList()
                , Collections.emptyMap()
                , Collections.emptySet()
        );

        // when
        final DefaultParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options.toArray(), args);

        // then
        Assert.assertEquals("test", group.getPreferredName());
        Assert.assertEquals("Test group", group.getDescription());
        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertEquals(Option.VALUE_PRESENT, cmd.getOptionValues("option1")[0].length);
        Assert.assertEquals("value1", cmd.getOptionValues("option1")[0]);
        Assert.assertEquals(true, cmd.hasOption("option1"));
        Assert.assertEquals(true, cmd.hasOption("option2"));
    }

    @Test
    public void testGroupWithMinimumConstraint() {
        // given
        final String[] args = {"--option1"};
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
                Collections.singletonList(OptionBuilder.withLongOpt("option1")
                        .withDescription("Option 1")
                        .withValueSeparator('=')
                        .isRequired(true)
                        .create("option1"))
                , "test"
                , "Test group"
                , 1
                , 1
                , Collections.emptyList()
                , Collections.emptyMap()
                , Collections.emptySet()
        );

        // when
        final DefaultParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options.toArray(), args);

        // then
        Assert.assertEquals("test", group.getPreferredName());
        Assert.assertEquals("Test group", group.getDescription());
        Assert.assertEquals(1, group.getOptions().size());
        Assert.assertFalse(cmd.hasOption("option1"));
        Assert.assertEquals(1, cmd.getNumMissing());
        Assert.assertEquals("Test group: Option 1 is required", cmd.getMissingValues().get(0));
    }

    @Test
    public void testGroupWithMaximumConstraint() {
        // given
        final String[] args = {"--option1", "--option1", "--option2"};
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
                Collections.singletonList(OptionBuilder.withLongOpt("option1")
                        .withDescription("Option 1")
                        .withValueSeparator('=')
                        .isRequired(false)
                        .create("option1"))
                , "test"
                , "Test group"
                , 0
                , 1
                , Collections.emptyList()
                , Collections.emptyMap()
                , Collections.emptySet()
        );

        // when
        final DefaultParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options.toArray(), args);

        // then
        Assert.assertEquals("test", group.getPreferredName());
        Assert.assertEquals("Test group", group.getDescription());
        Assert.assertEquals(1, group.getOptions().size());
        Assert.assertEquals(1, cmd.getNumValues("option1"));
        Assert.assertEquals(1, cmd.getNumMissing());
        Assert.assertEquals("Test group: Option 'option1' can only appear 0 times, it appears 2 times", cmd.getMissingValues().get(0));
    }

    @Test
    public void testGroupWithAnonymous() {
        // given
        final String[] args = {"", "--anonymous", "value"};
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
                Collections.singletonList(OptionBuilder.withLongOpt("anonymous")
                        .withDescription("Anonymous option")
                        .withValueSeparator('=')
                        .isRequired(false)
                        .create("anonymous"))
                , "test"
                , "Test group"
                , 0
                , 1
                , Collections.emptyList()
                , Collections.emptyMap()
                , Collections.emptySet()
        );

        // when
        final DefaultParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options.toArray(), args);

        // then
        Assert.assertEquals("test", group.getPreferredName());
        Assert.assertEquals("Test group", group.getDescription());
        Assert.assertEquals(1, group.getOptions().size());
        Assert.assertEquals(1, cmd.getNumValues("anonymous"));
        Assert.assertEquals("value", cmd.getOptionValues("anonymous")[0]);
        Assert.assertEquals(true, cmd.hasOption("anonymous"));
    }

    @Test(expected = OptionException.class)
    public void testGroupWithInvalidArgument() {
        // given
        final String[] args = {"--invalid"};
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
                Collections.emptyList()
                , "test"
                , "Test group"
                , 0
                , 1
                , Collections.emptyList()
                , Collections.emptyMap()
                , Collections.emptySet()
        );

        // when
        final DefaultParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options.toArray(), args);

        // then
        Assert.fail(); // should throw OptionException
    }

    @Test(expected = OptionException.class)
    public void testGroupWithInvalidOptionName() {
        // given
        final String[] args = {"--invalidOptionName"};
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
                Collections.emptyList()
                , "test"
                , "Test group"
                , 0
                , 1
                , Collections.emptyList()
                , Collections.emptyMap()
                , Collections.emptySet()
        );

        // when
        final DefaultParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options.toArray(), args);

        // then
        Assert.fail(); // should throw OptionException
    }

    @Test
    public void testHelp() {
        // given
        final String[] args = {"--help"};
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
                Collections.emptyList()
                , "test"
                , "Test group"
                , 0
                , 1
                , Collections.emptyList()
                , Collections.emptyMap()
                , Collections.emptySet()
        );

        // when
        final DefaultParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options.toArray(), args);

        // then
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "", options, true);

        // verify the output
        final List<String> lines = cmd.getHelpLines(Collections.singletonList(DisplaySetting.DISPLAY_GROUP_NAME), null, null);
        Assert.assertEquals(16, lines.size());
        Assert.assertEquals("group:", lines.get(0));
        Assert.assertEquals("test (Test group)", lines.get(1));
        Assert.assertEquals("  Options:", lines.get(2));
        Assert.assertEquals("", lines.get(3));
        Assert.assertEquals("  Option:", lines.get(4));
        Assert.assertEquals("    Option:", lines.get(5));
        Assert.assertEquals("      optionName (value)", lines.get(6));
        Assert.assertEquals("      Option description", lines.get(7));
        Assert.assertEquals("      Option is required", lines.get(8));
        Assert.assertEquals("      Option is repeatable", lines.get(9));
        Assert.assertEquals("      Option takes a value", lines.get(10));
        Assert.assertEquals("      Option values:", lines.get(11));
        Assert.assertEquals("      Option with a value:", lines.get(12));
        Assert.assertEquals("      Option without a value:", lines.get(13));
        Assert.assertEquals("  Arguments:", lines.get(14));
        Assert.assertEquals("    Argument:", lines.get(15));
        Assert.assertEquals("      argumentName:", lines.get(16));

        // verify the anonymous arguments
        final List<String> anonymousLines = cmd.getHelpLines(Collections.singletonList(DisplaySetting.DISPLAY_GROUP_NAME), null, null, "anonymous");
        Assert.assertEquals(1, anonymousLines.size());
        Assert.assertEquals("  Anonymous arguments:", anonymousLines.get(0));
    }

    @Test
    public void testToString() {
        // given
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
                Collections.singletonList(OptionBuilder.withLongOpt("option1")
                        .withDescription("Option 1")
                        .withValueSeparator('=')
                        .isRequired(false)
                        .create("option1"))
                , "test"
                , "Test group"
                , 0
                , 1
                , Collections.emptyList()
                , Collections.emptyMap()
                , Collections.emptySet()
        );

        // then
        Assert.assertEquals("Test group", group.getPreferredName());
        Assert.assertEquals("Test group", group.toString());
    }

    @Test
    public void testCanProcess() {
        // given
        final Options options = new Options();
        final GroupImpl group = new GroupImpl(
                Collections.singletonList(OptionBuilder.withLongOpt("option1")
                        .withDescription("Option 1")
                        .withValueSeparator('=')
                        .isRequired(false)
                        .create("option1"))
                , "test"
                , "Test group"
                , 0
                , 1
                , Collections.emptyList()
                , Collections.emptyMap()
                , Collections.emptySet()
        );

        final CommandLine cmd = new CommandLine();

        // when
        final boolean result = group.canProcess(cmd, "option1");

        // then
        Assert.assertTrue(result);

        // when
        final boolean result2 = group.canProcess(cmd, "option2");

        // then
        Assert.assertFalse(result2);

        // when
        final boolean result3 = group.canProcess(cmd, "anonymous");

        // then
        Assert.assertTrue(result3);

        // when
        final boolean result4 = group.canProcess(cmd, "test");

        // then
        Assert.assertFalse(result4);
    }
}