package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LocaleUtilsTest {

 // Test toLocale method with valid arguments
 @Test
 public void testToLocaleValidArguments() {
 String str = "en_US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(str);
 assertEquals("Locale should be created correctly", expectedLocale, actualLocale);
 }

 // Test toLocale method with empty string
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleEmptyString() {
 LocaleUtils.toLocale("");
 }

 // Test toLocale method with null argument
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleNullArgument() {
 LocaleUtils.toLocale(null);
 }

 // Test toLocale method with invalid locale format
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 String str = "en_US#";
 LocaleUtils.toLocale(str);
 }

 // Test localeLookupList method with valid argument
 @Test
 public void testLocaleLookupListValidArgument() {
 Locale locale = new Locale("en", "US");
 List<Locale> expectedList = Arrays.asList(locale, new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);
 assertEquals("LocaleLookupList should return the correct list", expectedList, actualList);
 }

 // Test localeLookupList method with null argument
 @Test
 public void testLocaleLookupListNullArgument() {
 List<Locale> actualList = LocaleUtils.localeLookupList(null);
 assertTrue("LocaleLookupList should return an empty list when locale is null", actualList.isEmpty());
 }

 // Test localeLookupList method with defaultLocale argument
 @Test
 public void testLocaleLookupListDefaultLocale() {
 Locale locale = new Locale("en", "US");
 Locale defaultLocale = new Locale("fr", "FR");
 List<Locale> expectedList = Arrays.asList(locale, new Locale("en", ""), new Locale("", "US"), defaultLocale);
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals("LocaleLookupList should return the correct list", expectedList, actualList);
 }

 // Test availableLocaleList method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedList = LocaleUtils.availableLocaleList();
 List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
 assertEquals("AvailableLocaleList should return the same list as getAvailableLocales()", expectedList, actualList);
 }

 // Test isAvailableLocale method with available locale
 @Test
 public void testIsAvailableLocaleAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue("Locale should be available", LocaleUtils.isAvailableLocale(locale));
 }

 // Test isAvailableLocale method with unavailable locale
 @Test
 public void testIsAvailableLocaleUnavailableLocale() {
 Locale locale = new Locale("xy", "xy");
 assertFalse("Locale should not be available", LocaleUtils.isAvailableLocale(locale));
 }

 // Test languagesByCountry method with valid argument
 @Test
 public void testLanguagesByCountryValidArgument() {
 String countryCode = "US";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"));
 List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals("LanguagesByCountry should return the correct list", expectedList, actualList);
 }

 // Test languagesByCountry method with null argument
 @Test
 public void testLanguagesByCountryNullArgument() {
 List<Locale> actualList = LocaleUtils.languagesByCountry(null);
 assertTrue("LanguagesByCountry should return an empty list when countryCode is null", actualList.isEmpty());
 }

 // Test countriesByLanguage method with valid argument
 @Test
 public void testCountriesByLanguageValidArgument() {
 String languageCode = "en";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "ZA"), new Locale("en", "AU"));
 List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals("CountriesByLanguage should return the correct list", expectedList, actualList);
 }

 // Test countriesByLanguage method with null argument
 @Test
 public void testCountriesByLanguageNullArgument() {
 List<Locale> actualList = LocaleUtils.countriesByLanguage(null);
 assertTrue("CountriesByLanguage should return an empty list when languageCode is null", actualList.isEmpty());
 }

}