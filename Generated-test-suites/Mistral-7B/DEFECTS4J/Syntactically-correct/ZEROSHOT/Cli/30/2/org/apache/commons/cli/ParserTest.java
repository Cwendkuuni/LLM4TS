package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParser() throws Exception
    {
        String[] args = {"-a", "1", "--b", "2", "--c", "3", "--", "d", "e", "f"};
        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");
        options.addOption("c", true, "C value");

        parser.setOptions(options);
        CommandLine commandLine = parser.parse(args, false);

        assertNotNull(commandLine);
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.getOptionValue("a").equals("1"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.getOptionValue("b").equals("2"));
        assertTrue(commandLine.hasOption("c"));
        assertTrue(commandLine.getOptionValue("c").equals("3"));
        assertFalse(commandLine.hasOption("d"));
        assertFalse(commandLine.hasOption("e"));
        assertFalse(commandLine.hasOption("f"));
        assertEquals(Arrays.asList("d", "e", "f"), commandLine.getArgList());
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws Exception
    {
        String[] args = {"--x"};
        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);
        parser.parse(args, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception
    {
        String[] args = {"--z"};
        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);
        parser.parse(args, false);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception
    {
        String[] args = {"-a"};
        Parser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("a", true, "A value");
        parser.setOptions(options);
        parser.parse(args, false);
    }

    @Test
    public void testNullArgs() throws Exception
    {
        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);
        CommandLine commandLine = parser.parse(new String[0], false);

        assertNull(commandLine);
    }

    @Test
    public void testParseProperties() throws Exception
    {
        String[] args = {"--foo", "bar"};
        Properties properties = new Properties();
        properties.setProperty("foo", "baz");

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("foo", true, "Option value");

        parser.setOptions(options);
        CommandLine commandLine = parser.parse(args, properties, false);

        assertTrue(commandLine.hasOption("foo"));
        assertTrue(commandLine.getOptionValue("foo").equals("baz"));
    }

    @Test
    public void testParsePropertiesWithMissingOption() throws Exception
    {
        String[] args = {"--qux"};
        Properties properties = new Properties();
        properties.setProperty("foo", "baz");

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("foo", true, "Option value");

        try
        {
            parser.parse(args, properties, false);
            fail();
        }
        catch (MissingOptionException ex)
        {
            // expected
        }

        assertEquals(1, options.getHelpOptions().size());
        assertEquals("Unrecognized option: qux", ex.getMessage());
    }
}