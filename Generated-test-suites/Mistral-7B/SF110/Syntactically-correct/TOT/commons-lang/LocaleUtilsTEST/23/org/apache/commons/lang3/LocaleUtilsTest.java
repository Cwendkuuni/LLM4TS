package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_ValidLocale() {
 String localeString = "en_US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);
 }

 @Test
 public void testToLocale_EmptyString() {
 String localeString = "";
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertNull(actualLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoLanguage() {
 String localeString = "_US";
 LocaleUtils.toLocale(localeString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoCountry() {
 String localeString = "en";
 LocaleUtils.toLocale(localeString);
 }

 @Test
 public void testToLocale_InvalidFormat_LanguageAndCountry() {
 String localeString = "en_";
 LocaleUtils.toLocale(localeString);
 }

 @Test
 public void testToLocale_ValidLocaleWithVariants() {
 String localeString = "en_US_POSIX";
 Locale expectedLocale = new Locale("en", "US", "POSIX");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidCharacter() {
 String localeString = "en#US";
 LocaleUtils.toLocale(localeString);
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
 Locale locale = null;
 List<Locale> expectedLocales = Collections.emptyList();
 List<Locale> actualLocales = LocaleUtils.localeLookupList(locale);
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testLocaleLookupList_ValidLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> expectedLocales = Arrays.asList(locale, new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"));
 List<Locale> actualLocales = LocaleUtils.localeLookupList(locale);
 assertArrayEquals(expectedLocales.toArray(), actualLocales.toArray(new Locale[0]));
 }

 @Test(expected = NullPointerException.class)
 public void testLocaleLookupList_NullDefaultLocale() {
 Locale locale = new Locale("en", "US");
 Locale defaultLocale = null;
 LocaleUtils.localeLookupList(locale, defaultLocale);
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 Locale locale = new Locale("en", "US");
 Locale defaultLocale = new Locale("fr", "FR");
 List<Locale> expectedLocales = Arrays.asList(locale, new Locale("en", "US"), new Locale("en", ""), defaultLocale);
 List<Locale> actualLocales = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertArrayEquals(expectedLocales.toArray(), actualLocales.toArray(new Locale[0]));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedLocales = LocaleUtils.availableLocaleList();
 List<Locale> actualLocales = Arrays.asList(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST.toArray(new Locale[0]));
 assertArrayEquals(expectedLocales.toArray(), actualLocales.toArray(new Locale[0]));
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expectedLocales = LocaleUtils.availableLocaleSet();
 Set<Locale> actualLocales = new HashSet<>(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET);
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testIsAvailableLocale_AvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
 Locale locale = new Locale("en", "US_POSIX");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry_ValidCountryCode() {
 String countryCode = "US";
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"));
 List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);
 assertArrayEquals(expectedLocales.toArray(), actualLocales.toArray(new Locale[0]));
 }

 @Test
 public void testLanguagesByCountry_EmptyCountryCode() {
 String countryCode = "";
 List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);
 assertEmpty(actualLocales);
 }

 @Test
 public void testCountriesByLanguage_ValidLanguageCode() {
 String languageCode = "en";
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"));
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);
 assertArrayEquals(expectedLocales.toArray(), actualLocales.toArray(new Locale[0]));
 }

 @Test
 public void testCountriesByLanguage_EmptyLanguageCode() {
 String languageCode = "";
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);
 assertEmpty(actualLocales);
 }
}