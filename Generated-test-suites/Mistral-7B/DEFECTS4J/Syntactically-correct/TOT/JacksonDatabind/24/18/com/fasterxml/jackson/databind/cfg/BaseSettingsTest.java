package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.HandlerInstantiator;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.type.TypeResolverBuilder;

public class BaseSettingsTest {

	@Test
	public void testBaseSettings() {
		// Test default constructor
		BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(),
				TimeZone.getDefault(), Base64Variant.STANDARD);
		assertNotNull(settings);
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
		assertEquals(Base64Variant.STANDARD, settings.getBase64Variant());

		// Test custom constructor
		ClassIntrospector ci = new ClassIntrospector();
		AnnotationIntrospector ai = new AnnotationIntrospector();
		VisibilityChecker<Object> vc = new VisibilityChecker<>();
		PropertyNamingStrategy pns = new PropertyNamingStrategy() {
			@Override
			public String nameForProperty(PropertyDescriptor pd) {
				return "custom_" + pd.getName();
			}
		};
		TypeFactory tf = new TypeFactory();
		TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
		DateFormat dateFormat = new StdDateFormat();
		HandlerInstantiator hi = new HandlerInstantantiator() { /* ... */ };
		Locale locale = Locale.ENGLISH;
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Base64Variant customBase64 = Base64Variant.BASE64_URL_SAFE;
		BaseSettings customSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, customBase64);
		assertNotNull(customSettings);
		assertEquals(ci, customSettings.getClassIntrospector());
		assertEquals(ai, customSettings.getAnnotationIntrospector());
		assertEquals(vc, customSettings.getVisibilityChecker());
		assertEquals(pns, customSettings.getPropertyNamingStrategy());
		assertEquals(tf, customSettings.getTypeFactory());
		assertEquals(typer, customSettings.getTypeResolverBuilder());
		assertEquals(dateFormat, customSettings.getDateFormat());
		assertEquals(hi, customSettings.getHandlerInstantiator());
		assertEquals(locale, customSettings.getLocale());
		assertEquals(timeZone, customSettings.getTimeZone());
		assertEquals(customBase64, customSettings.getBase64Variant());

		// Test with methods
		BaseSettings withClassIntrospector = settings.withClassIntrospector(ci);
		assertEquals(ci, withClassIntrospector.getClassIntrospector());
		assertEquals(settings, withClassIntrospector);

		BaseSettings withAnnotationIntrospector = settings.withAnnotationIntrospector(ai);
		assertEquals(ai, withAnnotationIntrospector.getAnnotationIntrospector());
		assertEquals(settings, withAnnotationIntrospector);

		BaseSettings withInsertedAnnotationIntrospector = settings.withInsertedAnnotationIntrospector(ai);
		assertEquals(ai, withInsertedAnnotationIntrospector.getAnnotationIntrospector().getAnnotationIntrospector());
		assertEquals(settings, withInsertedAnnotationIntrospector);

		BaseSettings withAppendedAnnotationIntrospector = settings.withAppendedAnnotationIntrospector(ai);
		assertEquals(settings, withAppendedAnnotationIntrospector);
		assertEquals(ai, withAppendedAnnotationIntrospector.getAnnotationIntrospector());

		BaseSettings withVisibilityChecker = settings.withVisibilityChecker(vc);
		assertEquals(vc, withVisibilityChecker.getVisibilityChecker());
		assertEquals(settings, withVisibilityChecker);

		BaseSettings withVisibility = settings.withVisibility(null, Visibility.ANY);
		assertEquals(settings, withVisibility);
		assertEquals(vc, withVisibility.getVisibilityChecker());

		BaseSettings withPropertyNamingStrategy = settings.withPropertyNamingStrategy(pns);
		assertEquals(pns, withPropertyNamingStrategy.getPropertyNamingStrategy());
		assertEquals(settings, withPropertyNamingStrategy);

		BaseSettings withTypeFactory = settings.withTypeFactory(tf);
		assertEquals(tf, withTypeFactory.getTypeFactory());
		assertEquals(settings, withTypeFactory);

		BaseSettings withTypeResolverBuilder = settings.withTypeResolverBuilder(typer);
		assertEquals(typer, withTypeResolverBuilder.getTypeResolverBuilder());
		assertEquals(settings, withTypeResolverBuilder);

		BaseSettings withDateFormat = settings.withDateFormat(dateFormat);
		assertEquals(dateFormat, withDateFormat.getDateFormat());
		assertEquals(settings, withDateFormat);

		BaseSettings withHandlerInstantiator = settings.withHandlerInstantiator(hi);
		assertEquals(hi, withHandlerInstantiator.getHandlerInstantiator());
		assertEquals(settings, withHandlerInstantiator);

		BaseSettings with = settings.with(locale);
		assertEquals(locale, with.getLocale());
		assertEquals(settings, with);

		BaseSettings withTimeZone = settings.with(timeZone);
		assertEquals(timeZone, withTimeZone.getTimeZone());
		assertEquals(settings, withTimeZone);

		BaseSettings withBase64Variant = settings.with(customBase64);
		assertEquals(customBase64, withBase64Variant.getBase64Variant());
		assertEquals(settings, withBase64Variant);

		// Test parseDateFormat
		try {
			DateFormat df = new StdDateFormat();
			settings = new BaseSettings(null, null, null, null, null, null, df, null, null, null, null);
			Date date = settings.parseDate("2023-03-31T12:00:00.123Z", null);
			assertNotNull(date);
			assertEquals("Sat Mar 31 12:00:03 GMT+00:00 2023", date.toString());
		} catch (IllegalArgumentException e) {
			assertEquals("Failed to parse date: '2023-03-31T12:00:00.123Z' to Date using default DateFormat: DateFormat:501", e.getMessage());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}