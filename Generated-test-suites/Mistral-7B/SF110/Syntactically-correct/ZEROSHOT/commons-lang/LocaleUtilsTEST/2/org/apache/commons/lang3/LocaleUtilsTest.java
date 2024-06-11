package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

 private static final String INVALID_LOCALE = "invalidLocale";
 private static final String COUNTRY_CODE = "US";
 private static final String LANGUAGE_CODE = "en";
 private static final String THREE_LETTER_LANGUAGE_CODE = "eng";
 private static final String LANGUAGE_AND_COUNTRY = "en_US";
 private static final String LANGUAGE_AND_VARIANT = "en_US_Custom";
 private static final String COUNTRY_AND_VARIANT = "US_Custom";
 private static final String LANGUAGE = "en";
 private static final String COUNTRY = "US";
 private static final String EMPTY_STRING = "";

 @Test
 public void testNullLocale() {
 Locale locale = LocaleUtils.toLocale(null);
 assertEquals(null, locale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyLocale() {
 Locale locale = LocaleUtils.toLocale(EMPTY_STRING);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidLocaleFormat() {
 Locale locale = LocaleUtils.toLocale("en#US");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testValidLocaleFormatWithLanguageAndCountry() {
 Locale locale = LocaleUtils.toLocale(LANGUAGE_AND_COUNTRY);
 assertEquals(LANGUAGE, locale.getLanguage());
 assertEquals(COUNTRY, locale.getCountry());
 }

 @Test
 public void testValidLocaleFormatWithLanguageAndCountryAndVariant() {
 Locale locale = LocaleUtils.toLocale(LANGUAGE_AND_VARIANT);
 assertEquals(LANGUAGE, locale.getLanguage());
 assertEquals(COUNTRY, locale.getCountry());
 assertEquals("Custom", locale.getVariant());
 }

 @Test
 public void testValidLocaleFormatWithCountryAndVariant() {
 Locale locale = LocaleUtils.toLocale(COUNTRY_AND_VARIANT);
 assertEquals(COUNTRY, locale.getCountry());
 assertEquals("Custom", locale.getVariant());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidLocaleFormatWithTwoLetterLanguageCodeAndEmptyCountry() {
 Locale locale = LocaleUtils.toLocale("en");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = Locale.forLanguageTag("en_US");
 List<Locale> locales = LocaleUtils.localeLookupList(locale);
 assertEquals(4, locales.size());
 assertTrue(locales.contains(locale));
 assertFalse(locales.contains(Locale.forLanguageTag("fr_FR")));
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale defaultLocale = Locale.getDefault();
 List<Locale> locales = LocaleUtils.localeLookupList(Locale.forLanguageTag("en_US"), defaultLocale);
 assertEquals(5, locales.size());
 assertTrue(locales.contains(Locale.forLanguageTag("en_US")));
 assertTrue(locales.contains(Locale.forLanguageTag("en")));
 assertTrue(locales.contains(Locale.forLanguageTag("US")));
 assertTrue(locales.contains(defaultLocale));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> locales = LocaleUtils.availableLocaleList();
 assertEquals(130, locales.size());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> locales = LocaleUtils.availableLocaleSet();
 assertEquals(130, locales.size());
 }

 @Test
 public void testIsAvailableLocale() {
 assertTrue(LocaleUtils.isAvailableLocale(Locale.forLanguageTag("en_US")));
 assertFalse(LocaleUtils.isAvailableLocale(Locale.forLanguageTag("xx_XX")));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> locales = LocaleUtils.languagesByCountry(COUNTRY_CODE);
 assertEquals(23, locales.size());
 assertTrue(locales.contains(Locale.forLanguageTag("en_US")));
 assertTrue(locales.contains(Locale.forLanguageTag("fr_CA")));
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> locales = LocaleUtils.countriesByLanguage(LANGUAGE_CODE);
 assertEquals(3, locales.size());
 assertTrue(locales.contains(Locale.forLanguageTag("en_US")));
 assertTrue(locales.contains(Locale.forLanguageTag("es_ES")));
 }

 @Test
 public void testLocaleUtilsInitialization() {
 assertNotNull(LocaleUtils.cLanguagesByCountry);
 assertNotNull(LocaleUtils.cCountriesByLanguage);
 }

 @Test
 public void testConcurrentModificationException() {
 List<Locale> locales = Arrays.asList(Locale.forLanguageTag("en_US"), Locale.forLanguageTag("es_ES"));
 List<Locale> expectedLocales = new ArrayList<>(locales);
 expectedLocales.remove(0);
 LocaleUtils.languagesByCountry(COUNTRY_CODE);
 try {
 LocaleUtils.cLanguagesByCountry.remove(COUNTRY_CODE);
 fail("Expected ConcurrentModificationException");
 } catch (ConcurrentModificationException e) {
 // Expected
 }
 }

}