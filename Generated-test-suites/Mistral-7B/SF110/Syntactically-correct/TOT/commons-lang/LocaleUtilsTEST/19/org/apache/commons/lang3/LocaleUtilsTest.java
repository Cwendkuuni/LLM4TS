import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_NullInput() {
 Assert.assertNull(LocaleUtils.toLocale(null));
 }

 @Test
 public void testToLocale_EmptyInput() {
 Assert.assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
 }

 @Test
 public void testToLocale_InvalidFormat_NoLanguageOrCountry() {
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("aa"));
 }

 @Test
 public void testToLocale_InvalidFormat_MissingUpperCaseCountryCode() {
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_fr"));
 }

 @Test
 public void testToLocale_ValidFormat_TwoCharacterCountryCode() {
 Assert.assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
 }

 @Test
 public void testToLocale_ValidFormat_ThreeCharacterCountryCode() {
 Assert.assertEquals(new Locale("es", "ES"), LocaleUtils.toLocale("es_ES"));
 }

 @Test
 public void testLocaleLookupList_NullInput() {
 List<Locale> result = LocaleUtils.localeLookupList(null);
 Assert.assertEquals(0, result.size());
 }

 @Test
 public void testLocaleLookupList_ValidLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> result = LocaleUtils.localeLookupList(locale);
 Assert.assertEquals(3, result.size());
 Assert.assertEquals(locale, result.get(0));
 }

 @Test
 public void testLocaleLookupList_ValidLocaleWithVariant() {
 Locale locale = new Locale("en", "US", "en_US");
 List<Locale> result = LocaleUtils.localeLookupList(locale);
 Assert.assertEquals(4, result.size());
 Assert.assertEquals(locale, result.get(0));
 Assert.assertEquals(new Locale("en", "US"), result.get(1));
 }

 @Test
 public void testLocaleLookupList_NullDefaultLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> result = LocaleUtils.localeLookupList(locale, null);
 Assert.assertEquals(4, result.size());
 }

 @Test
 public void testLocaleLookupList_ValidDefaultLocale() {
 Locale defaultLocale = new Locale("fr", "FR");
 Locale locale = new Locale("en", "US");
 List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
 Assert.assertEquals(5, result.size());
 Assert.assertEquals(locale, result.get(0));
 Assert.assertEquals(new Locale("en", "US"), result.get(1));
 Assert.assertEquals(new Locale("", "US"), result.get(2));
 Assert.assertEquals(new Locale("fr", "FR"), result.get(3));
 }

 @Test
 public void testLocaleLookupList_EmptyAvailableLocaleList() {
 List<Locale> result = LocaleUtils.localeLookupList(Locale.ROOT, null);
 Assert.assertEquals(0, result.size());
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> result = LocaleUtils.availableLocaleList();
 Assert.assertNotEquals(0, result.size());
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
 public void testIsAvailableLocale_ValidLocale() {
 Locale locale = new Locale("en", "US");
 Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry_NullCountryCode() {
 List<Locale> result = LocaleUtils.languagesByCountry(null);
 Assert.assertEquals(0, result.size());
 }

 @Test
 public void testLanguagesByCountry_EmptyAvailableLocaleList() {
 List<Locale> result = LocaleUtils.languagesByCountry("XX");
 Assert.assertEquals(0, result.size());
 }

 @Test
 public void testLanguagesByCountry_ValidCountryCode() {
 List<Locale> result = LocaleUtils.languagesByCountry("US");
 Assert.assertNotEquals(0, result.size());
 }

 @Test
 public void testCountriesByLanguage_NullLanguageCode() {
 List<Locale> result = LocaleUtils.countriesByLanguage(null);
 Assert.assertEquals(0, result.size());
 }

 @Test
 public void testCountriesByLanguage_EmptyAvailableLocaleList() {
 List<Locale> result = LocaleUtils.countriesByLanguage("aa");
 Assert.assertEquals(0, result.size());
 }

 @Test
 public void testCountriesByLanguage_ValidLanguageCode() {
 List<Locale> result = LocaleUtils.countriesByLanguage("en");
 Assert.assertNotEquals(0, result.size());
 }
}