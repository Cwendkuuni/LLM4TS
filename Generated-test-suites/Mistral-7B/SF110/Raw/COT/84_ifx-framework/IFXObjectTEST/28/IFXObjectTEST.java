import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class IFXObjectTest {

    @Test
    public void testEquals() throws Exception {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        assertTrue(obj1.equals(obj1));
        assertTrue(obj2.equals(obj2));
        assertFalse(obj1.equals(obj2));
        assertFalse(obj1.equals(obj3));

        IFXObject obj4 = new IFXObject();
        obj1 = obj4;
        assertTrue(obj1.equals(obj2 = obj4));

        IFXObject obj5 = new IFXObject();
        IFXObject obj6 = new IFXObject();
        obj5.getClass().setField("someField", new Object());
        obj6.getClass().setField("someField", new Object());
        assertTrue(obj5.equals(obj6));

        IFXObject obj7 = new IFXObject();
        obj7.getClass().setField("someField", null);
        assertFalse(obj5.equals(obj7));

        IFXObject obj8 = null;
        assertFalse(obj5.equals(obj8));
        assertNull(obj8);

        IFXObject obj9 = new String[] { null, new Integer(1), new Double("1.0") };
        IFXObject obj10 = new String[] { null, new Integer(1), new Double("1.0") };
        assertFalse(obj9.equals(obj10));

        obj9[0] = obj10;
        assertTrue(obj9.equals(obj10));

        IFXObject obj11 = new IFXObject() {
            @Override
            public String toString() {
                return "DifferentClass";
            }
        };
        assertFalse(obj5.equals(obj11));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsNullArgument() {
        new IFXObject().equals(null);
    }

    @Test
    public void testToString() throws Exception {
        IFXObject obj = new IFXObject() {
            Object someField = new Object();
            String getSomeFieldName() {
                return "someField";
            }

            @Override
            public String toString() {
                Method getSomeFieldMethod = getClass().getMethod("getSomeFieldName");
                return getClass().getName() + " = {" + getSomeFieldMethod.invoke(this) + "}";
            }
        };

        assertEquals(obj.getClass().getName() + " = {someField}", obj.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringNullObject() {
        new IFXObject().toString(null);
    }

    @Test
    public void testToStringArray() throws Exception {
        IFXObject[] objArray = new IFXObject[3];
        objArray[0] = new IFXObject();
        objArray[1] = new Object();
        objArray[2] = new IFXObject() {
            @Override
            public String toString() {
                return "DifferentClass";
            }
        };

        String expected = "IFXObject[] = {\n" +
                this.tab(1) + "IFXObject: IFXObject = {null}\n" +
                this.tab(1) + "Object: Object\n" +
                this.tab(1) + "DifferentClass: DifferentClass\n" +
                this.tab(0) + "}";

        assertEquals(expected, objArray.toString());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testToStringArrayNegativeIndex() {
        IFXObject[] objArray = new IFXObject[3];
        objArray[0] = new IFXObject();
        objArray[1] = new Object();

        objArray.toString(-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testToStringArrayIndexOutOfBounds() {
        IFXObject[] objArray = new IFXObject[3];
        objArray[0] = new IFXObject();
        objArray[1] = new Object();

        objArray.toString(2);
    }

    private String tab(final int level) {
        String tab = "";
        for (int i = 0; i < level; ++i) {
            tab += "  ";
        }
        return tab;
    }
}