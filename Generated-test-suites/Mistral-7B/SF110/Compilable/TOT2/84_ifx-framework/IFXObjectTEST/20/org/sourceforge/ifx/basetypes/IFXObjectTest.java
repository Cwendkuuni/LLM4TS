package org.sourceforge.ifx.basetypes;
import org.junit.Test;
import org.sourceforge.ifx.basetypes.IFXObject;
import static org.junit.Assert.assertTrue;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));
    }
}