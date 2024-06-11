package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class StringCollectionDeserializerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        AnnotatedClass ac = new AnnotatedClass(StringCollectionDeserializer.class, null);
        JsonDeserializer<Collection<String>> deserializer = objectMapper.deserializerForClass(ac.getRawClass());

        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser("[]");

        Collection<String> result = deserializer.deserialize(jsonParser, null);
        Assert.assertEquals(Arrays.asList(), result);
    }

    @Test
    public void testStringCollectionDeserialization() throws IOException {
        AnnotatedClass ac = new AnnotatedClass(StringCollectionDeserializer.class, null);
        JsonDeserializer<Collection<String>> deserializer = objectMapper.deserializerForClass(ac.getRawClass());

        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser("[\"apple\", \"banana\", \"cherry\"]");

        Collection<String> result = deserializer.deserialize(jsonParser, null);
        Assert.assertEquals(Arrays.asList("apple", "banana", "cherry"), result);
    }

    @Test
    public void testNullValueDeserialization() throws IOException {
        StringCollectionDeserializer serializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, true);

        AnnotatedClass ac = new AnnotatedClass(serializer.getClass(), null);
        JsonDeserializer<Collection<String>> deserializer = objectMapper.deserializerForClass(ac.getRawClass());

        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser("null");

        Collection<String> result = deserializer.deserialize(jsonParser, null);
        Assert.assertNull(result);
    }

    @Test
    public void testNullValueProviderDeserialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper.SerializeConfig serializeConfig = new ObjectMapper.SerializeConfig();
        serializeConfig.setNullValueProperty("nullValue");

        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser("{\"nullValue\": null}");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, new NullValueProvider.Base() {
                    @Override
                    public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                        return "nullValueProvided";
                    }
                }, false);

        Collection<String> result = deserializer.deserialize(jsonParser, deserializer, null);
        Assert.assertEquals("nullValueProvided", result.iterator().next());
    }

    @Test
    public void testJsonFormatFeatureDeserialization() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser("[]");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, false);

        jsonParser.setCodecFeature(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        Collection<String> result = deserializer.deserialize(jsonParser, null);
        Assert.assertEquals(Arrays.asList(), result);

        jsonParser.setCodecFeature(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, false);

        jsonParser.setValueAsString("{\"formatFeature\": true}");
        deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, false);

        result = deserializer.deserialize(jsonParser, null);
        Assert.assertEquals(Arrays.asList("formatFeature"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationFailure() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, false);

        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser("{\"invalid\": 123}");

        Collection<String> result = deserializer.deserialize(jsonParser, null);
        // This test should fail with JsonMappingException
    }

    @Test
    public void testTypeDeserializerDeserialization() throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser("[\"apple\", \"banana\", \"cherry\", 1, 2.0]");

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, false);

        TypeDeserializer typeDeserializer = ObjectMapper.getDefaultInstance().getTypeDeserializers().findValueDeserializer(int.class, null);

        Collection<String> result = deserializer.deserializeWithType(jsonParser, null, typeDeserializer);
        Assert.assertEquals(Arrays.asList("apple", "banana", "cherry", "1", "2.0"), result);
    }
}