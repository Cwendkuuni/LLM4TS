package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OptionGroupTest
{
    @Test
    public void testOptionGroup()
    {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", true, "Description for option2");
        Option option3 = new Option("o3", "option3", false, "Description for option3");

        group.addOption(option1);
        group.addOption(option2);
        group.addOption(option3);

        // Test getNames
        Collection<String> names = group.getNames();
        assertEquals(3, names.size());
        assertEquals("o1", names.iterator().next());
        assertEquals("o2", names.iterator().next());
        assertEquals("o3", names.iterator().next());

        // Test getOptions
        Collection<Option> options = group.getOptions();
        assertEquals(3, options.size());
        assertEquals(option1, options.iterator().next());
        assertEquals(option2, options.iterator().next());
        assertEquals(option3, options.iterator().next());

        // Test setSelected with valid option
        group.setSelected(option1);
        assertEquals("o1", group.getSelected());

        // Test setSelected with null option
        group.setSelected(null);
        assertNull(group.getSelected());

        // Test setSelected with already selected option
        group.setSelected(option1);
        assertThrows(AlreadySelectedException.class, () -> group.setSelected(option1));

        // Test setSelected with different option
        group.setSelected(option2);
        assertEquals("o2", group.getSelected());

        // Test isRequired
        assertFalse(group.isRequired());
    }
}