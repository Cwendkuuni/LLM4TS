import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", true, "Optional description");
        optionGroup.addOption(option);
        Collection options = optionGroup.getOptions();
        assert option.equals(options.iterator().next());
    }
}