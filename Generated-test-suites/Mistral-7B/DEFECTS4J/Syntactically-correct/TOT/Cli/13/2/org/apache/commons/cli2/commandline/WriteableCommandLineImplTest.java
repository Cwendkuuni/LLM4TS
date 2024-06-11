import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", true, "Set alpha value");
        options.addOption("b", "beta", true, "Set beta value");
        options.addOption("c", "char", 'c', "Set char value");
        options.addOption("d", "delta", 1, "Set delta value");

        WriteableCommandLineParser parser = new WriteableCommandLineParser();
        WriteableCommandLine cmd = parser.parse(options, new String[]{"-a", "1", "-b", "test", "--char", "x"});

        Assert.assertEquals(1, cmd.getValues(options.getOption("a"), null).size());
        Assert.assertEquals("1", cmd.getValues(options.getOption("a"), null).get(0));

        Assert.assertEquals("test", cmd.getValues(options.getOption("b"), null).get(0));

        Assert.assertEquals('x', cmd.getSwitch(options.getOption("char"), null).booleanValue());

        Assert.assertEquals(1, cmd.getValues(options.getOption("d"), null).size());
        Assert.assertEquals("1", cmd.getValues(options.getOption("d"), null).get(0).toString());

        Assert.assertFalse(cmd.hasOption(options.getOption("gamma")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSwitch() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", true, "Set alpha value");
        options.addOption("b", "beta", true, "Set beta value");
        options.addOption("c", "char", 'c', "Set char value");
        options.addOption("d", "delta", 1, "Set delta value");

        WriteableCommandLineParser parser = new WriteableCommandLineParser();
        WriteableCommandLine cmd = parser.parse(options, new String[]{"-a", "1", "-b", "test", "--char", "x", "--gamma"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", true, "Set alpha value");
        options.addOption("b", "beta", true, "Set beta value");
        options.addOption("c", "char", 'c', "Set char value");
        options.addOption("d", "delta", 1, "Set delta value");

        WriteableCommandLineParser parser = new WriteableCommandLineParser();
        WriteableCommandLine cmd = parser.parse(options, new String[]{"-a", "1", "-b", "test", "--char", "x", "-d", "apple"});
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "alpha", true, "Set alpha value");
        options.addOption("b", "beta", true, "Set beta value");
        options.addOption("c", "char", 'c', "Set char value");
        options.addOption("d", "delta", 1, "Set delta value");

        WriteableCommandLineParser parser = new WriteableCommandLineParser();
        WriteableCommandLine cmd = parser.parse(options, new String[]{"-a", "1", "-c"});
    }
}