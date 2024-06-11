import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expected = new Locale("en", "GB");
 Locale actual = LocaleUtils.toLocale("en_GB");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 LocaleUtils.toLocale("enGB");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleEmptyString() {
 LocaleUtils.toLocale("");
 }

 @Test
 public void testToLocaleLanguageCode() {
 Locale expected = new Locale("en", "");
 Locale actual = LocaleUtils.toLocale("en");
 assertEquals(expected, actual);
 }

 @Test
 public void testToLocaleCountryCode() {
 Locale expected = new Locale("en", "GB");
 Locale actual = LocaleUtils.toLocale("en_GB");
 assertEquals(expected, actual);
 }

 @Test
 public void testToLocaleLanguageAndCountryCode() {
 Locale expected = new Locale("en", "GB", "XXX");
 Locale actual = LocaleUtils.toLocale("en_GB_XXX");
 assertEquals(expected, actual);
 }

 @Test
 public void testToLocaleNullInput() {
 assertNull(LocaleUtils.toLocale(null));
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "YYY");
 List expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
 List actual = LocaleUtils.localeLookupList(locale);
 assertTrue(expected.equals(actual));
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale locale = new Locale("fr", "CA", "YYY");
 Locale defaultLocale = new Locale("en");
 List expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
 List actual = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertTrue(expected.equals(actual));
 }

 @Test
 public void testLocaleLookupListWithEmptyList() {
 Locale locale = null;
 List actual = LocaleUtils.localeLookupList(locale);
 assertTrue(Collections.isEmpty(actual));
 }

 @Test
 public void testLocaleLookupListWithDefaultLocaleEmpty() {
 Locale locale = null;
 Locale defaultLocale = null;
 List actual = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertTrue(Collections.isEmpty(actual));
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale1 = new Locale("en", "GB");
 Locale locale2 = new Locale("fr", "FR");
 Locale locale3 = new Locale("en_US");

 assertTrue(LocaleUtils.isAvailableLocale(locale1));
 assertTrue(LocaleUtils.isAvailableLocale(locale2));
 assertTrue(LocaleUtils.isAvailableLocale(locale3));

 assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> expected = Arrays.asList(
 new Locale("en", "GB"),
 new Locale("en", "US"),
 new Locale("en", "CA"),
 new Locale("en", "IE"),
 new Locale("en", "AU"),
 new Locale("en", "NZ"),
 new Locale("en", "ZA")
 );
 List<Locale> actual = LocaleUtils.languagesByCountry("GB");
 assertTrue(expected.equals(actual));
 }

 @Test
 public void testLanguagesByCountryNullInput() {
 List<Locale> actual = LocaleUtils.languagesByCountry(null);
 assertTrue(Collections.isEmpty(actual));
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> expected = Arrays.asList(
 new Locale("en", "GB"),
 new Locale("en", "US"),
 new Locale("en", "CA"),
 new Locale("en", "IE"),
 new Locale("en", "AU"),
 new Locale("en", "NZ"),
 new Locale("en", "ZA")
 );
 List<Locale> actual = LocaleUtils.countriesByLanguage("en");
 assertTrue(expected.equals(actual));
 }

 @Test
 public void testCountriesByLanguageNullInput() {
 List<Locale> actual = LocaleUtils.countriesByLanguage(null);
 assertTrue(Collections.isEmpty(actual));
 }
}