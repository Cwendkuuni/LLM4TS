package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testOptionGroupAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "output", false, "Print output");
        optionGroup.addOption(option);

        // Test that option was added to the group
        assertEquals(option, optionGroup.getOptions().iterator().next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionGroupAddNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;
        optionGroup.addOption(option);
    }

    @Test
    public void testOptionGroupSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            assertEquals(option1, optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            optionGroup.setSelected(option2);
            assertEquals(option2, optionGroup.getSelected());
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testOptionGroupSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
}
