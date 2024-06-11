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
        OptionGroup optionGroup = new OptionGroup();

        // Test adding options
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");
        Option option3 = new Option("o3", "option3", false, "Description for option3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // Test getting options
        Collection<Option> options = optionGroup.getOptions();
        assertEquals(3, options.size());
        assertEquals(option1, options.iterator().next());
        assertEquals(option2, options.iterator().next());
        assertEquals(option3, options.iterator().next());

        // Test getting option names
        Collection<String> names = optionGroup.getNames();
        assertEquals(3, names.size());
        assertEquals(option1.getKey(), names.iterator().next());
        assertEquals(option2.getKey(), names.iterator().next());
        assertEquals(option3.getKey(), names.iterator().next());

        // Test setting selected option
        optionGroup.setSelected(option1);
        assertEquals(option1.getKey(), optionGroup.getSelected());

        try
        {
            optionGroup.setSelected(option2);
            fail();
        }
        catch (AlreadySelectedException e)
        {
            // expected
        }

        optionGroup.setSelected(option3);
        assertEquals(option3.getKey(), optionGroup.getSelected());

        // Test if required
        optionGroup.setRequired(true);
        assertNull(optionGroup.getSelected());
    }
}