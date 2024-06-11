import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option one", true, "default1");
        Option option2 = new Option("o2", "option two", false, "default2");
        Option option3 = new Option("o3", "option three", false, "default3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        optionGroup.setSelected(option1);

        // Test with valid options
        Assert.assertEquals("option one", optionGroup.getSelected());

        // Test with invalid options
        try {
            optionGroup.setSelected(option2);
            Assert.fail();
        } catch (AlreadySelectedException e) {
            // expected
        }

        try {
            optionGroup.setSelected(option3);
            Assert.fail();
        } catch (AlreadySelectedException e) {
            // expected
        }

        // Test with null option
        try {
            optionGroup.setSelected(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }
}