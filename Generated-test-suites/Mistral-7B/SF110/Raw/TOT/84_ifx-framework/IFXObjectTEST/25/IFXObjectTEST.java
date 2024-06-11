import org.junit.Test;
import org.junit.Assert;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {
    // ... other imports and setup code

    @Test
    public void testEquals() throws Exception {
        IFXObject obj1 = new MyIFXObject();
        IFXObject obj2 = new MyIFXObject();
        IFXObject obj3 = new MyIFXObject();

        Assert.assertTrue(obj1.equals(obj1));
        Assert.assertTrue(obj1.equals(obj2));
        Assert.assertTrue(obj2.equals(obj1));

        obj2.setSomeField("different value");
        Assert.assertFalse(obj1.equals(obj2));

        IFXObject obj4 = null;
        Assert.assertFalse(obj1.equals(obj4));

        IFXObject obj5 = new Object() {}; // different class
        Assert.assertFalse(obj1.equals(obj5));
    }
}
// MyIFXObject is a subclass of IFXObject for testing purposes