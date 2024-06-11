package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intType = Integer.TYPE;
        SimpleType intSimpleType = new SimpleType(intType);

        assertEquals("java.lang.Integer", intSimpleType.getClassName());
        assertEquals(intType, intSimpleType.getType());
        assertFalse(intSimpleType.isArray());
        assertFalse(intSimpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeConstructionWithBindings() {
        Class<Map<String, Integer>> mapType = Map.class.cast(new Class[] {String.class, Integer.TYPE}[0].getClassFile());
        SimpleType mapSimpleType = new SimpleType(mapType, TypeBindings.emptyBindings());

        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapSimpleType.getClassName());
        assertEquals(mapType, mapSimpleType.getType());
        assertTrue(mapSimpleType.isArray());
        assertTrue(mapSimpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);

        SimpleType stringSimpleTypeCopy = new SimpleType(stringSimpleType);

        assertEquals(stringSimpleType, stringSimpleTypeCopy);
    }

    @Test
    public void testSimpleTypePassThroughConstructor() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), null, null);

        assertEquals(stringSimpleType, stringSimpleType.withStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<List<Integer>> listType = ArrayList.class.cast(new Class[] {List.class, Integer.TYPE}[0].getClassFile());
        new SimpleType(listType);
    }

    @Test
    public void testSimpleTypeGetCanonicalName() {
        Class<Map<String, List<Integer>>> mapListType = Map.class.cast(new Class[] {Map.class, List.class, Integer.TYPE}[0].getClassFile());
        SimpleType mapListSimpleType = new SimpleType(mapListType, TypeBindings.emptyBindings());

        assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>", mapListSimpleType.getCanonicalName());
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<String> stringType1 = String.class;
        SimpleType stringSimpleType1 = new SimpleType(stringType1);

        Class<String> stringType2 = String.class;
        SimpleType stringSimpleType2 = new SimpleType(stringType2);

        SimpleType stringSimpleType3 = new SimpleType(stringSimpleType1);

        assertTrue(stringSimpleType1.equals(stringSimpleType1));
        assertTrue(stringSimpleType1.equals(stringSimpleType3));
        assertTrue(stringSimpleType1.equals(stringSimpleType2));
        assertFalse(stringSimpleType1.equals(null));
        assertFalse(stringSimpleType1.equals(new Object()));
    }

    @Test
    public void testSimpleTypeGetSuperType() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);

        SimpleType objectSimpleType = new SimpleType(Object.class);

        JavaType superType = stringSimpleType.getSuperType();
        assertEquals(objectSimpleType.getType(), superType.getType());
    }

    @Test
    public void testSimpleTypeWithContentType() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);

        JavaType contentType = TypeFactory.stringType();

        assertNull(stringSimpleType.withContentType(contentType));
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);

        Object stringHandler = new Object() {
            public String toString(String s) {
                return "handled-" + s;
            }
        };

        SimpleType stringSimpleTypeWithHandler = stringSimpleType.withTypeHandler(stringHandler);

        assertEquals(stringHandler, stringSimpleTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);

        SimpleType stringSimpleTypeStatic = stringSimpleType.withStaticTyping();

        assertTrue(stringSimpleTypeStatic.isStatic());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);

        Class<Object> objectType = Object.class;
        SimpleType objectSimpleType = new SimpleType(objectType);

        SimpleType narrowedType = stringSimpleType._narrow(objectType);
        assertEquals(stringSimpleType, narrowedType);
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);

        Object stringHandler = new Object() {
            public String toString(String s) {
                return "handled-" + s;
            }
        };

        SimpleType stringSimpleTypeWithHandler = stringSimpleType.withValueHandler(stringHandler);

        assertEquals(stringHandler, stringSimpleTypeWithHandler.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithContentValueHandler() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);

        Object stringContentHandler = new Object() {
            public String toString(String s) {
                return "handled-" + s;
            }
        };

        assertNull(stringSimpleType.withContentValueHandler(stringContentHandler));
    }
}