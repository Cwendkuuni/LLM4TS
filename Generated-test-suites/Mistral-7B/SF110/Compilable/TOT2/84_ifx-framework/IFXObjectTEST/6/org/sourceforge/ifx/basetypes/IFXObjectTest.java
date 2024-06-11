package org.sourceforge.ifx.basetypes;
import org.junit.Test;
import org.sourceforge.ifx.basetypes.IFXObject;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();
        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj2)); // Two equal objects should be considered equal
        assertFalse(obj1.equals(obj3)); // Two different objects should not be equal
        assertFalse(obj1.equals(new Object())); // Objects of different classes should not be equal
    }
}