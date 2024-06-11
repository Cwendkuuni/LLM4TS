package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class OptionGroupTest {
    // Tests by Expert 1

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionName", "optionDescription");

        optionGroup.addOption(option);
        assertFalse(optionGroup.isRequired());
        assertEquals(option.getKey(), optionGroup.getNames().iterator().next());
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option1Description");
        Option option2 = new Option("option2", "option2Description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<String> names = new ArrayList<>(optionGroup.getNames());
        assertEquals(2, names.size());
        assertTrue(names.contains(option1.getKey()));
        assertTrue(names.contains(option2.getKey()));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option1Description");
        Option option2 = new Option("option2", "option2Description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        List<Option> options = new ArrayList<>(optionGroup.getOptions());
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option1Description");
        Option option2 = new Option("option2", "option2Description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option1Description");
        Option option2 = new Option("option2", "option2Description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        optionGroup.setSelected(option1);
        assertEquals(option1.getOpt(), optionGroup.getSelected());

        optionGroup.setSelected(option2);
        assertEquals(option2.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.setRequired(false);
        assertFalse(optionGroup2.isRequired());
    }

    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());

        OptionGroup optionGroup2 = new OptionGroup();
        optionGroup2.setRequired(false);
        assertFalse(optionGroup2.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option1Description");
        Option option2 = new Option("option2", "option2Description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        assertEquals("[--option1 option1Description, --option2 option2Description]", optionGroup.toString());
    }

    // Tests by Expert 2

    @Test
    public void testOptionGroupEmptyConstructor() {
        OptionGroup optionGroup = new OptionGroup();
        assertTrue(optionGroup.isRequired());
        assertTrue(optionGroup.getNames().isEmpty());
        assertTrue(optionGroup.getOptions().isEmpty());
        assertNull(optionGroup.getSelected());
    }

    @Test
    public void testOptionGroupConstructWithRequiredFalse() {
        OptionGroup optionGroup = new OptionGroup(false);
        assertFalse(optionGroup.isRequired());
        assertTrue(optionGroup.getNames().isEmpty());
        assertTrue(optionGroup.getOptions().isEmpty());
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = NullPointerException.class)
    public void testAddOptionNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(null);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedNullOption() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionName", "optionDescription");
        optionGroup.addOption(option);
        optionGroup.setSelected(null);
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelectedOption() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option1Description");
        Option option2 = new Option("option2", "option2Description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    // Tests by Expert 3

    @Test
    public void testOptionGroupConstructorWithOptions() {
        Option option1 = new Option("option1", "option1Description");
        Option option2 = new Option("option2", "option2Description");

        OptionGroup optionGroup = new OptionGroup(Arrays.asList(option1, option2));
        assertFalse(optionGroup.isRequired());
        assertEquals(2, optionGroup.getNames().size());
        assertEquals(option1.getKey(), optionGroup.getNames().iterator().next());
        assertEquals(2, optionGroup.getOptions().size());
        assertTrue(optionGroup.getOptions().contains(option1));
        assertTrue(optionGroup.getOptions().contains(option2));
        assertNull(optionGroup.getSelected());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionExistingOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionName", "optionDescription");
        optionGroup.addOption(option);
        optionGroup.addOption(option);
    }

    @Test
    public void testToStringEmpty() {
        OptionGroup optionGroup = new OptionGroup();
        assertEquals("[ ]", optionGroup.toString());
    }

    @Test
    public void testToStringMultipleOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("option1", "option1Description");
        Option option2 = new Option("option2", "option2Description");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        assertEquals("[--option1 option1Description, --option2 option2Description]", optionGroup.toString());
    }
}