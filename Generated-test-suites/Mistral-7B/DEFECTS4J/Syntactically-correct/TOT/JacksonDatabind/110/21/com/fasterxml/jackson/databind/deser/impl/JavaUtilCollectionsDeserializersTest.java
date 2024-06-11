package com.fasterxml.jackson.databind.deser.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollectionSingletonSet() throws Exception {
        String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":1,\"concreteType\":{\"rawClass\":\"java.util.Set\",\"elementType\":{\"rawClass\":\"java.lang.Boolean\"}}}";
        Set<Boolean> expected = Collections.singleton(Boolean.TRUE);

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {}));

        Object result = deserializer.deserialize(mapper.readTree(json), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testFindForCollectionSingletonList() throws Exception {
        String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":2,\"concreteType\":{\"rawClass\":\"java.util.List\",\"elementType\":{\"rawClass\":\"java.lang.Boolean\"}}}";
        List<Boolean> expected = Arrays.asList(Boolean.TRUE);

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>() {}));

        Object result = deserializer.deserialize(mapper.readTree(json), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testFindForCollectionSingletonMap() throws Exception {
        String json = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":3,\"concreteType\":{\"rawClass\":\"java.util.Map\",\"keyType\":{\"rawClass\":\"java.lang.String\"},\"valueType\":{\"rawClass\":\"java.lang.String\"}}}";
        Map<String, String> expected = Collections.singletonMap("a", "b");

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {}));

        Object result = deserializer.deserialize(mapper.readTree(json), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testFindForCollectionUnmodifiableSet() throws Exception {
        String json = "[true]";
        Set<Boolean> expected = Collections.unmodifiableSet(Arrays.asList(Boolean.TRUE));

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {}));

        Object result = deserializer.deserialize(mapper.readTree(json), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testFindForCollectionUnmodifiableList() throws Exception {
        String json = "[true]";
        List<Boolean> expected = Collections.unmodifiableList(Arrays.asList(Boolean.TRUE));

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<List<Boolean>>() {}));

        Object result = deserializer.deserialize(mapper.readTree(json), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testFindForCollectionUnmodifiableMap() throws Exception {
        String json = "{ \"a\": \"b\" }";
        Map<String, String> expected = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {}));

        Object result = deserializer.deserialize(mapper.readTree(json), mapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @Test
    public void testFindForCollectionEmpty() throws Exception {
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<List<String>>() {}));

        Object result = deserializer.deserialize(mapper.readTree("{}"), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testFindForMapEmpty() throws Exception {
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {}));

        Object result = deserializer.deserialize(mapper.readTree("{}"), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testFindForCollectionNull() throws Exception {
        String json = "null";
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Set<String>>() {}));

        Object result = deserializer.deserialize(mapper.readTree(json), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testFindForMapNull() throws Exception {
        String json = "null";
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {}));

        Object result = deserializer.deserialize(mapper.readTree(json), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollectionWrongType() throws Exception {
        String json = "[1,2,3]";
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {}));

        deserializer.deserialize(mapper.readTree(json), mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForMapWrongType() throws Exception {
        String json = "[1,2,3]";
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Map<String, String>>() {}));

        deserializer.deserialize(mapper.readTree(json), mapper.getDeserializationContext());
    }

    @Test
    public void testGetInputType() throws Exception {
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {}));

        JavaType inputType = deserializer.getInputType(mapper.getTypeFactory());
        assertEquals(mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {}), inputType);
    }

    @Test
    public void testGetOutputType() throws Exception {
        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(new TypeReference<Set<Boolean>>() {}));

        JavaType outputType = deserializer.getOutputType(mapper.getTypeFactory());
        assertEquals(mapper.getTypeFactory().constructType(new TypeReference<Object>() {}), outputType);
    }

    @Test
    public void testSerialization() throws Exception {
        Set<Boolean> set = Collections.singleton(Boolean.TRUE);
        String json = mapper.writeValueAsString(set);

        Object result = mapper.readValue(json, Set.class);
        assertEquals(set, result);
    }

    @Test
    public void testSerializationMap() throws Exception {
        Map<String, String> map = Collections.singletonMap("a", "b");
        String json = JsonMapper.builder()
                .findAndRegisterModules()
                .disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_IGNORABLE_VALUES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_OR_NULL_BEANS)
                .build()
                .writeValueAsString(map);

        Object result = JsonMapper.builder()
                .findAndRegisterModules()
                .disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_IGNORABLE_VALUES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_OR_NULL_BEANS)
                .build()
                .readValue(json, Map.class);
        assertEquals(map, result);
    }

    @Test
    public void testSerializationAsList() throws Exception {
        List<Boolean> list = Arrays.asList(Boolean.TRUE);
        String json = mapper.writeValueAsString(list);

        Object result = mapper.readValue(json, List.class);
        assertEquals(list, result);
    }

    static class JavaUtilCollectionsConverterMock implements JavaUtilCollectionsConverter {

        @Override
        public Object convert(Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            throw new UnsupportedOperationException();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testThrowException() throws Exception {
        JsonDeserializer<Object> deserializer = new StdDelegatingDeserializer<>(new JavaUtilCollectionsConverterMock());

        deserializer.deserialize(mapper.readTree("{}"), mapper.getDeserializationContext());
    }

}