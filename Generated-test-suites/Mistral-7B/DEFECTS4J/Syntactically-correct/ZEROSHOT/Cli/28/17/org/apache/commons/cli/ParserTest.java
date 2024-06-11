import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testParser() throws Exception
    {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");
        options.addOption("h", "help", false, "print this help message.");
        options.addOption("v", "verbose", false, "verbose mode.");
        options.addOptionGroup(new OptionGroup("pg", "processing")
                .addOption("p", "processing", false, "processing flag")
                .addOption("s", "skip", true, "number of lines to skip")
                .addOption("e", "encoding", true, "input file encoding"));

        Parser parser = new DefaultParser();
        String[] args = {"-i", "input.txt", "-o", "output.txt", "-h"};
        CommandLine cmd = parser.parse(options, args, null);

        assertTrue(cmd.hasOption("i"));
        assertEquals("input.txt", cmd.getOptionValue("i"));
        assertFalse(cmd.hasOption("o"));
        assertNull(cmd.getOptionValue("o"));
        assertTrue(cmd.hasOption("h"));
        assertFalse(cmd.hasOption("p"));
        assertFalse(cmd.hasOption("s"));
        assertFalse(cmd.hasOption("e"));
        assertNull(cmd.getOptionValue("p"));
        assertNull(cmd.getOptionValue("s"));
        assertNull(cmd.getOptionValue("e"));

        args = new String[]{"-i", "input.txt", "-o", "output.txt", "--help"};
        try
        {
            cmd = parser.parse(options, args, null);
            fail();
        }
        catch (UnrecognizedOptionException e)
        {
            assertEquals("Unrecognized option: help", e.getMessage());
        }

        args = new String[]{"-i", "input.txt", "--help"};
        try
        {
            cmd = parser.parse(options, args, null);
            fail();
        }
        catch (MissingOptionException e)
        {
            assertEquals("Missing required option 'o'", e.getMessage());
        }

        args = new String[]{"-i", "input.txt", "-o", "output.txt", "-v", "--help"};
        cmd = parser.parse(options, args, null);
        assertTrue(cmd.hasOption("i"));
        assertTrue(cmd.hasOption("o"));
        assertFalse(cmd.hasOption("h"));
        assertTrue(cmd.hasOption("v"));
        assertEquals("input.txt", cmd.getOptionValue("i"));
        assertEquals("output.txt", cmd.getOptionValue("o"));
        assertFalse(cmd.hasOption("p"));
        assertFalse(cmd.hasOption("s"));
        assertFalse(cmd.hasOption("e"));
        assertFalse(cmd.getOptionValue("p") == null);

        args = new String[]{"-i", "input.txt", "-o", "output.txt", "--processing", "-s", "1", "--encoding", "UTF-8"};
        cmd = parser.parse(options, args, null);
        assertTrue(cmd.hasOption("i"));
        assertTrue(cmd.hasOption("o"));
        assertTrue(cmd.hasOption("p"));
        assertTrue(cmd.hasOption("s"));
        assertTrue(cmd.hasOption("e"));
        assertEquals("input.txt", cmd.getOptionValue("i"));
        assertEquals("output.txt", cmd.getOptionValue("o"));
        assertFalse(cmd.getOptionValue("p") == null);
        assertEquals("UTF-8", cmd.getOptionValue("e"));
        assertEquals("1", cmd.getOptionValue("s"));

        args = new String[]{"-i", "input.txt", "-o", "output.txt", "--processing", "10"};
        try
        {
            cmd = parser.parse(options, args, null);
            fail();
        }
        catch (MissingArgumentException e)
        {
            assertEquals("Option '-s' requires an argument.", e.getMessage());
        }

        args = new String[]{"-i", "input.txt", "--processing", "10"};
        try
        {
            cmd = parser.parse(options, args, null);
            fail();
        }
        catch (MissingArgumentException e)
        {
            assertEquals("Option '-s' requires an argument.", e.getMessage());
        }

        args = new String[]{"-i", "input.txt", "--processing", "10", "output.txt"};
        cmd = parser.parse(options, args, null);
        assertTrue(cmd.hasOption("i"));
        assertTrue(cmd.hasOption("p"));
        assertFalse(cmd.hasOption("s"));
        assertFalse(cmd.hasOption("e"));
        assertEquals("input.txt", cmd.getOptionValue("i"));
        assertEquals(10, cmd.getOptionInt("p"));
        assertNull(cmd.getOptionValue("s"));
        assertNull(cmd.getOptionValue("e"));
        assertFalse(cmd.hasOption("o"));

        args = new String[]{"-i", "-o", "input.txt", "output.txt"};
        try
        {
            cmd = parser.parse(options, args, null);
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }

        args = new String[]{"-i", "--input", "input.txt", "output.txt"};
        cmd = parser.parse(options, args, null);
        assertTrue(cmd.hasOption("i"));
        assertTrue(cmd.hasOption("input"));
        assertEquals("input.txt", cmd.getOptionValue("i"));
        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertFalse(cmd.hasOption("o"));

        args = new String[]{"--processing", "-p", "10", "input.txt", "output.txt"};
        cmd = parser.parse(options, args, null);
        assertTrue(cmd.hasOption("p"));
        assertTrue(cmd.hasOption("processing"));
        assertEquals(10, cmd.getOptionInt("p"));
        assertFalse(cmd.hasOption("i"));
        assertFalse(cmd.hasOption("o"));

        args = new String[]{"--help", "-i", "input.txt", "output.txt"};
        try
        {
            cmd = parser.parse(options, args, null);
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }

        args = new String[]{"--help", "--help", "-i", "input.txt", "output.txt"};
        try
        {
            cmd = parser.parse(options, args, null);
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }

        args = new String[]{"--help", "-i", "input.txt", "output.txt", "--help"};
        try
        {
            cmd = parser.parse(options, args, null);
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }

        args = new String[]{"--help", "-i", "input.txt", "output.txt", "--help", "--help"};
        try
        {
            cmd = parser.parse(options, args, null);
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }
}