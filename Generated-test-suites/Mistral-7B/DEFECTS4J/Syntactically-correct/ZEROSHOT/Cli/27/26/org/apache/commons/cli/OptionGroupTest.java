import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest {

    /**
     * Test method for {@link OptionGroup#addOption(Option)}.
     */
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Map<String, Option> expectedOptionMap = new HashMap<>();
        expectedOptionMap.put("option1", option1);
        expectedOptionMap.put("option2", option2);

        Map<String, Option> actualOptionMap = (Map<String, Option>) optionGroup.getOptionMap();

        Assert.assertEquals(expectedOptionMap, actualOptionMap);
    }

    /**
     * Test method for {@link OptionGroup#getNames()}.
     */
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> expectedNames = Collections.singletonList("option1");;
        Collection<String> actualNames = optionGroup.getNames();

        Assert.assertEquals(expectedNames, actualNames);
    }

    /**
     * Test method for {@link OptionGroup#getOptions()}.
     */
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> expectedOptions = Collections.singletonList(option1);
        Collection<Option> actualOptions = optionGroup.getOptions();

        Assert.assertEquals(expectedOptions, actualOptions);
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)}.
     */
    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            String expectedSelected = option1.getOpt();
            String actualSelected = optionGroup.getSelected();

            Assert.assertEquals(expectedSelected, actualSelected);
        } catch (AlreadySelectedException e) {
            // Should not reach here since the option is not selected initially
            Assert.fail();
        }

        try {
            optionGroup.setSelected(option2);
            String expectedSelected = option2.getOpt();
            String actualSelected = optionGroup.getSelected();

            Assert.assertEquals(expectedSelected, actualSelected);
        } catch (AlreadySelectedException e) {
            // Should not reach here since the option is not selected initially
            Assert.fail();
        }

        try {
            optionGroup.setSelected(option1);
            String expectedSelected = option1.getOpt();
            String actualSelected = optionGroup.getSelected();

            Assert.assertEquals(expectedSelected, actualSelected);
        } catch (AlreadySelectedException e) {
            Assert.assertEquals(optionGroup, e.getOptionGroup());
            Assert.assertEquals(option1, e.getOption());
        }
    }

    /**
     * Test method for {@link OptionGroup#getSelected()}.
     */
    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // Test with no selected option
        Assert.assertNull(optionGroup.getSelected());

        try {
            optionGroup.setSelected(option1);
            String expectedSelected = option1.getOpt();
            String actualSelected = optionGroup.getSelected();

            Assert.assertEquals(expectedSelected, actualSelected);
        } catch (AlreadySelectedException e) {
            // Should not reach here since the test case sets the selected option first
            Assert.fail();
        }
    }

    /**
     * Test method for {@link OptionGroup#setRequired(boolean)}.
     */
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);
        boolean expectedRequired = true;
        boolean actualRequired = optionGroup.isRequired();

        Assert.assertEquals(expectedRequired, actualRequired);

        optionGroup.setRequired(false);
        expectedRequired = false;
        actualRequired = optionGroup.isRequired();

        Assert.assertEquals(expectedRequired, actualRequired);
    }

    /**
     * Test method for {@link OptionGroup#isRequired()}.
     */
    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        boolean expectedRequired = true;
        boolean actualRequired = optionGroup.isRequired();

        Assert.assertEquals(expectedRequired, actualRequired);
    }

    /**
     * Test method for {@link OptionGroup#toString()}.
     */
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "Description for option1");
        Option option2 = new Option("option2", "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedToString = "[-option1 Description for option1, --option2 Description for option2 ]";
        String actualToString = optionGroup.toString();

        Assert.assertEquals(expectedToString, actualToString);
    }
}