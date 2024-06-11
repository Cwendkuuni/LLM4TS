package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest
{
    @Test
    public void testOptionGroupWithSingleOption()
    {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "output", true, "Output directory")
                .required(false)
                .longOpt("output-dir");

        group.addOption(option);

        group.setSelected(option);

        assertEquals("output", group.getSelected());
        assertEquals(option, group.getOption(option.getKey()));
        assertEquals(1, group.getOptions().size());
        assertEquals(1, group.getNames().size());
        assertFalse(group.isRequired());

        Option expectedOption = group.getOption("output");
        Option actualOption = group.getOption("output-dir");

        assertEquals(expectedOption, actualOption);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testOptionGroupWithAlreadySelectedOption()
    {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", null, true, "Option 1");
        Option option2 = new Option("o2", null, true, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        try
        {
            group.setSelected(option2);
        }
        catch (AlreadySelectedException e)
        {
            // expected exception
        }

        assertEquals(option1, group.getSelectedOption());
    }

    @Test
    public void testOptionGroupWithMultipeOptions()
    {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("o1", "o1", true, "Option 1")
                .required(false)
                .longOpt("option-one");

        Option option2 = new Option("o2", "o2", true, "Option 2")
                .required(false)
                .longOpt("option-two");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option2);

        assertEquals("o2", group.getSelected());
        assertEquals(option2, group.getOption(option2.getKey()));
        assertEquals(2, group.getOptions().size());
        assertEquals(2, group.getNames().size());
        assertFalse(group.isRequired());

        Option expectedOption1 = group.getOption("o1");
        Option expectedOption2 = group.getOption("o2");

        Option actualOption1 = group.getOption("option-one");
        Option actualOption2 = group.getOption("option-two");

        assertEquals(expectedOption1, actualOption1);
        assertEquals(expectedOption2, actualOption2);
    }

    @Test
    public void testOptionGroupWithRequiredOption()
    {
        OptionGroup group = new OptionGroup();
        Option option = new Option("o", "output", true, "Output directory")
                .required(true)
                .longOpt("output-dir");

        group.addOption(option);

        try
        {
            group.setSelected(null);
        }
        catch (IllegalArgumentException e)
        {
            // expected exception
        }

        Option expectedOption = group.getOption("output");

        Option actualOption = group.getOption("output-dir");

        assertEquals(expectedOption, actualOption);
    }
}