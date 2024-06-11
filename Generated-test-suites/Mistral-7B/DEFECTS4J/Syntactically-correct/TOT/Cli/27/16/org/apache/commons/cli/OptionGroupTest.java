import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

    @Test
    public void testOptionGroupAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Description of option1");
        Option option2 = new Option("o2", "option2", false, "Description of option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));

        Collection<String> names = optionGroup.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains(option1.getOpt()));
        assertTrue(names.contains(option2.getOpt()));
    }

    @Test
    public void testOptionGroupSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Description of option1");
        Option option2 = new Option("o2", "option2", false, "Description of option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testOptionGroupSetSelectedSameOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Description of option1");

        optionGroup.addOption(option1);

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testOptionGroupSetSelectedDifferentOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Description of option1");
        Option option2 = new Option("o2", "option2", false, "Description of option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testOptionGroupToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", true, "Description of option1");
        Option option2 = new Option("o2", "option2", false, "Description of option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertEquals("[--option1 Description of option1, --option2 Description of option2]", optionGroup.toString());
    }

    @Test
    public void testOptionGroupIsRequired() {
        OptionGroup optionGroup1 = new OptionGroup();
        OptionGroup optionGroup2 = new OptionGroup();

        assertFalse(optionGroup1.isRequired());
        optionGroup2.setRequired(true);
        assertTrue(optionGroup2.isRequired());
    }
}