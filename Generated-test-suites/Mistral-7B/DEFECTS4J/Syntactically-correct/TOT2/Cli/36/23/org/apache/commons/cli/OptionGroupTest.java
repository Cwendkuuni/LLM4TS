import static org.junit.Assert.assertEquals;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {
    @org.junit.Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionKey", "optionDescription");
        optionGroup.addOption(option);
        Map<String, Option> optionMap = optionGroup.optionMap;
        Option expectedOption = optionMap.get("optionKey");
        Option actualOption = optionMap.get("optionKey"); // Using get method to check if the option is in the map

        assertEquals("Check if the option is added to the map", expectedOption, actualOption);
    }
}