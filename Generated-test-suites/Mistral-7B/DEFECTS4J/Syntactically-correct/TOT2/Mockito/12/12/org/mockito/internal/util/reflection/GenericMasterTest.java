// Test cases for the GenericMaster class

package org.junit.platform.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {

    // Test case for the getGenericType method with a valid field and generic type
    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = this.getClass().getDeclaredField("testField");
        assertEquals(String.class, genericMaster.getGenericType(field));
    }

    // Test case for the getGenericType method with a null field
    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWithNullField() {
        GenericMaster genericMaster = new GenericMaster();
        assertNull(genericMaster.getGenericType(null));
    }

    // Test case for the getGenericType method with a non-generic field
    @Test
    public void testGetGenericTypeWithNonGenericField() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = this.getClass().getDeclaredField("testIntField");
        assertEquals(Integer.class, genericMaster.getGenericType(field));
    }

    // Test case for the getGenericType method with a nested generic type
    @Test
    public void testGetGenericTypeWithNestedGenerics() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = List.class.getDeclaredField("list");
        assertEquals(List.class, genericMaster.getGenericType(field));
    }

    // Test case for the getGenericType method with an invalid type
    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericTypeWithInvalidType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = this.getClass().getDeclaredField("testField");
        Type invalidType = new Type() {}; // Invalid type
        assertNotSame(String.class, genericMaster.getGenericType((Field) invalidType));
    }

    // Test case for the getGenericType method with empty type arguments
    @Test
    public void testGetGenericTypeWithEmptyTypeArguments() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = List.class.getDeclaredField("list");
        Type emptyType = new ParameterizedType() {}.getRawType();
        assertEquals(List.class, genericMaster.getGenericType(field));
    }
}

//