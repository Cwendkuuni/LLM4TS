import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 private static final Map<String, List<Locale>> expectedLanguagesByCountry = new HashMap<>();
 private static final Map<String, List<Locale>> expectedCountriesByLanguage = new HashMap<>();

 static {
 // Initialize the expected maps with test data for edge cases and exceptions
 // ...
 }

 @Test
 public void testToLocale_ValidFormat_NoCountry() {
 String testString = "en";
 Locale expectedLocale = new Locale("en");
 Locale actualLocale = LocaleUtils.toLocale(testString);
 assertEquals("Expected locale to match the input string", expectedLocale, actualLocale);
 }

 @Test
 public void testToLocale_ValidFormat_TwoLetterLanguage() {
 String testString = "en";
 Locale expectedLocale = new Locale("en");
 Locale actualLocale = LocaleUtils.toLocale(testString);
 assertEquals("Expected locale to match the input string", expectedLocale, actualLocale);
 }

 @Test
 public void testToLocale_ValidFormat_ThreeLetterLanguage() {
 String testString = "en-US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(testString);
 assertEquals("Expected locale to match the input string", expectedLocale, actualLocale);
 }

 @Test
 public void testToLocale_ValidFormat_LanguageAndCountry() {
 String testString = "en_US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(testString);
 assertEquals("Expected locale to match the input string", expectedLocale, actualLocale);
 }

 @Test
 public void testToLocale_InvalidFormat_MissingLanguage() {
 String testString = "_US";
 try {
 LocaleUtils.toLocale(testString);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertTrue("Expected message to contain 'Invalid locale format'", e.getMessage().contains("Invalid locale format: _US"));
 }
 }

 @Test
 public void testToLocale_InvalidFormat_MissingCountry() {
 String testString = "en";
 try {
 LocaleUtils.toLocale(testString);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertTrue("Expected message to contain 'Invalid locale format'", e.getMessage().contains("Invalid locale format: en"));
 }
 }

 @Test
 public void testToLocale_InvalidFormat_EmptyString() {
 String testString = "";
 try {
 LocaleUtils.toLocale(testString);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertTrue("Expected message to contain 'Invalid locale format'", e.getMessage().contains("Invalid locale format: "));
 }
 }

 @Test
 public void testToLocale_InvalidFormat_MultipleUnderscores() {
 String testString = "en__US";
 try {
 LocaleUtils.toLocale(testString);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertTrue("Expected message to contain 'Invalid locale format'", e.getMessage().contains("Invalid locale format: en__US"));
 }
 }

 @Test
 public void testToLocale_InvalidFormat_TrailingUnderscore() {
 String testString = "en_";
 try {
 LocaleUtils.toLocale(testString);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertTrue("Expected message to contain 'Invalid locale format'", e.getMessage().contains("Invalid locale format: en_"));
 }
 }

 @Test
 public void testLocaleLookupList_ValidLocale() {
 Locale testLocale = new Locale("en", "US");
 List<Locale> expectedList = Arrays.asList(testLocale, new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
 List<Locale> actualList = LocaleUtils.localeLookupList(testLocale);
 assertEquals("Expected the list to match the expected locale list", expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
 List<Locale> actualList = LocaleUtils.localeLookupList(null);
 assertTrue("Expected the list to be empty", actualList.isEmpty());
 }

 @Test
 public void testLocaleLookupList_EmptyStringLocale() {
 String testString = "";
 Locale testLocale = new Locale(testString);
 List<Locale> expectedList = Arrays.asList(testLocale);
 List<Locale> actualList = LocaleUtils.localeLookupList(testLocale);
 assertEquals("Expected the list to match the expected locale list", expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_EmptyCountry() {
 Locale testLocale = new Locale("en", "");
 List<Locale> expectedList = Arrays.asList(testLocale, new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
 List<Locale> actualList = LocaleUtils.localeLookupList(testLocale);
 assertEquals("Expected the list to match the expected locale list", expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 Locale testLocale = null;
 List<Locale> expectedList = Arrays.asList(testLocale);
 List<Locale> actualList = LocaleUtils.localeLookupList(testLocale);
 assertEquals("Expected the list to match the expected locale list", expectedList, actualList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedList = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
 List<Locale> actualList = LocaleUtils.availableLocaleList();
 assertEquals("Expected the two lists to be equal", expectedList, actualList);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expectedSet = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
 Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
 assertEquals("Expected the two sets to be equal", expectedSet, actualSet);
 }

 @Test
 public void testIsAvailableLocale_AvailableLocale() {
 Locale testLocale = new Locale("en", "US");
 assertTrue("Expected isAvailableLocale to return true for an available locale", LocaleUtils.isAvailableLocale(testLocale));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
 Locale testLocale = new Locale("", "");
 assertFalse("Expected isAvailableLocale to return false for an invalid locale", LocaleUtils.isAvailableLocale(testLocale));
 }

 @Test
 public void testLanguagesByCountry() {
 String testCountryCode = "US";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"), new Locale("fr", "CA"), new Locale("fr", "US"));
 List<Locale> actualList = LocaleUtils.languagesByCountry(testCountryCode);
 assertEquals("Expected the list to match the expected locale list", expectedList, actualList);
 }

 @Test
 public void testCountriesByLanguage() {
 String testLanguageCode = "en";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "IN"), new Locale("en", "IE"));
 List<Locale> actualList = LocaleUtils.countriesByLanguage(testLanguageCode);
 assertEquals("Expected the list to match the expected locale list", expectedList, actualList);
 }

 // Add test cases for edge cases and exception handling scenarios identified based on the class source code
 // ...
}