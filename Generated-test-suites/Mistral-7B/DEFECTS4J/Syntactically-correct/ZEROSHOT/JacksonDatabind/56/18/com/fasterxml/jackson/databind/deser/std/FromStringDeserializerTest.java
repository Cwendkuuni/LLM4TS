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
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @InjectMocks
    private FromStringDeserializer<Locale> localeDeserializer;
    @InjectMocks
    private FromStringDeserializer<URI> uriDeserializer;
    @InjectMocks
    private FromStringDeserializer<Currency> currencyDeserializer;

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Locale.class, localeDeserializer);
        module.addDeserializer(URI.class, uriDeserializer);
        module.addDeserializer(Currency.class, currencyDeserializer);
        mapper.registerModule(module);
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonProcessingException {
        String localeString = "en_US";
        Locale locale = localeDeserializer.deserialize(createJsonParser(localeString), Mockito.mock(DeserializationContext.class));
        assertEquals(localeString, locale.toString());
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testDeserializeUri() throws IOException, JsonProcessingException {
        String uriString = "http://www.example.com";
        URI uri = uriDeserializer.deserialize(createJsonParser(uriString), Mockito.mock(DeserializationContext.class));
        assertEquals(uriString, uri.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidUri() throws IOException, JsonProcessingException {
        String uriString = "http://www.example.com/1234";
        uriDeserializer.deserialize(createJsonParser(uriString), Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonProcessingException {
        String currencyString = "USD";
        Currency currency = currencyDeserializer.deserialize(createJsonParser(currencyString), Mockito.mock(DeserializationContext.class));
        assertEquals(currencyString, currency.getCurrencyCode());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidCurrency() throws IOException, JsonProcessingException {
        String currencyString = "XCD";
        currencyDeserializer.deserialize(createJsonParser(currencyString), Mockito.mock(DeserializationContext.class));
    }

    @Test
    public void testDeserializeEmptyString() throws IOException, JsonProcessingException {
        String emptyString = "";
        Locale locale = localeDeserializer.deserializeFromEmptyString();
        assertNull(locale);

        URI uri = uriDeserializer.deserializeFromEmptyString();
        assertEquals("", uri.toString());

        Currency currency = currencyDeserializer.deserializeFromEmptyString();
        assertNull(currency);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException, JsonProcessingException {
        String jsonString = "{\"value\": {\"class\": \"java.net.URI\", \"value\": \"http://www.example.com\"}}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
        FromStringDeserializer<URI> uriDeserializer = new URIFromStringDeserializer();

        URI uri = mapper.readValue(jsonString, URI.class);
        assertEquals("http://www.example.com", uri.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedInvalidObject() throws IOException, JsonProcessingException {
        String jsonString = "{\"value\": {\"class\": \"java.net.URL\", \"value\": \"http://www.example.com\"}}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
        FromStringDeserializer<URI> uriDeserializer = new URIFromStringDeserializer();

        uriDeserializer._deserializeEmbedded(Mockito.mock(Object.class), Mockito.mock(DeserializationContext.class));
    }

    private JsonParser createJsonParser(String jsonString) throws IOException {
        return mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes()));
    }
}

class URIFromStringDeserializer extends FromStringDeserializer<URI> {
    URIFromStringDeserializer() {
        super(URI.class);
    }

    @Override
    protected URI _deserialize(String value, DeserializationContext ctxt) throws IOException {
        return new URI(value);
    }
}