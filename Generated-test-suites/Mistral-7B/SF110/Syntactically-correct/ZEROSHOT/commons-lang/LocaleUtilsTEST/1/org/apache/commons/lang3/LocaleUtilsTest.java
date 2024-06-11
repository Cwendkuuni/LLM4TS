package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class LocaleUtilsTest {

 private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap<String, List<Locale>>();
 private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap<String, List<Locale>>();

 @Test
 public void testToLocale_ValidArguments() {
 Locale expectedLocale = new Locale("en", "US");
 String localeString = "en_US";
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);
 }

 @Test
 public void testToLocale_EmptyString() {
 Locale actualLocale = LocaleUtils.toLocale("");
 assertEquals(new Locale("", ""), actualLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_NullString() {
 LocaleUtils.toLocale(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoLanguage() {
 String localeString = "_US";
 LocaleUtils.toLocale(localeString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoCountry() {
 String localeString = "en_";
 LocaleUtils.toLocale(localeString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidCountryLength() {
 String localeString = "en_US_";
 LocaleUtils.toLocale(localeString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidLanguageLength() {
 String localeString = "en_US_fr";
 LocaleUtils.toLocale(localeString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_MiddleSeparator() {
 String localeString = "en_US#fr";
 LocaleUtils.toLocale(localeString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidLanguageCase() {
 String localeString = "en_US_Fr";
 LocaleUtils.toLocale(localeString);
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("en", "US");
 List<Locale> expectedList = Arrays.asList(locale, new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);
 assertEquals(expectedList, actualList);
 }

 @Test(expected = NullPointerException.class)
 public void testLocaleLookupList_NullLocale() {
 LocaleUtils.localeLookupList(null);
 }

 @Test
 public void testLocaleLookupList_EmptyLocale() {
 Locale locale = new Locale("");
 List<Locale> expectedList = Arrays.asList(locale);
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 Locale defaultLocale = new Locale("en", "US");
 Locale locale = null;
 List<Locale> expectedList = Arrays.asList(defaultLocale, locale);
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedList = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
 List<Locale> actualList = LocaleUtils.availableLocaleList();
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expectedSet = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
 Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
 assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testIsAvailableLocale_ValidLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
 Locale locale = new Locale("en", "US_invalid");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"));
 List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testLanguagesByCountry_EmptyCountryCode() {
 String countryCode = "";
 List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(Collections.emptyList(), actualList);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"));
 List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testCountriesByLanguage_EmptyLanguageCode() {
 String languageCode = "";
 List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(Collections.emptyList(), actualList);
 }
}