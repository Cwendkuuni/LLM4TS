import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
 public void testToLocaleMissingLanguage() {
 LocaleUtils.toLocale("GB");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleMissingCountry() {
 LocaleUtils.toLocale("en");
 }

 @Test
 public void testToLocaleWithVariants() {
 Locale expected = new Locale("en", "GB", "en_GB");
 Locale actual = LocaleUtils.toLocale("en_GB_en_GB");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleMultipleVariants() {
 LocaleUtils.toLocale("en_GB_en_GB_en_GB");
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List list = LocaleUtils.localeLookupList(locale);
 List expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 assertArrayEquals(expected.toArray(), list.toArray());
 }

 @Test
 public void testLocaleLookupListEmpty() {
 Locale locale = null;
 List list = LocaleUtils.localeLookupList(locale);
 List expected = Collections.emptyList();
 assertArrayEquals(expected.toArray(), list.toArray());
 }

 @Test
 public void testLocaleLookupListDefaultLocale() {
 Locale locale = new Locale("fr", "CA", "xxx");
 Locale defaultLocale = new Locale("en");
 List list = LocaleUtils.localeLookupList(locale, defaultLocale);
 List expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 assertArrayEquals(expected.toArray(), list.toArray());
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("fr", "CA");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocaleInvalidLocale() {
 Locale locale = new Locale("fr", "CA", "xxx");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List expected = Arrays.asList(new Locale("en", countryCode), new Locale("fr", countryCode), new Locale("et", countryCode));
 List actual = LocaleUtils.languagesByCountry(countryCode);
 assertArrayEquals(expected.toArray(), actual.toArray());
 }

 @Test
 public void testLanguagesByCountryEmpty() {
 String countryCode = null;
 List actual = LocaleUtils.languagesByCountry(countryCode);
 List expected = Collections.emptyList();
 assertArrayEquals(expected.toArray(), actual.toArray());
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List expected = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "US"), new Locale(languageCode, "GB"));
 List actual = LocaleUtils.countriesByLanguage(languageCode);
 assertArrayEquals(expected.toArray(), actual.toArray());
 }

 @Test
 public void testCountriesByLanguageEmpty() {
 String languageCode = null;
 List actual = LocaleUtils.countriesByLanguage(languageCode);
 List expected = Collections.emptyList();
 assertArrayEquals(expected.toArray(), actual.toArray());
 }
}