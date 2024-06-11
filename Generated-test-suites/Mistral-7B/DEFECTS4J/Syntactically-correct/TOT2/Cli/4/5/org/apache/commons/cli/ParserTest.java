import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseWithNoArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertNull(parser.getOptions());
    }

    @Test
    public void testParseWithOptionalSingleArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals(option, options.getOptions().get(0));
        Assert.assertEquals(1, option.getValues().length);
        Assert.assertEquals("value", option.getValues()[0]);
    }

    @Test
    public void testParseWithOptionalMultiArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-o", "value1", "-o", "value2"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getArgs().get(0));
        Assert.assertEquals("value2", cmd.getArgs().get(1));
        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals(option, options.getOptions().get(0));
        Assert.assertEquals(2, option.getValues().length);
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testParseWithRequiredSingleArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();

        String[] arguments = new String[0];
        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (MissingArgumentException e) {
            Assert.assertEquals("Missing argument for option: option", e.getMessage());
        }

        arguments = new String[]{"-o"};
        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (MissingArgumentException e) {
            Assert.assertEquals("Missing argument for option: option", e.getMessage());
        }

        arguments = new String[]{"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals(option, options.getOptions().get(0));
        Assert.assertEquals(1, option.getValues().length);
        Assert.assertEquals("value", option.getValues()[0]);
    }

    @Test
    public void testParseWithRequiredMultiArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();

        String[] arguments = new String[0];
        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (MissingArgumentException e) {
            Assert.assertEquals("Missing argument for option: option", e.getMessage());
        }

        arguments = new String[]{"-o", "value1"};
        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (MissingArgumentException e) {
            Assert.assertEquals("Missing argument for option: option", e.getMessage());
        }

        arguments = new String[]{"-o", "value1", "-o", "value2"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getArgs().get(0));
        Assert.assertEquals("value2", cmd.getArgs().get(1));
        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals(option, options.getOptions().get(0));
        Assert.assertEquals(2, option.getValues().length);
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testParseWithShortOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals(option, options.getOptions().get(0));
        Assert.assertEquals(1, option.getValues().length);
        Assert.assertEquals("value", option.getValues()[0]);
    }

    @Test
    public void testParseWithLongOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("longOption", "long option", true, "Long option value");
        options.addOption(option);
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"--longOption", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals(option, options.getOptions().get(0));
        Assert.assertEquals(1, option.getValues().length);
        Assert.assertEquals("value", option.getValues()[0]);
    }

    @Test
    public void testParseWithGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        Option option = new Option("o", "option", true, "Option value");
        group.addOption(option);
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"--group", "-o", "value"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getArgs().get(0));
        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals(group, options.getOptions().get(0));
        Assert.assertEquals(1, group.getOptions().size());
        Assert.assertEquals(option, group.getOptions().get(0));
        Assert.assertEquals(1, option.getValues().length);
        Assert.assertEquals("value", option.getValues()[0]);
    }

    @Test
    public void testParseWithMixedOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "Option1 value");
        Option option2 = new Option("o2", "option2", true, "Option2 value");
        Option optionGroup = new OptionGroup("group", "Group");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"--group", "-o1", "value1", "--group", "-o2", "value2"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getArgs().get(0));
        Assert.assertEquals("value2", cmd.getArgs().get(1));
        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals(optionGroup, options.getOptions().get(0));
        Assert.assertEquals(2, optionGroup.getOptions().size());
        Assert.assertEquals(option1, optionGroup.getOptions().get(0));
        Assert.assertEquals(1, option1.getValues().length);
        Assert.assertEquals("value1", option1.getValues()[0]);
        Assert.assertEquals(option2, optionGroup.getOptions().get(1));
        Assert.assertEquals(1, option2.getValues().length);
        Assert.assertEquals("value2", option2.getValues()[0]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-x", "value"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();

        String[] arguments = new String[0];
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingOptionArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-o"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionValue() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option value");
        options.addOption(option);
        Parser parser = new DefaultParser();

        String[] arguments = new String[]{"-o", "value;value"};
        parser.parse(options, arguments);
    }
}