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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @Test
    public void testDeserializeUrl() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        URL expectedUrl = new URL("http://example.com");

        Mockito.when(ctxt.handleUnexpectedToken(URL.class, parser)).thenReturn(expectedUrl);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(URL.class);
        URL result = serializer.deserialize(parser, ctxt);

        assertEquals(expectedUrl, result);
    }

    @Test
    public void testDeserializeUri() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        URI expectedUri = new URI("http://example.com/path?query=param");

        Mockito.when(ctxt.handleUnexpectedToken(URI.class, parser)).thenReturn(expectedUri);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(URI.class);
        URI result = serializer.deserialize(parser, ctxt);

        assertEquals(expectedUri, result);
    }

    @Test
    public void testDeserializeClass() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        Class<StringBuilder> expectedClass = StringBuilder.class;

        Mockito.when(ctxt.findClass(Mockito.eq("StringBuilder"))).thenReturn(expectedClass);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(StringBuilder.class);
        StringBuilder result = (StringBuilder) serializer.deserialize(parser, ctxt);

        assertEquals(expectedClass, result.getClass());
    }

    @Test
    public void testDeserializeJavaType() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        JavaType expectedType = mapper.getTypeFactory().constructType(int[].class);

        Mockito.when(ctxt.getTypeFactory()).then(invocation -> expectedType);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(JavaType.class);
        JavaType result = (JavaType) serializer.deserialize(parser, ctxt);

        assertEquals(expectedType, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        String unknownTypeString = "UnknownType";

        FromStringDeserializer serializer = new FromStringDeserializer.Std(unknownTypeString.getClass());
        serializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        Currency expectedCurrency = Currency.getInstance("USD");

        Mockito.when(ctxt.handleUnexpectedToken(Currency.class, parser)).thenReturn(expectedCurrency);
        FromStringDeserializer serializer = new FromStringDeserializer.Std(Currency.class);
        Currency result = serializer.deserialize(parser, ctxt);

        assertEquals(expectedCurrency, result);
    }

    @Test
    public void testDeserializePattern() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        String patternString = "\\w+";

        FromStringDeserializer serializer = new FromStringDeserializer.Std(Pattern.class);
        Pattern result = serializer.deserialize(parser, ctxt);

        Pattern expectedPattern = Pattern.compile(patternString);
        assertEquals(expectedPattern.pattern(), result.pattern());
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        String localeString = "en_US";

        FromStringDeserializer serializer = new FromStringDeserializer.Std(Locale.class);
        Locale result = serializer.deserialize(parser, ctxt);

        Locale expectedLocale = new Locale("en", "US");
        assertEquals(expectedLocale, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        String charsetName = "UTF-8";

        FromStringDeserializer serializer = new FromStringDeserializer.Std(Charset.class);
        Charset result = serializer.deserialize(parser, ctxt);

        Charset expectedCharset = Charset.forName(charsetName);
        assertEquals(expectedCharset, result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        String timeZoneId = "America/New_York";

        FromStringDeserializer serializer = new FromStringDeserializer.Std(TimeZone.class);
        TimeZone result = serializer.deserialize(parser, ctxt);

        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneId);
        assertEquals(expectedTimeZone, result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        String ipAddress = "192.168.0.1";

        FromStringDeserializer serializer = new FromStringDeserializer.Std(InetAddress.class);
        InetAddress result = serializer.deserialize(parser, ctxt);

        InetAddress expectedAddress = InetAddress.getByName(ipAddress);
        assertEquals(expectedAddress, result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        String ipAddress = "192.168.0.1";
        int port = 80;

        FromStringDeserializer serializer = new FromStringDeserializer.Std(InetSocketAddress.class);
        InetSocketAddress result = serializer.deserialize(parser, ctxt);

        InetSocketAddress expectedAddress = new InetSocketAddress(ipAddress, port);
        assertEquals(expectedAddress, result);
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        String initialValue = "initial";

        FromStringDeserializer serializer = new FromStringDeserializer.Std(StringBuilder.class);
        StringBuilder result = (StringBuilder) serializer.deserialize(parser, ctxt);

        StringBuilder expectedBuilder = new StringBuilder(initialValue);
        assertEquals(expectedBuilder, result);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        FromStringDeserializer serializer = new FromStringDeserializer.Std(Object.class);
        Object result = serializer.deserialize(parser, ctxt);

        assertEquals(null, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyArray() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        FromStringDeserializer serializer = new FromStringDeserializer.Std(Object[].class);
        serializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);
        Object embeddedObject = new Object();

        TypeDeserializer typeDeserializerMock = Mockito.mock(TypeDeserializer.class);
        Mockito.when(ctxt.findValueDeserializer(Mockito.eq(embeddedObject.getClass())))
                .thenReturn(typeDeserializerMock);

        FromStringDeserializer serializer = new FromStringDeserializer.Std(Object.class);
        Object result = serializer.deserializeEmbedded(embeddedObject, ctxt);

        Mockito.verify(typeDeserializerMock).deserializeValue(parser, ctxt, Mockito.eq(embeddedObject.getClass()));
        assertEquals(embeddedObject, result);
    }
}