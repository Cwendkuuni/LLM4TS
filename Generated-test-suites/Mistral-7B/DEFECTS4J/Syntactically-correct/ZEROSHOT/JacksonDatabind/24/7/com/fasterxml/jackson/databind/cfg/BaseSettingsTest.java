package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

 private BaseSettings baseSettings;

 @Test
 public void testBaseSettingsConstruction() {
 BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker.Std(),
 PropertyNamingStrategy.DEFAULT, new TypeFactory(), new TypeResolverBuilderDefault(), new StdDateFormat(),
 new HandlerInstantiatorDefault(), Locale.getDefault(), TimeZone.getDefault(), Base64Variants.US_ASCII);
 assertNotNull(settings);
 }

 @Test
 public void testSettersAndGetters() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Std();
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new StdDateFormat();
 HandlerInstantiator handlerInstantiator = new HandlerInstantiatorDefault();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 Base64Variant base64Variant = Base64Variants.US_ASCII;

 baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, base64Variant);

 ClassIntrospector actualClassIntrospector = baseSettings.getClassIntrospector();
 assertEquals(classIntrospector, actualClassIntrospector);

 AnnotationIntrospector actualAnnotationIntrospector = baseSettings.getAnnotationIntrospector();
 assertEquals(annotationIntrospector, actualAnnotationIntrospector);

 VisibilityChecker<?> actualVisibilityChecker = baseSettings.getVisibilityChecker();
 assertEquals(visibilityChecker, actualVisibilityChecker);

 PropertyNamingStrategy actualPropertyNamingStrategy = baseSettings.getPropertyNamingStrategy();
 assertEquals(propertyNamingStrategy, actualPropertyNamingStrategy);

 TypeFactory actualTypeFactory = baseSettings.getTypeFactory();
 assertEquals(typeFactory, actualTypeFactory);

 TypeResolverBuilder<?> actualTypeResolverBuilder = baseSettings.getTypeResolverBuilder();
 assertEquals(typeResolverBuilder, actualTypeResolverBuilder);

 DateFormat actualDateFormat = baseSettings.getDateFormat();
 assertEquals(dateFormat, actualDateFormat);

 HandlerInstantiator actualHandlerInstantiator = baseSettings.getHandlerInstantiator();
 assertEquals(handlerInstantiator, actualHandlerInstantiator);

 Locale actualLocale = baseSettings.getLocale();
 assertEquals(locale, actualLocale);

 TimeZone actualTimeZone = baseSettings.getTimeZone();
 assertEquals(timeZone, actualTimeZone);

 Base64Variant actualBase64Variant = baseSettings.getBase64Variant();
 assertEquals(base64Variant, actualBase64Variant);
 }

 @Test
 public void testWithClassIntrospector() throws Exception {
 ClassIntrospector newClassIntrospector = new ClassIntrospector();
 baseSettings = baseSettings.withClassIntrospector(newClassIntrospector);
 ClassIntrospector actualClassIntrospector = baseSettings.getClassIntrospector();
 assertEquals(newClassIntrospector, actualClassIntrospector);
 }

 @Test
 public void testWithAnnotationIntrospector() throws Exception {
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
 baseSettings = baseSettings.withAnnotationIntrospector(newAnnotationIntrospector);
 AnnotationIntrospector actualAnnotationIntrospector = baseSettings.getAnnotationIntrospector();
 assertEquals(newAnnotationIntrospector, actualAnnotationIntrospector);
 }

 @Test
 public void testWithInsertedAnnotationIntrospector() throws Exception {
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
 AnnotationIntrospector existingAnnotationIntrospector = baseSettings.getAnnotationIntrospector();
 baseSettings = baseSettings.withInsertedAnnotationIntrospector(newAnnotationIntrospector);
 AnnotationIntrospector actualAnnotationIntrospector = baseSettings.getAnnotationIntrospector();
 AnnotationIntrospectorPair expectedAnnotationIntrospectorPair = AnnotationIntrospectorPair.create(newAnnotationIntrospector, existingAnnotationIntrospector);
 AnnotationIntrospectorPair actualAnnotationIntrospectorPair = AnnotationIntrospectorPair.create(baseSettings.getAnnotationIntrospector(), baseSettings._annotationIntrospector);
 assertEquals(expectedAnnotationIntrospectorPair, actualAnnotationIntrospectorPair);
 }

 @Test
 public void testWithAppendedAnnotationIntrospector() throws Exception {
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
 AnnotationIntrospector existingAnnotationIntrospector = baseSettings.getAnnotationIntrospector();
 baseSettings = baseSettings.withAppendedAnnotationIntrospector(newAnnotationIntrospector);
 AnnotationIntrospector actualAnnotationIntrospector = baseSettings.getAnnotationIntrospector();
 AnnotationIntrospectorPair expectedAnnotationIntrospectorPair = AnnotationIntrospectorPair.create(baseSettings._annotationIntrospector, newAnnotationIntrospector);
 AnnotationIntrospectorPair actualAnnotationIntrospectorPair = AnnotationIntrospectorPair.create(baseSettings.getAnnotationIntrospector(), baseSettings._annotationIntrospector);
 assertEquals(expectedAnnotationIntrospectorPair, actualAnnotationIntrospectorPair);
 }

 @Test
 public void testWithVisibilityChecker() throws Exception {
 VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker.Std();
 baseSettings = baseSettings.withVisibilityChecker(newVisibilityChecker);
 VisibilityChecker<?> actualVisibilityChecker = baseSettings.getVisibilityChecker();
 assertEquals(newVisibilityChecker, actualVisibilityChecker);
 }

 @Test
 public void testWithVisibility() throws Exception {
 JsonAutoDetect.Visibility newVisibility = JsonAutoDetect.Visibility.NONE;
 PropertyAccessor propertyAccessor = PropertyAccessor.FIELD;
 baseSettings = baseSettings.withVisibility(propertyAccessor, newVisibility);
 VisibilityChecker<?> actualVisibilityChecker = baseSettings.getVisibilityChecker();
 JsonAutoDetect.VisibilityChecker expectedVisibilityChecker = new JsonAutoDetect.VisibilityChecker.Std()
 .withVisibility(propertyAccessor, newVisibility);
 VisibilityChecker<?> actualAndExpectedVisibilityChecker = actualVisibilityChecker.asStartingValue();
 assertEquals(expectedVisibilityChecker, actualAndExpectedVisibilityChecker);
 }

 @Test
 public void testWithPropertyNamingStrategy() throws Exception {
 PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy.Default();
 baseSettings = baseSettings.withPropertyNamingStrategy(newPropertyNamingStrategy);
 PropertyNamingStrategy actualPropertyNamingStrategy = baseSettings.getPropertyNamingStrategy();
 assertEquals(newPropertyNamingStrategy, actualPropertyNamingStrategy);
 }

 @Test
 public void testWithTypeFactory() throws Exception {
 TypeFactory newTypeFactory = new TypeFactory();
 baseSettings = baseSettings.withTypeFactory(newTypeFactory);
 TypeFactory actualTypeFactory = baseSettings.getTypeFactory();
 assertEquals(newTypeFactory, actualTypeFactory);
 }

 @Test
 public void testWithTypeResolverBuilder() throws Exception {
 TypeResolverBuilder<?> newTypeResolverBuilder = new TypeResolverBuilderDefault();
 baseSettings = baseSettings.withTypeResolverBuilder(newTypeResolverBuilder);
 TypeResolverBuilder<?> actualTypeResolverBuilder = baseSettings.getTypeResolverBuilder();
 assertEquals(newTypeResolverBuilder, actualTypeResolverBuilder);
 }

 @Test
 public void testWithDateFormat() throws Exception {
 DateFormat newDateFormat = new StdDateFormat();
 baseSettings = baseSettings.withDateFormat(newDateFormat);
 DateFormat actualDateFormat = baseSettings.getDateFormat();
 assertEquals(newDateFormat, actualDateFormat);
 }

 @Test
 public void testWithHandlerInstantiator() throws Exception {
 HandlerInstantiator newHandlerInstantiator = new HandlerInstantiatorDefault();
 baseSettings = baseSettings.withHandlerInstantiator(newHandlerInstantiator);
 HandlerInstantiator actualHandlerInstantiator = baseSettings.getHandlerInstantiator();
 assertEquals(newHandlerInstantiator, actualHandlerInstantiator);
 }

 @Test
 public void testWithLocale() throws Exception {
 Locale newLocale = Locale.US;
 baseSettings = baseSettings.with(newLocale);
 Locale actualLocale = baseSettings.getLocale();
 assertEquals(newLocale, actualLocale);
 }

 @Test
 public void testWithTimeZone() throws Exception {
 TimeZone newTimeZone = TimeZone.getTimeZone("UTC");
 DateFormat dateFormat = new StdDateFormat();
 baseSettings = baseSettings.with(newTimeZone);
 DateFormat actualDateFormat = baseSettings.getDateFormat();
 assertEquals(dateFormat.clone(), actualDateFormat);
 TimeZone actualTimeZone = baseSettings.getTimeZone();
 assertEquals(newTimeZone, actualTimeZone);
 }

 @Test
 public void testWithBase64Variant() throws Exception {
 Base64Variant newBase64Variant = Base64Variants.US_ASCII;
 baseSettings = baseSettings.with(newBase64Variant);
 Base64Variant actualBase64Variant = baseSettings.getBase64Variant();
 assertEquals(newBase64Variant, actualBase64Variant);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTimeZone() throws Exception {
 TimeZone nullTimeZone = null;
 try {
 baseSettings = baseSettings.with(nullTimeZone);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGettersReturnSameInstance() throws Exception {
 baseSettings = baseSettings.withClassIntrospector(new ClassIntrospector());
 ClassIntrospector classIntrospector = baseSettings.getClassIntrospector();
 ClassIntrospector classIntrospector2 = baseSettings.getClassIntrospector();
 assertEquals(classIntrospector, classIntrospector2);
 }
}