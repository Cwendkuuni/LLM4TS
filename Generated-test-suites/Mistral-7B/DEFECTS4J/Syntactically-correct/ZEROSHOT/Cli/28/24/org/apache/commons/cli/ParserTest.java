import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setup()
    {
        options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @After
    public void tearDown()
    {
        parser = null;
        options = null;
    }

    @Test
    public void testParseWithNoArguments() throws ParseException
    {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertNull(cmd.getArgs());
        assertFalse(cmd.hasOptions());
    }

    @Test
    public void testParseWithInputOption() throws ParseException
    {
        String[] arguments = new String[]{"-i", "input.txt"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd.getArgs());
        assertEquals(1, cmd.getArgs().length);
        assertEquals("input.txt", cmd.getArgs()[0]);
        assertTrue(cmd.hasOption("input"));
        assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testParseWithInputAndOutputOptions() throws ParseException
    {
        String[] arguments = new String[]{"-i", "input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, arguments);
        assertNotNull(cmd.getArgs());
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.hasOption("input"));
        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertTrue(cmd.hasOption("output"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        String[] arguments = new String[]{"-x", "value"};
        parser.parse(options, arguments);
        fail("UnrecognizedOptionException expected");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingInputOption() throws ParseException
    {
        String[] arguments = new String[]{"-i"};
        parser.parse(options, arguments);
        fail("MissingArgumentException expected");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptions() throws ParseException
    {
        String[] arguments = new String[]{"-i", "input.txt", "-o"};
        parser.parse(options, arguments);
        fail("MissingOptionException expected");
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        options.addOption("p", "prefix", true, "prefix");
        Properties properties = new Properties();
        properties.setProperty("prefix", "test");

        String[] arguments = new String[]{"--prefix", "test"};
        CommandLine cmd = parser.parse(options, arguments, properties, false);
        assertTrue(cmd.hasOption("prefix"));
        assertEquals("test", cmd.getOptionValue("prefix"));

        properties.clear();
        properties.setProperty("prefix", "newTest");

        cmd = parser.parse(options, arguments, properties, false);
        assertTrue(cmd.hasOption("prefix"));
        assertEquals("newTest", cmd.getOptionValue("prefix"));
    }

    @Test
    public void testParseWithBooleanOption() throws ParseException
    {
        options.addOption("b", "boolean", false, "boolean option");

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertFalse(cmd.hasOption("boolean"));

        arguments = new String[]{"-b"};
        cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("boolean"));

        arguments = new String[]{"-b", "true"};
        cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("boolean"));
        assertTrue(cmd.getBooleanValue("boolean"));

        arguments = new String[]{"-b", "false"};
        cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("boolean"));
        assertFalse(cmd.getBooleanValue("boolean"));
    }
}