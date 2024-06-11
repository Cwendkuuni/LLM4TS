package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

 private String localeString;
 private Locale expectedLocale;

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 {"en_US", new Locale("en", "US")},
 {"fr_FR", new Locale("fr", "FR")},
 {"de_DE", new Locale("de", "DE")},
 {"es_ES", new Locale("es", "ES")},
 {"it_IT", new Locale("it", "IT")},
 {"ja_JP", new Locale("ja", "JP")},
 {"ko_KR", new Locale("ko", "KR")},
 {"zh_CN", new Locale("zh", "CN")},
 {"invalid_locale_1#", null},
 {"invalid_locale_2_en_", null},
 {"invalid_locale_3__", null},
 {"invalid_locale_4_en_invalid", null},
 {"invalid_locale_5__en", null},
 {"invalid_locale_6_en_country_too_long", null}
 });
 }

 public LocaleUtilsTest(String localeString, Locale expectedLocale) {
 this.localeString = localeString;
 this.expectedLocale = expectedLocale;
 }

 @Test
 public void testToLocale() {
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 Assert.assertEquals("Invalid locale format for: " + localeString, expectedLocale, actualLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat() {
 LocaleUtils.toLocale("invalid_locale_1#");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_EmptyLanguage() {
 LocaleUtils.toLocale("invalid_locale_2_en_");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_EmptyCountry() {
 LocaleUtils.toLocale("invalid_locale_3__");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLanguage() {
 LocaleUtils.toLocale("invalid_locale_4_en_invalid");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_EmptyCountryCode() {
 LocaleUtils.toLocale("invalid_locale_5__en");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_CountryCodeTooLong() {
 LocaleUtils.toLocale("invalid_locale_6_en_country_too_long");
 }

 @Test
 public void testLocaleLookupList() {
 List<Locale> actualList = LocaleUtils.localeLookupList(expectedLocale);
 assertEquals("Invalid locale lookup list for: " + expectedLocale, Arrays.asList(expectedLocale), actualList);
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
 List<Locale> actualList = LocaleUtils.localeLookupList(null);
 assertTrue("Invalid locale lookup list for null locale", actualList.isEmpty());
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 List<Locale> actualList = LocaleUtils.localeLookupList(Locale.getDefault());
 List<Locale> expectedList = Arrays.asList(Locale.getDefault(), Locale.ROOT, Locale.getDefault());
 assertEquals("Invalid locale lookup list for default locale", expectedList, actualList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actualList = LocaleUtils.availableLocaleList();
 List<Locale> expectedList = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
 Assert.assertEquals("Invalid available locale list", expectedList, actualList);
 }

 @Test
 public void testIsAvailableLocale() {
 assertTrue("Invalid availability check for: " + expectedLocale, LocaleUtils.isAvailableLocale(expectedLocale));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> expectedList = Arrays.asList(new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "JM"), new Locale("en", "US"));
 Assert.assertEquals("Invalid languages by country list for country code: " + countryCode, expectedList, actualList);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> expectedList = Arrays.asList(new Locale("CA", "ca"), new Locale("GB", "en"), new Locale("HK", "en"), new Locale("IE", "en"), new Locale("JM", "en"), new Locale("US", "en"));
 Assert.assertEquals("Invalid countries by language list for language code: " + languageCode, expectedList, actualList);
 }
}