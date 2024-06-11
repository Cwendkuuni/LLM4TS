package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

 private BaseSettings settings;

 @Before
 public void setup() {
 ClassIntrospector ci = new ClassIntrospector();
 AnnotationIntrospector ai = new AnnotationIntrospector();
 VisibilityChecker<?> vc = new VisibilityChecker.Default();
 PropertyNamingStrategy pns = StdPropertyNamingStrategy.INSTANCE;
 TypeFactory tf = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typer = new SimpleTypeResolverBuilder();
 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator hi = new HandlerInstanctor();
 settings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, Locale.ENGLISH, TimeZone.getDefault(), Base64Variant.US_STANDARD);
 }

 @Test
 public void testGetters() {
 ClassIntrospector actualClassIntrospector = settings.getClassIntrospector();
 AnnotationIntrospector actualAnnotationIntrospector = settings.getAnnotationIntrospector();
 VisibilityChecker<?> actualVisibilityChecker = settings.getVisibilityChecker();
 PropertyNamingStrategy actualPropertyNamingStrategy = settings.getPropertyNamingStrategy();
 TypeFactory actualTypeFactory = settings.getTypeFactory();
 TypeResolverBuilder<?> actualTypeResolverBuilder = settings.getTypeResolverBuilder();
 DateFormat actualDateFormat = settings.getDateFormat();
 HandlerInstantiator actualHandlerInstantiator = settings.getHandlerInstantiator();
 Locale actualLocale = settings.getLocale();
 TimeZone actualTimeZone = settings.getTimeZone();
 Base64Variant actualBase64Variant = settings.getBase64Variant();

 ClassIntrospector expectedClassIntrospector = new ClassIntrospector();
 AnnotationIntrospector expectedAnnotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> expectedVisibilityChecker = new VisibilityChecker.Default();
 PropertyNamingStrategy expectedPropertyNamingStrategy = StdPropertyNamingStrategy.INSTANCE;
 TypeFactory expectedTypeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> expectedTypeResolverBuilder = new SimpleTypeResolverBuilder();
 DateFormat expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator expectedHandlerInstantiator = new HandlerInstanctor();
 Locale expectedLocale = Locale.ENGLISH;
 TimeZone expectedTimeZone = TimeZone.getDefault();
 Base64Variant expectedBase64Variant = Base64Variant.US_STANDARD;

 assertEquals(expectedClassIntrospector, actualClassIntrospector);
 assertEquals(expectedAnnotationIntrospector, actualAnnotationIntrospector);
 assertEquals(expectedVisibilityChecker, actualVisibilityChecker);
 assertEquals(expectedPropertyNamingStrategy, actualPropertyNamingStrategy);
 assertEquals(expectedTypeFactory, actualTypeFactory);
 assertEquals(expectedTypeResolverBuilder, actualTypeResolverBuilder);
 assertEquals(expectedDateFormat, actualDateFormat);
 assertEquals(expectedHandlerInstantiator, actualHandlerInstantiator);
 assertEquals(expectedLocale, actualLocale);
 assertEquals(expectedTimeZone, actualTimeZone);
 assertEquals(expectedBase64Variant, actualBase64Variant);
 }

 @Test
 public void testWithClassIntrospector() {
 ClassIntrospector ci = new ClassIntrospector();
 settings = settings.withClassIntrospector(ci);
 ClassIntrospector actualClassIntrospector = settings.getClassIntrospector();
 assertEquals(ci, actualClassIntrospector);
 }

 @Test
 public void testWithAnnotationIntrospector() {
 AnnotationIntrospector ai = new AnnotationIntrospector();
 settings = settings.withAnnotationIntrospector(ai);
 AnnotationIntrospector actualAnnotationIntrospector = settings.getAnnotationIntrospector();
 assertEquals(ai, actualAnnotationIntrospector);
 }

 @Test
 public void testWithVisibilityChecker() {
 VisibilityChecker<?> vc = new VisibilityChecker.Default();
 settings = settings.withVisibilityChecker(vc);
 VisibilityChecker<?> actualVisibilityChecker = settings.getVisibilityChecker();
 assertEquals(vc, actualVisibilityChecker);
 }

 @Test
 public void testWithPropertyNamingStrategy() {
 PropertyNamingStrategy pns = new PropertyNamingStrategy.Default();
 settings = settings.withPropertyNamingStrategy(pns);
 PropertyNamingStrategy actualPropertyNamingStrategy = settings.getPropertyNamingStrategy();
 assertEquals(pns, actualPropertyNamingStrategy);
 }

 @Test
 public void testWithTypeFactory() {
 TypeFactory tf = new TypeFactoryBuilder().build();
 settings = settings.withTypeFactory(tf);
 TypeFactory actualTypeFactory = settings.getTypeFactory();
 assertEquals(tf, actualTypeFactory);
 }

 @Test
 public void testWithTypeResolverBuilder() {
 TypeResolverBuilder<?> typer = new SimpleTypeResolverBuilder();
 settings = settings.withTypeResolverBuilder(typer);
 TypeResolverBuilder<?> actualTypeResolverBuilder = settings.getTypeResolverBuilder();
 assertEquals(typer, actualTypeResolverBuilder);
 }

 @Test
 public void testWithDateFormat() {
 DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 settings = settings.withDateFormat(df);
 DateFormat actualDateFormat = settings.getDateFormat();
 assertEquals(df, actualDateFormat);
 }

 @Test
 public void testWithHandlerInstantiator() {
 HandlerInstantiator hi = new HandlerInstanctor();
 settings = settings.withHandlerInstantiator(hi);
 HandlerInstantiator actualHandlerInstantiator = settings.getHandlerInstantiator();
 assertEquals(hi, actualHandlerInstantiator);
 }

 @Test
 public void testWith() {
 Locale l = Locale.FRENCH;
 settings = settings.with(l);
 Locale actualLocale = settings.getLocale();
 assertEquals(l, actualLocale);
 }

 @Test
 public void testWithTimeZone() {
 TimeZone tz = TimeZone.getTimeZone("UTC");
 settings = settings.with(tz);
 TimeZone actualTimeZone = settings.getTimeZone();
 assertEquals(tz, actualTimeZone);
 }

 @Test
 public void testWithBase64Variant() {
 Base64Variant base64 = Base64Variant.US_MIL_STD_1B;
 settings = settings.with(base64);
 Base64Variant actualBase64Variant = settings.getBase64Variant();
 assertEquals(base64, actualBase64Variant);
 }
}