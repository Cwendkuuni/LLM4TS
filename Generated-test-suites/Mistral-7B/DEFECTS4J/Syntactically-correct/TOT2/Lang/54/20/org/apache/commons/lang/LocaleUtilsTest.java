import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test case for toLocale() method
 @Test
 public void testToLocale() {
 // Test with valid input
 String localeString = "en_US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);

 // Test with empty string
 assertNull(LocaleUtils.toLocale(""));

 // Test with invalid input
 String invalidLocaleString1 = "en_us"; // lowercase 's' in country code
 String invalidLocaleString2 = "en_GB_en"; // language code appears twice
 String invalidLocaleString3 = "en_GB_aa"; // unknown language code 'aa'
 String invalidLocaleString4 = "en_GB_123"; // invalid country code "123"
 String invalidLocaleString5 = "en_"; // missing country code
 String invalidLocaleString6 = "en"; // missing underscore
 String invalidLocaleString7 = "en_GB"; // missing language code
 String invalidLocaleString8 = "en_null"; // null country code
 String invalidLocaleString9 = "en_GB_null"; // null language code
 String invalidLocaleString10 = "en_GB_aa_zz"; // unknown variant code 'zz'
 for (String invalidLocaleString : Arrays.asList(invalidLocaleString1, invalidLocaleString2, invalidLocaleString3, invalidLocaleString4, invalidLocaleString5, invalidLocaleString6, invalidLocaleString7, invalidLocaleString8, invalidLocaleString9, invalidLocaleString10)) {
 try {
 LocaleUtils.toLocale(invalidLocaleString);
 fail("Expected IllegalArgumentException for invalid locale string: " + invalidLocaleString);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
 }

 // Test case for localeLookupList() method
 @Test
 public void testLocaleLookupList() {
 // Test with null locale
 List<Locale> expectedList1 = new ArrayList<>();
 List<Locale> actualList1 = LocaleUtils.localeLookupList(null);
 assertEquals(expectedList1, actualList1);

 // Test with valid locale
 Locale validLocale = new Locale("fr", "CA", "xxx");
 List<Locale> expectedList2 = new ArrayList<>();
 expectedList2.add(validLocale);
 expectedList2.add(new Locale("fr", "CA"));
 expectedList2.add(new Locale("fr"));
 List<Locale> actualList2 = LocaleUtils.localeLookupList(validLocale);
 assertEquals(expectedList2, actualList2);

 // Test with null defaultLocale
 List<Locale> expectedList3 = new ArrayList<>();
 List<Locale> actualList3 = LocaleUtils.localeLookupList(validLocale, null);
 assertEquals(expectedList2, actualList3);

 // Test with defaultLocale
 Locale defaultLocale = new Locale("en");
 List<Locale> expectedList4 = new ArrayList<>();
 expectedList4.add(validLocale);
 expectedList4.add(new Locale("fr", "CA"));
 expectedList4.add(new Locale("fr"));
 expectedList4.add(defaultLocale);
 List<Locale> actualList4 = LocaleUtils.localeLookupList(validLocale, defaultLocale);
 assertEquals(expectedList4, actualList4);
 }

 // Test case for availableLocaleList() method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedList = LocaleUtils.availableLocaleList();
 List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expectedList, actualList);
 }

 // Test case for availableLocaleSet() method
 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
 Set<Locale> actualSet = new HashSet<>(LocaleUtils.availableLocaleList());
 assertEquals(expectedSet, actualSet);
 }

 // Test case for isAvailableLocale() method
 @Test
 public void testIsAvailableLocale() {
 // Test with available locale
 Locale availableLocale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(availableLocale));

 // Test with invalid locale
 Locale invalidLocale = new Locale("en", "US", "xxx");
 assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
 }

 // Test case for languagesByCountry() method
 @Test
 public void testLanguagesByCountry() {
 // Test with valid country code
 String countryCode = "US";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "US-POSIX"));
 List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedList, actualList);

 // Test with null country code
 List<Locale> emptyList = new ArrayList<>();
 List<Locale> actualList1 = LocaleUtils.languagesByCountry(null);
 assertEquals(emptyList, actualList1);
 }

 // Test case for countriesByLanguage() method
 @Test
 public void testCountriesByLanguage() {
 // Test with valid language code
 String languageCode = "en";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "ZA"), new Locale("en", "AU"), new Locale("en", "NZ"), new Locale("en", "JM"), new Locale("en", "BM"), new Locale("en", "GG"), new Locale("en", "IM"), new Locale("en", "SH"), new Locale("en", "IE"), new Locale("en", "GB-GB"), new Locale("en", "GB-NI"), new Locale("en", "GB-SCT"), new Locale("en", "GB-JE"), new Locale("en", "GB-CI"), new Locale("en", "GB-BM"), new Locale("en", "GB-GS"));
 List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedList, actualList);

 // Test with null language code
 List<Locale> emptyList1 = new ArrayList<>();
 List<Locale> actualList1 = LocaleUtils.countriesByLanguage(null);
 assertEquals(emptyList1, actualList1);
 }
}