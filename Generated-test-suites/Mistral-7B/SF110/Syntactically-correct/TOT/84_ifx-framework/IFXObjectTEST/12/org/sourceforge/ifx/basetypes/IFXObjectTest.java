import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject(); // default instance
        IFXObject obj2 = new IFXObject(); // default instance

        // Test when both objects are equal
        assertTrue(obj1.equals(obj2));

        // Test when obj1 is equal to itself
        assertTrue(obj1.equals(obj1));

        // Test when obj1 is null
        assertFalse(obj1.equals(null));

        // Test when obj1 is not an instance of IFXObject
        assertFalse(obj1.equals(new Object()));

        // Test when obj1 and obj2 have different classes
        IFXObject obj3 = new AnotherClass();
        assertFalse(obj1.equals(obj3));

        // Test when obj1 and obj2 have the same class but different values
        IFXObject obj4 = new IFXObject();
        obj4.setSomeField("different value");
        assertFalse(obj1.equals(obj4));
    }
}