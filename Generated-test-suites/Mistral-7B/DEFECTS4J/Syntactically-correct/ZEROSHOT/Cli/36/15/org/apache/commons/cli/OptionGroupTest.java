package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", true);
        Option option2 = new Option("option2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", true);
        Option option2 = new Option("option2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();

        assertTrue(names.contains("option1"));
        assertTrue(names.contains("option2"));
    }

    @Test
    public void testSetAndGetSelected()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", true);
        Option option2 = new Option("option2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try
        {
            optionGroup.setSelected(option1);
            assertEquals("option1", optionGroup.getSelected());
            optionGroup.setSelected(option2);
            assertEquals("option2", optionGroup.getSelected());
            optionGroup.setSelected(option1);
            assertEquals("option1", optionGroup.getSelected());
        }
        catch (Exception e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedTwice()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", true);
        Option option2 = new Option("option2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try
        {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option1);
        }
        catch (AlreadySelectedException e)
        {
            // expected
        }
    }

    @Test
    public void testIsRequired()
    {
        OptionGroup optionGroup = new OptionGroup();

        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", true);
        Option option2 = new Option("option2", "Option 2", false);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[--option1, --option2]", optionGroup.toString());
    }
}