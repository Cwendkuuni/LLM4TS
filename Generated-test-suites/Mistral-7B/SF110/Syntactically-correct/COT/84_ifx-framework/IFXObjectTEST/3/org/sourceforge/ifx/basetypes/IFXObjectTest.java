package org.sourceforge.ifx.basetypes.test;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sourceforge.ifx.basetypes.IFXObject;
import org.sourceforge.ifx.basetypes.IBaseType;
import org.sourceforge.ifx.basetypes.IFXObjectTestHelper;

import java.lang.reflect.Array;
import java.util.Arrays;

@RunWith(org.junit.runners.JUnit4.class)
public class IFXObjectTest {

    @Test
    public void testEqualsNormalCase() {
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        IFXObject obj2 = new IFXObjectTestHelper().createIFXObject();
        Assert.assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testEqualsNull() {
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        Assert.assertFalse(obj1.equals(null));
    }

    @Test
    public void testEqualsDifferentClasses() {
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        Object obj2 = new Object();
        Assert.assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testToStringNormalCase() {
        IFXObject obj = new IFXObjectTestHelper().createIFXObject();
        String expected = "IFXObject = {" +
                "/* some members omitted for brevity */" +
                "}";
        String actual = obj.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringArray() {
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        IFXObject obj2 = new IFXObjectTestHelper().createIFXObject();
        IFXObject[] objArray = {obj1, obj2};
        String expected = "IFXObject[] = {" +
                "IFXObject = {/* members for obj1 */}" +
                ", IFXObject = {/* members for obj2 */}" +
                "}";
        String actual = objArray.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = Exception.class)
    public void testToStringException() {
        IFXObject obj = new IFXObjectTestHelper().createIFXObject();
        try {
            obj.toString(); // this method is supposed to throw an Exception
        } catch (Exception e) {
            // expected exception, no need to assert anything
        }
    }

    @Test
    public void testDoCheckEqualsReflexive() {
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        Assert.assertTrue(obj1.doCheckEquals(obj1, obj1));
    }

    @Test
    public void testDoCheckEqualsSymmetric() {
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        IFXObject obj2 = new IFXObjectTestHelper().createIFXObject();
        Assert.assertTrue(obj1.doCheckEquals(obj2, obj1));
    }

    @Test
    public void testDoCheckEqualsTransitive() {
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        IFXObject obj2 = new IFXObjectTestHelper().createIFXObject();
        IFXObject obj3 = new IFXObjectTestHelper().createIFXObject();
        IFXObject[] objArray = {obj1, obj2, obj3};
        Assert.assertTrue(obj1.doCheckEquals(obj1, obj2));
        Assert.assertTrue(obj1.doCheckEquals(obj2, obj3));
        Assert.assertTrue(obj1.doCheckEquals(obj1, obj3));
    }

    @Test
    public void testDoCheckEqualsNullEqualNull() {
        IFXObject obj1 = null;
        IFXObject obj2 = null;
        Assert.assertTrue(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEqualsNullNotEqualNull() {
        IFXObject obj1 = null;
        IFXObject obj2 = new IFXObjectTestHelper().createIFXObject();
        Assert.assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEqualsDifferentClasses() {
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        Object obj2 = new Object();
        Assert.assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEqualsReturnTypeArray() {
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        IFXObject obj2 = new IFXObjectTestHelper().createIFXObject();
        IFXObject[] objArray1 = {obj1};
        IFXObject[] objArray2 = {obj2};
        Assert.assertFalse(obj1.doCheckEquals(objArray1, objArray2));
    }

    @Test
    public void testDoCheckEqualsIBaseType() {
        IBaseType baseType1 = new IFXObjectTestHelper().createBaseType();
        IBaseType baseType2 = new IFXObjectTestHelper().createBaseType();
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        IFXObject obj2 = new IFXObjectTestHelper().createIFXObject();
        obj1.setField(baseType1);
        obj2.setField(baseType2);
        Assert.assertTrue(obj1.doCheckEquals(obj1, obj2));
    }

    @Test
    public void testDoCheckEqualsIBaseTypeDifferentStrings() {
        IBaseType baseType1 = new IFXObjectTestHelper().createBaseType();
        IBaseType baseType2 = new IFXObjectTestHelper().createBaseType();
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        IFXObject obj2 = new IFXObjectTestHelper().createIFXObject();
        obj1.setField(baseType1);
        obj2.setField(new IFXObjectTestHelper().createBaseTypeWithDifferentString());
        Assert.assertFalse(obj1.doCheckEquals(obj1, obj2));
    }

    @Test(expected = Exception.class)
    public void testDoCheckEqualsException() {
        IFXObject obj1 = new IFXObjectTestHelper().createIFXObject();
        IFXObject obj2 = new IFXObjectTestHelper().createIFXObject();
        try {
            obj1.doCheckEquals(obj1, obj2);
            // this method is supposed to throw an Exception
        } catch (Exception e) {
            // expected exception, no need to assert anything
        }
    }

    @Test
    public void testTabNormalCase() {
        String expected = "  ";
        String actual = IFXObject.tab(1);
        Assert.assertEquals(expected, actual);
    }
}