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
    public void testOptionGroupCreation() {
        OptionGroup optionGroup = new OptionGroup();
        Assert.assertNotNull(optionGroup);
        Assert.assertNotNull(optionGroup.getSelected());
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Set<String> expectedOptions = new HashSet<>(Arrays.asList("o1", "o2"));
        Set<String> actualOptions = new HashSet<>(optionGroup.getNames());

        Assert.assertEquals(expectedOptions, actualOptions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "o1-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option1);
    }

    @Test
    public void testSetSelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            String selectedOption = optionGroup.getSelected();
            Assert.assertEquals("o1", selectedOption);
        } catch (AlreadySelectedException e) {
            Assert.fail("Should not have thrown AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option2);
            String selectedOption = optionGroup.getSelected();
            Assert.assertEquals("o2", selectedOption);
        } catch (AlreadySelectedException e) {
            Assert.fail("Should not have thrown AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option1);
            String selectedOption = optionGroup.getSelected();
            Assert.assertEquals("o1", selectedOption);
        } catch (AlreadySelectedException e) {
            Assert.assertEquals("An option from this group has already been selected.", e.getMessage());
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "o1-desc");
        Option option2 = new Option("o2", "option2", false, "o2-desc");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedOptionGroup = "[--o1 o1-desc, --o2 o2-desc]";
        String actualOptionGroup = optionGroup.toString();

        Assert.assertEquals(expectedOptionGroup, actualOptionGroup);
    }
}