package org.apache.commons.lang.enums.test;

import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        assertEquals("JavaVersionEnum.JAVA1_0", java1_0.toString());
        int java1_0Value = java1_0.getValue();
        assertEquals(100, java1_0Value);

        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
        assertEquals("JavaVersionEnum.JAVA1_1", java1_1.toString());
        int java1_1Value = java1_1.getValue();
        assertEquals(110, java1_1Value);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumNullClass() {
        ValuedEnum.getEnum(null, "JAVA1_0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullName() {
        ValuedEnum.getEnum("JavaVersionEnum", null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumNullNameValue() {
        ValuedEnum.getEnum("JavaVersionEnum", null);
    }

    @Test
    public void testGetEnumInvalidName() {
        ValuedEnum invalidEnum = ValuedEnum.getEnum("JavaVersionEnum", "INVALID_ENUM");
        assertNull(invalidEnum);
    }

    @Test
    public void testGetEnumInvalidValue() {
        ValuedEnum invalidEnum = ValuedEnum.getEnum("JavaVersionEnum", 150);
        assertNull(invalidEnum);
    }

    @Test
    public void testGetEnumMap() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(enumClass);
        ValuedEnum java1_0 = enumMap.get("JAVA1_0");
        assertEquals("JavaVersionEnum.JAVA1_0", java1_0.toString());
        int java1_0Value = java1_0.getValue();
        assertEquals(100, java1_0Value);

        ValuedEnum java1_1 = enumMap.get("JAVA1_1");
        assertEquals("JavaVersionEnum.JAVA1_1", java1_1.toString());
        int java1_1Value = java1_1.getValue();
        assertEquals(110, java1_1Value);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumMapNullClass() {
        ValuedEnum.getEnumMap(null);
    }

    @Test
    public void testGetEnumList() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        ValuedEnum java1_0 = enumList.get(0);
        assertEquals("JavaVersionEnum.JAVA1_0", java1_0.toString());
        int java1_0Value = java1_0.getValue();
        assertEquals(100, java1_0Value);

        ValuedEnum java1_1 = enumList.get(1);
        assertEquals("JavaVersionEnum.JAVA1_1", java1_1.toString());
        int java1_1Value = java1_1.getValue();
        assertEquals(110, java1_1Value);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumListNullClass() {
        ValuedEnum.getEnumList(null);
    }

    @Test
    public void testCompareTo() {
        ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum_0", 0) {
            @Override
            public int compareTo(Object other) {
                ValuedEnum otherValuedEnum = (ValuedEnum) other;
                return super.compareTo(otherValuedEnum) - 1;
            }
        };

        ValuedEnum java1_1 = new ValuedEnum("JavaVersionEnum_1", 1) {
            @Override
            public int compareTo(Object other) {
                ValuedEnum otherValuedEnum = (ValuedEnum) other;
                return super.compareTo(otherValuedEnum) + 1;
            }
        };

        int java1_0Java1_1Comparison = java1_0.compareTo(java1_1);
        int java1_1Java1_0Comparison = java1_1.compareTo(java1_0);

        // Java 1.0 should be less than Java 1.1
        // Java 1.1 should be greater than Java 1.0
        int expectedJava1_0Java1_1Comparison = -1;
        int expectedJava1_1Java1_0Comparison = 1;

        assertEquals(expectedJava1_0Java1_1Comparison, java1_0Java1_1Comparison);
        assertEquals(expectedJava1_1Java1_0Comparison, java1_1Java1_0Comparison);
    }
}