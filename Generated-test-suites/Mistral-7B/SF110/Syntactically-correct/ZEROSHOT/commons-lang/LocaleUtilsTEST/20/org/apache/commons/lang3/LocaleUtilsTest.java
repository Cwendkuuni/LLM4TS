package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

 private static final String INVALID_LOCALE_FORMAT = "Invalid locale format: {0}";

 @Test
 public void testToLocale_EmptyString() {
     assertNull(LocaleUtils.toLocale(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_NullString() {
     LocaleUtils.toLocale(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoLanguage() {
     String invalidLocale = "_invalid";
     assertNull(LocaleUtils.toLocale(invalidLocale));
     String message = String.format(INVALID_LOCALE_FORMAT, invalidLocale);
     assertEquals(message, new IllegalArgumentException(message).getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoCountry() {
     String invalidLocale = "language_";
     assertNull(LocaleUtils.toLocale(invalidLocale));
     String message = String.format(INVALID_LOCALE_FORMAT, invalidLocale);
     assertEquals(message, new IllegalArgumentException(message).getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidLanguage() {
     String invalidLocale = "language_invalidCountry";
     assertNull(LocaleUtils.toLocale(invalidLocale));
     String message = String.format(INVALID_LOCALE_FORMAT, invalidLocale);
     assertEquals(message, new IllegalArgumentException(message).getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidCountry() {
     String invalidLocale = "language_invalidCountry_";
     assertNull(LocaleUtils.toLocale(invalidLocale));
     String message = String.format(INVALID_LOCALE_FORMAT, invalidLocale);
     assertEquals(message, new IllegalArgumentException(message).getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_EmptyCountry() {
     String invalidLocale = "language_";
     assertNull(LocaleUtils.toLocale(invalidLocale));
     String message = String.format(INVALID_LOCALE_FORMAT, invalidLocale);
     assertEquals(message, new IllegalArgumentException(message).getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidLanguageAndCountry() {
     String invalidLocale = "language_invalidCountry_invalid";
     assertNull(LocaleUtils.toLocale(invalidLocale));
     String message = String.format(INVALID_LOCALE_FORMAT, invalidLocale);
     assertEquals(message, new IllegalArgumentException(message).getMessage());
 }

 @Test
 public void testToLocale_ValidFormat() {
     Locale locale = LocaleUtils.toLocale("en_US");
     assertEquals("en", locale.getLanguage());
     assertEquals("US", locale.getCountry());
 }

 @Test
 public void testToLocale_ValidFormat_EmptyVariant() {
     Locale locale = LocaleUtils.toLocale("en_");
     assertEquals("en", locale.getLanguage());
     assertEquals("", locale.getCountry());
     assertEquals("", locale.getVariant());
 }

 @Test
 public void testToLocale_ValidFormat_NonEmptyVariant() {
     Locale locale = LocaleUtils.toLocale("en_US_variant");
     assertEquals("en", locale.getLanguage());
     assertEquals("US", locale.getCountry());
     assertEquals("variant", locale.getVariant());
 }

 @Test
 public void testLocaleLookupList() {
     Locale locale = new Locale("en", "US");
     List<Locale> locales = LocaleUtils.localeLookupList(locale);
     List<Locale> expectedLocales = Arrays.asList(locale, new Locale("en", ""), new Locale("", "US"));
     assertEquals(expectedLocales, locales);
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
     List<Locale> locales = LocaleUtils.localeLookupList(null);
     assertEquals(Collections.emptyList(), locales);
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
     List<Locale> locales = LocaleUtils.localeLookupList(Locale.getDefault());
     List<Locale> expectedLocales = Arrays.asList(Locale.getDefault(), new Locale("", ""));
     assertEquals(expectedLocales, locales);
 }

 @Test
 public void testLocaleLookupList_DefaultLocale_EmptyCountry() {
     Locale locale = new Locale("", "");
     List<Locale> locales = LocaleUtils.localeLookupList(locale);
     List<Locale> expectedLocales = Arrays.asList(locale, new Locale("", ""), Locale.getDefault());
     assertEquals(expectedLocales, locales);
 }

 @Test
 public void testLocaleLookupList_InvalidLocale() {
     String invalidLocale = "_invalid";
     List<Locale> locales = LocaleUtils.localeLookupList(new Locale(invalidLocale));
     assertEquals(Collections.emptyList(), locales);
 }

 @Test
 public void testAvailableLocaleList() {
     List<Locale> locales = LocaleUtils.availableLocaleList();
     assertFalse(locales.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet() {
     Set<Locale> locales = LocaleUtils.availableLocaleSet();
     assertFalse(locales.isEmpty());
 }

 @Test
 public void testIsAvailableLocale() {
     Locale locale = new Locale("en", "US");
     assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
     Locale locale = new Locale("_invalid");
     assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
     List<Locale> expectedLocales = Arrays.asList(new Locale("es", "ES"), new Locale("es", "MX"), new Locale("es", "AR"));
     List<Locale> locales = LocaleUtils.languagesByCountry("ES");
     assertEquals(expectedLocales, locales);
 }

 @Test
 public void testLanguagesByCountry_InvalidCountryCode() {
     List<Locale> locales = LocaleUtils.languagesByCountry(null);
     assertEquals(Collections.emptyList(), locales);
 }

 @Test
 public void testCountriesByLanguage() {
     List<Locale> expectedLocales = Arrays.asList(new Locale("es", "ES"), new Locale("es", "MX"), new Locale("es", "AR"), new Locale("es", "VE"), new Locale("es", "GT"));
     List<Locale> locales = LocaleUtils.countriesByLanguage("es");
     assertEquals(expectedLocales, locales);
 }

 @Test
 public void testCountriesByLanguage_InvalidLanguageCode() {
     List<Locale> locales = LocaleUtils.countriesByLanguage(null);
     assertEquals(Collections.emptyList(), locales);
 }
}