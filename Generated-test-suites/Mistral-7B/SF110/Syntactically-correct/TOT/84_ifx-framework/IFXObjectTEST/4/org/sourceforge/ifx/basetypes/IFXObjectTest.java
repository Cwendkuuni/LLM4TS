import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        // Test self-equality
        assertTrue(obj1.equals(obj1));

        // Test equality with null
        assertFalse(obj1.equals(null));

        // Test equality with different classes
        assertFalse(obj1.equals(new Object()));

        // Test equality with identical objects
        obj1.setField1("Test1");
        obj2.setField1("Test1");
        assertTrue(obj1.equals(obj2));

        // Test inequality with non-identical objects
        obj1.setField1("Test1");
        obj3.setField1("Test2");
        assertFalse(obj1.equals(obj3));

        // Test inequality with different classes
        assertFalse(obj1.equals(new String("Test")));

        // Test inequality with different fields
        obj1.setField1("Test1");
        obj2.setField1("Test2");
        obj2.setField2(123);
        assertFalse(obj1.equals(obj2));
    }

    // Assuming IFXObject has a setter method for 'field1'
}