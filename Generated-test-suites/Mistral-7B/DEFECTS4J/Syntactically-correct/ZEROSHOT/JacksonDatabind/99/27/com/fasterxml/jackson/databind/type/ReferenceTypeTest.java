package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeModifier;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType baseType = new TypeBase() {
            @Override
            public JavaType getBaseType() {
                return TypeFactory.defaultInstance().constructType(String.class);
            }
        };
        JavaType anchorType = null;

        // Test default constructor
        ReferenceType referenceType1 = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, refType, anchorType, null, null, false);
        assertEquals(stringClass, referenceType1._class);
        assertEquals(refType, referenceType1._referencedType);
        assertNull(referenceType1._anchorType);

        // Test constructor with anchor type
        anchorType = new TypeReference<Object>() {}.getType();
        ReferenceType referenceType2 = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, refType, anchorType, null, null, false);
        assertEquals(stringClass, referenceType2._class);
        assertEquals(refType, referenceType2._referencedType);
        assertEquals(anchorType, referenceType2._anchorType);

        // Test constructor with base type
        TypeBase base = new TypeBase() {
            @Override
            public JavaType getBaseType() {
                return TypeFactory.defaultInstance().constructType(String.class);
            }
        };
        ReferenceType referenceType3 = new ReferenceType(base, refType);
        assertEquals(base.getClass(), referenceType3._class);
        assertEquals(refType, referenceType3._referencedType);
        assertEquals(base, referenceType3._anchorType);

        // Test constructor with base and anchor type
        JavaType baseType2 = new TypeBase() {
            @Override
            public JavaType getBaseType() {
                return TypeFactory.defaultInstance().constructType(Number.class);
            }
        }.getType();
        JavaType anchorType2 = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType4 = new ReferenceType(baseType2, refType);
        assertEquals(baseType2, referenceType4._anchorType);
        assertEquals(refType, referenceType4._referencedType);
        assertNotSame(baseType2, referenceType4._class);

        // Test deprecated constructors
        JavaType refTypeDeprecated = new TypeReference<Object>() {}.getType();
        JavaType baseTypeDeprecated = new TypeBase() {
            @Override
            public JavaType getBaseType() {
                return TypeFactory.defaultInstance().constructType(String.class);
            }
        }.getType();
        ReferenceType referenceType5 = new ReferenceType(stringClass, refTypeDeprecated);
        assertEquals(stringClass, referenceType5._class);
        assertEquals(refTypeDeprecated, referenceType5._referencedType);
        assertNull(referenceType5._anchorType);

        ReferenceType referenceType6 = new ReferenceType(stringClass, baseTypeDeprecated);
        assertEquals(stringClass, referenceType6._class);
        assertEquals(baseTypeDeprecated, referenceType6._referencedType);
        assertSame(baseTypeDeprecated, referenceType6._anchorType);
    }

    @Test
    public void testReferenceTypeMethods() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);

        // Test getContentType()
        ReferenceType referenceType = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        assertEquals(refType, referenceType.getContentType());

        // Test getReferencedType()
        assertEquals(refType, referenceType.getReferencedType());

        // Test hasContentType()
        assertTrue(referenceType.hasContentType());

        // Test isReferenceType()
        assertTrue(referenceType.isReferenceType());

        // Test getErasedSignature()
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());

        // Test getGenericSignature()
        expected = new StringBuilder("[Ljava.lang.String;<T:Ljava.lang.Object;>Ljava.lang.reflect.Type;>");
        actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());

        // Test getAnchorType()
        ReferenceType referenceType2 = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        assertNull(referenceType2.getAnchorType());

        ReferenceType referenceType3 = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, refType, TypeFactory.defaultInstance().constructType(String.class), null, null, false);
        assertEquals(referenceType3, referenceType3.getAnchorType());
    }

    @Test
    public void testReferenceTypeUpgrade() {
        Class<String> stringClass = String.class;
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);

        // Test upgradeFrom()
        JavaType upgradedType = ReferenceType.upgradeFrom(simpleType, refType);
        assertEquals(refType, upgradedType);

        // Test construct()
        ReferenceType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, refType);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(stringClass, referenceType._class);
    }

    @Test
    public void testReferenceTypeWithContentType() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType withContentType = refType.withContentType(TypeFactory.defaultInstance().constructType(Integer.class));

        ReferenceType referenceType = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        ReferenceType withContentTypeReferenceType = referenceType.withContentType(TypeFactory.defaultInstance().constructType(Integer.class));

        assertEquals(withContentType, withContentTypeReferenceType._referencedType);
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);

        Object handler = new Object() {};

        ReferenceType referenceType = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        ReferenceType withTypeHandler = referenceType.withTypeHandler(handler);

        assertEquals(handler, withTypeHandler._typeHandler);
    }

    @Test
    public void testReferenceTypeWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);

        Object handler = new Object() {};

        ReferenceType referenceType = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        ReferenceType withContentTypeHandler = referenceType.withContentTypeHandler(handler);

        assertEquals(handler, withContentTypeHandler._referencedType.<Object>getTypeHandler());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);

        Object handler = new Object() {};

        ReferenceType referenceType = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        ReferenceType withValueHandler = referenceType.withValueHandler(handler);

        assertEquals(handler, withValueHandler._valueHandler);
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);

        Object handler = new Object() {};

        ReferenceType referenceType = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        ReferenceType withContentValueHandler = referenceType.withContentValueHandler(handler);

        assertEquals(handler, withContentValueHandler._referencedType.<Object>getValueHandler());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);

        ReferenceType referenceType = new ReferenceType(stringClass, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        ReferenceType withStaticTyping = referenceType.withStaticTyping();

        assertTrue(withStaticTyping._asStatic);
    }

    @Test
    public void testReferenceTypeRefine() {
        Class<String> stringClass = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType refinedType = referenceType.refine(String[].class, TypeBindings.emptyBindings(), null, null);

        assertEquals(refinedType, refType);
    }

    @Test
    public void testReferenceTypeNarrow() {
        // Not implemented and marked as deprecated
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeNoReferencedType() {
        Class<String> stringClass = String.class;
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType referenceType = new ReferenceType(baseType, null);
    }
}