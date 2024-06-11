package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        // Test constructors
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(int[].class, referenceType.getRawClass());

        // Test factory method
        JavaType cls = TypeFactory.defaultInstance().constructType(ReferenceType.class);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType2 = ReferenceType.construct(cls, TypeBindings.emptyBindings(), null, null, refType2);
        assertEquals(Integer.class, referenceType2.getContentType().getRawClass());
        assertEquals(ReferenceType.class, referenceType2.getRawClass());

        // Test deprecated constructor
        JavaType cls2 = TypeFactory.defaultInstance().constructType(ReferenceType.class);
        JavaType refType3 = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType3 = ReferenceType.construct(cls2, refType3);
        assertEquals(Integer.class, referenceType3.getContentType().getRawClass());
        assertEquals(ReferenceType.class, referenceType3.getRawClass());
    }

    @Test
    public void testReferenceTypeMethods() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        JavaType contentType = referenceType.withContentType(TypeFactory.defaultInstance().constructType(Character.class));
        assertEquals(Character.class, contentType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertNotEquals(Character.class, referenceType.getContentType().getRawClass());

        ReferenceType referenceType2 = referenceType.withTypeHandler(new TypeIdResolverDelegate());
        assertNotEquals(referenceType2.getTypeHandler(), referenceType.getTypeHandler());
        referenceType2 = referenceType.withTypeHandler(referenceType.getTypeHandler());
        assertEquals(referenceType2.getTypeHandler(), referenceType.getTypeHandler());

        ReferenceType referenceType3 = referenceType.withContentTypeHandler(new TypeIdResolverDelegate());
        assertNotEquals(referenceType3.getReferencedType().getTypeHandler(), referenceType.getReferencedType().getTypeHandler());
        referenceType3 = referenceType.withContentTypeHandler(referenceType.getReferencedType().getTypeHandler());
        assertEquals(referenceType3.getReferencedType().getTypeHandler(), referenceType.getReferencedType().getTypeHandler());

        ReferenceType referenceType4 = referenceType.withValueHandler(new TypeIdResolverDelegate());
        assertNotEquals(referenceType4.getValueHandler(), referenceType.getValueHandler());
        referenceType4 = referenceType.withValueHandler(referenceType.getValueHandler());
        assertEquals(referenceType4.getValueHandler(), referenceType.getValueHandler());

        ReferenceType referenceType5 = referenceType.withContentValueHandler(new TypeIdResolverDelegate());
        assertNotEquals(referenceType5.getReferencedType().getValueHandler(), referenceType.getReferencedType().getValueHandler());
        referenceType5 = referenceType.withContentValueHandler(referenceType.getReferencedType().getValueHandler());
        assertEquals(referenceType5.getReferencedType().getValueHandler(), referenceType.getReferencedType().getValueHandler());

        ReferenceType referenceType6 = referenceType.withStaticTyping();
        assertTrue(referenceType6.isStatic());
        referenceType6 = referenceType.withStaticTyping();
        assertTrue(referenceType6.isStatic());

        ReferenceType referenceType7 = referenceType.refine(int.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(int.class, referenceType7.getRawClass());
        assertNotEquals(String.class, referenceType7.getContentType().getRawClass());

        // Test getters
        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
        assertTrue(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeConstructionFailure() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType refType = null;
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
    }
}