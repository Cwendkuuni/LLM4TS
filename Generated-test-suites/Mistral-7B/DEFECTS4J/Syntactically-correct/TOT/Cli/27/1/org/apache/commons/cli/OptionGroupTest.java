package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for OptionGroup class
 */
public class OptionGroupTest
{
    @Test
    public void testOptionGroup()
    {
        // create an option group
        OptionGroup optionGroup = new OptionGroup();

        // add options
        Option option1 = new Option("o1", "option1", false, "Describe option 1");
        Option option2 = new Option("o2", "option2", false, "Describe option 2");
        Option option3 = new Option("o3", "option3", false, "Describe option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // test getNames()
        Collection names = optionGroup.getNames();
        assertEquals(3, names.size());
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
        assertTrue(names.contains("o3"));

        // test getOptions()
        Collection options = optionGroup.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));

        // set selected option
        optionGroup.setSelected(option1);
        assertEquals("o1", optionGroup.getSelected());

        // set selected option again
        optionGroup.setSelected(option1);
        assertNull(optionGroup.getSelected()); // AlreadySelectedException should be thrown

        // test isRequired()
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        // test toString()
        String expectedToString = "[-o1 Describe option 1, -o2 Describe option 2, -o3 Describe option 3]";
        assertEquals(expectedToString, optionGroup.toString());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testAlreadySelectedException()
    {
        // create an option group
        OptionGroup optionGroup = new OptionGroup();

        // add options
        Option option1 = new Option("o1", "option1", false, "Describe option 1");
        Option option2 = new Option("o2", "option2", false, "Describe option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // set selected option
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
}