import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OptionGroupTest {

    @Test
    public void testOptionGroupWithNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Assert.assertNull(optionGroup.getSelected());

        try {
            optionGroup.setSelected(null);
            Assert.fail();
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    @Test
    public void testOptionGroupAddOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(2, optionGroup.getOptions().size());
        Assert.assertEquals(Arrays.asList("o1", "o2"), new HashSet<>(optionGroup.getNames()));
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
            Assert.assertEquals("option1", optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(option2);
            Assert.assertEquals("option2", optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals("option1", optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
        }

        try {
            optionGroup.setSelected(option2);
            Assert.assertEquals("option2", optionGroup.getSelected());
        } finally {
            optionGroup.setSelected(null);
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

    @Test
    public void testOptionGroupIsRequired() {
        OptionGroup optionGroup1 = new OptionGroup();
        OptionGroup optionGroup2 = new OptionGroup();

        optionGroup1.setRequired(true);
        optionGroup2.setRequired(false);

        Assert.assertTrue(optionGroup1.isRequired());
        Assert.assertFalse(optionGroup2.isRequired());
    }

    @Test
    public void testOptionGroupToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals("[-o1 Option 1, -o2 Option 2]", optionGroup.toString());
    }
}