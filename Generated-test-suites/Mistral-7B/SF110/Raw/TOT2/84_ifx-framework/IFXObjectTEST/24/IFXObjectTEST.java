package org.sourceforge.ifx.basetypes.test;

import org.junit.*;
import static org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {

    @Before
    public void setUp() {
        // Set up test data here
    }

    @After
    public void tearDown() {
        // Clean up test data here
    }

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        // Test when both objects are equal
        assertTrue(obj1.equals(obj1));

        // Test when first object is equal to second object
        obj1.setField1("Test1");
        obj2.setField1("Test1");
        assertTrue(obj1.equals(obj2));

        // Test when first object is not equal to null
        assertFalse(obj1.equals(null));

        // Test when first object is not equal to an instance of different class
        assertFalse(obj1.equals(new Object()));

        // Test when first object is not equal to an instance of same class with different fields
        obj3.setField1("Test3");
        assertFalse(obj1.equals(obj3));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();

        // Test when the object has no fields
        obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}";
        assertEquals(expected, obj.toString());

        // Test when the object has fields of different types
        obj.setField1("Test1");
        obj.setField2(123);
        String expectedWithFields = "org.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = Test1\ngetField2 = 123\n}";
        assertEquals(expectedWithFields, obj.toString());
    }

    private void setField1(IFXObject obj, String value) {
        try {
            Method method = IFXObject.class.getDeclaredMethod("setField1", String.class);
            method.setAccessible(true);
            method.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setField2(IFXObject obj, int value) {
        try {
            Method method = IFXObject.class.getDeclaredMethod("setField2", int.class);
            method.setAccessible(true);
            method.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}