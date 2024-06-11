package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;

/**
 * Tests for Parser class
 */
public class ParserTest
{
    /**
     * Test parse method with valid options and arguments
     */
    @org.junit.Test
    public void testParseValid() throws Exception
    {
        String[] args = new String[]{"-a", "1", "-b", "2", "-c", "3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null);
        assertEquals(3, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals("3", cmd.getOptionValue("c"));
    }

    /**
     * Test parse method with invalid options
     */
    @org.junit.Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption() throws Exception
    {
        String[] args = new String[]{"--invalid"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        parser.parse(options, args, null);
        fail("Expected UnrecognizedOptionException");
    }

    /**
     * Test parse method with missing required options
     */
    @org.junit.Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws Exception
    {
        String[] args = new String[]{"-a", "1"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "Option B is required");
        parser.setOptions(options);
        parser.parse(options, args, null);
        fail("Expected MissingOptionException");
    }

    /**
     * Test parse method with missing required argument
     */
    @org.junit.Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws Exception
    {
        String[] args = new String[]{"-a"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "Option B requires an argument");
        parser.setOptions(options);
        parser.parse(options, args, null);
        fail("Expected MissingArgumentException");
    }

    /**
     * Test parse method with stopAtNonOption set to false
     */
    @org.junit.Test
    public void testParseWithStopAtNonOptionFalse() throws Exception
    {
        String[] args = new String[]{"--", "-a", "1", "--invalid", "2", "-b", "3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null, false);
        assertEquals(5, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("invalid"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("3", cmd.getOptionValue("b"));
    }

    /**
     * Test parse method with stopAtNonOption set to true
     */
    @org.junit.Test
    public void testParseWithStopAtNonOptionTrue() throws Exception
    {
        String[] args = new String[]{"--", "-a", "1", "--invalid", "2", "-b", "3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null, true);
        assertEquals(2, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("invalid"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("3", cmd.getOptionValue("b"));
    }

    /**
     * Test parse method with properties
     */
    @org.junit.Test
    public void testParseWithProperties() throws Exception
    {
        String[] args = new String[]{"--propertyFile", "test.properties"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("pf", true, "propertyFile", true, "Path to property file");
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        CommandLine cmd = parser.parse(options, args, properties, false);
        assertEquals(1, cmd.getArgs().size());
        assertTrue(cmd.hasOption("pf"));
        assertEquals("test.properties", cmd.getOptionValue("pf"));
        assertTrue(properties.getProperty("property").equals("value"));
    }
}