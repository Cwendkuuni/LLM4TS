package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        assertEquals("ReferenceType for String and Integer", referenceType.toString(), "[reference type, class [Ljava.lang.String;<Integer>]");
        assertEquals("Referenced type is Integer", referenceType.getContentType().toString(), "[int TypeId#5]");
    }

    @Test
    public void testConstructorWithSuperClass() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType superclass = JavaType.construct(Object.class);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), superclass, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), superclass, null, referencedType, anchorType);
        assertEquals("ReferenceType for String and Integer with Object as superclass", referenceType.toString(), "[reference type, class [Ljava.lang.String;<java.lang.Object>], superclass class java.lang.Object, interfaces [], type id 0, value handler class null, type handler class null, static true]");
    }

    @Test
    public void testUpgradeFrom() {
        Class<String> stringClass = String.class;
        JavaType simpleType = JavaType.construct(String.class);
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, referencedType);
        assertEquals("Referenced type is Integer", referenceType.getContentType().toString(), "[int TypeId#5]");
        assertTrue("Is instance of ReferenceType", referenceType instanceof ReferenceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullReferencedType() {
        Class<String> stringClass = String.class;
        JavaType simpleType = JavaType.construct(String.class);
        JavaType referenceType = ReferenceType.upgradeFrom(simpleType, null);
    }

    @Test
    public void testWithContentType() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        JavaType newReferenceType = referenceType.withContentType(JavaType.construct(Long.TYPE));
        assertEquals("Referenced type is Long", newReferenceType.getContentType().toString(), "[long TypeId#6]");
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        Object handler = new Object();
        JavaType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotNull("Type handler is set", newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        JavaType newReferenceType = referenceType.withContentTypeHandler(valueHandler).withTypeHandler(typeHandler);
        assertNotNull("Value handler is set", newReferenceType.getValueHandler());
        assertNotNull("Type handler is set", newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        Object handler = new Object();
        JavaType newReferenceType = referenceType.withValueHandler(handler);
        assertNotNull("Value handler is set", newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        Object valueHandler = new Object();
        JavaType newReferenceType = referenceType.withContentValueHandler(valueHandler);
        assertNotNull("Content value handler is set", newReferenceType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        JavaType newReferenceType = referenceType.withStaticTyping();
        assertTrue("Is static typing", newReferenceType.isStaticTyping());
    }

    @Test
    public void testGetContentType() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        assertEquals("Referenced type is Integer", referenceType.getContentType().toString(), "[int TypeId#5]");
    }

    @Test
    public void testGetReferencedType() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        assertEquals("Referenced type is Integer", referenceType.getReferencedType().toString(), "[int TypeId#5]");
    }

    @Test
    public void testHasContentType() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        assertTrue("Has content type", referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType referencedType = JavaType.construct(Integer.TYPE);
        JavaType anchorType = JavaType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType referenceType = ReferenceType.construct(stringClass, TypeBindings.emptyBindings(), null, null, referencedType, anchorType);
        assertTrue("Is reference type", referenceType.isReferenceType());
    }
}