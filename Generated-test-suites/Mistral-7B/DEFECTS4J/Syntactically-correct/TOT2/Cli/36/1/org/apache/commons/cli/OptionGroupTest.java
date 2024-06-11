package org.junit.tests;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1", false, "o1");
        Option addedOption = optionGroup.addOption(option);
        assertEquals(option, addedOption);
    }

    @Test(expected = NullPointerException.class)
    public void testAddOptionNullOptionGroup() {
        Option option = new Option("option1", "Option 1", false, "o1");
        OptionGroup optionGroup = null;
        optionGroup.addOption(option);
    }

    @Test(expected = NullPointerException.class)
    public void testAddOptionNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;
        optionGroup.addOption(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionDuplicateOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1", false, "o1");
        OptionGroup addOptionResult = optionGroup.addOption(option);
        Option option2 = new Option("option1", "Option 1", false, "o1");
        OptionGroup addOptionResult2 = optionGroup.addOption(option2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNullOptionName() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option(null, "Option 1", false, "o1");
        optionGroup.addOption(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionEmptyOptionName() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("", "Option 1", false, "o1");
        optionGroup.addOption(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionEmptyOptionDescription() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "", false, "o1");
        optionGroup.addOption(option);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1", false, "o1");
        Option option2 = new Option("option2", "Option 2", false, "o2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testGetNamesEmptyOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        assertEquals(Collections.emptyList(), optionGroup.getNames());
    }

    @Test
    public void testGetOptionsEmptyOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        assertEquals(Collections.emptyList(), optionGroup.getOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRequiredNegativeBooleanValue() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(-1);
    }

    @Test
    public void testIsRequiredNullOptionGroup() {
        OptionGroup optionGroup = null;
        assertFalse(optionGroup.isRequired());
    }
}