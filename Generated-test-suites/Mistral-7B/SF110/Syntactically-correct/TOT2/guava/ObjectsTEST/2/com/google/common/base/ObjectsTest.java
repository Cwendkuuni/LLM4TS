package com.google.common.base.test;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.common.base.Objects;

public class ObjectsTest {

    @Test
    public void testEqual() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Object obj3 = new Object();

        // Test with null objects
        assertEquals(true, Objects.equal(obj1, obj2));
        assertEquals(false, Objects.equal(obj1, obj3));
        assertEquals(false, Objects.equal(null, obj1));
        assertEquals(false, Objects.equal(null, null));

        // Test with non-null objects of the same class
        assertEquals(true, Objects.equal(obj1, obj2));

        // Test with non-null objects of different classes
        assertEquals(false, Objects.equal(obj1, new String("test")));

        // Test with null and non-null objects
        assertEquals(false, Objects.equal(null, obj1));
    }

    @Test
    public void testHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();

        // Test with empty array
        int hashCode = Objects.hashCode();
        assertEquals(hashCode, Objects.hashCode());

        // Test with single object
        int hashCode1 = Objects.hashCode(obj1);
        assertEquals(hashCode1, Objects.hashCode(obj1));

        // Test with multiple objects
        int hashCode2 = Objects.hashCode(obj1, obj2, obj3);
        int hashCode3 = Arrays.hashCode(new Object[]{obj1, obj2, obj3});
        assertEquals(hashCode2, hashCode3);
    }

    @Test
    public void testToStringHelper() {
        Object obj = new Object();
        String className = "Object";
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj);
        Objects.ToStringHelper toStringHelperClassName = Objects.toStringHelper(className);

        // Test omitNullValues()
        assertEquals("Object{}", toStringHelper.toString());
        toStringHelper.omitNullValues();
        // Add a null value
        toStringHelper.add("field1", null);
        assertEquals("Object{field1=}", toStringHelper.toString());

        // Test adding values
        toStringHelper.add("field1", "value1");
        toStringHelper.add("field2", 1);
        toStringHelper.add("field3", 3.14);
        toStringHelper.add("field4", true);
        toStringHelper.add("field5", 'A');
        toStringHelper.addValue(42);
        toStringHelper.addValue(42.0);
        toStringHelper.addValue(42L);
        assertEquals("Object{field1=\"value1\", field2=1, field3=3.14, field4=true, field5=A, field5=42, field5=42.0, field5=42}", toStringHelper.toString());

        // Test multiple ToStringHelper instances
        assertEquals("Object{field1=\"value1\"}", toStringHelperClassName.add("field1", "value1").toString());
        assertEquals("Object{field1=\"value1\", field2=1}", toStringHelperClassName.add("field2", 1).toString());
        assertEquals("Object{field1=\"value1\", field2=1, field3=3.14}", toStringHelperClassName.add("field3", 3.14).toString());
    }
}