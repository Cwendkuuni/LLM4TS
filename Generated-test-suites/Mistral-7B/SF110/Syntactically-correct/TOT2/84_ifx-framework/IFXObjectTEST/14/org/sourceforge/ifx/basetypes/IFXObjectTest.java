import org.junit.Test;
import org.sourceforge.ifx.basetypes.IFXObject;
import static org.junit.Assert.assertTrue;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        assertTrue(obj1.equals(obj1)); // checks self-equality
        assertTrue(obj1.equals(obj2)); // checks equality of identical objects
        assertFalse(obj1.equals(obj3)); // checks inequality of different objects
        assertFalse(obj1.equals(new Object())); // checks inequality with object of different type
    }
}