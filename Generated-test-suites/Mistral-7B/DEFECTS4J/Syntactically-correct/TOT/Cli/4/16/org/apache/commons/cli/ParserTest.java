import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseWithSimpleOptions() throws ParseException {
        String[] args =new String[]{"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertEquals("1", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] args =new String[]{"--input"};
        Options options = new Options();
        options.addRequiredOption("input", true, "Input file");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertNull(cmd.getArgs());
        assertTrue(cmd.hasOption("input"));
        assertNull(cmd.getOptionValue("input"));

        try {
            cmd = parser.parse(options, args, false);
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: 'input'", e.getMessage());
            assertArrayEquals(new String[]{"--input"}, cmd.getUnrecognizedOptions());
        }
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        String[] args =new String[]{"-a", "1", "-b", "2", "--input", "data.txt"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("input", true, "Input file");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertTrue(cmd.hasOption("input"));
        assertEquals("data.txt", cmd.getOptionValue("input"));
        assertArrayEquals(new String[]{}, cmd.getArgs());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] args =new String[]{"--unknown"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.parse(options, args, false);

        fail("Should throw UnrecognizedOptionException");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        String[] args =new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("b"));
        assertArrayEquals(new String[]{}, cmd.getArgs());

        try {
            cmd = parser.parse(options, args, false);
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option: b", e.getMessage());
            fail("Should not get here");
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args =new String[]{"--property", "file.name=test.txt"};
        Properties properties = new Properties();
        properties.setProperty("file.name", "test.txt");

        Options options = new Options();
        options.addOption("property", true, "Property file");
        options.addOption("file", true, "File");
        options.addOption("help", false);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertTrue(cmd.hasOption("property"));
        assertEquals("test.txt", cmd.getOptionValue("property"));
        assertTrue(cmd.hasOption("file"));
        assertNull(cmd.getOptionValue("file"));
        assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithDashBeforeOption() throws ParseException {
        String[] args =new String[]{"--", "-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertEquals("1", cmd.getOptionValue("a"));
    }
}