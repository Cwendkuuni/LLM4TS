import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest
{
    @Test
    public void testOptionGroup() throws ParseException
    {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o", "opt1", false, "This is option 1");
        Option option2 = new Option("o", "opt2", false, "This is option 2");
        Option option3 = new Option("o", "opt3", false, "This is option 3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        // Test with no selected option
        Assert.assertNull(optionGroup.getSelected());

        // Test with one option selected
        optionGroup.setSelected(option1);
        Assert.assertEquals("opt1", optionGroup.getSelected());

        // Test with an existing option selected
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
        Assert.assertEquals("opt2", optionGroup.getSelected());

        // Test with an invalid option selected
        optionGroup.setSelected(new Option("invalid", "invalidOption", false, "Invalid option"));
        Assert.assertNull(optionGroup.getSelected());

        // Test required group
        OptionGroup requiredOptionGroup = new OptionGroup();
        requiredOptionGroup.setRequired(true);

        Assert.assertNull(requiredOptionGroup.getSelected());
        try
        {
            requiredOptionGroup.parse("-o", new String[0]);
            Assert.fail();
        }
        catch (ParseException e)
        {
            // expected
        }

        // Test with a valid option in a required group
        Option validOption = new Option("v", "validOption", false, "Valid option");
        requiredOptionGroup.addOption(validOption);
        Assert.assertNull(requiredOptionGroup.getSelected());
        requiredOptionGroup.parse("-o", new String[]{"v"});
        Assert.assertEquals("validOption", requiredOptionGroup.getSelected());

        // Test with an invalid option in a required group
        try
        {
            requiredOptionGroup.parse("-o", new String[]{"invalid"});
            Assert.fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }
}