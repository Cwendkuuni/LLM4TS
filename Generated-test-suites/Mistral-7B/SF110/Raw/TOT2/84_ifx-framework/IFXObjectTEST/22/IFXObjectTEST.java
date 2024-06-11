import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {
    // Test for equals() method with same instance
    @Test
    public void testEqualsSameInstance() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = obj1;
        assertTrue(obj1.equals(obj2));
    }

    // Test for equals() method with null argument
    @Test
    public void testEqualsNull() {
        IFXObject obj1 = new IFXObject();
        assertFalse(obj1.equals(null));
    }

    // Test for equals() method with different classes
    @Test
    public void testEqualsDifferentClasses() {
        IFXObject obj1 = new IFXObject();
        String obj2 = "Hello World";
        assertFalse(obj1.equals(obj2));
    }

    // Test for equals() method with different instances of same class
    @Test
    public void testEqualsDifferentInstances() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertFalse(obj1.equals(obj2));
    }

    // Test for equals() method with identical fields
    @Test
    public void testEqualsIdenticalFields() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        obj1.setField1("Test1");
        obj2.setField1("Test1");
        assertTrue(obj1.equals(obj2));
    }

    // Test for equals() method with different fields
    @Test
    public void testEqualsDifferentFields() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        obj1.setField1("Test1");
        obj2.setField1("Test2");
        assertFalse(obj1.equals(obj2));
    }

    // Test for equals() method with arrays
    @Test
    public void testEqualsArrays() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        obj1.setField1(array1);
        obj2.setField1(array2);
        assertTrue(obj1.equals(obj2));
    }

    // Test for equals() method with null arrays
    @Test
    public void testEqualsNullArrays() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        obj1.setField1(null);
        obj2.setField1(null);
        assertTrue(obj1.equals(obj2));
    }

    // Test for equals() method with different arrays
    @Test
    public void testEqualsDifferentArrays() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 4};
        obj1.setField1(array1);
        obj2.setField1(array2);
        assertFalse(obj1.equals(obj2));
    }
}