package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.NodeType;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.RootNameLookup;
import com.fasterxml.jackson.databind.util.StdConverter;

public class StdKeyDeserializerTest {

 private DeserializationContext ctxt;
 private ValueIntrospector valueIntrospector;
 private AnnotationIntrospector annotationIntrospector;
 private RootNameLookup rootNameLookup;
 private TypeDeserializer typeDeserializer;
 private EnumResolver enumResolver;
 private Converter<String, UUID> uuidConverter;
 private Converter<String, URI> uriConverter;
 private Converter<String, URL> urlConverter;
 private StdKeyDeserializer booleanDeserializer;
 private StdKeyDeserializer byteDeserializer;
 private StdKeyDeserializer shortDeserializer;
 private StdKeyDeserializer charDeserializer;
 private StdKeyDeserializer intDeserializer;
 private StdKeyDeserializer longDeserializer;
 private StdKeyDeserializer floatDeserializer;
 private StdKeyDeserializer doubleDeserializer;
 private StdKeyDeserializer localeDeserializer;
 private StdKeyDeserializer dateDeserializer;
 private StdKeyDeserializer calendarDeserializer;
 private StdKeyDeserializer uuidDeserializer;
 private StdKeyDeserializer uriDeserializer;
 private StdKeyDeserializer urlDeserializer;
 private StdKeyDeserializer classDeserializer;
 private StdKeyDeserializer currencyDeserializer;

 @Before
 public void setup() {
 ctxt = new DeserializationContext(new SerializerProvider(), new RootNameLookup());
 valueIntrospector = valueIntrospector = new ValueIntrospector();
 annotationIntrospector = annotationIntrospector = valueIntrospector.getAnnotationIntrospector();
 rootNameLookup = new RootNameLookup();
 typeDeserializer = new TypeDeserializer();
 enumResolver = new EnumResolver();
 uuidConverter = new StdConverter.StdConverterInstance<>(UUID.class, String.class);
 uriConverter = new StdConverter.StdConverterInstance<>(URI.class, String.class);
 urlConverter = new StdConverter.StdConverterInstance<>(URL.class, String.class);
 }

