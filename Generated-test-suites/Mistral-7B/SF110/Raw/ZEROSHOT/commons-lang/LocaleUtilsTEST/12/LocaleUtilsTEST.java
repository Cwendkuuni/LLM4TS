package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

 private static final String COUNTRY_CODE = "US";
 private static final String LANGUAGE_CODE = "en";

 @Test
 public void testToLocale_EmptyString() {
 Locale result = LocaleUtils.toLocale("");
 assertNull(result);
 }

 @Test
 public void testToLocale_NullString() {
 Locale result = LocaleUtils.toLocale(null);
 assertNull(result);
 }

 @Test
 public void testToLocale_InvalidFormat_NoLanguage() {
 String str = "_InvalidFormat";
 try {
 Locale result = LocaleUtils.toLocale(str);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 assertEquals("Invalid locale format: _InvalidFormat", e.getMessage());
 }

 @Test
 public void testToLocale_InvalidFormat_NoCountry() {
 String str = "InvalidFormat";
 try {
 Locale result = LocaleUtils.toLocale(str);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 assertEquals("Invalid locale format: InvalidFormat", e.getMessage());
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidCountry() {
 String str = "_InvalidFormat_InvalidCountry";
 try {
 Locale result = LocaleUtils.toLocale(str);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 assertEquals("Invalid locale format: _InvalidFormat_InvalidCountry", e.getMessage());
 }

 @Test
 public void testToLocale_ValidFormat_EmptyLanguage() {
 String str = "_en";
 Locale result = LocaleUtils.toLocale(str);
 assertEquals("", result.getCountry());
 assertEquals("en", result.getLanguage());
 }

 @Test
 public void testToLocale_ValidFormat_EmptyCountry() {
 String str = "en_";
 Locale result = LocaleUtils.toLocale(str);
 assertEquals("en", result.getLanguage());
 assertEquals("", result.getCountry());
 }

 @Test
 public void testToLocale_ValidFormat_ValidLanguageAndCountry() {
 String str = "en_US";
 Locale result = LocaleUtils.toLocale(str);
 assertEquals("US", result.getCountry());
 assertEquals("en", result.getLanguage());
 }

 @Test
 public void testToLocale_ValidFormat_ValidLanguageAndCountryWithVariant() {
 String str = "en_US_Varient";
 Locale result = LocaleUtils.toLocale(str);
 assertEquals("US", result.getCountry());
 assertEquals("en", result.getLanguage());
 assertEquals("Varient", result.getVariant());
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
 assertTrue(result.contains(locale));
 }

 @Test
 public void testLocaleLookupList_ValidLocaleWithVariant() {
 Locale locale = new Locale("en", "US", "Varient");
 List<Locale> result = LocaleUtils.localeLookupList(locale);
 assertEquals(4, result.size());
 assertTrue(result.contains(locale));
 }

 @Test
 public void testLocaleLookupList_NullDefaultLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> result = LocaleUtils.localeLookupList(locale, null);
 assertEquals(3, result.size());
 assertTrue(result.contains(locale));
 }

 @Test
 public void testLocaleLookupList_ValidDefaultLocale() {
 Locale defaultLocale = new Locale("fr", "CA");
 Locale locale = new Locale("en", "US");
 List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(4, result.size());
 assertTrue(result.contains(locale));
 assertTrue(result.contains(new Locale("en", "US")));
 assertTrue(result.contains(new Locale("en", "")));
 assertTrue(result.contains(defaultLocale));
 }

 @Test
 public void testAvailableLocaleList() {
 Collection<Locale> result = LocaleUtils.availableLocaleList();
 assertFalse(result.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet() {
 Collection<Locale> result = LocaleUtils.availableLocaleList();
 Set<Locale> set = new HashSet<>(result);
 assertEquals(result.size(), set.size());
 }

 @Test
 public void testIsAvailableLocale_NotNullLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_NullLocale() {
 assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> result = LocaleUtils.languagesByCountry(COUNTRY_CODE);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testLanguagesByCountry_EmptyCountryCode() {
 List<Locale> result = LocaleUtils.languagesByCountry("");
 assertTrue(result.isEmpty());
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> result = LocaleUtils.countriesByLanguage(LANGUAGE_CODE);
 assertFalse(result.isEmpty());
 }

 @Test
 public void testCountriesByLanguage_EmptyLanguageCode() {
 List<Locale> result = LocaleUtils.countriesByLanguage("");
 assertTrue(result.isEmpty());
 }
}