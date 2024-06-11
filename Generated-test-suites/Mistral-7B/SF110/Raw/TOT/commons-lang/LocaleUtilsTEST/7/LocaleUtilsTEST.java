package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.test.ifjunit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

 private String input;
 private Locale expected;

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 // Valid test cases
 {"en", new Locale("en")},
 {"en_US", new Locale("en", "US")},
 {"fr_FR", new Locale("fr", "FR")},
 // Edge cases
 {"es", new Locale("es")},
 {"es_ES", new Locale("es", "ES")},
 {"de_DE", new Locale("de", "DE")},
 {"zh_CN", new Locale("zh", "CN")},
 {"ja_JP", new Locale("ja", "JP")},
 // Error test cases
 {"", null},
 {"en_", null},
 {"en_US#", null},
 {"en_US_", null},
 {"en_US#abc", null},
 {"en_US_abc", null},
 {"en_US_abc#", null},
 {"en_US_abc#def", null},
 {"en_US_#abc", null},
 {"en_US_#abc#def", null},
 // Invalid locale formats
 {"en_US_en", null},
 {"en_US__en", null},
 {"en_US_en_en", null},
 {"en_US___en", null},
 {"en_US_en_en_en", null},
 {"en_US___en_en", null},
 {"en_US_en_en_en_en", null},
 {"en_US____en", null},
 {"en_US____en_en", null},
 {"en_US____en_en_en", null},
 {"en_US____en_en_en_en_en", null},
 {"en_US____en___en", null},
 {"en_US____en___en_en", null},
 {"en_US____en___en_en_en", null},
 {"en_US____en___en_en_en_en", null}
 });
 }

 public LocaleUtilsTest(String input, Locale expected) {
 this.input = input;
 this.expected = expected;
 }

 @Test
 public void testToLocale() {
 Locale actual = LocaleUtils.toLocale(input);
 Assert.assertEquals("Test toLocale with valid input", expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidInput() {
 LocaleUtils.toLocale(input);
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "FR");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = Arrays.asList(locale, new Locale("fr", ""), new Locale("", "FR"));
 Assert.assertEquals("Test localeLookupList with valid locale", expected, actual);
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
 List<Locale> actual = LocaleUtils.localeLookupList(null);
 List<Locale> expected = Collections.emptyList();
 Assert.assertEquals("Test localeLookupList with null locale", expected, actual);
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> actual = LocaleUtils.localeLookupList(null, defaultLocale);
 List<Locale> expected = Arrays.asList(defaultLocale);
 Assert.assertEquals("Test localeLookupList with null locale and default locale", expected, actual);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
 Assert.assertEquals("Test availableLocaleList", expected, actual);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> actual = LocaleUtils.availableLocaleSet();
 Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
 Assert.assertEquals("Test availableLocaleSet", expected, actual);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("fr", "FR");
 Assert.assertTrue("Test isAvailableLocale with available locale", LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
 Locale locale = new Locale("es", "US");
 Assert.assertFalse("Test isAvailableLocale with invalid locale", LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", "IE"), new Locale("en", "AU"), new Locale("en", "NZ"));
 Assert.assertEquals("Test languagesByCountry with valid country code", expected, actual);
 }

 @Test
 public void testLanguagesByCountry_NullCountryCode() {
 List<Locale> actual = LocaleUtils.languagesByCountry(null);
 List<Locale> expected = Collections.emptyList();
 Assert.assertEquals("Test languagesByCountry with null country code", expected, actual);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> expected = Arrays.asList(new Locale("US", "en"), new Locale("CA", "en"), new Locale("GB", "en"), new Locale("HK", "en"), new Locale("IE", "en"), new Locale("AU", "en"), new Locale("NZ", "en"));
 Assert.assertEquals("Test countriesByLanguage with valid language code", expected, actual);
 }

 @Test
 public void testCountriesByLanguage_NullLanguageCode() {
 List<Locale> actual = LocaleUtils.countriesByLanguage(null);
 List<Locale> expected = Collections.emptyList();
 Assert.assertEquals("Test countriesByLanguage with null language code", expected, actual);
 }
}