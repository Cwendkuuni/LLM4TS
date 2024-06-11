package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 private static final Locale DEFAULT_LOCALE = new Locale("en", "US");

 @Test
 public void testToLocaleEmptyString() {
     assertNull(LocaleUtils.toLocale(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleNullString() {
     assertNull(LocaleUtils.toLocale(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatNoLanguage() {
     String invalidLocale = "_invalidLocale";
     assertNull(LocaleUtils.toLocale(invalidLocale));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatNoCountry() {
     String invalidLocale = "invalidLocale_";
     assertNull(LocaleUtils.toLocale(invalidLocale));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatInvalidLanguageFormat() {
     String invalidLocale = "_en_invalidCountry";
     assertNull(LocaleUtils.toLocale(invalidLocale));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatInvalidCountryFormat() {
     String invalidLocale = "en__invalidLanguage";
     assertNull(LocaleUtils.toLocale(invalidLocale));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatMixedCaseLanguage() {
     String invalidLocale = "_en_US";
     assertNull(LocaleUtils.toLocale(invalidLocale));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatMixedCaseCountry() {
     String invalidLocale = "en_US_";
     assertNull(LocaleUtils.toLocale(invalidLocale));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatEmptyLanguage() {
     String invalidLocale = "__US";
     assertNull(LocaleUtils.toLocale(invalidLocale));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatEmptyCountry() {
     String invalidLocale = "en_";
     assertNull(LocaleUtils.toLocale(invalidLocale));
 }

 @Test
 public void testToLocaleValidFormat() {
     Locale locale = LocaleUtils.toLocale("en_US");
     assertEquals("en", locale.getLanguage());
     assertEquals("US", locale.getCountry());
 }

 @Test
 public void testToLocaleValidFormatWithVariant() {
     Locale locale = LocaleUtils.toLocale("en_US_abc");
     assertEquals("en", locale.getLanguage());
     assertEquals("US", locale.getCountry());
     assertEquals("abc", locale.getVariant());
 }

 @Test
 public void testLocaleLookupList() {
     Locale locale = new Locale("fr", "CA");
     List<Locale> localeList = LocaleUtils.localeLookupList(locale);
     assertEquals(4, localeList.size());
     assertTrue(localeList.contains(locale));
     assertTrue(localeList.contains(new Locale("fr", "")));
     assertTrue(localeList.contains(new Locale("fr", "CA")));
     assertTrue(localeList.contains(DEFAULT_LOCALE));
 }

 @Test
 public void testLocaleLookupListNullLocale() {
     List<Locale> localeList = LocaleUtils.localeLookupList(null);
     assertEquals(1, localeList.size());
     assertTrue(localeList.contains(DEFAULT_LOCALE));
 }

 @Test
 public void testLocaleLookupListDefaultLocale() {
     List<Locale> localeList = LocaleUtils.localeLookupList(DEFAULT_LOCALE);
     assertEquals(4, localeList.size());
     assertTrue(localeList.contains(DEFAULT_LOCALE));
     assertTrue(localeList.contains(new Locale("en", "US")));
     assertTrue(localeList.contains(new Locale("en", "")));
     assertTrue(localeList.contains(new Locale("", "US")));
 }

 @Test
 public void testAvailableLocaleList() {
     List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
     // Add assertions based on the available locales in your system
 }

 @Test
 public void testIsAvailableLocale() {
     Locale locale = new Locale("fr", "CA");
     assertTrue(LocaleUtils.isAvailableLocale(locale));
     Locale invalidLocale = new Locale("fr", "CA_ABC");
     assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
 }

 @Test
 public void testLanguagesByCountry() {
     List<Locale> frenchLocales = LocaleUtils.languagesByCountry("CA");
     assertEquals(3, frenchLocales.size());
     assertTrue(frenchLocales.contains(new Locale("fr", "CA")));
     assertTrue(frenchLocales.contains(new Locale("fr", "FR")));
     assertTrue(frenchLocales.contains(new Locale("fr", "BE")));
 }

 @Test
 public void testCountriesByLanguage() {
     List<Locale> canadianLocales = LocaleUtils.countriesByLanguage("fr");
     assertEquals(2, canadianLocales.size());
     assertTrue(canadianLocales.contains(new Locale("fr", "CA")));
     assertTrue(canadianLocales.contains(new Locale("fr", "BE")));
 }
}