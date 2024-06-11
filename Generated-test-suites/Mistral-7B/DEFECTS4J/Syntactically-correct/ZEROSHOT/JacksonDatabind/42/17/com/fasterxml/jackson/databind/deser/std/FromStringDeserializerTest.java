package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @InjectMocks
    private FromStringDeserializer<URI> uriDeserializer;
    @InjectMocks
    private FromStringDeserializer<Currency> currencyDeserializer;
    @InjectMocks
    private FromStringDeserializer<Locale> localeDeserializer;
    @InjectMocks
    private FromStringDeserializer<TimeZone> timeZoneDeserializer;

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        objectMapper = new JsonMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeUri() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        URI expectedUri = new URI("http://www.example.com");

        Mockito.when(jsonParser.getValueAsString()).thenReturn("http://www.example.com");
        when(context.findClass(URI.class)).thenReturn(URI.class);
        when(context.getTypeFactory()).thenReturn(TypeFactory.defaultInstance());

        URI actualUri = uriDeserializer.deserialize(jsonParser, context);
        assertEquals(expectedUri, actualUri);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Currency expectedCurrency = Currency.getInstance("USD");

        Mockito.when(jsonParser.getValueAsString()).thenReturn("USD");
        when(context.findClass(Currency.class)).thenReturn(Currency.class);

        Currency actualCurrency = currencyDeserializer.deserialize(jsonParser, context);
        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Locale expectedLocale = new Locale("en", "US");

        Mockito.when(jsonParser.getValueAsString()).thenReturn("en_US");

        Locale actualLocale = localeDeserializer.deserialize(jsonParser, context);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidLocale() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Mockito.when(jsonParser.getValueAsString()).thenReturn("es_XX");

        localeDeserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        TimeZone expectedTimeZone = TimeZone.getTimeZone("America/New_York");

        Mockito.when(jsonParser.getValueAsString()).thenReturn("America/New_York");

        TimeZone actualTimeZone = timeZoneDeserializer.deserialize(jsonParser, context);
        assertEquals(expectedTimeZone, actualTimeZone);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Mockito.when(jsonParser.getValueAsString()).thenReturn("invalid_json");

        uriDeserializer.deserialize(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullJson() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Mockito.when(jsonParser.getValueAsString()).thenReturn(null);

        uriDeserializer.deserialize(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedToken() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        JsonToken unexpectedToken = JsonToken.START_OBJECT;

        Mockito.when(jsonParser.getCurrentToken()).thenReturn(unexpectedToken);

        uriDeserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testDeserializeEmptyStringUri() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        URI expectedUri = URI.create("");

        Mockito.when(jsonParser.getValueAsString()).thenReturn("");
        when(context.findClass(URI.class)).thenReturn(URI.class);
        when(context.getTypeFactory()).thenReturn(TypeFactory.defaultInstance());

        URI actualUri = uriDeserializer.deserialize(jsonParser, context);
        assertEquals(expectedUri, actualUri);
    }

    @Test
    public void testDeserializeEmptyStringCurrency() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Currency expectedCurrency = Currency.getInstance("EUR");

        when(_localeDeserializer._deserializeFromEmptyString()).thenReturn(expectedCurrency);

        Currency actualCurrency = currencyDeserializer.deserialize(jsonParser, context);
        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    public void testDeserializeEmptyStringLocale() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Locale expectedLocale = Locale.getDefault();

        when(_localeDeserializer._deserializeFromEmptyString()).thenReturn(expectedLocale);

        Locale actualLocale = localeDeserializer.deserialize(jsonParser, context);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyStringTimeZone() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        TimeZone expectedTimeZone = TimeZone.getDefault();

        when(_timeZoneDeserializer._deserializeFromEmptyString()).thenThrow(new IllegalArgumentException());

        timeZoneDeserializer.deserialize(jsonParser, context);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Object embeddedObject = new URI("http://www.example.com");

        when(jsonParser.getEmbeddedObject()).thenReturn(embeddedObject);

        URI actualUri = uriDeserializer.deserialize(jsonParser, context);
        assertEquals(expectedUri, actualUri);
    }

    @Test
    public void testDeserializeNullEmbeddedObject() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(jsonParser.getEmbeddedObject()).thenReturn(null);

        URI actualUri = uriDeserializer.deserialize(jsonParser, context);
        assertNull(actualUri);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidEmbeddedObject() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Object invalidEmbeddedObject = new Object();

        when(jsonParser.getEmbeddedObject()).thenReturn(invalidEmbeddedObject);

        uriDeserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        URI[] expectedUris = new URI[] { new URI("http://www.example1.com"), new URI("http://www.example2.com") };

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextValue()).thenReturn(new URI("http://www.example1.com"));
        when(jsonParser.nextValue()).thenReturn(new URI("http://www.example2.com"));
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        URI[] actualUris = (URI[]) uriDeserializer.deserialize(jsonParser, context);
        assertArrayEquals(expectedUris, actualUris);
    }

    @Test
    public void testDeserializeArrayWithSingleElement() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        URI expectedUri = new URI("http://www.example.com");

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextValue()).thenReturn(new URI("http://www.example.com"));
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        URI[] actualUris = (URI[]) uriDeserializer.deserialize(jsonParser, context);
        assertEquals(expectedUri, actualUris[0]);
    }

    @Test
    public void testDeserializeArrayEmpty() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        URI[] actualUris = (URI[]) uriDeserializer.deserialize(jsonParser, context);
        assertArrayEquals(new URI[0], actualUris);
    }

    private void when(_LocaleDeserializer localeDeserializer, String... localeStrings) {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Mockito.when(jsonParser.getValueAsString()).thenReturn(localeStrings[0]);

        when(context.findClass(Locale.class)).thenReturn(Locale.class);
        when(context.getTypeFactory()).thenReturn(TypeFactory.defaultInstance());

        Locale expectedLocale = new Locale(localeStrings[1], localeStrings[2]);

        when(localeDeserializer._deserialize(eq(expectedLocale.toString()), eq(context))).thenReturn(expectedLocale);
    }

    private void when(_TimeZoneDeserializer timeZoneDeserializer, String timeZoneId) {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Mockito.when(jsonParser.getValueAsString()).thenReturn(timeZoneId);

        when(context.findClass(TimeZone.class)).thenReturn(TimeZone.class);
        when(context.getTypeFactory()).thenReturn(TypeFactory.defaultInstance());

        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneId);

        when(timeZoneDeserializer._deserialize(eq(expectedTimeZone.getID()), eq(context))).thenReturn(expectedTimeZone);
    }
}