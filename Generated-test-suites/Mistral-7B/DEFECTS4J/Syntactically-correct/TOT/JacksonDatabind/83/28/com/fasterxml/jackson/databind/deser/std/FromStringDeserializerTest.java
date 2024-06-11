package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws IOException, URISyntaxException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"path/to/file.txt\"");
        Std.StdFile fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File deserializedObject = fileDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals("path/to/file.txt", deserializedObject.getAbsolutePath());
    }

    @Test
    public void testURLDeserialization() throws IOException, URISyntaxException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"http://www.example.com/\"");
        Std.StdURL urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL deserializedObject = urlDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals("http://www.example.com/", deserializedObject.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException, URISyntaxException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"http://www.example.com/path/to/file.txt\"");
        Std.StdURI uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI deserializedObject = uriDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals("http://www.example.com/path/to/file.txt", deserializedObject.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException, InstantiationException, IllegalAccessException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"java.lang.String\"");
        Std.StdClass classDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String deserializedObject = (String) classDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals("java.lang.String", deserializedObject.getClass().getName());
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"com.example.MyClass\"");
        Std.StdJavaType javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType deserializedObject = javaTypeDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals("com.example.MyClass", deserializedObject.getTypeName());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"USD\"");
        Std.StdCurrency currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency deserializedObject = currencyDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals(Currency.getInstance("USD"), deserializedObject);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"/[a-z]+\\.txt$/\"");
        Std.StdPattern patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern deserializedObject = patternDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertTrue(deserializedObject.matcher("example.txt").matches());
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"en_US\"");
        Std.StdLocale localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale deserializedObject = localeDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals("en_US", deserializedObject.toString());
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"UTF-8\"");
        Std.StdCharset charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset deserializedObject = charsetDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals("UTF-8", deserializedObject.name());
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"America/Los_Angeles\"");
        Std.StdTimeZone timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone deserializedObject = timeZoneDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals("America/Los_Angeles", deserializedObject.getId());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"192.168.1.1\"");
        Std.StdInetAddress inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress deserializedObject = inetAddressDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals("192.168.1.1", deserializedObject.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("[\"192.168.1.1\", 80]");
        Std.StdInetSocketAddress inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress deserializedObject = inetSocketAddressDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals("192.168.1.1", deserializedObject.getAddress().getHostAddress());
        Assert.assertEquals(80, deserializedObject.getPort());
    }

    @Test
    public void testStringBuilderDeserialization() throws IOException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"Hello World\"");
        Std.StdStringBuilder stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder deserializedObject = stringBuilderDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals("Hello World", deserializedObject.toString());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"\"");
        Std.StdFile fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File deserializedObject = fileDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertNull(deserializedObject);
    }

    @Test
    public void testInvalidFormatException() throws IOException {
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"InvalidFormatException\"");
        Std.StdClass invalidFormatExceptionDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String deserializedObject = (String) invalidFormatExceptionDeserializer.deserialize(parser, mapper.getDeserializationContext());
        Assert.assertEquals("InvalidFormatException", deserializedObject);
    }
}