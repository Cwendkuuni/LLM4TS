package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Options.
 *
 * @author <a href="mailto:jansd@apache.org">Jens Schauder</a>
 */
public class OptionsTest
{
    /**
     * Test for addOption method with single character option.
     */
    @Test
    public void testAddOptionSingleChar()
    {
        Options opts = new Options();
        opts.addOption('a', "alpha", false, "an alpha option");
        assertTrue(opts.hasOption('a'));
        assertEquals("an alpha option", opts.getOption('a').getLongOpt());
        assertEquals(false, opts.getOption('a').isRequired());
    }

    /**
     * Test for addOption method with long option.
     */
    @Test
    public void testAddOptionLong()
    {
        Options opts = new Options();
        opts.addOption("alpha", "an alpha option", false, "an alpha option");
        assertTrue(opts.hasOption("alpha"));
        assertEquals("an alpha option", opts.getOption("alpha").getLongOpt());
        assertEquals(false, opts.getOption("alpha").isRequired());
    }

    /**
     * Test for addOption method with single character required option.
     */
    @Test
    public void testAddOptionRequiredSingleChar()
    {
        Options opts = new Options();
        opts.addOption('a', "an alpha option", true, "an alpha option");
        assertTrue(opts.hasOption('a'));
        assertEquals("an alpha option", opts.getOption('a').getLongOpt());
        assertTrue(opts.getOption('a').isRequired());
    }

    /**
     * Test for addOption method with long required option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionRequiredLong()
    {
        Options opts = new Options();
        opts.addOption("alpha", "an alpha option", true, "an alpha option");
        assertTrue(opts.hasOption("alpha"));
        assertEquals("an alpha option", opts.getOption("alpha").getLongOpt());
        // assert required option cannot be added to an OptionGroup
        opts.addOptionGroup(new OptionGroup("mygroup", "My Group"));
        opts.addOption("alpha", "an alpha option", true, "an alpha option");
        fail("Should throw an IllegalArgumentException");
    }

    /**
     * Test for addOptionGroup method.
     */
    @Test
    public void testAddOptionGroup()
    {
        Options rootOptions = new Options();
        OptionGroup group = new OptionGroup("group", "My Group");
        group.addOption(new Option("g", "group", false, "A group option"));
        rootOptions.addOptionGroup(group);
        assertNotNull(rootOptions.getOptionGroup("group"));
        assertTrue(rootOptions.getOptions().contains(group.getOptions().get(0)));

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try
        {
            cmd = parser.parse(rootOptions, new String[]{"-g"});
        }
        catch (ParseException e)
        {
            fail("Parsing failed: " + e.getMessage());
        }
        assertTrue(cmd.hasOption('g'));
    }

    /**
     * Test for getMatchingOptions method.
     */
    @Test
    public void testGetMatchingOptions()
    {
        Options opts = new Options();
        opts.addOption("foo", "an option with long name foo");
        opts.addOption("bar", "an option with long name bar");
        opts.addOption("baz", "an option with long name baz");

        List<String> matching = opts.getMatchingOptions("ba");
        assertEquals(1, matching.size());
        assertEquals("bar", matching.get(0));
    }

    /**
     * Test for getMatchingOptions method with no matching options.
     */
    @Test
    public void testGetMatchingOptionsNoMatch()
    {
        Options opts = new Options();
        opts.addOption("foo", "an option with long name foo");
        opts.addOption("bar", "an option with long name bar");

        List<String> matching = opts.getMatchingOptions("qux");
        assertEquals(0, matching.size());
    }
}