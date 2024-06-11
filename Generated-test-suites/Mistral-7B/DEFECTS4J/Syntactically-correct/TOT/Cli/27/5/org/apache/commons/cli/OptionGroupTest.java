package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

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
    private OptionGroup optionGroup;

    @Before
    public void setUp()
    {
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        Option option3 = new Option("o3", "opt3", false, "Option 3");

        optionGroup = new OptionGroup("group1", "Group 1", Collections.asList(option1, option2, option3));
    }

    @Test
    public void testAddOption()
    {
        optionGroup.addOption(option3);
        assertEquals(3, optionGroup.getOptions().size());
    }

    @Test
    public void testGetNames()
    {
        assertEquals(3, optionGroup.getNames().size());
    }

    @Test
    public void testSetSelectedNull()
    {
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedAlreadySelected()
    {
        optionGroup.setSelected(option1);
        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
    }

    @Test
    public void testSetSelectedOption()
    {
        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSelectedNullOption()
    {
        optionGroup.setSelected(null);
    }

    @Test
    public void testIsRequired()
    {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        assertEquals("[--group1 Group 1 -opt1 Option 1, -o2 Option 2, --opt3 Option 3]", optionGroup.toString());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedException() throws AlreadySelectedException
    {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
}