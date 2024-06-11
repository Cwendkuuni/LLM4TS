import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test case for toLocale method
 @Test
 public void testToLocale() {
 String input = "en_GB";
 Locale expected = new Locale("en", "GB");
 Locale actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);

 input = "fr_CA_xxx";
 expected = new Locale("fr", "CA", "xxx");
 actual = LocaleUtils.toLocale(input);
 assertEquals(expected, actual);

 input = null;
 actual = LocaleUtils.toLocale(input);
 assertNull(actual);

 input = "InvalidFormat";
 try {
 LocaleUtils.toLocale(input);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test case for localeLookupList method
 @Test
 public void testLocaleLookupList() {
 String inputLocale = "fr_CA_xxx";
 Locale defaultLocale = new Locale("en");
 List<Locale> expected = Arrays.asList(
 new Locale("fr", "CA", "xxx"),
 new Locale("fr", "CA"),
 new Locale("fr"),
 new Locale("en")
 );
 List<Locale> actual = LocaleUtils.localeLookupList(Locale.parse(inputLocale), defaultLocale);
 assertEquals(expected, actual);

 inputLocale = "en";
 actual = LocaleUtils.localeLookupList(Locale.parse(inputLocale), defaultLocale);
 expected = Arrays.asList(
 new Locale("en")
 );
 assertEquals(expected, actual);

 inputLocale = null;
 actual = LocaleUtils.localeLookupList(null, defaultLocale);
 assertEquals(Collections.singletonList(defaultLocale), actual);
 }

 // Test case for availableLocaleList method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(expected, actual);
 }

 // Test case for isAvailableLocale method
 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("fr_CA");
 assertTrue(LocaleUtils.isAvailableLocale(locale));

 locale = new Locale("es_MX_InvalidVariant");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 // Test case for languagesByCountry method
 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expected = Arrays.asList(
 new Locale("en", "US"),
 new Locale("es", "US"),
 new Locale("fr", "CA")
 );
 List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expected, actual);

 countryCode = null;
 actual = LocaleUtils.languagesByCountry(null);
 assertEquals(Collections.emptyList(), actual);
 }

 // Test case for countriesByLanguage method
 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expected = Arrays.asList(
 new Locale("en", "US"),
 new Locale("en", "GB"),
 new Locale("en", "CA")
 );
 List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expected, actual);

 languageCode = null;
 actual = LocaleUtils.countriesByLanguage(null);
 assertEquals(Collections.emptyList(), actual);
 }
}