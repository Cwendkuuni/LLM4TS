package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

 private ObjectMapper objectMapper;
 private DatabindContext databindContext;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 databindContext = objectMapper.getDeserializationContext();
 }

 @Test
 public void testGetConfig() {
 MapperConfig<?> config = databindContext.getConfig();
 Assert.assertNotNull(config);
 }

 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector annotationIntrospector = databindContext.getAnnotationIntrospector();
 Assert.assertNotNull(annotationIntrospector);
 }

 @Test
 public void testIsEnabled() {
 Assert.assertTrue(databindContext.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODifiers));
 Assert.assertFalse(databindContext.isEnabled(MapperFeature.DEFAULT_USE_STATIC_TYPING));
 }

 @Test
 public void testCanOverrideAccessModifiers() {
 Assert.assertTrue(databindContext.canOverrideAccessModifiers());
 }

 @Test
 public void testGetActiveView() {
 Class<?> activeView = databindContext.getActiveView();
 Assert.assertNull(activeView);
 }

 @Test
 public void testGetLocale() {
 Locale locale = databindContext.getLocale();
 Assert.assertEquals(Locale.getDefault(), locale);
 }

 @Test
 public void testGetTimeZone() {
 TimeZone timeZone = databindContext.getTimeZone();
 Assert.assertEquals(TimeZone.getDefault(), timeZone);
 }

 @Test
 public void testGetDefaultPropertyFormat() {
 Class<?> baseType = String.class;
 JsonFormat.Value defaultPropertyFormat = databindContext.getDefaultPropertyFormat(baseType);
 Assert.assertNotNull(defaultPropertyFormat);
 }

 @Test
 public void testGetAttribute() {
 Object key = "test";
 Object value = "testValue";
 databindContext.setAttribute(key, value);
 Object attribute = databindContext.getAttribute(key);
 Assert.assertEquals(value, attribute);
 }

 @Test
 public void testConstructType() {
 Type type = Object.class;
 JavaType javaType = databindContext.getTypeFactory().constructType(type);
 Assert.assertNotNull(javaType);
 }

 @Test
 public void testConstructSpecializedType() {
 JavaType baseType = JavaType.construct();
 Class<?> subclass = String[].class;
 JavaType specializedType = databindContext.constructSpecializedType(baseType, subclass);
 Assert.assertNotNull(specializedType);
 }

 @Test
 public void testResolveSubType() throws JsonMappingException {
 JavaType baseType = JavaType.construct();
 String subClass = "java.util.ArrayList";
 JavaType resolvedType = databindContext.resolveSubType(baseType, subClass);
 Assert.assertNotNull(resolvedType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeIdException() throws JsonMappingException {
 JavaType baseType = JavaType.construct();
 String typeId = "InvalidType";
 databindContext.invalidTypeIdException(baseType, typeId, "Error Message");
 }

 @Test
 public void testgetTypeFactory() {
 TypeFactory typeFactory = databindContext.getTypeFactory();
 Assert.assertNotNull(typeFactory);
 }

 @Test
 public void testObjectIdGeneratorInstance() throws JsonMappingException {
 Annotated annotated = new AnnotatedClassInfo(JsonAutoDetect.None.class, null, null);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
 ObjectIdGenerator<?> objectIdGenerator = databindContext.objectIdGeneratorInstance(annotated, objectIdInfo);
 Assert.assertNotNull(objectIdGenerator);
 }

 @Test
 public void testObjectIdResolverInstance() {
 Annotated annotated = new AnnotatedClassInfo(JsonSubTypes.with(JsonSubTypes.Type.STATIC).value(Child.class), null, null);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null);
 ObjectIdResolver objectIdResolver = databindContext.objectIdResolverInstance(annotated, objectIdInfo);
 Assert.assertNotNull(objectIdResolver);
 }

 @Test
 public void testConverterInstance() throws JsonMappingException {
 Annotated annotated = new AnnotatedClassInfo(String[].class, null, null);
 Object converterDef = StringToIntConverter.class;
 Converter<Object, Object> converter = databindContext.converterInstance(annotated, converterDef);
 Assert.assertNotNull(converter);
 }

 @Test
 public void testReportBadDefinition() throws JsonMappingException {
 JavaType type = JavaType.construct();
 String msg = "Bad definition";
 Object badDefinition = databindContext.reportBadDefinition(type, msg);
 Assert.assertNull(badDefinition);
 }

 @Test
 public void testReportBadDefinitionWithClass() throws JsonMappingException {
 Class<?> type = String[].class;
 String msg = "Bad definition";
 Object badDefinition = databindContext.reportBadDefinition(type, msg);
 Assert.assertNull(badDefinition);
 }

}