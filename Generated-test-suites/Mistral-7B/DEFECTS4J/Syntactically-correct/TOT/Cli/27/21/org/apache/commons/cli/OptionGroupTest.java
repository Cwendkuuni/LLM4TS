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
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Description of option1");
        Option option2 = new Option("o2", "opt2", true, "Description of option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Description of option1");
        Option option2 = new Option("o2", "opt2", true, "Description of option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains("o1"));
        assertTrue(names.contains("o2"));
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Description of option1");
        Option option2 = new Option("o2", "opt2", true, "Description of option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSameOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Description of option1");
        Option option2 = new Option("o2", "opt2", true, "Description of option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option1);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "opt1", false, "Description of option1");
        Option option2 = new Option("o2", "opt2", true, "Description of option2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option2);
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup1 = new OptionGroup();
        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup1.setRequired(true);
        optionGroup2.setRequired(false);

        assertTrue(optionGroup1.isRequired());
        assertFalse(optionGroup2.isRequired());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSelectedNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = null;
        optionGroup.setSelected(option);
    }
}