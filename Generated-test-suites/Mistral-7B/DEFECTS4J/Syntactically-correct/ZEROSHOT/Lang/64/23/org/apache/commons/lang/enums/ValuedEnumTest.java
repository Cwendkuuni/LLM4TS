package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.EnumUtils;
import org.apache.commons.lang.enums.ValuedEnum;

import java.lang.reflect.Method;
import java.util.List;

public class ValuedEnumTest {

    private static class TestValuedEnum extends ValuedEnum {
        public TestValuedEnum(String name, int value) {
            super(name, value);
        }

        public static TestValuedEnum getEnum(String name) {
            return (TestValuedEnum) getEnum(TestValuedEnum.class, name);
        }

        public static TestValuedEnum getEnum(int value) {
            return (TestValuedEnum) getEnum(TestValuedEnum.class, value);
        }

        public static List<TestValuedEnum> getEnumList() {
            return getEnumList(TestValuedEnum.class);
        }

        public static Map<String, TestValuedEnum> getEnumMap() {
            return getEnumMap(TestValuedEnum.class);
        }

        public static Iterator<TestValuedEnum> iterator() {
            return iterator(TestValuedEnum.class);
        }
    }

    @org.junit.BeforeClass
    public static void setup() {
        TestValuedEnum.JAVA1 = new TestValuedEnum("Java 1.0", 1);
        TestValuedEnum.JAVA2 = new TestValuedEnum("Java 2.0", 2);
        TestValuedEnum.JAVA3 = new TestValuedEnum("Java 3.0", 3);
    }

    @org.junit.Test
    public void testGetEnumByName() {
        TestValuedEnum enumByValue = TestValuedEnum.getEnum("Java 1.0");
        assertEquals(TestValuedEnum.JAVA1, enumByValue);
    }

    @org.junit.Test
    public void testGetEnumByValue() {
        TestValuedEnum enumByName = TestValuedEnum.JAVA1;
        TestValuedEnum enumByValue = TestValuedEnum.getEnum(enumByName.getValue());
        assertEquals(enumByName, enumByValue);
    }

    @org.junit.Test
    public void testGetEnumMap() {
        Map<String, TestValuedEnum> map = TestValuedEnum.getEnumMap();
        assertEquals(TestValuedEnum.JAVA1, map.get("Java 1.0"));
    }

    @org.junit.Test
    public void testGetEnumList() {
        List<TestValuedEnum> list = TestValuedEnum.getEnumList();
        assertEquals(TestValuedEnum.JAVA1, list.get(0));
    }

    @org.junit.Test
    public void testIterator() {
        Iterator<TestValuedEnum> it = TestValuedEnum.iterator();
        assertEquals(TestValuedEnum.JAVA1, it.next());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullEnumClass() {
        getEnum(null, 0);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        getEnum(TestValuedEnum.class, null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNegativeValue() {
        getEnum(TestValuedEnum.class, -1);
    }

    @org.junit.Test
    public void testCompareTo() {
        TestValuedEnum java1 = TestValuedEnum.JAVA1;
        TestValuedEnum java2 = TestValuedEnum.JAVA2;
        TestValuedEnum java3 = TestValuedEnum.JAVA3;
        assertEquals(-1, java1.compareTo(java2));
        assertEquals(1, java2.compareTo(java1));
        assertEquals(0, java1.compareTo(java1));
        assertEquals(0, java2.compareTo(java2));
        assertEquals(-1, java1.compareTo(java3));
        assertEquals(1, java3.compareTo(java1));
        assertEquals(0, java3.compareTo(java3));
    }

    @org.junit.Test
    public void testToString() {
        TestValuedEnum java1 = TestValuedEnum.JAVA1;
        assertEquals("TestValuedEnum[Java 1.0=1]", java1.toString());
    }

    @org.junit.Test
    public void testGetValue() {
        TestValuedEnum java1 = TestValuedEnum.JAVA1;
        assertEquals(1, java1.getValue());
    }

    @org.junit.Test
    public void testGetEnumWithNullClass() {
        assertNull(getEnum(null, 0));
    }

    @org.junit.Test
    public void testGetEnumWithNullName() {
        assertNull(getEnum(TestValuedEnum.class, null));
    }

    @org.junit.Test
    public void testGetEnumWithValueFromDifferentClassLoader() throws Exception {
        Class<?> clazz = Class.forName("TestValuedEnumDifferentClassLoader");
        TestValuedEnumDifferentClassLoader enumDifferentClassLoader =
                (TestValuedEnumDifferentClassLoader) clazz.newInstance();
        TestValuedEnum java1 = TestValuedEnum.JAVA1;
        Object value = getMethodAndInvoke("getValue", enumDifferentClassLoader);
        int enumValue = (int) value;
        TestValuedEnum enumWithValue = getEnum(TestValuedEnum.class, enumValue);
        assertEquals(java1, enumWithValue);
    }

    private Object getMethodAndInvoke(String methodName, Object object) throws Exception {
        Class<?> clazz = object.getClass();
        Method method = clazz.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(object);
    }
}

class TestValuedEnumDifferentClassLoader extends TestValuedEnum {
    // empty
}