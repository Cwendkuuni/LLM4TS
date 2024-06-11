package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 private static final String[] VALID_LOCALES = new String[]{"en", "fr_CA", "es_ES", "de_DE"};
 private static final String[] INVALID_LOCALES = new String[]{"", "_invalid", "en#_invalid", "en_invalid#"};

 @Test
 public void testToLocale_ValidLocales() {
 for (String localeString : VALID_LOCALES) {
 Locale locale = LocaleUtils.toLocale(localeString);
 assertNotNull(locale);
 assertEquals(localeString, locale.getDisplayName());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_EmptyString() {
 LocaleUtils.toLocale("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoLanguage() {
 LocaleUtils.toLocale("_invalid");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoCountry() {
 LocaleUtils.toLocale("en#_invalid");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidCountry() {
 LocaleUtils.toLocale("en_invalid#");
 }

 @Test
 public void testToLocale_ComplexFormat() {
 List<String> complexFormatLocales = Arrays.asList("en_US-u-ca-zrs", "fr_FR-u-nu-nu");
 for (String localeString : complexFormatLocales) {
 Locale locale = LocaleUtils.toLocale(localeString);
 assertNotNull(locale);
 }
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA");
 List<Locale> localeList = LocaleUtils.localeLookupList(locale);
 List<Locale> expectedLocaleList = Arrays.asList(locale, new Locale("fr", ""), new Locale("", "CA"), new Locale("", ""));
 assertEquals(expectedLocaleList, localeList);
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
 List<Locale> localeList = LocaleUtils.localeLookupList(null);
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("", ""));
 assertEquals(expectedLocaleList, localeList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> localeList = LocaleUtils.availableLocaleList();
 List<Locale> expectedLocaleList = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
 assertEquals(expectedLocaleList, localeList);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
 Set<Locale> expectedLocaleSet = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
 assertEquals(expectedLocaleSet, localeSet);
 }

 @Test
 public void testIsAvailableLocale_ValidLocale() {
 Locale locale = new Locale("fr", "CA");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
 Locale locale = new Locale("invalid", "invalid");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List<Locale> localeList = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
 assertEquals(expectedLocaleList, localeList);
 }

 @Test
 public void testLanguagesByCountry_EmptyCountryCode() {
 String countryCode = "";
 List<Locale> localeList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(Collections.emptyList(), localeList);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "fr";
 List<Locale> localeList = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
 assertEquals(expectedLocaleList, localeList);
 }

 @Test
 public void testCountriesByLanguage_EmptyLanguageCode() {
 String languageCode = "";
 List<Locale> localeList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(Collections.emptyList(), localeList);
 }
}