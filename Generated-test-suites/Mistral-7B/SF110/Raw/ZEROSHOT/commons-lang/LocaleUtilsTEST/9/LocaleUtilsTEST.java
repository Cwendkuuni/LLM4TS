import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

 private static final String VALID_LOCALE_SHORT = "en";
 private static final String VALID_LOCALE_LANG_COUNTRY = "en_US";
 private static final String VALID_LOCALE_LANG_COUNTRY_VARIANT = "en_US_POSIX";
 private static final String INVALID_LOCALE_FORMAT_NO_LANG = "XX";
 private static final String INVALID_LOCALE_FORMAT_NO_COUNTRY = "_XX";
 private static final String INVALID_Locale_FORMAT_NO_LANG_NO_COUNTRY = "_XX_YY";
 private static final String INVALID_LOCALE_FORMAT_INVALID_CHAR = "en#US";
 private static final String INVALID_LOCALE_FORMAT_TOO_SHORT = "en_";

 @Test
 public void testToLocaleValidFormat() {
 Locale locale = LocaleUtils.toLocale(VALID_LOCALE_SHORT);
 assertEquals("en", locale.getLanguage());
 assertNull(locale.getCountry());

 locale = LocaleUtils.toLocale(VALID_LOCALE_LANG_COUNTRY);
 assertEquals("en", locale.getLanguage());
 assertEquals("US", locale.getCountry());
 assertNull(locale.getVariant());
 }

 @Test
 public void testToLocaleInvalidFormat() {
 assertNull(LocaleUtils.toLocale(INVALID_LOCALE_FORMAT_NO_LANG));
 assertNull(LocaleUtils.toLocale(INVALID_LOCALE_FORMAT_NO_COUNTRY));
 assertNull(LocaleUtils.toLocale(INVALID_LOCALE_FORMAT_NO_LANG_NO_COUNTRY));
 assertNull(LocaleUtils.toLocale(INVALID_LOCALE_FORMAT_INVALID_CHAR));
 assertNull(LocaleUtils.toLocale(INVALID_LOCALE_FORMAT_TOO_SHORT));
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("en", "US");
 List<Locale> localeList = LocaleUtils.localeLookupList(locale);
 assertEquals(3, localeList.size());
 assertTrue(localeList.contains(locale));
 assertEquals("en", localeList.get(0).getLanguage());
 assertNull(localeList.get(0).getCountry());
 assertEquals("en", localeList.get(1).getLanguage());
 assertEquals("US", localeList.get(1).getCountry());
 assertEquals("en", localeList.get(2).getLanguage());
 assertNull(localeList.get(2).getCountry());
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale locale = new Locale("en", "US");
 Locale defaultLocale = new Locale("fr", "FR");
 List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(4, localeList.size());
 assertTrue(localeList.contains(locale));
 assertTrue(localeList.contains(new Locale("en", "US")));
 assertTrue(localeList.contains(new Locale("en", "")));
 assertTrue(localeList.contains(defaultLocale));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> localeList = LocaleUtils.availableLocaleList();
 assertEquals(262, localeList.size());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
 assertEquals(262, localeSet.size());
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> localeList = LocaleUtils.languagesByCountry("US");
 assertEquals(2, localeList.size());
 assertSame(localeList.get(0), new Locale("en", "US"));
 assertSame(localeList.get(1), new Locale("es", "US"));
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> localeList = LocaleUtils.countriesByLanguage("en");
 assertEquals(25, localeList.size());
 assertTrue(localeList.contains(new Locale("en", "AG")));
 assertTrue(localeList.contains(new Locale("en", "US")));
 }

 @Test
 public void testInitStaticFields() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
 assertNotNull(LocaleUtils.cLanguagesByCountry);
 assertNotNull(LocaleUtils.cCountriesByLanguage);
 List<Locale> usLocales = LocaleUtils.languagesByCountry("US");
 assertEquals(2, usLocales.size());
 assertTrue(usLocales.contains(new Locale("en", "US")));
 assertTrue(usLocales.contains(new Locale("es", "US")));
 List<Locale> enLocales = LocaleUtils.countriesByLanguage("en");
 assertEquals(25, enLocales.size());
 }
}