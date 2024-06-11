import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OptionGroupTest {

    @Test
    public void testOptionGroup() {
        // Test the creation of an OptionGroup
        OptionGroup optionGroup = new OptionGroup();

        // Test the getters and setters
        Assert.assertEquals("The serialVersionUID should be the same", 1L, optionGroup.serialVersionUID);
        Assert.assertNull("The selected option should be null by default", optionGroup.getSelected());
        Assert.assertFalse("OptionGroup should not be required by default", optionGroup.isRequired());

        // Test the addOption method
        Option option1 = new Option("opt1", "Option 1");
        Option option2 = new Option("opt2", "Option 2");
        Option option3 = new Option("opt3", "Option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // Test the getNames method
        Map<String, Option> expectedOptions = new HashMap<>();
        expectedOptions.put("opt1", option1);
        expectedOptions.put("opt2", option2);
        expectedOptions.put("opt3", option3);

        Assert.assertEquals("The names of the options should match", expectedOptions, optionGroup.getOptionsMap());

        // Test the getOptions method
        Assert.assertEquals("The options should match", Collections.natalOrder(optionGroup.getOptions()), expectedOptions.values());

        // Test the setSelected method
        optionGroup.setSelected(option1);
        Assert.assertEquals("The selected option should be option1", "opt1", optionGroup.getSelected());

        try {
            optionGroup.setSelected(option2);
            Assert.fail("An AlreadySelectedException should be thrown");
        } catch (AlreadySelectedException e) {
            // Expected
        }

        // Test the isRequired method
        optionGroup.setRequired(true);
        Assert.assertTrue("The option group should be required", optionGroup.isRequired());

        // Test the toString method
        Assert.assertEquals("The toString method should return the expected string",
                "[--opt1 Option 1, --opt2 Option 2, --opt3 Option 3]", optionGroup.toString());
    }
}