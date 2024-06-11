import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

 // Test toLocale method with valid inputs
 @Test
 public void testToLocaleValidInput() {
 String testString = "en_US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(testString);
 assertEquals(expectedLocale, actualLocale);
 }

 // Test toLocale method with empty string
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleEmptyString() {
 LocaleUtils.toLocale("");
 }

 // Test toLocale method with null input
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleNullInput() {
 LocaleUtils.toLocale(null);
 }

 // Test toLocale method with invalid locale format
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidLocaleFormat() {
 String testString = "en_US#";
 LocaleUtils.toLocale(testString);
 }

 // Test toLocale method with invalid number of parts
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidNumberOfParts() {
 String testString = "en";
 LocaleUtils.toLocale(testString);
 }

 // Test localeLookupList method with valid input
 @Test
 public void testLocaleLookupListValidInput() {
 Locale testLocale = new Locale("en", "US");
 List<Locale> expectedList = LocaleUtils.localeLookupList(testLocale);
 List<Locale> actualList = LocaleUtils.localeLookupList(testLocale, testLocale);
 assertEquals(expectedList, actualList);
 }

 // Test localeLookupList method with null input
 @Test
 public void testLocaleLookupListNullInput() {
 Locale testLocale = null;
 List<Locale> expectedList = Collections.emptyList();
 List<Locale> actualList = LocaleUtils.localeLookupList(testLocale);
 assertEquals(expectedList, actualList);
 }

 // Test localeLookupList method with invalid locale
 @Test(expected = IllegalArgumentException.class)
 public void testLocaleLookupListInvalidLocale() {
 Locale testLocale = new Locale("invalid", "US");
 List<Locale> expectedList = Collections.emptyList();
 List<Locale> actualList = LocaleUtils.localeLookupList(testLocale);
 assertEquals(expectedList, actualList);
 }

 // Test availableLocaleList method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedList = LocaleUtils.availableLocaleList();
 int expectedSize = Locale.getAvailableLocales().length;
 int actualSize = expectedList.size();
 assertEquals(expectedSize, actualSize);
 }

 // Test availableLocaleSet method
 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
 int expectedSize = Locale.getAvailableLocales().length;
 int actualSize = expectedSet.size();
 assertEquals(expectedSize, actualSize);
 }

 // Test isAvailableLocale method with available locale
 @Test
 public void testIsAvailableLocaleAvailableLocale() {
 Locale testLocale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(testLocale));
 }

 // Test isAvailableLocale method with invalid locale
 @Test(expected = IllegalArgumentException.class)
 public void testIsAvailableLocaleInvalidLocale() {
 Locale testLocale = new Locale("invalid", "US");
 assertFalse(LocaleUtils.isAvailableLocale(testLocale));
 }

 // Test languagesByCountry method with valid input
 @Test
 public void testLanguagesByCountryValidInput() {
 String testCountryCode = "US";
 List<Locale> expectedList = LocaleUtils.languagesByCountry(testCountryCode);
 List<Locale> actualList = LocaleUtils.availableLocaleList();
 List<Locale> filteredList = new ArrayList<>();
 for (Locale locale : actualList) {
 if (testCountryCode.equals(locale.getCountry())) {
 filteredList.add(locale);
 }
 }
 assertEquals(expectedList, filteredList);
 }

 // Test languagesByCountry method with null input
 @Test
 public void testLanguagesByCountryNullInput() {
 String testCountryCode = null;
 List<Locale> expectedList = Collections.emptyList();
 List<Locale> actualList = LocaleUtils.languagesByCountry(testCountryCode);
 assertEquals(expectedList, actualList);
 }

 // Test countriesByLanguage method with valid input
 @Test
 public void testCountriesByLanguageValidInput() {
 String testLanguageCode = "en";
 List<Locale> expectedList = LocaleUtils.countriesByLanguage(testLanguageCode);
 List<Locale> actualList = LocaleUtils.availableLocaleList();
 List<Locale> filteredList = new ArrayList<>();
 for (Locale locale : actualList) {
 if (testLanguageCode.equals(locale.getLanguage())) {
 filteredList.add(locale);
 }
 }
 assertEquals(expectedList, filteredList);
 }

 // Test countriesByLanguage method with null input
 @Test
 public void testCountriesByLanguageNullInput() {
 String testLanguageCode = null;
 List<Locale> expectedList = Collections.emptyList();
 List<Locale> actualList = LocaleUtils.countriesByLanguage(testLanguageCode);
 assertEquals(expectedList, actualList);
 }
}