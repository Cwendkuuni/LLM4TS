package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.apache.commons.cli.utils.HelpFormatter;

import java.util.List;

/**
 * Tests for {@link Parser}.
 */
public class ParserTest
{
    @org.junit.Test
    public void testParseSimple() throws Exception
    {
        String[] args = {"test", "--option1", "value1", "--option2"};

        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(2, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertFalse(cmd.hasOption("nonexistent"));
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseMissingOption() throws Exception
    {
        String[] args = {"test", "--option1", "--option3"};

        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, false);

        fail("Expected ParseException");
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseMissingValue() throws Exception
    {
        String[] args = {"test", "--option1"};

        Options options = new Options();
        options.addOption("option1", true, "o1");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, false);

        fail("Expected ParseException");
    }

    @org.junit.Test
    public void testParseWithProperties() throws Exception
    {
        String[] args = {"test", "--option1=value1", "--option2=value2"};

        Properties properties = new Properties();
        properties.setProperty("option1", "propValue1");
        properties.setProperty("option2", "propValue2");

        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("propValue1", cmd.getOptionValue("option1"));
        assertEquals("propValue2", cmd.getOptionValue("option2"));
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseMissingPropertyValue() throws Exception
    {
        String[] args = {"test", "--option1"};

        Properties properties = new Properties();

        Options options = new Options();
        options.addOption("option1", true, "o1");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, properties, false);

        fail("Expected ParseException");
    }

    @org.junit.Test
    public void testParseWithGroup() throws Exception
    {
        String[] args = {"test", "--group", "--option1", "value1", "--option2", "value2"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "test group");
        group.setRequired(true);
        options.addOptionGroup(group);
        group.addOption("option1", true, "o1");
        group.addOption("option2", true, "o2");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));
        assertTrue(group.isSelected());

        List<String> required = parser.getRequiredOptions();
        assertTrue(required.isEmpty());
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseMissingGroup() throws Exception
    {
        String[] args = {"test", "--option1", "value1", "--option2", "value2"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "test group");
        group.setRequired(true);
        options.addOptionGroup(group);
        group.addOption("option1", true, "o1");
        group.addOption("option2", true, "o2");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, false);

        fail("Expected ParseException");
    }

    @org.junit.Test
    public void testParseWithStopAtNonOption() throws Exception
    {
        String[] args = {"test", "--option1", "value1", "nonoption"};

        Options options = new Options();
        options.addOption("option1", true, "o1");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        List<String> argsList = Arrays.asList(args);
        CommandLine cmd = parser.parse(options, args, true);

        assertEquals(1, cmd.getArgs().size());
        assertEquals("nonoption", cmd.getArgs().get(0));
    }

    @org.junit.Test
    public void testPrintHelp() throws Exception
    {
        String[] args = {"test", "--help"};

        Options options = new Options();
        options.addOption("help", false, "h", "print help message");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null, false);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "", options, true);
    }
}