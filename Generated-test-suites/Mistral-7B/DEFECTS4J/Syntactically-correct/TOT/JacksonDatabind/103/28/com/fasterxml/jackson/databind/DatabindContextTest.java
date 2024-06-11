package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabindContextTest {

 private DatabindContext context;

 @Before
 public void setUp() {
 MapperConfig<?> config = new MapperConfig.Std();
 context = config.getDefaultInstance();
 }

 @Test
 public void testGetConfig() {
 MapperConfig<?> config = context.getConfig();
 assertNotNull(config);
 assertEquals(MapperConfig.Std.class, config.getClass());
 }

 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector introspector = context.getAnnotationIntrospector();
 assertNotNull(introspector);
 }

 @Test
 public void testIsEnabled_MapperFeature() {
 assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_SERIALIZABLE));
 assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
 }

 @Test
 public void testCanOverrideAccessModifiers() {
 assertTrue(context.canOverrideAccessModifiers());
 }

 @Test
 public void testGetActiveView() {
 assertNull(context.getActiveView());
 }

 @Test
 public void testGetLocale() {
 assertEquals(Locale.getDefault(), context.getLocale());
 }

 @Test
 public void testGetTimeZone() {
 assertEquals(TimeZone.getDefault(), context.getTimeZone());
 }

 @Test
 public void testGetDefaultPropertyFormat() {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 JsonFormat.Value format = context.getDefaultPropertyFormat(baseType);
 assertNotNull(format);
 }

 @Test
 public void testGetAttribute() {
 context.setAttribute("test", "testValue");
 Object attribute = context.getAttribute("test");
 assertEquals("testValue", attribute);
 }

 @Test
 public void testConstructType() {
 Type type = Integer.TYPE;
 JavaType javaType = context.constructType(type);
 assertEquals(javaType.getRawClass(), Integer.class);
 }

 @Test
 public void testConstructSpecializedType() {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 Class<?> subclass = Integer.class;
 JavaType specializedType = context.constructSpecializedType(baseType, subclass);
 assertEquals(specializedType.getRawClass(), Integer.class);
 }

 @Test(expected = JsonMappingException.class)
 public void testResolveSubType_Invalid() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 String subClass = "java.util.ArrayList<String>";
 context.resolveSubType(baseType, subClass);
 fail("Should throw exception");
 }

 @Test
 public void testInvalidTypeIdException() {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 String typeId = "com.example.MyClass";
 try {
 context.invalidTypeIdException(baseType, typeId, null);
 fail("Should throw exception");
 } catch (JsonMappingException e) {
 // expected
 }
}

 @Test
 public void testTypeFactory() {
 TypeFactory typeFactory = context.getTypeFactory();
 assertEquals(TypeFactory.class, typeFactory.getClass());
 }

 @Test
 public void testObjectIdGeneratorInstance() throws JsonMappingException {
 Annotated annotated = new AnnotatedClassInfo(ObjectIdGenerator.class, null, null, null);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(context.getAnnotationIntrospector(), annotated);
 ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
 assertNotNull(objectIdGenerator);
 }

 @Test
 public void testObjectIdResolverInstance() {
 Annotated annotated = new AnnotatedClassInfo(ObjectIdResolver.class, null, null, null);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, annotated);
 ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
 assertNotNull(objectIdResolver);
 }

 @Test
 public void testConverterInstance() throws JsonMappingException {
 Annotated annotated = new AnnotatedClassInfo(StringToIntegerConverter.class, null, null, null);
 Object converterDef = StringToIntegerConverter.class;
 Converter<Object,Object> converter = context.converterInstance(annotated, converterDef);
 assertNotNull(converter);
 }

 @Test
 public void testReportBadDefinition() throws JsonMappingException {
 JavaType type = context.getTypeFactory().constructType(Integer.class);
 String msg = "Invalid definition for type %s";
 assertNull(context.reportBadDefinition(type, msg));
 }
}