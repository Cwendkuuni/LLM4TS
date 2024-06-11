package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @org.junit.Before
    public void setup() throws Exception {
        objectMapper = JsonMapperFactory.getMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"/path/to/file.txt\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(new File("/path/to/file.txt"), result);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"http://google.com\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(new URL("http://google.com"), result);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"http://google.com/path?query\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(new URI("http://google.com/path?query"), result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"java.lang.String\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext()).toString();
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"java.util.List<java.lang.String>\",[\"java.lang.String\"]");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(List.class);
        JavaType javaType = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(new TypeFactory().constructParametrizedType(List.class, String.class), javaType);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"USD\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"\\w+\\.\\w+\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Pattern.compile("\\w+\\.\\w+"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        jsonParser = objectMapper.getFactory().createParser("\"en_GB_POSIX\"");
        result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Locale.forLanguageTag("en_GB"), result);

        jsonParser = objectMapper.getFactory().createParser("\"en_GB\"");
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"America/Los_Angeles\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);

        jsonParser = objectMapper.getFactory().createParser("\"Pacific/Honolulu\"");
        result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("Pacific/Honolulu"), result);

        jsonParser = objectMapper.getFactory().createParser("\"EST\"");
        result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);

        jsonParser = objectMapper.getFactory().createParser("\"EST5EDT\"");
        result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"192.168.0.1\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(InetAddress.getByName("192.168.0.1"), result);

        jsonParser = objectMapper.getFactory().createParser("\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]\"");
        result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(InetAddress.getByName("2001:0db8:85a3:0:0:8a2e:370:7334"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"[192.168.0.1]:8080\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.0.1"), 8080), result);

        jsonParser = objectMapper.getFactory().createParser("\"http://localhost:8080\"");
        result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(new InetSocketAddress("localhost", 8080), result);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"null\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeInvalidString() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"InvalidType\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeInvalidFormat() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"2022-03-31T23:60:60Z+02:00\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(ZoneId.class);
        assertThrows(InvalidFormatException.class, () -> deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext()));
    }
}