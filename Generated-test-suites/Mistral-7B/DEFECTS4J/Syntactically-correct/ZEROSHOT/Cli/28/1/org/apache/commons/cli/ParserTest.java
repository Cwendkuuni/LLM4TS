import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        String[] args = {"--input", "input.txt", "--output", "output.txt", "-o", "out.txt", "--help"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addRequiredOption("o", "output", true, "Output file");
        options.addOption("h", "help", false, "Print help");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, true);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("o"));
        Assert.assertNull(cmd.getOptionValue("help"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws ParseException
    {
        String[] args = {"--input", "input.txt"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addRequiredOption("o", "output", true, "Output file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, true);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        String[] args = {"--unknown"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addRequiredOption("o", "output", true, "Output file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, true);
    }

    @Test
    public void testOptionalOption() throws ParseException
    {
        String[] args = {"--input", "input.txt", "-o", "out.txt"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addOption("o", "output", true, "Output file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, true);

        Assert.assertEquals("out.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("help"));
    }

    @Test
    public void testNoOptions() throws ParseException
    {
        String[] args = {"input.txt", "output.txt"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, false);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("input"));
        Assert.assertTrue(cmd.hasOption("output"));
    }

    @Test
    public void testProperties() throws ParseException
    {
        String[] args = {"--input", "input.txt", "--property1=value1", "--property2=value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addOption("p", true, "Property");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, null, properties, true);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("value1", cmd.getOptionValue("property"));
        Assert.assertEquals("value2", cmd.getOptionValue("property"));
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws ParseException
    {
        String[] args = {"--input", "input.txt", "--output", "output.txt", "unknown"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file");
        options.addRequiredOption("o", "output", true, "Output file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, true);
    }
}