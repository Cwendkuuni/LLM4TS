package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for {@link OptionGroup}.
 */
public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        Map<String, Option> expectedOptions = new HashMap<String, Option>() {{
            put("opt1", option1);
            put("opt2", option2);
        }};

        Assert.assertEquals(expectedOptions, group.getOptionMap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullOption() {
        OptionGroup group = new OptionGroup();
        Option option = null;

        group.addOption(option);
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        Collection<String> expectedNames = Collections.singletonList("opt1");;

        Assert.assertEquals(expectedNames, group.getNames());
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        Collection<Option> expectedOptions = Collections.singletonList(option1);

        Assert.assertEquals(expectedOptions, group.getOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNullOption() {
        OptionGroup group = new OptionGroup();
        Option option = null;

        group.setSelected(option);
    }

    @Test
    public void testSetSelectedOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");

        group.setSelected(option1);

        Assert.assertEquals(option1.getKey(), group.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedOption() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");

        group.setSelected(option1);
        group.setSelected(option1);
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();

        group.setRequired(true);

        Assert.assertTrue(group.isRequired());
    }

    @Test
    public void testIsRequired() {
        OptionGroup group = new OptionGroup();

        group.setRequired(false);

        Assert.assertFalse(group.isRequired());

        group.setRequired(true);

        Assert.assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        String expectedString = "[--opt1 Option 1, --opt2 Option 2]";

        Assert.assertEquals(expectedString, group.toString());
    }
}