import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {
    @Test
    public void testEquals() throws Exception {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));
        obj1.someMethod(); // Add some state to obj1
        assertFalse(obj1.equals(obj2));
        obj2.someMethod(); // Add the same state to obj2
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            Object[] array = new Object[3];
            {
                array[0] = new Object();
                array[1] = new int[2];
                array[2] = new IFXObject();
            }
        };
        String expected = "IFXObject = {" +
                "class org.sourceforge.ifx.basetypes.IFXObject = {" +
                "getArray() = [Ljava.lang.Object;@" + Long.toHexString(System.identityHashCode(obj.array)) +
                "]," +
                "getArray()[0] = class java.lang.Object = NULL," +
                "getArray()[1] = class [I = {I={0, 0}, I={0, 0}}]," +
                "getArray()[1][0] = I=0," +
                "getArray()[1][1] = I=0," +
                "getArray()[2] = class org.sourceforge.ifx.basetypes.IFXObject = {" +
                "class org.sourceforge.ifx.basetypes.IFXObject = {" +
                "getArray() = null," +
                "getClass() = class org.sourceforge.ifx.basetypes.IFXObject," +
                "toString() = NULL" +
                "}," +
                "}," +
                "}";
        String result = obj.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();
        IFXObject obj4 = new IFXObject();
        IFXObject obj5 = new IFXObject();

        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj1));

        assertFalse(obj1.equals(obj2));
        assertFalse(obj2.equals(obj1));
        assertFalse(obj2.equals(obj2));

        obj1.someMethod(); // Add some state to obj1
        obj2.someMethod(); // Add some state to obj2
        assertTrue(obj1.equals(obj2));
        assertTrue(obj2.equals(obj1));

        obj1.someMethod(); // Add some additional state to obj1
        obj2.someMethod(); // Add the same state to obj2
        assertFalse(obj1.equals(obj2));

        obj1.someMethod(); // Remove some state from obj1
        obj2.someMethod(); // Keep the same state in obj2
        assertFalse(obj1.equals(obj2));

        obj1.someMethod(); // Remove all state from obj1
        obj2.someMethod(); // Keep the same state in obj2
        assertFalse(obj1.equals(obj2));

        obj1.someMethod(); // Add some state to obj1
        obj4.someMethod(); // Add some different state to obj4
        assertFalse(obj1.equals(obj4));
        assertFalse(obj4.equals(obj1));

        obj1.someMethod(); // Add some state to obj1
        obj5.someMethod(); // Add some null state to obj5
        assertFalse(obj1.equals(obj5));
        assertFalse(obj5.equals(obj1));
    }

    @Test
    public void testDoCheckEquals_Arrays() {
        IFXObject obj1 = new IFXObject() {
            Object array1 = new Object[2];
            Object array2 = new Object[2];
            {
                array1[0] = new int[]{1, 2};
                array1[1] = new int[]{3, 4};
                array2[0] = new int[]{1, 2};
                array2[1] = new int[]{5, 6};
            }
        };
        IFXObject obj2 = new IFXObject() {
            Object array1 = new Object[2];
            Object array2 = new Object[2];
            {
                array1[0] = new int[]{1, 2};
                array1[1] = new int[]{3, 4};
                array2[0] = new int[]{1, 2};
                array2[1] = new int[]{5, 6};
            }
        };
        IFXObject obj3 = new IFXObject() {
            Object array1 = new Object[2];
            Object array2 = new Object[2];
            {
                array1[0] = new int[]{1, 2};
                array1[1] = new int[]{3, 4};
                array2[0] = new int[]{1, 3};
                array2[1] = new int[]{5, 6};
            }
        };

        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj1));

        assertTrue(obj1.equals(obj2));
        assertTrue(obj2.equals(obj1));
        assertTrue(obj1.equals(obj2));

        assertFalse(obj1.equals(obj3));
        assertFalse(obj3.equals(obj1));
        assertFalse(obj3.equals(obj3));
    }

    @Test
    public void testDoCheckEquals_BaseTypes() {
        IFXObject obj1 = new IFXObject() {
            IBaseType iBt1 = new StringType("Test1");
            IBaseType iBt2 = new StringType("Test2");
        };
        IFXObject obj2 = new IFXObject() {
            IBaseType iBt1 = new StringType("Test1");
            IBaseType iBt2 = new StringType("Test2");
        };
        IFXObject obj3 = new IFXObject() {
            IBaseType iBt1 = new StringType("Test1");
            IBaseType iBt2 = new IntegerType(1);
        };
        IFXObject obj4 = new IFXObject() {
            IBaseType iBt1 = new StringType("Test1");
            IBaseType iBt2 = null;
        };

        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj1));

        assertTrue(obj1.equals(obj2));
        assertTrue(obj2.equals(obj1));
        assertTrue(obj1.equals(obj2));

        assertFalse(obj1.equals(obj3));
        assertFalse(obj3.equals(obj1));
        assertFalse(obj3.equals(obj3));

        assertFalse(obj1.equals(obj4));
        assertFalse(obj4.equals(obj1));
        assertFalse(obj4.equals(obj4));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject() {
            Object[] array = new Object[3];
            {
                array[0] = new Object();
                array[1] = new int[2];
                array[2] = new IFXObject();
            }
        };
        String expected = "IFXObject = {" +
                "class org.sourceforge.ifx.basetypes.IFXObject = {" +
                "getArray() = [Ljava.lang.Object;@" + Long.toHexString(System.identityHashCode(obj.array)) +
                "]," +
                "getArray()[0] = class java.lang.Object = NULL," +
                "getArray()[1] = class [I = {I={0, 0}, I={0, 0}}]," +
                "getArray()[1][0] = I=0," +
                "getArray()[1][1] = I=0," +
                "getArray()[2] = class org.sourceforge.ifx.basetypes.IFXObject = {" +
                "class org.sourceforge.ifx.basetypes.IFXObject = {" +
                "getArray() = null," +
                "getClass() = class org.sourceforge.ifx.basetypes.IFXObject," +
                "toString() = NULL" +
                "}," +
                "}," +
                "}";
        String result = obj.toString();
        assertEquals(expected, result);
    }
}