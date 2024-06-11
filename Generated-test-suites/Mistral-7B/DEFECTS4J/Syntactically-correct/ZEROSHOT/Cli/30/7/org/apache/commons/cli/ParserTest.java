package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Parser.java
 *
 * @author John Doe (john.doe@example.com)
 */
public class ParserTest
{
    private Parser parser;

    @Before
    public void setUp()
    {
        parser = new MyParser();
    }

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     *
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test
    public void testParse() throws ParseException
    {
        String[] args = {"--inputFile", "test.txt", "-o", "output.csv", "-h"};
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "Input file path");
        options.addRequiredOption("o", "output", true, "Output file path");
        options.addOption("h", false, "Display help");

        CommandLine cmd = parser.parse(options, args);

        assertEquals("test.txt", cmd.getOptionValue("inputFile"));
        assertEquals("output.csv", cmd.getOptionValue("output"));
        assertEquals(false, cmd.hasOption("help"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     *
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = {"--inputFile", "test.txt", "-o"};
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "Input file path");
        options.addRequiredOption("o", "output", true, "Output file path");
        options.addProperty("output", "output.csv");

        CommandLine cmd = parser.parse(options, args, new Properties());

        assertEquals("test.txt", cmd.getOptionValue("inputFile"));
        assertEquals("output.csv", cmd.getOptionValue("output"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     *
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        String[] args = {"--inputFile", "test.txt", "-n", "newFile.csv", "-o"};
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "Input file path");
        options.addRequiredOption("o", "output", true, "Output file path");

        CommandLine cmd = parser.parse(options, args, true);

        assertEquals("test.txt", cmd.getOptionValue("inputFile"));
        assertEquals(null, cmd.getOptionValue("output"));
        assertEquals(true, cmd.hasArg("newFile.csv"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     *
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test
    public void testParseWithPropertiesAndStopAtNonOption() throws ParseException
    {
        String[] args = {"--inputFile", "test.txt", "-o"};
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "Input file path");
        options.addRequiredOption("o", "output", true, "Output file path");
        options.addProperty("output", "output.csv");

        CommandLine cmd = parser.parse(options, args, new Properties(), true);

        assertEquals("test.txt", cmd.getOptionValue("inputFile"));
        assertEquals(null, cmd.getOptionValue("output"));
    }

    /**
     * Test method for {@link Parser#flatten(Options, String[], boolean)}.
     *
     * @throws ParseException if an error occurs when flattening the arguments.
     */
    @Test
    public void testFlatten() throws ParseException
    {
        String[] args = {"--inputFile", "test.txt", "-o", "output.csv", "-h"};
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "Input file path");
        options.addRequiredOption("o", "output", true, "Output file path");
        options.addOption("h", false, "Display help");

        String[] flattenedArgs = parser.flatten(options, args, false);

        assertEquals(6, flattenedArgs.length);
        assertEquals("--inputFile", flattenedArgs[0]);
        assertEquals("test.txt", flattenedArgs[1]);
        assertEquals("-o", flattenedArgs[2]);
        assertEquals("output.csv", flattenedArgs[3]);
        assertEquals("--help", flattenedArgs[4]);
        assertEquals(null, flattenedArgs[5]);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, int)}.
     *
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test(expected = ParseException.class)
    public void testParseWithNonOption() throws ParseException
    {
        String[] args = {"--inputFile", "test.txt", "newFile.csv"};
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "Input file path");
        options.addRequiredOption("o", "output", true, "Output file path");

        parser.parse(options, args, true, 0);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean, int)}.
     *
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        String[] args = {"--inputFile", "test.txt"};
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "Input file path");
        options.addRequiredOption("o", "output", true, "Output file path");

        parser.parse(options, args, true, 0);
    }
}

class MyParser extends Parser
{
    // empty implementation
}