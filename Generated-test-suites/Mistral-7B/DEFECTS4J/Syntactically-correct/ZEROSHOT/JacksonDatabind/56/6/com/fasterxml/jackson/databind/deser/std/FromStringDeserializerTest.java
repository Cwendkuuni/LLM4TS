package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static final JavaType javaTypeUrl = TypeFactory.defaultInstance().constructType(URL.class);
    @SuppressWarnings("unchecked")
    private static final JavaType javaTypeUri = TypeFactory.defaultInstance().constructType(URI.class);
    @SuppressWarnings("unchecked")
    private static final JavaType javaTypeCurrency = TypeFactory.defaultInstance().constructType(Currency.class);
    @SuppressWarnings("unchecked")
    private static final JavaType javaTypeLocale = TypeFactory.defaultInstance().constructType(Locale.class);
    @SuppressWarnings("unchecked")
    private static final JavaType javaTypeTimeZone = TypeFactory.defaultInstance().constructType(TimeZone.class);

    @Test
    public void testDeserializeUrl() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"http://localhost:8080\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std urlDeserializer = (Std) FromStringDeserializer.findDeserializer(URL.class);
        URL result = urlDeserializer.deserialize(parser, ctxt);
        assertEquals("http://localhost:8080", result.toExternalForm());
    }

    @Test
    public void testDeserializeUri() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"http://localhost:8080/api/v1/users\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std uriDeserializer = (Std) FromStringDeserializer.findDeserializer(URI.class);
        URI result = uriDeserializer.deserialize(parser, ctxt);
        assertEquals("http", result.getScheme());
        assertEquals("/api/v1/users", result.getPath());
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"USD\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std currencyDeserializer = (Std) FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = currencyDeserializer.deserialize(parser, ctxt);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"en_US\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std localeDeserializer = (Std) FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = localeDeserializer.deserialize(parser, ctxt);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"America/Los_Angeles\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std timeZoneDeserializer = (Std) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = timeZoneDeserializer.deserialize(parser, ctxt);
        assertEquals("America/Los_Angeles", result.getID());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"42\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Integer> invalidDeserializer = new FromStringDeserializer<Integer>(Integer.class) {
            @Override
            protected Integer _deserialize(String value, DeserializationContext ctxt) throws IOException {
                throw new InvalidFormatException(ctxt.getParser(), "Invalid number", JsonToken.VALUE_NUMBER_INT, value, Integer.class);
            }
        };
        Integer result = invalidDeserializer.deserialize(parser, ctxt);
        fail("Deserialization should not be successful");
    }

}