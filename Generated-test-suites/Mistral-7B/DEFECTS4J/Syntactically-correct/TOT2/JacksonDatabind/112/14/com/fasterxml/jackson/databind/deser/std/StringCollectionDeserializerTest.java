package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringCollectionDeserializerTest extends BaseTest {

    private static final String JSON_EMPTY_ARRAY = "[]";
    private static final String JSON_STRING_ARRAY = "[\"element1\", \"element2\", \"element3\"]";
    private static final String JSON_NULL_ARRAY = "[null]";

    private StringCollectionDeserializer stringCollectionDeserializer;
    private JsonFactory jsonFactory;

    @BeforeClass
    public void setUp() throws Exception {
        stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        jsonFactory = new JsonFactory();
    }

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        Collection<String> result = mapper.readValue(JSON_EMPTY_ARRAY, Collection.class, jsonFactory);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testStringArrayDeserialization() throws IOException {
        Collection<String> result = mapper.readValue(JSON_STRING_ARRAY, Collection.class, jsonFactory);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 3);
        Assert.assertEquals(result.toString(), "[element1, element2, element3]");
    }

    @Test
    public void testNullArrayDeserialization() throws IOException {
        Collection<String> result = mapper.readValue(JSON_NULL_ARRAY, Collection.class, jsonFactory);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.toString(), "[null]");
    }

    @Test
    public void testDeserializationWithCustomDeserializer() throws IOException {
        // Assuming we have a custom deserializer for String type
        JsonDeserializer<String> customStringDeserializer = createMockCustomStringDeserializer();
        StringCollectionDeserializer deserializerWithCustomDeserializer =
                new StringCollectionDeserializer(null, null, null, null, null, null) {
                    @Override
                    public JsonDeserializer<Object> createContextual(DeserializationContext ctxt,
                            BeanProperty property) throws JsonMappingException {
                        return customStringDeserializer;
                    }
                };

        Collection<String> result = mapper.readValue(JSON_STRING_ARRAY, Collection.class, jsonFactory, deserializerWithCustomDeserializer);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 3);
        Assert.assertEquals(result.toString(), "[element1, element2, element3]");
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testNonArrayDeserialization() throws IOException {
        String nonArrayJson = "{\"nonArray\":[\"element1\", \"element2\"]}";
        mapper.readValue(nonArrayJson, Collection.class, jsonFactory, stringCollectionDeserializer);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testNonStringElementDeserialization() throws IOException {
        String nonStringElementJson = "[1, 2, 3]";
        mapper.readValue(nonStringElementJson, Collection.class, jsonFactory, stringCollectionDeserializer);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testEmptyStringElementDeserialization() throws IOException {
        String emptyStringElementJson = "[\"\", \"element2\", \"element3\"]";
        mapper.readValue(emptyStringElementJson, Collection.class, jsonFactory, stringCollectionDeserializer);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testNullValueDeserialization() throws IOException {
        String nullValueJson = "[null, \"element2\", \"element3\"]";
        mapper.readValue(nullValueJson, Collection.class, jsonFactory, stringCollectionDeserializer);
    }

    @Test
    public void testDeserializationWithNullValuesSkipping() throws IOException {
        String jsonWithNullValues = "[\"element1\", null, \"element3\"]";
        StringCollectionDeserializer deserializerWithNullValuesSkipping =
                new StringCollectionDeserializer(null, null, null, null, null, true);
        Collection<String> result = mapper.readValue(jsonWithNullValues, Collection.class, jsonFactory, deserializerWithNullValuesSkipping);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 2);
        Assert.assertEquals(result.toString(), "[element1, element3]");
    }

    private JsonDeserializer<String> createMockCustomStringDeserializer() {
        // Implementation of the custom String deserializer
        // for testing purposes
        return new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "mock_deserialized_string";
            }
        };
    }
}