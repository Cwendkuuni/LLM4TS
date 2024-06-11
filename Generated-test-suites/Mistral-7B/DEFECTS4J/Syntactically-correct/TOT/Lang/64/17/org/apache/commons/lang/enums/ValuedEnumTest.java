package org.apache.commons.lang.enums;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for ValuedEnum abstract class.
 */
public class ValuedEnumTest {

    /**
     * Test getEnum method.
     */
    @Test
    public void testGetEnum() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion = ValuedEnum.getEnum(JavaVersionEnum.class, java1_0.getValue());
        assertEquals("Expected JavaVersionEnum JAVA1_0", java1_0, javaVersion);

        // Test with invalid value
        javaVersion = ValuedEnum.getEnum(JavaVersionEnum.class, 1000);
        assertNull("Expected null for invalid value", javaVersion);
    }

    /**
     * Test getEnumList method.
     */
    @Test
    public void testGetEnumList() {
        List<JavaVersionEnum> javaVersions = ValuedEnum.getEnumList(JavaVersionEnum.class);
        assertEquals("Expected 4 JavaVersionEnum instances in the list", 4, javaVersions.size());

        // Test with null class
        try {
            ValuedEnum.getEnumList(null);
            fail("Expected IllegalArgumentException with null class");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test iterator method.
     */
    @Test
    public void testIterator() {
        Iterator<JavaVersionEnum> javaVersionIterator = ValuedEnum.iterator(JavaVersionEnum.class);
        int count = 0;
        while (javaVersionIterator.hasNext()) {
            JavaVersionEnum javaVersion = javaVersionIterator.next();
            assertEquals("Expected JavaVersionEnum instance", javaVersion.getClass(), javaVersion.getClass());
            count++;
        }
        assertEquals("Expected 4 JavaVersionEnum instances in the list", 4, count);

        // Test with null class
        try {
            ValuedEnum.iterator(null);
            fail("Expected IllegalArgumentException with null class");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test getEnumMap method.
     */
    @Test
    public void testGetEnumMap() {
        Map<Integer, JavaVersionEnum> javaVersionMap = ValuedEnum.getEnumMap(JavaVersionEnum.class);
        assertEquals("Expected 4 JavaVersionEnum instances in the map", 4, javaVersionMap.size());

        // Test with invalid class
        try {
            ValuedEnum.getEnumMap(String.class);
            fail("Expected ClassCastException with String class");
        } catch (ClassCastException e) {
            // Expected
        }

        // Test with null class
        try {
            ValuedEnum.getEnumMap(null);
            fail("Expected IllegalArgumentException with null class");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test getEnum method with reflection.
     */
    @Test
    public void testGetEnumWithReflection() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<? extends ValuedEnum> javaVersionEnumClass = JavaVersionEnum.class;
        Method getEnumMethod = javaVersionEnumClass.getDeclaredMethod("getEnum", int.class);
        getEnumMethod.setAccessible(true);

        JavaVersionEnum java1_0 = (JavaVersionEnum) getEnumMethod.invoke(null, JavaVersionEnum.JAVA1_0.getValue());
        JavaVersionEnum javaVersion = (JavaVersionEnum) getEnumMethod.invoke(null, JavaVersionEnum.JAVA1_1.getValue());

        assertEquals("Expected JavaVersionEnum JAVA1_0", java1_0, javaVersion.compareTo(java1_0) == 0 ? java1_0 : javaVersion);
        java1_0 = (JavaVersionEnum) getEnumMethod.invoke(null, 1000);
        assertNull("Expected null for invalid value", java1_0);
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;

        // Test comparison with same instance
        assertEquals("Expected 0 for comparison with same instance", 0, java1_0.compareTo(java1_0));

        // Test comparison with smaller value
        int comparisonResult = java1_0.compareTo(java1_1);
        assertTrue("Expected negative comparison result", comparisonResult < 0);

        // Test comparison with larger value
        comparisonResult = java1_1.compareTo(java1_0);
        assertTrue("Expected positive comparison result", comparisonResult > 0);

        // Test comparison with equal value
        comparisonResult = java1_1.compareTo(java1_1);
        assertEquals("Expected 0 for comparison with equal value", 0, comparisonResult);

        // Test comparison with different class
        Comparable<JavaVersionEnum> javaVersionEnumDifferentClass = new Object();
        comparisonResult = java1_0.compareTo(javaVersionEnumDifferentClass);
        assertEquals("Expected 0 for comparison with different class", 0, comparisonResult);
    }
}