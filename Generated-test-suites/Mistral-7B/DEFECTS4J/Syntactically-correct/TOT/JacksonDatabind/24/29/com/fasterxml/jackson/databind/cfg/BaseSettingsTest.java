package com.fasterxml.jackson.databind.cfg;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

 private BaseSettings baseSettings;

 @Test
 public void testBaseSettingsConstruction() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Default();
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.DEFAULT();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new StandardTypeResolverBuilder();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

 assertEquals("Unexpected classIntrospector", classIntrospector, baseSettings._classIntrospector);
 assertEquals("Unexpected annotationIntrospector", annotationIntrospector, baseSettings._annotationIntrospector);
 assertEquals("Unexpected visibilityChecker", visibilityChecker, baseSettings._visibilityChecker);
 assertEquals("Unexpected propertyNamingStrategy", propertyNamingStrategy, baseSettings._propertyNamingStrategy);
 assertEquals("Unexpected typeFactory", typeFactory, baseSettings._typeFactory);
 assertEquals("Unexpected typeResolverBuilder", typeResolverBuilder, baseSettings._typeResolverBuilder);
 assertEquals("Unexpected dateFormat", dateFormat, baseSettings._dateFormat);
 assertEquals("Unexpected handlerInstantiator", handlerInstantiator, baseSettings._handlerInstantiator);
 assertEquals("Unexpected locale", locale, baseSettings._locale);
 assertEquals("Unexpected timeZone", timeZone, baseSettings._timeZone);
 assertEquals("Unexpected defaultBase64", defaultBase64, baseSettings._defaultBase64);
 }

 @Test
 public void testBaseSettingsWithClassIntrospector() {
 ClassIntrospector newClassIntrospector = new ClassIntrospector();
 baseSettings = baseSettings.withClassIntrospector(newClassIntrospector);

 assertEquals("Unexpected original classIntrospector", classIntrospector, baseSettings._classIntrospector);
 assertEquals("Unexpected new classIntrospector", newClassIntrospector, baseSettings.getClassIntrospector());
 }

 @Test
 public void testBaseSettingsWithAnnotationIntrospector() {
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
 baseSettings = baseSettings.withAnnotationIntrospector(newAnnotationIntrospector);

 assertEquals("Unexpected original annotationIntrospector", annotationIntrospector, baseSettings._annotationIntrospector);
 assertEquals("Unexpected new annotationIntrospector", newAnnotationIntrospector, baseSettings.getAnnotationIntrospector());
 }

 @Test
 public void testBaseSettingsWithVisibilityChecker() {
 VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker.Default();
 baseSettings = baseSettings.withVisibilityChecker(newVisibilityChecker);

 assertEquals("Unexpected original visibilityChecker", visibilityChecker, baseSettings._visibilityChecker);
 assertEquals("Unexpected new visibilityChecker", newVisibilityChecker, baseSettings.getVisibilityChecker());
 }

 @Test
 public void testBaseSettingsWithPropertyNamingStrategy() {
 PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy.SnakeCaseStrategy();
 baseSettings = baseSettings.withPropertyNamingStrategy(newPropertyNamingStrategy);

 assertEquals("Unexpected original propertyNamingStrategy", propertyNamingStrategy, baseSettings._propertyNamingStrategy);
 assertEquals("Unexpected new propertyNamingStrategy", newPropertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
 }

 @Test
 public void testBaseSettingsWithTypeFactory() {
 TypeFactory newTypeFactory = new TypeFactoryBuilder().build();
 baseSettings = baseSettings.withTypeFactory(newTypeFactory);

 assertEquals("Unexpected original typeFactory", typeFactory, baseSettings._typeFactory);
 assertEquals("Unexpected new typeFactory", newTypeFactory, baseSettings.getTypeFactory());
 }

 @Test
 public void testBaseSettingsWithTypeResolverBuilder() {
 TypeResolverBuilder<?> newTypeResolverBuilder = new MinimalTypeResolverBuilder();
 baseSettings = baseSettings.withTypeResolverBuilder(newTypeResolverBuilder);

 assertEquals("Unexpected original typeResolverBuilder", typeResolverBuilder, baseSettings._typeResolverBuilder);
 assertEquals("Unexpected new typeResolverBuilder", newTypeResolverBuilder, baseSettings.getTypeResolverBuilder());
 }

 @Test
 public void testBaseSettingsWithDateFormat() {
 DateFormat newDateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
 baseSettings = baseSettings.withDateFormat(newDateFormat);

 assertEquals("Unexpected original dateFormat", dateFormat, baseSettings._dateFormat);
 assertEquals("Unexpected new dateFormat", newDateFormat, baseSettings.getDateFormat());
 }

 @Test
 public void testBaseSettingsWithHandlerInstantiator() {
 HandlerInstantiator newHandlerInstantiator = new MinimalHandlerInstantiator();
 baseSettings = baseSettings.withHandlerInstantiator(newHandlerInstantiator);

 assertEquals("Unexpected original handlerInstantiator", handlerInstantiator, baseSettings._handlerInstantiator);
 assertEquals("Unexpected new handlerInstantiator", newHandlerInstantiator, baseSettings.getHandlerInstantiator());
 }

 @Test
 public void testBaseSettingsWithLocale() {
 Locale newLocale = new Locale("fr", "FR");
 baseSettings = baseSettings.with(newLocale);

 assertEquals("Unexpected original locale", locale, baseSettings._locale);
 assertEquals("Unexpected new locale", newLocale, baseSettings.getLocale());
 }

 @Test
 public void testBaseSettingsWithTimeZone() {
 TimeZone newTimeZone = TimeZone.getTimeZone("Europe/Paris");
 baseSettings = baseSettings.with(newTimeZone);

 assertEquals("Unexpected original timeZone", timeZone, baseSettings._timeZone);
 assertEquals("Unexpected new timeZone", newTimeZone, baseSettings.getTimeZone());
 }

 @Test
 public void testBaseSettingsWithBase64Variant() {
 Base64Variant newBase64Variant = Base64Variants.URL_SAFE;
 baseSettings = baseSettings.with(newBase64Variant);

 assertEquals("Unexpected original base64Variant", defaultBase64, baseSettings._defaultBase64);
 assertEquals("Unexpected new base64Variant", newBase64Variant, baseSettings.getBase64Variant());
 }
}