package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdValueDeserializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() throws Exception {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        SimpleType simpleIntType = (SimpleType) intType;

        assertEquals(IntType.class, simpleIntType.getRawClass());
        assertEquals(int.class, simpleIntType.getRawClass());
        assertNotNull(simpleIntType.getRawClass().isInstance(new Integer(1)));
        assertEquals(Integer.TYPE, simpleIntType.getRawClass().getComponentType());
        assertEquals(Integer.class, simpleIntType.getRawClass().getGenericSuperclass());
        assertNull(simpleIntType.getSuperClass());
        assertNull(simpleIntType.getSuperInterfaces());

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        SimpleType simpleStringType = (SimpleType) stringType;

        assertEquals(String.class, simpleStringType.getRawClass());
        assertEquals(String.class, simpleStringType.getRawClass());
        assertNotNull(simpleStringType.getRawClass().isInstance("hello"));
        assertNull(simpleStringType.getSuperClass());
        assertNull(simpleStringType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeCopyConstructor() throws Exception {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        SimpleType simpleIntType = (SimpleType) intType;

        JavaType intType2 = TypeFactory.defaultInstance().constructType(int.class);
        SimpleType simpleIntType2 = new SimpleType(simpleIntType);

        assertEquals(simpleIntType, simpleIntType2);
        assertEquals(int.class, simpleIntType2.getRawClass());
        assertNotNull(simpleIntType2.getRawClass().isInstance(new Integer(1)));
        assertEquals(Integer.TYPE, simpleIntType2.getRawClass().getComponentType());
        assertEquals(Integer.class, simpleIntType2.getRawClass().getGenericSuperclass());
        assertNull(simpleIntType2.getSuperClass());
        assertNull(simpleIntType2.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeWithTypeBindings() throws Exception {
        JavaType listIntType = TypeFactory.defaultInstance()
                .constructParametricType(List.class, new Type[]{int.class});
        SimpleType simpleListIntType = (SimpleType) listIntType;

        assertEquals(List.class, simpleListIntType.getRawClass());
        assertEquals(List.class, simpleListIntType.getRawClass());
        assertNotNull(simpleListIntType.getRawClass().isInstance(new ArrayList<>()));
        assertEquals(List.class, simpleListIntType.getSuperClass());
        assertEquals(JavaType.class, simpleListIntType.getSuperClass().getComponentType());
        assertEquals(List.class, simpleListIntType.getRawClass().getGenericSuperclass());
        assertNull(simpleListIntType.getSuperInterfaces());

        JavaType mapIntIntType = TypeFactory.defaultInstance()
                .constructParametricType(Map.class, new Type[]{Integer.class, Integer.class});
        SimpleType simpleMapIntIntType = (SimpleType) mapIntIntType;

        assertEquals(Map.class, simpleMapIntIntType.getRawClass());
        assertEquals(Map.class, simpleMapIntIntType.getRawClass());
        assertNotNull(simpleMapIntIntType.getRawClass().isInstance(new HashMap<>()));
        assertEquals(Map.Entry.class, simpleMapIntIntType.getRawClass().getComponentType());
        assertEquals(Map.class, simpleMapIntIntType.getSuperClass());
        assertNull(simpleMapIntIntType.getSuperInterfaces());

        JavaType mapStringIntType = TypeFactory.defaultInstance()
                .constructParametricType(Map.class, new Type[]{String.class, Integer.class});
        SimpleType simpleMapStringIntType = (SimpleType) mapStringIntType;

        assertEquals(Map.class, simpleMapStringIntType.getRawClass());
        assertEquals(Map.class, simpleMapStringIntType.getRawClass());
        assertNotNull(simpleMapStringIntType.getRawClass().isInstance(new HashMap<>()));
        assertEquals(Map.Entry.class, simpleMapStringIntType.getRawClass().getComponentType());
        assertEquals(Map.class, simpleMapStringIntType.getSuperClass());
        assertNull(simpleMapStringIntType.getSuperInterfaces());

        JavaType mapIntStringType = TypeFactory.defaultInstance()
                .constructParametricType(Map.class, new Type[]{Integer.class, String.class});
        SimpleType simpleMapIntStringType = (SimpleType) mapIntStringType;

        assertEquals(Map.class, simpleMapIntStringType.getRawClass());
        assertEquals(Map.class, simpleMapIntStringType.getRawClass());
        assertNotNull(simpleMapIntStringType.getRawClass().isInstance(new HashMap<>()));
        assertEquals(Map.Entry.class, simpleMapIntStringType.getRawClass().getComponentType());
        assertNull(simpleMapIntStringType.getSuperClass());
        assertNull(simpleMapIntStringType.getSuperInterfaces());

        JavaType listSimpleType = TypeFactory.defaultInstance()
                .constructParametricType(List.class, new Type[]{SimpleType.class});
        SimpleType simpleListSimpleType = (SimpleType) listSimpleType;

        assertEquals(List.class, simpleListSimpleType.getRawClass());
        assertEquals(List.class, simpleListSimpleType.getRawClass());
        assertNotNull(simpleListSimpleType.getRawClass().isInstance(new ArrayList<>()));
        assertEquals(SimpleType.class, simpleListSimpleType.getRawClass().getComponentType());
        assertEquals(List.class, simpleListSimpleType.getSuperClass());
        assertNull(simpleListSimpleType.getSuperInterfaces());
    }

    @Test
    public void testSimpleTypeWithCustomTypeDeserializers() throws Exception {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        SimpleType simpleIntType = (SimpleType) intType;

        JavaType intTypeWithCustomDeserializer = TypeFactory.defaultInstance()
                .constructType(int.class, new StdValueDeserializer(StdValueDeserializer.NullValue.nullValue()));
        SimpleType simpleIntTypeWithCustomDeserializer = (SimpleType) intTypeWithCustomDeserializer;

        assertEquals(int.class, simpleIntTypeWithCustomDeserializer.getRawClass());
        assertEquals(int.class, simpleIntTypeWithCustomDeserializer.getRawClass());
        assertNotNull(simpleIntTypeWithCustomDeserializer.getRawClass().isInstance(new Integer(1)));
        assertEquals(Integer.TYPE, simpleIntTypeWithCustomDeserializer.getRawClass().getComponentType());
        assertEquals(Integer.class, simpleIntTypeWithCustomDeserializer.getRawClass().getGenericSuperclass());
        assertNull(simpleIntTypeWithCustomDeserializer.getSuperClass());
        assertNull(simpleIntTypeWithCustomDeserializer.getSuperInterfaces());

        JavaType intTypeWithCustomDeserializerAndValueHandler = TypeFactory.defaultInstance()
                .constructType(int.class, new StdValueDeserializer(StdValueDeserializer.NullValue.nullValue()), new StdValueDeserializer(StdValueDeserializer.NullValue.nullValue()));
        SimpleType simpleIntTypeWithCustomDeserializerAndValueHandler = (SimpleType) intTypeWithCustomDeserializerAndValueHandler;

        assertEquals(int.class, simpleIntTypeWithCustomDeserializerAndValueHandler.getRawClass());
        assertEquals(int.class, simpleIntTypeWithCustomDeserializerAndValueHandler.getRawClass());
        assertNotNull(simpleIntTypeWithCustomDeserializerAndValueHandler.getRawClass().isInstance(new Integer(1)));
        assertEquals(Integer.TYPE, simpleIntTypeWithCustomDeserializerAndValueHandler.getRawClass().getComponentType());
        assertEquals(Integer.class, simpleIntTypeWithCustomDeserializerAndValueHandler.getRawClass().getGenericSuperclass());
        assertNull(simpleIntTypeWithCustomDeserializerAndValueHandler.getSuperClass());
        assertNull(simpleIntTypeWithCustomDeserializerAndValueHandler.getSuperInterfaces());
    }
}