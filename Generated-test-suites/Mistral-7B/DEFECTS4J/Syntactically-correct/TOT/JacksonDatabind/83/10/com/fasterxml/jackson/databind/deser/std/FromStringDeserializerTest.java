package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.junit.BaseTest;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest extends BaseTest {

    @Test
    public void testURLDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(URL.class);

        String jsonString = "http://www.example.com";
        JsonParser p = jsonFactory.createParser(jsonString);
        Std.StdURL stdURL = Std.findDeserializer(URL.class);
        URL url = stdURL.deserialize(p, ctxt);
        assertEquals("http://www.example.com", url.toString());

        jsonString = "https://www.example.com:8080/path?query=value#fragment";
        p = jsonFactory.createParser(jsonString);
        url = stdURL.deserialize(p, ctxt);
        assertEquals("https://www.example.com:8080/path?query=value#fragment", url.toString());
    }

    @Test
    public void testURIDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(URI.class);

        String jsonString = "http://www.example.com";
        JsonParser p = jsonFactory.createParser(jsonString);
        Std.StdURI stdURI = Std.findDeserializer(URI.class);
        URI uri = stdURI.deserialize(p, ctxt);
        assertEquals("http", uri.getScheme());
        assertEquals("/www.example.com", uri.getPath());
        assertEquals(21, uri.getPort());

        jsonString = "https://user:pass@www.example.com:8080/path?query=value#fragment";
        p = jsonFactory.createParser(jsonString);
        uri = stdURI.deserialize(p, ctxt);
        assertEquals("https", uri.getScheme());
        assertEquals("/user:pass@www.example.com:8080/path?query=value#fragment", uri.toString());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(InetSocketAddress.class);

        String jsonString = "[255.255.255.255]:8080";
        JsonParser p = jsonFactory.createParser(jsonString);
        Std.StdInetSocketAddress stdInetSocketAddress = Std.findDeserializer(InetSocketAddress.class);
        InetSocketAddress address = stdInetSocketAddress.deserialize(p, ctxt);
        assertEquals("255.255.255.255", address.getAddress().getHostAddress());
        assertEquals(8080, address.getPort());

        jsonString = "[::1]:8080";
        p = jsonFactory.createParser(jsonString);
        address = stdInetSocketAddress.deserialize(p, ctxt);
        assertEquals("0:0:0:0:0:0:0:1", address.getAddress().getHostAddress());
        assertEquals(8080, address.getPort());
    }

    @Test
    public void testDeserializationFromEmptyString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(StringBuilder.class);

        String jsonString = "";
        JsonParser p = jsonFactory.createParser(jsonString);
        StdFromStringDeserializer<StringBuilder> deserializer = new StdFromStringDeserializer<StringBuilder>(StringBuilder.class);
        StringBuilder stringBuilder = deserializer.deserialize(p, ctxt);
        assertNull(stringBuilder);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationFromInvalidString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);

        String jsonString = "invalid";
        JsonParser p = jsonFactory.createParser(jsonString);
        StdFromStringDeserializer<Integer> deserializer = new StdFromStringDeserializer<Integer>(Integer.class);
        deserializer.deserialize(p, ctxt);
    }

    @Test
    public void testDeserializationFromNullString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = TypeFactory.defaultInstance().constructType(StringBuilder.class);

        String jsonString = null;
        JsonParser p = jsonFactory.createParser(jsonString);
        StdFromStringDeserializer<StringBuilder> deserializer = new StdFromStringDeserializer<StringBuilder>(StringBuilder.class);
        StringBuilder stringBuilder = deserializer.deserialize(p, ctxt);
        assertNull(stringBuilder);
    }
}

class StdFromStringDeserializer<T> extends FromStringDeserializer<T> {
    public StdFromStringDeserializer(Class<T> valueType) {
        super(valueType);
    }

    @Override
    protected T _deserialize(String value, DeserializationContext ctxt) throws IOException {
        // Implement your custom deserialization logic here
        throw new UnsupportedOperationException();
    }
}