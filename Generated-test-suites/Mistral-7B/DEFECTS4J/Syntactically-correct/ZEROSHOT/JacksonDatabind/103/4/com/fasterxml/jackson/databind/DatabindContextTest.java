package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ObjectIdInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabindContextTest {

  private ObjectMapper mapper;
  private ObjectReader reader;

  @Before
  public void setUp() {
    mapper = new ObjectMapper();
    reader = mapper.readerForClass(Object.class);
  }

  @After
  public void tearDown() {
    mapper = null;
    reader = null;
  }

  @Test
  public void testGetConfig() {
    DeserializationContext ctx = reader.getContext();
    SerializationConfig config = ctx.getConfig();
    assertNotNull(config);
  }

  @Test
  public void testGetAnnotationIntrospector() {
    DeserializationContext ctx = reader.getContext();
    AnnotationIntrospector ai = ctx.getAnnotationIntrospector();
    assertNotNull(ai);
  }

  @Test
  public void testIsEnabled() {
    DeserializationContext ctx = reader.getContext();
    assertTrue(ctx.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
    SerializationConfig config = ctx.getConfig();
    config.disable(MapperFeature.AUTO_DETECT_TYPES);
    assertFalse(ctx.isEnabled(MapperFeature.AUTO_DETECT_TYPES));
  }

  @Test
  public void testCanOverrideAccessModifiers() {
    DeserializationContext ctx = reader.getContext();
    assertTrue(ctx.canOverrideAccessModifiers());
    SerializationConfig config = ctx.getConfig();
    config.disableAccessModifiers();
    assertFalse(ctx.canOverrideAccessModifiers());
  }

  @Test
  public void testGetActiveView() {
    DeserializationContext ctx = reader.getContext();
    assertNull(ctx.getActiveView());
  }

  @Test
  public void testGetLocale() {
    DeserializationContext ctx = reader.getContext();
    assertEquals(Locale.getDefault(), ctx.getLocale());
  }

  @Test
  public void testGetTimeZone() {
    DeserializationContext ctx = reader.getContext();
    assertEquals(TimeZone.getDefault(), ctx.getTimeZone());
  }

  @Test
  public void testGetDefaultPropertyFormat() {
    DeserializationContext ctx = reader.getContext();
    assertEquals(null, ctx.getDefaultPropertyFormat(Object.class));
  }

  @Test
  public void testGetAttribute() {
    DeserializationContext ctx = reader.getContext();
    ctx.setAttribute("key", "value");
    assertEquals("value", ctx.getAttribute("key"));
    assertEquals(null, ctx.getAttribute(Integer.class));
  }

  @Test
  public void testSetAttribute() {
    DeserializationContext ctx = reader.getContext();
    ctx.setAttribute("key", "value");
    Object obj = ctx.setAttribute("key", "newValue");
    assertEquals("newValue", ctx.getAttribute("key"));
    assertNull(obj);
  }

  @Test
  public void testConstructType() {
    DeserializationContext ctx = reader.getContext();
    Type type = Integer.TYPE;
    JavaType javaType = ctx.constructType(type);
    assertEquals(Integer.class, javaType.getRawClass());
  }

  @Test
  public void testConstructSpecializedType() {
    DeserializationContext ctx = reader.getContext();
    JavaType baseType = ctx.getTypeFactory().constructType(Integer.class);
    JavaType subType = ctx.constructSpecializedType(baseType, String.class);
    assertFalse(baseType.isTypeOrSubTypeOf(subType));
    assertTrue(subType.isSubTypeOf(baseType));
  }

  @Test(expected = JsonMappingException.class)
  public void testResolveSubTypeInvalidTypeId() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    JavaType baseType = ctx.getTypeFactory().constructType(Integer.class);
    ctx.resolveSubType(baseType, "java.util.Date");
  }

  @Test(expected = JsonMappingException.class)
  public void testResolveSubTypeInvalidTypeIdWithCanonicalName() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    JavaType baseType = ctx.getTypeFactory().constructType(Integer.class);
    ctx.resolveSubType(baseType, "java.util.Date");
  }

  @Test
  public void testResolveSubTypeValidTypeId() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    JavaType baseType = ctx.getTypeFactory().constructType(Integer.class);
    JavaType subType = ctx.resolveSubType(baseType, "java.util.Integer");
    assertTrue(subType.isSubTypeOf(baseType));
  }

  @Test
  public void testInvalidTypeIdException() {
    DeserializationContext ctx = reader.getContext();
    JavaType baseType = ctx.getTypeFactory().constructType(Integer.class);
    assertNull(ctx.invalidTypeIdException(baseType, "java.util.Date", ""));
  }

  @Test(expected = JsonMappingException.class)
  public void testInvalidTypeIdExceptionInvalidTypeId() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    JavaType baseType = ctx.getTypeFactory().constructType(Integer.class);
    ctx.invalidTypeIdException(baseType, "java.util.Date", "");
  }

  @Test
  public void testGetTypeFactory() {
    DeserializationContext ctx = reader.getContext();
    TypeFactory tf = ctx.getTypeFactory();
    assertNotNull(tf);
  }

  @Test
  public void testObjectIdGeneratorInstance() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(reader.forType(String[].class), ObjectIdGenerator.Value.class);
    ObjectIdGenerator<?> gen = ctx.objectIdGeneratorInstance(new AnnotatedClass(String[].class), objectIdInfo);
    assertNotNull(gen);
  }

  @Test
  public void testObjectIdResolverInstance() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(reader.forType(String[].class), ObjectIdResolver.Standalone.class);
    ObjectIdResolver resolver = ctx.objectIdResolverInstance(new AnnotatedClass(String[].class), objectIdInfo);
    assertNotNull(resolver);
  }

  @Test
  public void testConverterInstance() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    AnnotatedClass annotated = new AnnotatedClass(String.class);
    JsonTypeInfo.Id annotation = new JsonTypeInfo.Id() {{ setClass(StringConverter.class); }};
    Converter<String, String> converter = ctx.converterInstance(annotated, annotation);
    assertNotNull(converter);
  }

  @Test(expected = JsonMappingException.class)
  public void testReportBadDefinitionInvalidType() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    JavaType javaType = ctx.reportBadDefinition(null, "");
    fail("Expected JsonMappingException");
  }

  @Test(expected = JsonMappingException.class)
  public void testReportBadDefinitionInvalidTypeWithMessage() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    JavaType javaType = ctx.reportBadDefinition(null, "message");
    fail("Expected JsonMappingException");
  }

  @Test(expected = JsonMappingException.class)
  public void testReportBadDefinitionInvalidTypeWithMessageAndArgs() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    JavaType javaType = ctx.reportBadDefinition(null, "msg: {0} {1}", "arg1", "arg2");
    fail("Expected JsonMappingException");
  }

  @Test
  public void testReportBadDefinitionValidType() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    JavaType javaType = ctx.reportBadDefinition(ctx.getTypeFactory().constructType(String.class), "");
    assertNotNull(javaType);
  }

  @Test
  public void testReportBadDefinitionValidTypeWithMessage() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    JavaType javaType = ctx.reportBadDefinition(ctx.getTypeFactory().constructType(String.class), "message");
    assertNotNull(javaType);
  }

  @Test
  public void testReportBadDefinitionValidTypeWithMessageAndArgs() throws JsonMappingException {
    DeserializationContext ctx = reader.getContext();
    JavaType javaType = ctx.reportBadDefinition(ctx.getTypeFactory().constructType(String.class), "msg: {0} {1}", "arg1", "arg2");
    assertNotNull(javaType);
  }

  @Test
  public void testFormat() {
    DeserializationContext ctx = reader.getContext();
    String msg = ctx._format("msg: {0} {1}", "arg1", "arg2");
    assertEquals("msg: arg1 arg2", msg);
  }

  @Test
  public void testTruncate() {
    DeserializationContext ctx = reader.getContext();
    String desc = "very long description";
    String truncated = ctx._truncate(desc);
    assertTrue(truncated.endsWith("]..."));
  }

  @Test
  public void testQuotedString() {
    DeserializationContext ctx = reader.getContext();
    String desc = "string with 'single quotes' and \"double quotes\"";
    String quoted = ctx._quotedString(desc);
    assertTrue(quoted.startsWith("\"") && quoted.endsWith("\"") && quoted.contains("'") && quoted.contains("\""));
  }

  @Test
  public void testColonConcat() {
    DeserializationContext ctx = reader.getContext();
    String msgBase = "msgBase:";
    String extra = "extra";
    String concat = ctx._colonConcat(msgBase, extra);
    assertEquals(msgBase + ": extra", concat);
  }

  @Test
  public void testDesc() {
    DeserializationContext ctx = reader.getContext();
    String desc = "desc";
    String description = ctx._desc(desc);
    assertEquals(desc, description);
  }
}