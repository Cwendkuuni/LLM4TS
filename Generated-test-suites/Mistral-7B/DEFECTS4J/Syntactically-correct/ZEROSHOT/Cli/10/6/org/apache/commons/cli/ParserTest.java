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
 * Test class for Parser.java
 *
 * @author your_name
 */
public class ParserTest {

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @org.junit.Test
    public void testParse() throws Exception {
        // Given
        String[] args = new String[]{"--test", "-a", "1", "-b", "2", "-c", "3"};
        Options options = new Options();
        options.addOption("test", false, null, "Test option");
        options.addOption("a", true, "1", "Option A");
        options.addOption("b", true, "2", "Option B");
        options.addOption("c", true, "3", "Option C");

        Parser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, args);

        // Then
        assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], cmd.getArgs().get(i));
        }

        assertTrue(cmd.hasOption("test"));
        assertEquals(1, cmd.getOptionValue("a").length());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals(2, cmd.getOptionValue("b").length());
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals(3, cmd.getOptionValue("c").length());
        assertEquals("3", cmd.getOptionValue("c"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}.
     */
    @org.junit.Test
    public void testParseProperties() throws Exception {
        // Given
        String[] args = new String[]{"--test", "-a=1", "--other=true"};
        Options options = new Options();
        options.addOption("test", false, null, "Test option");
        options.addOption("a", true, null, "Option A");
        options.addOption("other", true, null, "Option Other");

        Parser parser = new DefaultParser();

        Properties properties = new Properties();
        properties.setProperty("test", "value");
        properties.setProperty("a", "1");

        // When
        CommandLine cmd = parser.parse(options, args, properties);

        // Then
        assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], cmd.getArgs().get(i));
        }

        assertTrue(cmd.hasOption("test"));
        assertNull(cmd.getOptionValue("test"));
        assertTrue(cmd.hasOption("a"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("other"));
        assertEquals("true", cmd.getOptionValue("other"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @org.junit.Test
    public void testParseStopAtNonOption() throws Exception {
        // Given
        String[] args = new String[]{"--test", "-a", "1", "nonOption", "3"};
        Options options = new Options();
        options.addOption("test", false, null, "Test option");
        options.addOption("a", true, "1", "Option A");

        Parser parser = new DefaultParser();

        // When
        CommandLine cmd = parser.parse(options, args, true);

        // Then
        assertEquals(args.length - 1, cmd.getArgs().size());
        assertTrue(cmd.hasOption("test"));
        assertTrue(cmd.hasOption("a"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertTrue(cmd.getArgs().contains("nonOption"));
        assertFalse(cmd.hasOption("nonOption"));
        assertFalse(cmd.getArgs().contains("3"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @org.junit.Test
    public void testParsePropertiesStopAtNonOption() throws Exception {
        // Given
        String[] args = new String[]{"--test", "-a=1", "nonOption", "--other=true"};
        Options options = new Options();
        options.addOption("test", false, null, "Test option");
        options.addOption("a", true, null, "Option A");
        options.addOption("other", true, null, "Option Other");

        Parser parser = new DefaultParser();

        Properties properties = new Properties();
        properties.setProperty("test", "value");
        properties.setProperty("a", "1");

        // When
        CommandLine cmd = parser.parse(options, args, properties, true);

        // Then
        assertEquals(args.length - 1, cmd.getArgs().size());
        assertTrue(cmd.hasOption("test"));
        assertTrue(cmd.hasOption("a"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertTrue(cmd.getArgs().contains("nonOption"));
        assertFalse(cmd.hasOption("nonOption"));
        assertFalse(cmd.getArgs().contains("3"));
        assertTrue(cmd.hasOption("other"));
        assertEquals("true", cmd.getOptionValue("other"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @org.junit.Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        // Given
        String[] args = new String[]{"--test", "-x", "1"};
        Options options = new Options();
        options.addOption("test", false, null, "Test option");

        Parser parser = new DefaultParser();

        // When
        parser.parse(options, args);

        // Then
        fail("Expected UnrecognizedOptionException");
    }

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @org.junit.Test(expected = MissingOptionException.class)
    public void testMissingOption() throws Exception {
        // Given
        String[] args = new String[]{"--test"};
        Options options = new Options();
        options.addOption("test", false, null, "Test option");
        options.addOption("a", true, null, "Option A");

        Parser parser = new DefaultParser();

        // When
        parser.parse(options, args);

        // Then
        fail("Expected MissingOptionException");
    }

    /**
     * Test method for {@link Parser#parse(Options, String[])}.
     */
    @org.junit.Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        // Given
        String[] args = new String[]{"--test", "-a"};
        Options options = new Options();
        options.addOption("test", false, null, "Test option");
        options.addOption("a", true, null, "Option A");

        Parser parser = new DefaultParser();

        // When
        parser.parse(options, args);

        // Then
        fail("Expected MissingArgumentException");
    }
}