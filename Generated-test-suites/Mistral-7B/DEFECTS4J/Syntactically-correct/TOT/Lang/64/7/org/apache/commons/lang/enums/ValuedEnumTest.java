package org.apache.commons.lang.enums;

import static org.junit.Assert.*;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;

/**
 * Test class for ValuedEnum abstract class.
 */
public class ValuedEnumTest {

    /**
     * Test for getEnum method with a valid enum value.
     */
    @Test
    public void testGetEnumValidValue() {
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(ValuedEnum.class, ValuedEnum.JAVA1_0_VALUE);
        assertNotNull(javaVersion1_0);
        assertEquals(ValuedEnum.JAVA1_0.getName(), javaVersion1_0.getName());
        assertEquals(ValuedEnum.JAVA1_0_VALUE, javaVersion1_0.getValue());
    }

    /**
     * Test for getEnum method with an invalid enum value.
     */
    @Test(expected = NullPointerException.class)
    public void testGetEnumInvalidValue() {
        ValuedEnum emptyEnum = ValuedEnum.getEnum(ValuedEnum.class, 0);
    }

    /**
     * Test for getEnum method with a null enum class.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullClass() {
        ValuedEnum.getEnum(null, 0);
    }

    /**
     * Test for getEnumMap method.
     */
    @Test
    public void testGetEnumMap() {
        Map<ValuedEnum, Integer> enumMap = ValuedEnum.getEnumMap(ValuedEnum.class);
        assertEquals(4, enumMap.size());
        assertTrue(enumMap.containsKey(ValuedEnum.JAVA1_0));
        assertTrue(enumMap.containsKey(ValuedEnum.JAVA1_1));
        assertTrue(enumMap.containsKey(ValuedEnum.JAVA1_2));
        assertTrue(enumMap.containsKey(ValuedEnum.JAVA1_3));
    }

    /**
     * Test for getEnumList method.
     */
    @Test
    public void testGetEnumList() {
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(ValuedEnum.class);
        assertEquals(4, enumList.size());
        assertTrue(enumList.contains(ValuedEnum.JAVA1_0));
        assertTrue(enumList.contains(ValuedEnum.JAVA1_1));
        assertTrue(enumList.contains(ValuedEnum.JAVA1_2));
        assertTrue(enumList.contains(ValuedEnum.JAVA1_3));
    }

    /**
     * Test for iterator method.
     */
    @Test
    public void testIterator() {
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(ValuedEnum.class);
        List<ValuedEnum> enumList = new ArrayList<>();
        while (iterator.hasNext()) {
            enumList.add(iterator.next());
        }
        assertEquals(4, enumList.size());
        assertTrue(enumList.contains(ValuedEnum.JAVA1_0));
        assertTrue(enumList.contains(ValuedEnum.JAVA1_1));
        assertTrue(enumList.contains(ValuedEnum.JAVA1_2));
        assertTrue(enumList.contains(ValuedEnum.JAVA1_3));
    }

    /**
     * Test for compareTo method.
     */
    @Test
    public void testCompareTo() {
        ValuedEnum javaVersion1_0 = ValuedEnum.JAVA1_0;
        ValuedEnum javaVersion1_1 = ValuedEnum.JAVA1_1;
        int compareResult = javaVersion1_0.compareTo(javaVersion1_1);
        int expectedCompareResult = javaVersion1_0.getValue() - javaVersion1_1.getValue();
        assertEquals(expectedCompareResult, compareResult);
    }
}