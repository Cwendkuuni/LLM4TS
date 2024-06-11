import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    public FromStringDeserializerTest() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFileDeserialization() throws IOException {
        String json = "{\"className\":\"java.io.File\",\"value\":\"test.txt\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        File expectedFile = new File("test.txt");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        File deserializedFile = deserializer.deserialize(objectMapper.getFactory(), objectMapper.getDeserializationContext(), inputStream);

        assertEquals(expectedFile, deserializedFile);
    }

    @Test
    public void testURLDeserialization() throws IOException {
        String json = "{\"className\":\"java.net.URL\",\"value\":\"https://www.example.com\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        URL expectedURL = new URL("https://www.example.com");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL deserializedURL = deserializer.deserialize(objectMapper.getFactory(), objectMapper.getDeserializationContext(), inputStream);

        assertEquals(expectedURL, deserializedURL);
    }

    @Test
    public void testURIDeserialization() throws IOException {
        String json = "{\"className\":\"java.net.URI\",\"value\":\"https://www.example.com\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        URI expectedURI = new URI("https://www.example.com");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI deserializedURI = deserializer.deserialize(objectMapper.getFactory(), objectMapper.getDeserializationContext(), inputStream);

        assertEquals(expectedURI, deserializedURI);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        String json = "{\"className\":\"java.util.Currency\",\"value\":\"USD\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        Currency expectedCurrency = Currency.getInstance("USD");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency deserializedCurrency = deserializer.deserialize(objectMapper.getFactory(), objectMapper.getDeserializationContext(), inputStream);

        assertEquals(expectedCurrency, deserializedCurrency);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        String json = "{\"className\":\"java.util.regex.Pattern\",\"value\":\"\\w+\\.\\w+\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        Pattern expectedPattern = Pattern.compile("\\w+\\.\\w+");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern deserializedPattern = deserializer.deserialize(objectMapper.getFactory(), objectMapper.getDeserializationContext(), inputStream);

        assertEquals(expectedPattern, deserializedPattern);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        String json = "{\"className\":\"java.util.Locale\",\"value\":\"en_US\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        Locale expectedLocale = new Locale("en", "US");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale deserializedLocale = deserializer.deserialize(objectMapper.getFactory(), objectMapper.getDeserializationContext(), inputStream);

        assertEquals(expectedLocale, deserializedLocale);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        String json = "{\"className\":\"java.nio.charset.Charset\",\"value\":\"US-ASCII\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        Charset expectedCharset = Charset.forName("US-ASCII");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset deserializedCharset = deserializer.deserialize(objectMapper.getFactory(), objectMapper.getDeserializationContext(), inputStream);

        assertEquals(expectedCharset, deserializedCharset);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        String json = "{\"className\":\"java.util.TimeZone\",\"value\":\"America/Los_Angeles\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        TimeZone expectedTimeZone = TimeZone.getTimeZone("America/Los_Angeles");

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone deserializedTimeZone = deserializer.deserialize(objectMapper.getFactory(), objectMapper.getDeserializationContext(), inputStream);

        assertEquals(expectedTimeZone, deserializedTimeZone);
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        String json = "{\"className\":\"java.io.File\",\"value\":\"invalid\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());

        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        File deserializedFile = deserializer.deserialize(objectMapper.getFactory(), objectMapper.getDeserializationContext(), inputStream);

        assertNull(deserializedFile);
    }
}