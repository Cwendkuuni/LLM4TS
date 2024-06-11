import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithValidArguments() throws ParseException {
        String[] arguments = {"--optionA=valueA", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        String[] arguments = {"arg1", "arg2"};
        Options options = new Options();

        Parser parser = new DefaultParser();

        Assert.assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, arguments);
        });
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        String[] arguments = {"--invalidOption"};
        Options options = new Options();

        Parser parser = new DefaultParser();

        Assert.assertThrows(UnrecognizedOptionException.class, () -> {
            parser.parse(options, arguments);
        });
    }

    @Test
    public void testParseWithInvalidValueType() throws ParseException {
        String[] arguments = {"--optionA=invalidValue"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            parser.parse(options, arguments);
        });
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] arguments = {"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A")
                .required(true);

        Parser parser = new DefaultParser();

        Assert.assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, arguments);
        });
    }

    @Test
    public void testParseWithExtraneousArguments() throws ParseException {
        String[] arguments = {"--optionA", "arg1", "--optionB=valueB", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("arg1", cmd.getArg(0));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        Assert.assertEquals("arg2", cmd.getArg(2));
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        String[] arguments = {"--optionA", "arg1", "--optionB=valueB", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("arg1", cmd.getArg(0));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        Assert.assertEquals("arg2", cmd.getArg(2));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        String[] arguments = {"--optionA", "--optionB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
    }

    @Test
    public void testParseWithGroupedOptions() throws ParseException {
        String[] arguments = {"--mygroup", "--option1", "--option2"};
        Options options = new Options();
        OptionGroup myGroup = new OptionGroup("mygroup", "My Group");
        myGroup.addOption(new Option("option1", true, "Option 1"));
        myGroup.addOption(new Option("option2", true, "Option 2"));
        options.addOptionGroup(myGroup);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("option1"));
        Assert.assertEquals(true, cmd.hasOption("option2"));
    }
}