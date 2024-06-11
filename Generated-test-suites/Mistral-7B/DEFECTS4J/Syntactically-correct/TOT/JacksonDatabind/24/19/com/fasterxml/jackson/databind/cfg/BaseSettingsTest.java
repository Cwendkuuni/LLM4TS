package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.VisibilityChecker;
import com.fasterxml.jackson.databind.introspect.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.jsontype.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

 private BaseSettings settings;

 @Before
 public void setUp() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Std();
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new SimpleTypeResolverBuilder();
 DateFormat dateFormat = new StdDateFormat();
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, Locale.getDefault(), TimeZone.getDefault(), null);
 }

 @Test
 public void testGetters() {
 ClassIntrospector gotClassIntrospector = settings.getClassIntrospector();
 AnnotationIntrospector gotAnnotationIntrospector = settings.getAnnotationIntrospector();
 VisibilityChecker<?> gotVisibilityChecker = settings.getVisibilityChecker();
 PropertyNamingStrategy gotPropertyNamingStrategy = settings.getPropertyNamingStrategy();
 TypeFactory gotTypeFactory = settings.getTypeFactory();
 TypeResolverBuilder<?> gotTypeResolverBuilder = settings.getTypeResolverBuilder();
 DateFormat gotDateFormat = settings.getDateFormat();
 HandlerInstantiator gotHandlerInstantiator = settings.getHandlerInstantiator();
 Locale gotLocale = settings.getLocale();
 TimeZone gotTimeZone = settings.getTimeZone();
 Base64Variant gotBase64Variant = settings.getBase64Variant();

 assertTrue(gotClassIntrospector.getClass().equals(ClassIntrospector.class));
 assertTrue(gotAnnotationIntrospector.getClass().equals(AnnotationIntrospector.class));
 assertTrue(gotVisibilityChecker.getClass().equals(VisibilityChecker.Std.class));
 assertTrue(gotPropertyNamingStrategy.getClass().equals(PropertyNamingStrategy.Default.class));
 assertTrue(gotTypeFactory.getClass().equals(TypeFactory.class));
 assertTrue(gotTypeResolverBuilder.getClass().equals(SimpleTypeResolverBuilder.class));
 assertTrue(gotDateFormat.getClass().equals(StdDateFormat.class));
 assertTrue(gotHandlerInstantiator.getClass().equals(HandlerInstantiator.class));
 assertEquals(Locale.getDefault(), gotLocale);
 assertEquals(TimeZone.getDefault(), gotTimeZone);
 assertNull(gotBase64Variant);
 }

 @Test
 public void testWithClassIntrospector() {
 BaseSettings newSettings = settings.withClassIntrospector(new CustomClassIntrospector());
 assertNotSame(settings, newSettings);
 assertSame(settings.getClassIntrospector(), newSettings.getClassIntrospector());
 assertNotSame(newSettings.getClassIntrospector(), settings.getClassIntrospector());
 }

 // Add other test methods for other getters and factory methods here

}