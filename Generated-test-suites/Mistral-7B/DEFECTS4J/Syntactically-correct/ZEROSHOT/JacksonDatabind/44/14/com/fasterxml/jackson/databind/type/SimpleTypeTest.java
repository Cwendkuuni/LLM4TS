package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.getRawType();
        assertEquals(String.class, javaType.getRawClass());

        SimpleType simpleTypeWithBindings = new SimpleType(stringClass, TypeBindings.defaultBindings());
        JavaType javaTypeWithBindings = simpleTypeWithBindings.getRawType();
        assertEquals(String.class, javaTypeWithBindings.getRawClass());

        SimpleType simpleTypeWithSuperClass = new SimpleType(String[].class);
        JavaType javaTypeWithSuperClass = simpleTypeWithSuperClass.getRawType();
        assertArrayEquals(String[].class, javaTypeWithSuperClass.getRawTypes());

        SimpleType simpleTypeWithSuperInterfaces = new SimpleType(String[].class, TypeBindings.defaultBindings(),
                TypeFactory.unknownType(), new JavaType[] { List.class });
        JavaType javaTypeWithSuperInterfaces = simpleTypeWithSuperInterfaces.getRawType();
        JavaType[] javaTypeWithSuperInterfacesRawTypes = javaTypeWithSuperInterfaces.getRawTypes();
        assertArrayEquals(new Class<?>[] { List.class }, javaTypeWithSuperInterfacesRawTypes);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType originalSimpleType = new SimpleType(stringClass, TypeBindings.defaultBindings());

        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        JavaType originalJavaType = originalSimpleType.getRawType();
        JavaType copiedJavaType = copiedSimpleType.getRawType();
        assertEquals(originalJavaType, copiedJavaType);
    }

    @Test
    public void testSimpleTypePassThroughConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new ReferenceType(stringClass, TypeBindings.defaultBindings());
        JavaType javaType = new SimpleType(simpleType).getRawType();
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testUnsafeTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        JavaType javaType = simpleType.getRawType();
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingMapType() {
        new SimpleType(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingCollectionType() {
        new SimpleType(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingArrayType() {
        new SimpleType(int[].class);
    }

    @Test
    public void testNarrowMethod() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType narrowedType = simpleType.narrow(integerClass);
        JavaType javaType = narrowedType.getRawType();
        assertFalse(integerClass.isAssignableFrom(javaType.getRawClass()));
        assertEquals(SimpleType.class, narrowedType.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenNarrowingToIncompatibleType() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType = new SimpleType(stringClass);
        simpleType.narrow(integerClass);
    }

    @Test
    public void testWithContentTypeMethod() {
        JavaType listJavaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        SimpleType simpleType = new SimpleType(Integer.class);
        JavaType javaTypeWithContentType = simpleType.withContentType(listJavaType);
        assertEquals(List.class, javaTypeWithContentType.getRawClass());
        assertEquals(Integer.class, javaTypeWithContentType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenSettingContentTypeOnSimpleType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        simpleType.withContentType(null);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        SimpleType simpleType = new SimpleType(Integer.class);
        Object handler = new Object() {};
        SimpleType simpleTypeWithTypeHandler = simpleType.withTypeHandler(handler);
        assertEquals(handler, simpleTypeWithTypeHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenSettingTypeHandlerOnSimpleType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        simpleType.withTypeHandler(null);
    }

    @Test
    public void testWithValueHandlerMethod() {
        SimpleType simpleType = new SimpleType(Integer.class);
        Object handler = new Object() {};
        SimpleType simpleTypeWithValueHandler = simpleType.withValueHandler(handler);
        assertEquals(handler, simpleTypeWithValueHandler.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenSettingValueHandlerOnSimpleType() {
        SimpleType simpleType = new SimpleType(Integer.class);
        simpleType.withValueHandler(null);
    }

    @Test
    public void testWithStaticTypingMethod() {
        SimpleType simpleType = new SimpleType(Integer.class);
        SimpleType simpleTypeWithStaticTyping = simpleType.withStaticTyping();
        assertTrue(simpleTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testRefineMethod() {
        Class<List<Integer>> listOfIntegerClass = List.class.getTypeParameters()[0].getType();
        SimpleType simpleType = new SimpleType(List.class);
        JavaType javaType = simpleType.refine(listOfIntegerClass, TypeBindings.emptyBindings(), null, null);
        assertNull(javaType);
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        Class<List<List<String>>> doubleNestedListClass = List.class.getGenericType();
        TypeVariable<?> listTypeVariable = (TypeVariable<?>) doubleNestedListClass.getGenericComponentType();
        Class<List<String>> innerListClass = List.class;
        Class<String> stringClass = String.class;

        SimpleType simpleType = new SimpleType(innerListClass);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("[Ljava.util.List;", canonicalName);

        SimpleType nestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings());
        canonicalName = nestedSimpleType.buildCanonicalName();
        assertEquals("[Ljava.util.List;", canonicalName);

        SimpleType doubleNestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings(), innerListClass);
        canonicalName = doubleNestedSimpleType.buildCanonicalName();
        assertEquals("[Ljava.util.List;[Ljava.lang.String;", canonicalName);

        SimpleType tripleNestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings(), innerListClass, listTypeVariable);
        canonicalName = tripleNestedSimpleType.buildCanonicalName();
        assertEquals("[Ljava.util.List;[Ljava.util.List;[Ljava.lang.String;", canonicalName);

        SimpleType quadrupleNestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings(), List.class, listTypeVariable, stringClass);
        canonicalName = quadrupleNestedSimpleType.buildCanonicalName();
        assertEquals("[Ljava.util.List;[Ljava.util.List;[Ljava.util.List;[Ljava.lang.String;", canonicalName);
    }

    @Test
    public void testGetErasedSignatureMethod() {
        Class<List<List<String>>> doubleNestedListClass = List.class.getGenericType();
        TypeVariable<?> listTypeVariable = (TypeVariable<?>) doubleNestedListClass.getGenericComponentType();
        Class<List<String>> innerListClass = List.class;
        Class<String> stringClass = String.class;

        SimpleType simpleType = new SimpleType(innerListClass);
        StringBuilder erasedSignature = new StringBuilder();
        simpleType.getErasedSignature(erasedSignature);
        assertEquals("java.util.List", erasedSignature.toString());

        SimpleType nestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings());
        erasedSignature = new StringBuilder();
        nestedSimpleType.getErasedSignature(erasedSignature);
        assertEquals("java.util.List", erasedSignature.toString());

        SimpleType doubleNestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings(), innerListClass);
        erasedSignature = new StringBuilder();
        doubleNestedSimpleType.getErasedSignature(erasedSignature);
        assertEquals("java.util.List<Ljava.util.List;>", erasedSignature.toString());

        SimpleType tripleNestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings(), innerListClass, listTypeVariable);
        erasedSignature = new StringBuilder();
        tripleNestedSimpleType.getErasedSignature(erasedSignature);
        assertEquals("java.util.List<java.util.List<Ljava.util.List;>>", erasedSignature.toString());

        SimpleType quadrupleNestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings(), List.class, listTypeVariable, stringClass);
        erasedSignature = new StringBuilder();
        quadrupleNestedSimpleType.getErasedSignature(erasedSignature);
        assertEquals("java.util.List<java.util.List<java.util.List<Ljava.util.List<Ljava.lang.String;>>>>", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        Class<List<List<String>>> doubleNestedListClass = List.class.getGenericType();
        TypeVariable<?> listTypeVariable = (TypeVariable<?>) doubleNestedListClass.getGenericComponentType();
        Class<List<String>> innerListClass = List.class;
        Class<String> stringClass = String.class;

        SimpleType simpleType = new SimpleType(innerListClass);
        StringBuilder genericSignature = new StringBuilder();
        simpleType.getGenericSignature(genericSignature);
        assertEquals("<Ljava.util.List<Ljava.lang.Object;>;>", genericSignature.toString());

        SimpleType nestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings());
        genericSignature = new StringBuilder();
        nestedSimpleType.getGenericSignature(genericSignature);
        assertEquals("<Ljava.util.List<Ljava.lang.Object;>;>", genericSignature.toString());

        SimpleType doubleNestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings(), innerListClass);
        genericSignature = new StringBuilder();
        doubleNestedSimpleType.getGenericSignature(genericSignature);
        assertEquals("<Ljava.util.List<Ljava.util.List<*>;>;>", genericSignature.toString());

        SimpleType tripleNestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings(), innerListClass, listTypeVariable);
        genericSignature = new StringBuilder();
        tripleNestedSimpleType.getGenericSignature(genericSignature);
        assertEquals("<Ljava.util.List<Ljava.util.List<Ljava.util.List<*>;>>;>", genericSignature.toString());

        SimpleType quadrupleNestedSimpleType = new SimpleType(List.class, TypeBindings.defaultBindings(), List.class, listTypeVariable, stringClass);
        genericSignature = new StringBuilder();
        quadrupleNestedSimpleType.getGenericSignature(genericSignature);
        assertEquals("<Ljava.util.List<Ljava.util.List<Ljava.util.List<Ljava.util.List<*>;>>>;>", genericSignature.toString());
    }

    @Test
    public void testEqualsMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass, TypeBindings.defaultBindings());
        SimpleType simpleType2 = new SimpleType(stringClass, TypeBindings.defaultBindings());
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(List.class, TypeBindings.defaultBindings());
        assertFalse(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = new SimpleType(stringClass, TypeBindings.emptyBindings());
        assertFalse(simpleType1.equals(simpleType4));
    }

    @Test
    public void testToStringMethod() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String stringRepresentation = simpleType.toString();
        String expectedStringRepresentation = "[simple type, class " + stringClass.getName() + "]";
        assertEquals(expectedStringRepresentation, stringRepresentation);
    }
}