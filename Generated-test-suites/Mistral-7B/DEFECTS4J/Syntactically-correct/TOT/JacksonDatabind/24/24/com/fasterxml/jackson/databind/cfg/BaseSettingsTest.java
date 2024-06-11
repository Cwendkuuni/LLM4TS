package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.Default;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

	@Test
	public void testBaseSettingsDefault() throws Exception {
		BaseSettings settings = new BaseSettings(
				new ClassIntrospector(), new AnnotationIntrospector(),
				JsonAutoDetect.Visibility.ANY(), new Default(),
				new TypeFactory(), new TypeResolverBuilder<>(), null,
				new HandlerInstantiator(), Locale.getDefault(),
				TimeZone.getDefault(), Base64Variant.US_STANDARD);
		assertEquals(JsonAutoDetect.Visibility.ANY, settings.getVisibilityChecker().forProperty("test"));
		assertEquals(Locale.getDefault(), settings.getLocale());
		assertEquals(TimeZone.getDefault(), settings.getTimeZone());
		assertEquals(Base64Variant.US_STANDARD, settings.getBase64Variant());
	}

	@Test
	public void testBaseSettingsCustom() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		DateFormat dateFormat = new StdDateFormat();
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		BaseSettings settings = new BaseSettings(
				new ClassIntrospector(), new AnnotationIntrospector(),
				JsonAutoDetect.Visibility.NONE, new Default(),
				new TypeFactory(), new TypeResolverBuilder<>(), dateFormat,
				new HandlerInstantiator(), new Locale("en_US"),
				TimeZone.getTimeZone("UTC"), Base64Variant.US_STANDARD);
		assertEquals(JsonAutoDetect.Visibility.NONE, settings.getVisibilityChecker().forProperty("test"));
		assertEquals("en_US", settings.getLocale().getDisplayName());
		assertEquals("UTC", settings.getTimeZone().getDisplayName());
		assertEquals(Base64Variant.US_STANDARD, settings.getBase64Variant());

		JavaType type = settings.getTypeFactory().constructType(String[].class);
		String[] values = {"test1", "test2"};
		String json = mapper.writerWith(settings).writeValueAsString(values);
		assertNotNull(json);
		Object obj = mapper.readValue(json, String[].class);
		assertArrayEquals(values, (String[]) obj);
	}

	@Test(expected = JsonMappingException.class)
	public void testBaseSettingsFailed() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		DateFormat dateFormat = new StdDateFormat();
		BaseSettings settings = new BaseSettings(
				new ClassIntrospector(), new AnnotationIntrospector(),
				JsonAutoDetect.Visibility.NONE, new Default(),
				new TypeFactory(), new TypeResolverBuilder<>(), dateFormat,
				new HandlerInstantiator(), new Locale("en_US"),
				TimeZone.getTimeZone("UTC"), Base64Variant.US_STANDARD);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
		JavaType type = settings.getTypeFactory().constructType(String[].class);
		String json = mapper.writerWith(settings).writeValueAsString(null);
		assertNotNull(json);
		mapper.readValue(json, String[].class);
	}
}