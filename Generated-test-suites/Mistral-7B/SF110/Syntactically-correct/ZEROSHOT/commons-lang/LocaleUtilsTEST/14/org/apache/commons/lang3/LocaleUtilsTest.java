package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

 private static final String[] VALID_LOCALE_STRINGS = new String[]{"en", "en_US", "fr_FR", "es_ES", "de_DE"};
 private static final String[] INVALID_LOCALE_STRINGS = new String[]{"", "en_", "en_US#", "en_US_", "en_US__", "en_US___", "fr_FR#", "fr_FR_", "fr_FR__", "fr_FR____"};

 @Test
 public void testToLocale() {
 for (String localeString : VALID_LOCALE_STRINGS) {
 Locale locale = LocaleUtils.toLocale(localeString);
 assertNotNull(locale);
 }

 for (String localeString : INVALID_LOCALE_STRINGS) {
 Locale locale = LocaleUtils.toLocale(localeString);
 assertNull(locale);
 }
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("es", "ES");
 List<Locale> localeList = LocaleUtils.localeLookupList(locale);
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("es", "ES"), new Locale("es"), new Locale(""), new Locale(""));
 assertTrue(localeList.equals(expectedLocaleList));

 locale = null;
 localeList = LocaleUtils.localeLookupList(locale, new Locale("en", "US"));
 assertTrue(localeList.size() == 4);
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale locale = new Locale("es", "ES");
 List<Locale> localeList = LocaleUtils.localeLookupList(locale);
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("es", "ES"), new Locale("es"), new Locale(""), new Locale("en", "US"));
 assertTrue(localeList.equals(expectedLocaleList));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> localeList = LocaleUtils.availableLocaleList();
 assertFalse(localeList.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
 assertFalse(localeSet.isEmpty());
 }

 @Test
 public void testIsAvailableLocale() {
 List<Locale> localeList = Arrays.asList(new Locale("es", "ES"), new Locale("fr", "FR"), new Locale("en", "US"));
 for (Locale locale : localeList) {
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 Locale invalidLocale = new Locale("", "");
 assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "FR"), new Locale("it", "IT"));
 List<Locale> localeList = LocaleUtils.languagesByCountry("ca");
 assertTrue(localeList.equals(expectedLocaleList));

 localeList = LocaleUtils.languagesByCountry(null);
 assertNull(localeList);
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("ca", "CA"), new Locale("fr", "CA"), new Locale("it", "IT"));
 List<Locale> localeList = LocaleUtils.countriesByLanguage("it");
 assertTrue(localeList.equals(expectedLocaleList));

 localeList = LocaleUtils.countriesByLanguage(null);
 assertNull(localeList);
 }
}