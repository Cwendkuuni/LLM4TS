import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testOptionGroupAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Description for option 1");
        Option option2 = new Option("b", "option2", false, "Description for option 2");
        Option option3 = new Option("c", "option3", false, "Description for option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        Assert.assertEquals(3, optionGroup.getOptions().size());
        Assert.assertEquals(Collections.singletonList("a"), optionGroup.getNames());
    }

    @Test
    public void testOptionGroupGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Description for option 1");
        Option option2 = new Option("b", "option2", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals("option1", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // should not reach here
            Assert.fail();
        }

        try {
            optionGroup.setSelected(option2);
            Assert.assertEquals("option2", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            // should not reach here
            Assert.fail();
        }

        try {
            optionGroup.setSelected(option1);
            Assert.assertEquals("option1", optionGroup.getSelected());
        } catch (AlreadySelectedException e) {
            Assert.assertEquals("An option from this group has already been selected.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionGroupAddNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;
        optionGroup.addOption(option);
    }

    @Test
    public void testOptionGroupToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Description for option 1");
        Option option2 = new Option("b", "option2", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals("[ -option1 Description for option 1, -option2 Description for option 2 ]", optionGroup.toString());
    }

    @Test
    public void testOptionGroupIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testOptionGroupGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Description for option 1");
        Option option2 = new Option("b", "option2", false, "Description for option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Assert.assertEquals(Arrays.asList("a", "b"), optionGroup.getNames());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testOptionGroupSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Description for option 1");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);

        try {
            optionGroup.setSelected(option1);
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    @Test
    public void testOptionGroupSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();

        try {
            optionGroup.setSelected(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}