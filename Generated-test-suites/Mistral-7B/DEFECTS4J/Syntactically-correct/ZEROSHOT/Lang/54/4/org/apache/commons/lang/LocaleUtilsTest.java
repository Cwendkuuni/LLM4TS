import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expected = new Locale("en", "GB");
 Locale actual = LocaleUtils.toLocale("en_GB");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 LocaleUtils.toLocale("en_gb"); // lowercase 'g'
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidLanguage() {
 LocaleUtils.toLocale("XX_YY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidCountry() {
 LocaleUtils.toLocale("en_xy");
 }

 @Test
 public void testToLocaleEmpty() {
 Locale actual = LocaleUtils.toLocale("");
 assertNull(actual);
 }

 @Test
 public void testToLocaleNull() {
 Locale actual = LocaleUtils.toLocale(null);
 assertNull(actual);
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListEmpty() {
 Locale locale = null;
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 assertEquals(Collections.emptyList(), actual);
 }

 @Test
 public void testLocaleLookupListDefault() {
 Locale locale = new Locale("fr", "CA", "xxx");
 Locale defaultLocale = new Locale("en");
 List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
 List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 // check size and first few elements
 assertTrue(actual.size() > 0);
 assertTrue(actual.get(0) instanceof Locale);
 assertEquals("en_US", actual.get(0).toString());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> actual = LocaleUtils.availableLocaleSet();
 // check size and first few elements
 assertTrue(actual.size() > 0);
 assertTrue(actual.contains(new Locale("en", "US")));
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("fr", "CH");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocaleFalse() {
 Locale locale = new Locale("fr", "XX");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> actual = LocaleUtils.languagesByCountry("CA");
 List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
 assertEquals(expected, actual);
 }

 @Test
 public void testLanguagesByCountryEmpty() {
 List<Locale> actual = LocaleUtils.languagesByCountry(null);
 assertTrue(Collections.isEmpty(actual));
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
 List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguageEmpty() {
 List<Locale> actual = LocaleUtils.countriesByLanguage(null);
 assertTrue(Collections.isEmpty(actual));
 }
}