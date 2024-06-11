package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 String[] testCases = new String[]{"", "en_US", "_en_US", "en_US_FOO", "en_US_FOO_BAR", "es_ES", "_es_ES", "es_ES_FOO", "es_ES_FOO_BAR", "es_ES_FOO_BAR_BA", "fr_FR", "_fr_FR", "fr_FR_FOO", "fr_FR_FOO_BAR", "fr_FR_FOO_BAR_BA", "ru_RU", "_ru_RU", "ru_RU_FOO", "ru_RU_FOO_BAR", "ru_RU_FOO_BAR_BA"};
 Locale[] expectedLocales = new Locale[]{null, Locale.US, Locale.US, Locale.US, Locale.US, Locale.SPAIN, Locale.SPAIN, Locale.SPAIN, Locale.SPAIN, Locale.SPAIN, Locale.SPAIN, Locale.FRANCE, Locale.FRANCE, Locale.FRANCE, Locale.FRANCE, Locale.FRANCE, Locale.RUSSIA, Locale.RUSSIA, Locale.RUSSIA, Locale.RUSSIA, Locale.RUSSIA};
 for (int i = 0; i < testCases.length; i++) {
 Locale result = LocaleUtils.toLocale(testCases[i]);
 assertEquals(expectedLocales[i], result);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 LocaleUtils.toLocale("en_US#");
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale1 = new Locale("fr", "FR");
 Locale locale2 = new Locale("en", "US");
 Locale locale3 = Locale.US;
 Locale locale4 = null;
 List<Locale> testLocales = new ArrayList<Locale>(Arrays.asList(locale1, locale2, locale3, locale4));
 List<Locale> expectedList = new ArrayList<Locale>(Arrays.asList(locale1, locale2, locale3, locale4));
 List<Locale> resultList = LocaleUtils.localeLookupList(locale1);
 assertEquals(expectedList, resultList);
 resultList = LocaleUtils.localeLookupList(locale1, locale3);
 assertEquals(expectedList, resultList);
 resultList = LocaleUtils.localeLookupList(locale2);
 assertEquals(expectedList, resultList);
 resultList = LocaleUtils.localeLookupList(locale3);
 assertEquals(expectedList, resultList);
 resultList = LocaleUtils.localeLookupList(locale4);
 assertEquals(expectedList, resultList);
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale defaultLocale = Locale.US;
 List<Locale> testLocales = new ArrayList<Locale>(Arrays.asList(new Locale("fr", "FR"), new Locale("en", "US"), defaultLocale));
 List<Locale> expectedList = new ArrayList<Locale>(Arrays.asList(new Locale("fr", "FR"), new Locale("en", "US"), defaultLocale));
 List<Locale> resultList = LocaleUtils.localeLookupList(new Locale("fr", "FR"));
 assertEquals(expectedList, resultList);
 resultList = LocaleUtils.localeLookupList(new Locale("fr", "FOO"));
 assertEquals(expectedList, resultList);
 resultList = LocaleUtils.localeLookupList(new Locale("en", "US"));
 assertEquals(expectedList, resultList);
 resultList = LocaleUtils.localeLookupList(defaultLocale);
 assertEquals(expectedList, resultList);
 }

 @Test(expected = NullPointerException.class)
 public void testLocaleLookupListNullLocale() {
 LocaleUtils.localeLookupList(null);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 assertNotNull(availableLocaleList);
 assertEquals(LocaleUtils.AVAILABLE_LOCALE_LIST.size(), availableLocaleList.size());
 for (Locale locale : availableLocaleList) {
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
 assertNotNull(availableLocaleSet);
 assertEquals(LocaleUtils.AVAILABLE_LOCALE_SET.size(), availableLocaleSet.size());
 for (Locale locale : availableLocaleSet) {
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }
 }

 @Test
 public void testLanguagesByCountry() {
 String[] testCountryCodes = new String[]{"US", "ES", "FR", "RU"};
 List<Locale>[] expectedLocaleLists = new List[testCountryCodes.length][];
 for (int i = 0; i < testCountryCodes.length; i++) {
 expectedLocaleLists[i] = LocaleUtils.languagesByCountry(testCountryCodes[i]);
 }
 for (int i = 0; i < testCountryCodes.length; i++) {
 for (Locale locale : expectedLocaleLists[i]) {
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 assertTrue(locale.getCountry().equals(testCountryCodes[i]));
 }
 }
 }

 @Test
 public void testCountriesByLanguage() {
 String[] testLanguageCodes = new String[]{"en", "es", "fr", "ru"};
 List<Locale>[] expectedLocaleLists = new List[testLanguageCodes.length][];
 for (int i = 0; i < testLanguageCodes.length; i++) {
 expectedLocaleLists[i] = LocaleUtils.countriesByLanguage(testLanguageCodes[i]);
 }
 for (int i = 0; i < testLanguageCodes.length; i++) {
 for (Locale locale : expectedLocaleLists[i]) {
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 assertTrue(locale.getLanguage().equals(testLanguageCodes[i]));
 }
 }
 }

 @Test
 public void testIsAvailableLocale() {
 List<Locale> testLocales = new ArrayList<Locale>(Arrays.asList(Locale.US, new Locale("fr", "FR"), new Locale("es", "ES"), new Locale("ru", "RU")));
 for (Locale locale : testLocales) {
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }
 Locale invalidLocale = new Locale("invalid", "locale");
 assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
 }
}