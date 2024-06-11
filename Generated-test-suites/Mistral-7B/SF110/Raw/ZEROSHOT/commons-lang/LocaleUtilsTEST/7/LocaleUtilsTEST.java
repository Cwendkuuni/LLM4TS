package org.apache.commons.lang3;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class LocaleUtilsTest {

 private static ConcurrentHashMap<String, List<Locale>> cLanguagesByCountry;
 private static ConcurrentHashMap<String, List<Locale>> cCountriesByLanguage;

 @BeforeClass
 public static void setup() {
 cLanguagesByCountry = new ConcurrentHashMap<>();
 cCountriesByLanguage = new ConcurrentHashMap<>();
 }

 @Test
 public void testToLocaleNull() {
 assertNull(LocaleUtils.toLocale(null));
 }

 @Test
 public void testToLocaleEmpty() {
 assertNotNull(LocaleUtils.toLocale(""));
 assertEquals("", LocaleUtils.toLocale("").getLanguage());
 assertEquals("", LocaleUtils.toLocale("").getCountry());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatNoLanguage() {
 LocaleUtils.toLocale("FR");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatNoCountry() {
 LocaleUtils.toLocale("en_");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatNoLanguageAndCountry() {
 LocaleUtils.toLocale("_");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatExtraCharacters() {
 LocaleUtils.toLocale("en_US#");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatLanguageTooShort() {
 LocaleUtils.toLocale("en");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatLanguageTooLong() {
 LocaleUtils.toLocale("en_FR");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatCountryTooShort() {
 LocaleUtils.toLocale("en_US");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatCountryTooLong() {
 LocaleUtils.toLocale("en_US_France");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatMixedCasesLanguage() {
 LocaleUtils.toLocale("en_US_france");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormatMixedCasesCountry() {
 LocaleUtils.toLocale("en_FR_us");
 }

 @Test
 public void testToLocaleValidFormat() {
 assertEquals("en", LocaleUtils.toLocale("en").getLanguage());
 assertEquals("US", LocaleUtils.toLocale("en_US").getCountry());
 assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
 }

 @Test
 public void testLocaleLookupListNullLocale() {
 assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
 }

 @Test
 public void testLocaleLookupListEmptyLocale() {
 Locale emptyLocale = new Locale("", "");
 List<Locale> resultList = LocaleUtils.localeLookupList(emptyLocale);
 assertEquals(1, resultList.size());
 assertEquals(emptyLocale, resultList.get(0));
 }

 @Test
 public void testLocaleLookupListLocaleWithVariant() {
 Locale locale = new Locale("es", "ES", "Castilian");
 List<Locale> resultList = LocaleUtils.localeLookupList(locale);
 assertEquals(3, resultList.size());
 assertEquals(locale, resultList.get(0));
 assertEquals(new Locale("es", "ES"), resultList.get(1));
 assertEquals(new Locale("es"), resultList.get(2));
 }

 @Test
 public void testLocaleLookupListLocaleWithoutVariant() {
 Locale locale = new Locale("es", "ES");
 List<Locale> resultList = LocaleUtils.localeLookupList(locale);
 assertEquals(2, resultList.size());
 assertEquals(locale, resultList.get(0));
 assertEquals(new Locale("es"), resultList.get(1));
 }

 @Test
 public void testLocaleLookupListDefaultLocale() {
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> resultList = LocaleUtils.localeLookupList(defaultLocale);
 assertEquals(3, resultList.size());
 assertEquals(defaultLocale, resultList.get(0));
 assertEquals(new Locale("en"), resultList.get(1));
 assertEquals(new Locale(""), resultList.get(2));
 }

 @Test
 public void testLocaleLookupListDefaultLocaleEmpty() {
 List<Locale> resultList = LocaleUtils.localeLookupList(null);
 assertEquals(1, resultList.size());
 assertEquals(Locale.getDefault(), resultList.get(0));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> resultList = LocaleUtils.availableLocaleList();
 assertFalse(resultList.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> resultSet = LocaleUtils.availableLocaleSet();
 assertFalse(resultSet.isEmpty());
 }

 @Test
 public void testIsAvailableLocaleNull() {
 assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testIsAvailableLocaleUnknownLocale() {
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "yy")));
 }

 @Test
 public void testIsAvailableLocaleAvailableLocale() {
 assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> resultList = LocaleUtils.languagesByCountry("US");
 assertFalse(resultList.isEmpty());
 }

 @Test
 public void testLanguagesByCountryEmptyCountry() {
 assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> resultList = LocaleUtils.countriesByLanguage("en");
 assertFalse(resultList.isEmpty());
 }

 @Test
 public void testCountriesByLanguageEmptyLanguage() {
 assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
 }
}