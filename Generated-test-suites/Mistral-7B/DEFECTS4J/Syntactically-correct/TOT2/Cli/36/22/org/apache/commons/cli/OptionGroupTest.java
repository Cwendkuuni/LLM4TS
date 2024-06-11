import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        String optionName = "testOption";
        Option option = new Option(optionName, null, null);

        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();

        assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", null, null);
        Option option2 = new Option("option2", null, null);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> names = optionGroup.getNames();

        assertTrue(names.contains("option1"));
        assertTrue(names.contains("option2"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", null, null);
        Option option2 = new Option("option2", null, null);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> options = optionGroup.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option nullOption = null;

        optionGroup.addOption(nullOption);
    }

    @Test(expected = NullPointerException.class)
    public void testAddOptionWithNullName() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option(null, null, null);

        optionGroup.addOption(option);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithDuplicateName() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", null, null);
        Option option2 = new Option("option1", null, null);

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option", null, null);

        optionGroup.setSelected(option);

        assertEquals(option.getKey(), optionGroup.getSelected());
    }

    @Test(expected = NullPointerException.class)
    public void testSetSelectedWithNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option nullOption = null;

        optionGroup.setSelected(nullOption);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSelectedWithNonExistentOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("nonExistentOption", null, null);

        optionGroup.setSelected(option);
    }

    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option", null, null);

        optionGroup.setSelected(option);

        assertEquals(option.getKey(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        assertFalse(optionGroup.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRequiredWithInvalidBooleanValue() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(-1);
    }

    @Test
    public void testIsRequiredWithEmptyOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();

        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", null, null);
        Option option2 = new Option("option2", null, null);
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[--option1, --option2]";

        assertEquals(expected, optionGroup.toString());
    }
}