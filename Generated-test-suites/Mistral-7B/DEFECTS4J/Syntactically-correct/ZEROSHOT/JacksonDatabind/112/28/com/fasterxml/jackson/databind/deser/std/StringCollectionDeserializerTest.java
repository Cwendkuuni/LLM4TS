package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider.Std;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class StringCollectionDeserializerTest {

    @InjectMocks
    private StringCollectionDeserializer stringCollectionDeserializer;

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_AS_ARRAY_OBJECT, true);
    }

    @Test
    public void testDeserializeEmptyStringCollection() throws IOException, JsonMappingException {
        String json = "[]";
        Collection<String> expectedResult = new ArrayList<>();

        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeSingleString() throws IOException, JsonMappingException {
        String json = "[\"test\"]";
        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("test");

        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeMultipleStrings() throws IOException, JsonMappingException {
        String json = "[\"test1\", \"test2\", \"test3\"]";
        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("test1");
        expectedResult.add("test2");
        expectedResult.add("test3");

        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException, JsonMappingException {
        String json = "[{\"name\": \"test1\"}, {\"name\": \"test2\"}]";

        @JsonDeserialize(using = MyCustomStringDeserializer.class)
        static class MyString {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        @JsonFormat(serializableAs = MyString[].class)
        static class MyCollection {
            private MyString[] value;

            public MyString[] getValue() {
                return value;
            }

            public void setValue(MyString[] value) {
                this.value = value;
            }
        }

        MyCollection expectedResult = new MyCollection();
        expectedResult.setValue(new MyString[2]);
        expectedResult.getValue()[0].setName("test1");
        expectedResult.getValue()[1].setName("test2");

        Collection<MyCollection> result = mapper.readValue(json, StringCollectionDeserializer.class);

        assertEquals(expectedResult, result.iterator().next());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnexpectedToken() throws IOException {
        String json = "{\"name\": \"test\"}";

        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class);
    }

    @Test
    public void testDeserializeWithNullValues() throws IOException, JsonMappingException {
        String json = "[null, \"test2\", null]";
        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add(null);
        expectedResult.add("test2");
        expectedResult.add(null);

        Collection<String> result = mapper.readValue(json, StringCollectionDeserializer.class, new Std());

        assertEquals(expectedResult, result);
    }
}

class MyCustomStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}