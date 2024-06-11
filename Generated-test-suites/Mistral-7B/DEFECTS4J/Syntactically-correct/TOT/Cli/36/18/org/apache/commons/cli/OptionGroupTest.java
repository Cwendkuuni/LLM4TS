package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    @org.junit.Test
    public void testOptionGroup() throws ParseException
    {
        // Create a new OptionGroup
        OptionGroup optionGroup = new OptionGroup();

        // Add options to the group
        Option optGroupOption1 = new Option("g1", "group1", false, "G1 Description");
        Option optGroupOption2 = new Option("g2", "group2", false, "G2 Description");
        Option optGroupOption3 = new Option("g3", "group3", false, "G3 Description");

        optionGroup.addOption(optGroupOption1);
        optionGroup.addOption(optGroupOption2);
        optionGroup.addOption(optGroupOption3);

        // Test getting options
        Collection<Option> options = optionGroup.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(optGroupOption1));
        assertTrue(options.contains(optGroupOption2));
        assertTrue(options.contains(optGroupOption3));

        // Test getting names
        Collection<String> names = optionGroup.getNames();
        assertEquals(3, names.size());
        assertEquals("g1", names.iterator().next());
        assertEquals("g2", names.iterator().next());
        assertEquals("g3", names.iterator().next());

        // Test setting and getting selected option
        optionGroup.setSelected(optGroupOption1);
        assertEquals("g1", optionGroup.getSelected());

        optionGroup.setSelected(optGroupOption2);
        assertEquals("g2", optionGroup.getSelected());

        try
        {
            optionGroup.setSelected(optGroupOption1);
        }
        catch (AlreadySelectedException e)
        {
            // expected
        }

        // Test isRequired
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());

        // Test toString
        assertEquals("[-g1, G1 Description, --group1, -g2, G2 Description, --group2, -g3, G3 Description]", optionGroup.toString());
    }

    @org.junit.Test(expected = AlreadySelectedException.class)
    public void testAlreadySelectedException() throws ParseException
    {
        // Create a new OptionGroup
        OptionGroup optionGroup = new OptionGroup();

        // Add options to the group
        Option optGroupOption1 = new Option("g1", "group1", false, "G1 Description");
        Option optGroupOption2 = new Option("g2", "group2", false, "G2 Description");

        optionGroup.addOption(optGroupOption1);
        optionGroup.addOption(optGroupOption2);

        // Test setting and selecting same option twice
        optionGroup.setSelected(optGroupOption1);
        optionGroup.setSelected(optGroupOption1);
    }
}