package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.types.ClassType;

public class ReferenceTypeTest {

    // Test data
    private static Class<?> clazz = String.class;
    private static JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(Array.class, String.class);
    private static JavaType referenceType = TypeFactory.defaultInstance().constructReferenceType(clazz, new TypeBindings.Builder().build());
    private static JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
    private static JavaType baseType = new ClassType(clazz);

    @Test
    public void testReferenceTypeConstruction() {
        ReferenceType referenceType1 = ReferenceType.construct(clazz, new TypeBindings.Builder().build(), null, null, referencedType);
        ReferenceType referenceType2 = new ReferenceType(clazz, new TypeBindings.Builder().build(), null, null, referencedType);
        ReferenceType referenceType3 = new ReferenceType(clazz, new TypeBindings.Builder().build(), referencedType.getTypeId(), null, referencedType);
        ReferenceType referenceType4 = ReferenceType.upgradeFrom(baseType, referencedType);

        assertEquals(referenceType1, referenceType2);
        assertEquals(referenceType1, referenceType3);
        assertEquals(referenceType1, referenceType4);
        assertEquals(clazz, referenceType1.getRawClass());
        assertEquals(referencedType, referenceType1.getContentType());
        assertFalse(referenceType1.isAnchorType());
    }

    @Test
    public void testReferenceTypeGetters() {
        ReferenceType referenceType = new ReferenceType(clazz, new TypeBindings.Builder().build(), referencedType, null, null, null, null, null, false);

        assertEquals(clazz, referenceType.getRawClass());
        assertEquals(referencedType, referenceType.getContentType());
        assertTrue(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
        assertEquals(javaType, referenceType.withContentType(javaType));
        assertNotNull(referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testReferenceTypeEquality() {
        ReferenceType referenceType1 = new ReferenceType(clazz, new TypeBindings.Builder().build(), referencedType, null, null, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(clazz, new TypeBindings.Builder().build(), referencedType, null, null, null, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(clazz, new TypeBindings.Builder().build(), referencedType, null, null, null, null, null, true);
        ReferenceType referenceType4 = new ReferenceType(Object.class, new TypeBindings.Builder().build(), referencedType, null, null, null, null, null, false);
        ReferenceType referenceType5 = new ReferenceType(clazz, new TypeBindings.Builder().build(), javaType, null, null, null, null, null, false);

        assertEquals(referenceType1, referenceType1);
        assertEquals(referenceType1, referenceType2);
        assertNotSame(referenceType1, referenceType3);
        assertNotEquals(referenceType1, referenceType4);
        assertNotEquals(referenceType1, referenceType5);
    }

    @Test
    public void testReferenceTypeNarrowing() {
        ReferenceType referenceType = new ReferenceType(String[].class, new TypeBindings.Builder().build(), null, null, referencedType, null, null, null, false);
        ReferenceType narrowedType = referenceType.narrow(String[].class);

        assertEquals(referenceType.getRawClass(), narrowedType.getRawClass());
        assertEquals(referenceType.getContentType(), narrowedType.getContentType());
        assertTrue(narrowedType.isReferenceType());
        assertSame(referenceType, narrowedType); // Since it's not implemented
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeInvalidUpgrade() {
        ReferenceType referenceType = new ReferenceType(Integer.TYPE.getClass(), new TypeBindings.Builder().build(), referencedType, null, null, null, null, null, false);
        TypeFactory.defaultInstance().upgradeFrom(baseType, referenceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeNullReferenceType() {
        ReferenceType referenceType = new ReferenceType(clazz, new TypeBindings.Builder().build(), referencedType, null, null, null, null, null, false);
        ReferenceType.upgradeFrom(baseType, null);
    }
}