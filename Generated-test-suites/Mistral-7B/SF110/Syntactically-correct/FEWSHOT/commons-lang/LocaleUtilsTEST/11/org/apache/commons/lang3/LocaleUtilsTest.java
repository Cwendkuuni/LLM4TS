package org.apache.commons.lang3;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_EmptyString() {
 assertNull(LocaleUtils.toLocale(""));
 }

 @Test
 public void testToLocale_NullString() {
 assertNull(LocaleUtils.toLocale(null));
 }

 @Test
 public void testToLocale_InvalidFormat_NoCountry() {
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en"));
 }

 @Test
 public void testToLocale_InvalidFormat_NoLanguage() {
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_fr"));
 }

 @Test
 public void testToLocale_ValidFormat_LanguageOnly() {
 assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
 }

 @Test
 public void testToLocale_ValidFormat_LanguageCountry() {
 assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
 }

 @Test
 public void testToLocale_ValidFormat_LanguageCountryVariant() {
 assertEquals(new Locale("en", "US", "MS"), LocaleUtils.toLocale("en_US_MS"));
 }

 @Test
 public void testToLocale_InvalidFormat_CountryWithoutLanguage() {
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("US"));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidCountryCode() {
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("XX"));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidLanguageCode() {
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("XXX"));
 }

 @Test
 public void testToLocale_InvalidFormat_MultipleUnderscores() {
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en__US"));
 }

 @Test
 public void testToLocale_InvalidFormat_LeadingUnderscore() {
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_en_US"));
 }

 @Test
 public void testToLocale_InvalidFormat_TrailingUnderscore() {
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US_"));
 }

 @Test
 public void testToLocale_InvalidFormat_DoubleUnderscore() {
 assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US__"));
 }

 @Test
 public void testLocaleLookupList() {
 List<Locale> locales = LocaleUtils.localeLookupList(Locale.US);
 assertEquals(4, locales.size());
 locales = LocaleUtils.localeLookupList(Locale.FRANCE);
 assertEquals(3, locales.size());
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
 assertTrue(LocaleUtils.localeLookupList(null).isEmpty());
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 List<Locale> locales = LocaleUtils.localeLookupList(null);
 assertEquals(Locale.getDefault(), locales.get(0));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocales = LocaleUtils.availableLocaleList();
 assertFalse(availableLocales.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
 assertFalse(availableLocales.isEmpty());
 }

 @Test
 public void testIsAvailableLocale() {
 assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> frLocales = LocaleUtils.languagesByCountry("FR");
 assertFalse(frLocales.isEmpty());
 }

 @Test
 public void testLanguagesByCountry_NullCountryCode() {
 assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> enLocales = LocaleUtils.countriesByLanguage("en");
 assertFalse(enLocales.isEmpty());
 }

 @Test
 public void testCountriesByLanguage_NullLanguageCode() {
 assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
 }
}