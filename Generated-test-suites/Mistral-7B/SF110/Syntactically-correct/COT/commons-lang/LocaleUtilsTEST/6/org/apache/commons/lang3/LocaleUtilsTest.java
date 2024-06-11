import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LocaleUtilsTest {

 private static final ConcurrentHashMap<String, List<Locale>> mockCCountriesByLanguage = new ConcurrentHashMap<>();
 private static final ConcurrentHashMap<String, List<Locale>> mockCLanguagesByCountry = new ConcurrentHashMap<>();

 @Test
 public void testToLocale_EmptyString() {
     Assert.assertNull(LocaleUtils.toLocale(""));
 }

 @Test
 public void testToLocale_Null() {
     Assert.assertNull(LocaleUtils.toLocale(null));
 }

 @Test
 public void testToLocale_InvalidFormat_NoLanguage() {
     Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("US"));
 }

 @Test
 public void testToLocale_InvalidFormat_MissingCountry() {
     Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en"));
 }

 @Test
 public void testToLocale_InvalidFormat_MissingLanguage() {
     Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_ES"));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidCountry() {
     Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XX"));
 }

 @Test
 public void testToLocale_ValidFormat_TwoParts() {
     Assert.assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en-US"));
 }

 @Test
 public void testToLocale_ValidFormat_ThreeParts() {
     Assert.assertEquals(new Locale("en", "US", "en"), LocaleUtils.toLocale("en-US-en"));
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
     List<Locale> result = LocaleUtils.localeLookupList(null);
     Assert.assertEquals(0, result.size());
 }

 @Test
 public void testLocaleLookupList_EmptyLocale() {
     List<Locale> result = LocaleUtils.localeLookupList(new Locale("", ""));
     Assert.assertEquals(0, result.size());
 }

 @Test
 public void testLocaleLookupList() {
     List<Locale> result = LocaleUtils.localeLookupList(new Locale("en", "US"));
     List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
     Assert.assertEquals(expected, result);
 }

 @Test
 public void testAvailableLocaleList_Empty() {
     List<Locale> result = LocaleUtils.availableLocaleList();
     Assert.assertEquals(0, result.size());
 }

 @Test
 public void testAvailableLocaleList_NotEmpty() {
     List<Locale> result = LocaleUtils.availableLocaleList();
     List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
     Assert.assertNotEquals(0, result.size());
     Assert.assertEquals(expected, result);
 }

 @Test
 public void testIsAvailableLocale_NullLocale() {
     Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
     Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
 }

 @Test
 public void testIsAvailableLocale_AvailableLocale() {
     List<Locale> availableLocales = Arrays.asList(Locale.getAvailableLocales());
     Assert.assertTrue(LocaleUtils.isAvailableLocale(availableLocales.get(0)));
 }

 @Test
 public void testLanguagesByCountry() {
     String countryCode = "US";
     List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"), new Locale("fr", "US"));
     mockCCountriesByLanguage.put(countryCode, expected);
     List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
     Assert.assertEquals(expected, result);
 }

 @Test
 public void testCountriesByLanguage() {
     String languageCode = "en";
     List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"));
     mockCLanguagesByCountry.put(languageCode, expected);
     List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
     Assert.assertEquals(expected, result);
 }
}