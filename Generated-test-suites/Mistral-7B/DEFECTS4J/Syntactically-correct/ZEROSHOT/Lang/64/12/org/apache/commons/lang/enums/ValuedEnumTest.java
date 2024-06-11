package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.EnumUtils;
import org.junit.Test;

/**
 * Test class for ValuedEnum.
 */
public class ValuedEnumTest {

    /**
     * Test method for {@link ValuedEnum#getValue()}.
     */
    @Test
    public void testGetValue() {
        ValuedEnum javaVersionEnum = ValuedEnum.JAVA1_0;
        int value = javaVersionEnum.getValue();
        assertEquals("Expected value of JavaVersionEnum JAVA1_0 to be 100", 100, value);
    }

    /**
     * Test method for {@link ValuedEnum#compareTo(Object)}.
     */
    @Test
    public void testCompareTo() {
        ValuedEnum javaVersionEnum1 = ValuedEnum.JAVA1_0;
        ValuedEnum javaVersionEnum2 = ValuedEnum.JAVA1_1;
        ValuedEnum javaVersionEnum3 = ValuedEnum.JAVA1_0;

        int comparisonResult1 = javaVersionEnum1.compareTo(javaVersionEnum2);
        int comparisonResult2 = javaVersionEnum1.compareTo(javaVersionEnum3);

        assertEquals("Expected JAVA1_0 to be less than JAVA1_1", -1, comparisonResult1);
        assertEquals("Expected JAVA1_0 to be equal to itself", 0, comparisonResult2);
    }

    /**
     * Test method for {@link ValuedEnum#getEnum(Class, String)}.
     */
    @Test
    public void testGetEnumByString() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum(ValuedEnum.class, "JAVA1_0");
        assertNotNull("Expected JAVA1_0 to not be null", javaVersionEnum);
        assertEquals("Expected JAVA1_0 to have value 100", 100, javaVersionEnum.getValue());
    }

    /**
     * Test method for {@link ValuedEnum#getEnum(Class, int)}.
     */
    @Test
    public void testGetEnumByValue() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum(ValuedEnum.class, 100);
        assertNotNull("Expected JAVA1_0 to not be null", javaVersionEnum);
        assertEquals("Expected JAVA1_0 to have name 'Java 1.0'", "Java 1.0", javaVersionEnum.getName());
    }

    /**
     * Test method for {@link ValuedEnum#getEnumMap()}.
     */
    @Test
    public void testGetEnumMap() {
        Method getEnumMapMethod = ValuedEnum.class.getDeclaredMethod("getEnumMap");
        getEnumMapMethod.setAccessible(true);
        Object enumMap = getEnumMapMethod.invoke(null);

        List enumList = (List) EnumUtils.getValues(ValuedEnum.class);
        assertEquals("Expected size of enum map to be equal to size of enum list", enumList.size(), enumMap.getClass().getMethod("size").invoke(enumMap));

        ValuedEnum javaVersionEnum = (ValuedEnum) enumList.get(0);
        Object enumMapEntry = enumMap.getClass().getMethod("get", javaClassToObjectType(javaVersionEnum.getClass())).invoke(enumMap, javaVersionEnum);
        assertEquals("Expected name of first enum to be 'Java 1.0'", javaVersionEnum.getName(), enumMapEntry.getClass().getMethod("getName").invoke(enumMapEntry));
        assertEquals("Expected value of first enum to be 100", javaVersionEnum.getValue(), enumMapEntry.getClass().getMethod("getValue").invoke(enumMapEntry));
    }

    /**
     * Test method for {@link ValuedEnum#getEnumList()}.
     */
    @Test
    public void testGetEnumList() {
        List enumList = ValuedEnum.getEnumList();
        assertEquals("Expected size of enum list to be 3", 3, enumList.size());

        for (Object enumObject : enumList) {
            ValuedEnum enumValue = (ValuedEnum) enumObject;
            assertEquals("Expected name of enum to start with 'JAVA'", enumValue.getName().startsWith("JAVA"), true);
        }
    }

    /**
     * Test method for {@link ValuedEnum#iterator()}.
     */
    @Test
    public void testIterator() {
        Iterator iterator = ValuedEnum.iterator();
        List enumList = ValuedEnum.getEnumList();

        int i = 0;
        while (iterator.hasNext()) {
            ValuedEnum enumValue = (ValuedEnum) iterator.next();
            assertEquals("Expected name of enum to be equal to name in enum list", enumList.get(i), enumValue);
            i++;
        }
    }

    /**
     * Helper method to convert a Java class to its Object type.
     *
     * @param clazz Java class
     * @return Object type
     */
    private static Class javaClassToObjectType(Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        return Class.forName("java.lang.Object");
    }
}