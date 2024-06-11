package com.fasterxml.jackson.databind.cfg.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.HandlerInstantiator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

 private BaseSettings settings;

 @Before
 public void setup() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.Std();
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new StdTypeResolverBuilder();
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variant.Standard;
 settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);
 }

 @Test
 public void testGetters() {
 assertNotNull(settings.getClassIntrospector());
 assertNotNull(settings.getAnnotationIntrospector());
 assertNotNull(settings.getVisibilityChecker());
 assertNotNull(settings.getPropertyNamingStrategy());
 assertNotNull(settings.getTypeFactory());
 assertNotNull(settings.getTypeResolverBuilder());
 assertNotNull(settings.getDateFormat());
 assertNotNull(settings.getHandlerInstantiator());
 assertNotNull(settings.getLocale());
 assertNotNull(settings.getTimeZone());
 assertNotNull(settings.getBase64Variant());
 }

 @Test
 public void testDateFormat() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.setDateFormat(settings.getDateFormat());
 assertEquals("2022-12-31 23:59:59", mapper.readValue("\"2022-12-31 23:59:59\"", String.class));
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidDateFormat() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.setDateFormat(null);
 mapper.readValue("\"2022-12-31 23:59:59\"", String.class);
 }

 @Test
 public void testChangeDateFormat() {
 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
 settings = settings.withDateFormat(dateFormat);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setDateFormat(settings.getDateFormat());
 assertEquals("2022-12-31 11:59:59 PM", mapper.readValue("\"2022-12-31 11:59:59 PM\"", String.class));
 }
}