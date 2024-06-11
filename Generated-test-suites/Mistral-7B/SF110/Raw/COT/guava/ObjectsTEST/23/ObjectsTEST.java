package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest
{
    @Test
    public void testEqual()
    {
        // Basic test
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        assertTrue(Objects.equal(new Object(), new Object()));
        assertTrue(Objects.equal(new Object(), new Object()));

        // Null objects
        assertFalse(Objects.equal(null, null));

        // Objects of different classes
        assertFalse(Objects.equal(new Object(), new String()));

        // Objects with equal values
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));
    }

    @Test
    public void testHashCode()
    {
        // Basic test
        Object[] objects = new Object[5];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new Object();
        }
        int hashCode = Objects.hashCode(objects);
        assertTrue(hashCode > 0);

        // Empty array
        hashCode = Objects.hashCode(new Object[0]);
        assertTrue(hashCode == 0);

        // Null array
        hashCode = Objects.hashCode(null);
        assertEquals(-1, hashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToStringHelperNullSelf()
    {
        Objects.toStringHelper(null);
    }

    @Test
    public void testToStringHelperEmpty()
    {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("ClassName");
        assertEquals("ClassName{}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelperSimple()
    {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(new Object());
        assertEquals("Object{}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelperWithValues()
    {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .add("name", "value");
        assertEquals("ClassName{name=value}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelperWithBooleanValue()
    {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .add("field", true);
        assertEquals("ClassName{field=true}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelperWithNullValue()
    {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .add("field", null);
        Objects.ToStringHelper toStringHelper2 = toStringHelper.omitNullValues();
        assertEquals("ClassName{}", toStringHelper2.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelperNullValueAdd()
    {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName");
        toStringHelper.add("field", null);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelperNullNameAdd()
    {
        Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName");
        toStringHelper.add(null, "value");
    }
}