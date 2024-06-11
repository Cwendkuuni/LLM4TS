package org.apache.commons.lang.enums;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum(ValuedEnum.class, "JavaVersionEnum", 100);
        assertNotNull(javaVersionEnum);
        assertEquals("Java 1.0", javaVersionEnum.getName());
        assertEquals(100, javaVersionEnum.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumNullClass() {
        ValuedEnum.getEnum(null, "JavaVersionEnum", 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumInvalidEnumClass() {
        ValuedEnum.getEnum(String.class, "JavaVersionEnum", 100);
    }

    @Test
    public void testGetEnumInvalidValue() {
        assertNull(ValuedEnum.getEnum(ValuedEnum.class, "JavaVersionEnum", 999));
    }

    @Test
    public void testGetEnumList() {
        List<ValuedEnum> javaVersionEnumList = ValuedEnum.getEnumList(ValuedEnum.class);
        assertFalse(javaVersionEnumList.isEmpty());
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(ValuedEnum.class, "JavaVersionEnum", 100);
        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum(ValuedEnum.class, "JavaVersionEnum", 110);
        assertTrue(javaVersionEnumList.contains(javaVersion1_0));
        assertTrue(javaVersionEnumList.contains(javaVersion1_1));
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumListNullClass() {
        ValuedEnum.getEnumList(null);
    }

    @Test
    public void testGetEnumMap() {
        Map<String, ValuedEnum> javaVersionEnumMap = ValuedEnum.getEnumMap(ValuedEnum.class);
        assertFalse(javaVersionEnumMap.isEmpty());
        assertEquals(javaVersion1_0, javaVersionEnumMap.get("Java 1.0"));
        assertEquals(javaVersion1_1, javaVersionEnumMap.get("Java 1.1"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumMapNullClass() {
        ValuedEnum.getEnumMap(null);
    }

    @Test
    public void testCompareTo() {
        ValuedEnum javaVersion1_0 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum javaVersion1_1 = new JavaVersionEnum("Java 1.1", 110);
        ValuedEnum javaVersion1_2 = new JavaVersionEnum("Java 1.2", 120);

        assertEquals(0, javaVersion1_0.compareTo(javaVersion1_0));
        assertEquals(-1, javaVersion1_0.compareTo(javaVersion1_1));
        assertEquals(1, javaVersion1_1.compareTo(javaVersion1_0));
        assertEquals(1, javaVersion1_0.compareTo(javaVersion1_2));
    }

    @Test
    public void testToString() {
        ValuedEnum javaVersion1_0 = new JavaVersionEnum("Java 1.0", 100);
        assertEquals("JavaVersion[Java 1.0=100]", javaVersion1_0.toString());
    }
}

class JavaVersionEnum extends ValuedEnum {
    private static final long serialVersionUID = 1L;

    public JavaVersionEnum(String name, int value) {
        super(name, value);
    }
}