import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testParseWithEmptyArguments() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        String[] arguments = {};

        CommandLine commandLine = parser.parse(options, arguments);

        assertNull(commandLine.getArgs());
        assertTrue(commandLine.getOptions().isEmpty());
    }

    @Test
    public void testParseWithArguments() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        String[] arguments = {"arg1", "arg2", "--option"};

        CommandLine commandLine = parser.parse(options, arguments);

        List<Option> optionsList = commandLine.getOptions();
        Option option = optionsList.get(0);

        assertEquals("option", option.getKey());
        assertNull(option.getValue());
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingArgument() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        String[] arguments = {"arg1", "--option", "arg3"};

        parser.parse(options, arguments);

        fail("ParseException expected");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("option", true, "o");

        String[] arguments = {"arg1"};

        parser.parse(options, arguments);

        fail("MissingOptionException expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        String[] arguments = {"--unknown"};

        parser.parse(options, arguments);

        fail("UnrecognizedOptionException expected");
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");

        String[] arguments = {"arg1", "--option1=value3"};

        CommandLine commandLine = parser.parse(options, arguments, properties, false);

        List<Option> optionsList = commandLine.getOptions();
        Option option1 = optionsList.get(0);
        Option option2 = optionsList.get(1);

        assertEquals("option1", option1.getKey());
        assertEquals("value3", option1.getValue());
        assertEquals("option2", option2.getKey());
        assertNull(option2.getValue());
        assertEquals(0, commandLine.getArgs().length);
    }
}