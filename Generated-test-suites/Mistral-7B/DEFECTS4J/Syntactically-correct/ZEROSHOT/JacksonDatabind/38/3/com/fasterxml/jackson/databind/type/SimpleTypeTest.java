package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType simpleTypeWithBindings = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleTypeWithSuperClass = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleTypeWithValues = new SimpleType(String.class, TypeBindings.emptyBindings(), String[].class, null, null, null, false);

        assertEquals(stringClass, simpleType.getRawClass());
        assertEquals(simpleType, simpleTypeWithBindings.withBindings(TypeBindings.emptyBindings()));
        assertEquals(simpleType, simpleTypeWithSuperClass);
        assertEquals(String[].class, simpleTypeWithValues.getRawClass().getComponentType());
    }

    @Test
    public void testSimpleTypeMethods() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);

        assertFalse(simpleType.isContainerType());
        assertEquals("[" + stringClass.getName() + ']', simpleType.getErasedSignature().toString());
        assertEquals("<" + stringClass.getName() + '>', simpleType.getGenericSignature().toString());
        assertEquals(simpleType, simpleType.withTypeHandler(null));
        assertEquals(simpleType, simpleType.withValueHandler(null));
        assertEquals(simpleType, simpleType.withStaticTyping());
        assertNull(simpleType.refine(String[].class, TypeBindings.emptyBindings(), null, null));
        assertEquals("[simple type, class " + stringClass.getName() + ']', simpleType.toString());
    }

    @Test
    public void testSimpleTypeEquality() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass);
        SimpleType simpleType2 = new SimpleType(stringClass);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType1, simpleType3);
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<List<String>> listClass = List.class.getTypeParameters()[0].getRawType();
        TypeBindings bindings = new TypeBindings();
        bindings.put(0, new SimpleType(String.class));
        SimpleType simpleType = new SimpleType(listClass, bindings, null, null);

        assertEquals(listClass.getGenericComponentType(), simpleType.getRawClass().getComponentType());
        assertEquals(1, simpleType.getGenericTypeParameters().length);
        assertEquals(SimpleType.class, simpleType.getGenericTypeParameters()[0].getClass());
        assertEquals(String.class, simpleType.getGenericTypeParameters()[0].getType().getRawClass());
        assertEquals("<String>", simpleType.getGenericSignature().toString());
    }
}