package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class LocaleUtilsTest {

 private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap<String, List<Locale>>();
 private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap<String, List<Locale>>();

 @Test
 public void testToLocale_EmptyString() {
     assertNull(LocaleUtils.toLocale(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_Null() {
     assertNull(LocaleUtils.toLocale(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoLanguage() {
     assertNull(LocaleUtils.toLocale("AB"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoCountry() {
     assertNull(LocaleUtils.toLocale("en"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidCountryCode() {
     assertNull(LocaleUtils.toLocale("en_XXX"));
 }

 @Test
 public void testToLocale_ValidFormat_EmptyLanguage() {
     Locale locale = LocaleUtils.toLocale("_XX");
     assertEquals("", locale.getCountry());
     assertEquals("", locale.getLanguage());
 }

 @Test
 public void testToLocale_ValidFormat_LanguageOnly() {
     Locale locale = LocaleUtils.toLocale("en");
     assertEquals("en", locale.getLanguage());
     assertEquals("", locale.getCountry());
 }

 @Test
 public void testToLocale_ValidFormat_LanguageCountry() {
     Locale locale = LocaleUtils.toLocale("en_US");
     assertEquals("en", locale.getLanguage());
     assertEquals("US", locale.getCountry());
 }

 @Test
 public void testToLocale_ValidFormat_LanguageCountryVariants() {
     Locale locale = LocaleUtils.toLocale("en_US_POSIX");
     assertEquals("en", locale.getLanguage());
     assertEquals("US", locale.getCountry());
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
     List<Locale> result = LocaleUtils.localeLookupList(null);
     assertTrue(result.isEmpty());
 }

 @Test
 public void testLocaleLookupList_ValidLocale() {
     Locale locale = new Locale("en", "US");
     List<Locale> result = LocaleUtils.localeLookupList(locale);
     assertEquals(3, result.size());
     assertEquals(locale, result.get(0));
     assertEquals(new Locale("en", ""), result.get(1));
     assertEquals(new Locale("", "US"), result.get(2));
 }

 @Test
 public void testLocaleLookupList_NullDefaultLocale() {
     Locale locale = new Locale("en", "US");
     List<Locale> result = LocaleUtils.localeLookupList(locale, null);
     assertEquals(4, result.size());
     assertEquals(locale, result.get(0));
     assertEquals(new Locale("en", ""), result.get(1));
     assertEquals(new Locale("", "US"), result.get(2));
     assertNull(result.get(3));
 }

 @Test
 public void testLocaleLookupList_ValidLocaleAndDefaultLocale() {
     Locale locale = new Locale("en", "US");
     Locale defaultLocale = new Locale("fr", "FR");
     List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
     assertEquals(4, result.size());
     assertEquals(locale, result.get(0));
     assertEquals(new Locale("en", ""), result.get(1));
     assertEquals(new Locale("", "US"), result.get(2));
     assertEquals(defaultLocale, result.get(3));
 }

 @Test
 public void testAvailableLocaleList_Empty() {
     List<Locale> result = LocaleUtils.availableLocaleList();
     assertTrue(result.isEmpty());
 }

 @Test
 public void testAvailableLocaleList_NonEmpty() {
     List<Locale> result = LocaleUtils.availableLocaleList();
     assertFalse(result.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet_Empty() {
     Set<Locale> result = LocaleUtils.availableLocaleSet();
     assertTrue(result.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet_NonEmpty() {
     Set<Locale> result = LocaleUtils.availableLocaleSet();
     assertFalse(result.isEmpty());
 }

 @Test
 public void testIsAvailableLocale_NullLocale() {
     assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
     Locale locale = new Locale("", "");
     assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_ValidLocale() {
     Locale locale = new Locale("en", "US");
     assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry_EmptyString() {
     List<Locale> result = LocaleUtils.languagesByCountry("");
     assertTrue(result.isEmpty());
 }

 @Test
 public void testLanguagesByCountry_ValidCountryCode() {
     List<Locale> result = LocaleUtils.languagesByCountry("US");
     assertFalse(result.isEmpty());
 }

 @Test
 public void testCountriesByLanguage_EmptyString() {
     List<Locale> result = LocaleUtils.countriesByLanguage("");
     assertTrue(result.isEmpty());
 }

 @Test
 public void testCountriesByLanguage_ValidLanguageCode() {
     List<Locale> result = LocaleUtils.countriesByLanguage("en");
     assertFalse(result.isEmpty());
 }
}