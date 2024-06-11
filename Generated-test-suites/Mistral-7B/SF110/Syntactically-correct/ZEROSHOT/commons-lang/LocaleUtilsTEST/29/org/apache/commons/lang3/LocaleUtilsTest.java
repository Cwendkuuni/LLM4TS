package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.junit.Test;

public class LocaleUtilsTest {

	@Test
	public void testToLocale_ValidLocale() {
		Locale expected = new Locale("en", "US");
		Locale actual = LocaleUtils.toLocale("en_US");
		assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocale_NullString() {
		LocaleUtils.toLocale(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocale_EmptyString() {
		LocaleUtils.toLocale("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocale_InvalidFormat_NoLanguage() {
		LocaleUtils.toLocale("_US");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocale_InvalidFormat_NoCountry() {
		LocaleUtils.toLocale("en");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocale_InvalidFormat_InvalidCountryCode() {
		LocaleUtils.toLocale("en_XX");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testToLocale_InvalidFormat_InvalidLanguageCode() {
		LocaleUtils.toLocale("xx_US");
	}

	@Test
	public void testToLocale_ValidThreeLetterCountryCode() {
		Locale expected = new Locale("", "GB");
		Locale actual = LocaleUtils.toLocale("en_GB");
		assertEquals(expected, actual);
	}

	@Test
	public void testToLocale_ValidThreeLetterLanguageCode() {
		Locale expected = new Locale("ro", "");
		Locale actual = LocaleUtils.toLocale("ro_");
		assertEquals(expected, actual);
	}

	@Test
	public void testToLocale_ValidLocaleWithVariant() {
		Locale expected = new Locale("fr", "FR", "Latn");
		Locale actual = LocaleUtils.toLocale("fr_FR_Latn");
		assertEquals(expected, actual);
	}

	@Test
	public void testLocaleLookupList_NullLocale() {
		List<Locale> actual = LocaleUtils.localeLookupList(null);
		assertNotNull(actual);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void testLocaleLookupList_ValidLocale() {
		Locale locale = new Locale("de", "DE");
		List<Locale> expected = Arrays.asList(locale, new Locale("de", ""), new Locale("", "DE"), new Locale("", ""));
		List<Locale> actual = LocaleUtils.localeLookupList(locale);
		assertEquals(expected, actual);
	}

	@Test
	public void testLocaleLookupList_EmptyList_NullDefaultLocale() {
		List<Locale> actual = LocaleUtils.localeLookupList(null, null);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void testLocaleLookupList_EmptyList_ValidDefaultLocale() {
		Locale defaultLocale = new Locale("en", "US");
		List<Locale> actual = LocaleUtils.localeLookupList(null, defaultLocale);
		assertTrue(actual.contains(defaultLocale));
		assertTrue(actual.size() == 1);
	}

	@Test
	public void testLocaleLookupList_ValidLocale_EmptyList_ValidDefaultLocale() {
		Locale locale = new Locale("fr", "FR");
		Locale defaultLocale = new Locale("en", "US");
		List<Locale> expected = Arrays.asList(locale, new Locale("fr", ""), new Locale("", "FR"), new Locale("", ""), defaultLocale);
		List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
		assertEquals(expected, actual);
	}

	@Test
	public void testLocaleLookupList_ValidLocale_NonEmptyList_ValidDefaultLocale() {
		Locale locale = new Locale("fr", "FR");
		Locale defaultLocale = new Locale("en", "US");
		Locale alternateLocale = new Locale("fr", "CA");
		List<Locale> expected = Arrays.asList(locale, new Locale("fr", ""), new Locale("", "FR"), new Locale("", ""), defaultLocale, alternateLocale);
		List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
		assertEquals(expected, actual);
	}

	@Test
	public void testAvailableLocaleList() {
		List<Locale> actual = LocaleUtils.availableLocaleList();
		assertNotNull(actual);
		assertFalse(actual.isEmpty());
		assertEquals(LocaleUtils.availableLocaleSet().size(), actual.size());
	}

	@Test
	public void testAvailableLocaleSet() {
		Set<Locale> actual = LocaleUtils.availableLocaleSet();
		assertNotNull(actual);
		assertFalse(actual.isEmpty());
		assertEquals(LocaleUtils.availableLocaleList().size(), actual.size());
	}

	@Test
	public void testIsAvailableLocale_ValidLocale() {
		Locale locale = new Locale("en", "US");
		assertTrue(LocaleUtils.isAvailableLocale(locale));
	}

	@Test
	public void testIsAvailableLocale_InvalidLocale() {
		Locale locale = new Locale("xx", "XX");
		assertFalse(LocaleUtils.isAvailableLocale(locale));
	}

	@Test
	public void testLanguagesByCountry() {
		List<Locale> actual = LocaleUtils.languagesByCountry("US");
		assertNotNull(actual);
		assertTrue(actual.isEmpty() == false);
		assertEquals(3, actual.size());
		List<String> expected = Arrays.asList("en", "es", "fr");
		List<String> actualLangs = actual.stream().map(Locale::getLanguage).collect(Collectors.toList());
		assertEquals(expected, actualLangs);
	}

	@Test
	public void testCountriesByLanguage() {
		List<Locale> actual = LocaleUtils.countriesByLanguage("en");
		assertNotNull(actual);
		assertTrue(actual.isEmpty() == false);
		assertEquals(2, actual.size());
		List<String> expected = Arrays.asList("US", "GB");
		List<String> actualCountries = actual.stream().map(Locale::getCountry).collect(Collectors.toList());
		assertEquals(expected, actualCountries);
	}

	@Test
	public void testLanguagesByCountry_EmptyList() {
		List<Locale> actual = LocaleUtils.languagesByCountry(null);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void testCountriesByLanguage_EmptyList() {
		List<Locale> actual = LocaleUtils.countriesByLanguage(null);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void testToLocale_ValidLocaleWithInvalidFormat() {
		String invalidLocale = "en_US#";
		assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale));
	}

	@Test
	public void testLocaleLookupList_EmptyList() {
		Locale locale = null;
		List<Locale> actual = LocaleUtils.localeLookupList(locale, null);
		assertTrue(actual.isEmpty());
	}
}