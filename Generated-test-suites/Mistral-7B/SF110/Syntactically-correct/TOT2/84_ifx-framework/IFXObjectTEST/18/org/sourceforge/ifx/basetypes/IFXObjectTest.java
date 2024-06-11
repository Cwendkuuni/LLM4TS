package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;
import org.sourceforge.ifx.basetypes.DifferentClassObject;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        // Test equal objects
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj2));

        // Test equal objects with same class
        IFXObject obj3 = new IFXObject();
        IFXObject obj4 = new IFXObject();
        assertTrue(obj3.equals(obj4));

        // Test objects with different classes
        assertFalse(obj1.equals(new Object()));

        // Test null object
        assertFalse(obj1.equals(null));

        // Test identical objects
        obj1.setField1("value1");
        obj2.setField1("value1");
        obj3.setField1("value2");
        assertTrue(obj1.equals(obj2));
        assertFalse(obj1.equals(obj3));

        // Test equals with null argument
        assertFalse(obj1.equals(null));

        // Test equals with different classes
        IFXObject obj5 = new DifferentClassObject();
        assertFalse(obj1.equals(obj5));
    }

    @Test
    public void testToString() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();
        IFXObject obj4 = new DifferentClassObject();
        obj1.setField1("value1");
        obj1.setField2(new IFXObject());
        obj1.setField2_field1("value5");
        obj2.setField1("value2");
        obj3.setField1("value1");
        obj3.setField2(new IFXObject());
        obj3.setField2_field1("value6");

        // Test toString with null object
        assertNotNull(obj1.toString());

        // Test toString with empty object
        assertEquals("IFXObject = {}\n", obj1.toString());

        // Test toString with simple object
        assertEquals("IFXObject = {field1 = value1}\n", obj1.toString());

        // Test toString with complex object
        assertEquals("IFXObject = {field1 = value1, field2 = IFXObject = {field1 = value5}}\n", obj1.toString());

        // Test toString with different classes
        assertEquals("IFXObject = {field1 = value1}\n", obj1.toString());
        assertEquals("DifferentClassObject = [Ljava.lang.Object;@15db9742\n", obj4.toString());
    }

    @Test
    public void testDoCheckEquals() {
        // Test doCheckEquals with null arguments
        assertFalse(obj1.doCheckEquals(null, obj2));
        assertFalse(obj1.doCheckEquals(obj1, null));

        // Test doCheckEquals with same objects
        assertTrue(obj1.doCheckEquals(obj1, obj1));

        // Test doCheckEquals with different classes
        assertFalse(obj1.doCheckEquals(obj1, obj4));

        // Test doCheckEquals with different fields
        assertFalse(obj1.doCheckEquals(obj1, obj3));

        // Test doCheckEquals with arrays
        IFXObject[] arr1 = {obj1, obj2};
        IFXObject[] arr2 = {obj1, obj3};
        assertFalse(obj1.doCheckEquals(obj1, arr1));
        assertFalse(obj1.doCheckEquals(arr1, obj1));
        assertFalse(obj1.doCheckEquals(arr1, arr2));
    }

    // ... similar test cases for other methods and edge cases
}