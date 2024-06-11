package org.apache.commons.lang.enums;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersionEnum = ValuedEnum.class.getEnum(ValuedEnum.JAVA1_0_VALUE);
        Assert.assertNotNull(javaVersionEnum);
        Assert.assertEquals("Java 1.0", javaVersionEnum.getName());
        Assert.assertEquals(100, javaVersionEnum.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumNullEnumClass() {
        ValuedEnum.getEnum(null, 100);
    }

    @Test
    public void testGetEnumWrongClass() {
        Assert.assertNull(ValuedEnum.getEnum(String.class, 100));
    }

    @Test
    public void testGetEnumWrongValue() {
        Assert.assertNull(ValuedEnum.getEnum(ValuedEnum.class, 200));
    }

    @Test
    public void testGetEnumWithString() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum(ValuedEnum.class, "Java 1.0");
        Assert.assertNotNull(javaVersionEnum);
        Assert.assertEquals("Java 1.0", javaVersionEnum.getName());
        Assert.assertEquals(100, javaVersionEnum.getValue());
    }

    @Test
    public void testGetEnumMap() {
        List<ValuedEnum> javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);
        Map<Integer, ValuedEnum> map = ValuedEnum.getEnumMap(ValuedEnum.class);

        for (ValuedEnum javaVersion : javaVersions) {
            Assert.assertEquals(javaVersion, map.get(javaVersion.getValue()));
        }

        Assert.assertEquals(javaVersions.size(), map.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumMapNullEnumClass() {
        ValuedEnum.getEnumMap(null);
    }

    @Test
    public void testCompareTo() {
        ValuedEnum java1_0 = ValuedEnum.JAVA1_0;
        ValuedEnum java1_1 = ValuedEnum.JAVA1_1;

        int comparison = java1_0.compareTo(java1_1);
        Assert.assertTrue(comparation < 0);

        comparison = java1_1.compareTo(java1_0);
        Assert.assertTrue(comparation > 0);

        comparison = java1_0.compareTo(java1_0);
        Assert.assertEquals(0, comparison);
    }

    @Test
    public void testToString() {
        ValuedEnum javaVersionEnum = ValuedEnum.JAVA1_0;
        String toString = javaVersionEnum.toString();
        Assert.assertEquals("JavaVersion[Java 1.0=100]", toString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareToDifferentClass() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> enumClass = Class.forName("java.lang.String");
        ValuedEnum javaVersionEnum = (ValuedEnum) ValuedEnum.getEnum(ValuedEnum.class, 100);
        Method compareToMethod = ValuedEnum.class.getMethod("compareTo", Object.class);
        Object result = compareToMethod.invoke(javaVersionEnum, enumClass.newInstance());
        Assert.fail();
    }
}