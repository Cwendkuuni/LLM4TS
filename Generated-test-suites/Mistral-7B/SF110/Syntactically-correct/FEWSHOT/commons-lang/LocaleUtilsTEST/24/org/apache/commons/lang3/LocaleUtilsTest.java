package org.apache.commons.lang3;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 String[] testCases = new String[]{"en", "en_US", "fr_FR", "es_ES", "_es", "fr_FR_x", "invalidFormat"};
 for (String testCase : testCases) {
 try {
 Locale locale = LocaleUtils.toLocale(testCase);
 assertEquals("Expected valid locale for: " + testCase, locale.getClass(), Locale.class);
 } catch (IllegalArgumentException e) {
 assertEquals("Expected IllegalArgumentException for: " + testCase, e.getMessage(), "Invalid locale format: " + testCase);
 }
 }
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("en", "US");
 List<Locale> list = LocaleUtils.localeLookupList(locale);
 List<Locale> expectedList = new ArrayList<Locale>();
 expectedList.add(locale);
 expectedList.add(new Locale("en", ""));
 expectedList.add(new Locale("", ""));
 assertEquals("Expected list size: " + list.size(), expectedList.size(), list.size());
 for (int i = 0; i < expectedList.size(); i++) {
 assertEquals("Expected equal locale at index: " + i, expectedList.get(i), list.get(i));
 }

 locale = new Locale("fr", "FR");
 list = LocaleUtils.localeLookupList(locale);
 expectedList.clear();
 expectedList.add(locale);
 expectedList.add(new Locale("fr", ""));
 expectedList.add(new Locale("", "FR"));
 expectedList.add(new Locale("", ""));
 assertEquals("Expected list size: " + list.size(), expectedList.size(), list.size());
 for (int i = 0; i < expectedList.size(); i++) {
 assertEquals("Expected equal locale at index: " + i, expectedList.get(i), list.get(i));
 }
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale defaultLocale = new Locale("en", "US");
 Locale locale = new Locale("fr", "FR");
 List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
 List<Locale> expectedList = new ArrayList<Locale>();
 expectedList.add(locale);
 expectedList.add(new Locale("fr", ""));
 expectedList.add(defaultLocale);
 assertEquals("Expected list size: " + list.size(), expectedList.size(), list.size());
 for (int i = 0; i < expectedList.size(); i++) {
 assertEquals("Expected equal locale at index: " + i, expectedList.get(i), list.get(i));
 }
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 List<Locale> expectedAvailableLocaleList = Arrays.asList(Locale.getAvailableLocales());
 assertEquals("Expected list size: " + availableLocaleList.size(), expectedAvailableLocaleList.size(), availableLocaleList.size());
 for (int i = 0; i < expectedAvailableLocaleList.size(); i++) {
 assertEquals("Expected equal locale at index: " + i, expectedAvailableLocaleList.get(i), availableLocaleList.get(i));
 }
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
 Set<Locale> expectedAvailableLocaleSet = new HashSet<Locale>(Locale.getAvailableLocales());
 assertEquals("Expected set size: " + availableLocaleSet.size(), expectedAvailableLocaleSet.size(), availableLocaleSet.size());
 for (Locale locale : expectedAvailableLocaleSet) {
 assertTrue("Expected set to contain: " + locale, availableLocaleSet.contains(locale));
 }
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> expectedLanguages = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"));
 assertEquals("Expected list size: " + languages.size(), expectedLanguages.size(), languages.size());
 for (int i = 0; i < expectedLanguages.size(); i++) {
 assertEquals("Expected equal locale at index: " + i, expectedLanguages.get(i), languages.get(i));
 }
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> expectedCountries = Arrays.asList(new Locale("US", "en"), new Locale("CA", "en"), new Locale("GB", "en"));
 assertEquals("Expected list size: " + countries.size(), expectedCountries.size(), countries.size());
 for (int i = 0; i < expectedCountries.size(); i++) {
 assertEquals("Expected equal locale at index: " + i, expectedCountries.get(i), countries.get(i));
 }
 }

 @Test
 public void testCachedLocaleLookupLists() {
 Locale locale = new Locale("en", "US");
 List<Locale> list = LocaleUtils.localeLookupList(locale);
 List<Locale> cachedList = new ArrayList<Locale>();
 cachedList.add(locale);
 cachedList.add(new Locale("en", ""));
 cachedList.add(new Locale("", ""));
 LocaleUtils.cLanguagesByCountry.put("US", cachedList);

 locale = new Locale("fr", "FR");
 list = LocaleUtils.localeLookupList(locale);
 List<Locale> cachedListFR = new ArrayList<Locale>();
 cachedListFR.add(locale);
 cachedListFR.add(new Locale("fr", ""));
 cachedListFR.add(new Locale("", "FR"));
 cachedListFR.add(new Locale("", ""));
 LocaleUtils.cCountriesByLanguage.put("fr", cachedListFR);

 assertEquals("Expected cached list size: " + list.size(), cachedList.size(), list.size());
 for (int i = 0; i < cachedList.size(); i++) {
 assertEquals("Expected equal locale at index: " + i, cachedList.get(i), list.get(i));
 }
 }
}