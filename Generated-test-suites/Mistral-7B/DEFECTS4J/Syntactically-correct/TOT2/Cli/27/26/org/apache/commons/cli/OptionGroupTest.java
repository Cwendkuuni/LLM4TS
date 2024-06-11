package org.junit.tests.OptionGroup;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1 description", false, 'o');
        Option addedOption = optionGroup.addOption(option);
        assertEquals(option, addedOption);
    }

    @Test
    public void testEmptyOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        assertEquals(0, optionGroup.getOptions().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(null);
    }

    @Test
    public void testAddSameOptionTwice() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1 description", false, 'o');
        Option addedOption1 = optionGroup.addOption(option);
        Option addedOption2 = optionGroup.addOption(option);
        assertEquals(option, addedOption1);
        assertEquals(option, addedOption2);
    }

    // Test other edge cases and exception handling scenarios

    // Test getting names and options

    @Test
    public void testSetSelectedNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        try {
            OptionGroup optionGroup1 = new OptionGroup();
            optionGroup1.setSelected(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Option option = new Option("option1", "Option 1 description", false, 'o');
        OptionGroup optionGroup = new OptionGroup();
        try {
            optionGroup.setSelected(option);
            optionGroup.setSelected(null);
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }

        OptionGroup optionGroup1 = new OptionGroup();
        optionGroup1.setSelected(option);
        assertEquals(option, optionGroup1.getSelectedOption());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedNonExistentOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1 description", false, 'o');
        optionGroup.addOption(option);
        Option nonExistentOption = new Option("nonExistentOption", "Non-existent option description", false, 'n');
        try {
            optionGroup.setSelected(nonExistentOption);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetSelectedOptionTwice() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1 description", false, 'o');
        Option option2 = new Option("option2", "Option 2 description", false, 'p');
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelectedOption());
        optionGroup.setSelected(option2);
        assertEquals(option2, optionGroup.getSelectedOption());
    }

    @Test
    public void testSetRequiredFalseAndSetOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1 description", false, 'o');
        optionGroup.addOption(option);
        optionGroup.setRequired(false);
        try {
            optionGroup.setSelected(option);
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }

        optionGroup.setRequired(true);
        optionGroup.setSelected(option);
        assertEquals(option, optionGroup.getSelectedOption());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetRequiredTrueAndSetSameOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1 description", false, 'o');
        Option option2 = new Option("option2", "Option 2 description", false, 'p');
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setRequired(true);
        try {
            optionGroup.setSelected(option1);
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            optionGroup.setSelected(option2);
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            optionGroup.setSelected(option1);
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testSetRequiredTrueAndSetDifferentOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1 description", false, 'o');
        Option option2 = new Option("option2", "Option 2 description", false, 'p');
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setRequired(true);
        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelectedOption());
        optionGroup.setSelected(option2);
        assertEquals(option2, optionGroup.getSelectedOption());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Option 1 description", false, 'o');
        Option option2 = new Option("option2", "Option 2 description", false, 'p');
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals("[--option1 Option 1 description, --option2 Option 2 description]", optionGroup.toString());
    }

    // Test other methods
}