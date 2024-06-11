import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();
        IFXObject obj4 = null;

        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj2)); // two identical objects should be equal
        assertFalse(obj1.equals(obj3)); // two different objects should not be equal
        assertFalse(obj1.equals(obj4)); // null object is not equal to any object

        // Test equals with different classes
        Object otherObj = new Object();
        assertFalse(obj1.equals(otherObj));

        // Test equals with same class but different values
        IFXObject obj5 = new IFXObject();
        obj5.setSomeField("different value");
        assertFalse(obj1.equals(obj5));
    }
}