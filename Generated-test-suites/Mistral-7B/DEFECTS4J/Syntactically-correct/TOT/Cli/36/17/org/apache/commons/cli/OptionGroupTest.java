package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for OptionGroup class.
 */
public class OptionGroupTest
{
    @Test
    public void testOptionGroup() throws ParseException
    {
        OptionGroup optionGroup = new OptionGroup();

        // test addOption
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");
        Option option3 = new Option("o", "opt3", false, "Option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // test getNames
        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(3, names.size());
        Assert.assertTrue(names.contains("opt1"));
        Assert.assertTrue(names.contains("opt2"));
        Assert.assertTrue(names.contains("opt3"));

        // test getOptions
        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertEquals(option1, options.iterator().next());
        Assert.assertEquals(option2, options.iterator().next());
        Assert.assertEquals(option3, options.iterator().next());

        // test setSelected
        optionGroup.setSelected(option1);
        Assert.assertEquals("opt1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        try
        {
            optionGroup.setSelected(option1);
        }
        catch (AlreadySelectedException e)
        {
            // expected
        }
        Assert.assertEquals("opt2", optionGroup.getSelected());

        // test isRequired
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        // test toString
        Assert.assertEquals("[--opt1 Option 1, --opt2 Option 2, --opt3 Option 3]", optionGroup.toString());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testAlreadySelectedException() throws ParseException
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
}