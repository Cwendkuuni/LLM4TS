package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest
{
    @Test
    public void testAddOption()
    {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o1", "option1");
        Option opt2 = new Option("o2", "option2");

        group.addOption(opt1);
        group.addOption(opt2);

        assertEquals(2, group.getOptions().size());
    }

    @Test
    public void testGetNames()
    {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o1", "option1");
        Option opt2 = new Option("o2", "option2");

        group.addOption(opt1);
        group.addOption(opt2);

        Collection<String> names = group.getNames();

        assertTrue(names.containsAll(asList("o1", "o2")));
    }

    @Test
    public void testSetSelectedNull()
    {
        OptionGroup group = new OptionGroup();

        group.setSelected(null);

        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedSameOption()
    {
        OptionGroup group = new OptionGroup();
        Option opt = new Option("o", "option");

        group.setSelected(opt);

        assertEquals("o", group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption() throws AlreadySelectedException
    {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o1", "option1");
        Option opt2 = new Option("o2", "option2");

        group.setSelected(opt1);
        group.setSelected(opt2);
    }

    @Test
    public void testIsRequired()
    {
        OptionGroup group = new OptionGroup();

        assertFalse(group.isRequired());

        group.setRequired(true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testToString()
    {
        OptionGroup group = new OptionGroup();
        Option opt1 = new Option("o1", "option1");
        Option opt2 = new Option("o2", "option2");

        group.addOption(opt1);
        group.addOption(opt2);

        String expected = "[--option1 option1, --option2 option2]";

        assertEquals(expected, group.toString());
    }
}

class AlreadySelectedException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private final OptionGroup group;
    private final Option option;

    public AlreadySelectedException(OptionGroup group, Option option)
    {
        super("Option " + option.getKey() + " has already been selected in group " + group);
        this.group = group;
        this.option = option;
    }
}