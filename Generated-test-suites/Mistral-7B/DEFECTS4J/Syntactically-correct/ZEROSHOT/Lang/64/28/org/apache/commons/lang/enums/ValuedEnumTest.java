package org.apache.commons.lang.enums;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

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
                {ValuedEnum.class, "JavaVersionEnum$JAVA1_3", 130},
        });
    }

    @Test
    public void testGetEnumByName() {
        ValuedEnum enumValue = ValuedEnum.getEnum(enumClass, name);
        assertEquals(name, enumValue.getName());
        assertEquals(value, enumValue.getValue());
    }

    @Test
    public void testNullName() {
        assertNull(ValuedEnum.getEnum(enumClass, null));
    }

    @Test
    public void testGetEnumByValue() {
        ValuedEnum enumValue = ValuedEnum.getEnum(enumClass, value);
        assertEquals(name, enumValue.getName());
        assertEquals(value, enumValue.getValue());
    }

    @Test
    public void testNullValue() {
        assertNull(ValuedEnum.getEnum(enumClass, 0));
    }

    @Test
    public void testGetEnumMap() {
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(enumClass);
        for (ValuedEnum enumValue : enumList) {
            assertEquals(enumValue, enumMap.get(enumValue.getName()));
        }
    }

    @Test
    public void testGetEnumList() {
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        assertEquals(4, enumList.size());
        for (ValuedEnum enumValue : enumList) {
            assertEquals(name, enumValue.getName());
            assertEquals(value, enumValue.getValue());
        }
    }

    @Test
    public void testCompareTo() {
        ValuedEnum first = ValuedEnum.getEnum(enumClass, "JavaVersionEnum$JAVA1_0");
        ValuedEnum second = ValuedEnum.getEnum(enumClass, "JavaVersionEnum$JAVA1_1");
        ValuedEnum third = ValuedEnum.getEnum(enumClass, "JavaVersionEnum$JAVA1_2");
        ValuedEnum fourth = ValuedEnum.getEnum(enumClass, "JavaVersionEnum$JAVA1_3");

        assertEquals(first.compareTo(first), 0);
        assertEquals(first.compareTo(second), -1);
        assertEquals(first.compareTo(third), -1);
        assertEquals(first.compareTo(fourth), -1);

        assertEquals(second.compareTo(first), 1);
        assertEquals(second.compareTo(second), 0);
        assertEquals(second.compareTo(third), 1);
        assertEquals(second.compareTo(fourth), 1);

        assertEquals(third.compareTo(first), 1);
        assertEquals(third.compareTo(second), 0);
        assertEquals(third.compareTo(third), 0);
        assertEquals(third.compareTo(fourth), -1);

        assertEquals(fourth.compareTo(first), 1);
        assertEquals(fourth.compareTo(second), 1);
        assertEquals(fourth.compareTo(third), 0);
        assertEquals(fourth.compareTo(fourth), 0);
    }

    @Test
    public void testToString() {
        ValuedEnum enumValue = ValuedEnum.getEnum(enumClass, name);
        String toString = enumValue.toString();
        String expected = String.format("%s[%s=%d]", ClassUtils.getShortClassName(enumClass.getEnclosingClass()), name, value);
        assertEquals(expected, toString);
    }
}