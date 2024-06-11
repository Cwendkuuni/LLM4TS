import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {

    @Test
    public void testEqualsSameClassSameData() {
        IFXObject obj1 = new IFXObject();
        obj1.setData(1);
        IFXObject obj2 = new IFXObject();
        obj2.setData(1);
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testEqualsSameClassDifferentData() {
        IFXObject obj1 = new IFXObject();
        obj1.setData(1);
        IFXObject obj2 = new IFXObject();
        obj2.setData(2);
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEqualsNull() {
        IFXObject obj = new IFXObject();
        assertFalse(obj.equals(null));
    }

    @Test
    public void testEqualsDifferentClasses() {
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEqualsDifferentInstancesSameData() {
        IFXObject obj1 = new IFXObject();
        obj1.setData(1);
        IFXObject obj2 = new IFXObject();
        obj2.setData(1);
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testEqualsDifferentInstancesSameDataButDifferentClass() {
        IFXObject obj1 = new IFXObject();
        obj1.setData(new Object());
        IFXObject obj2 = new IFXObject();
        obj2.setData(new Object());
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEqualsDifferentInstancesDifferentData() {
        IFXObject obj1 = new IFXObject();
        obj1.setData(1);
        IFXObject obj2 = new IFXObject();
        obj2.setData(2);
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEqualsDifferentInstancesSameClassButDifferentData() {
        IFXObject obj1 = new IFXObject();
        obj1.setData(1);
        IFXObject obj2 = new IFXObject();
        obj2.setData(2);
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEqualsReferentialEquality() {
        IFXObject obj1 = new IFXObject();
        obj1.setData(new Object());
        IFXObject obj2 = obj1; // They refer to the same object
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testToStringEmptyObject() {
        IFXObject obj = new IFXObject();
        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {\n}";
        String actual = obj.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringArray() {
        int[] intArray = { 1, 2, 3 };
        IFXObject obj = new IFXObject() {
            Object data = intArray;
        };
        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {\ngetData = [3]\n}";

        String actual = obj.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringNullData() {
        IFXObject obj = new IFXObject() {
            Object data = null;
        };
        obj.toString();
    }

    @Test
    public void testToStringComplexObject() {
        IFXObject obj = new IFXObject() {
            Object data = new IFXObject() {
                Object innerData = new Object();
            };
        };
        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {\ngetData = class org.sourceforge.ifx.basetypes.IFXObject = {\ngetData = class java.lang.Object = {\n}\n}";

        String actual = obj.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToStringIllegalArgument() {
        IFXObject obj = new IFXObject() {
            Object data = new Object() {
                public String getString() {
                    throw new IllegalArgumentException();
                }
            };
        };
        obj.toString();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testToStringArrayIndexOutOfBounds() {
        int[] intArray = { 1, 2, 3 };
        IFXObject obj = new IFXObject() {
            Object data = intArray;
        };
        obj.toString(); // Runs the first time
        obj.toString(); // Throws the exception
    }

}