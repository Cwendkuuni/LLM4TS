package com.example.junit.tests.google.common.base;

import com.google.common.base.Objects;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class ObjectsTest {

    @Test
    public void testEqual() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Object obj3 = new Object();

        // Test when both objects are null
        assertTrue(Objects.equal(null, null));

        // Test when both objects are the same instance
        assertTrue(Objects.equal(obj1, obj2));

        // Test when both objects are of the same class and have the same value
        Object obj4 = new Object();
        assertTrue(Objects.equal(obj1, obj4));

        // Test when one object is null and the other is not
        assertFalse(Objects.equal(null, obj1));

        // Test when both objects are of different classes
        Object obj5 = new String("test");
        assertFalse(Objects.equal(obj1, obj5));

        // Test when one object is null and the other has a value
        assertFalse(Objects.equal(null, obj1));
    }

    @Test(expected = NullPointerException.class)
    public void testEqualNullFirstArgument() {
        Objects.equal(null, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testEqualNullSecondArgument() {
        Objects.equal(new Object(), null);
    }

    @Test
    public void testHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        List<Object> list = Arrays.asList(obj1, obj2, obj3);

        int result = Objects.hashCode(list.toArray());
        int expected = 511; // Assuming hashcodes for obj1, obj2 and obj3 give this value

        assertEquals(expected, result);
    }

    @Test
    public void testToStringHelper_omitNullValues_false() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", null)
                .add("name3", "value3");

        String result = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name2=null," +
                "name3=value3" +
                '}';

        assertEquals(expected, result);
    }

    @Test
    public void testToStringHelper_omitNullValues_true() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", null)
                .add("name3", "value3")
                .omitNullValues();

        String result = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name3=value3" +
                '}';

        assertEquals(expected, result);
    }
}