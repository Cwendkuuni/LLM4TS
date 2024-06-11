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

        assertTrue(obj1.equals(obj1));
        assertTrue(obj1.equals(obj2)); // assuming obj1 and obj2 are equal

        assertFalse(obj1.equals(obj3));
        assertFalse(obj1.equals(null));
        assertFalse(obj1.equals(new Object()));

        IFXObject obj4 = new IFXObject();
        IFXObject obj5 = new IFXObject();
        obj4.getClass().setName("com.example.IFXObject");
        assertFalse(obj1.equals(obj4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToString_nullObject() {
        IFXObject.stringify(null, 0);
    }

    @Test
    public void testToString_simpleObject() {
        IFXObject obj = new IFXObject();
        String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {}";
        String actual = obj.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_arrayObject() {
        IFXObject[] objArray = new IFXObject[5];
        for (int i = 0; i < objArray.length; i++) {
            objArray[i] = new IFXObject();
        }
        String expected = "class [Lorg.sourceforge.ifx.basetypes.IFXObject; = [";
        for (int i = 0; i < objArray.length; i++) {
            if (i > 0) {
                expected += "\n, ";
            }
            expected += "\n" + this.tab(1) + "[" + i + "]:";
            expected += "\n" + this.tab(2) + "class org.sourceforge.ifx.basetypes.IFXObject = {}";
        }
        expected += "\n" + this.tab(1) + "]";

        String actual = IFXObject.stringify(objArray, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_nestedObject() {
        IFXObject outerObj = new IFXObject();
        IFXObject innerObj = new IFXObject();
        Object[] innerArray = new Object[3];
        for (int i = 0; i < innerArray.length; i++) {
            innerArray[i] = innerObj;
        }

        Method getInnerArrayMethod = ReflectionUtils.findMethod(outerObj.getClass(), "getInnerArray");
        Assert.assertNotNull(getInnerArrayMethod);

        try {
            Object innerArrayObj = getInnerArrayMethod.invoke(outerObj, (Object[])null);
            String expected = "class org.sourceforge.ifx.basetypes.IFXObject = {getInnerArray = [";
            for (int i = 0; i < 3; i++) {
                if (i > 0) {
                    expected += "\n, ";
                }
                expected += "\n" + this.tab(1) + "[" + i + "]:";
                expected += "\n" + this.tab(2) + "class org.sourceforge.ifx.basetypes.IFXObject = {}";
            }
            expected += "\n]";

            String actual = IFXObject.stringify(innerArrayObj, 0);
            assertEquals(expected, actual);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = java.lang.reflect.InvocationTargetException.class)
    public void testToString_exception() {
        IFXObject obj = new IFXObject() {
            @Override
            public String toString() throws Exception {
                throw new RuntimeException();
            }
        };
        IFXObject.stringify(obj, 0);
    }

    private static int tab(int level) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < level; ++i) {
            buf.append("  ");
        }
        return buf.toString().length();
    }
}