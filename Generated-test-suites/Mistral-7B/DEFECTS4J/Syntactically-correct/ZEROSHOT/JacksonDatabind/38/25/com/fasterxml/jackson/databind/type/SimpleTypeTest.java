package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType simpleTypeWithBindings = new SimpleType(cls, TypeBindings.emptyBindings());
        SimpleType simpleTypeWithSuperclass = new SimpleType(cls, TypeBindings.emptyBindings(), Arrays.asList(Integer.class).toJavaType(typeFactory), null);
        SimpleType simpleTypeWithValueHandler = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, new StdScalarValueHandler(), null, false);
        SimpleType simpleTypeWithTypeHandler = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, new StdTypeResolver(), false);

        SimpleType copiedSimpleType = new SimpleType(simpleType);
        SimpleType simpleTypeWithExtraHash = new SimpleType(cls, TypeBindings.emptyBindings(), Arrays.asList(Integer.class).toJavaType(typeFactory), 1, null, null, false);

        assertEquals(cls, simpleType.getRawClass());
        assertEquals(SimpleType.class, simpleType.getClass());
        assertNotNull(simpleType.getName());
        assertFalse(simpleType.isContainerType());

        assertEquals(simpleType, copiedSimpleType);
        assertNotSame(simpleType, simpleTypeWithBindings);
        assertNotSame(simpleType, simpleTypeWithSuperclass);
        assertNotSame(simpleType, simpleTypeWithValueHandler);
        assertNotSame(simpleType, simpleTypeWithTypeHandler);
        assertNotSame(simpleType, simpleTypeWithExtraHash);
    }

    @Test
    public void testUnsafeConstruction() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstructMethod() {
        SimpleType simpleType = SimpleType.construct(String.class);
    }

    @Test
    public void testNarrowing() {
        Class<String> original = String.class;
        Class<StringBuilder> subClass = StringBuilder.class;

        SimpleType originalSimpleType = new SimpleType(original);
        SimpleType subClassSimpleType = new SimpleType(subClass);

        assertEquals(original, originalSimpleType.getRawClass());
        assertEquals(original, subClassSimpleType.getRawClass());

        SimpleType narrowedType = originalSimpleType.narrow(subClass);
        assertEquals(subClass, narrowedType.getRawClass());

        assertNotSame(originalSimpleType, narrowedType);
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType simpleTypeWithBindings = new SimpleType(stringClass, TypeBindings.emptyBindings());
        SimpleType simpleTypeWithSuperclass = new SimpleType(stringClass, TypeBindings.emptyBindings(), Arrays.asList(Integer.class).toJavaType(typeFactory), null);
        SimpleType simpleTypeWithValueHandler = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, new StdScalarValueHandler(), null, false);
        SimpleType simpleTypeWithTypeHandler = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, new StdTypeResolver(), false);

        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
        assertEquals("[simple type, class java.lang.String]", simpleTypeWithBindings.toString());
        assertEquals("[simple type, class java.lang.String]", simpleTypeWithSuperclass.toString());
        assertEquals("[simple type, class java.lang.String]", simpleTypeWithValueHandler.toString());
        assertEquals("[simple type, class java.lang.String]", simpleTypeWithTypeHandler.toString());
    }

    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType simpleTypeWithBindings = new SimpleType(stringClass, TypeBindings.emptyBindings());
        SimpleType simpleTypeWithSuperclass = new SimpleType(stringClass, TypeBindings.emptyBindings(), Arrays.asList(Integer.class).toJavaType(typeFactory), null);
        SimpleType simpleTypeWithValueHandler = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, new StdScalarValueHandler(), null, false);
        SimpleType simpleTypeWithTypeHandler = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, new StdTypeResolver(), false);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        SimpleType simpleTypeWithExtraHash = new SimpleType(stringClass, TypeBindings.emptyBindings(), Arrays.asList(Integer.class).toJavaType(typeFactory), 1, null, null, false);

        assertTrue(simpleType.equals(simpleType));
        assertTrue(simpleType.equals(copiedSimpleType));
        assertFalse(simpleType.equals(simpleTypeWithBindings));
        assertFalse(simpleType.equals(simpleTypeWithSuperclass));
        assertFalse(simpleType.equals(simpleTypeWithValueHandler));
        assertFalse(simpleType.equals(simpleTypeWithTypeHandler));
        assertFalse(simpleType.equals(simpleTypeWithExtraHash));
    }

    // Add other test methods as needed for thorough testing

    private JavaType.TypeFactory typeFactory = new JavaType.DefaultTypeFactory();
}