package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocaleNullInput() {
 assertNull(LocaleUtils.toLocale(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleEmptyInput() {
 LocaleUtils.toLocale("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatNoLanguage() {
 LocaleUtils.toLocale("US");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatNoCountry() {
 LocaleUtils.toLocale("en");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatMissingLanguageUpperCase() {
 LocaleUtils.toLocale("en_");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatMissingCountryUpperCase() {
 LocaleUtils.toLocale("en_US_");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatMissingLanguage() {
 LocaleUtils.toLocale("en_US");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatMissingCountry() {
 LocaleUtils.toLocale("en_US_es");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatExtraCharacters() {
 LocaleUtils.toLocale("en_US#");
 }

 @Test
 public void testToLocaleValidFormat() {
 assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
 }

 @Test
 public void testLocaleLookupListNullLocale() {
 assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
 }

 @Test
 public void testLocaleLookupListEmptyLocale() {
 Locale locale = new Locale("");
 assertEquals(Collections.singletonList(locale), LocaleUtils.localeLookupList(locale));
 }

 @Test
 public void testLocaleLookupListValidLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> list = LocaleUtils.localeLookupList(locale);
 assertEquals(3, list.size());
 assertEquals(locale, list.get(0));
 assertEquals(new Locale("en", ""), list.get(1));
 assertEquals(new Locale("", "US"), list.get(2));
 }

 @Test
 public void testLocaleLookupListDefaultLocale() {
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> list = LocaleUtils.localeLookupList(null, defaultLocale);
 assertEquals(4, list.size());
 assertTrue(list.contains(Locale.getDefault()));
 assertTrue(list.contains(defaultLocale));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> list = LocaleUtils.availableLocaleList();
 assertFalse(list.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> set = LocaleUtils.availableLocaleSet();
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIsAvailableLocaleNullInput() {
 assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testIsAvailableLocaleInvalidLocale() {
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
 }

 @Test
 public void testIsAvailableLocaleAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountryValidInput() {
 List<Locale> list = LocaleUtils.languagesByCountry("US");
 assertFalse(list.isEmpty());
 }

 @Test
 public void testLanguagesByCountryEmptyInput() {
 List<Locale> list = LocaleUtils.languagesByCountry(null);
 assertEquals(Collections.emptyList(), list);
 }

 @Test
 public void testCountriesByLanguageValidInput() {
 List<Locale> list = LocaleUtils.countriesByLanguage("en");
 assertFalse(list.isEmpty());
 }

 @Test
 public void testCountriesByLanguageEmptyInput() {
 List<Locale> list = LocaleUtils.countriesByLanguage(null);
 assertEquals(Collections.emptyList(), list);
 }
}