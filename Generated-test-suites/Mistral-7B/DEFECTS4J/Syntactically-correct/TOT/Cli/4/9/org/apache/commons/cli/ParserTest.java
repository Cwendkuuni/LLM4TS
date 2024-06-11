import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserTest {

    @Test
    public void testParseSimpleCase() throws ParseException {
        String[] args = new String[]{"-a", "arg1", "--optionB", "valB"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLine cmd = parser.parse(options, args, false);

        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals("valB", cmd.getOptionValue("b"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingRequiredArgument() throws ParseException {
        String[] args = new String[]{"-a"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "argB");

        parser.parse(options, args, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] args = new String[]{"--optionB", "valB"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        parser.parse(options, args, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"--unknownOption", "value"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        parser.parse(options, args, false);
    }

    @Test
    public void testParseOptionsWithValues() throws ParseException {
        String[] args = new String[]{"-a", "arg1", "--optionB=valB"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLine cmd = parser.parse(options, args, false);

        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals("valB", cmd.getOptionValue("b"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParseOptionsWithGroup() throws ParseException {
        String[] args = new String[]{"-g", "MyGroup", "--optionA", "valueA", "--optionB", "valueB"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group = new OptionGroup("MyGroup", "Group of options");
        group.addOption(new Option("a", true, "Option A"));
        group.addOption(new Option("b", true, "Option B"));
        options.addOptionGroup(group);

        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(group, cmd.getOptionGroup());
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParseOptionsWithProperties() throws ParseException {
        String[] args = new String[]{"--propertyFile", "/path/to/file.properties"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("pf", true, "propertyFile");

        Properties properties = new Properties();
        properties.load(ParserTest.class.getResourceAsStream("/test.properties"));

        CommandLine cmd = parser.parse(options, null, properties, false);

        assertEquals("/path/to/file.properties", cmd.getOptionValue("pf"));
        assertEquals(0, cmd.getArgs().length);

        String value = properties.getProperty("test.option");
        assertEquals("testValue", cmd.getOptionValue("test.option"));
    }

    @Test
    public void testParseHelp() throws ParseException {
        String[] args = new String[]{"--help"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        CommandLine cmd = parser.parse(options, args, false);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java", options);
    }

    @Test
    public void testParseWithInvalidArguments() throws ParseException {
        String[] args = new String[]{"--invalidOption=value", "arg1"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        try {
            parser.parse(options, args, false);
            assertNull(true);
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseWithInvalidArgumentsAndInvalidOption() throws ParseException {
        String[] args = new String[]{"--invalidOption", "arg1"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        try {
            parser.parse(options, args, false);
            assertNull(true);
        } catch (ParseException e) {
            // expected
        }
    }
}