import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionName", "Option description");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNullOptionDescription() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option(null, "");
        optionGroup.addOption(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionEmptyOptionName() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("", "Option description");
        optionGroup.addOption(option);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionSameOptionName() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("optionName", "Option description");
        Option option2 = new Option("optionName", "Option description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
    }
}