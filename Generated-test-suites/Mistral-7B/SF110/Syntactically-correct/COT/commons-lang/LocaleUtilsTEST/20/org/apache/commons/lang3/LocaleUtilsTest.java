import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

 private List<Locale> getTestLocales() {
 return Arrays.asList(new Locale("en_US"), new Locale("fr_FR"), new Locale("es_ES"), new Locale("de_DE"), new Locale("zh_CN"));
 }

 @Test
 public void testToLocale_ValidLocale_ReturnsCorrectLocaleObject() {
 for (Locale locale : getTestLocales()) {
 Locale result = LocaleUtils.toLocale(locale.toString());
 Assert.assertEquals(locale, result);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_EmptyString_ThrowsIllegalArgumentException() {
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_NullString_ThrowsIllegalArgumentException() {
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_ThrowsIllegalArgumentException() {
 String invalidLocale = "en_US#";
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale));
 }

 @Test
 public void testLocaleLookupList_ValidLocale_ReturnsListWithLocaleAndVariants() {
 Locale locale = new Locale("fr", "FR");
 List<Locale> result = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = Arrays.asList(locale, new Locale("fr", ""), new Locale("fr"));
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
 List<Locale> result = LocaleUtils.localeLookupList(null);
 Assert.assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testLocaleLookupList_ValidLocaleWithVariant_ReturnsListWithLocaleAndBaseLocale() {
 Locale locale = new Locale("es", "ES", "Castilian");
 List<Locale> result = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = Arrays.asList(locale, new Locale("es", "ES"), new Locale("es"));
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testLocaleLookupList_ValidLocaleWithEmptyCountry_ReturnsListWithBaseLocale() {
 Locale locale = new Locale("zh", "");
 List<Locale> result = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = Arrays.asList(locale, new Locale("zh"));
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testLocaleLookupList_DefaultLocale_ReturnsListWithDefaultLocale() {
 Locale defaultLocale = Locale.getDefault();
 List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);
 List<Locale> expected = Collections.singletonList(defaultLocale);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testAvailableLocaleList_ReturnsAvailableLocalesAsUnmodifiableList() {
 List<Locale> result = LocaleUtils.availableLocaleList();
 List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testAvailableLocaleSet_ReturnsAvailableLocalesAsUnmodifiableSet() {
 Set<Locale> result = LocaleUtils.availableLocaleSet();
 Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIsAvailableLocale_ValidLocale_ReturnsTrue() {
 Locale locale = new Locale("en", "US");
 Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale_ReturnsFalse() {
 Locale invalidLocale = new Locale("es", "CO"); // Colombia is not available as a locale
 Assert.assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
 }

 @Test
 public void testLanguagesByCountry_ValidCountryCode_ReturnsCorrectListOfLocales() {
 String countryCode = "US";
 List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"));
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
 List<Locale> result = LocaleUtils.languagesByCountry(null);
 Assert.assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testCountriesByLanguage_ValidLanguageCode_ReturnsCorrectListOfLocales() {
 String languageCode = "en";
 List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> expected = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"), new Locale("IE"));
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
 List<Locale> result = LocaleUtils.countriesByLanguage(null);
 Assert.assertEquals(Collections.emptyList(), result);
 }
}