package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

 private static final String VALID_Locale_1 = "en_US";
 private static final String VALID_Locale_2 = "fr_FR";
 private static final String VALID_Locale_3 = "es_ES";
 private static final String VALID_Locale_4 = "de_DE";
 private static final String VALID_Locale_5 = "it_IT";
 private static final String VALID_Locale_6 = "ja_JP";
 private static final String VALID_Locale_7 = "ko_KR";
 private static final String VALID_Locale_8 = "zh_CN";
 private static final String VALID_Locale_9 = "pt_BR";
 private static final String VALID_Locale_10 = "ru_RU";

 private static final String INVALID_Locale_1 = "en_US#";
 private static final String INVALID_Locale_2 = "";
 private static final String INVALID_Locale_3 = "en_US_";
 private static final String INVALID_Locale_4 = "en_US_US";
 private static final String INVALID_Locale_5 = "_US";
 private static final String INVALID_Locale_6 = "_US_";
 private static final String INVALID_Locale_7 = "en_US_US_";
 private static final String INVALID_Locale_8 = "en_US_US#";
 private static final String INVALID_Locale_9 = "en_US_US_US";

 @Test
 public void testToLocale_ValidInput() {
 for (String validLocale : new String[] { VALID_Locale_1, VALID_Locale_2, VALID_Locale_3, VALID_Locale_4, VALID_Locale_5, VALID_Locale_6, VALID_Locale_7, VALID_Locale_8, VALID_Locale_9, VALID_Locale_10 }) {
 Locale locale = LocaleUtils.toLocale(validLocale);
 assertNotNull(locale);
 assertEquals(validLocale, locale.toString());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidInput() {
 for (String invalidLocale : new String[] { INVALID_Locale_1, INVALID_Locale_2, INVALID_Locale_3, INVALID_Locale_4, INVALID_Locale_5, INVALID_Locale_6, INVALID_Locale_7, INVALID_Locale_8, INVALID_Locale_9 }) {
 try {
 LocaleUtils.toLocale(invalidLocale);
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("en", "US");
 List<Locale> localeList = LocaleUtils.localeLookupList(locale);
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"));
 assertEquals(expectedLocaleList, localeList);

 locale = null;
 localeList = LocaleUtils.localeLookupList(locale, Locale.getDefault());
 expectedLocaleList = Collections.singletonList(Locale.getDefault());
 assertEquals(expectedLocaleList, localeList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> expectedAvailableLocaleList = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
 assertEquals(expectedAvailableLocaleList, availableLocaleList);
 }

 @Test
 public void testIsAvailableLocale() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 for (Locale locale : new Locale[] { new Locale("en", "US"), new Locale("fr", "FR"), new Locale("es", "ES"), new Locale("de", "DE"), new Locale("it", "IT"), new Locale("ja", "JP"), new Locale("ko", "KR"), new Locale("zh", "CN"), new Locale("pt", "BR"), new Locale("ru", "RU") }) {
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 List<Locale> invalidLocaleList = Arrays.asList(new Locale("", ""), new Locale("", "US"), new Locale("en", ""), new Locale("en", "US_"), new Locale("en_US", ""), new Locale("en_US", "_"), new Locale("en_US", "US_"), new Locale("en_US", "US_US"), new Locale("en_US", "US_US_"), new Locale("en_US", "US_US_US"));
 for (Locale locale : invalidLocaleList) {
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> usLocales = LocaleUtils.languagesByCountry("US");
 List<Locale> expectedUsLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "US-POSIX"));
 assertEquals(expectedUsLocales, usLocales);

 List<Locale> frLocales = LocaleUtils.languagesByCountry("FR");
 List<Locale> expectedFrLocales = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "FR-POSIX"));
 assertEquals(expectedFrLocales, frLocales);
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> enLocales = LocaleUtils.countriesByLanguage("en");
 List<Locale> expectedEnLocales = Arrays.asList(new Locale("US"), new Locale("GB"), new Locale("IE"), new Locale("CA"), new Locale("NZ"), new Locale("ZA"), new Locale("AU"), new Locale("IN"), new Locale("IE-GB"), new Locale("JE"), new Locale("SG"), new Locale("TT"), new Locale("GB-NI"), new Locale("GS"));
 assertEquals(expectedEnLocales, enLocales);
 }
}