import org.junit.Test;
import org.junit.Assert.*;
import org.sourceforge.ifx.basetypes.IFXObject;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj2));
        obj2.getClass().getMethods()[0].invoke(obj2, new Object[0]); // set a value for obj2
        assertFalse(obj1.equals(obj2));
        IFXObject obj3 = new IFXObject();
        assertFalse(obj1.equals(obj3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsWithNullObj() {
        IFXObject obj1 = new IFXObject();
        assertFalse(obj1.equals(null));
    }

    @Test
    public void testToString() {
        IFXObject obj1 = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}";
        String actual = obj1.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithArray() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        Object[] arr = {obj1, obj2};
        IFXObject obj3 = new IFXObject();
        obj1.getClass().getMethods()[0].invoke(obj1, new Object[0]); // set value for obj1
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {get = " + obj1.getClass().getName() + " = {...}; get = " + obj2.getClass().getName() + " = {}}";
        String actual = obj3.toString(new IFXObject[]{obj1, obj2});
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToStringWithNullObj() {
        IFXObject obj1 = new IFXObject();
        IFXObject[] arr = {obj1, null};
        throw new RuntimeException("Testing for IllegalArgumentException when passing null object to toString method.");
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullMethods() {
        IFXObject obj1 = new IFXObject();
        Class<?> clazz = Object.class;
        throw new RuntimeException("Testing for NullPointerException when passing null Method array to toString method.");
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();
        obj1.getClass().getMethods()[0].invoke(obj1, new Object[0]); // set value for obj1
        obj2.getClass().getMethods()[0].invoke(obj2, new Object[0]); // set value for obj2
        assertTrue(obj1.doCheckEquals(obj1, obj1));
        assertTrue(obj1.doCheckEquals(obj1, obj2));
        assertFalse(obj1.doCheckEquals(obj1, obj3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoCheckEqualsWithNullObj() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = null;
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoCheckEqualsWithDifferentClass() {
        IFXObject obj1 = new IFXObject();
        String obj2 = new String("Hello");
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }
}