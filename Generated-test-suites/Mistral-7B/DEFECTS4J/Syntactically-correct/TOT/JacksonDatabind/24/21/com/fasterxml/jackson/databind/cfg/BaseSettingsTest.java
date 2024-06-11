package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Test cases for BaseSettings class
 */
public class BaseSettingsTest {

 private BaseSettings settings;

 @Before
 public void setUp() {
 ClassIntrospector ci = new ClassIntrospector(new AnnotationIntrospector());
 AnnotationIntrospector ai = new AnnotationIntrospector();
 VisibilityChecker<?> vc = new VisibilityChecker.Std();
 PropertyNamingStrategy pns = new PropertyNamingStrategy.Default();
 TypeFactory tf = new TypeFactory();
 TypeResolverBuilder<?> trb = new TypeResolverBuilderDefault();
 DateFormat df = new StdDateFormat();
 HandlerInstantiator hi = new HandlerInstantiator();
 settings = new BaseSettings(ci, ai, vc, pns, tf, trb, df, hi, Locale.ENGLISH, TimeZone.getDefault(), null);
 }

 @Test
 public void testDefaultSettings() {
 // Check default settings
 assertEquals(Settings.Std, settings.getClassIntrospector());
 assertEquals(Settings.Std, settings.getAnnotationIntrospector());
 assertEquals(Settings.StdVisibilityChecker.INSTANCE, settings.getVisibilityChecker());
 assertEquals(Settings.DEFAULT_PROPERTY_NAMING_STRATEGY, settings.getPropertyNamingStrategy());
 assertEquals(Settings.DefaultTypeFactory.INSTANCE, settings.getTypeFactory());
 assertEquals(Settings.DefaultTypeResolverBuilder.INSTANCE, settings.getTypeResolverBuilder());
 assertNull(settings.getDateFormat());
 assertEquals(Locale.getDefault(), settings.getLocale());
 assertEquals(TimeZone.getDefault(), settings.getTimeZone());
 assertEquals(Settings.DEFAULT_BASE64_VARIANT, settings.getBase64Variant());
 }

 @Test
 public void testSetters() {
 // Test setters
 settings = settings.withClassIntrospector(new CustomClassIntrospector());
 assertEquals(CustomClassIntrospector.class, settings.getClassIntrospector());
 settings = settings.withAnnotationIntrospector(new CustomAnnotationIntrospector());
 assertEquals(CustomAnnotationIntrospector.class, settings.getAnnotationIntrospector());
 settings = settings.withVisibilityChecker(new CustomVisibilityChecker());
 assertEquals(CustomVisibilityChecker.class, settings.getVisibilityChecker());
 settings = settings.withPropertyNamingStrategy(new CustomPropertyNamingStrategy());
 assertEquals(CustomPropertyNamingStrategy.class, settings.getPropertyNamingStrategy());
 settings = settings.withTypeFactory(new CustomTypeFactory());
 assertEquals(CustomTypeFactory.class, settings.getTypeFactory());
 settings = settings.withTypeResolverBuilder(new CustomTypeResolverBuilder());
 assertEquals(CustomTypeResolverBuilder.class, settings.getTypeResolverBuilder());
 DateFormat newDateFormat = new StdDateFormat("dd-MM-yyyy");
 settings = settings.withDateFormat(newDateFormat());
 assertEquals(newDateFormat, settings.getDateFormat());
 settings = settings.withLocale(Locale.FRENCH);
 assertEquals(Locale.FRENCH, settings.getLocale());
 settings = settings.withTimeZone(ZoneId.of("Europe/Paris").toZoneId());
 assertEquals(ZoneId.of("Europe/Paris").toZoneId(), settings.getTimeZone().toZoneId());
 settings = settings.withBase64Variant(Base64Variant.UTF8);
 assertEquals(Base64Variant.UTF8, settings.getBase64Variant());
 }

