package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.java
 */
public class ParserTest
{
    @Test
    public void testParser() throws ParseException
    {
        // Test with empty arguments
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, new String[0]);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.getArgs().length > 0);

        // Test with required options
        options.addRequiredOption("b", true, "Option B");
        cmd = parser.parse(options, new String[]{"--b"});
        assertTrue(cmd.hasOption("b"));

        // Test with optional options
        options.addOption("c", true, "Option C");
        cmd = parser.parse(options, new String[]{"--c"});
        assertTrue(cmd.hasOption("c"));

        // Test with values for options
        options.addOption("d", true, "Option D");
        cmd = parser.parse(options, new String[]{"--d", "Value"});
        assertTrue(cmd.hasOption("d"));
        assertEquals("Value", cmd.getOptionValue("d"));

        // Test with long options
        options.addOption("e", true, "Option E");
        options.addOption("long-e", true, "Option E, long form");
        cmd = parser.parse(options, new String[]{"--long-e", "Value2"});
        assertTrue(cmd.hasOption("e"));
        assertTrue(cmd.hasOption("long-e"));
        assertEquals("Value2", cmd.getOptionValue("e"));
        assertEquals("Value2", cmd.getOptionValue("long-e"));

        // Test with multiple values for options
        options.addOption("f", true, "Option F");
        options.addOption("g", true, "Option G");
        cmd = parser.parse(options, new String[]{"--f", "Value1", "--f", "Value2", "--g", "Value3"});
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("g"));
        List<String> fValues = cmd.getOptionValues("f");
        List<String> gValues = cmd.getOptionValues("g");
        assertEquals(2, fValues.size());
        assertEquals("Value1", fValues.get(0));
        assertEquals("Value2", fValues.get(1));
        assertEquals(1, gValues.size());
        assertEquals("Value3", gValues.get(0));

        // Test with properties
        Properties properties = new Properties();
        properties.put("h", "Value4");
        cmd = parser.parse(options, new String[0], properties);
        assertTrue(cmd.hasOption("h"));
        assertEquals("Value4", cmd.getOptionValue("h"));

        // Test with multiple options and arguments
        options.addOption("i", true, "Option I");
        options.addOption("j", true, "Option J");
        cmd = parser.parse(options, new String[]{"--i", "Value5", "--j", "Value6", "Argument1", "Argument2"});
        assertTrue(cmd.hasOption("i"));
        assertTrue(cmd.hasOption("j"));
        assertEquals("Value5", cmd.getOptionValue("i"));
        assertEquals("Value6", cmd.getOptionValue("j"));
        assertFalse(cmd.hasOption("k"));
        List<String> args = cmd.getArgs();
        assertEquals(2, args.size());
        assertEquals("Argument1", args.get(0));
        assertEquals("Argument2", args.get(1));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption()
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, new String[]{"--x"});
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument()
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "BArg");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, new String[]{"--a", "--b"});
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption()
    {
        Options options = new Options();
        options.addRequiredOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, new String[0]);
    }

    @Test(expected = ParseException.class)
    public void testParseException()
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, new String[]{"--a", "--c"});
    }

    @Test
    public void testParseWithEmptyRequiredOption()
    {
        Options options = new Options();
        options.addRequiredOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        try
        {
            parser.parse(options, new String[0]);
            fail("Expected MissingOptionException");
        }
        catch (MissingOptionException e)
        {
            // expected
        }
    }

    private static String[] getArgs(String... args)
    {
        return args;
    }
}