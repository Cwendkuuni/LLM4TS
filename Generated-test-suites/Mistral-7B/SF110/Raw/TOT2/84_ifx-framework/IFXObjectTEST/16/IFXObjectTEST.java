import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        // Test equal objects
        assertTrue(obj1.equals(obj1));

        // Test objects with same attributes are equal
        obj2.setSomeAttribute("attributeValue");
        assertTrue(obj1.equals(obj2));
        assertTrue(obj2.equals(obj1));

        // Test different objects are not equal
        IFXObject obj3 = new IFXObject();
        assertFalse(obj1.equals(obj3));
        assertFalse(obj3.equals(obj1));
    }
}