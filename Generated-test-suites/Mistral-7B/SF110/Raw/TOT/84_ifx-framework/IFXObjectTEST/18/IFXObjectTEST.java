import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        // Test when both objects are equals
        assertTrue(obj1.equals(obj1));

        // Test when obj1 is equals to obj2
        obj1.setField("value1");
        obj2.setField("value1");
        assertTrue(obj1.equals(obj2));

        // Test when obj1 is not equals to obj3
        obj3.setField("value2");
        assertFalse(obj1.equals(obj3));

        // Test when obj1 is null
        assertFalse(obj1.equals(null));

        // Test when obj2 is null
        assertFalse(obj2.equals(null));

        // Test when obj1 and obj2 have different classes
        IFXObject obj4 = new AnotherClass();
        assertFalse(obj1.equals(obj4));
    }

    // Setter method for testing purposes
    private void setField(String value) {
        // Implementation of the setter method here
    }
}

class AnotherClass {}