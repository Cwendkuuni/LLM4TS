import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_ValidInput() {
 String input = "en_US";
 Locale expected = new Locale("en", "US");
 Locale actual = LocaleUtils.toLocale(input);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testToLocale_EmptyInput() {
 String input = "";
 Locale expected = null;
 Locale actual = LocaleUtils.toLocale(input);
 Assert.assertNull(actual);
 }

 @Test
 public void testToLocale_InvalidFormat_MissingLanguage() {
 String input = "_US";
 Assert.throwException(IllegalArgumentException.class, () -> LocaleUtils.toLocale(input));
 }

 @Test
 public void testToLocale_InvalidFormat_MissingCountry() {
 String input = "en_";
 Assert.throwException(IllegalArgumentException.class, () -> LocaleUtils.toLocale(input));
 }

 @Test
 public void testToLocale_InvalidFormat_MissingLanguageAndCountry() {
 String input = "_";
 Assert.throwException(IllegalArgumentException.class, () -> LocaleUtils.toLocale(input));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidLanguageLength() {
 String input = "en_XX";
 Assert.throwException(IllegalArgumentException.class, () -> LocaleUtils.toLocale(input));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidCountryLength() {
 String input = "en_CCC";
 Assert.throwException(IllegalArgumentException.class, () -> LocaleUtils.toLocale(input));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidLanguageAndCountry() {
 String input = "en_CCC_XXX";
 Assert.throwException(IllegalArgumentException.class, () -> LocaleUtils.toLocale(input));
 }

 @Test
 public void testLocaleLookupList_ValidInput() {
 Locale locale = new Locale("en", "US");
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 Assert.assertArrayEquals(expected.toArray(), actual.toArray());
 }

 @Test
 public void testLocaleLookupList_NullInput() {
 List<Locale> actual = LocaleUtils.localeLookupList(null);
 Assert.assertTrue(actual.isEmpty());
 }

 @Test
 public void testLocaleLookupList_EmptyInput() {
 Locale locale = new Locale("");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 Assert.assertTrue(actual.isEmpty());
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 List<Locale> actual = LocaleUtils.localeLookupList(Locale.getDefault());
 Assert.assertFalse(actual.isEmpty());
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expected = LocaleUtils.availableLocaleList();
 List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
 Assert.assertArrayEquals(expected.toArray(), actual.toArray());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expected = LocaleUtils.availableLocaleSet();
 Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocale_ValidInput() {
 Locale locale = new Locale("en", "US");
 boolean actual = LocaleUtils.isAvailableLocale(locale);
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocale_InvalidInput() {
 Locale locale = new Locale("en", "XX");
 boolean actual = LocaleUtils.isAvailableLocale(locale);
 boolean expected = false;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", "IE"), new Locale("en", "AU"));
 List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
 Assert.assertArrayEquals(expected.toArray(), actual.toArray());
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", "IE"), new Locale("en", "AU"), new Locale("en", "ZA"), new Locale("en", "JM"), new Locale("en", "TT"), new Locale("en", "BM"), new Locale("en", "IN"), new Locale("en", "ID"), new Locale("en", "MT"), new Locale("en", "LR"), new Locale("en", "SL"), new Locale("en", "SG"), new Locale("en", "TO"), new Locale("en", "GG"), new Locale("en", "GU"), new Locale("en", "KY"), new Locale("en", "MP"), new Locale("en", "PN"), new Locale("en", "VC"), new Locale("en", "VG"));
 List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
 Assert.assertArrayEquals(expected.toArray(), actual.toArray());
 }

}