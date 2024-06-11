package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest
{
    private DeserializationContext context;
    private JsonParser parser;
    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception
    {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        context = Mockito.mock(DeserializationContext.class);
        parser = Mockito.mock(JsonParser.class);
    }

    @Test
    public void testDeserializeFile() throws IOException, JsonProcessingException, URISyntaxException
    {
        String json = "{\"@class\":\"File\",\"path\":\"test.txt\"}";
        File expected = new File("test.txt");
        T result = FromStringDeserializer.findDeserializer(File.class).deserialize(parser, context);
        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUrl() throws IOException, JsonProcessingException
    {
        String json = "{\"@class\":\"URL\",\"string\":\"http://example.com\"}";
        URL expected = new URL("http://example.com");
        T result = FromStringDeserializer.findDeserializer(URL.class).deserialize(parser, context);
        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUri() throws IOException, JsonProcessingException, URISyntaxException
    {
        String json = "{\"@class\":\"URI\",\"string\":\"http://example.com/path\"}";
        URI expected = new URI("http://example.com/path");
        T result = FromStringDeserializer.findDeserializer(URI.class).deserialize(parser, context);
        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    // Add test cases for other supported types here

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException, JsonProcessingException
    {
        String json = "{\"@class\":\"InvalidType\",\"string\":\"test\"}";
        T result = FromStringDeserializer.findDeserializer(String.class).deserialize(parser, context);
        assertThrows(JsonMappingException.class, () -> result);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidValue() throws IOException, JsonProcessingException
    {
        String json = "{\"@class\":\"File\",\"path\":\"\"}";
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        T result = deserializer.deserialize(parser, context);
        assertNull(result);
        // Call the custom method to test empty string handling
        result = deserializer._deserializeFromEmptyString();
        assertNull(result);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException, JsonProcessingException
    {
        Object jsonObject = new Object() {{
            put("@class", "java.net.URI");
            put("value", "http://example.com");
        }};
        URI expected = new URI("http://example.com");
        T result = FromStringDeserializer.findDeserializer(URI.class)._deserializeEmbedded(jsonObject, context);
        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedUnknownObject() throws IOException, JsonProcessingException
    {
        Object jsonObject = new Object() {{
            put("@class", "UnknownClass");
            put("value", "test");
        }};
        T result = FromStringDeserializer.findDeserializer(URI.class)._deserializeEmbedded(jsonObject, context);
        assertThrows(JsonMappingException.class, () -> result);
    }
}