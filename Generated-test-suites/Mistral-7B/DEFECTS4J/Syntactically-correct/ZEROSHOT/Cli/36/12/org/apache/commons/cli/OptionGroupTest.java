package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1");
        Option option2 = new Option("o2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals(2, optionGroup.getOptions().size());
        assertEquals(Arrays.asList("o1", "o2"), optionGroup.getNames());
    }

    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1");
        Option option2 = new Option("o2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertNull(optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
            assertEquals(option1.getKey(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // should not reach here
        }

        try {
            optionGroup.setSelected(option2);
            assertEquals(option2.getKey(), optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // should not reach here
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1");

        optionGroup.addOption(option1);

        assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "Option 1");
        Option option2 = new Option("o2", "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--o1 Option 1, --o2 Option 2]";
        assertEquals(expected, optionGroup.toString());
    }

    @Test
    public void testEmptyGroup() {
        OptionGroup optionGroup = new OptionGroup();
        assertTrue(optionGroup.getOptions().isEmpty());
        assertTrue(optionGroup.getNames().isEmpty());
    }
}