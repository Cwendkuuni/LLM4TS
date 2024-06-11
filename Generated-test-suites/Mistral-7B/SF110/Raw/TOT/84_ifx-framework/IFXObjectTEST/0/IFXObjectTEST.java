
import org.junit.Assert.*;
import org.junit.Test;
import org.sourceforge.ifx.basetypes.BaseType;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {

    @Test
    public void testEqualsSameInstance() {
        //Test Case 1: equals() - same instance
        IFXObject obj1 = new IFXObject();
        assertTrue(obj1.equals(obj1));
    }

    @Test
    public void testEqualsDifferentInstancesSameClass() {
        //Test Case 2: equals() - different instances, same class
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEqualsDifferentInstancesDifferentClass() {
        //Test Case 3: equals() - different instances, different classes
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testEqualsNull() {
        //Test Case 4: equals() - null
        IFXObject obj1 = new IFXObject();
        assertFalse(obj1.equals(null));
    }

    @Test
    public void testEqualsNullInstance() {
        //Test Case 5: equals() - null instance
        IFXObject obj1 = null;
        assertFalse(obj1.equals(new IFXObject()));
    }

    @Test
    public void testEqualsSameClassSameData() {
        //Test Case 6: equals() - same class, same data
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        IFXObject obj3 = new IFXObject();
        obj1.setField1(new Integer(1));
        obj2.setField1(new Integer(1));
        obj3.setField1(new Integer(2));
        obj1.setField2(new String("test"));
        obj2.setField2(new String("test"));
        assertTrue(obj1.equals(obj2));
        assertFalse(obj1.equals(obj3));
    }

    @Test
    public void testEqualsWithArrays() {
        //Test Case 7: equals() - arrays
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        Object[] array1 = new Object[] { 1, "test" };
        Object[] array2 = new Object[] { 1, "test" };
        Object[] array3 = new Object[] { 2, "test" };
        obj1.setField1(array1);
        obj2.setField1(array2);
        assertTrue(obj1.equals(obj2));
        assertFalse(obj1.equals(obj3));
    }

    @Test
    public void testEqualsWithIBaseType() {
        //Test Case 8: equals() - IBaseType
        IFXObject obj1 = new IFXObject();
        IBaseType baseType1 = new BaseType();
        IBaseType baseType2 = new BaseType();
        obj1.setField1(baseType1);
        obj1.setField2(baseType2);
        String expectedString1 = "test1";
        String expectedString2 = "test2";
        assertFalse(obj1.equals(null));
        assertTrue(obj1.equals(obj1));
        assertFalse(obj1.equals(new Object()));
        IFXObject obj2 = new IFXObject();
        obj2.setField1(baseType1);
        obj2.setField2(baseType2);
        obj1.setField1(baseType2);
        obj2.setField2(baseType1);
        assertFalse(obj1.equals(obj2));
        assertTrue(obj1.equals(obj1));
        assertFalse(obj1.equals(new Object()));
        IFXObject obj3 = new IFXObject();
        obj3.setField1(baseType1);
        obj3.setField2(new BaseType());
        assertFalse(obj1.equals(obj3));
        IFXObject obj4 = new IFXObject();
        obj4.setField1(new BaseType());
        obj4.setField2(baseType2);
        assertFalse(obj1.equals(obj4));
        IFXObject obj5 = new IFXObject();
        obj5.setField1(baseType2);
        obj5.setField2(baseType1);
        assertFalse(obj1.equals(obj5));
        assertFalse(obj1.equals(new Object()));
    }

    @Test
    public void testToString() {
        //Test Case 9: toString() - default values
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}\n";
        String result = obj.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithArrays() {
        //Test Case 10: toString() - arrays
        IFXObject obj = new IFXObject() {
            Object field1 = new Object[] { 1, "test" };
        };
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = [1, test]\n}";
        String result = obj.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithIBaseType() {
        //Test Case 11: toString() - IBaseType
        IFXObject obj = new IFXObject() {
            IBaseType field1 = new BaseType();
        };
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\ngetField1 = test1\n}";
        obj.setField1(new BaseType() {
            String string = "test1";
        });
        String result = obj.toString();
        assertEquals(expected, result);
    }
}