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
    private String name;
    private int value;

    public ValuedEnumTest(Class<ValuedEnum> enumClass, String name, int value) {
        this.enumClass = enumClass;
        this.name = name;
        this.value = value;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ValuedEnum.class, "JavaVersionEnum$JAVA1_0", 100},
                {ValuedEnum.class, "JavaVersionEnum$JAVA1_1", 110},
                {ValuedEnum.class, "JavaVersionEnum$JAVA1_2", 120},
                {ValuedEnum.class, "JavaVersionEnum$JAVA1_3", 130}
        });
    }

    @Test
    public void testGetEnumByName() {
        ValuedEnum enumByName = ValuedEnum.getEnum(enumClass, name);
        assertEquals(enumByName.getName(), name);
        assertEquals(enumByName.getValue(), value);
    }

    @Test
    public void testGetEnumByValue() {
        ValuedEnum enumByValue = ValuedEnum.getEnum(enumClass, value);
        assertEquals(enumByValue.getName(), getEnumNameByValue(value));
        assertEquals(enumByValue.getValue(), value);
    }

    @Test
    public void testGetEnumByNameNull() {
        assertNull(ValuedEnum.getEnum(enumClass, null));
    }

    @Test
    public void testGetEnumByValueNull() {
        assertNull(ValuedEnum.getEnum(enumClass, -1));
    }

    private String getEnumNameByValue(int value) {
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        for (ValuedEnum enumInstance : enumList) {
            if (enumInstance.getValue() == value) {
                return enumInstance.getName();
            }
        }
        return null;
    }
}