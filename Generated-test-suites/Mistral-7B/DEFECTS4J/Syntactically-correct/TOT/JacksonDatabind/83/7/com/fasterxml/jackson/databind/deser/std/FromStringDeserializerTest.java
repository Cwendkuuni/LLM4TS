package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        String jsonString = "\"C:\\test\\file.txt\"";
        File expectedFile = new File("C:\\test\\file.txt");
        File deserializedFile = fileDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedFile, deserializedFile);
    }

    @Test
    public void testURLDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        String jsonString = "\"http://www.example.com/\"";
        URL expectedURL = new URL("http://www.example.com/");
        URL deserializedURL = urlDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedURL, deserializedURL);
    }

    @Test
    public void testURIDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        String jsonString = "\"http://www.example.com/\"";
        URI expectedURI = new URI("http://www.example.com/");
        URI deserializedURI = uriDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedURI, deserializedURI);
    }

    @Test
    public void testClassDeserialization() throws JsonMappingException, InstantiationException, IllegalAccessException, JsonProcessingException {
        FromStringDeserializer classDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String jsonString = "\"java.lang.String\"";
        Class<?> expectedClass = String.class;
        Class<?> deserializedClass = classDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedClass, deserializedClass);
    }

    @Test
    public void testCurrencyDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String jsonString = "\"USD\"";
        Currency expectedCurrency = Currency.getInstance("USD");
        Currency deserializedCurrency = currencyDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedCurrency, deserializedCurrency);
    }

    @Test
    public void testPatternDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String jsonString = "/[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}/";
        Pattern expectedPattern = Pattern.compile(jsonString);
        Pattern deserializedPattern = patternDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedPattern, deserializedPattern);
    }

    @Test
    public void testLocaleDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String jsonString = "\"en_US\"";
        Locale expectedLocale = Locale.US;
        Locale deserializedLocale = localeDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedLocale, deserializedLocale);

        jsonString = "\"it_IT\"_\"Europe\"";
        expectedLocale = new Locale("it", "IT", "Europe");
        deserializedLocale = localeDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedLocale, deserializedLocale);
    }

    @Test
    public void testCharsetDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String jsonString = "\"UTF-8\"";
        Charset expectedCharset = Charset.forName("UTF-8");
        Charset deserializedCharset = charsetDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedCharset, deserializedCharset);
    }

    @Test
    public void testTimeZoneDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String jsonString = "\"America/Los_Angeles\"";
        TimeZone expectedTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        TimeZone deserializedTimeZone = timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedTimeZone, deserializedTimeZone);
    }

    @Test
    public void testInetAddressDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String jsonString = "\"192.168.1.1\"";
        InetAddress expectedInetAddress = InetAddress.getByName("192.168.1.1");
        InetAddress deserializedInetAddress = inetAddressDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedInetAddress, deserializedInetAddress);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String jsonString = "[\"192.168.1.1\", 80]";
        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress("192.168.1.1", 80);
        InetSocketAddress deserializedInetSocketAddress = inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedInetSocketAddress, deserializedInetSocketAddress);

        jsonString = "[\"::1\", 80]";
        expectedInetSocketAddress = new InetSocketAddress("::1", 80);
        deserializedInetSocketAddress = inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedInetSocketAddress, deserializedInetSocketAddress);
    }

    @Test
    public void testStringBuilderDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        String jsonString = "\"\"";
        StringBuilder expectedStringBuilder = new StringBuilder();
        StringBuilder deserializedStringBuilder = stringBuilderDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedStringBuilder, deserializedStringBuilder);

        jsonString = "\"abcdefg\"";
        expectedStringBuilder = new StringBuilder("abcdefg");
        deserializedStringBuilder = stringBuilderDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expectedStringBuilder, deserializedStringBuilder);
    }

    @Test
    public void testEmptyStringDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String jsonString = "\"\"";
        String expectedString = null;
        String deserializedString = stringDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext()).getText();
        assertNull(deserializedString);
        assertEquals(expectedString, deserializedString);
    }

    @Test
    public void testInvalidDeserialization() throws JsonMappingException, JsonProcessingException {
        FromStringDeserializer stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String jsonString = "\"invalid\"";
        try {
            stringDeserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}