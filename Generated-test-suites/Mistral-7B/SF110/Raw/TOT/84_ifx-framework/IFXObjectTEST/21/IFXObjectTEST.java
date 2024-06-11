import org.junit.Test;
import static org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        // Test object equality
        assertTrue(obj1.equals(obj1));
        // Test null object equality
        assertFalse(obj1.equals(null));
        // Test equal objects of the same class
        assertTrue(obj1.equals(obj2));
        // Test unequal objects of the same class
        assertFalse(obj1.equals(obj3));
        // Test equal objects of different classes
        IFXObject obj4 = new Object();
        assertFalse(obj1.equals(obj4));
        // Test equals with null argument
        assertFalse(obj1.equals(null));
        // Test equals with objects of different classes
        IFXObject obj5 = new String("Test");
        assertFalse(obj1.equals(obj5));
    }
}