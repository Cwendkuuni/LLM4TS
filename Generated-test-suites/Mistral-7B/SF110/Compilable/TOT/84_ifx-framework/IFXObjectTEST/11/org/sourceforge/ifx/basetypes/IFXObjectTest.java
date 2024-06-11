package org.sourceforge.ifx.basetypes;
import org.junit.Test;
import org.sourceforge.ifx.basetypes.IFXObject;
import static org.junit.Assert.assertEquals;

public class IFXObjectTest {
  @Test
  public void testEquals() {
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    assertEquals(obj1, obj2); // should be true

    IFXObject obj3 = new IFXObject();
    IFXObject obj4 = new IFXObject();
    assertEquals(obj3, obj4); // should be false
  }
}