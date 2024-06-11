package com.example.junit.tests.google.common.base;

import static org.junit.Assert.*;

import com.google.common.base.Objects;

import java.util.Arrays;

import org.junit.Test;

public class ObjectsTest
{
    // equal method tests
    
    @Test
    public void testEqual_NullNull()
    {
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testEqual_NullObject()
    {
        assertFalse(Objects.equal(null, new Object()));
    }

    @Test
    public void testEqual_SameObjects()
    {
        String obj1 = new String("test");
        assertTrue(Objects.equal(obj1, obj1));
    }

    @Test
    public void testEqual_DifferentObjectsWithSameContent()
    {
        String obj1 = new String("test");
        String obj2 = new String("test");
        assertTrue(Objects.equal(obj1, obj2));
    }

    @Test
    public void testEqual_DifferentObjectsWithDifferentContent()
    {
        String obj1 = new String("test1");
        String obj2 = new String("test2");
        assertFalse(Objects.equal(obj1, obj2));
    }

    @Test(expected = NullPointerException.class)
    public void testEqual_NullArgument()
    {
        Objects.equal(null, new Object());
    }

    // hashCode method tests

    @Test
    public void testHashCode_EmptyArray()
    {
        int result = Objects.hashCode(new Object[0]);
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testHashCode_NullObject()
    {
        int result = Objects.hashCode(null);
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testHashCode_SingleObject()
    {
        String obj = new String("test");
        int result = Objects.hashCode(obj);
        int expected = obj.hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testHashCode_MultipleObjects()
    {
        String obj1 = new String("test1");
        String obj2 = new String("test2");
        int result = Objects.hashCode(obj1, obj2);
        int expected = Arrays.hashCode(new Object[]{obj1, obj2});
        assertEquals(expected, result);
    }

    // toStringHelper deprecated methods tests (for coverage)

    @Test
    public void testToStringHelper_ConstructorWithString()
    {
        Objects.ToStringHelper helper = Objects.toStringHelper("testClassName");
        assertNotNull(helper);
        assertEquals("testClassName{", helper.toString());
    }

    @Test
    public void testToStringHelper_ConstructorWithClass()
    {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        assertNotNull(helper);
        assertEquals("String{", helper.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelper_ConstructorWithNullString()
    {
        Objects.toStringHelper(null);
    }

    @Test
    public void testToStringHelper_OmitNullValues()
    {
        Objects.ToStringHelper helper = Objects.toStringHelper("testClassName").omitNullValues();
        String obj1 = new String("test1");
        String obj2 = null;
        helper.add("field1", obj1);
        helper.add("field2", obj2);
        String result = helper.toString();
        String expected = "testClassName{field1=test1}";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringHelper_AddBoolean()
    {
        Objects.ToStringHelper helper = Objects.toStringHelper("testClassName");
        boolean boolValue = true;
        helper.add("field1", boolValue);
        String result = helper.toString();
        String expected = "testClassName{field1=true}";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringHelper_AddString()
    {
        Objects.ToStringHelper helper = Objects.toStringHelper("testClassName");
        String stringValue = "testValue";
        helper.add("field1", stringValue);
        String result = helper.toString();
        String expected = "testClassName{field1=testValue}";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringHelper_AddLong()
    {
        Objects.ToStringHelper helper = Objects.toStringHelper("testClassName");
        long longValue = 12345L;
        helper.add("field1", longValue);
        String result = helper.toString();
        String expected = "testClassName{field1=12345}";
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelper_AddNullString()
    {
        Objects.ToStringHelper helper = Objects.toStringHelper("testClassName");
        helper.add("field1", null);
    }

    @Test
    public void testToStringHelper_AddNullValue()
    {
        Objects.ToStringHelper helper = Objects.toStringHelper("testClassName").omitNullValues();
        String obj1 = new String("test1");
        Object obj2 = null;
        helper.add("field1", obj1);
        helper.addValue(obj2);
        String result = helper.toString();
        String expected = "testClassName{field1=test1}";
        assertEquals(expected, result);
    }
}