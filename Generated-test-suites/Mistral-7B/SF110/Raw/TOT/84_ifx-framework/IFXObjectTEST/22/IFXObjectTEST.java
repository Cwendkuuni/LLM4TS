import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        // Test identical objects
        assertTrue(obj1.equals(obj1));

        // Test objects with equal values
        IFXObject obj3 = new IFXObject();
        obj3.setSomeField("someValue");
        assertTrue(obj1.equals(obj3));
        assertTrue(obj3.equals(obj1));

        // Test null objects
        IFXObject obj4 = null;
        assertTrue(obj1.equals(obj1)); // reflexive property
        assertTrue(obj1.equals(obj4)); // null is equal to null

        // Test objects of different classes
        Object obj5 = new Object();
        assertFalse(obj1.equals(obj5));
    }
}