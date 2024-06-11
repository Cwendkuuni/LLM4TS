package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], referencedType);

        assertEquals(String[].class, referenceType.getRawType());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertEquals(new JavaType[0], referenceType.getSuperTypes());
        assertEquals(referencedType.getClass(), referenceType.getContentType().getClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawType());
    }

    @Test
    public void testReferenceTypeUpgrade() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(baseType, referencedType);

        assertEquals(String.class, referenceType.getRawType());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getBindings());
        assertEquals(null, referenceType.getSuperClass());
        assertEquals(new JavaType[0], referenceType.getSuperTypes());
        assertEquals(referencedType.getClass(), referenceType.getContentType().getClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawType());
    }

    @Test
    public void testReferenceTypeWidening() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType narrowerType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType widerType = ReferenceType.upgradeFrom(narrowerType, referencedType);

        assertEquals(baseType.getRawType(), widerType.getRawType());
        assertEquals(baseType.getBindings(), widerType.getBindings());
        assertEquals(baseType.getSuperClass(), widerType.getSuperClass());
        assertArrayEquals(baseType.getSuperTypes(), widerType.getSuperTypes());
        assertEquals(referencedType.getClass(), widerType.getContentType().getClass());
        assertEquals(Integer.class, widerType.getContentType().getRawType());
    }

    @Test
    public void testReferenceTypeIsReferenceType() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(referenceType.isReferenceType());

        JavaType simpleType = TypeFactory.defaultInstance().constructType(Integer.class);
        assertFalse(simpleType.isReferenceType());
    }

    @Test
    public void testReferenceTypeWithContentType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], referencedType);

        JavaType withContentType = referenceType.withContentType(TypeFactory.defaultInstance().constructType(Double.class));
        assertNotSame(referenceType, withContentType);
        assertEquals(String[].class, withContentType.getRawType());
        assertEquals(TypeBindings.emptyBindings(), withContentType.getBindings());
        assertEquals(superClass, withContentType.getSuperClass());
        assertArrayEquals(new JavaType[0], withContentType.getSuperTypes());
        assertEquals(Double.class, withContentType.getContentType().getRawType());
    }

    @Test
    public void testReferenceTypeWithAnchorType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType anchorType = ReferenceType.construct(baseType.getClass(), referencedType);
        JavaType referenceType = new ReferenceType(baseType, referencedType);

        JavaType withAnchorType = referenceType.withTypeHandler(anchorType);
        assertEquals(referenceType, withAnchorType);
        assertEquals(baseType.getClass(), withAnchorType.getAnchorType().getClass());
        assertEquals(baseType, withAnchorType.getAnchorType());
    }

    @Test
    public void testReferenceTypeWithValueHandler() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], referencedType);

        JavaType withValueHandler = referenceType.withValueHandler(TypeFactory.defaultInstance().getValueFactory().valueOf("test"));
        assertNotSame(referenceType, withValueHandler);
        assertEquals(String[].class, withValueHandler.getRawType());
        assertEquals(TypeBindings.emptyBindings(), withValueHandler.getBindings());
        assertEquals(superClass, withValueHandler.getSuperClass());
        assertArrayEquals(new JavaType[0], withValueHandler.getSuperTypes());
        assertEquals("test", withValueHandler.getValueHandler());
        assertEquals(Integer.class, withValueHandler.getContentType().getRawType());
    }

    @Test
    public void testReferenceTypeWithContentValueHandler() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], referencedType);

        JavaType withContentValueHandler = referenceType.withContentValueHandler(TypeFactory.defaultInstance().getValueFactory().valueOf(42));
        assertNotSame(referenceType, withContentValueHandler);
        assertEquals(String[].class, withContentValueHandler.getRawType());
        assertEquals(TypeBindings.emptyBindings(), withContentValueHandler.getBindings());
        assertEquals(superClass, withContentValueHandler.getSuperClass());
        assertArrayEquals(new JavaType[0], withContentValueHandler.getSuperTypes());
        assertEquals(42, withContentValueHandler.getContentValueHandler().intValue());
        assertEquals(Integer.class, withContentValueHandler.getContentType().getRawType());
    }

    @Test
    public void testReferenceTypeWithStaticTyping() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], referencedType);

        JavaType staticTyping = referenceType.withStaticTyping();
        assertNotSame(referenceType, staticTyping);
        assertEquals(String[].class, staticTyping.getRawType());
        assertEquals(TypeBindings.emptyBindings(), staticTyping.getBindings());
        assertEquals(superClass, staticTyping.getSuperClass());
        assertArrayEquals(new JavaType[0], staticTyping.getSuperTypes());
        assertFalse(staticTyping.isDynamicPolymorphic());
        assertTrue(staticTyping.isStaticTyping());
        assertEquals(Integer.class, staticTyping.getContentType().getRawType());
    }

    @Test
    public void testReferenceTypeEquals() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType1 = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], referencedType);
        JavaType referenceType2 = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], referencedType);

        assertEquals(referenceType1, referenceType2);

        JavaType referenceType3 = new ReferenceType(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], referencedType);
        assertNotEquals(referenceType1, referenceType3);

        JavaType referenceType4 = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], TypeFactory.defaultInstance().constructType(Long.class));
        assertNotEquals(referenceType1, referenceType4);
    }

    @Test
    public void testReferenceTypeHashCode() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referencedType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], referencedType);

        int hashCode1 = referenceType.hashCode();

        JavaType referenceType2 = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], referencedType);

        int hashCode2 = referenceType2.hashCode();

        assertEquals(hashCode1, hashCode2);

        JavaType referenceType3 = new ReferenceType(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], referencedType);

        int hashCode3 = referenceType3.hashCode();

        assertNotEquals(hashCode1, hashCode3);

        JavaType referenceType4 = ReferenceType.construct(String[].class, TypeBindings.emptyBindings(),
                superClass, new JavaType[0], TypeFactory.defaultInstance().constructType(Long.class));

        int hashCode4 = referenceType4.hashCode();

        assertNotEquals(hashCode1, hashCode4);
    }
}