import static org.junit.Assert.assertEquals;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    @org.junit.Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionName", "optionDescription");
        optionGroup.addOption(option);
        Collection optionNames = optionGroup.getNames();
        assertEquals(optionNames.size(), 1);
        assertEquals(optionNames.iterator().next(), option.getKey());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddEmptyOptionName() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("", "optionDescription");
        optionGroup.addOption(option);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testAddOptionsWhenGroupIsFull() {
        OptionGroup optionGroup = new OptionGroup();
        for (int i = 0; i < OptionGroup.MAX_OPTIONS; i++) {
            Option option = new Option("option" + i, "optionDescription" + i);
            optionGroup.addOption(option);
        }
        Option newOption = new Option("optionNew", "optionDescriptionNew");
        optionGroup.addOption(newOption);
    }

    @org.junit.Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "optionDescription1");
        Option option2 = new Option("option2", "optionDescription2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection optionNames = optionGroup.getNames();
        assertEquals(optionNames.size(), 2);
        assertEquals(optionNames.iterator().next(), option1.getKey());
        assertEquals(optionNames.iterator().next(), option2.getKey());
    }

    @org.junit.Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "optionDescription1");
        Option option2 = new Option("option2", "optionDescription2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection options = optionGroup.getOptions();
        assertEquals(options.size(), 2);
        assertEquals(options.iterator().next(), option1);
        assertEquals(options.iterator().next(), option2);
    }

    @org.junit.Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "optionDescription1");
        Option option2 = new Option("option2", "optionDescription2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @org.junit.Test
    public void testSetSelectedNull() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option", "optionDescription");
        optionGroup.setSelected(option);
        optionGroup.setSelected(null);
        assertEquals(optionGroup.getSelected(), null);
    }

    @org.junit.Test
    public void testSetSelectedNotSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option", "optionDescription");
        assertEquals(optionGroup.getSelected(), null);
        optionGroup.setSelected(option);
        assertEquals(optionGroup.getSelected(), option.getOpt());
        optionGroup.setSelected(null);
        assertEquals(optionGroup.getSelected(), null);
        optionGroup.setSelected(option);
        assertEquals(optionGroup.getSelected(), option.getOpt());
    }

    @org.junit.Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "optionDescription1");
        Option option2 = new Option("option2", "optionDescription2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-option1 optionDescription1, --option2 optionDescription2]";
        assertEquals(optionGroup.toString(), expected);
    }

    @org.junit.Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }
}