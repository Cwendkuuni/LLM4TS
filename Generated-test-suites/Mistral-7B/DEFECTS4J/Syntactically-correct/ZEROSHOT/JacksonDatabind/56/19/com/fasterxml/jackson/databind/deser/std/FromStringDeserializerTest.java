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
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @InjectMocks
    private FromStringDeserializer<Locale> localeDeserializer;

    @InjectMocks
    private FromStringDeserializer<URI> uriDeserializer;

    @InjectMocks
    private FromStringDeserializer<URL> urlDeserializer;

    @InjectMocks
    private FromStringDeserializer<Currency> currencyDeserializer;

    @InjectMocks
    private FromStringDeserializer<TimeZone> timeZoneDeserializer;

    private DeserializationContext context;
    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        context = Mockito.mock(DeserializationContext.class);
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeStringToLocale() throws JsonProcessingException, IOException {
        String localeString = "en_US";
        Locale locale = localeDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
        assertEquals(localeString, locale.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringToLocale() throws JsonProcessingException, IOException {
        String invalidLocaleString = "en_US_VN";
        localeDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
    }

    @Test
    public void testDeserializeStringToURI() throws JsonProcessingException, IOException {
        String uriString = "http://www.example.com/test";
        URI uri = uriDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
        assertEquals(uriString, uri.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringToURI() throws JsonProcessingException, IOException {
        String invalidUriString = "http://www.example.com:test";
        uriDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
    }

    @Test
    public void testDeserializeStringToURL() throws JsonProcessingException, IOException {
        String urlString = "http://www.example.com/test";
        URL url = urlDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
        assertEquals(urlString, url.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringToURL() throws JsonProcessingException, IOException {
        String invalidUrlString = "http://www.example.com:test";
        urlDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
    }

    @Test
    public void testDeserializeStringToCurrency() throws JsonProcessingException, IOException {
        String currencyString = "USD";
        Currency currency = currencyDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringToCurrency() throws JsonProcessingException, IOException {
        String invalidCurrencyString = "EURUSD";
        currencyDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
    }

    @Test
    public void testDeserializeStringToTimeZone() throws JsonProcessingException, IOException {
        String timeZoneString = "America/New_York";
        TimeZone timeZone = timeZoneDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
        assertEquals(timeZoneString, timeZone.getID());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringToTimeZone() throws JsonProcessingException, IOException {
        String invalidTimeZoneString = "Europe/London";
        timeZoneDeserializer.deserialize(Mockito.mock(JsonParser.class), context);
    }
}