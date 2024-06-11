import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 // Test valid inputs
 Locale expected = new Locale("en", "GB");
 Locale actual = LocaleUtils.toLocale("en_GB");
 assertEquals(expected, actual);

 expected = new Locale("fr", "");
 actual = LocaleUtils.toLocale("fr");
 assertEquals(expected, actual);

 expected = new Locale("fr", "CA", "xxx");
 actual = LocaleUtils.toLocale("fr_CA_xxx");
 assertEquals(expected, actual);

 // Test invalid inputs
 try {
 LocaleUtils.toLocale(null);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 LocaleUtils.toLocale("fr_CA_xxx_yyy");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 LocaleUtils.toLocale("en_GB_xx");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA", "xxx");
 List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
 List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
 assertEquals(expected, actual);

 locale = new Locale("fr", "CA");
 expected = Arrays.asList(locale, new Locale("fr"), new Locale("en"));
 actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
 assertEquals(expected, actual);

 locale = new Locale("fr");
 expected = Arrays.asList(locale, new Locale("en"));
 actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
 assertEquals(expected, actual);

 locale = null;
 expected = Collections.emptyList();
 actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> actual = LocaleUtils.availableLocaleSet();
 Set<Locale> expected = new HashSet<>(Locale.getAvailableLocales());
 assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("fr", "CA");
 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("fr", "CA", "xxx");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List<Locale> expected = Collections.singletonList(new Locale("fr", countryCode));
 List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expected, actual);

 countryCode = "US";
 expected = Arrays.asList(new Locale("en", countryCode), new Locale("es", countryCode));
 actual = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expected, actual);

 countryCode = null;
 expected = Collections.emptyList();
 actual = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "fr";
 List<Locale> expected = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "BE"));
 List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expected, actual);

 languageCode = "es";
 expected = Arrays.asList(new Locale("es", "US"));
 actual = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expected, actual);

 languageCode = null;
 expected = Collections.emptyList();
 actual = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expected, actual);
 }
}