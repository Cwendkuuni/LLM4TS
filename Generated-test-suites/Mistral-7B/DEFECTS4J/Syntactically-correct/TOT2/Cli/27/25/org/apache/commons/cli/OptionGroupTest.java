import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("o", "option", true, "Option description");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        Assert.assertEquals(1, options.size());
        Option actualOption = options.iterator().next();
        Assert.assertEquals(option, actualOption);
    }
}