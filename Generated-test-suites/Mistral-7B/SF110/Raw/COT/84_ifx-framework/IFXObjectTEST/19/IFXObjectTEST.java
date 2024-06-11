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
        IFXObject obj3 = new IFXObject();
        Object obj4 = new Object();

        assertTrue(obj1.equals(obj1));
        assertTrue(obj2.equals(obj2));
        assertFalse(obj1.equals(obj2));
        assertFalse(obj1.equals(obj4));

        IFXObject obj5 = new IFXObject();
        obj5.setX(1);
        IFXObject obj6 = new IFXObject();
        obj6.setX(1);
        assertTrue(obj5.equals(obj6));

        IFXObject obj7 = new IFXObject();
        obj7.setX(2);
        assertFalse(obj5.equals(obj7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsWithNull() {
        IFXObject obj1 = new IFXObject();
        assertFalse(obj1.equals(null));
    }

    @Test
    public void testToString() {
        IFXObject obj1 = new IFXObject();
        String expected = "IFXObject = {}";
        assertEquals(expected, obj1.toString());

        IFXObject obj2 = new IFXObject();
        obj2.setX(1);
        String expected2 = "IFXObject = {X = 1}";
        assertEquals(expected2, obj2.toString());

        IFXObject obj3 = new IFXObject();
        obj3.setX(1);
        obj3.setY(2);
        String expected3 = "IFXObject = {X = 1, Y = 2}";
        assertEquals(expected3, obj3.toString());

        IFXObject obj4 = new IFXObject();
        obj4.setX(new Integer(1));
        String expected4 = "IFXObject = {X = [I@7852e942}";
        assertEquals(expected4, obj4.toString());

        IFXObject obj5 = new IFXObject();
        obj5.setX(new String[]{"one", "two"});
        String expected5 = "IFXObject = {X = [Ljava.lang.String;@16a6d16d}";
        assertEquals(expected5, obj5.toString());
    }

    @Test(expected = ReflectiveOperationException.class)
    public void testToStringWithException() {
        IFXObject obj1 = new IFXObject();
        obj1.setX(new Exception("Test Exception"));
        String expected = "Exception! Test Exception";
        String actual = obj1.toString();
        fail("Expected exception: ReflectiveOperationException. But got: " + actual);
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();

        assertFalse(obj1.doCheckEquals(obj2, obj3));

        obj2.setX(1);
        obj3.setX(1);
        assertTrue(obj1.doCheckEquals(obj2, obj3));

        obj2.setY(2);
        assertFalse(obj1.doCheckEquals(obj2, obj3));

        obj3.setY(2);
        assertTrue(obj1.doCheckEquals(obj2, obj3));
    }

    @Test
    public void testDoCheckEqualsWithDifferentClasses() {
        IFXObject obj1 = new IFXObject();
        AnotherClass obj2 = new AnotherClass();
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testTab() {
        String expected = "  ";
        assertEquals(expected, new IFXObject().tab(1));
    }
}