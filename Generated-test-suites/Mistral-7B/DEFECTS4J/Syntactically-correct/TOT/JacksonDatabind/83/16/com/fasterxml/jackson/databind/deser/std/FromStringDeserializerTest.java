package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.nio.charset.Charset;
import java.util.TimeZone;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.lang.StringBuilder;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    public FromStringDeserializerTest() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFileDeserialization() throws Exception {
        Std fileStd = (Std) FromStringDeserializer.findDeserializer(File.class);
        File file = new File("test.txt");
        String json = mapper.writerWithRootValue(file).writeValueAsString(file);
        T fileValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), fileStd.getValueClass());
        Assert.assertEquals(file, fileValue);
    }

    @Test
    public void testURLDeserialization() throws Exception {
        Std urlStd = (Std) FromStringDeserializer.findDeserializer(URL.class);
        URL url = new URL("http://www.example.com");
        String json = mapper.writerWithRootValue(url).writeValueAsString(url);
        T urlValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), urlStd.getValueClass());
        Assert.assertEquals(url, urlValue);
    }

    @Test
    public void testURIDeserialization() throws Exception {
        Std uriStd = (Std) FromStringDeserializer.findDeserializer(URI.class);
        URI uri = new URI("http://www.example.com");
        String json = mapper.writerWithRootValue(uri).writeValueAsString(uri);
        T uriValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), uriStd.getValueClass());
        Assert.assertEquals(uri, uriValue);
    }

    @Test
    public void testClassDeserialization() throws Exception {
        Std classStd = (Std) FromStringDeserializer.findDeserializer(String.class);
        String className = String.class.getName();
        String json = mapper.writerWithRootValue(className).writeValueAsString(className);
        T classValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), classStd.getValueClass());
        Assert.assertEquals(className, classValue.getClass().getName());
    }

    @Test
    public void testJavaTypeDeserialization() throws Exception {
        Std javaTypeStd = (Std) FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        String json = mapper.writerWithRootValue(javaType.getClass()).writeValueAsString(javaType);
        T javaTypeValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), javaTypeStd.getValueClass());
        Assert.assertEquals(javaType, javaTypeValue);
    }

    @Test
    public void testCurrencyDeserialization() throws Exception {
        Std currencyStd = (Std) FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = Currency.getInstance("EUR");
        String json = mapper.writerWithRootValue(currency).writeValueAsString(currency);
        T currencyValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), currencyStd.getValueClass());
        Assert.assertEquals(currency, currencyValue);
    }

    @Test
    public void testPatternDeserialization() throws Exception {
        Std patternStd = (Std) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern pattern = Pattern.compile("^\\w+@\\w+\\.(\\w{2,3})(\\.\\w{2})+$");
        String json = mapper.writerWithRootValue(pattern).writeValueAsString(pattern);
        T patternValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), patternStd.getValueClass());
        Assert.assertEquals(pattern, patternValue);
    }

    @Test
    public void testLocaleDeserialization() throws Exception {
        Std localeStd = (Std) FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = new Locale("en", "US");
        String json = mapper.writerWithRootValue(locale).writeValueAsString(locale);
        T localeValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json),
                localeStd.getValueClass());
        Assert.assertEquals(locale, localeValue);
    }

    @Test
    public void testCharsetDeserialization() throws Exception {
        Std charsetStd = (Std) FromStringDeserializer.findDeserializer(Charset.class);
        Charset charset = Charset.forName("UTF-8");
        String json = mapper.writerWithRootValue(charset).writeValueAsString(charset.name());
        T charsetValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), charsetStd.getValueClass());
        Assert.assertEquals(charset, Charset.forName((String) charsetValue));
    }

    @Test
    public void testTimeZoneDeserialization() throws Exception {
        Std timeZoneStd = (Std) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        String json = mapper.writerWithRootValue(timeZone).writeValueAsString(timeZone.getID());
        T timeZoneValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), timeZoneStd.getValueClass());
        Assert.assertEquals(timeZone, timeZoneValue);
    }

    @Test
    public void testInetAddressDeserialization() throws Exception {
        Std inetAddressStd = (Std) FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
        String json = mapper.writerWithRootValue(inetAddress).writeValueAsString(inetAddress.getHostAddress());
        T inetAddressValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), inetAddressStd.getValueClass());
        Assert.assertEquals(inetAddress, inetAddressValue);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws Exception {
        Std inetSocketAddressStd = (Std) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
        String json = mapper.writerWithRootValue(inetSocketAddress).writeValueAsString(inetSocketAddress.toString());
        T inetSocketAddressValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), inetSocketAddressStd.getValueClass());
        Assert.assertEquals(inetSocketAddress, inetSocketAddressValue);
    }

    @Test
    public void testStringBuilderDeserialization() throws Exception {
        Std stringBuilderStd = (Std) FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder stringBuilder = new StringBuilder();
        String json = mapper.writerWithRootValue(stringBuilder).writeValueAsString(stringBuilder.toString());
        T stringBuilderValue = mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), stringBuilderStd.getValueClass());
        Assert.assertEquals(stringBuilder, stringBuilderValue);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidFromStringDeserialization() throws Exception {
        Std stringStd = (Std) FromStringDeserializer.findDeserializer(String.class);
        String json = "\"InvalidJSON\"";
        mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), stringStd.getValueClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testNullFromStringDeserialization() throws Exception {
        Std nullStd = (Std) FromStringDeserializer.findDeserializer(String.class);
        String json = null;
        mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), nullStd.getValueClass());
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidURLDeserialization() throws Exception {
        Std urlStd = (Std) FromStringDeserializer.findDeserializer(URL.class);
        String json = "\"invalid:url\"";
        mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), urlStd.getValueClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleDeserialization() throws Exception {
        Std localeStd = (Std) FromStringDeserializer.findDeserializer(Locale.class);
        String json = "\"InvalidLocale\"";
        mapper.readValue(JsonModule.getDefaultInstance().getSerializerProvider().getSerializerProvider().textParser(json), localeStd.getValueClass());
    }

}