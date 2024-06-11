import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject(); // empty object
        IFXObject obj2 = new IFXObject(); // empty object
        IFXObject obj3 = new IFXObject(); // non-empty object

        // Test with null objects
        assertFalse(obj1.equals(null));
        assertFalse(null.equals(obj1));

        // Test with same object
        assertTrue(obj1.equals(obj1));
        assertTrue(obj1 == obj1);

        // Test with different empty objects
        assertFalse(obj1.equals(obj2));

        // Test with different non-empty objects
        obj2.setStringProperty("test"); // assume setStringProperty is a setter method in IFXObject
        obj3.setStringProperty("test2");
        assertFalse(obj1.equals(obj2));
        assertFalse(obj2.equals(obj3));

        // Test with same non-empty objects
        IFXObject obj4 = new IFXObject();
        obj4.setStringProperty("test");
        assertTrue(obj1.equals(obj4));
        assertTrue(obj4.equals(obj1));
    }
}