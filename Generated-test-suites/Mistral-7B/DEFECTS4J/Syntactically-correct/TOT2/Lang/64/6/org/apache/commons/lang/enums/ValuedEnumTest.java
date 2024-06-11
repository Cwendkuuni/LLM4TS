package org.apache.commons.lang.enums;

import org.junit.*;
import static org.junit.Assert.*;

public class ValuedEnumTest {
    private static final String TEST_NAME = "Test";
    private static final int TEST_VALUE = 1;

    @Test
    public void testgetValue() {
        ValuedEnum testEnum = new TestValuedEnum(TEST_NAME, TEST_VALUE);
        int actualValue = testEnum.getValue();
        int expectedValue = TEST_VALUE;
        assertEquals(expectedValue, actualValue);
    }

    // Edge cases for constructor
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValueConstructor() {
        try {
            new ValuedEnum(TEST_NAME, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullNameConstructor() {
        try {
            new ValuedEnum(null, TEST_VALUE);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
    }

    // Edge cases for getEnum() method
    @Test
    public void testGetEnumNullEnumClass() {
        Class enumClass = null;
        Enum testEnum = ValuedEnum.getEnum(enumClass, TEST_VALUE);
        assertNull(testEnum);
    }

    @Test
    public void testGetEnumNullValue() {
        Enum testEnum = ValuedEnum.getEnum(ValuedEnum.class, null);
        assertNull(testEnum);
    }

    // Exception handling for compareTo() method
    @Test(expected = ClassCastException.class)
    public void testCompareToNonInstanceOfValuedEnum() {
        Object other = new Object();
        int actualCompareTo = new TestValuedEnum(TEST_NAME, TEST_VALUE).compareTo(other);
        fail("Expected ClassCastException");
    }
}

class TestValuedEnum extends ValuedEnum {
    TestValuedEnum(String name, int value) {
        super(name, value);
    }
}