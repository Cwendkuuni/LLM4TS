package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    private ObjectMapper mapper;
    private JsonFactory jsonFactory;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jsonFactory = mapper.getFactory();
    }

    @Test
    public void testDeserializeFile() throws IOException {
        String json = "\"test.txt\"";
        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        File result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals("test.txt", result.getName());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        String json = "\"http://example.com\"";
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        URL result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        String json = "\"http://example.com/path/to/resource\"";
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        URI result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals("http", result.getScheme());
        assertEquals("/path/to/resource", result.getPath());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        String json = "java.lang.String";
        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer<Class<?>>(Class.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Class<?> result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        String json = "com.example.MyClass";
        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer<JavaType>(JavaType.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        TypeFactory typeFactory = mapper.getTypeFactory();

        JavaType result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(typeFactory.constructType(MyClass.class), result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String json = "USD";
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<Currency>(Currency.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Currency result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        String json = "/[A-Za-z]+/";
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer<Pattern>(Pattern.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Pattern result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(Pattern.compile("/[A-Za-z]+/"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        String json = "en_US";
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Locale result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        String json = "UTF-8";
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer<Charset>(Charset.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Charset result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        String json = "America/New_York";
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        TimeZone result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        String json = "[255.255.255.255]";
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        InetAddress result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(InetAddress.getByName("255.255.255.255"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String json = "[255.255.255.255]:8080";
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        InetSocketAddress result = deserializer.deserialize(parser, context);

        assertNotNull(result);
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 8080), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidType() throws IOException {
        String json = "java.util.ArrayList";
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(ArrayList.class.getName());

        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        deserializer.deserialize(parser, context);
    }
}