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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @InjectMocks
    private FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
    @InjectMocks
    private FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
    @InjectMocks
    private FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
    @InjectMocks
    private FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        objectMapper = Mockito.spy(new ObjectMapper());
    }

    @Test
    public void testDeserialize_URI() throws JsonProcessingException, IOException {
        String uriString = "http://www.example.com:8080/path/to/resource";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getValueAsString()).thenReturn(uriString);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        URI uri = uriDeserializer.deserialize(jsonParser, context);
        assertEquals(new URI(uriString), uri);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_MalformedURI() throws JsonProcessingException, IOException {
        String uriString = "http://www.example.com/path/to/resource:8080";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getValueAsString()).thenReturn(uriString);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        uriDeserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testDeserialize_Locale() throws JsonProcessingException, IOException {
        String localeString = "en_US";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getValueAsString()).thenReturn(localeString);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Locale locale = localeDeserializer.deserialize(jsonParser, context);
        assertEquals(new Locale("en", "US"), locale);
    }

    @Test
    public void testDeserialize_InvalidLocale() throws JsonProcessingException, IOException {
        String localeString = "en_US_Invalid";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getValueAsString()).thenReturn(localeString);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        localeDeserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testDeserialize_TimeZone() throws JsonProcessingException, IOException {
        String timeZoneString = "America/Los_Angeles";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getValueAsString()).thenReturn(timeZoneString);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        TimeZone timeZone = timeZoneDeserializer.deserialize(jsonParser, context);
        assertEquals(TimeZone.getTimeZone(timeZoneString), timeZone);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidTimeZone() throws JsonProcessingException, IOException {
        String timeZoneString = "InvalidTimeZone";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getValueAsString()).thenReturn(timeZoneString);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        timeZoneDeserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testDeserialize_Currency() throws JsonProcessingException, IOException {
        String currencyString = "USD";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getValueAsString()).thenReturn(currencyString);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        Currency currency = currencyDeserializer.deserialize(jsonParser, context);
        assertEquals(Currency.getInstance(currencyString), currency);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidCurrency() throws JsonProcessingException, IOException {
        String currencyString = "InvalidCurrency";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        Mockito.when(jsonParser.getValueAsString()).thenReturn(currencyString);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        currencyDeserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        assertNotNull(FromStringDeserializer.findDeserializer(URI.class));
        assertNotNull(FromStringDeserializer.findDeserializer(Locale.class));
        assertNotNull(FromStringDeserializer.findDeserializer(TimeZone.class));
        assertNotNull(FromStringDeserializer.findDeserializer(Currency.class));
        assertNull(FromStringDeserializer.findDeseralizer(int.class));
    }
}