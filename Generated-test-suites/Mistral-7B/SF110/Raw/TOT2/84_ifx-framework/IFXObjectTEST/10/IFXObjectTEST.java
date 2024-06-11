import org.junit.Test;
import org.sourceforge.ifx.basetypes.IFXObject;
import static org.junit.Assert.assertEquals;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        assertEquals(obj1, obj1); // tests self-equality
        assertEquals(obj1, obj2); // tests equality with identical objects
        assertNotEquals(obj1, obj3); // tests inequality with different objects
    }
}