package org.mockito.internal.util.reflection.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.test.TestWithNestedGenerics;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testSimpleField() {
        Class clazz = TestWithNestedGenerics.class;
        Field field = null;
        try {
            field = clazz.getDeclaredField("field1");
        } catch (NoSuchFieldException e) {
            Assert.fail("Field not found");
        }

        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = Integer.TYPE;
        Class actualType = genericMaster.getGenericType(field);

        Assert.assertEquals(expectedType, actualType);
    }

    @Test
    public void testNullField() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = null;
        Class expectedType = Object.class;
        Class actualType = genericMaster.getGenericType(field);

        Assert.assertEquals(expectedType, actualType);
    }

    @Test
    public void testNonGenericField() {
        Class clazz = TestWithNestedGenerics.class;
        Field field = null;
        try {
            field = clazz.getDeclaredField("nonGenericField");
        } catch (NoSuchFieldException e) {
            Assert.fail("Field not found");
        }

        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = String.class;
        Class actualType = genericMaster.getGenericType(field);

        Assert.assertEquals(expectedType, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        Class clazz = TestWithNestedGenerics.class;
        Field field = null;
        try {
            field = clazz.getDeclaredField("fieldWithIllegalArgumentException");
        } catch (NoSuchFieldException e) {
            Assert.fail("Field not found");
        }

        GenericMaster genericMaster = new GenericMaster();
        genericMaster.getGenericType(field); // this line will throw an IllegalArgumentException
    }

    @Test
    public void testNestedGenerics() {
        Class clazz = TestWithNestedGenerics.class;
        Field field = null;
        try {
            field = clazz.getDeclaredField("fieldWithNestedGenerics");
        } catch (NoSuchFieldException e) {
            Assert.fail("Field not found");
        }

        GenericMaster genericMaster = new GenericMaster();
        Class<TestWithNestedGenerics> expectedType = clazz;
        Class actualType = genericMaster.getGenericType(field);

        Assert.assertEquals(expectedType, actualType);
    }
}

class TestWithNestedGenerics {
    private List<List<Integer>> list;
}