package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testParseOptions() throws ParseException
    {
        // Test parsing with no options or arguments
        Options options = new Options();
        CommandLine cmd = new Parser().parse(options, new String[0]);
        assertEquals(0, cmd.getArgs().size());
        assertNull(cmd.getOptionValues("option1"));

        // Test parsing with a single required option
        options.addRequiredOption("option1", false, "o", "On");
        cmd = new Parser().parse(options, new String[]{"-o"});
        assertEquals(0, cmd.getArgs().size());
        assertSame("On", cmd.getOptionValues("option1")[0]);

        // Test parsing with a single optional option
        options.addOption("option2", true, "n", "No");
        cmd = new Parser().parse(options, new String[]{"-n"});
        assertEquals(0, cmd.getArgs().size());
        assertNull(cmd.getOptionValues("option2"));

        cmd = new Parser().parse(options, new String[]{"-n", "Yes"});
        assertEquals(0, cmd.getArgs().size());
        assertSame("Yes", cmd.getOptionValues("option2")[0]);

        // Test parsing with multiple options
        cmd = new Parser().parse(options, new String[]{"-o", "-n", "Yes"});
        assertEquals(0, cmd.getArgs().size());
        assertSame("On", cmd.getOptionValues("option1")[0]);
        assertSame("Yes", cmd.getOptionValues("option2")[0]);

        // Test parsing with an argument
        options.addOption("input", true, "i", "Input file");
        cmd = new Parser().parse(options, new String[]{"-i", "input.txt", "arg1"});
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs()[0]);
        assertNull(cmd.getOptionValues("input"));

        // Test parsing with an argument and options
        cmd = new Parser().parse(options, new String[]{"-i", "input.txt", "-o", "-n", "Yes"});
        assertEquals(1, cmd.getArgs().size());
        assertEquals("input.txt", cmd.getArgs()[0]);
        assertSame("On", cmd.getOptionValues("option1")[0]);
        assertSame("Yes", cmd.getOptionValues("option2")[0]);

        // Test parsing with an option group
        Options optionsGroup = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(false);
        group.addOption(new Option("g1", "group1", true, "Group 1"));
        group.addOption(new Option("g2", "group2", true, "Group 2"));
        optionsGroup.addOptionGroup(group);
        cmd = new Parser().parse(optionsGroup, new String[]{"-g1", "val1", "-g2", "val2"});
        assertTrue(group.isSelected());
        assertSame("val1", cmd.getOptionValues("group1")[0]);
        assertSame("val2", cmd.getOptionValues("group2")[0]);

        // Test parsing with conflicting options
        options = new Options();
        options.addOption("opt1", true, "o1", "Option 1");
        options.addOption("opt2", true, "o2", "Option 2");
        try
        {
            cmd = new Parser().parse(options, new String[]{"-o1", "-o2"});
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("option1", false, "o", "On");
        new Parser().parse(options, new String[0]);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingArgument() throws ParseException
    {
        Options options = new Options();
        options.addOption("input", true, "i", "Input file");
        new Parser().parse(options, new String[]{"-i"});
    }
}