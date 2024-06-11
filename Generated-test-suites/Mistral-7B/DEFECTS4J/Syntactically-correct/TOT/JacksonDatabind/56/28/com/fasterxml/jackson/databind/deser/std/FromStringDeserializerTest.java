package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer<File> fileDeserializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        String fileString = "path/to/myfile.txt";
        File deserializedFile = fileDeserializer.deserialize(mapper.getFactory().createParser(fileString), mapper.getDeserializationContext());
        assertEquals(new File("path/to/myfile.txt"), deserializedFile);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUrl() throws IOException {
        FromStringDeserializer<URL> urlDeserializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        String urlString = "http://example.com";
        URL deserializedUrl = urlDeserializer.deserialize(mapper.getFactory().createParser(urlString), mapper.getDeserializationContext());
        assertEquals(new URL("http://example.com"), deserializedUrl);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeUri() throws IOException {
        FromStringDeserializer<URI> uriDeserializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        String uriString = "http://example.com";
        URI deserializedUri = uriDeserializer.deserialize(mapper.getFactory().createParser(uriString), mapper.getDeserializationContext());
        assertEquals(new URI("http://example.com"), deserializedUri);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer<String> stringDeserializer = (FromStringDeserializer<String>) FromStringDeserializer.findDeserializer(String.class);
        String className = "java.lang.String";
        String value = "Hello World";
        String deserializedValue = stringDeserializer.deserialize(mapper.getFactory().createParser(className), mapper.getDeserializationContext()).getValueAsString();
        assertEquals("Hello World", deserializedValue);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer<TypeReference> typeReferenceDeserializer = (FromStringDeserializer<TypeReference>) FromStringDeserializer.findDeserializer(TypeReference.class);
        String typeString = "com.example.MyClass";
        TypeReference deserializedTypeReference = typeReferenceDeserializer.deserialize(mapper.getFactory().createParser(typeString), mapper.getDeserializationContext());
        assertEquals(new TypeReference<MyClass>() {}, deserializedTypeReference);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer<Currency> currencyDeserializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        String currencyString = "USD";
        Currency deserializedCurrency = currencyDeserializer.deserialize(mapper.getFactory().createParser(currencyString), mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), deserializedCurrency);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer<Pattern> patternDeserializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        String patternString = "(.*)(\\.txt)$";
        Pattern deserializedPattern = patternDeserializer.deserialize(mapper.getFactory().createParser(patternString), mapper.getDeserializationContext());
        assertEquals(Pattern.compile(patternString), deserializedPattern);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer<Locale> localeDeserializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        String localeString = "en_US";
        Locale deserializedLocale = localeDeserializer.deserialize(mapper.getFactory().createParser(localeString), mapper.getDeserializationContext());
        assertEquals(Locale.forLanguageTag("en_US"), deserializedLocale);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer<Charset> charsetDeserializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        String charsetString = "UTF-8";
        Charset deserializedCharset = charsetDeserializer.deserialize(mapper.getFactory().createParser(charsetString), mapper.getDeserializationContext());
        assertEquals(Charset.forName(charsetString), deserializedCharset);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        String timeZoneString = "UTC";
        TimeZone deserializedTimeZone = timeZoneDeserializer.deserialize(mapper.getFactory().createParser(timeZoneString), mapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("UTC"), deserializedTimeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        String ipv4AddressString = "192.168.0.1";
        InetAddress deserializedIpv4Address = inetAddressDeserializer.deserialize(mapper.getFactory().createParser(ipv4AddressString), mapper.getDeserializationContext());
        assertEquals(InetAddress.getByName(ipv4AddressString), deserializedIpv4Address);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String ipv6AddressWithPortString = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:1234";
        InetSocketAddress deserializedIpv6AddressWithPort = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser(ipv6AddressWithPortString), mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]"), 1234), deserializedIpv6AddressWithPort);
    }

    @Test
    public void testDeserializeNull() throws IOException {
        FromStringDeserializer<Object> nullDeserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        String nullString = null;
        Object deserializedNull = nullDeserializer.deserialize(mapper.getFactory().createParser(nullString), mapper.getDeserializationContext());
        assertNull(deserializedNull);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        FromStringDeserializer<Object> emptyStringDeserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        String emptyString = "";
        Object deserializedEmptyString = emptyStringDeserializer.deserialize(mapper.getFactory().createParser(emptyString), mapper.getDeserializationContext());
        assertNull(deserializedEmptyString);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidString() throws IOException {
        FromStringDeserializer<Object> invalidStringDeserializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        String invalidString = "invalid";
        invalidStringDeserializer.deserialize(mapper.getFactory().createParser(invalidString), mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        FromStringDeserializer<FromStringDeserializer> deserializerUnderTest = new FromStringDeserializer<FromStringDeserializer>(FromStringDeserializer.class);
        ObjectMapper objectMapper = new ObjectMapper();
        FromStringDeserializer embeddedDeserializer = deserializerUnderTest;
        String jsonString = "{\"_class\":\"com.fasterxml.jackson.databind.deser.std.FromStringDeserializer\",\"_kind\":1}";
        Object deserializedObject = deserializerUnderTest.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(embeddedDeserializer, deserializedObject);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidEmbeddedObject() throws IOException {
        FromStringDeserializer<FromStringDeserializer> deserializerUnderTest = new FromStringDeserializer<FromStringDeserializer>(FromStringDeserializer.class);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "{\"_class\":\"com.fasterxml.jackson.databind.deser.std.FromStringDeserializer\",\"_kind\":13}";
        deserializerUnderTest.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
    }
}