 @Test
 public void testSerialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializationInclusion(JsonInclude.Value.ALL);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 // Serialize default settings
 String json = mapper.writeValueAsString(settings);
 // Check serialization of all fields
 assertNotNull(json);
 assertTrue(json.contains("classIntrospector"));
 assertTrue(json.contains("annotationIntrospector"));
 assertTrue(json.contains("visibilityChecker"));
 assertTrue(json.contains("propertyNamingStrategy"));
 assertTrue(json.contains("typeFactory"));
 assertTrue(json.contains("typeResolverBuilder"));
 assertTrue(json.contains("dateFormat"));
 assertTrue(json.contains("locale"));
 assertTrue(json.contains("timeZone"));
 assertTrue(json.contains("base64"));
 // Serialize settings with custom values
 settings.withClassIntrospector(new CustomClassIntrospector());
 json = mapper.writeValueAsString(settings);
 // Check serialization of new custom value
 assertTrue(json.contains("classIntrospector"));
 assertTrue(json.contains("classIntrospectorClass: " + CustomClassIntrospector.class.getName()));
 // Test serialization of DateFormat with custom TimeZone
 settings.withDateFormat(new StdDateFormat("dd-MM-yyyy", Locale.FRENCH, TimeZone.getTimeZone("Europe/Paris")));
 json = mapper.writeValueAsString(settings);
 // Check serialization of new custom TimeZone
 assertTrue(json.contains("dateFormat"));
 assertTrue(json.contains("dateFormatTimeZone: Europe/Paris"));
 }

 @Test
 public void testDeserialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 // Deserialize default settings
 BaseSettings settingsFromJson = mapper.readValue(getClass().getResource("/BaseSettingsTest-default.json"), BaseSettings.class);
 // Check deserialization of all fields
 assertEquals(settings.getClassIntrospector(), settingsFromJson.getClassIntrospector());
 assertEquals(settings.getAnnotationIntrospector(), settingsFromJson.getAnnotationIntrospector());
 assertEquals(settings.getVisibilityChecker(), settingsFromJson.getVisibilityChecker());
 assertEquals(settings.getPropertyNamingStrategy(), settingsFromJson.getPropertyNamingStrategy());
 assertEquals(settings.getTypeFactory(), settingsFromJson.getTypeFactory());
 assertEquals(settings.getTypeResolverBuilder(), settingsFromJson.getTypeResolverBuilder());
 assertNotNull(settings.getDateFormat());
 assertEquals(settings.getLocale(), settingsFromJson.getLocale());
 assertEquals(settings.getTimeZone(), settingsFromJson.getTimeZone());
 assertEquals(settings.getBase64Variant(), settingsFromJson.getBase64Variant());
 // Deserialize settings with custom values
 String json = "{\"classIntrospectorClass\": \"com.fasterxml.jackson.databind.introspect.CustomClassIntrospector\", "
 + "\"annotationIntrospector\":{\"class\":\"com.fasterxml.jackson.databind.introspect.CustomAnnotationIntrospector\", "
 + "\"annotationIntrospectorClass\":\"com.fasterxml.jackson.databind.introspect.AnnotationIntrospector\"}, "
 + "\"visibilityChecker\":{\"class\":\"com.fasterxml.jackson.databind.introspect.CustomVisibilityChecker\", "
 + "\"visibilityCheckerClass\":\"com.fasterxml.jackson.databind.introspect.VisibilityChecker\"}, "
 + "\"propertyNamingStrategy\":{\"class\":\"com.fasterxml.jackson.databind.PropertyNamingStrategy.CustomPropertyNamingStrategy\", "
 + "\"propertyNamingStrategyClass\":\"com.fasterxml.jackson.databind.PropertyNamingStrategy\"}, "
 + "\"typeFactory\":{\"class\":\"com.fasterxml.jackson.databind.type.CustomTypeFactory\", "
 + "\"typeFactoryClass\":\"com.fasterxml.jackson.databind.type.TypeFactory\"}, "
 + "\"typeResolverBuilder\":{\"class\":\"com.fasterxml.jackson.databind.type.CustomTypeResolverBuilder\", "
 + "\"typeResolverBuilderClass\":\"com.fasterxml.jackson.databind.type.TypeResolverBuilder\"}, "
 + "\"dateFormat\":{\"class\":\"com.fasterxml.jackson.databind.util.StdDateFormat\", "
 + "\"dateFormatClass\":\"com.fasterxml.jackson.databind.util.StdDateFormat\", "
 + "\"timeZone\":\"Europe/Paris\"}, "
 + "\"locale\":\"fr\", "
 + "\"timeZone\":\"Europe/Paris\", "
 + "\"base64\":\"UTF8\"}";
 settingsFromJson = mapper.readValue(json, BaseSettings.class);
 // Check deserialization of new custom values
 assertEquals(settings.getClassIntrospector(), settingsFromJson.getClassIntrospector());
 assertEquals(CustomClassIntrospector.class, settingsFromJson.getClassIntrospectorClass());
 assertEquals(settings.getAnnotationIntrospector(), settingsFromJson.getAnnotationIntrospector());
 assertEquals(CustomAnnotationIntrospector.class, settingsFromJson.getAnnotationIntrospectorClass());
 assertEquals(settings.getVisibilityChecker(), settingsFromJson.getVisibilityChecker());
 assertEquals(CustomVisibilityChecker.class, settingsFromJson.getVisibilityCheckerClass());
 assertEquals(settings.getPropertyNamingStrategy(), settingsFromJson.getPropertyNamingStrategy());
 assertEquals(CustomPropertyNamingStrategy.class, settingsFromJson.getPropertyNamingStrategyClass());
 assertEquals(settings.getTypeFactory(), settingsFromJson.getTypeFactory());
 assertEquals(CustomTypeFactory.class, settingsFromJson.getTypeFactoryClass());
 assertEquals(settings.getTypeResolverBuilder(), settingsFromJson.getTypeResolverBuilder());
 assertEquals(settings.getTypeResolverBuilderClass(), settingsFromJson.getTypeResolverBuilderClass());
 assertNotNull(settingsFromJson.getDateFormat());
 assertEquals(StdDateFormat.class, settingsFromJson.getDateFormatClass());
 assertEquals(Locale.FRENCH, settingsFromJson.getLocale());
 assertEquals(ZoneId.of("Europe/Paris").toZoneId(), settingsFromJson.getTimeZone().toZoneId());
 assertEquals(Base64Variant.UTF8, settingsFromJson.getBase64());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDateFormat() {
 // Test setting null DateFormat
 settings = settings.withDateFormat(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullLocale() {
 // Test setting null Locale
 settings = settings.with(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTimeZone() {
 // Test setting null TimeZone
 settings = settings.with(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBase64Variant() {
 // Test setting null Base64Variant
 settings = settings.with(null);
 }
}

class CustomClassIntrospector extends ClassIntrospector {
 // Custom implementation of ClassIntrospector
}

class CustomAnnotationIntrospector extends AnnotationIntrospector {
 // Custom implementation of AnnotationIntrospector
}

class CustomVisibilityChecker extends VisibilityChecker<JsonAutoDetect.Visibility> {
 // Custom implementation of VisibilityChecker
}

class CustomPropertyNamingStrategy extends PropertyNamingStrategy {
 // Custom implementation of PropertyNamingStrategy
}

class CustomTypeFactory extends TypeFactory {
 // Custom implementation of TypeFactory
}

class CustomTypeResolverBuilder extends TypeResolverBuilder<JavaType> {
 // Custom implementation of TypeResolverBuilder
}

class CustomHandlerInstantiator extends HandlerInstantiator {
 // Custom implementation of HandlerInstantiator
}