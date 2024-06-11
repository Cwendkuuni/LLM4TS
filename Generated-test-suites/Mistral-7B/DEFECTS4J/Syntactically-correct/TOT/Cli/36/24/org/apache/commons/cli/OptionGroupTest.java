package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link OptionGroup} class.
 */
public class OptionGroupTest
{
    /**
     * Test for constructor and basic methods.
     */
    @Test
    public void testBasic()
    {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option one", false, "o");
        Option option2 = new Option("o2", "option two", false, "o2");
        OptionGroup group1 = group.addOption(option1).addOption(option2);

        assertEquals(2, group1.getOptions().size());
        assertEquals( Arrays.asList("o", "o2"), group1.getNames() );
        assertNull(group1.getSelected());
        assertFalse(group1.isRequired());
    }

    /**
     * Test for selecting an option.
     */
    @Test
    public void testSelectOption()
    {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option one", false, "o");
        Option option2 = new Option("o2", "option two", false, "o2");
        OptionGroup group1 = group.addOption(option1).addOption(option2);

        assertNull(group1.getSelected());

        group1.setSelected(option1);
        assertEquals("o", group1.getSelected());

        group1.setSelected(option2);
        assertEquals("o2", group1.getSelected());

        assertThrows(AlreadySelectedException.class, () -> group1.setSelected(option1));
    }

    /**
     * Test for selecting a required option.
     */
    @Test
    public void testSelectRequiredOption()
    {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);

        Option option1 = new Option("o1", "option one", true, "o");
        Option option2 = new Option("o2", "option two", false, "o2");
        OptionGroup group1 = group.addOption(option1).addOption(option2);

        assertThrows(IllegalArgumentException.class, () -> group1.setSelected(option2));

        group1.setSelected(option1);
        assertEquals("o", group1.getSelected());
    }

    /**
     * Test for setting selected option when no option is selected.
     */
    @Test
    public void testSelectOptionWhenNoneSelected()
    {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option one", false, "o");
        Option option2 = new Option("o2", "option two", false, "o2");
        OptionGroup group1 = group.addOption(option1).addOption(option2);

        group1.setSelected(null);

        assertNull(group1.getSelected());

        group1.setSelected(option1);
        assertEquals("o", group1.getSelected());
    }

    /**
     * Test for getting options by name.
     */
    @Test
    public void testGetOptionByName()
    {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("o1", "option one", false, "o");
        Option option2 = new Option("o2", "option two", false, "o2");
        OptionGroup group1 = group.addOption(option1).addOption(option2);

        Option option = group1.getOption("o1");
        assertEquals(option1, option);

        option = group1.getOption("o3");
        assertNull(option);
    }
}