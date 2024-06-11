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
    public void testOptionGroupWithValidOptions() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", true, "This is option1");
        Option option2 = new Option("o2", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[--option1, --option2]", optionGroup.toString());

        optionGroup.setSelected(option1);
        assertEquals("option1", optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertNull(optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals("option1", optionGroup.getSelected());
    }

    @Test
    public void testOptionGroupWithRequiredOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);

        Option option1 = new Option("o1", "option1", true, "This is option1");
        Option option2 = new Option("o2", "option2", false, "This is option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertThrows(IllegalArgumentException.class, () -> optionGroup.setSelected(option2));

        optionGroup.setSelected(option1);
        assertEquals("option1", optionGroup.getSelected());
    }

    @Test
    public void testOptionGroupWithSameOptionTwice() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", true, "This is option1");

        optionGroup.addOption(option1);
        optionGroup.addOption(option1);

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));

        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testOptionGroupWithNullOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option = null;
        assertThrows(NullPointerException.class, () -> optionGroup.addOption(option));
    }

    @Test
    public void testOptionGroupWithEmptyList() {
        OptionGroup optionGroup = new OptionGroup();

        assertEquals("[]", optionGroup.toString());
        assertFalse(optionGroup.isRequired());
    }
}