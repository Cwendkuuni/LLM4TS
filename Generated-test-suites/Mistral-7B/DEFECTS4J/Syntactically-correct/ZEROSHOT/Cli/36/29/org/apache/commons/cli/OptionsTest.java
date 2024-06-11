package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

/**
 * Test class for Options.
 */
public class OptionsTest {

    /**
     * Test method for {@link Options#addOptionGroup(OptionGroup)} .
     */
    @org.junit.Test
    public void testAddOptionGroup() {
        OptionGroup optionGroup = new OptionGroup("TestGroup", "Test group options");
        Option option = new Option("t", "test", false, "Test option");
        optionGroup.addOption(option);

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        Option testOption = options.getOption("t");
        assertEquals("Test group options", testOption.getLongOpt());
        assertEquals(false, testOption.isRequired());

        List<OptionGroup> optionGroups = options.getOptionGroups();
        assertEquals(1, optionGroups.size());
        OptionGroup testOptionGroup = optionGroups.get(0);
        assertEquals("TestGroup", testOptionGroup.getName());
    }

    /**
     * Test method for {@link Options#addOption(String, String)} .
     */
    @org.junit.Test
    public void testAddOptionStringString() {
        Options options = new Options();

        Option option = options.addOption("t", "test", false, "Test option");

        assertEquals("test", option.getShortOpt());
        assertEquals("Test option", option.getDescription());
        assertFalse(option.isRequired());
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean)} .
     */
    @org.junit.Test
    public void testAddOptionStringStringBoolean() {
        Options options = new Options();

        Option option = options.addOption("t", "test", true, "Test option");

        assertEquals("test", option.getShortOpt());
        assertEquals("Test option", option.getDescription());
        assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link Options#addOption(String, String, boolean, String)} .
     */
    @org.junit.Test
    public void testAddOptionStringStringBooleanString() {
        Options options = new Options();

        Option option = options.addOption("t", "test", true, "Test option");

        assertEquals("test", option.getShortOpt());
        assertEquals("Test option", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals("Test option", option.getLongOpt());
    }

    /**
     * Test method for {@link Options#addOption(Option)} .
     */
    @org.junit.Test
    public void testAddOptionOption() {
        Option option = new Option("t", "test", false, "Test option");

        Options options = new Options();
        options.addOption(option);

        assertEquals("test", option.getShortOpt());
        assertEquals("Test option", option.getDescription());
        assertFalse(option.isRequired());
    }

    /**
     * Test method for {@link Options#getOptions()} .
     */
    @org.junit.Test
    public void testGetOptions() {
        Options options = new Options();
        Option option = options.addOption("t", "test", false, "Test option");

        List<Option> optionsList = options.getOptions();

        assertEquals(1, optionsList.size());
        Option testOption = optionsList.get(0);
        assertEquals("test", testOption.getShortOpt());
        assertEquals("Test option", testOption.getDescription());
        assertFalse(testOption.isRequired());
    }

    /**
     * Test method for {@link Options#getRequiredOptions()} .
     */
    @org.junit.Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = options.addOption("t", "test", true, "Test option");

        List<Option> requiredOptionsList = options.getRequiredOptions();

        assertEquals(1, requiredOptionsList.size());
        Option testOption = requiredOptionsList.get(0);
        assertEquals("test", testOption.getShortOpt());
        assertEquals("Test option", testOption.getDescription());
        assertTrue(testOption.isRequired());
    }

    /**
     * Test method for {@link Options#getOption(String)} .
     */
    @org.junit.Test
    public void testGetOptionString() {
        Options options = new Options();
        Option option = options.addOption("t", "test", false, "Test option");

        Option testOption = options.getOption("t");

        assertEquals("test", testOption.getShortOpt());
        assertEquals("Test option", testOption.getDescription());
        assertFalse(testOption.isRequired());
    }

    /**
     * Test method for {@link Options#getMatchingOptions(String)} .
     */
    @org.junit.Test
    public void testGetMatchingOptionsString() {
        Options options = new Options();
        Option option1 = options.addOption("t", "test1", false, "Test option 1");
        Option option2 = options.addOption("t", "test2", false, "Test option 2");

        List<String> matchingOptionsList = options.getMatchingOptions("t");

        assertEquals(2, matchingOptionsList.size());
        assertTrue(matchingOptionsList.contains("test1"));
        assertTrue(matchingOptionsList.contains("test2"));
    }

    /**
     * Test method for {@link Options#hasOption(String)} .
     */
    @org.junit.Test
    public void testHasOptionString() {
        Options options = new Options();
        Option option = options.addOption("t", "test", false, "Test option");

        assertTrue(options.hasOption("t"));
        assertTrue(options.hasOption("test"));
    }

    /**
     * Test method for {@link Options#hasLongOption(String)} .
     */
    @org.junit.Test
    public void testHasLongOptionString() {
        Options options = new Options();
        Option option = options.addOption("t", "test", false, "Test option");

        assertTrue(options.hasLongOption("test"));
    }

    /**
     * Test method for {@link Options#hasShortOption(String)} .
     */
    @org.junit.Test
    public void testHasShortOptionString() {
        Options options = new Options();
        Option option = options.addOption("t", "test", false, "Test option");

        assertTrue(options.hasShortOption("t"));
    }

    /**
     * Test method for {@link Options#getOptionGroup(Option)} .
     */
    @org.junit.Test
    public void testGetOptionGroupOption() {
        OptionGroup optionGroup = new OptionGroup("TestGroup", "Test group options");
        Option option = new Option("t", "test", false, "Test option");
        optionGroup.addOption(option);

        Options options = new Options();
        options.addOptionGroup(optionGroup);

        Option testOption = options.getOption("t");
        OptionGroup testOptionGroup = options.getOptionGroup(testOption);

        assertEquals("TestGroup", testOptionGroup.getName());
    }

    /**
     * Test method for {@link Options#toString()} .
     */
    @org.junit.Test
    public void testToString() {
        Options options = new Options();
        Option option = options.addOption("t", "test", false, "Test option");

        String optionsString = options.toString();

        assertTrue(optionsString.contains("[ Options: [ short "));
        assertTrue(optionsString.contains("[ long "));
    }
}