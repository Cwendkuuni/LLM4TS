package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Tests for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testBasicTypeDeserialization() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
                .constructTypeDeserializer(String.class, null);

        // Given: A valid JSON string
        String jsonString = "\"Hello World\"";

        // When: Deserialize the JSON string using the TypeDeserializerBase
        String result = mapper.readValue(jsonString, String.class);

        // Then: The deserialized value should match the original JSON string
        Assert.assertEquals(jsonString, result);

        // Given: A JSON string with a type id
        String jsonStringWithTypeId = "{\"typeId\":\"java.lang.String\",\"value\":\"Hello World\"}";
        TypeIdResolver typeIdResolver = mapper.getPolymorphicTypeIdResolver();
        TypeDeserializerBase typeDeserializerWithTypeId = mapper.getTypeFactory()
                .constructTypeDeserializer(String.class, typeIdResolver);

        // When: Deserialize the JSON string using the TypeDeserializerBase with type id
        String resultWithTypeId = mapper.readValue(jsonStringWithTypeId, String.class);

        // Then: The deserialized value should match the original JSON string
        Assert.assertEquals(jsonString, resultWithTypeId);

        // Given: A JSON string with an unknown type id
        String jsonStringWithUnknownTypeId = "{\"typeId\":\"unknownTypeId\",\"value\":\"Hello World\"}";

        // When: Attempt to deserialize the JSON string using the TypeDeserializerBase with unknown type id
        Assert.assertThrows(JsonMappingException.class, () -> mapper.readValue(jsonStringWithUnknownTypeId, String.class));
    }

    @Test
    public void testDefaultTypeDeserialization() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
                .constructTypeDeserializer(Void.class, null);

        // Given: A JSON string with a null value
        String jsonString = "null";

        // When: Deserialize the JSON string using the TypeDeserializerBase
        Object result = mapper.readValue(jsonString, Void.class);

        // Then: The deserialized value should be null
        Assert.assertNull(result);
    }

    @Test
    public void testTypeIdVisibleTrue() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
                .constructTypeDeserializer(String.class, null);
        TypeDeserializerBase typeDeserializerBaseWithTypeIdVisible = mapper.getTypeFactory()
                .constructTypeDeserializer(String.class, null, true);

        // Given: A JSON string with a type id and 'typeIdVisible' set to true
        String jsonString = "{\"typeId\":\"java.lang.String\",\"value\":\"Hello World\"}";

        // When: Deserialize the JSON string using the TypeDeserializerBase with type id and 'typeIdVisible' set to true
        String result = mapper.readValue(jsonString, String.class, typeDeserializerBaseWithTypeIdVisible);

        // Then: The deserialized value should match the original JSON string
        Assert.assertEquals(jsonString, result);

        // Given: A JSON string with a type id and 'typeIdVisible' set to false
        String jsonStringWithTypeIdHidden = "{\"value\":\"Hello World\"}";

        // When: Attempt to deserialize the JSON string using the TypeDeserializerBase with type id and 'typeIdVisible' set to false
        Assert.assertThrows(JsonMappingException.class, () -> mapper.readValue(jsonStringWithTypeIdHidden, String.class, typeDeserializerBaseWithTypeIdVisible));
    }

    @Test
    public void testDeserializerMapping() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<MyCustomClass> myCustomClassDeserializer = new MyCustomClassDeserializer();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory()
                .constructTypeDeserializer(MyCustomClass.class, null);

        // Given: A JSON string with a custom type id
        String jsonString = "{\"typeId\":\"com.mypackage.MyCustomClass\",\"value\":{\"field1\":1,\"field2\":\"Test\"}}";
        TypeIdResolver typeIdResolver = mapper.getPolymorphicTypeIdResolver();
        typeDeserializerBase.setDeserializers(typeIdResolver, myCustomClassDeserializer);

        // When: Deserialize the JSON string using the TypeDeserializerBase with custom deserializer
        MyCustomClass result = mapper.readValue(jsonString, MyCustomClass.class);

        // Then: The deserialized value should match the expected result
        MyCustomClass expectedResult = new MyCustomClass(1, "Test");
        Assert.assertEquals(expectedResult, result);

        // Given: A JSON string with an unknown type id
        String jsonStringWithUnknownTypeId = "{\"typeId\":\"unknownTypeId\",\"value\":{\"field1\":1,\"field2\":\"Test\"}}";

        // When: Attempt to deserialize the JSON string using the TypeDeserializerBase with unknown type id
        Assert.assertThrows(JsonMappingException.class, () -> mapper.readValue(jsonStringWithUnknownTypeId, MyCustomClass.class));
    }

    static class MyCustomClassDeserializer extends JsonDeserializer<MyCustomClass> {
        @Override
        public MyCustomClass deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            int field1 = node.get(0).asInt();
            String field2 = node.get(1).asText();
            return new MyCustomClass(field1, field2);
        }
    }

    static class MyCustomClass {
        private final int field1;
        private final String field2;

        public MyCustomClass(int field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public int getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }
    }
}