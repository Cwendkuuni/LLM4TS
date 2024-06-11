import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class LocaleUtilsTest {

 private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountryMock = new ConcurrentHashMap<>();
 private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguageMock = new ConcurrentHashMap<>();

 @Test
 public void testToLocale_ValidFormat_EmptyString() {
     assertNull(LocaleUtils.toLocale(""));
 }

 @Test
 public void testToLocale_ValidFormat_EmptyCountry() {
     List<Locale> expected = new ArrayList<>();
     expected.add(Locale.CHINESE);
     when(cLanguagesByCountryMock.get("CN")).thenReturn(expected);
     Locale actual = LocaleUtils.toLocale("zh_CN");
     assertEquals("Expected locale to be equal to zh_CN", new Locale("zh", "CN"), actual);
 }

 @Test
 public void testToLocale_ValidFormat_EmptyLanguage() {
     List<Locale> expected = new ArrayList<>();
     expected.add(Locale.ENGLISH);
     when(cCountriesByLanguageMock.get("US")).thenReturn(expected);
     Locale actual = LocaleUtils.toLocale("en_US");
     assertEquals("Expected locale to be equal to en_US", new Locale("en", "US"), actual);
 }

 @Test
 public void testToLocale_ValidFormat_ThreeParts() {
     List<Locale> expected = new ArrayList<>();
     expected.add(Locale.GERMAN);
     when(cLanguagesByCountryMock.get("DE")).thenReturn(expected);
     Locale actual = LocaleUtils.toLocale("de_DE");
     assertEquals("Expected locale to be equal to de_DE", new Locale("de", "DE"), actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_LessThanTwoParts() {
     LocaleUtils.toLocale("en");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidCharacter() {
     String localeString = "en#FR";
     LocaleUtils.toLocale(localeString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NotTwoUppercaseLetters() {
     String localeString = "en_fr";
     LocaleUtils.toLocale(localeString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NotThreeParts() {
     String localeString = "en_FR_US";
     LocaleUtils.toLocale(localeString);
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
     List<Locale> expected = new ArrayList<>();
     List<Locale> actual = LocaleUtils.localeLookupList(null);
     assertEquals("Expected empty list for null locale", Collections.emptyList(), actual);
 }

 @Test
 public void testLocaleLookupList_EmptyLocale() {
     Locale locale = Locale.EMPTY;
     List<Locale> expected = new ArrayList<>();
     expected.add(locale);
     List<Locale> actual = LocaleUtils.localeLookupList(locale);
     assertEquals("Expected list to contain locale: " + locale, expected, actual);
 }

 @Test
 public void testLocaleLookupList_NonEmptyLocale() {
     Locale locale = new Locale("en", "US");
     List<Locale> expected = new ArrayList<>();
     expected.add(locale);
     expected.add(Locale.ENGLISH);
     expected.add(Locale.US);
     List<Locale> actual = LocaleUtils.localeLookupList(locale);
     assertEquals("Expected list to match expected locale and default locales", expected, actual);
 }

 @Test
 public void testAvailableLocaleList_Empty() {
     List<Locale> actual = LocaleUtils.availableLocaleList();
     List<Locale> expected = Arrays.asList();
     assertEquals("Expected empty list", expected, actual);
 }

 @Test
 public void testAvailableLocaleList_NonEmpty() {
     List<Locale> actual = LocaleUtils.availableLocaleList();
     List<Locale> expected = Arrays.asList(Locale.CHINESE, Locale.ENGLISH, Locale.GERMAN);
     assertEquals("Expected list to match expected locales", expected, actual);
 }

 @Test
 public void testAvailableLocaleSet_Empty() {
     Set<Locale> actual = LocaleUtils.availableLocaleSet();
     Set<Locale> expected = Collections.emptySet();
     assertEquals("Expected empty set", expected, actual);
 }

 @Test
 public void testAvailableLocaleSet_NonEmpty() {
     Set<Locale> actual = LocaleUtils.availableLocaleSet();
     Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.CHINESE, Locale.ENGLISH, Locale.GERMAN));
     assertEquals("Expected set to match expected locales", expected, actual);
 }

 @Test
 public void testIsAvailableLocale_NullLocale() {
     assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testIsAvailableLocale_EmptyLocale() {
     assertTrue(LocaleUtils.isAvailableLocale(Locale.EMPTY));
 }

 @Test
 public void testIsAvailableLocale_AvailableLocale() {
     assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
     assertFalse(LocaleUtils.isAvailableLocale(new Locale("InvalidLanguage", "InvalidCountry")));
 }

 @Test(expected = NullPointerException.class)
 public void testLanguagesByCountry_NullCountryCode() {
     LocaleUtils.languagesByCountry(null);
 }

 @Test
 public void testLanguagesByCountry_EmptyCountryCode() {
     List<Locale> actual = LocaleUtils.languagesByCountry("");
     List<Locale> expected = Collections.emptyList();
     assertEquals("Expected empty list for empty country code", expected, actual);
 }

 @Test
 public void testLanguagesByCountry_ValidCountryCode() {
     List<Locale> actual = LocaleUtils.languagesByCountry("US");
     List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.US);
     assertEquals("Expected list to match expected locales for US", expected, actual);
 }

 @Test
 public void testCountriesByLanguage_NullLanguageCode() {
     List<Locale> actual = LocaleUtils.countriesByLanguage(null);
     List<Locale> expected = Collections.emptyList();
     assertEquals("Expected empty list for null language code", expected, actual);
 }

 @Test
 public void testCountriesByLanguage_EmptyLanguageCode() {
     List<Locale> actual = LocaleUtils.countriesByLanguage("");
     List<Locale> expected = Collections.emptyList();
     assertEquals("Expected empty list for empty language code", expected, actual);
 }

 @Test
 public void testCountriesByLanguage_ValidLanguageCode() {
     List<Locale> actual = LocaleUtils.countriesByLanguage("en");
     List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.US);
     assertEquals("Expected list to match expected locales for English", expected, actual);
 }
}