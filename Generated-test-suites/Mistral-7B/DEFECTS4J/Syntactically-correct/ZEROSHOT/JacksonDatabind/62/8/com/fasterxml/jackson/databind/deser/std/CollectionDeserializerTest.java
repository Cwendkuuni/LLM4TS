package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializerTests;
import org.junit.Test;

/**
 * Test class for CollectionDeserializer.
 */
public class CollectionDeserializerTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = StdSerializerTests.createArraySerializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");
        Collection<Object> collection = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Collections.emptyList(), collection);
    }

    @Test
    public void testCollectionDeserializationWithSingleElement() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = StdSerializerTests.createArraySerializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"" + "test" + "\"]");
        Collection<Object> collection = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("test", collection.get(0));
    }

    @Test
    public void testCollectionDeserializationWithMultipleElements() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = StdSerializerTests.createArraySerializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"" + "test1" + "\",\"" + "test2" + "\"]");
        Collection<Object> collection = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("test1", collection.get(0));
        assertEquals("test2", collection.get(1));
    }

    @Test
    public void testCollectionDeserializationWithNullValue() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = StdSerializerTests.createArraySerializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[\"" + null + "\"]");
        Collection<Object> collection = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertNull(collection.get(0));
    }

    @Test
    public void testCollectionDeserializationWithCustomTypeDeserializer() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, CustomType.class);
        JsonDeserializer<Object> valueDeserializer = StdSerializerTests.createArraySerializer(CustomType.class);
        TypeDeserializer valueTypeDeserializer = new CustomTypeDeserializer();
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = objectMapper.getFactory().createParser("[" + CustomTypeTest.CUSTOM_TYPE_STRING + "]");
        Collection<Object> collection = collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(CustomTypeTest.CUSTOM_TYPE_VALUE, collection.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializationWithInvalidJson() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = StdSerializerTests.createArraySerializer(String.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"test\": \"value\"}");
        collectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
    }

    private static class CustomType {
        public static final String CUSTOM_TYPE_STRING = "CustomType";
        private final int id;

        public CustomType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return Integer.toString(getId());
        }
    }

    private static class CustomTypeDeserializer extends JsonDeserializer<CustomType> {
        @Override
        public CustomType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            int id = jsonParser.getIntValue();
            return new CustomType(id);
        }
    }

    private static class CustomTypeTest {
        public static final String CUSTOM_TYPE_STRING = "CustomType";
        public static final int CUSTOM_TYPE_VALUE = 1;
    }
}