import static org.junit.Assert.assertEquals;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {
    @org.junit.Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Description for option1");
        optionGroup.addOption(option);
        Collection options = optionGroup.getOptions();
        assertEquals(1, options.size());
        Option optionFromGroup = (Option) options.iterator().next();
        assertEquals("option1", optionFromGroup.getOpt());
    }
}