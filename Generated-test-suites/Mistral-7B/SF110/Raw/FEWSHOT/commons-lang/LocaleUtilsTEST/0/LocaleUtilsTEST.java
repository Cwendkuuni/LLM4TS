package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

	@Test
	public void testToLocaleEmpty() {
		assertNull(LocaleUtils.toLocale(""));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocaleInvalidFormat1() {
		LocaleUtils.toLocale("XXX");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocaleInvalidFormat2() {
		LocaleUtils.toLocale("en_");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocaleInvalidFormat3() {
		LocaleUtils.toLocale("en_fr_");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocaleInvalidFormat4() {
		LocaleUtils.toLocale("en_fr_es");
	}

	@Test
	public void testToLocaleSimple() {
		assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
		assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
		assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));
	}

	@Test
	public void testToLocaleWithCountry() {
		assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
		assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
		assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));
	}

	@Test
	public void testToLocaleWithLanguageAndCountry() {
		assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_AT"));
		assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_CH"));
		assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_LI"));
	}

	@Test
	public void testToLocaleWithLanguageAndCountryAndVariant() {
		assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_AT_de-AT"));
		assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_CH_de-CH"));
		assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_LI_de-LI"));
	}

	@Test
	public void testLocaleLookupList() {
		List<Locale> locales = LocaleUtils.localeLookupList(Locale.ENGLISH);
		assertEquals(2, locales.size());
		assertEquals(Locale.ENGLISH, locales.get(0));
		assertEquals(Locale.ENGLISH, new Locale("en"));

		locales = LocaleUtils.localeLookupList(Locale.FRENCH);
		assertEquals(3, locales.size());
		assertEquals(Locale.FRENCH, locales.get(0));
		assertEquals(Locale.FRENCH, new Locale("fr"));
		assertEquals(Locale.FRENCH, new Locale("fr_FR"));

		locales = LocaleUtils.localeLookupList(Locale.GERMAN);
		assertEquals(4, locales.size());
		assertEquals(Locale.GERMAN, locales.get(0));
		assertEquals(Locale.GERMAN, new Locale("de"));
		assertEquals(Locale.GERMAN, new Locale("de_DE"));
		assertEquals(Locale.GERMAN, new Locale("de_AT"));
	}

	@Test
	public void testLocaleLookupListWithDefaultLocale() {
		List<Locale> locales = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH);
		assertEquals(3, locales.size());
		assertEquals(Locale.ENGLISH, locales.get(0));
		assertEquals(Locale.ENGLISH, new Locale("en"));
		assertEquals(Locale.FRENCH, locales.get(2));

		locales = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
		assertEquals(3, locales.size());
		assertEquals(Locale.FRENCH, locales.get(0));
		assertEquals(Locale.FRENCH, new Locale("fr"));
		assertEquals(Locale.ENGLISH, locales.get(2));
	}

	@Test
	public void testAvailableLocaleList() {
		List<Locale> locales = LocaleUtils.availableLocaleList();
		assertEquals(212, locales.size());
		assertNotNull(locales.get(0));
		assertEquals("en_US", locales.get(0).toString());
		assertEquals("zh_CN", locales.get(211).toString());
	}

	@Test
	public void testAvailableLocaleSet() {
		Set<Locale> locales = LocaleUtils.availableLocaleSet();
		assertEquals(212, locales.size());
		assertTrue(locales.contains(Locale.ENGLISH));
		assertTrue(locales.contains(new Locale("zh", "CN")));
	}

	@Test
	public void testIsAvailableLocale() {
		assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
		assertTrue(LocaleUtils.isAvailableLocale(new Locale("zh", "CN")));
		assertFalse(LocaleUtils.isAvailableLocale(null));
		assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "xx")));
	}

	@Test
	public void testLanguagesByCountry() {
		List<Locale> locales = LocaleUtils.languagesByCountry("US");
		assertEquals(13, locales.size());
		assertEquals("en", locales.get(0).getLanguage());
		assertEquals("es", locales.get(3).getLanguage());
		assertEquals("fr", locales.get(7).getLanguage());
		assertEquals("hi", locales.get(10).getLanguage());
		assertEquals("it", locales.get(11).getLanguage());

		locales = LocaleUtils.languagesByCountry("CN");
		assertEquals(13, locales.size());
		assertEquals("zh", locales.get(0).getLanguage());
		assertEquals("en", locales.get(11).getLanguage());
		assertEquals("fr", locales.get(12).getLanguage());

		locales = LocaleUtils.languagesByCountry("FR");
		assertEquals(3, locales.size());
		assertEquals("fr", locales.get(0).getLanguage());
		assertEquals("fr", locales.get(1).getLanguage());
		assertEquals("fr", locales.get(2).getLanguage());
	}

	@Test
	public void testCountriesByLanguage() {
		List<Locale> locales = LocaleUtils.countriesByLanguage("en");
		assertEquals(5, locales.size());
		assertEquals("US", locales.get(0).getCountry());
		assertEquals("CA", locales.get(1).getCountry());
		assertEquals("GB", locales.get(3).getCountry());
		assertEquals("IE", locales.get(4).getCountry());
		assertEquals("JE", locales.get(4).getCountry());

		locales = LocaleUtils.countriesByLanguage("zh");
		assertEquals(2, locales.size());
		assertEquals("CN", locales.get(0).getCountry());
		assertEquals("HK", locales.get(1).getCountry());

		locales = LocaleUtils.countriesByLanguage("fr");
		assertEquals(11, locales.size());
		assertEquals("AD", locales.get(0).getCountry());
		assertEquals("BE", locales.get(1).getCountry());
		assertEquals("CA", locales.get(2).getCountry());
		assertEquals("CH", locales.get(3).getCountry());
		assertEquals("FR", locales.get(4).getCountry());
		assertEquals("GF", locales.get(5).getCountry());
		assertEquals("MC", locales.get(6).getCountry());
		assertEquals("RE", locales.get(8).getCountry());
		assertEquals("TF", locales.get(9).getCountry());
	}

	@Test
	public void testLanguagesByCountryAndCountryNull() {
		assertNull(LocaleUtils.languagesByCountry(null));
	}

	@Test
	public void testLanguagesByCountryAndLanguageNull() {
		assertNull(LocaleUtils.languagesByCountry(null, new Locale("en")));
	}

	@Test
	public void testCountriesByLanguageAndLanguageNull() {
		assertNull(LocaleUtils.countriesByLanguage(null));
	}

	@Test
	public void testCountriesByLanguageAndLanguageAndLocaleNotInList() {
		assertNull(LocaleUtils.countriesByLanguage(new Locale("xx"), new Locale("xx")));
	}

	@Test
	public void testCountriesByLanguageAndLanguageAndLocaleInList() {
		Locale locale = new Locale("fr", "FR");
		assertEquals(locale, LocaleUtils.countriesByLanguage("fr", locale));
		assertEquals("FR", locale.getCountry());
	}
}