package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;
    private DeserializationContext context;

    @Before
    public void setUp() {
        objectMapper = Mockito.spy(new ObjectMapper());
        context = Mockito.spy(objectMapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeFile() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String json = "\"/path/to/file.txt\"";

        Mockito.when(parser.getValueAsString()).thenReturn(json);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
        File result = fileDeserializer.deserialize(parser, context);

        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String json = "\"http://www.example.com\"";

        Mockito.when(parser.getValueAsString()).thenReturn(json);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
        URL result = urlDeserializer.deserialize(parser, context);

        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String json = "\"uri://www.example.com/path?query=value\"";

        Mockito.when(parser.getValueAsString()).thenReturn(json);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
        URI result = uriDeserializer.deserialize(parser, context);

        assertEquals("uri://www.example.com/path", result.toURI().toURL().toString());
        assertEquals("query=value", result.getQuery());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String json = "\"java.lang.String\"";

        Mockito.when(parser.getValueAsString()).thenReturn(json);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer<Class<?>>(Class.class);
        Class<?> result = classDeserializer.deserialize(parser, context);

        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String json = "\"com.example.MyClass\"";

        Mockito.when(parser.getValueAsString()).thenReturn(json);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
        JavaType result = javaTypeDeserializer.deserialize(parser, context);

        assertNotNull(result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String json = "\"USD\"";

        Mockito.when(parser.getValueAsString()).thenReturn(json);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
        Currency result = currencyDeserializer.deserialize(parser, context);

        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String json = "\"/^[a-zA-Z]+$/\"";

        Mockito.when(parser.getValueAsString()).thenReturn(json);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
        Pattern result = patternDeserializer.deserialize(parser, context);

        assertEquals(Pattern.compile("/^[a-zA-Z]+$/"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String json = "\"en_US\"";

        Mockito.when(parser.getValueAsString()).thenReturn(json);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
        Locale result = localeDeserializer.deserialize(parser, context);

        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String json = "\"UTF-8\"";

        Mockito.when(parser.getValueAsString()).thenReturn(json);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
        Charset result = charsetDeserializer.deserialize(parser, context);

        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String json = "\"America/Los_Angeles\"";

        Mockito.when(parser.getValueAsString()).thenReturn(json);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        TimeZone result = timeZoneDeserializer.deserialize(parser, context);

        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String jsonIpv4 = "\"192.168.1.1\"";
        String jsonIpv6 = "[\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"]";

        Mockito.when(parser.getValueAsString()).thenReturn(jsonIpv4).thenReturn(jsonIpv6);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);

        InetAddress resultIpv4 = inetAddressDeserializer.deserialize(parser, context);
        InetAddress resultIpv6 = inetAddressDeserializer.deserialize(parser, context);

        assertEquals(InetAddress.getByName("192.168.1.1"), resultIpv4);
        assertEquals(InetAddress.getByName("2001:0db8:85a3:0:0:0:8a2e:370:7334"), resultIpv6);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        String jsonIpv4 = "\"192.168.1.1:80\"";
        String jsonIpv6 = "[\"2001:0db8:85a3:0:0:0:8a2e:370:7334:80\"]";

        Mockito.when(parser.getValueAsString()).thenReturn(jsonIpv4).thenReturn(jsonIpv6);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);

        InetSocketAddress resultIpv4 = inetSocketAddressDeserializer.deserialize(parser, context);
        InetSocketAddress resultIpv6 = inetSocketAddressDeserializer.deserialize(parser, context);

        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80), resultIpv4);
        assertEquals(new InetSocketAddress(InetAddress.getByName("2001:0db8:85a3:0:0:0:8a2e:370:7334"), 80), resultIpv6);
    }

    @Test
    public void testDeserializeEmptyStringForFile() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.getValueAsString()).thenReturn(null);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);

        assertNull(fileDeserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeEmptyStringForUri() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.getValueAsString()).thenReturn(null);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);

        assertNull(uriDeserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        Object embeddedObject = Mockito.mock(Object.class);

        Mockito.when(parser.getValueAsString()).thenReturn("\"ObjectToTest\"");
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getEmbeddedObject()).thenReturn(embeddedObject);

        FromStringDeserializer<String> stringDeserializer = new FromStringDeserializer<String>(String.class);

        String result = stringDeserializer._deserializeEmbedded(embeddedObject, context);

        assertEquals("ObjectToTest", result);
    }
}