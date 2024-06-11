package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for OptionGroup.
 */
public class OptionGroupTest {

    /**
     * Test method for {@link OptionGroup#addOption(Option)} method.
     */
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();

        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Map<String, Option> expectedOptionMap = new HashMap<>();
        expectedOptionMap.put("o1", option1);
        expectedOptionMap.put("o2", option2);

        Assert.assertEquals(expectedOptionMap, optionGroup.optionMap);
    }

    /**
     * Test method for {@link OptionGroup#getNames()} method.
     */
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<String> expectedNames = Collections.unmodifiableCollection(optionMap.keySet());

        Assert.assertEquals(expectedNames, optionGroup.getNames());
    }

    /**
     * Test method for {@link OptionGroup#getOptions()} method.
     */
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Collection<Option> expectedOptions = Collections.unmodifiableCollection(optionMap.values());

        Assert.assertEquals(expectedOptions, optionGroup.getOptions());
    }

    /**
     * Test method for {@link OptionGroup#setSelected(Option)} method.
     */
    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            String selected = optionGroup.getSelected();
            Assert.assertEquals("o1", selected);
        } catch (AlreadySelectedException e) {
            // should not reach here
            Assert.fail("Unexpected AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option2);
            String selected = optionGroup.getSelected();
            Assert.assertEquals("o2", selected);
        } catch (AlreadySelectedException e) {
            // should not reach here
            Assert.fail("Unexpected AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option1);
            Assert.fail("AlreadySelectedException should be thrown");
        } catch (AlreadySelectedException e) {
            // expected
        }
    }

    /**
     * Test method for {@link OptionGroup#getSelected()} method.
     */
    @Test
    public void testGetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            String selected = optionGroup.getSelected();
            Assert.assertEquals("o1", selected);
        } catch (AlreadySelectedException e) {
            // should not reach here
            Assert.fail("Unexpected AlreadySelectedException");
        }

        try {
            optionGroup.setSelected(option2);
            String selected = optionGroup.getSelected();
            Assert.assertEquals("o2", selected);
        } catch (AlreadySelectedException e) {
            // should not reach here
            Assert.fail("Unexpected AlreadySelectedException");
        }

        String selected = optionGroup.getSelected();
        Assert.assertNull(selected);
    }

    /**
     * Test method for {@link OptionGroup#setRequired(boolean)} method.
     */
    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());

        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#isRequired()} method.
     */
    @Test
    public void testIsRequired() {
        OptionGroup optionGroup = new OptionGroup();

        Assert.assertFalse(optionGroup.isRequired());

        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    /**
     * Test method for {@link OptionGroup#toString()} method.
     */
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("o1", "option1", false, "Description for option1");
        Option option2 = new Option("o2", "option2", false, "Description for option2");

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        String expectedToString = "[-o1 Description for option1, --o2 Description for option2]";

        String toString = optionGroup.toString();
        Assert.assertEquals(expectedToString, toString);
    }
}