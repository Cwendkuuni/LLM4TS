import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        assertEquals(2, options.size());
        assertEquals(option1, options.iterator().next());
        assertEquals(option2, options.iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2);
            fail("Should have thrown AlreadySelectedException");
        } catch (AlreadySelectedException e) {
            // pass
        }

        optionGroup.setSelected(option1);

        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }
}