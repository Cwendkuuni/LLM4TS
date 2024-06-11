package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocaleWithValidArguments() {
 String validLocale1 = "en_US";
 String validLocale2 = "fr_FR";
 String validLocale3 = "es_ES";
 Locale expectedLocale1 = new Locale("en", "US");
 Locale expectedLocale2 = new Locale("fr", "FR");
 Locale expectedLocale3 = new Locale("es", "ES");
 Locale actualLocale1 = LocaleUtils.toLocale(validLocale1);
 Locale actualLocale2 = LocaleUtils.toLocale(validLocale2);
 Locale actualLocale3 = LocaleUtils.toLocale(validLocale3);
 assertEquals(expectedLocale1, actualLocale1);
 assertEquals(expectedLocale2, actualLocale2);
 assertEquals(expectedLocale3, actualLocale3);
 }

 @Test
 public void testToLocaleWithEmptyString() {
 String emptyString = "";
 Locale actualLocale = LocaleUtils.toLocale(emptyString);
 assertNull(actualLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithNullArgument() {
 String nullString = null;
 LocaleUtils.toLocale(nullString);
 }

 @Test
 public void testToLocaleWithInvalidFormat() {
 String invalidLocale1 = "en_US#";
 String invalidLocale2 = "en_US_";
 String invalidLocale3 = "_US_en";
 String invalidLocale4 = "en_US_en";
 String invalidLocale5 = "en_US__";
 String invalidLocale6 = "en_US_en_";
 String invalidLocale7 = "_US_en_";
 String invalidLocale8 = "_US__";
 String invalidLocale9 = "_US_en_";
 String invalidLocale10 = "en_US_en_";
 for (String locale : Arrays.asList(invalidLocale1, invalidLocale2, invalidLocale3, invalidLocale4, invalidLocale5, invalidLocale6, invalidLocale7, invalidLocale8, invalidLocale9, invalidLocale10)) {
 try {
 LocaleUtils.toLocale(locale);
 fail("Expected IllegalArgumentException with " + locale);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
 }

 @Test
 public void testLocaleLookupListWithValidArguments() {
 Locale locale1 = new Locale("en", "US");
 Locale locale2 = new Locale("fr", "FR");
 Locale locale3 = new Locale("es", "ES");
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> expectedLocales1 = Arrays.asList(locale1, locale1, locale2, locale3, defaultLocale);
 List<Locale> expectedLocales2 = Arrays.asList(locale1, locale1, defaultLocale);
 List<Locale> actualLocales1 = LocaleUtils.localeLookupList(locale1, defaultLocale);
 List<Locale> actualLocales2 = LocaleUtils.localeLookupList(locale1);
 assertEquals(expectedLocales1, actualLocales1);
 assertEquals(expectedLocales2, actualLocales2);
 }

 @Test
 public void testLocaleLookupListWithNullLocale() {
 Locale nullLocale = null;
 List<Locale> actualLocales = LocaleUtils.localeLookupList(nullLocale);
 assertTrue(actualLocales.isEmpty());
 }

 @Test
 public void testLocaleLookupListWithEmptyStringLocale() {
 String emptyStringLocale = "";
 List<Locale> actualLocales = LocaleUtils.localeLookupList(new Locale(emptyStringLocale, ""));
 assertTrue(actualLocales.isEmpty());
 }

 @Test
 public void testLocaleLookupListWithInvalidLocale() {
 String invalidLocale = "_US_en_";
 try {
 LocaleUtils.localeLookupList(new Locale(invalidLocale));
 fail("Expected IllegalArgumentException with " + invalidLocale);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLocaleLookupListWithDefaultLocaleOnly() {
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> actualLocales = LocaleUtils.localeLookupList(null);
 assertEquals(Collections.singletonList(defaultLocale), actualLocales);
 }

 @Test
 public void testLocaleLookupListWithEmptyList() {
 Locate emptyLocale = null;
 List<Locale> actualLocales = LocaleUtils.localeLookupList(emptyLocale);
 assertTrue(actualLocales.isEmpty());
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedLocales = LocaleUtils.availableLocaleList();
 List<Locale> actualLocales = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expectedLocales = LocaleUtils.availableLocaleSet();
 Set<Locale> actualLocales = Collections.unmodifiableSet(Arrays.asList(Locale.getAvailableLocales()));
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testIsAvailableLocaleWithValidLocales() {
 Locale locale1 = new Locale("en", "US");
 Locale locale2 = new Locale("fr", "FR");
 Locale locale3 = new Locale("es", "ES");
 assertTrue(LocaleUtils.isAvailableLocale(locale1));
 assertTrue(LocaleUtils.isAvailableLocale(locale2));
 assertTrue(LocaleUtils.isAvailableLocale(locale3));
 }

 @Test
 public void testIsAvailableLocaleWithInvalidLocales() {
 String invalidLocale1 = "_US_en_";
 String invalidLocale2 = "en_US#";
 String invalidLocale3 = "en_US_";
 String invalidLocale4 = "_US_en";
 String invalidLocale5 = "_US__";
 String invalidLocale6 = "_US_en_";
 String invalidLocale7 = "en_US_en";
 for (String locale : Arrays.asList(invalidLocale1, invalidLocale2, invalidLocale3, invalidLocale4, invalidLocale5, invalidLocale6, invalidLocale7)) {
 assertFalse(LocaleUtils.isAvailableLocale(new Locale(locale)));
 }
 }

 @Test
 public void testLanguagesByCountryWithValidArguments() {
 String usCountryCode = "US";
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "JM"), new Locale("en", "TT"), new Locale("en", "ZW"));
 List<Locale> actualLocales = LocaleUtils.languagesByCountry(usCountryCode);
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testLanguagesByCountryWithNullCountryCode() {
 String nullCountryCode = null;
 List<Locale> actualLocales = LocaleUtils.languagesByCountry(nullCountryCode);
 assertTrue(actualLocales.isEmpty());
 }

 @Test
 public void testCountriesByLanguageWithValidArguments() {
 String engLanguageCode = "en";
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "AU"), new Locale("en", "BZ"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "JM"), new Locale("en", "TT"), new Locale("en", "ZW"), new Locale("en", "US"));
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage(engLanguageCode);
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testCountriesByLanguageWithNullLanguageCode() {
 String nullLanguageCode = null;
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage(nullLanguageCode);
 assertTrue(actualLocales.isEmpty());
 }
}