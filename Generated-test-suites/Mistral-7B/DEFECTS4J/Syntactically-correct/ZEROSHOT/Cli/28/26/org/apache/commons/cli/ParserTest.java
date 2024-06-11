import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");

        String[] arguments = new String[]{"-a", "--b", "2", "-c", "3", "--d", "4", "--e", "5"};

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(false, cmd.hasOption("z"));
        Assert.assertEquals(false, cmd.hasOption("b", true));
        Assert.assertEquals((Integer) 2, cmd.getOptionValue("b"));
        Assert.assertEquals((Integer) 3, cmd.getOptionValue("c"));
        Assert.assertEquals((Integer) 4, cmd.getOptionValue("d"));
        Assert.assertEquals((Integer) 5, cmd.getOptionValue("e"));
        Assert.assertEquals(null, cmd.getOptionValue("f"));
        Assert.assertEquals(null, cmd.getArgs());

        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E", true);

        arguments = new String[]{"-a", "--b", "2", "-c", "3", "--d", "4", "--e", "5", "6"};

        cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(false, cmd.hasOption("z"));
        Assert.assertEquals(false, cmd.hasOption("b", true));
        Assert.assertEquals((Integer) 2, cmd.getOptionValue("b"));
        Assert.assertEquals((Integer) 3, cmd.getOptionValue("c"));
        Assert.assertEquals((Integer) 4, cmd.getOptionValue("d"));
        Assert.assertEquals((Integer) 5, cmd.getOptionValue("e"));
        Assert.assertEquals((List) Arrays.asList("6"), cmd.getArgs());

        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");
        options.addOptionGroup(new OptionGroup("g", "Option Group", "Option Group description"));
        options.addOption("h", true, "Option H", options.getOptionGroup("g"));
        options.addOption("i", true, "Option I", options.getOptionGroup("g"));

        arguments = new String[]{"-a", "--b", "2", "--g", "--h", "--i"};

        cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(false, cmd.hasOption("z"));
        Assert.assertEquals(false, cmd.hasOption("b", true));
        Assert.assertEquals((Integer) 2, cmd.getOptionValue("b"));
        Assert.assertEquals(true, cmd.hasOption("h"));
        Assert.assertEquals(true, cmd.hasOption("i"));
        Assert.assertEquals(null, cmd.getOptionValue("f"));
        Assert.assertEquals(null, cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = new String[]{"--b"};

        DefaultParser parser = new DefaultParser();
        parser.parse(options, arguments, true);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException, MissingOptionException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", false, "Option B");

        String[] arguments = new String[]{"-a"};

        DefaultParser parser = new DefaultParser();
        parser.parse(options, arguments, true);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException, UnrecognizedOptionException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = new String[]{"--x"};

        DefaultParser parser = new DefaultParser();
        parser.parse(options, arguments, true);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException, MissingArgumentException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] arguments = new String[]{"-a"};

        DefaultParser parser = new DefaultParser();
        parser.parse(options, arguments, true);
    }
}