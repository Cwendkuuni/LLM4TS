import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest {

    @Test
    public void testOptionGroup() {
        // Test instantiation and basic functionality
        OptionGroup optionGroup = new OptionGroup();
        Assert.assertNotNull(optionGroup);
        Assert.assertNull(optionGroup.getSelected());
        Assert.assertFalse(optionGroup.isRequired());

        // Test addOption method
        Option option1 = new Option("opt1", "option 1", false, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");
        Option option3 = new Option("opt3", "option 3", false, "o3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // Test getNames method
        List<String> expectedNames = Arrays.asList("opt1", "opt2", "opt3");
        List<String> names = (List<String>) optionGroup.getNames();
        Assert.assertEquals(expectedNames, names);

        // Test getOptions method
        List<Option> expectedOptions = Arrays.asList(option1, option2, option3);
        List<Option> options = (List<Option>) optionGroup.getOptions();
        Assert.assertEquals(expectedOptions, options);

        // Test setSelected method
        optionGroup.setSelected(option1);
        Assert.assertEquals("o1", optionGroup.getSelected());

        // Test setSelected method with an already selected option
        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
        } catch (AssertionError e) {
            // expected AlreadySelectedException
        }

        // Test setSelected method with a null option
        try {
            optionGroup.setSelected(null);
        } catch (AssertionError e) {
            // expected NullPointerException
        }

        // Test isRequired method
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        // Test toString method
        String expectedToString = "[-o1 option 1, -o2 option 2, -o3 option 3]";
        String toString = optionGroup.toString();
        Assert.assertEquals(expectedToString, toString);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithDuplicateKey() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1", false, "o1");
        Option option2 = new Option("opt1", "option 1", false, "o1");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
    }

    @Test(expected = NullPointerException.class)
    public void testSetSelectedWithNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setSelected(null);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1", false, "o1");

        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyOptionMap() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1", false, "o1");

        Map<String, Option> optionMap = optionGroup.getOptionMap();
        optionMap.put("newKey", new Option("newKey", "new key", false, "newKey"));
    }

    @Test
    public void testGetOptionMap() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1", false, "o1");

        Map<String, Option> optionMap = optionGroup.getOptionMap();
        optionMap.put("key1", option1);

        Option expectedOption = optionMap.get("key1");
        Option option = (Option) optionGroup.getOptions().iterator().next();

        Assert.assertEquals(expectedOption, option);
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("opt1", "option 1", false, "o1");
        Option option2 = new Option("opt2", "option 2", false, "o2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        OptionGroup clonedOptionGroup = (OptionGroup) optionGroup.clone();

        Assert.assertEquals(optionGroup.getOptions(), clonedOptionGroup.getOptions());
        Assert.assertEquals(optionGroup.isRequired(), clonedOptionGroup.isRequired());
        Assert.assertEquals(optionGroup.getSelected(), clonedOptionGroup.getSelected());
    }
}