package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the {@link Options} class.
 */
public class OptionsTest
{
    /**
     * Test method for {@link Options#Options()}.
     */
    @Test
    public void testOptionsConstructor()
    {
        Options options = new Options();
        assertNotNull(options);
        assertEquals(0, options.size());
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)}.
     */
    @Test
    public void testAddOptionShort()
    {
        Options options = new Options();
        Option opt = options.addOption("a", "optionA", false, "Option A");
        assertEquals("optionA", opt.getLongOpt());
        assertEquals(false, opt.hasArg());
        assertEquals("Option A", opt.getDescription());
        assertEquals("a", opt.getKey());
        assertEquals(1, options.size());
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)}.
     */
    @Test
    public void testAddOptionLong()
    {
        Options options = new Options();
        Option opt = options.addOption("optionB", "optionB", false, "Option B");
        assertEquals("optionB", opt.getKey());
        assertEquals("optionB", opt.getLongOpt());
        assertEquals(false, opt.hasArg());
        assertEquals("Option B", opt.getDescription());
        assertEquals(1, options.size());
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)}.
     */
    @Test
    public void testAddOptionWithArg()
    {
        Options options = new Options();
        Option opt = options.addOption("b", "optionB", true, "Option B with argument");
        assertEquals("optionB", opt.getKey());
        assertEquals("optionB", opt.getLongOpt());
        assertEquals(true, opt.hasArg());
        assertEquals("Option B with argument", opt.getDescription());
        assertEquals(1, options.size());
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionDuplicateShort()
    {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("a", "optionA", false, "Option A");
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionDuplicateLong()
    {
        Options options = new Options();
        options.addOption("optionB", "optionB", false, "Option B");
        options.addOption("optionB", "optionB", false, "Option B");
    }

    /**
     * Test method for {@link Options#addOptionGroup(OptionGroup)}.
     */
    @Test
    public void testAddOptionGroup()
    {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("TestGroup", "Test Group");
        options.addOptionGroup(optionGroup);
        Option opt = optionGroup.addOption("c", "optionC", false, "Option C");
        assertEquals(1, options.size());
        assertEquals("TestGroup", options.getOptionGroup("TestGroup"));
        assertEquals("Test Group", options.getOptionGroup("TestGroup").getName());
        assertEquals("c", opt.getKey());
        assertEquals("optionC", opt.getLongOpt());
        assertEquals(false, opt.hasArg());
        assertEquals("Option C", opt.getDescription());
    }

    /**
     * Test method for {@link Options#getOptions()}.
     */
    @Test
    public void testGetOptions()
    {
        Options options = new Options();
        Option opt1 = options.addOption("a", "optionA", false, "Option A");
        Option opt2 = options.addOption("b", "optionB", false, "Option B");

        List<Option> optionsList = options.getOptions();

        assertEquals(2, optionsList.size());
        assertEquals(opt1, optionsList.get(0));
        assertEquals(opt2, optionsList.get(1));
    }

    /**
     * Test method for {@link Options#getRequiredOptions()}.
     */
    @Test
    public void testGetRequiredOptions()
    {
        Options options = new Options();
        Option option = options.addOption("optionA", true, "Option A");

        List<Option> requiredOptions = options.getRequiredOptions();

        assertEquals(1, requiredOptions.size());
        assertEquals(option, requiredOptions.get(0));
    }

    /**
     * Test method for {@link Options#getOption(String)}.
     */
    @Test
    public void testGetOption()
    {
        Options options = new Options();
        Option option = options.addOption("optionA", false, "Option A");

        Option retrievedOption = options.getOption("optionA");

        assertEquals(option, retrievedOption);
    }

    /**
     * Test method for {@link Options#getOption(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionNull()
    {
        Options options = new Options();
        options.getOption(null);
    }

    /**
     * Test method for {@link Options#getOption(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionUnknownOption()
    {
        Options options = new Options();
        options.addOption("optionA", false, "Option A");
        options.getOption("optionB");
    }

    /**
     * Test method for {@link Options#getMatchingOptions(String)}.
     */
    @Test
    public void testGetMatchingOptions()
    {
        Options options = new Options();
        Option opt1 = options.addOption("option1", false, "Option 1");
        Option opt2 = options.addOption("option2", false, "Option 2");

        List<String> matchingOptions = options.getMatchingOptions("opt");

        assertEquals(1, matchingOptions.size());
        assertEquals("option1", matchingOptions.get(0));
    }

    /**
     * Test method for {@link Options#hasOption(String)}.
     */
    @Test
    public void testHasOptionShort()
    {
        Options options = new Options();
        Option opt = options.addOption("a", "optionA", false, "Option A");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("optionA"));
        assertFalse(options.hasOption("b"));
    }

    /**
     * Test method for {@link Options#hasOption(String)}.
     */
    @Test
    public void testHasOptionLong()
    {
        Options options = new Options();
        Option opt = options.addOption("optionB", "optionB", false, "Option B");

        assertTrue(options.hasOption("optionB"));
        assertTrue(options.hasOption("optionB"));
        assertFalse(options.hasOption("a"));
    }

    /**
     * Test method for {@link Options#hasOption(String)}.
     */
    @Test
    public void testHasOptionUnknownOption()
    {
        Options options = new Options();
        options.addOption("optionA", false, "Option A");

        assertFalse(options.hasOption("optionB"));
    }

    /**
     * Test method for {@link Options#hasLongOption(String)}.
     */
    @Test
    public void testHasLongOption()
    {
        Options options = new Options();
        Option opt = options.addOption("optionB", "optionB", false, "Option B");

        assertTrue(options.hasLongOption("optionB"));
        assertFalse(options.hasLongOption("optionA"));
    }

    /**
     * Test method for {@link Options#hasShortOption(String)}.
     */
    @Test
    public void testHasShortOption()
    {
        Options options = new Options();
        Option opt = options.addOption("a", "optionA", false, "Option A");

        assertTrue(options.hasShortOption("a"));
        assertFalse(options.hasShortOption("b"));
    }

    /**
     * Test method for {@link Options#getOptionGroup(Option)}.
     */
    @Test
    public void testGetOptionGroup()
    {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("TestGroup", "Test Group");
        Option opt = optionGroup.addOption("optionC", false, "Option C");
        options.addOptionGroup(optionGroup);

        OptionGroup retrievedOptionGroup = options.getOptionGroup(opt);

        assertEquals(optionGroup, retrievedOptionGroup);
    }

    /**
     * Test method for {@link Options#getOptionGroup(Option)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionGroupNull()
    {
        Options options = new Options();
        options.getOptionGroup(null);
    }
}