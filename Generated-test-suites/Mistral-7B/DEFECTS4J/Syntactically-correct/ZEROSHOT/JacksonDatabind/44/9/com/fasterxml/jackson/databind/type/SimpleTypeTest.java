package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test the public constructor
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringType.getBindings());
        assertEquals(null, stringType.getSuperclass());
        assertEquals(null, stringType.getSubTypes());
        assertEquals(false, stringType.isStatic());

        // Test the constructor with bindings
        TypeBindings bindings = new TypeBindings();
        bindings.putBindings("List", Arrays.asList(Integer.class, String.class));

        Class<List<Integer>> listIntClass = List.class.getGenericType();
        JavaType listIntType = TypeFactory.defaultInstance().constructType(listIntClass);
        SimpleType listIntTypeSimple = new SimpleType(listIntType, bindings);
        assertEquals(List.class, listIntTypeSimple.getRawClass());
        assertEquals(bindings, listIntTypeSimple.getBindings());
        assertEquals(JavaType.class, listIntTypeSimple.getSuperclass());
        assertEquals(Arrays.asList(Integer.class, String.class).toArray(new Class[0]), listIntTypeSimple.getSubTypes());
        assertEquals(false, listIntTypeSimple.isStatic());

        // Test the copy constructor
        SimpleType stringTypeCopy = new SimpleType(stringType);
        assertEquals(String.class, stringTypeCopy.getRawClass());
        assertEquals(stringType.getBindings(), stringTypeCopy.getBindings());
        assertEquals(stringType.getSuperclass(), stringTypeCopy.getSuperclass());
        assertEquals(stringType.getSubTypes(), stringTypeCopy.getSubTypes());
        assertEquals(stringType.isStatic(), stringTypeCopy.isStatic());

        // Test the copy constructor with bindings
        SimpleType listIntTypeSimpleCopy = new SimpleType(listIntTypeSimple);
        assertEquals(List.class, listIntTypeSimpleCopy.getRawClass());
        assertEquals(listIntTypeSimple.getBindings(), listIntTypeSimpleCopy.getBindings());
        assertEquals(JavaType.class, listIntTypeSimpleCopy.getSuperclass());
        assertEquals(Arrays.asList(Integer.class, String.class).toArray(new Class[0]), listIntTypeSimpleCopy.getSubTypes());
        assertEquals(false, listIntTypeSimpleCopy.isStatic());

        // Test the SimpleType constructor with valueHandler and typeHandler
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType valueHandlerType = new SimpleType(String.class, bindings, null, null, valueHandler, typeHandler, false);
        assertEquals(String.class, valueHandlerType.getRawClass());
        assertEquals(bindings, valueHandlerType.getBindings());
        assertEquals(null, valueHandlerType.getSuperclass());
        assertEquals(null, valueHandlerType.getSubTypes());
        assertEquals(false, valueHandlerType.isStatic());
        assertEquals(valueHandler, valueHandlerType.getValueHandler());
        assertEquals(typeHandler, valueHandlerType.getTypeHandler());

        // Test the SimpleType constructor with asStatic
        SimpleType stringTypeStatic = new SimpleType(String.class, bindings, null, null, null, null, true);
        assertEquals(String.class, stringTypeStatic.getRawClass());
        assertEquals(bindings, stringTypeStatic.getBindings());
        assertEquals(null, stringTypeStatic.getSuperclass());
        assertEquals(null, stringTypeStatic.getSubTypes());
        assertEquals(true, stringTypeStatic.isStatic());

        // Test the SimpleType constructor with extraHash
        SimpleType stringTypeExtraHash = new SimpleType(String.class, bindings, null, null, 0, valueHandler, typeHandler, false);
        SimpleType stringTypeExtraHash2 = new SimpleType(String.class, bindings, null, null, 1, valueHandler, typeHandler, false);
        assertNotEquals(stringTypeExtraHash, stringTypeExtraHash2);

        // Test the constructUnsafe method
        SimpleType unsafeType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, unsafeType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), unsafeType.getBindings());
        assertEquals(null, unsafeType.getSuperclass());
        assertEquals(null, unsafeType.getSubTypes());
        assertEquals(false, unsafeType.isStatic());

        // Test the construct method (deprecated)
        SimpleType constructType = SimpleType.construct(String.class);
        assertEquals(String.class, constructType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), constructType.getBindings());
        assertEquals(null, constructType.getSuperclass());
        assertEquals(null, constructType.getSubTypes());
        assertEquals(false, constructType.isStatic());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        // Test narrowing with same class
        SimpleType intType = new SimpleType(Integer.class);
        SimpleType intTypeNarrowed = intType.narrow(Integer.class);
        assertEquals(intType, intTypeNarrowed);

        // Test narrowing with subclass
        SimpleType stringType = new SimpleType(String.class);
        SimpleType intTypeNarrowedSubclass = stringType.narrow(Integer.class);
        assertEquals(intType, intTypeNarrowedSubclass);

        // Test narrowing with superclass
        SimpleType intTypeSuper = new SimpleType(Integer.class);
        SimpleType objectTypeNarrowedSuper = intTypeSuper.narrow(Object.class);
        assertNotEquals(intTypeSuper, objectTypeNarrowedSuper);
        assertEquals(Object.class, objectTypeNarrowedSuper.getRawClass());

        // Test narrowing with interface
        interface MyInterface {
        }

        SimpleType intTypeInterface = new SimpleType(Integer.class);
        SimpleType myInterfaceNarrowed = intTypeInterface.narrow(MyInterface.class);
        assertEquals(MyInterface.class, myInterfaceNarrowed.getRawClass());
    }

    @Test
    public void testSimpleTypeWithHandlers() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);

        Object valueHandler = new Object();
        SimpleType valueHandlerType = simpleType.withValueHandler(valueHandler);
        assertEquals(String.class, valueHandlerType.getRawClass());
        assertEquals(simpleType.getBindings(), valueHandlerType.getBindings());
        assertEquals(simpleType.getSuperclass(), valueHandlerType.getSuperclass());
        assertEquals(simpleType.getSubTypes(), valueHandlerType.getSubTypes());
        assertEquals(false, valueHandlerType.isStatic());
        assertEquals(valueHandler, valueHandlerType.getValueHandler());
        assertNull(valueHandlerType.getTypeHandler());

        Object typeHandler = new Object();
        SimpleType typeHandlerType = valueHandlerType.withTypeHandler(typeHandler);
        assertEquals(String.class, typeHandlerType.getRawClass());
        assertEquals(valueHandlerType.getBindings(), typeHandlerType.getBindings());
        assertEquals(valueHandlerType.getSuperclass(), typeHandlerType.getSuperclass());
        assertEquals(valueHandlerType.getSubTypes(), typeHandlerType.getSubTypes());
        assertEquals(false, typeHandlerType.isStatic());
        assertEquals(valueHandler, typeHandlerType.getValueHandler());
        assertEquals(typeHandler, typeHandlerType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType staticType = simpleType.withStaticTyping();
        assertEquals(String.class, staticType.getRawClass());
        assertEquals(simpleType.getBindings(), staticType.getBindings());
        assertEquals(simpleType.getSuperclass(), staticType.getSuperclass());
        assertEquals(simpleType.getSubTypes(), staticType.getSubTypes());
        assertEquals(true, staticType.isStatic());
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        Object contentTypeHandler = new Object();
        SimpleType contentTypeHandlerType = simpleType.withContentTypeHandler(contentTypeHandler);
        assertEquals(String.class, contentTypeHandlerType.getRawClass());
        assertEquals(simpleType.getBindings(), contentTypeHandlerType.getBindings());
        assertEquals(simpleType.getSuperclass(), contentTypeHandlerType.getSuperclass());
        assertEquals(simpleType.getSubTypes(), contentTypeHandlerType.getSubTypes());
        assertEquals(false, contentTypeHandlerType.isStatic());
        assertEquals(simpleType.getValueHandler(), contentTypeHandlerType.getValueHandler());
        assertEquals(contentTypeHandler, contentTypeHandlerType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        Object contentValueHandler = new Object();
        SimpleType contentValueHandlerType = simpleType.withContentValueHandler(contentValueHandler);
        assertEquals(String.class, contentValueHandlerType.getRawClass());
        assertEquals(simpleType.getBindings(), contentValueHandlerType.getBindings());
        assertEquals(simpleType.getSuperclass(), contentValueHandlerType.getSuperclass());
        assertEquals(simpleType.getSubTypes(), contentValueHandlerType.getSubTypes());
        assertEquals(false, contentValueHandlerType.isStatic());
        assertEquals(simpleType.getValueHandler(), contentValueHandlerType.getValueHandler());
        assertEquals(contentValueHandler, contentValueHandlerType.getTypeHandler());
        assertEquals(contentValueHandler, contentValueHandlerType.getContentValueHandler());
    }

    @Test
    public void testSimpleTypeRefine() {
        // Test refining with a simple type
        SimpleType intType = new SimpleType(Integer.class);
        SimpleType intTypeRefined = intType.refine(Integer.class, null, null, null);
        assertEquals(intType, intTypeRefined);

        // Test refining with a subclass
        SimpleType stringType = new SimpleType(String.class);
        SimpleType intTypeRefinedSubclass = stringType.refine(Integer.class, null, null, null);
        assertNotEquals(stringType, intTypeRefinedSubclass);
        assertEquals(Integer.class, intTypeRefinedSubclass.getRawClass());

        // Test refining with a superclass
        SimpleType integerType = new SimpleType(Integer.class);
        SimpleType objectTypeRefinedSuperclass = integerType.refine(Object.class, null, null, null);
        assertNotEquals(integerType, objectTypeRefinedSuperclass);
        assertEquals(Object.class, objectTypeRefinedSuperclass.getRawClass());
    }

    @Test
    public void testSimpleTypeCanonicalName() {
        SimpleType stringType = new SimpleType(String.class);
        assertEquals("java.lang.String", stringType.buildCanonicalName());

        TypeBindings bindings = new TypeBindings();
        bindings.putBindings("List", Arrays.asList(Integer.class, String.class));

        Class<List<Integer>> listIntClass = List.class.getGenericType();
        JavaType listIntType = TypeFactory.defaultInstance().constructType(listIntClass);
        SimpleType listIntTypeSimple = new SimpleType(listIntType, bindings);
        assertEquals("java.util.List<java.lang.Integer, java.lang.String>", listIntTypeSimple.buildCanonicalName());
    }
}