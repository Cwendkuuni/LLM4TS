// Your complete JUnit test file (OptionTest.java) should begin with this comment

package org.junit.test; // Change the package name as needed

import org.apache.commons.cli.Option;
import org.junit.*;
import static org.junit.Assert.*;

public class OptionTest {
    @Test
    public void testGetId() {
        Option option = new Option('a', "Test Option");
        int expectedId = 'a';
        int actualId = option.getId();
        assertEquals(expectedId, actualId);
    }
    
    @Test
    public void testOptionConstructionWithNullOpt() {
        try {
            new Option(null, "Test Option");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid option character: null", e.getMessage());
        }
    }
    
    @Test
    public void testOptionConstructionWithEmptyOpt() {
        try {
            new Option("", "Test Option");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid option character: ", e.getMessage());
        }
    }
    
    @Test
    public void testOptionConstructionWithNegativeNumberOfArgs() {
        try {
            new Option('a', "Test Option", -1, "Test Description");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Number of arguments cannot be negative", e.getMessage());
        }
    }
    
    @Test
    public void testAddMoreValuesThanAllowed() {
        Option option = new Option('a', "Test Option", false, "Test Description");
        option.setArgs(1);
        option.addValueForProcessing("Value 1");
        option.addValueForProcessing("Value 1"); // This should throw an exception
    }
    
    @Test(expected = RuntimeException.class)
    public void testAddValueWithWrongValueSeparator() {
        Option option = new Option('a', "Test Option", false, "Test Description");
        option.setValueSeparator('|');
        option.addValueForProcessing("Value 1|Value 2");
    }
    
    @Test
    public void testGetValueWhenNoValues() {
        Option option = new Option('a', "Test Option", false, "Test Description");
        assertNull(option.getValue());
    }
    
    @Test
    public void testGetValue() {
        Option option = new Option('a', "Test Option", false, "Test Description");
        option.addValueForProcessing("Test Value");
        String expectedValue = "Test Value";
        String actualValue = option.getValue();
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testGetValueWhenMultipleValues() {
        Option option = new Option('a', "Test Option", false, "Test Description");
        option.addValueForProcessing("Test Value 1");
        option.addValueForProcessing("Test Value 2");
        String[] expectedValues = {"Test Value 1", "Test Value 2"};
        String[] actualValues = option.getValues();
        assertArrayEquals(expectedValues, actualValues);
    }
}

// Your complete JUnit test file (OptionTest.java) should end with this comment