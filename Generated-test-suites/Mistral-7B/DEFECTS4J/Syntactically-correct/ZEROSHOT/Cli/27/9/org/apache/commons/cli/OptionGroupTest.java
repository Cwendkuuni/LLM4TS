import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest
{
    private OptionGroup optionGroup;

    @Before
    public void setup()
    {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption()
    {
        Option option1 = new Option("opt1", "option 1");
        Option option2 = new Option("opt2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Set<String> expectedNames = new HashSet<>(Arrays.asList("opt1", "opt2"));
        Set<Option> expectedOptions = new HashSet<>(Arrays.asList(option1, option2));

        assertEquals(expectedNames, optionGroup.getNames());
        assertEquals(expectedOptions, optionGroup.getOptions());
    }

    @Test
    public void testGetSelectedIsNull()
    {
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedNull()
    {
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedNoPreviousSelection()
    {
        Option option1 = new Option("opt1", "option 1");

        optionGroup.addOption(option1);

        optionGroup.setSelected(option1);

        assertEquals("opt1", optionGroup.getSelected());
    }

    @Test
    public void testSetSelectedSameOptionTwice()
    {
        Option option1 = new Option("opt1", "option 1");

        optionGroup.addOption(option1);

        optionGroup.setSelected(option1);

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
    }

    @Test
    public void testSetSelectedDifferentOption()
    {
        Option option1 = new Option("opt1", "option 1");
        Option option2 = new Option("opt2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);

        assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option2));
    }

    @Test
    public void testIsRequiredFalseByDefault()
    {
        assertFalse(optionGroup.isRequired());
    }

    @Test
    public void testSetRequired()
    {
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

    @Test
    public void testToString()
    {
        Option option1 = new Option("opt1", "option 1");
        Option option2 = new Option("opt2", "option 2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expected = "[-opt1 option 1, --opt2 option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}