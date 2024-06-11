package org.junit.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    OptionGroup optionGroup;
    Option option1, option2;

    @Before
    public void setUp() {
        optionGroup = new OptionGroup();
        option1 = new Option("o1", "opt1", false, "This is option 1");
        option2 = new Option("o2", "opt2", false, "This is option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
    }

    @Test
    public void testOptionGroupWithSingleOption() {
        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelected());
    }

    @Test
    public void testOptionGroupWithMultipleOptions() {
        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2, optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelected());
    }

    @Test(expected = org.apache.commons.cli.AlreadySelectedException.class)
    public void testOptionGroupWithSameOptionSelectedTwice() {
        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelected());

        optionGroup.setSelected(option1);
    }

    @Test
    public void testOptionGroupWithRequiredOption() throws AlreadySelectedException {
        optionGroup.setRequired(true);
        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelected());

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals(option1, optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testOptionGroupWithUnregisteredOption() throws AlreadySelectedException {
        Option unregisteredOption = new Option("u", "opt", false, "This is an unregistered option");
        optionGroup.setSelected(unregisteredOption);
    }
}