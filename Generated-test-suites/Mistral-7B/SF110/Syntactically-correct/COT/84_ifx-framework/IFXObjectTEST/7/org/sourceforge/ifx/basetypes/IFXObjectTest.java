import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.ClassCastException;
import java.util.Arrays;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = obj1;
        IFXObject obj4 = new IFXObject();
        IFXObject obj5 = new IFXObject();
        IFXObject obj6 = new IFXObject();
        IFXObject obj7 = new IFXObject();

        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[3];
        arr1[0] = obj1;
        arr1[1] = obj2;
        arr1[2] = obj3;

        arr2[0] = obj4;
        arr2[1] = obj5;
        arr2[2] = obj6;

        IFXObject[] arrIFXObj1 = {obj1, obj2, obj3};
        IFXObject[] arrIFXObj2 = {obj4, obj5, obj6};

        assertEquals(obj1, obj1);
        assertEquals(obj1, obj3);
        assertNotEquals(obj1, obj2);
        assertNotEquals(obj1, obj4);
        assertNotEquals(obj1, null);

        // Test with arrays
        assertEquals(arr1, arr1);
        assertEquals(arr1, arrIFXObj1);
        assertNotEquals(arr1, arr2);
        assertNotEquals(arr1, null);

        // Test with different classes
        IFXObject obj8 = new Object();
        assertNotEquals(obj1, obj8);

        // Test null arguments
        assertFalse(obj1.equals(null));
        assertFalse(null.equals(obj1));

        // Test equals method in IFXObject class
        assertTrue(obj1.doCheckEquals(obj1, obj1));
        assertTrue(obj1.doCheckEquals(obj1, obj3));
        assertFalse(obj1.doCheckEquals(obj1, obj2));
        assertFalse(obj1.doCheckEquals(obj1, obj4));
        assertFalse(obj1.doCheckEquals(obj1, null));
        assertFalse(null.doCheckEquals(obj1, obj1));
        assertFalse(null.doCheckEquals(null, obj1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToString_invalidObject() {
        IFXObject obj = null;
        obj.toString();
    }

    @Test
    public void testToString() {
        IFXObject obj1 = new IFXObject();
        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {}";
        String actual = obj1.toString();
        assertEquals(expected, actual);

        IFXObject obj2 = new IFXObject();
        obj2.getClass().getMethods()[0].setAccessible(true);
        Object arrObj = obj2.getClass().getMethods()[0].invoke(obj2);
        Class arrClass = arrObj.getClass();
        String tab = IFXObject.class.newInstance().tab(1);

        assertEquals("class org.sourceforge.ifx.basetypes.IFXObject = {get = NULL, getClass = class org.sourceforge.ifx.basetypes.IFXObject}", obj2.toString());
        assertEquals("class org.sourceforge.ifx.basetypes.IFXObject = {get = [class org.sourceforge.ifx.basetypes.IFXObject, class org.sourceforge.ifx.basetypes.IFXObject, class org.sourceforge.ifx.basetypes.IFXObject], getClass = class org.sourceforge.ifx.basetypes.IFXObject}", obj2.toString());

        for (int i = 0; i < arrObj.length; ++i) {
            IFXObject arrIFXObj = (IFXObject)arrObj[i];
            assertEquals("class org.sourceforge.ifx.basetypes.IFXObject = {get = NULL, getClass = class org.sourceforge.ifx.basetypes.IFXObject}", arrIFXObj.toString());
        }
    }

    @Test
    public void testDoCheckEquals_sameObjects() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = obj1;
        assertTrue(IFXObject.class.newInstance().doCheckEquals(obj1, obj1));
        assertTrue(IFXObject.class.newInstance().doCheckEquals(obj1, obj3));
    }

    @Test
    public void testDoCheckEquals_differentObjects() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertFalse(IFXObject.class.newInstance().doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_classesDifferent() {
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        assertFalse(IFXObject.class.newInstance().doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEquals_arrays_sameLength() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();
        IFXObject obj4 = new IFXObject();
        IFXObject obj5 = new IFXObject();
        IFXObject obj6 = new IFXObject();
        IFXObject obj7 = new IFXObject();

        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[3];
        arr1[0] = obj1;
        arr1[1] = obj2;
        arr1[2] = obj3;

        arr2[0] = obj4;
        arr2[1] = obj5;
        arr2[2] = obj6;

        IFXObject[] arrIFXObj1 = {obj1, obj2, obj3};
        IFXObject[] arrIFXObj2 = {obj4, obj5, obj6};

        assertTrue(IFXObject.class.newInstance().doCheckEquals(arrIFXObj1, arrIFXObj1));
        assertTrue(IFXObject.class.newInstance().doCheckEquals(arrIFXObj1, arrIFXObj2));

        for (int i = 0; i < arrIFXObj1.length; ++i) {
            for (int j = 0; j < arrIFXObj2.length; ++j) {
                assertTrue(IFXObject.class.newInstance().doCheckEquals(arrIFXObj1[i], arrIFXObj2[j]));
            }
        }
    }

    @Test
    public void testDoCheckEquals_arrays_differentLengths() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();
        IFXObject obj4 = new IFXObject();
        IFXObject obj5 = new IFXObject();
        IFXObject obj6 = new IFXObject();
        IFXObject obj7 = new IFXObject();

        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[2];
        arr1[0] = obj1;
        arr1[1] = obj2;
        arr1[2] = obj3;

        arr2[0] = obj4;
        arr2[1] = obj5;

        IFXObject[] arrIFXObj1 = {obj1, obj2, obj3};
        IFXObject[] arrIFXObj2 = {obj4, obj5};

        assertFalse(IFXObject.class.newInstance().doCheckEquals(arrIFXObj1, arrIFXObj2));
    }

    @Test
    public void testDoCheckEquals_arrays_null() {
        IFXObject obj1 = new IFXObject();
        Object[] arr1 = new Object[3];
        arr1[0] = obj1;
        arr1[1] = null;
        arr1[2] = obj1;

        IFXObject[] arrIFXObj1 = {obj1, null, obj1};
        IFXObject[] arrIFXObj2 = {obj1, obj1, obj1};

        assertFalse(IFXObject.class.newInstance().doCheckEquals(arrIFXObj1, arrIFXObj2));
    }

    @Test(expected = NullPointerException.class)
    public void testDoCheckEquals_arrays_nullElements() {
        IFXObject obj1 = new IFXObject();
        Object[] arr1 = new Object[3];
        arr1[0] = obj1;
        arr1[1] = null;
        arr1[2] = obj1;

        IFXObject obj2 = null;
        IFXObject[] arrIFXObj2 = {obj1, obj2, obj1};

        IFXObject.class.newInstance().doCheckEquals(arr1, arrIFXObj2);
    }

    @Test
    public void testDoCheckEquals_nestedObjects_same() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        Method getGetMethod = obj1.getClass().getMethod("getGet");
        getGetMethod.setAccessible(true);
        Object nestedObj1 = getGetMethod.invoke(obj1);
        assertEquals(obj1, nestedObj1);

        Method setGetMethod = obj1.getClass().getMethod("setGet", Object.class);
        setGetMethod.setAccessible(true);
        setGetMethod.invoke(obj1, obj2);

        assertTrue(IFXObject.class.newInstance().doCheckEquals(obj1, obj1));
        assertTrue(IFXObject.class.newInstance().doCheckEquals(obj1, obj1));
    }

    @Test
    public void testDoCheckEquals_nestedObjects_different() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        Method getGetMethod = obj1.getClass().getMethod("getGet");
        getGetMethod.setAccessible(true);
        Object nestedObj1 = getGetMethod.invoke(obj1);
        Method setGetMethod = obj1.getClass().getMethod("setGet", Object.class);
        setGetMethod.setAccessible(true);
        setGetMethod.invoke(obj1, obj2);

        Method getGetMethodObj2 = obj2.getClass().getMethod("getGet");
        getGetMethodObj2.setAccessible(true);
        Object nestedObj2 = getGetMethodObj2.invoke(obj2);
        assertEquals(obj2, nestedObj2);

        Method setGetMethodObj2 = obj2.getClass().getMethod("setGet", Object.class);
        setGetMethodObj2.setAccessible(true);
        setGetMethodObj2.invoke(obj2, obj3);

        assertFalse(IFXObject.class.newInstance().doCheckEquals(obj1, obj2));
        assertFalse(IFXObject.class.newInstance().doCheckEquals(obj1, obj3));
        assertFalse(IFXObject.class.newInstance().doCheckEquals(obj2, obj3));
    }
}