
package com.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.common.base.Objects.*;

public class ObjectsTest {
    @Test
    public void testEqualNullNull() {
        assertTrue(equal(null, null));
    }

    @Test
    public void testEqualNullNonnull() {
        assertFalse(equal(null, new Object()));
    }

    @Test
    public void testEqualNonnullNull() {
        assertFalse(equal(new Object(), null));
    }

    @Test
    public void testEqualNonnullNonnull() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(equal(obj1, obj2));
    }

    @Test
    public void testHashCodeEqualArrays() {
        Object[] array1 = new Object[] {1, 2, 3};
        Object[] array2 = new Object[] {1, 2, 3};
        int hashCode1 = hashCode(array1);
        int hashCode2 = hashCode(array2);
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeDifferentArrays() {
        Object[] array1 = new Object[] {1, 2, 3};
        Object[] array2 = new Object[] {1, 3, 3};
        int hashCode1 = hashCode(array1);
        int hashCode2 = hashCode(array2);
        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testToStringHelperBasicUse() {
        ToStringHelper helper = toStringHelper(new Object())
                .add("field1", "value1");
        assertEquals("Object{field1=value1}", helper.toString());
    }

    @Test
    public void testToStringHelperNullValues() {
        ToStringHelper helper = toStringHelper(new Object())
                .add("field1", null)
                .add("field2", new Object());
        assertEquals("Object{field1=null, field2=Object}", helper.toString());
    }

    @Test
    public void testToStringHelperOmitNullValues() {
        ToStringHelper helper = toStringHelper(new Object())
                .add("field1", null)
                .omitNullValues()
                .add("field2", new Object());
        assertEquals("Object{field2=Object}", helper.toString());
    }
}