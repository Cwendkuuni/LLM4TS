package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"/path/to/file.txt\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(parser, ctxt);
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"http://www.example.com/path/to/resource\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(parser, ctxt);
        assertEquals("http://www.example.com/path/to/resource", result.toString());
    }

    @Test
    public void testDeserializeURI() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"uri://www.example.com/path/to/resource#fragment\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(parser, ctxt);
        assertEquals("uri://www.example.com/path/to/resource#fragment", result.toString());
    }

    @Test
    public void testDeserializeClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"java.util.Locale\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Class<?>> deserializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(parser, ctxt);
        assertEquals(Locale.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"com.example.MyList<java.lang.Integer>\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(parser, ctxt);
        assertEquals(JavaType.constructType(int[].class), result);
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"EUR\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(parser, ctxt);
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"/pattern/[a-zA-Z]+\\.txt$/\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(parser, ctxt);
        assertEquals(Pattern.compile("/pattern/[a-zA-Z]+\\.txt$/"), result);
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"en_US\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(parser, ctxt);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"UTF-8\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(parser, ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"America/New_York\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, ctxt);
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);
    }

    @Test
    public void testDeserializeInvalidType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"java.util.Date\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(parser, ctxt));
    }

    @Test
    public void testDeserializeEmptyString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "\"\"";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<String> deserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        String result = deserializer.deserialize(parser, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        String jsonString = "null";
        JsonParser parser = jsonFactory.createParser(jsonString);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<String> deserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        String result = deserializer.deserialize(parser, ctxt);
        assertNull(result);
    }
}