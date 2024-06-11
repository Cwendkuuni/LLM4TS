package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap<>();
 private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap<>();

 @Test
 public void testToLocale() {
 final String[] validLocales = new String[]{"en", "en_US", "en_US_POSIX", "fr_FR", "fr_FR_POSIX", "de_DE", "de_DE_POSIX", "es_ES", "es_ES_POSIX", "ja_JP", "ja_JP_POSIX", "zh_CN", "zh_CN_POSIX", "ko_KR", "ko_KR_POSIX"};
 for (String locale : validLocales) {
 Locale expected = null;
 if (locale.contains("_")) {
 int index = locale.indexOf("_");
 int languageLength = locale.length() - index;
 String language = locale.substring(0, index);
 String country = locale.substring(index + 1);
 expected = new Locale(language, country);
 } else {
 expected = new Locale(locale);
 }
 Locale actual = LocaleUtils.toLocale(locale);
 assertEquals(expected.getLanguage(), actual.getLanguage());
 assertEquals(expected.getCountry(), actual.getCountry());
 assertEquals(expected.getVariant(), actual.getVariant());
 }

 final String[] invalidLocales = new String[]{"", "_invalid_", "en_US#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#", "en_US_POSIX#"};
 for (String invalidLocale : invalidLocales) {
 try {
 LocaleUtils.toLocale(invalidLocale);
 fail("Expected IllegalArgumentException for invalid locale: " + invalidLocale);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "FR");
 List<Locale> expected = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", ""), new Locale("", "FR"), new Locale("", ""));
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 assertEquals(expected, actual);

 locale = null;
 actual = LocaleUtils.localeLookupList(locale, new Locale("en", "US"));
 assertEquals(Arrays.asList(new Locale("en", "US")), actual);
 }

 @Test
 public void testLocaleLookupListConcurrentModificationException() {
 List<Locale> list = new ArrayList<>();
 list.add(new Locale("fr", "FR"));
 list.add(new Locale("fr", ""));
 list.add(new Locale("", "FR"));
 list.add(new Locale("", ""));

 Locale testLocale = new Locale("en", "US");
 LocaleUtils.localeLookupList(testLocale, testLocale);

 try {
 list.remove(1);
 LocaleUtils.localeLookupList(testLocale, testLocale);
 fail("Expected ConcurrentModificationException");
 } catch (ConcurrentModificationException e) {
 // expected
 }
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expected = LocaleUtils.availableLocaleList();
 int size = expected.size();
 List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
 assertEquals(size, actual.size());
 for (int i = 0; i < size; i++) {
 assertEquals(expected.get(i).getDisplayName(), actual.get(i).getDisplayName());
 }
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expected = LocaleUtils.availableLocaleSet();
 int size = expected.size();
 Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
 assertEquals(size, actual.size());
 assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocale() {
 List<Locale> locales = Arrays.asList(Locale.getAvailableLocales());
 for (Locale locale : locales) {
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 Locale invalidLocale = new Locale("invalid", "locale");
 assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> expectedFr = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "FR"), new Locale("fr", "BE"), new Locale("fr", "CH"));
 List<Locale> actualFr = LocaleUtils.languagesByCountry("FR");
 assertEquals(expectedFr, actualFr);

 List<Locale> expectedKo = Arrays.asList(new Locale("ko", "KR"));
 List<Locale> actualKo = LocaleUtils.languagesByCountry("KR");
 assertEquals(expectedKo, actualKo);

 List<Locale> empty = Collections.emptyList();
 List<Locale> actualEmpty = LocaleUtils.languagesByCountry(null);
 assertEquals(empty, actualEmpty);
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> expectedEn = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "JM"), new Locale("en", "NZ"), new Locale("en", "TT"), new Locale("en", "ZW"));
 List<Locale> actualEn = LocaleUtils.countriesByLanguage("en");
 assertEquals(expectedEn, actualEn);

 List<Locale> empty = Collections.emptyList();
 List<Locale> actualEmpty = LocaleUtils.countriesByLanguage(null);
 assertEquals(empty, actualEmpty);
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> expected = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", ""), new Locale("", "FR"), new Locale("", ""), new Locale("en", "US"));
 List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "FR"), defaultLocale);
 assertEquals(expected, actual);
 }
}