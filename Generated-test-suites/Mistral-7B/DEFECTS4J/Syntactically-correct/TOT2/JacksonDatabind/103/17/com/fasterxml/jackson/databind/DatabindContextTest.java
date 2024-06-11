package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

 private DatabindContext context;

 @Before
 public void setup() {
 MapperFactory factory = new MapperFactory();
 MapperConfig config = factory.getConfig();
 context = config.getInstance(DatabindContext.class);
 }

 @Test
 public void testGetConfig() {
 MapperConfig<?> config = context.getConfig();
 assertNotNull(config);
 }

 @Test
 public void testIsEnabled() {
 assertTrue(context.isEnabled(MapperFeature.DEFAULT_INT_CREATOR));
 assertFalse(context.isEnabled(MapperFeature.DEFAULT_LOCALE_CREATOR));
 }

 @Test
 public void testGetActiveView() {
 assertNull(context.getActiveView());
 }

 @Test
 public void testGetLocale() {
 Locale locale = context.getLocale();
 assertNotNull(locale);
 }

 @Test
 public void testGetTimeZone() {
 TimeZone timeZone = context.getTimeZone();
 assertNotNull(timeZone);
 }

 @Test
 public void testGetDefaultPropertyFormat() {
 Class<String> baseType = String.class;
 JsonFormat.Value format = context.getDefaultPropertyFormat(baseType);
 assertNotNull(format);
 }

 @Test
 public void testGetAttribute() {
 Object attribute = context.getAttribute("testKey");
 assertNull(attribute);

 context.setAttribute("testKey", "testValue");
 Object attribute2 = context.getAttribute("testKey");
 assertEquals("testValue", attribute2);
 }

 @Test
 public void testConstructType() {
 Type type = new TypeReference<String[]>() {}.getType();
 JavaType javaType = context.constructType(type);
 assertTrue(javaType.isArrayType());
 JavaType elementType = javaType.getComponentType();
 assertEquals(String.class, elementType.getRawClass());
 }

 @Test
 public void testResolveSubType() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 JavaType subType = context.resolveSubType(baseType, "java.util.ArrayList");
 assertTrue(subType.isCollectionType());
 JavaType collectionElementType = subType.getContentType();
 assertEquals(Object.class, collectionElementType.getRawClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeIdException() throws JsonMappingException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 String typeId = "InvalidTypeId";
 context.invalidTypeIdException(baseType, typeId, "Test");
 }

 @Test
 public void testReportBadDefinition() {
 try {
 context.reportBadDefinition(ObjectNode.class, "Test");
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
}

 @Test
 public void testObjectIdGeneratorInstance() throws JsonMappingException {
 Annotated annotated = Mockito.mock(Annotated.class);
 ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 ObjectIdGenerator<?> gen = context.objectIdGeneratorInstance(annotated, objectIdInfo);
 assertNotNull(gen);
 }

 @Test
 public void testObjectIdResolverInstance() {
 Annotated annotated = Mockito.mock(Annotated.class);
 ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 ObjectIdResolver resolver = context.objectIdResolverInstance(annotated, objectIdInfo);
 assertNotNull(resolver);
 }

 @Test
 public void testConverterInstance() throws JsonMappingException {
 Annotated annotated = Mockito.mock(Annotated.class);
 Object converterDef = StringToIntegerConverter.class;
 Converter<Object,Object> conv = context.converterInstance(annotated, converterDef);
 assertNotNull(conv);
 }

 @Test
 public void testReportBadDefinition() throws JsonMappingException {
 JavaType type = context.getTypeFactory().constructType(ObjectNode.class);
 context.reportBadDefinition(type, "Test");
 }
}