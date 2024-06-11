package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFileDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new File("testFile.txt").getAbsolutePath());
        StdDeserializer fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File deserializedFile = fileDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(new File("testFile.txt"), deserializedFile);
    }

    @Test
    public void testURLDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new String("http://example.com"));
        StdDeserializer urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL deserializedURL = urlDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("http://example.com", deserializedURL.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new String("http://example.com/path/to/file"));
        StdDeserializer uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI deserializedURI = uriDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("http://example.com/path/to/file", deserializedURI.toURI().toString());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new String("java.lang.String"));
        StdDeserializer classDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String deserializedString = (String) classDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("java.lang.String", deserializedString.getClass().getName());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new String("USD"));
        StdDeserializer currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency deserializedCurrency = currencyDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), deserializedCurrency);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new String("/(.*)\\.txt$"));
        StdDeserializer patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern deserializedPattern = patternDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Pattern.compile("/(.*)\\.txt$"), deserializedPattern);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new String("en_US"));
        StdDeserializer localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale deserializedLocale = localeDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Locale.US, deserializedLocale);
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new String(""));
        StdDeserializer emptyStringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String deserializedEmptyString = (String) emptyStringDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertNull(deserializedEmptyString);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidStringDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new String("Invalid"));
        StdDeserializer stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        stringDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
    }

    @Test
    public void testInvalidEmbeddedObjectDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(new String("{ \"class\": \"java.lang.Integer\", \"value\": 42 }"));
        StdDeserializer embeddedObjectDeserializer = FromStringDeserializer.findDeserializer(Integer.class);
        Integer deserializedInteger = embeddedObjectDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertNull(deserializedInteger);
    }
}