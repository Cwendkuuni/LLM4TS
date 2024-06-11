import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        // Test equals with same object
        assertTrue(obj1.equals(obj1));

        // Test equals with null object
        assertFalse(obj1.equals(null));

        // Test equals with object of different class
        assertFalse(obj1.equals(new Object()));

        // Test equals with different objects of the same class
        assertFalse(obj1.equals(obj2));

        // Test equals with identical objects
        obj2 = obj1;
        assertTrue(obj1.equals(obj2));

        // Test equals with objects of the same class having identical fields
        obj1 = new IFXObject();
        obj2 = new IFXObject();
        obj3 = new IFXObject();
        obj1.setField1("value1");
        obj2.setField1("value1");
        obj3.setField1("value2");
        assertTrue(obj1.equals(obj2));
        assertFalse(obj1.equals(obj3));

        // Test equals with objects of the same class having different fields
        obj1.setField2("value3");
        assertFalse(obj1.equals(obj2));

        // Test equals with objects of the same class having arrays as fields
        IFXObject obj4 = new IFXObject();
        IFXObject obj5 = new IFXObject();
        obj4.setField1(new String[]{"value1"});
        obj5.setField1(new String[]{"value1"});
        assertTrue(obj4.equals(obj5));

        obj4.setField1(new String[]{"value1", "value2"});
        assertFalse(obj4.equals(obj5));

        // Test equals with objects of the same class having nested objects as fields
        IFXObject obj6 = new IFXObject();
        IFXObject obj7 = new IFXObject();
        obj6.setField1(new IFXObject());
        obj7.setField1(new IFXObject());
        obj6.getField1().setField1("value1");
        obj7.getField1().setField1("value2");
        assertFalse(obj6.equals(obj7));

        obj6.getField1().setField1("value1");
        assertTrue(obj6.equals(obj7));
    }
}