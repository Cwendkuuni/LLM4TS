package org.junit.Test;

import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GenericMasterTest {

    @SuppressWarnings("unchecked")
    private static final Class<?> FIELD_TYPE = Integer.TYPE;
    private static final String FIELD_NAME = "testField";

    @SuppressWarnings("rawtypes")
    private static final Class[] TYPE_ARGUMENTS = new Class[]{Integer.TYPE};
    private static final Class<?> LIST_TYPE = Arrays.asList(TYPE_ARGUMENTS).getClass();

    @SuppressWarnings("rawtypes")
    private static final Class<?> FIELD_TYPE_WITH_LIST = LIST_TYPE.getComponentType();

    @SuppressWarnings("rawtypes")
    private static final Class<?> FIELD_TYPE_WITH_NESTED_LIST = Arrays.asList(TYPE_ARGUMENTS, TYPE_ARGUMENTS).getClass();

    @Before
    public void setUp() throws Exception {
        FieldTestDouble testDouble = new FieldTestDouble();
        Whitebox.setInternalState(testDouble, "testField", new Object(){});
    }

    @Test
    public void testGetGenericType_PrimitiveField() {
        Field field = testDouble.getClass().getDeclaredField(FIELD_NAME);
        Class expected = FIELD_TYPE;
        Class actual = new GenericMaster().getGenericType(field);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericType_FieldWithObjectType() {
        Field field = testDouble.getClass().getDeclaredField(FIELD_NAME);
        field.setAccessible(true);
        field.set(testDouble, "testString");
        Class expected = Object.class;
        Class actual = new GenericMaster().getGenericType(field);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericType_FieldWithSingleTypeArgument() {
        Field field = testDouble.getClass().getDeclaredField(FIELD_NAME);
        field.setAccessible(true);
        field.set(testDouble, Arrays.asList(new Integer(1)));
        Class expected = FIELD_TYPE;
        Class actual = new GenericMaster().getGenericType(field);
        assertEquals(expected, actual);

        field.set(testDouble, Arrays.asList(new Double(2.0)));
        expected = Number.class;
        actual = new GenericMaster().getGenericType(field);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericType_FieldWithListTypeArgument() {
        Field field = testDouble.getClass().getDeclaredField(FIELD_NAME);
        field.setAccessible(true);
        field.set(testDouble, Arrays.asList( Arrays.asList(new Integer(1)), new Integer(2) ));
        Class expected = FIELD_TYPE_WITH_LIST;
        Class actual = new GenericMaster().getGenericType(field);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericType_NullField() {
        Field field = null;
        new GenericMaster().getGenericType(field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericType_NullType() {
        Field field = testDouble.getClass().getDeclaredField(FIELD_NAME);
        Type type = null;
        new GenericMaster().getGenericType(field);
        field.set(testDouble, new Object[]{});
        type = field.getGenericType();
        new GenericMaster().getGenericType(field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericType_NonParameterizedType() {
        Field field = testDouble.getClass().getDeclaredField("hashCode");
        new GenericMaster().getGenericType(field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericType_FieldWithMoreThanOneTypeArgument() {
        Field field = testDouble.getClass().getDeclaredField("testField2");
        field.setAccessible(true);
        field.set(testDouble, Arrays.asList(new Integer(1), new Integer(2)));
        new GenericMaster().getGenericType(field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericType_FieldWithNestedListTypeArgument() {
        Field field = testDouble.getClass().getDeclaredField("testField3");
        field.setAccessible(true);
        field.set(testDouble, Arrays.asList( Arrays.asList(Arrays.asList(new Integer(1))), Arrays.asList(new Integer(2)) ));
        new GenericMaster().getGenericType(field);
    }
}