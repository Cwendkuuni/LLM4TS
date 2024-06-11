package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default construction
        Class<?> intClass = Integer.TYPE;
        SimpleType intType = new SimpleType(intClass);
        assertEquals(Integer.TYPE, intType.getRawClass());
        assertNull(intType.getSuperClass());
        assertNull(intType.getSuperInterfaces());
        assertFalse(intType.isContainerType());

        // Test construction with bindings, superClass, and superInterfaces
        TypeBindings bindings = new TypeBindings();
        JavaType listIntType = new SimpleType(intClass, bindings, null, new JavaType[] {JavaType.getCollectionType(List.class, Integer.TYPE)});
        assertEquals(Integer.TYPE, listIntType.getRawClass());
        assertEquals(JavaType.getCollectionType(List.class, Integer.TYPE), listIntType.getSuperClass());
        assertArrayEquals(new JavaType[] {JavaType.getCollectionType(List.class, Integer.TYPE)}, listIntType.getSuperInterfaces());
        assertTrue(listIntType.isContainerType());

        // Test copy constructor
        SimpleType copiedIntType = new SimpleType(intType);
        assertEquals(intType, copiedIntType);

        // Test refined construction
        SimpleType refinedIntType = new SimpleType(intType.withBinding("newName", String.class));
        assertNotEquals(intType, refinedIntType);
        assertEquals("newName", refinedIntType.getBinding("newName").getType().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalConstruction() {
        // Test illegal construction with Map and Collection classes
        SimpleType mapType = new SimpleType(Map.class);
        SimpleType collectionType = new SimpleType(Collection.class);
        SimpleType arrayType = new SimpleType(int[].class);

        // Test deprecated construct method
        SimpleType deprecatedConstructType = SimpleType.construct(intClass);
        assertEquals(intType, deprecatedConstructType);
        deprecatedConstructType = SimpleType.construct(String.class);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testTypeNarrowing() {
        // Test type narrowing with same class
        SimpleType intType = new SimpleType(Integer.TYPE);
        SimpleType intSubclassType = new SimpleType(Integer.TYPE, null, intType, null);
        SimpleType narrowedType = intType.narrow(Integer.class);
        assertEquals(intSubclassType, narrowedType);

        // Test type narrowing with superclass
        SimpleType superclassType = new SimpleType(Object.class, null, null, null);
        SimpleType subclassType = new SimpleType(String.class, null, superclassType, null);
        SimpleType narrowedSuperclassType = superclassType.narrow(String.class);
        assertEquals(subclassType, narrowedSuperclassType);

        // Test type narrowing with interface
        SimpleType interfaceType = new SimpleType(Object.class, null, null, new JavaType[] {List.class});
        SimpleType subinterfaceType = new SimpleType(Object.class, null, null, new JavaType[] {List.class, Collection.class});
        SimpleType narrowedInterfaceType = interfaceType.narrow(Collection.class);
        assertEquals(subinterfaceType, narrowedInterfaceType);
    }

    @Test
    public void testTypeMethods() {
        // Test getRawClass, getSuperClass, getSuperInterfaces, isContainerType
        SimpleType intType = new SimpleType(Integer.TYPE);
        assertEquals(Integer.TYPE, intType.getRawClass());
        assertNull(intType.getSuperClass());
        assertNull(intType.getSuperInterfaces());
        assertFalse(intType.isContainerType());

        SimpleType listIntType = new SimpleType(Integer.TYPE, null, null, new JavaType[] {JavaType.getCollectionType(List.class, Integer.TYPE)});
        assertEquals(Integer.TYPE, listIntType.getRawClass());
        assertEquals(JavaType.getCollectionType(List.class, Integer.TYPE), listIntType.getSuperClass());
        assertArrayEquals(new JavaType[] {JavaType.getCollectionType(List.class, Integer.TYPE)}, listIntType.getSuperInterfaces());
        assertTrue(listIntType.isContainerType());

        // Test toString
        SimpleType stringType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", stringType.toString());

        // Test equals
        SimpleType equalType = new SimpleType(String.class);
        SimpleType notEqualType = new SimpleType(Integer.TYPE);
        assertTrue(stringType.equals(equalType));
        assertFalse(stringType.equals(notEqualType));

        // Test hashCode
        SimpleType stringType1 = new SimpleType(String.class);
        SimpleType stringType2 = new SimpleType(String.class);
        int hashCode1 = stringType1.hashCode();
        int hashCode2 = stringType2.hashCode();
        assertTrue(hashCode1 == hashCode2);
    }

    @Test
    public void testTypeMethodsWithBindings() {
        // Test getRawClass, getSuperClass, getSuperInterfaces, isContainerType with bindings
        SimpleType intType = new SimpleType(Integer.TYPE, new TypeBindings(), null, null);
        assertEquals(Integer.TYPE, intType.getRawClass());
        assertNull(intType.getSuperClass());
        assertNull(intType.getSuperInterfaces());
        assertFalse(intType.isContainerType());

        SimpleType listIntType = new SimpleType(Integer.TYPE, new TypeBindings(), null, new JavaType[] {JavaType.getCollectionType(List.class, Integer.TYPE)});
        assertEquals(Integer.TYPE, listIntType.getRawClass());
        assertEquals(JavaType.getCollectionType(List.class, Integer.TYPE), listIntType.getSuperClass());
        assertArrayEquals(new JavaType[] {JavaType.getCollectionType(List.class, Integer.TYPE)}, listIntType.getSuperInterfaces());
        assertTrue(listIntType.isContainerType());

        SimpleType listIntTypeWithBinding = new SimpleType(Integer.TYPE, new TypeBindings().putBinding("newName", String.class), null, new JavaType[] {JavaType.getCollectionType(List.class, Integer.TYPE)});
        assertEquals("newName", listIntTypeWithBinding.getBinding("newName").getType().getName());

        // Test toString with bindings
        SimpleType stringTypeWithBinding = new SimpleType(String.class, new TypeBindings().putBinding("newName", String.class));
        assertEquals("[simple type, class java.lang.String, binding(newName=class java.lang.String)]", stringTypeWithBinding.toString());

        // Test equals with bindings
        SimpleType equalTypeWithBinding = new SimpleType(String.class, new TypeBindings().putBinding("newName", String.class));
        SimpleType notEqualTypeWithBinding = new SimpleType(Integer.TYPE, new TypeBindings().putBinding("newName", String.class));
        assertTrue(stringTypeWithBinding.equals(equalTypeWithBinding));
        assertFalse(stringTypeWithBinding.equals(notEqualTypeWithBinding));
    }

    @Test
    public void testTypeMethodsWithValueHandlers() {
        // Test withValueHandler method
        SimpleType intType = new SimpleType(Integer.TYPE);
        SimpleType intTypeWithValueHandler = intType.withValueHandler(new MyIntValueHandler());
        assertNotEquals(intType, intTypeWithValueHandler);
        assertEquals(intType.getValueHandler(), null);
        assertEquals(intTypeWithValueHandler.getValueHandler(), new MyIntValueHandler());

        // Test withContentValueHandler method
        SimpleType listIntType = new SimpleType(Integer.TYPE, null, null, new JavaType[] {JavaType.getCollectionType(List.class, Integer.TYPE)});
        SimpleType listIntTypeWithContentValueHandler = listIntType.withContentValueHandler(new MyListIntContentValueHandler());
        assertNotEquals(listIntType, listIntTypeWithContentValueHandler);
        assertNull(listIntType.getValueHandler());
        assertNull(listIntType.getContentValueHandler());
        assertEquals(listIntTypeWithContentValueHandler.getContentValueHandler(), new MyListIntContentValueHandler());
    }

    @Test
    public void testTypeMethodsWithTypeHandlers() {
        // Test withTypeHandler method
        SimpleType intType = new SimpleType(Integer.TYPE);
        SimpleType intTypeWithTypeHandler = intType.withTypeHandler(new MyIntTypeHandler());
        assertNotEquals(intType, intTypeWithTypeHandler);
        assertEquals(intType.getTypeHandler(), null);
        assertEquals(intTypeWithTypeHandler.getTypeHandler(), new MyIntTypeHandler());

        // Test withContentTypeHandler method
        SimpleType listIntType = new SimpleType(Integer.TYPE, null, null, new JavaType[] {JavaType.getCollectionType(List.class, Integer.TYPE)});
        SimpleType listIntTypeWithContentTypeHandler = listIntType.withContentTypeHandler(new MyListIntContentTypeHandler());
        assertNotEquals(listIntType, listIntTypeWithContentTypeHandler);
        assertNull(listIntType.getTypeHandler());
        assertNull(listIntType.getContentTypeHandler());
        assertEquals(listIntTypeWithContentTypeHandler.getContentTypeHandler(), new MyListIntContentTypeHandler());
    }

    @Test
    public void testTypeMethodsWithStaticTyping() {
        // Test withStaticTyping method
        SimpleType intType = new SimpleType(Integer.TYPE);
        SimpleType intTypeWithStaticTyping = intType.withStaticTyping();
        assertNotEquals(intType, intTypeWithStaticTyping);
        assertFalse(intTypeWithStaticTyping.isStaticType());
        assertTrue(intTypeWithStaticTyping.withStaticTyping().isStaticType());
    }

    @Test
    public void testRefineMethod() {
        // Test refine method
        SimpleType intType = new SimpleType(Integer.TYPE);
        SimpleType intSubclassType = new SimpleType(Integer.class);
        SimpleType refinedType = intType.refine(Integer.class, new TypeBindings(), intSubclassType, null);
        assertEquals(intSubclassType, refinedType);

        SimpleType listIntType = new SimpleType(Integer.TYPE, null, null, new JavaType[] {JavaType.getCollectionType(List.class, Integer.TYPE)});
        SimpleType listIntSubinterfaceType = new SimpleType(Object.class, null, null, new JavaType[] {JavaType.getCollectionType(List.class, Integer.TYPE), Collection.class});
        SimpleType refinedListType = listIntType.refine(Collection.class, new TypeBindings(), listIntSubinterfaceType, null);
        assertEquals(listIntSubinterfaceType, refinedListType);
    }
}