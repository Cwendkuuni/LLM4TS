package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    private DeserializationContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        context = mock(DeserializationContext.class);
        objectMapper = mock(ObjectMapper.class);
        when(objectMapper.findValueToBeDeserialized(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class))).thenReturn(null);
    }

    @Test
    public void testDeserializeFile() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(File.class);
        String fileString = "test.txt";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(fileString);

        File result = deserializer.deserialize(parser, context);
        assertEquals(new File(fileString), result);
    }

    @Test
    public void testDeserializeUrl() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class);
        String urlString = "http://test.com";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(urlString);

        URL result = deserializer.deserialize(parser, context);
        assertEquals(new URL(urlString), result);
    }

    @Test
    public void testDeserializeUri() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class);
        String uriString = "http://test.com";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(uriString);

        URI result = deserializer.deserialize(parser, context);
        assertEquals(new URI(uriString), result);
    }

    @Test
    public void testDeserializeClass() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        String className = "java.lang.String";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(className);

        Class<?> result = deserializer.deserialize(parser, context);
        assertEquals(Class.forName(className), result.getClass());
    }

    @Test
    public void testDeserializeJavaType() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(JavaType.class);
        String javaTypeString = "java.util.List<java.lang.String>";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(javaTypeString);

        JavaType result = deserializer.deserialize(parser, context);
        assertEquals(objectMapper.getTypeFactory().constructType(javaTypeString), result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Currency.class);
        String currencyString = "EUR";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(currencyString);

        Currency result = deserializer.deserialize(parser, context);
        assertEquals(Currency.getInstance(currencyString), result);
    }

    @Test
    public void testDeserializePattern() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Pattern.class);
        String patternString = "\\d{4}-\\d{2}-\\d{2}";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(patternString);

        Pattern result = deserializer.deserialize(parser, context);
        assertEquals(Pattern.compile(patternString), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
        String localeString = "en_US";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(localeString);

        Locale result = deserializer.deserialize(parser, context);
        assertEquals(new Locale(localeString), result);
    }

    @Test
    public void testDeserializeCharset() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Charset.class);
        String charsetString = "UTF-8";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(charsetString);

        Charset result = deserializer.deserialize(parser, context);
        assertEquals(Charset.forName(charsetString), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(TimeZone.class);
        String timeZoneString = "America/Los_Angeles";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(timeZoneString);

        TimeZone result = deserializer.deserialize(parser, context);
        assertEquals(TimeZone.getTimeZone(timeZoneString), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetAddress.class);
        String inetAddressString = "192.168.1.1";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(inetAddressString);

        InetAddress result = deserializer.deserialize(parser, context);
        assertEquals(InetAddress.getByName(inetAddressString), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetSocketAddress.class);
        String inetSocketAddressString = "[255.255.255.255]:80";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(inetSocketAddressString);

        InetSocketAddress result = deserializer.deserialize(parser, context);
        InetSocketAddress expected = new InetSocketAddress("255.255.255.255", 80);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeNull() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Object.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(null);

        Object result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException, JsonProcessingException {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Integer.class);
        String value = "test";
        JsonParser parser = Mockito.mock(JsonParser.class);
        when(parser.getValueAsString()).thenReturn(value);

        deserializer.deserialize(parser, context);
    }
}