package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link PosixParser}.
 */
public class PosixParserTest
{
    @Test
    public void testSimpleOptions() throws ParseException
    {
        String[] args = {"-a", "--optionB=valueB", "-c", "--optionD"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        assertEquals(args.length, parsedArgs.length);
        assertEquals("--optionB=valueB", parsedArgs[1]);
        assertTrue(Arrays.asList(parsedArgs[2]).contains("-c"));
        assertTrue(Arrays.asList(parsedArgs[3]).contains("--optionD"));
    }

    @Test(expected = ParseException.class)
    public void testUnknownOption() throws ParseException
    {
        String[] args = {"--unknownOption"};

        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.parse(options, args);
    }

    @Test
    public void testLongOptions() throws ParseException
    {
        String[] args = {"--longOptionA", "--longOptionB=valueB", "--longOptionC"};

        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");
        options.addOption("longOptionC", true, "Long Option C");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        assertEquals(args.length, parsedArgs.length);
        assertEquals("--longOptionB=valueB", parsedArgs[1]);
        assertTrue(Arrays.asList(parsedArgs[2]).contains("--longOptionC"));
    }

    @Test(expected = ParseException.class)
    public void testMissingArgumentForOption() throws ParseException
    {
        String[] args = {"-a", "--optionB"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        parser.parse(options, args);
    }

    @Test
    public void testBurstedOptions() throws ParseException
    {
        String[] args = {"-a--optionB=valueB", "-c--optionD"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        assertEquals(args.length, parsedArgs.length);
        assertEquals("--optionB=valueB", parsedArgs[0]);
        assertTrue(Arrays.asList(parsedArgs[1]).contains("-c"));
        assertTrue(Arrays.asList(parsedArgs[1]).contains("--optionD"));
    }

    @Test
    public void testStopAtNonOption() throws ParseException
    {
        String[] args = {"--", "-a", "--optionB=valueB", "-c"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        parser.setStopAtNonOption(true);

        String[] parsedArgs = parser.parse(options, args);

        assertEquals(args.length, parsedArgs.length);
        assertEquals("--", parsedArgs[1]);
        assertTrue(Arrays.asList(parsedArgs[2]).contains("-c"));
    }

    @Test
    public void testHelp()
    {
        String[] args = {"--help"};

        Options options = new Options();
        DefaultParser parser = new DefaultParser();

        HelpFormatter formatter = new HelpFormatter();

        try
        {
            parser.parse(options, args, true, true);
        }
        catch (ParseException e)
        {
            formatter.printHelp("Help", options);
            System.out.println(e.getMessage());
        }

        String help = "Help\n"
               + "\n"
               + "usage: Help [-h] [--help]\n"
               + "\n"
               + "options:\n"
               + "\n"
               + "   --help                shows detailed help\n"
               + "\n"
               + "   -h, --help            shows detailed help\n";

        assertEquals(help, System.console().reader().readLine());
    }
}