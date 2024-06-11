package com.google.common.base.test;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // ... Expert 1's test case ...
    }
    
    @Test
    public void testHashCode() {
        // ... Expert 2's test case ...
    }
    
    @Test
    public void testToStringHelper() {
        // ... Expert 3's test case ...
    }
    
    @Test
    public void testToStringHelper_NullClassName() {
        try {
            Objects.ToStringHelper helper = new Objects.ToStringHelper(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    @Test
    public void testToStringHelper_EmptyStringClassName() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("");
        String toString = helper.toString();
        String expected = "";
        assertEquals(expected, toString);
    }
    
    @Test
    public void testToStringHelper_AddNullValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
                .add("field1", 1)
                .add("field2", null);
        String toString = helper.toString();
        String expected = "MyClass{field1=1, field2=null}";
        assertEquals(expected, toString);
        
        helper = helper.omitNullValues();
        toString = helper.toString();
        expected = "MyClass{field1=1}";
        assertEquals(expected, toString);
    }
    
    @Test
    public void testToStringHelper_AddNullClassName() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper(null)
                .add("field1", 1);
        String toString = helper.toString();
        String expected = "";
        assertEquals(expected, toString);
    }
}