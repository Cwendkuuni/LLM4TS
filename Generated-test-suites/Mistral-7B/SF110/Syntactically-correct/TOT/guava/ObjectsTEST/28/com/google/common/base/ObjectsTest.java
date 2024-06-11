
package com.google.common.base;

import com.google.common.annotations.*;
import javax.annotation.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;;

@GwtCompatible
public final class ObjectsTest {

    @Test
    public void testEqual() {
        // Test with null objects
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Test with same object
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Objects.equal(obj1, obj2));

        // Test with different but equal objects
        Object obj3 = new Object();
        Object obj4 = new Object();
        if (obj1.equals(obj4)) {
            assertTrue(Objects.equal(obj1, obj4));
        } else {
            assertFalse(Objects.equal(obj1, obj4));
        }

        // Test with different objects
        Object obj5 = new Object();
        Object obj6 = new Object();
        assertFalse(Objects.equal(obj5, obj6));
    }

    @Test
    public void testHashCode() {
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        Object d = new Object();

        int hashCodeA = Objects.hashCode(a);
        int hashCodeB = Objects.hashCode(b);
        int hashCodeC = Objects.hashCode(c, d);

        // Test hashCode with single object
        assertEquals(hashCodeA, Objects.hashCode(a));

        // Test hashCode with multiple objects
        assertNotEquals(hashCodeA, hashCodeB);
        assertNotEquals(hashCodeA, hashCodeC);
        assertEquals(hashCodeC, Objects.hashCode(c, d));
        assertEquals(hashCodeC, Objects.hashCode(d, c));

        // Test hashCode with empty array
        int hashCodeEmpty = Objects.hashCode();
        assertEquals(hashCodeEmpty, Objects.hashCode());
    }

    @Test
    public void testToStringHelper() {
        Object obj = new Object();

        // Test with empty ToStringHelper
        ToStringHelper emptyToStringHelper = Objects.toStringHelper(obj);
        assertEquals("Object{}", emptyToStringHelper.toString());

        // Test adding different types of values
        ToStringHelper stringToStringHelper = Objects.toStringHelper(obj).add("name", "John Doe");
        assertEquals("Object{name=John Doe}", stringToStringHelper.toString());

        ToStringHelper booleanToStringHelper = Objects.toStringHelper(obj).add("isActive", true);
        assertEquals("Object{isActive=true}", booleanToStringHelper.toString());

        ToStringHelper charToStringHelper = Objects.toStringHelper(obj).add("gender", 'M');
        assertEquals("Object{gender=M}", charToStringHelper.toString());

        ToStringHelper doubleToStringHelper = Objects.toStringHelper(obj).add("age", 30.5);
        assertEquals("Object{age=30.5}", doubleToStringHelper.toString());

        ToStringHelper floatToStringHelper = Objects.toStringHelper(obj).add("height", 1.75f);
        assertEquals("Object{height=1.75}", floatToStringHelper.toString());

        ToStringHelper intToStringHelper = Objects.toStringHelper(obj).add("weight", 65);
        assertEquals("Object{weight=65}", intToStringHelper.toString());

        ToStringHelper longToStringHelper = Objects.toStringHelper(obj).add("id", 123L);
        assertEquals("Object{id=123}", longToStringHelper.toString());

        // Test omitting null values
        ToStringHelper nullValueToStringHelper = Objects.toStringHelper(obj).omitNullValues().add("address", null);
        assertEquals("Object{}", nullValueToStringHelper.toString());
    }
}

final class Object {
    // Original Objects class code here
}
