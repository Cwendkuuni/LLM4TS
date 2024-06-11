package org.apache.commons.lang.enums;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.EnumConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class ValuedEnumTest {

    @Test
    public void testGetEnum() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<ValuedEnum> clazz = ValuedEnum.class;
        Method getEnumMethod = clazz.getMethod("getEnum", String.class);

        ValuedEnum java1_0 = (ValuedEnum) getEnumMethod.invoke(null, "JAVA1_0");
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());

        ValuedEnum java1_1 = (ValuedEnum) getEnumMethod.invoke(null, "JAVA1_1");
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());

        ValuedEnum java1_2 = (ValuedEnum) getEnumMethod.invoke(null, "JAVA1_2");
        assertEquals("Java 1.2", java1_2.getName());
        assertEquals(120, java1_2.getValue());

        assertNull(getEnumMethod.invoke(null, "Unknown"));
    }

    @Test
    public void testGetEnumWithInt() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<ValuedEnum> clazz = ValuedEnum.class;
        Method getEnumMethod = clazz.getMethod("getEnum", int.class);

        ValuedEnum java1_0 = (ValuedEnum) getEnumMethod.invoke(null, 100);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());

        ValuedEnum java1_1 = (ValuedEnum) getEnumMethod.invoke(null, 110);
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());

        ValuedEnum java1_2 = (ValuedEnum) getEnumMethod.invoke(null, 120);
        assertEquals("Java 1.2", java1_2.getName());
        assertEquals(120, java1_2.getValue());

        assertNull(getEnumMethod.invoke(null, 130));
    }

    @Test
    public void testGetEnumMap() {
        Class<ValuedEnum> clazz = ValuedEnum.class;
        Method getEnumMapMethod = clazz.getDeclaredMethod("getEnumMap", (Class[]) null);
        getEnumMapMethod.setAccessible(true);

        Object enumMap = getEnumMapMethod.invoke(null);
        List enumList = (List) EnumConstants.asList(clazz.getEnumConstants());
        for (Object enumObj : enumList) {
            ValuedEnum enumItem = (ValuedEnum) enumObj;
            assertEquals(enumItem, getEnumMapMethod.get(enumMap).get(Integer.valueOf(enumItem.getValue())));
        }
    }

    @Test
    public void testGetEnumList() {
        Class<ValuedEnum> clazz = ValuedEnum.class;
        Method getEnumListMethod = clazz.getDeclaredMethod("getEnumList", (Class[]) null);
        getEnumListMethod.setAccessible(true);

        Object enumListObj = getEnumListMethod.invoke(null);
        List enumList = (List) enumListObj;
        for (Object enumObj : enumList) {
            ValuedEnum enumItem = (ValuedEnum) enumObj;
            assertEquals(enumItem, getEnumListMethod.get(enumListObj).get(Integer.valueOf(enumItem.getValue())));
        }
    }

    @Test
    public void testIterator() {
        Class<ValuedEnum> clazz = ValuedEnum.class;
        Method iteratorMethod = clazz.getDeclaredMethod("iterator", (Class[]) null);
        iteratorMethod.setAccessible(true);

        Iterator enumIterator = (Iterator) iteratorMethod.invoke(null);
        List enumList = (List) EnumConstants.asList(clazz.getEnumConstants());
        for (Object enumObj : enumList) {
            ValuedEnum enumItem = (ValuedEnum) enumObj;
            assertEquals(enumItem, iteratorMethod.get(enumIterator).next());
        }
    }
}