package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionGroupTest
{
    @Test
    public void testOptionGroup()
    {
        // Creating an OptionGroup
        OptionGroup optionGroup = new OptionGroup();

        // Adding Options to the OptionGroup
        Option option1 = new Option("o1", "option1", false, "This is option1.");
        Option option2 = new Option("o2", "option2", false, "This is option2.");
        Option option3 = new Option("o3", "option3", false, "This is option3.");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // Testing getNames() method
        Collection names = optionGroup.getNames();
        assertEquals("Size of names collection should be 3.", 3, names.size());
        assertEquals("Names should contain option1.", "o1", names.toString());
        assertEquals("Names should contain option2.", "o2", names.toString());
        assertEquals("Names should contain option3.", "o3", names.toString());

        // Testing getOptions() method
        Collection options = optionGroup.getOptions();
        assertEquals("Size of options collection should be 3.", 3, options.size());
        assertEquals("Options should contain option1.", option1, options.toString());
        assertEquals("Options should contain option2.", option2, options.toString());
        assertEquals("Options should contain option3.", option3, options.toString());

        // Testing setSelected() method with correct option
        optionGroup.setSelected(option1);
        assertEquals("Selected option should be option1.", "o1", optionGroup.getSelected());

        // Testing setSelected() method with wrong option
        try
        {
            optionGroup.setSelected(option2);
            assert false; // This line should not be executed
        }
        catch (AlreadySelectedException e)
        {
            // Expected exception
        }

        // Testing setSelected() method with null option
        optionGroup.setSelected(null);
        assertNull("Selected option should be null after reset.", optionGroup.getSelected());

        // Testing isRequired() method
        optionGroup.setRequired(true);
        assertTrue("Option group is required.", optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse("Option group is not required.", optionGroup.isRequired());
    }
}