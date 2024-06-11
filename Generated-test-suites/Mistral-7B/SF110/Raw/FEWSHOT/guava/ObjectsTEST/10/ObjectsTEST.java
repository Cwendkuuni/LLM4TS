package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertEquals(true, Objects.equal(null, null));
        assertEquals(true, Objects.equal(new Object(), new Object()));
        assertEquals(true, Objects.equal("foo", "foo"));
        assertEquals(false, Objects.equal("foo", "bar"));
        assertEquals(false, Objects.equal(null, "foo"));
        assertEquals(false, Objects.equal("foo", null));
    }

    @Test
    public void testHashCode() {
        Object[] array1 = new Object[0];
        Object[] array2 = new Object[0];
        Object[] array3 = {null, 1, "foo"};

        assertEquals(Arrays.hashCode(array1), Objects.hashCode(array1));
        assertEquals(Arrays.hashCode(array2), Objects.hashCode(array2));
        assertEquals(Arrays.hashCode(array3), Objects.hashCode(array3));
    }

    @Test
    public void testToStringHelper_omitNullValues() {
        String className = "MyClass";

        Objects.ToStringHelper helper = new Objects.ToStringHelper(className)
                .omitNullValues();

        helper.add("name1", "value1");
        helper.add("name2", null);
        helper.add("name3", "value3");

        assertEquals("MyClass{name1=value1, name3=value3}", helper.toString());
    }

    @Test
    public void testToStringHelper_addValues() {
        String className = "MyClass";

        Objects.ToStringHelper helper = new Objects.ToStringHelper(className);

        helper.add("name1", 1);
        helper.addValue(2);
        helper.add("name2", null);
        helper.add("name3", "value3");
        helper.addValue(true);

        assertEquals("MyClass{name1=1, name3=value3, name2=, value1=2, value2=true}", helper.toString());
    }

    @Test
    public void testToStringHelper_add_String() {
        String className = "MyClass";

        Objects.ToStringHelper helper = new Objects.ToStringHelper(className);

        helper.add("name", "value");

        assertEquals("MyClass{name=value}", helper.toString());
    }

    @Test
    public void testToStringHelper_add_Boolean() {
        String className = "MyClass";

        Objects.ToStringHelper helper = new Objects.ToStringHelper(className);

        helper.add("name", true);

        assertEquals("MyClass{name=true}", helper.toString());
    }

    @Test
    public void testToStringHelper_add_Char() {
        String className = "MyClass";

        Objects.ToStringHelper helper = new Objects.ToStringHelper(className);

        helper.add("name", 'c');

        assertEquals("MyClass{name=c}", helper.toString());
    }

    @Test
    public void testToStringHelper_add_Double() {
        String className = "MyClass";

        Objects.ToStringHelper helper = new Objects.ToStringHelper(className);

        helper.add("name", 3.14);

        assertEquals("MyClass{name=3.14}", helper.toString());
    }

    @Test
    public void testToStringHelper_add_Float() {
        String className = "MyClass";

        Objects.ToStringHelper helper = new Objects.ToStringHelper(className);

        helper.add("name", 3.14f);

        assertEquals("MyClass{name=3.14}", helper.toString());
    }

    @Test
    public void testToStringHelper_add_Int() {
        String className = "MyClass";

        Objects.ToStringHelper helper = new Objects.ToStringHelper(className);

        helper.add("name", 1);

        assertEquals("MyClass{name=1}", helper.toString());
    }

    @Test
    public void testToStringHelper_add_Long() {
        String className = "MyClass";

        Objects.ToStringHelper helper = new Objects.ToStringHelper(className);

        helper.add("name", 1L);

        assertEquals("MyClass{name=1}", helper.toString());
    }
}