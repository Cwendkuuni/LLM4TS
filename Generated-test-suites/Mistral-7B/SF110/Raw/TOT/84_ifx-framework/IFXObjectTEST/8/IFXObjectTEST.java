import org.junit.Test;
import static org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {
    // Test for equals method with same instance
    @Test
    public void testEqualsSameInstance() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = obj1;
        assertTrue(obj1.equals(obj2));
    }

    // Test for equals method with null object
    @Test
    public void testEqualsNullObject() {
        IFXObject obj1 = new IFXObject();
        assertFalse(obj1.equals(null));
    }

    // Test for equals method with different classes
    @Test
    public void testEqualsDifferentClasses() {
        IFXObject obj1 = new IFXObject();
        String obj2 = new String("Test");
        assertFalse(obj1.equals(obj2));
    }

    // Test for equals method with different instances of the same class
    @Test
    public void testEqualsDifferentInstances() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertFalse(obj1.equals(obj2));

        // Test for equals method with same class and same values
        IFXObject obj3 = new IFXObject();
        obj3.setField1("Test1");
        obj3.setField2(1);
        obj1.setField1("Test1");
        obj1.setField2(1);
        assertTrue(obj1.equals(obj3));
    }

    // Test for toString method with empty object
    @Test
    public void testToStringEmptyObject() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}";
        assertEquals(expected, obj.toString());
    }

    // Test for toString method with simple object
    @Test
    public void testToStringSimpleObject() {
        IFXObject obj = new IFXObject();
        obj.setField1("Test1");
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                "field1 = Test1" +
                "}";
        assertEquals(expected, obj.toString());
    }

    // Test for toString method with nested objects
    @Test
    public void testToStringNestedObjects() {
        IFXObject obj = new IFXObject();
        obj.setField1("Test1");
        IFXObject nestedObj = new IFXObject();
        nestedObj.setField1("NestedTest1");
        obj.setField2(nestedObj);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                "field1 = Test1" +
                "field2 = org.sourceforge.ifx.basetypes.IFXObject = {" +
                "field1 = NestedTest1" +
                "}" +
                "}";
        assertEquals(expected, obj.toString());
    }
}