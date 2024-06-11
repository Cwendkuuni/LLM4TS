import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

    OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();

        Option option1 = new Option("option1", "This is option 1", true, "o1");
        Option option2 = new Option("option2", "This is option 2", true, "o2");
        Option option3 = new Option("option3", "This is option 3", false, "o3");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
    }

    @Test
    public void testSetSelected() {
        try {
            optionGroup.setSelected(option1);
            optionGroup.setSelected(option2); // This should fail with AlreadySelectedException
        } catch (AlreadySelectedException e) {
            // Expected exception
        }

        optionGroup.setSelected(option3);

        assertEquals("o3", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedTwiceWithSameOption() {
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test
    public void testGetOptionsAndNames() {
        Collection<Option> options = optionGroup.getOptions();
        Collection<String> names = optionGroup.getNames();

        assertEquals(3, options.size());
        assertEquals(3, names.size());

        Iterator<Option> optionsIterator = options.iterator();
        Iterator<String> namesIterator = names.iterator();

        Option expectedOption1 = optionsIterator.next();
        String expectedName1 = namesIterator.next();

        Option expectedOption2 = optionsIterator.next();
        String expectedName2 = namesIterator.next();

        Option expectedOption3 = optionsIterator.next();
        String expectedName3 = namesIterator.next();

        assertEquals(expectedOption1, optionGroup.getOption(expectedName1));
        assertEquals(expectedOption2, optionGroup.getOption(expectedName2));
        assertEquals(expectedOption3, optionGroup.getOption(expectedName3));

        assertEquals(expectedName1, optionGroup.getOption(expectedOption1).getKey());
        assertEquals(expectedName2, optionGroup.getOption(expectedOption2).getKey());
        assertEquals(expectedName3, optionGroup.getOption(expectedOption3).getKey());
    }

    @Test
    public void testToString() {
        assertEquals("[--option1 This is option 1, --option2 This is option 2, -o3 This is option 3]", optionGroup.toString());
    }

    @Test
    public void testIsRequired() {
        OptionGroup requiredOptionGroup = new OptionGroup();
        requiredOptionGroup.setRequired(true);

        OptionGroup notRequiredOptionGroup = new OptionGroup();

        assertFalse(notRequiredOptionGroup.isRequired());
        assertTrue(requiredOptionGroup.isRequired());
    }

}