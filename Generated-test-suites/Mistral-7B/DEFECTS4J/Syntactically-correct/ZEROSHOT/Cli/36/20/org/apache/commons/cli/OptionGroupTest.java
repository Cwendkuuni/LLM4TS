package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for OptionGroup
 */
public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> options = new ArrayList<>(optionGroup.getOptions());

        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> names = new ArrayList<>(optionGroup.getNames());

        Assert.assertEquals(2, names.size());
        Assert.assertEquals(option1.getKey(), names.get(0));
        Assert.assertEquals(option2.getKey(), names.get(1));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            // expected
        }

        optionGroup.setSelected(option2);

        Assert.assertEquals(option2.getKey(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);

        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--option1 This is option1, --option2 This is option2]";

        Assert.assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testEmptyOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Assert.assertTrue(optionGroup.isEmpty());

        Option option1 = new Option("a", "option1", false, "This is option1");

        optionGroup.addOption(option1);

        Assert.assertFalse(optionGroup.isEmpty());
    }

    @Test
    public void testSelectOptionWithNullName() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(null);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSelectOptionWithUnknownName() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(new Option("c", "optionC", false, "This is optionC"));
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSelectOptionAfterAddingOneMoreOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");
        Option option3 = new Option("c", "option3", false, "This is option3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option3);
        } catch (IllegalArgumentException e) {
            // expected
        }

        optionGroup.addOption(option3);

        try {
            optionGroup.setSelected(option3);
        } catch (AlreadySelectedException e) {
            // expected
        }

        optionGroup.setSelected(option3);

        Assert.assertEquals(option3.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testToStringWithOptionsWithoutShortOpt() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option(null, "longOption1", false, "This is long option1");
        Option option2 = new Option(null, "longOption2", false, "This is long option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--longOption1 This is long option1, --longOption2 This is long option2]";

        Assert.assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testToStringWithOptionsWithoutLongOpt() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", null, false, "This is opt1");
        Option option2 = new Option("opt2", null, false, "This is opt2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-opt1 This is opt1, -opt2 This is opt2]";

        Assert.assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testToStringWithOptionsWithBothShortAndLongOpt() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o", "opt1", false, "This is opt1");
        Option option2 = new Option(null, "longOpt2", false, "This is long opt2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-o This is opt1, --longOpt2 This is long opt2]";

        Assert.assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testToStringWithEmptyOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();

        String expected = "[ ]";

        Assert.assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> actual = new ArrayList<>(optionGroup.getOptions());
        List<Option> expected = new ArrayList<>();
        expected.add(option1);
        expected.add(option2);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetSelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        Option actual = optionGroup.getSelectedOption();

        Assert.assertEquals(option1, actual);
    }

    @Test
    public void testGetSelectedOptionNull() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Option actual = optionGroup.getSelectedOption();

        Assert.assertNull(actual);
    }

    @Test
    public void testGetSelectedOptionUnknown() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "This is option1");
        Option option2 = new Option("b", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Option actual = new Option("c", "unknownOption", false, "This is unknown option");

        Assert.assertFalse(optionGroup.getSelectedOption().equals(actual));
    }

    @Test
    public void testIsEmpty() {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertTrue(optionGroup.isEmpty());

        Option option1 = new Option("option1", null, false, "This is option1");

        optionGroup.addOption(option1);

        Assert.assertFalse(optionGroup.isEmpty());
    }
}