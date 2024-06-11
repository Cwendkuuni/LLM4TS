
package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {
    // Test cases for equals method
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        // Test self-equality
        assertTrue(obj1.equals(obj1));

        // Test null object comparison
        assertFalse(obj1.equals(null));

        // Test equals with same object
        assertTrue(obj1.equals(obj2));

        // Test equals with different objects of the same class
        obj3.someMethod(); // Assume some method sets a unique value for obj3
        assertFalse(obj1.equals(obj3));

        // Test equals with different classes
        Object anotherObj = new Object();
        assertFalse(obj1.equals(anotherObj));

        // Test equals with deeply nested objects
        IFXObject complexObj1 = new IFXObject();
        IFXObject complexObj2 = new IFXObject();
        complexObj1.someDeeplyNestedMethod().someDeeplyNestedMethod().someDeeplyNestedMethod().setSomeValue("TestValue1");
        complexObj2.someDeeplyNestedMethod().someDeeplyNestedMethod().someDeeplyNestedMethod().setSomeValue("TestValue2");
        assertFalse(complexObj1.equals(complexObj2));

        complexObj1.someDeeplyNestedMethod().someDeeplyNestedMethod().someDeeplyNestedMethod().setSomeValue("TestValue1");
        complexObj2.someDeeplyNestedMethod().someDeeplyNestedMethod().someDeeplyNestedMethod().setSomeValue("TestValue1");
        assertTrue(complexObj1.equals(complexObj2));
    }

    // Test case for toString method
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        Object[] array = new Object[3];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new IFXObject();
        }
        obj.someArrayField = array;

        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                "someArrayField = [" +
                "org.sourceforge.ifx.basetypes.IFXObject = {" +
                "someDeeplyNestedMethod.someDeeplyNestedMethod.someDeeplyNestedMethod.someValue = NULL" +
                "}" +
                "org.sourceforge.ifx.basetypes.IFXObject = {" +
                "someDeeplyNestedMethod.someDeeplyNestedMethod.someDeeplyNestedMethod.someValue = NULL" +
                "}" +
                "org.sourceforge.ifx.basetypes.IFXObject = {" +
                "someDeeplyNestedMethod.someDeeplyNestedMethod.someDeeplyNestedMethod.someValue = NULL" +
                "}" +
                "]" +
                "}";

        String actual = obj.toString();
        assertEquals(expected, actual);
    }
}
