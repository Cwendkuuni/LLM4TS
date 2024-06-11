package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        String json = "{\"fileName\":\"testFile.txt\"}";
        File expected = new File("testFile.txt");

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        String json = "{\"url\":\"http://www.example.com\"}";
        URL expected = new URL("http://www.example.com");

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        String json = "{\"uri\":\"http://www.example.com/path/to/resource\"}";
        URI expected = new URI("http://www.example.com/path/to/resource");

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test
    public void testDeserializeClass() throws IOException, ClassNotFoundException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String json = "{\"className\":\"java.lang.String\"}";
        Class<?> expected = Class.forName("java.lang.String");

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        json = "{\"className\":\"java.lang.String\"}";
        Object deserialized = deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext());
        assertEquals("", deserialized.toString());
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer[].class);
        String json = "{\"javaType\":\"[I\"}";
        JavaType expected = mapper.getTypeFactory().constructType(Integer[].class);

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String json = "{\"currency\":\"USD\"}";
        Currency expected = Currency.getInstance("USD");

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String json = "{" +
                "\"pattern\":\"\\\\w+\\\\.\\\\w+\"" +
                "}";
        Pattern expected = Pattern.compile("\\w+\\.\\w+");

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String json = "{" +
                "\"locale\":\"en_US\"" +
                "}";
        Locale expected = new Locale("en", "US");

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));

        json = "{" +
                "\"locale\":\"fr_FR_fr_CH\"" +
                "}";
        expected = new Locale("fr", "FR", "CH");

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String json = "{" +
                "\"charset\":\"UTF-8\"" +
                "}";
        Charset expected = Charset.forName("UTF-8");

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String json = "{" +
                "\"timeZone\":\"UTC\"" +
                "}";
        TimeZone expected = TimeZone.getTimeZone("UTC");

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String json = "{" +
                "\"inetAddress\":\"192.168.1.1\"" +
                "}";
        InetAddress expected = InetAddress.getByName("192.168.1.1");

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String json = "{" +
                "\"inetSocketAddress\":\"[192.168.1.1]:80\"" +
                "}";
        InetSocketAddress expected = new InetSocketAddress("192.168.1.1", 80);

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));

        json = "{" +
                "\"inetSocketAddress\":\"[::1]:80\"" +
                "}";
        expected = new InetSocketAddress("0:0:0:0:0:0:0:1", 80);

        assertEquals(expected, deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test
    public void testDeserializeEmptyStringForClass() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String json = "{}";

        assertNull(deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringForClass() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String json = "{\"className\":\"InvalidClass\"}";

        deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringForURI() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        String json = "{\"uri\":\"invalidURI\"}";

        deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringForLocale() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String json = "{" +
                "\"locale\":\"en_US_invalid\"" +
                "}";

        deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringForCharset() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String json = "{" +
                "\"charset\":\"invalidCharset\"" +
                "}";

        deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringForTimeZone() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String json = "{" +
                "\"timeZone\":\"invalidTimeZone\"" +
                "}";

        deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringForInetAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String json = "{" +
                "\"inetAddress\":\"invalidIPAddress\"" +
                "}";

        deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringForInetSocketAddress() throws IOException {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String json = "{" +
                "\"inetSocketAddress\":\"[invalidIPAddress]:80\"" +
                "}";

        deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext());

        json = "{" +
                "\"inetSocketAddress\":\"[::1]:-1\"" +
                "}";

        deserializer.deserialize(mapper.readerForTree(json.getBytes(StandardCharsets.UTF_8)), mapper.getContext());
    }
}