package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionGroupTest
{
    @Test
    public void testAddAndGetOptions() throws ParseException
    {
        OptionGroup group = new OptionGroup();

        Option opt1 = new Option("o", "option1", false, "This is option 1");
        Option opt2 = new Option("o", "option2", false, "This is option 2");

        group.addOption(opt1);
        group.addOption(opt2);

        Collection<Option> options = group.getOptions();
        assertEquals(2, options.size());
        assertEquals(opt1, options.iterator().next());
        assertEquals(opt2, options.iterator().next());

        Collection<String> names = group.getNames();
        assertEquals(2, names.size());
        assertEquals("o", names.iterator().next());
        assertEquals("o", names.iterator().next());
    }

    @Test
    public void testSetAndGetSelectedOption() throws ParseException
    {
        OptionGroup group = new OptionGroup();

        Option opt1 = new Option("o1", "option1", false, "This is option 1");
        Option opt2 = new Option("o2", "option2", false, "This is option 2");

        group.addOption(opt1);
        group.addOption(opt2);

        group.setSelected(opt1);

        assertEquals("option1", group.getSelected());
        assertNull(group.getSelected(opt2.getOpt()));

        group.setSelected(opt2);
        assertEquals("option2", group.getSelected());

        try
        {
            group.setSelected(new Option("o3", "option3", false, "This is option 3"));
            fail("AlreadySelectedException expected");
        }
        catch (AlreadySelectedException e)
        {
            // expected
        }
    }

    @Test
    public void testIsRequired()
    {
        OptionGroup group = new OptionGroup();

        group.setRequired(true);
        assertTrue(group.isRequired());

        OptionGroup notRequired = new OptionGroup();
        assertFalse(notRequired.isRequired());
    }
}