package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JavaTypeTest {

    @Test
    public void testJavaTypeCreation() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType intType = typeFactory.constructType(int.class);
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType listType = typeFactory.constructCollectionType(List.class, Integer.class);
        JavaType mapType = typeFactory.constructMapType(Map.class, String.class, Integer.class);

        assertNotNull(intType);
        assertNotNull(stringType);
        assertNotNull(listType);
        assertNotNull(mapType);

        assertEquals(Integer.TYPE, intType.getRawClass());
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(List.class, listType.getRawClass());
        assertEquals(Map.class, mapType.getRawClass());

        assertTrue(intType.isPrimitive());
        assertFalse(stringType.isPrimitive());
        assertTrue(listType.isContainerType());
        assertTrue(mapType.isContainerType());
        assertFalse(intType.isCollectionLikeType());
        assertFalse(intType.isMapLikeType());
        assertFalse(intType.isInterface());
        assertFalse(intType.isEnumType());
    }

    @Test
    public void testJavaTypeNarrowing() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType intType = typeFactory.constructType(int.class);
        JavaType integerType = intType.narrowBy(Integer.class);

        assertNotNull(integerType);
        assertEquals(Integer.TYPE, integerType.getRawClass());
        assertTrue(integerType.isPrimitive());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingInvalid() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType intType = typeFactory.constructType(int.class);
        intType.narrowBy(String.class);
    }

    @Test
    public void testJavaTypeWidening() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType intType = typeFactory.constructType(int.class);
        JavaType numberType = intType.widenBy(Number.class);

        assertNotNull(numberType);
        assertTrue(numberType.isAssignableFrom(intType));
        assertFalse(intType.isAssignableFrom(numberType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWideningInvalid() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType intType = typeFactory.constructType(int.class);
        intType.widenBy(String.class);
    }

    @Test
    public void testJavaTypeWithHandlers() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType intType = mapper.getTypeFactory().constructType(int.class);
        intType = intType.withValueHandler(new IntValueHandler());
        intType = intType.withTypeHandler(new IntTypeHandler());

        JavaType widenedType = intType.widenBy(Number.class);
        assertEquals(Number.class, widenedType.getRawClass());

        TypeReference<List<Integer>> intListTypeRef = new TypeReference<List<Integer>>() {};
        JavaType intListType = mapper.getTypeFactory().constructType(intListTypeRef);
        JavaType intListTypeWithHandlers = intListType.withValueHandler(new IntListValueHandler()).withTypeHandler(new IntListTypeHandler());

        assertEquals(List.class, intListTypeWithHandlers.getRawClass());
        assertNotNull(intListTypeWithHandlers.getValueHandler());
        assertNotNull(intListTypeWithHandlers.getTypeHandler());
    }

    @Test
    public void testJavaTypeIsEmpty() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType emptyType = typeFactory.constructType(Void.TYPE);

        assertTrue(emptyType.isPrimitive());
        assertFalse(emptyType.isContainerType());
        assertFalse(emptyType.isCollectionLikeType());
        assertFalse(emptyType.isMapLikeType());
        assertFalse(emptyType.isInterface());
        assertFalse(emptyType.isEnumType());
    }

    @Test
    public void testJavaTypeIsArray() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType intArrayType = typeFactory.constructType(int[].class);

        assertTrue(intArrayType.isPrimitive());
        assertTrue(intArrayType.isArrayType());
        assertTrue(intArrayType.isContainerType());
        assertFalse(intArrayType.isCollectionLikeType());
        assertFalse(intArrayType.isMapLikeType());
    }

    private static class IntValueHandler implements ValueHandler<Number> {
        @Override
        public void serialize(Serializer serializers, Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber((Number) value);
        }

        @Override
        public Object deserialize(DeserializationContext ctxt, JsonParser p, DeserializerProvider prov) throws IOException {
            return p.getValueAsNumber().intValue();
        }
    }

    private static class IntTypeHandler implements TypeHandler<Number> {
        @Override
        public void serialize(Serializer serializers, Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString("IntTypeHandler");
        }

        @Override
        public Number deserialize(DeserializationContext ctxt, JsonParser p, DeserializerProvider prov) throws IOException {
            return null;
        }
    }

    private static class IntListValueHandler implements ValueHandler<List<Number>> {
        @Override
        public void serialize(Serializer serializers, Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            Serializer serializer = serializers.findValueSerializer(Number.class, null);
            for (Number number : (List<Number>) value) {
                serializer.serialize(number, gen, provider);
                gen.writeComma();
            }
        }

        @Override
        public Object deserialize(DeserializationContext ctxt, JsonParser p, DeserializerProvider prov) throws IOException {
            TypeDeserializer deserializer = prov.findValueDeserializer(List.class, JsonToken.START_ARRAY, null);
            ArrayList<Number> list = deserializer.deserialize(ctxt, p, prov);
            return list;
        }
    }

    private static class IntListTypeHandler implements TypeHandler<List<Number>> {
        @Override
        public void serialize(Serializer serializers, Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartArray();
            for (Number number : (List<Number>) value) {
                Serializer serializer = serializers.findValueSerializer(Number.class, null);
                serializer.serialize(number, gen, provider);
                gen.writeComma();
            }
            gen.writeEndArray();
        }

        @Override
        public List<Number> deserialize(DeserializationContext ctxt, JsonParser p, DeserializerProvider prov) throws IOException {
            TypeDeserializer deserializer = prov.findValueDeserializer(List.class, JsonToken.START_ARRAY, null);
            return deserializer.deserialize(ctxt, p, prov);
        }
    }
}