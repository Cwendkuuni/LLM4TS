import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

 private List<Locale> getTestData() {
 return Arrays.asList(new Locale("en"), new Locale("es_ES"), new Locale("fr_FR"), new Locale("de_DE"), new Locale("it_IT"), new Locale("zh_CN"), new Locale("ja_JP"), new Locale("ko_KR"), new Locale("ru_RU"));
 }

 @Test
 public void testToLocale_ValidInput() {
 for (Locale locale : getTestData()) {
 Locale actual = LocaleUtils.toLocale(locale.toString());
 assertEquals("Expected the same Locale", locale, actual);
 }
 }

 @Test
 public void testToLocale_NullInput() {
 assertNull("Expected null when input is null", LocaleUtils.toLocale(null));
 }

 @Test
 public void testToLocale_EmptyStringInput() {
 assertSame("Expected an empty Locale when input is an empty string", new Locale("", ""), LocaleUtils.toLocale(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoLanguageCode() {
 LocaleUtils.toLocale("US");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoCountryCode() {
 LocaleUtils.toLocale("en");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidLength() {
 LocaleUtils.toLocale("en_US___");
 }

 @Test
 public void testToLocale_InvalidFormat_MissingLanguageCode() {
 LocaleUtils toLocale = new LocaleUtils();
 assertFalse("Expected false when missing language code", toLocale.isValidLocale("US"));
 }

 @Test
 public void testToLocale_InvalidFormat_MissingCountryCode() {
 LocaleUtils toLocale = new LocaleUtils();
 assertFalse("Expected false when missing country code", toLocale.isValidLocale("en"));
 }

 @Test
 public void testToLocale_InvalidFormat_MissingLanguageCodeAndCountryCode() {
 LocaleUtils toLocale = new LocaleUtils();
 assertFalse("Expected false when missing both language and country code", toLocale.isValidLocale(""));
 }

 @Test
 public void testLocaleLookupList_ValidInput() {
 Locale locale = new Locale("es", "ES");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = Arrays.asList(locale, new Locale("es", ""), new Locale("es", "ES"), new Locale("", "es"));
 assertEquals("Expected the same list", expected, actual);
 }

 @Test
 public void testLocaleLookupList_NullInput() {
 assertSame("Expected an empty list when input is null", Collections.emptyList(), LocaleUtils.localeLookupList(null));
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 Locale locale = new Locale("", "");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = Arrays.asList(locale, new Locale("", ""));
 assertEquals("Expected the same list", expected, actual);
 }

 @Test
 public void testLocaleLookupList_EmptyLocale() {
 Locale locale = new Locale("", "");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en", "US"));
 List<Locale> expected = Arrays.asList(locale, new Locale("es", "ES"), new Locale("es", ""), new Locale("", "es"), new Locale("en", "US"));
 assertEquals("Expected the same list", expected, actual);
 }

 @Test
 public void testLocaleLookupList_NullDefaultLocale() {
 Locale locale = new Locale("es", "ES");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, null);
 List<Locale> expected = Arrays.asList(locale, new Locale("es", ""), new Locale("es", "ES"), new Locale("", "es"));
 assertEquals("Expected the same list", expected, actual);
 }

 @Test
 public void testLocaleLookupList_EmptyDefaultLocale() {
 Locale locale = new Locale("es", "ES");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("", ""));
 List<Locale> expected = Arrays.asList(locale, new Locale("es", ""), new Locale("es", "ES"), new Locale("", "es"), new Locale("", ""));
 assertEquals("Expected the same list", expected, actual);
 }

 @Test
 public void testLocaleLookupList_SameLocale() {
 Locale locale = new Locale("es", "ES");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, locale);
 List<Locale> expected = Arrays.asList(locale, new Locale("es", ""), new Locale("es", "ES"), new Locale("", "es"), new Locale("", ""));
 assertEquals("Expected the same list", expected, actual);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 List<Locale> expected = LocaleUtils.getTestData();
 assertEquals("Expected the same list", expected, actual);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> actual = LocaleUtils.availableLocaleSet();
 Set<Locale> expected = new HashSet<Locale>(LocaleUtils.getTestData());
 assertTrue("Expected the same set", actual.containsAll(expected));
 }

 @Test
 public void testIsAvailableLocale_ValidInput() {
 List<Locale> testData = LocaleUtils.getTestData();
 for (Locale locale : testData) {
 assertTrue("Expected true when valid Locale", LocaleUtils.isAvailableLocale(locale));
 }
 }

 @Test
 public void testIsAvailableLocale_InvalidInput() {
 assertFalse("Expected false when invalid input", LocaleUtils.isAvailableLocale(null));
 assertFalse("Expected false when invalid input", LocaleUtils.isAvailableLocale(""));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "ES";
 List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> expected = Arrays.asList(new Locale("es", "ES"), new Locale("es", "ES"), new Locale("an", "ES"), new Locale("ast", "ES"), new Locale("ca", "ES"), new Locale("csb", "ES"), new Locale("eo", "ES"), new Locale("gag", "ES"), new Locale("gl", "ES"), new Locale("he", "IL"), new Locale("ka", "GE"), new Locale("la", ""), new Locale("lv", "LV"), new Locale("mt", "MT"), new Locale("oc", "FR"), new Locale("pms", "IT"), new Locale("pot", ""), new Locale("raet", "CH"), new Locale("sgs", "NO"), new Locale("ta", "LK"), new Locale("trv", "IN"), new Locale("udm", "RU"), new Locale("zu", "ZA"));
 assertEquals("Expected the same list", expected, actual);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "es";
 List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> expected = Arrays.asList(new Locale("ES", "ES"), new Locale("AD", "ES"), new Locale("AR", "DO"), new Locale("BO", "BO"), new Locale("CL", "CL"), new Locale("CO", "CO"), new Locale("CR", "CR"), new Locale("DO", "DO"), new Locale("EC", "EC"), new Locale("ES", "IC"), new Locale("ES", "UM"), new Locale("GT", "GT"), new Locale("HN", "HN"), new Locale("MX", "MX"), new Locale("NI", "NI"), new Locale("PA", "PA"), new Locale("PE", "PE"), new Locale("PR", "PR"), new Locale("PY", "PY"), new Locale("SV", "SV"), new Locale("US", "VI"), new Locale("UY", "UY"), new Locale("VE", "VE"));
 assertEquals("Expected the same list", expected, actual);
 }

}