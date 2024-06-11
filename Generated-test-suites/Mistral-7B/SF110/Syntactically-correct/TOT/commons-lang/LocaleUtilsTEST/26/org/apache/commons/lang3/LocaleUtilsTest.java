package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.*;

@RunWith(JUnit4.class)
public class LocaleUtilsTest {

 @Test
 public void testToLocaleWithValidInput() {
 String input = "en_US";
 Locale expected = new Locale("en", "US");
 Locale actual = LocaleUtils.toLocale(input);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithEmptyInput() {
 LocaleUtils.toLocale("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidFormat() {
 LocaleUtils.toLocale("en_US#");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidLanguageCode() {
 LocaleUtils.toLocale("xx_US");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidCountryCode() {
 LocaleUtils.toLocale("en_XX");
 }

 @Test
 public void testLocaleLookupListWithValidLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> expected = LocaleUtils.localeLookupList(locale);
 List<Locale> actual = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); ++i) {
 assertEquals(expected.get(i), actual.get(i));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testLocaleLookupListWithNullLocale() {
 LocaleUtils.localeLookupList(null);
 }

 @Test
 public void testLocaleLookupListWithNullDefaultLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
 List<Locale> actual = LocaleUtils.localeLookupList(locale, null);
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); ++i) {
 assertEquals(expected.get(i), actual.get(i));
 }
 }

 @Test
 public void testLocaleLookupListWithValidDefaultLocale() {
 Locale locale = new Locale("en", "US");
 Locale defaultLocale = new Locale("fr", "FR");
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"), new Locale("fr", "FR"));
 List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); ++i) {
 assertEquals(expected.get(i), actual.get(i));
 }
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expected = LocaleUtils.availableLocaleList();
 List<Locale> actual = Arrays.asList(LocaleUtils.syncAvoid().AVAILABLE_LOCALE_LIST.toArray(new Locale[0]));
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); ++i) {
 assertEquals(expected.get(i), actual.get(i));
 }
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expected = LocaleUtils.availableLocaleSet();
 Set<Locale> actual = Arrays.asList(LocaleUtils.syncAvoid().AVAILABLE_LOCALE_SET.toArray(new Locale[0])).stream().collect(Collectors.toSet());
 assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocaleWithValidLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocaleWithInvalidLocale() {
 Locale locale = new Locale("xx", "US");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "JM"), new Locale("en", "SG"), new Locale("en", "ZW"));
 List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); ++i) {
 assertEquals(expected.get(i), actual.get(i));
 }
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "JM"), new Locale("en", "SG"), new Locale("en", "ZW"), new Locale("en", "AU"), new Locale("en", "NZ"), new Locale("en", "TT"), new Locale("en", "DM"), new Locale("en", "SH"), new Locale("en", "MP"), new Locale("en", "VC"), new Locale("en", "GG"), new Locale("en", "IM"), new Locale("en", "JE"), new Locale("en", "CI"), new Locale("en", "HK"), new Locale("en", "MO"), new Locale("en", "GB-IE"));
 List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); ++i) {
 assertEquals(expected.get(i), actual.get(i));
 }
 }
}