package org.apache.commons.lang.enums;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class ValuedEnumTest {

    private Class<ValuedEnum> enumClass;
    private String enumName;
    private int enumValue;

    public ValuedEnumTest(Class<ValuedEnum> enumClass, String enumName, int enumValue) {
        this.enumClass = enumClass;
        this.enumName = enumName;
        this.enumValue = enumValue;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {JavaVersionEnum.class, "Java1_0", 100},
                {JavaVersionEnum.class, "Java1_1", 110},
                {JavaVersionEnum.class, "Java1_2", 120},
                {JavaVersionEnum.class, "Java1_3", 130}
        });
    }

    @Test
    public void testGetEnum() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = enumClass.getMethod("getEnum", String.class);
        ValuedEnum enumInstance = (ValuedEnum) method.invoke(null, enumName);
        assertEquals(enumValue, enumInstance.getValue());
    }

    @Test
    public void testGetEnumNull() {
        assertNull(ValuedEnum.getEnum(enumClass, null));
    }

    @Test
    public void testGetEnumInt() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = enumClass.getMethod("getEnum", int.class);
        ValuedEnum enumInstance = (ValuedEnum) method.invoke(null, enumValue);
        assertEquals(enumValue, enumInstance.getValue());
    }

    @Test
    public void testGetEnumIntNull() {
        assertNull(ValuedEnum.getEnum(enumClass, -1));
    }

    @Test
    public void testGetEnumMap() {
        Method method = ValuedEnum.class.getMethod("getEnumMap", Class.class);
        List<ValuedEnum> enumList = (List<ValuedEnum>) method.invoke(null, enumClass);
        ValuedEnum enumInstance = enumList.get(0);
        assertEquals(enumValue, enumInstance.getValue());
    }

    @Test
    public void testGetEnumList() {
        Method method = ValuedEnum.class.getMethod("getEnumList", Class.class);
        List<ValuedEnum> enumList = (List<ValuedEnum>) method.invoke(null, enumClass);
        ValuedEnum enumInstance = enumList.get(0);
        assertEquals(enumValue, enumInstance.getValue());
    }

    @Test
    public void testIterator() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ValuedEnum.class.getMethod("iterator", Class.class);
        Iterator<ValuedEnum> iterator = (Iterator<ValuedEnum>) method.invoke(null, enumClass);
        ValuedEnum enumInstance = iterator.next();
        assertEquals(enumValue, enumInstance.getValue());
    }
}