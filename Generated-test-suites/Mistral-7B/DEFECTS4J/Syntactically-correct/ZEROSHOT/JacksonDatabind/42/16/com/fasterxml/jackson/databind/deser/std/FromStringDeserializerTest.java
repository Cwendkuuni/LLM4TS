package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> T createTestInstance(Class<T> type) {
        return (T) new FromStringDeserializer<T>(type) {};
    }

    @Test
    public void testDeserializeFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"/path/to/file.txt\"");
        FromStringDeserializer<File> serializer = createTestInstance(File.class);
        File result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"http://example.com\"");
        FromStringDeserializer<URL> serializer = createTestInstance(URL.class);
        URL result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"http://example.com/path/to/file.txt\"");
        FromStringDeserializer<URI> serializer = createTestInstance(URI.class);
        URI result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertEquals("http://example.com/path/to/file.txt", result.toURL().toString());
    }

    @Test
    public void testDeserializeClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"java.lang.String\"");
        FromStringDeserializer<Class<?>> serializer = createTestInstance(Class.class);
        Class<?> result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"com.example.Person\"");
        FromStringDeserializer<JavaType> serializer = createTestInstance(JavaType.class);
        JavaType result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertEquals(JavaType.constructType(Person.class), result);
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"EUR\"");
        FromStringDeserializer<Currency> serializer = createTestInstance(Currency.class);
        Currency result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"/^(\\w+):(\\d{1,3})(\\.|:|\\/)([a-zA-Z]+|[0-9]+|[/?])+\"");
        FromStringDeserializer<Pattern> serializer = createTestInstance(Pattern.class);
        Pattern result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertTrue(result.matcher("example.com:80/path/to/file.txt").matches());
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer<Locale> serializer = createTestInstance(Locale.class);
        Locale result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer<Charset> serializer = createTestInstance(Charset.class);
        Charset result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"America/Los_Angeles\"");
        FromStringDeserializer<TimeZone> serializer = createTestInstance(TimeZone.class);
        TimeZone result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"127.0.0.1\"");
        FromStringDeserializer<InetAddress> serializer = createTestInstance(InetAddress.class);
        InetAddress result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertEquals("127.0.0.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("[\"127.0.0.1\", 80]");
        FromStringDeserializer<InetSocketAddress> serializer = createTestInstance(InetSocketAddress.class);
        InetSocketAddress result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        assertEquals("127.0.0.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"test\"");
        FromStringDeserializer<Object> serializer = createTestInstance(Object.class);
        serializer.deserialize(jp, new DeserializationContext(null, null, null));
        fail("Should throw JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<String> serializer = createTestInstance(String.class);
        String result = serializer.deserialize(jp, new DeserializationContext(null, null, null));
        fail("Should throw JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("\"invalid\"");
        FromStringDeserializer<Locale> serializer = createTestInstance(Locale.class);
        serializer.deserialize(jp, new DeserializationContext(null, null, null));
        fail("Should throw JsonMappingException");
    }
}