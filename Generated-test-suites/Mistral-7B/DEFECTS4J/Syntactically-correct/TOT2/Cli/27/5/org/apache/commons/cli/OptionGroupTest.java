import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionName", "optionDescription", false, null, null);
        Option addedOption = optionGroup.addOption(option);
        Assertions.assertNotNull(addedOption);
        Assertions.assertEquals(option, addedOption);
    }
}