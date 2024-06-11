package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> intClass = int.class;
        SimpleType simpleType = new SimpleType(intClass);
        SimpleType simpleTypeWithBindings = new SimpleType(intClass, TypeBindings.emptyBindings());
        SimpleType simpleTypeWithSuperClass = new SimpleType(intClass, TypeBindings.emptyBindings(), TypeFactory.unknownType());
        SimpleType simpleTypeWithSuperInterfaces = new SimpleType(intClass, TypeBindings.emptyBindings(), null, new JavaType[] {TypeFactory.unknownType(), TypeFactory.unknownType()});
        SimpleType simpleTypeWithValues = new SimpleType(intClass, TypeBindings.emptyBindings(), TypeFactory.unknownType(), null, new Object() {});
        SimpleType simpleTypeWithTypeHandler = new SimpleType(intClass, TypeBindings.emptyBindings(), TypeFactory.unknownType(), null, null, new Object() {});

        assertEquals(intClass, simpleType.getRawClass());
        assertEquals(simpleType, simpleTypeWithBindings.withBindings(TypeBindings.emptyBindings()));
        assertEquals(simpleTypeWithSuperClass, simpleType.withSuperClass(TypeFactory.unknownType()));
        assertEquals(simpleTypeWithSuperInterfaces, simpleType.withSuperTypes(TypeFactory.unknownType(), TypeFactory.unknownType()));
        assertEquals(simpleTypeWithValues.withValueHandler(new Object()), simpleType.withValueHandler(new Object()));
        assertEquals(simpleTypeWithTypeHandler.withTypeHandler(new Object()), simpleType.withTypeHandler(new Object()));
    }

    @Test
    public void testSimpleTypeMethods() {
        Class<?> intClass = int.class;
        SimpleType simpleType = new SimpleType(intClass);

        assertFalse(simpleType.isContainerType());
        assertEquals("[simple type, class java.lang.Integer]", simpleType.toString());
        assertEquals("java.lang.Integer", simpleType.getCanonicalName());

        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Integer", sb.toString());

        sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("<java.lang.Integer>", sb.toString());

        assertNull(simpleType.withContentType(TypeFactory.stringType()));
        assertNotSame(simpleType, simpleType.withTypeHandler(new Object()));
    }

    @Test
    public void testSimpleTypeNarrowing() {
        SimpleType intType = new SimpleType(int.class);
        SimpleType longType = new SimpleType(long.class);

        assertNotSame(intType, longType);
        assertSame(intType, intType.narrow(int.class));
        assertSame(longType, longType.narrow(long.class));
        assertSame(intType, intType.narrow(int[].class));
        assertSame(longType, longType.narrow(long[].class));
        assertNotSame(intType, longType.narrow(long.class));
        assertNotSame(longType, intType.narrow(int.class));
        assertNull(intType.narrow(String.class));
        assertNull(longType.narrow(String.class));
    }

    @Test
    public void testSimpleTypeRefine() {
        SimpleType intType = new SimpleType(int.class);
        SimpleType longType = new SimpleType(long.class);

        assertNull(intType.refine(int[].class, TypeBindings.emptyBindings(), null, null));
        assertNull(longType.refine(int.class, TypeBindings.emptyBindings(), null, null));
        assertNull(intType.refine(long.class, TypeBindings.emptyBindings(), null, null));
        assertNull(longType.refine(int[].class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testSimpleTypeWithContentType() {
        SimpleType intType = new SimpleType(int.class);
        JavaType stringType = TypeFactory.stringType();

        assertNull(intType.withContentType(stringType));
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        JavaType stringType = TypeFactory.stringType();
        SimpleType intType = new SimpleType(int.class, null, null, null, null, null, false);
        SimpleType staticIntType = intType.withStaticTyping();

        assertFalse(intType.isStatic());
        assertTrue(staticIntType.isStatic());
        assertNotSame(intType, staticIntType);
        assertSame(intType, intType.withStaticTyping());
        assertSame(stringType, stringType);
        assertNotSame(intType, stringType);
        assertNotSame(staticIntType, stringType);
        assertSame(staticIntType, staticIntType.withStaticTyping());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        SimpleType intType = new SimpleType(int.class);
        JavaType javaIntType = TypeFactory.constructType(Integer.TYPE);
        TypeBindings bindings = new TypeBindings();
        Class<?> stringClass = String.class;
        JavaType stringType = TypeFactory.constructType(stringClass);

        SimpleType simpleTypeWithBindings = intType.withBindings(bindings);
        SimpleType simpleTypeWithBindingsAndContentType = intType.withBindingsAndContentType(bindings, stringType);
        SimpleType simpleTypeWithBindingsAndSuperClass = intType.withBindingsAndSuperClass(bindings, javaIntType);
        SimpleType simpleTypeWithBindingsAndSuperInterfaces = intType.withBindingsAndSuperInterfaces(bindings, javaIntType, stringType);

        assertSame(intType, simpleTypeWithBindings.withBindings(TypeBindings.emptyBindings()));
        assertNotSame(intType, simpleTypeWithBindings);
        assertSame(intType, simpleTypeWithBindingsAndContentType.withContentType(null));
        assertNotSame(intType, simpleTypeWithBindingsAndContentType);
        assertSame(intType, simpleTypeWithBindingsAndSuperClass.withSuperClass(null));
        assertNotSame(intType, simpleTypeWithBindingsAndSuperClass);
        assertSame(intType, simpleTypeWithBindingsAndSuperInterfaces.withSuperTypes(null, null));
        assertNotSame(intType, simpleTypeWithBindingsAndSuperInterfaces);
    }

    @Test
    public void testSimpleTypeWithContentValueHandler() {
        SimpleType intType = new SimpleType(int.class);
        Object intHandler = new Object();

        assertNull(intType.withContentValueHandler(intHandler));
    }
}