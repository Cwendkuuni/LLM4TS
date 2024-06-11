import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionWithNoPrefixes() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .withDescription("An example option")
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(cmd, new String[]{"--example"});

        Assert.assertTrue(option.isSet(cmd));
    }

    @Test
    public void testOptionWithPrefixes() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .withDescription("An example option")
                .withPrefixes(new String[]{"ex", "e"})
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(cmd, new String[]{"--ex", "example"});

        Assert.assertTrue(option.isSet(cmd));
    }

    @Test(expected = ParseException.class)
    public void testOptionWithInvalidPrefix() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .withDescription("An example option")
                .withPrefixes(new String[]{"in", "v"})
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(cmd, new String[]{"--example"});

        Assert.assertFalse(option.isSet(cmd));

        parser.parse(cmd, new String[]{"--in-example"});

        Assert.fail("ParseException expected but not thrown");
    }

    @Test
    public void testOptionWithDescription() {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .withDescription("An example option")
                .build();

        Assert.assertEquals("An example option", option.getDescription());
    }

    @Test
    public void testOptionIsRequired() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .isRequired()
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        try {
            parser.parse(cmd, new String[0]);
        } catch (ParseException e) {
            Assert.assertEquals("option 'example' is missing", e.getMessage());
        }
    }

    @Test
    public void testOptionIsNotRequired() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(cmd, new String[0]);

        Assert.assertFalse(option.isSet(cmd));
    }

    @Test
    public void testOptionWithValue() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .withValueSeparator('=')
                .withValueSeparator(':')
                .isRequired()
                .hasArg()
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(cmd, new String[]{"--example", "value"});

        Assert.assertEquals("value", option.getValue(cmd));
    }

    @Test(expected = ParseException.class)
    public void testOptionWithInvalidValueSeparator() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .withValueSeparator('+')
                .isRequired()
                .hasArg()
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(cmd, new String[]{"--example", "value1", "value2"});

        Assert.fail("ParseException expected but not thrown");
    }

    @Test
    public void testOptionWithMultipleValues() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .withValueSeparator(',')
                .isRequired()
                .hasArgs()
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(cmd, new String[]{"--example", "value1,value2"});

        String[] values = option.getValues(cmd);

        Assert.assertEquals(2, values.length);
        Assert.assertEquals("value1", values[0]);
        Assert.assertEquals("value2", values[1]);
    }

    @Test
    public void testOptionWithNoValues() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .isRequired()
                .hasArg(false)
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(cmd, new String[0]);

        Assert.assertNull(option.getValue(cmd));
    }

    @Test
    public void testOptionWithConflictingValues() throws ParseException {
        Option option1 = new OptionBuilder()
                .withLongOpt("example1")
                .isRequired()
                .hasArg()
                .build();

        Option option2 = new OptionBuilder()
                .withLongOpt("example2")
                .isRequired()
                .hasArg()
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        try {
            parser.parse(cmd, new String[]{"--example1", "value1", "--example2", "value2"});
        } catch (ParseException e) {
            Assert.assertEquals("The following conflicts: example1, example2", e.getMessage());
        }
    }

    @Test
    public void testOptionWithMultipleInstances() throws ParseException {
        Option option1 = new OptionBuilder()
                .withLongOpt("example1")
                .isRequired()
                .hasArg()
                .build();

        Option option2 = new OptionBuilder()
                .withLongOpt("example2")
                .isRequired()
                .hasArg()
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(cmd, new String[]{"--example1", "value1", "--example2", "value2"});

        Assert.assertEquals("value1", option1.getValue(cmd));
        Assert.assertEquals("value2", option2.getValue(cmd));
    }

    @Test
    public void testHelp() {
        Option option = new OptionBuilder()
                .withLongOpt("example")
                .withDescription("An example option")
                .build();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "1.0", true, "Test program", null, false,
                null, option.toString(), null);

        String help = formatter.getHelpMessage();

        Assert.assertTrue(help.contains("An example option"));
        Assert.assertTrue(help.contains("--example value"));
    }
}