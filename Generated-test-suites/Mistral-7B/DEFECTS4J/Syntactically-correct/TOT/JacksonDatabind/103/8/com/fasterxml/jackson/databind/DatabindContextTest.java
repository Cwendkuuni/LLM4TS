package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;
import static org.junit.Assert.*;

public class DatabindContextTest {

 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testGetConfig() {
 DeserializationContext context = objectMapper.getDeserializationContext();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 assertNotNull(context.getConfig());
 assertNotNull(serializerProvider.getConfig());
 }

 @Test
 public void testGetAnnotationIntrospector() {
 DeserializationContext context = objectMapper.getDeserializationContext();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 assertSame(context.getAnnotationIntrospector(), serializerProvider.getAnnotationIntrospector());
 }

 @Test
 public void testIsEnabled() {
 DeserializationContext context = objectMapper.getDeserializationContext();
 MapperFeature feature = MapperFeature.DEFAULT_INTERNAL_DATE_FORMAT;
 assertTrue(context.isEnabled(feature));
 context.disable(feature);
 assertFalse(context.isEnabled(feature));
 }

 @Test
 public void testCanOverrideAccessModifiers() {
 DeserializationContext context = objectMapper.getDeserializationContext();
 SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
 assertTrue(context.canOverrideAccessModifiers());
 assertTrue(serializerProvider.canOverrideAccessModifiers());
}

 @Test
 public void testGetActiveView() {
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertNull(context.getActiveView());
}

 @Test
 public void testGetLocale() {
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(Locale.getDefault(), context.getLocale());
}

 @Test
 public void testGetTimeZone() {
 DeserializationContext context = objectMapper.getDeserializationContext();
 assertEquals(TimeZone.getDefault(), context.getTimeZone());
}

 @Test
 public void testGetDefaultPropertyFormat() {
 DeserializationContext context = objectMapper.getDeserializationContext();
 Class<?> baseType = String.class;
 JsonFormat.Value format = context.getDefaultPropertyFormat(baseType);
 assertNotNull(format);
}

 @Test
 public void testGetAttribute() {
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object key = JsonTest.TEST_STRING_KEY;
 Object value = "testValue";
 context.setAttribute(key, value);
 Object result = context.getAttribute(key);
 assertEquals(value, result);
}

 @Test
 public void testSetAttribute() {
 DeserializationContext context = objectMapper.getDeserializationContext();
 Object key = JsonTest.TEST_STRING_KEY;
 Object value = "testValue";
 Object result = context.setAttribute(key, value);
 assertEquals(context, result);
}

 @Test
 public void testConstructType() throws Exception {
 DeserializationContext context = objectMapper.getDeserializationContext();
 Type type = String[].class;
 JavaType javaType = context.constructType(type);
 assertNotNull(javaType);
 assertEquals(javaType.getRawClass(), String[].class);
}

 @Test
 public void testConstructSpecializedType() throws Exception {
 DeserializationContext context = objectMapper.getDeserializationContext();
 JavaType baseType = String.class;
 Class<?> subclass = StringBuffer.class;
 JavaType javaType = context.constructSpecializedType(baseType, subclass);
 assertNotNull(javaType);
 assertEquals(javaType.getRawClass(), StringBuffer.class);
}

 @Test
 public void testResolveSubType() throws Exception {
 DeserializationContext context = objectMapper.getDeserializationContext();
 JavaType baseType = String.class;
 String subclassName = "java.util.ArrayList";
 JavaType javaType = context.resolveSubType(baseType, subclassName);
 assertNotNull(javaType);
 assertEquals(javaType.getRawClass(), ArrayList.class);
}

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeIdException() throws Exception {
 DeserializationContext context = objectMapper.getDeserializationContext();
 JavaType baseType = String.class;
 String typeId = "java.util.HashMap";
 context.invalidTypeIdException(baseType, typeId, "Not a subtype");
}

 @Test
 public void testObjectIdGeneratorInstance() throws Exception {
 DeserializationContext context = objectMapper.getDeserializationContext();
 Annotated annotated = null;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(JsonTypeInfo.Id.NAME, "id");
 ObjectIdGenerator<?> generator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
 assertNull(generator);
}

 @Test
 public void testObjectIdResolverInstance() throws Exception {
 DeserializationContext context = objectMapper.getDeserializationContext();
 Annotated annotated = null;
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(JsonTypeInfo.Id.NAME, "id");
 ObjectIdResolver resolver = context.objectIdResolverInstance(annotated, objectIdInfo);
 assertNull(resolver);
}

 @Test
 public void testConverterInstance() throws Exception {
 DeserializationContext context = objectMapper.getDeserializationContext();
 Annotated annotated = null;
 Converter<Object,Object> converterDef = StringToIntegerConverter.class;
 Converter<Object,Object> converter = context.converterInstance(annotated, converterDef);
 assertNotNull(converter);
}

 @Test
 public void testReportBadDefinition() throws Exception {
 DeserializationContext context = objectMapper.getDeserializationContext();
 JavaType type = null;
 String msg = "reportBadDefinition with null JavaType";
 try {
 context.reportBadDefinition(type, msg);
 fail();
 } catch (JsonMappingException e) {
 // expected
 assertEquals(msg, e.getMessage());
 }

 TypeIdResolver idResolver = new TypeResolverBuilder().buildTypeResolver(context.getTypeFactory(), TypeIdResolver.Std.class);
 type = context.getTypeFactory().constructType(String[].class);
 msg = "reportBadDefinition with invalid JavaType";
 try {
 context.reportBadDefinition(new Class[] {Integer.TYPE, Integer.TYPE, String[].class}[0], msg);
 fail();
 } catch (JsonMappingException e) {
 // expected
 assertEquals(msg, e.getMessage());
 }

 msg = "reportBadDefinition with null message";
 try {
 context.reportBadDefinition(type, null);
 fail();
 } catch (JsonMappingException e) {
 // expected
 assertNotNull(e.getMessage());
 }

 msg = "reportBadDefinition with empty message";
 try {
 context.reportBadDefinition(type, "");
 fail();
 } catch (JsonMappingException e) {
 // expected
 assertNotNull(e.getMessage());
 }

 msg = "reportBadDefinition with message longer than MAX_ERROR_STR_LEN";
 String longMsg = "This is a test message which is longer than MAX_ERROR_STR_LEN, which is 500 characters";
 try {
 context.reportBadDefinition(type, longMsg);
 fail();
 } catch (JsonMappingException e) {
 // expected
 assertTrue(longMsg.length() > 500);
 String truncatedMsg = e.getMessage();
 assertTrue(truncatedMsg.length() <= 500);
 assertTrue(truncatedMsg.endsWith("..."));
 }
}
}