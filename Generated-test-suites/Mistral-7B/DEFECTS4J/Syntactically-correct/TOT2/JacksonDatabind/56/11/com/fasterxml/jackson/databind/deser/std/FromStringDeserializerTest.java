import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        mapper = new ObjectMapper(new JsonFactory(), module);
    }

    @Test
    public void testDeserializeStringToFile() throws IOException {
        String jsonString = "\"/path/to/file\"";
        File expected = new File("/path/to/file");
        File actual = mapper.readValue(jsonParserFromString(jsonString), File.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringToURL() throws IOException {
        String jsonString = "\"http://example.com\"";
        URL expected = new URL("http://example.com");
        URL actual = mapper.readValue(jsonParserFromString(jsonString), URL.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringToURI() throws IOException {
        String jsonString = "\"http://example.com/path?query=value#fragment\"";
        URI expected = new URI("http", "example.com", "/path", null, null, null, null, null, null, null);
        URI actual = mapper.readValue(jsonParserFromString(jsonString), URI.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringToClass() throws IOException {
        String jsonString = "java.lang.String";
        Class<?> expected = String.class;
        Class<?> actual = mapper.readValue(jsonParserFromString(jsonString), Class.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringToJavaType() throws IOException {
        String jsonString = "java.util.List<java.lang.Integer>";
        JavaType expected = mapper.getTypeFactory().constructParametricType(java.util.List.class, Integer.class);
        JavaType actual = mapper.readValue(jsonParserFromString(jsonString), JavaType.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringToCurrency() throws IOException {
        String jsonString = "\"USD\"";
        Currency expected = Currency.getInstance("USD");
        Currency actual = mapper.readValue(jsonParserFromString(jsonString), Currency.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringToPattern() throws IOException {
        String jsonString = "/[a-z]+/i";
        Pattern expected = Pattern.compile("/[a-z]+/i");
        Pattern actual = mapper.readValue(jsonParserFromString(jsonString), Pattern.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringToLocale() throws IOException {
        String jsonString = "\"en_US\"";
        Locale expected = new Locale("en", "US");
        Locale actual = mapper.readValue(jsonParserFromString(jsonString), Locale.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringToCharset() throws IOException {
        String jsonString = "\"UTF-8\"";
        Charset expected = Charset.forName("UTF-8");
        Charset actual = mapper.readValue(jsonParserFromString(jsonString), Charset.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringToTimeZone() throws IOException {
        String jsonString = "\"America/Los_Angeles\"";
        TimeZone expected = TimeZone.getTimeZone("America/Los_Angeles");
        TimeZone actual = mapper.readValue(jsonParserFromString(jsonString), TimeZone.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringToInetAddress() throws IOException {
        String jsonString = "\"192.168.1.1\"";
        InetAddress expected = InetAddress.getByName("192.168.1.1");
        InetAddress actual = mapper.readValue(jsonParserFromString(jsonString), InetAddress.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringToInetSocketAddress() throws IOException {
        String jsonString = "[\"192.168.1.1\", 80]";
        InetSocketAddress expected = new InetSocketAddress("192.168.1.1", 80);
        InetSocketAddress actual = mapper.readValue(jsonParserFromString(jsonString), InetSocketAddress.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeEmptyStringToClass() throws IOException {
        String jsonString = "";
        Class<?> expected = null;
        Class<?> actual = mapper.readValue(jsonParserFromString(jsonString), Class.class);
        assertNull(actual);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWrongStringFormat() throws IOException {
        String jsonString = "\"invalid\"";
        mapper.readValue(jsonParserFromString(jsonString), File.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObjectWithInvalidType() throws IOException {
        String jsonString = "{\"type\": \"Invalid\", \"value\": \"123\"}";
        mapper.readValue(jsonParserFromString(jsonString), File.class);
    }

    private JsonParser jsonParserFromString(String jsonString) {
        return mapper.getFactory().createParser(jsonString);
    }
}