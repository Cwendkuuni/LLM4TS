import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Locale;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEmpty;
import static org.junit.Assert.assertNotSame;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_NullInput() {
 assertNull(LocaleUtils.toLocale(null));
 }

 @Test
 public void testToLocale_EmptyInput() {
 assertNotNull(LocaleUtils.toLocale(""));
 assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoLanguageCode() {
 assertNull(LocaleUtils.toLocale("#"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_MissingLanguageCode() {
 assertNull(LocaleUtils.toLocale("_FR"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_MissingCountryCode() {
 assertNull(LocaleUtils.toLocale("en"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidLanguageCodeLength() {
 assertNull(LocaleUtils.toLocale("en_XXXXX"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidCountryCodeLength() {
 assertNull(LocaleUtils.toLocale("en_XX"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidLanguageCodeCase() {
 assertNull(LocaleUtils.toLocale("en_France"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidCountryCodeCase() {
 assertNull(LocaleUtils.toLocale("en_france"));
 }

 @Test
 public void testToLocale_ValidFormat() {
 assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
 }

 @Test
 public void testToLocale_ValidFormat_EmptyCountryCode() {
 assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
 }

 @Test
 public void testToLocale_ValidFormat_EmptyLanguageCode() {
 assertEquals(new Locale("", "CA"), LocaleUtils.toLocale("", "CA"));
 }

 @Test
 public void testToLocale_ValidFormat_LanguageCodeLengthGreaterThanThree() {
 assertEquals(new Locale("fr_CA", "qc"), LocaleUtils.toLocale("fr_CA_qc"));
 }

 @Test
 public void testToLocale_ValidFormat_CountryCodeLengthGreaterThanTwo() {
 assertEquals(new Locale("fr", "qc_CA"), LocaleUtils.toLocale("fr_qc_CA"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_MultipleUnderscores() {
 assertNull(LocaleUtils.toLocale("en_US_FR"));
 }

 @Test
 public void testLocaleLookupList() {
 Locale localeFrCA = new Locale("fr", "CA");
 Locale localeFr = new Locale("fr");
 Locale localeUs = new Locale("en", "US");
 List<Locale> result = LocaleUtils.localeLookupList(localeFrCA);
 assertEquals(4, result.size());
 assertEquals(localeFrCA, result.get(0));
 assertEquals(localeFr, result.get(1));
 assertEquals(localeUs, result.get(2));
 assertEquals(localeUs, result.get(3));
 }

 @Test
 public void testLocaleLookupList_NullInput() {
 List<Locale> result = LocaleUtils.localeLookupList(null);
 assertEmpty(result);
 }

 @Test
 public void testLocaleLookupList_EmptyInput() {
 Locale localeEmpty = new Locale("");
 List<Locale> result = LocaleUtils.localeLookupList(localeEmpty);
 assertEquals(1, result.size());
 assertEquals(localeEmpty, result.get(0));
 }

 @Test
 public void testLocaleLookupList_DefaultInput() {
 Locale localeDefault = Locale.getDefault();
 List<Locale> result = LocaleUtils.localeLookupList(localeDefault);
 assertEquals(2, result.size());
 assertEquals(localeDefault, result.get(0));
 assertNotSame(localeDefault, result.get(1));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> result = LocaleUtils.availableLocaleList();
 assertFalse(result.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> result = LocaleUtils.availableLocaleSet();
 assertFalse(result.isEmpty());
 }

 @Test
 public void testIsAvailableLocale() {
 Locale localeFrCA = new Locale("fr", "CA");
 assertTrue(LocaleUtils.isAvailableLocale(localeFrCA));
 Locale localeInvalid = new Locale("invalid", "invalid");
 assertFalse(LocaleUtils.isAvailableLocale(localeInvalid));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
 assertFalse(result.isEmpty());
 String[] expected = {"fr", "en"};
 Arrays.sort(expected);
 for (Locale locale : result) {
 String languageCode = locale.getLanguage();
 Arrays.sort(new String[]{languageCode});
 assertEquals(expected[0], languageCode);
 }
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "fr";
 List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
 assertFalse(result.isEmpty());
 String[] expected = {"CA"};
 Arrays.sort(expected);
 for (Locale locale : result) {
 String countryCode = locale.getCountry();
 Arrays.sort(new String[]{countryCode});
 assertEquals(expected[0], countryCode);
 }
 }

 @Test
 public void testLanguagesByCountry_NullInput() {
 List<Locale> result = LocaleUtils.languagesByCountry(null);
 assertEmpty(result);
 }

 @Test
 public void testCountriesByLanguage_EmptyInput() {
 List<Locale> result = LocaleUtils.countriesByLanguage("");
 assertEmpty(result);
 }
}