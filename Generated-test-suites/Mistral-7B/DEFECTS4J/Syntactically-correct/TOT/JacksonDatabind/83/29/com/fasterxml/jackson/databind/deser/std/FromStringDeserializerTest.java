package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testURLDeserializer() throws JsonMappingException, IOException {
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctxt = mapper.getDeserializationContext().withBehavior(DeserializationConfig.Feature.ACCEPT_empty_STRING_AS_NULL_OBJECT, true);

        String validURLString = "https://www.example.com";
        URL expectedURL = new URL("https", "www.example.com", 443, "/");
        URL actualURL = (URL) urlDeserializer.deserialize(mapper.getFactory().createParser(validURLString), ctxt);
        assertEquals(expectedURL, actualURL);

        String emptyStringURL = "";
        URL emptyURL = urlDeserializer._deserializeFromEmptyString();
        assertNull(emptyURL);

        String invalidURL = "invalid:/";
        try {
            urlDeserializer.deserialize(mapper.getFactory().createParser(invalidURL), ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testLocaleDeserializer() throws JsonMappingException, IOException {
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctxt = mapper.getDeserializationContext().withBehavior(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        String validLocaleString = "en_US";
        Locale expectedLocale = Locale.US;
        Locale actualLocale = (Locale) localeDeserializer.deserialize(mapper.getFactory().createParser(validLocaleString), ctxt);
        assertEquals(expectedLocale, actualLocale);

        String emptyStringLocale = "";
        Locale emptyLocale = localeDeserializer._deserializeFromEmptyString();
        assertNull(emptyLocale);

        String invalidLocaleString = "fr_CHina";
        try {
            localeDeserializer.deserialize(mapper.getFactory().createParser(invalidLocaleString), ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    // Add more test cases for other types like File, Class, JavaType, Currency, Pattern, Charset, TimeZone, InetAddress, InetSocketAddress, StringBuilder

    @Test
    public void testUnexpectedToken() throws JsonMappingException, IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String jsonString = "[1, 2, 3]";
        JsonParser parser = mapper.getFactory().createParser(jsonString);
        parser.nextToken(); // nextToken should be JsonToken.START_ARRAY
        assertNull(deserializer.deserialize(parser, ctxt));
    }
}