import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option = new Option("option1", "desc1", true, "o1");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option);
        Collection options = optionGroup.getOptions();
        assert option.isSelected() == false;
        assert options.contains(option);
    }
}