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

        // Test null object
        assertFalse(obj1.equals(null));

        // Test non-IFXObject equality
        assertFalse(obj1.equals(new Object()));

        // Test equality with same instance
        obj2 = obj1;
        assertTrue(obj1.equals(obj2));

        // Test equality with different instances of same class
        obj3 = new IFXObject();
        assertFalse(obj1.equals(obj3));

        // Test equality with different classes
        Object otherObj = new Object();
        assertFalse(obj1.equals(otherObj));

        // Test deep equality with identical objects
        IFXObject identicalObj1 = new IFXObject();
        IFXObject identicalObj2 = new IFXObject();
        identicalObj1.setField1("field1Value");
        identicalObj2.setField1("field1Value");
        IFXObject nestedObj1 = new IFXObject();
        IFXObject nestedObj2 = new IFXObject();
        nestedObj1.setField1("nestedField1Value");
        nestedObj2.setField1("nestedField1Value");
        identicalObj1.setNestedObject(nestedObj1);
        identicalObj2.setNestedObject(nestedObj2);
        assertTrue(obj1.equals(obj2));

        // Test deep equality with different objects
        IFXObject differentObj1 = new IFXObject();
        IFXObject differentObj2 = new IFXObject();
        differentObj1.setField1("field1Value");
        differentObj2.setField1("field2Value");
        nestedObj1 = new IFXObject();
        nestedObj2 = new IFXObject();
        nestedObj1.setField1("nestedField1Value");
        nestedObj2.setField1("nestedField2Value");
        differentObj1.setNestedObject(nestedObj1);
        differentObj2.setNestedObject(nestedObj2);
        assertFalse(obj1.equals(obj2));
    }
}