 @Test
 public void testBooleanDeserializer() throws Exception {
 JavaType booleanType = valueIntrospector.findRootValueType(JavaType.construct(Boolean.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(booleanType.getRawClass());
 assertEquals(Boolean.TRUE, testee.deserializeKey("true", ctxt));
 assertEquals(Boolean.FALSE, testee.deserializeKey("false", ctxt));
 assertNull(testee.deserializeKey("false", ctxt));
 }

 @Test
 public void testByteDeserializer() throws Exception {
 JavaType byteType = valueIntrospector.findRootValueType(JavaType.construct(Byte.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(byteType.getRawClass());
 assertEquals((byte) 1, testee.deserializeKey("1", ctxt));
 assertEquals((byte) 127, testee.deserializeKey("127", ctxt));
 assertEquals((byte) -128, testee.deserializeKey("-128", ctxt));
 assertNull(testee.deserializeKey("129", ctxt));
 }

 @Test
 public void testShortDeserializer() throws Exception {
 JavaType shortType = valueIntrospector.findRootValueType(JavaType.construct(Short.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(shortType.getRawClass());
 assertEquals((short) 1, testee.deserializeKey("1", ctxt));
 assertEquals((short) 32767, testee.deserializeKey("32767", ctxt));
 assertEquals((short) -32768, testee.deserializeKey("-32768", ctxt));
 assertNull(testee.deserializeKey("32769", ctxt));
 }

 @Test
 public void testCharDeserializer() throws Exception {
 JavaType charType = valueIntrospector.findRootValueType(JavaType.construct(Character.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(charType.getRawClass());
 assertEquals('a', testee.deserializeKey("a", ctxt));
 assertEquals('\u0061', testee.deserializeKey("\u0061", ctxt));
 assertNull(testee.deserializeKey("z", ctxt));
 }

 @Test
 public void testIntDeserializer() throws Exception {
 JavaType intType = valueIntrospector.findRootValueType(JavaType.construct(Integer.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(intType.getRawClass());
 assertEquals(1, testee.deserializeKey("1", ctxt));
 assertEquals(Integer.MIN_VALUE, testee.deserializeKey("-2147483648", ctxt));
 assertEquals(Integer.MAX_VALUE, testee.deserializeKey("2147483647", ctxt));
 assertNull(testee.deserializeKey("2147483648", ctxt));
 }

 @Test
 public void testLongDeserializer() throws Exception {
 JavaType longType = valueIntrospector.findRootValueType(JavaType.construct(Long.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(longType.getRawClass());
 assertEquals(1L, testee.deserializeKey("1", ctxt));
 assertEquals(Long.MIN_VALUE, testee.deserializeKey("-9223372036854775808", ctxt));
 assertEquals(Long.MAX_VALUE, testee.deserializeKey("9223372036854775807", ctxt));
 assertNull(testee.deserializeKey("9223372036854775809", ctxt));
 }

 @Test
 public void testFloatDeserializer() throws Exception {
 JavaType floatType = valueIntrospector.findRootValueType(JavaType.construct(Float.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(floatType.getRawClass());
 assertEquals(1.0f, testee.deserializeKey("1.0", ctxt), 0.001f);
 assertEquals(Float.NaN, testee.deserializeKey("NaN", ctxt));
 assertEquals(Float.NEGATIVE_INFINITY, testee.deserializeKey("-Infinity", ctxt));
 assertEquals(Float.POSITIVE_INFINITY, testee.deserializeKey("Infinity", ctxt));
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testDoubleDeserializer() throws Exception {
 JavaType doubleType = valueIntrospector.findRootValueType(JavaType.construct(Double.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(doubleType.getRawClass());
 assertEquals(1.0, testee.deserializeKey("1.0", ctxt), 0.001);
 assertEquals(Double.NaN, testee.deserializeKey("NaN", ctxt));
 assertEquals(Double.NEGATIVE_INFINITY, testee.deserializeKey("-Infinity", ctxt));
 assertEquals(Double.POSITIVE_INFINITY, testee.deserializeKey("Infinity", ctxt));
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testLocaleDeserializer() throws Exception {
 JavaType localeType = valueIntrospector.findRootValueType(JavaType.construct(Locale.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(localeType.getRawClass());
 assertEquals(Locale.ENGLISH, testee.deserializeKey("en_US", ctxt));
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testDateDeserializer() throws Exception {
 JavaType dateType = valueIntrospector.findRootValueType(JavaType.construct(Date.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(dateType.getRawClass());
 assertEquals("Sat Sep 24 16:42:16 EDT 2022", testee.deserializeKey("2022-09-24T16:42:16-0400", ctxt).toString());
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testCalendarDeserializer() throws Exception {
 JavaType calendarType = valueIntrospector.findRootValueType(JavaType.construct(Calendar.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(calendarType.getRawClass());
 Calendar expected = Calendar.getInstance();
 expected.set(Calendar.YEAR, 2022);
 expected.set(Calendar.MONTH, Calendar.SEPTEMBER);
 expected.set(Calendar.DAY_OF_MONTH, 24);
 expected.set(Calendar.HOUR_OF_DAY, 16);
 expected.set(Calendar.MINUTE, 42);
 expected.set(Calendar.SECOND, 16);
 expected.set(Calendar.MILLISECOND, 0);
 assertEquals(expected, testee.deserializeKey("2022-09-24T16:42:16", ctxt));
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testURIDeserializer() throws Exception {
 JavaType uriType = valueIntrospector.findRootValueType(JavaType.construct(URI.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(uriType.getRawClass());
 URI expected = new URI("https://www.example.com/test");
 assertEquals(expected, testee.deserializeKey("https://www.example.com/test", ctxt));
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testURIDeserializerWithSlash() throws Exception {
 JavaType uriType = valueIntrospector.findRootValueType(JavaType.construct(URI.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(uriType.getRawClass());
 URI expected = new URI("/test");
 assertEquals(expected, testee.deserializeKey("/test", ctxt));
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testURIDeserializerWithScheme() throws Exception {
 JavaType uriType = valueIntrospector.findRootValueType(JavaType.construct(URI.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(uriType.getRawClass());
 URI expected = new URI("http://www.example.com/test");
 assertEquals(expected, testee.deserializeKey("http://www.example.com/test", ctxt));
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testURLDeserializer() throws Exception {
 JavaType urlType = valueIntrospector.findRootValueType(JavaType.construct(URL.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(urlType.getRawClass());
 URL expected = new URL("https://www.example.com/test");
 assertEquals(expected, testee.deserializeKey("https://www.example.com/test", ctxt));
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testURLDeserializerWithSlash() throws Exception {
 JavaType urlType = valueIntrospector.findRootValueType(JavaType.construct(URL.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(urlType.getRawClass());
 URL expected = new URL("/test");
 assertEquals(expected, testee.deserializeKey("/test", ctxt));
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testURLDeserializerWithScheme() throws Exception {
 JavaType urlType = valueIntrospector.findRootValueType(JavaType.construct(URL.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(urlType.getRawClass());
 URL expected = new URL("http://www.example.com/test");
 assertEquals(expected, testee.deserializeKey("http://www.example.com/test", ctxt));
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testClassDeserializer() throws Exception {
 JavaType classType = valueIntrospector.findRootValueType(JavaType.construct(String.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(classType.getRawClass());
 assertEquals(String.class, testee.deserializeKey("java.lang.String", ctxt));
 assertNull(testee.deserializeKey("java.util.Date", ctxt));
 }

 @Test
 public void testCurrencyDeserializer() throws Exception {
 JavaType currencyType = valueIntrospector.findRootValueType(JavaType.construct(Currency.class));
 StdKeyDeserializer testee = StdKeyDeserializer.forType(currencyType.getRawClass());
 assertEquals(Currency.getInstance("USD"), testee.deserializeKey("USD", ctxt));
 assertNull(testee.deserializeKey("invalid", ctxt));
 }

 @Test
 public void testNullValueProvider() throws Exception {
 JavaType enumType = valueIntrospector.findRootValueType(JavaType.construct(EnumSet.class));
 StdKeyDeserializer testee = new StdKeyDeserializer.StringFactoryKeyDeserializer(nullValueProviderMethod(enumType));
 assertEquals(EnumSet.noneOf(EnumSet.class), testee.deserializeKey("null", ctxt));
 }

 private Method nullValueProviderMethod(JavaType javaType) throws NoSuchMethodException {
 return annotationIntrospector.findMethodAnnotations(valueIntrospector.findClassAnnotations(javaType.getRawClass())[0], "nullValueProvider")[0]
        .getAnnotatedMember("value");
 }
}