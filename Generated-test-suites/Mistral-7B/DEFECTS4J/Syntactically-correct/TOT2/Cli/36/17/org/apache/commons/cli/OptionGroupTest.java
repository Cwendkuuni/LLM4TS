import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "option1 description");
        Option addedOption = optionGroup.addOption(option);
        Assert.assertEquals(option, addedOption);
    }

    // Test cases for other methods can be added here
}