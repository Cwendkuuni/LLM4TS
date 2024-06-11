package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ObjectsTest {
    @Test
    public void testEqual() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Object obj3 = new Object();

        // Test with null reference
        assertEquals(true, Objects.equal(obj1, obj2));
        assertEquals(false, Objects.equal(obj1, obj3));
        assertEquals(false, Objects.equal(null, obj1));

        // Test with non-null reference
        assertEquals(true, Objects.equal(obj1, obj1));
        assertEquals(false, Objects.equal(obj1, new Object()));
    }
}