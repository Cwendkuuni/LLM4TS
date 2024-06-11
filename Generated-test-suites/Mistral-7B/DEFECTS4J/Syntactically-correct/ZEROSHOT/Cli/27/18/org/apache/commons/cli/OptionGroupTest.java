package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;

public class OptionGroupTest {

    @SuppressWarnings("unchecked")
    private OptionGroup createOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new OptionBuilder("option1").build();
        Option option2 = new OptionBuilder("option2").build();
        Option option3 = new OptionBuilder("option3").build();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);

        return optionGroup;
    }

    @org.junit.Test
    public void testGetNames() {
        OptionGroup optionGroup = createOptionGroup();
        Collection<String> names = optionGroup.getNames();

        assertEquals(3, names.size());
        List<String> expectedNames = Arrays.asList("option1", "option2", "option3");
        assertEquals(expectedNames, names);
    }

    @org.junit.Test
    public void testGetOptions() {
        OptionGroup optionGroup = createOptionGroup();
        Collection<Option> options = optionGroup.getOptions();

        assertEquals(3, options.size());
        List<Option> expectedOptions = Arrays.asList(option1, option2, option3);
        assertEquals(expectedOptions, options);
    }

    @org.junit.Test
    public void testSetSelectedNull() {
        OptionGroup optionGroup = createOptionGroup();
        optionGroup.setSelected(null);

        assertNull(optionGroup.getSelected());
    }

    @org.junit.Test
    public void testSetSelectedSameOption() {
        OptionGroup optionGroup = createOptionGroup();
        Option option = optionGroup.getOptions().iterator().next();

        optionGroup.setSelected(option);
        String selected = optionGroup.getSelected();

        assertSame(option, selected);
    }

    @org.junit.Test(expected = AlreadySelectedException.class)
    public void testSetSelectedDifferentOption() {
        OptionGroup optionGroup = createOptionGroup();
        Option option1 = optionGroup.getOptions().iterator().next();
        Option option2 = optionGroup.getOptions().iterator().next();
        Option option3 = optionGroup.getOptions().iterator().next();

        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @org.junit.Test
    public void testIsRequiredFalse() {
        OptionGroup optionGroup = createOptionGroup();
        optionGroup.setRequired(false);

        assertFalse(optionGroup.isRequired());
    }

    @org.junit.Test
    public void testIsRequiredTrue() {
        OptionGroup optionGroup = createOptionGroup();
        optionGroup.setRequired(true);

        assertTrue(optionGroup.isRequired());
    }

    @org.junit.Test(expected = ParseException.class)
    public void testToString() {
        OptionGroup optionGroup = createOptionGroup();

        // Invoke toString() method
        optionGroup.toString();
    }

    @org.junit.Test
    public void testToStringOutput() {
        OptionGroup optionGroup = createOptionGroup();
        String toString = optionGroup.toString();

        // Assert that the output is what we expect
        String expected = "[-option1 Option 1\n--option2 Option 2\n--option3 Option 3 ]";
        assertEquals(expected, toString);
    }

    @org.junit.Test
    public void testEmptyOptionGroupToString() {
        OptionGroup optionGroup = new OptionGroup();
        String toString = optionGroup.toString();

        assertEquals("[]", toString);
    }
}