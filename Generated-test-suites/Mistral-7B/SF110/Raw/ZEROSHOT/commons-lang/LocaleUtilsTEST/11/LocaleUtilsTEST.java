package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Locale;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 private static final ConcurrentHashMap<String, List<Locale>> cLanguagesByCountryMock = new ConcurrentHashMap<String, List<Locale>>();
 private static final ConcurrentHashMap<String, List<Locale>> cCountriesByLanguageMock = new ConcurrentHashMap<String, List<Locale>>();

 @Test
 public void testToLocale_NullInput() {
 Locale actual = LocaleUtils.toLocale(null);
 assertEquals(null, actual);
 }

 @Test
 public void testToLocale_EmptyInput() {
 Locale actual = LocaleUtils.toLocale("");
 assertEquals(new Locale("", ""), actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormatNoLanguage() {
 LocaleUtils.toLocale("US");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormatNoCountry() {
 LocaleUtils.toLocale("en");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormatNoLanguageAndCountry() {
 LocaleUtils.toLocale("en_");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormatMissingCountry() {
 LocaleUtils.toLocale("en_fr");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormatMissingLanguage() {
 LocaleUtils.toLocale("fr_");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormatLanguageAndCountryLengthLessThanTwo() {
 LocaleUtils.toLocale("fr");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormatLanguageAndCountryLengthGreaterThanThree() {
 LocaleUtils.toLocale("fr_FR_FR");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormatLanguageAndCountryNotUppercase() {
 LocaleUtils.toLocale("fr_fr");
 }

 @Test
 public void testToLocale_ValidFormat() {
 Locale actual = LocaleUtils.toLocale("en_US");
 assertEquals(new Locale("en", "US"), actual);
 }

 @Test
 public void testToLocale_ValidFormatWithVariant() {
 Locale actual = LocaleUtils.toLocale("en_US_POSIX");
 assertEquals(new Locale("en", "US", "POSIX"), actual);
 }

 @Test
 public void testLocaleLookupList_NullInput() {
 List<Locale> actual = LocaleUtils.localeLookupList(null);
 assertEquals(Collections.emptyList(), actual);
 }

 @Test
 public void testLocaleLookupList_NullDefaultLocaleInput() {
 Locale defaultLocale = null;
 List<Locale> actual = LocaleUtils.localeLookupList(Locale.US, defaultLocale);
 assertEquals(Arrays.asList(Locale.US), actual);
 }

 @Test
 public void testLocaleLookupList_EmptyLocaleInput() {
 List<Locale> actual = LocaleUtils.localeLookupList(Locale.EMPTY);
 assertEquals(Collections.emptyList(), actual);
 }

 @Test
 public void testLocaleLookupList_SameLocaleInput() {
 Locale locale = new Locale("en", "US");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, locale);
 assertEquals(Arrays.asList(locale), actual);
 }

 @Test
 public void testLocaleLookupList_DifferentLocaleInput() {
 Locale locale1 = new Locale("en", "US");
 Locale locale2 = new Locale("es", "ES");
 List<Locale> actual = LocaleUtils.localeLookupList(locale1, locale2);
 assertEquals(Arrays.asList(locale1, new Locale("en", ""), new Locale("", ""), locale2), actual);
 }

 @Test
 public void testLocaleLookupList_DefaultLocaleInput() {
 Locale locale = new Locale("en", "US");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, Locale.getDefault());
 assertEquals(Arrays.asList(locale, new Locale("en", ""), new Locale("", ""), Locale.getDefault()), actual);
 }

 @Test
 public void testLocaleLookupList_LocaleWithVariantInput() {
 Locale locale = new Locale("en", "US", "POSIX");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, Locale.getDefault());
 assertEquals(Arrays.asList(locale, new Locale("en", "US"), new Locale("en", ""), new Locale("", ""), Locale.getDefault()), actual);
 }

 @Test
 public void testLocaleLookupList_LocaleWithDifferentVariantInput() {
 Locale locale1 = new Locale("en", "US", "POSIX");
 Locale locale2 = new Locale("en", "US", "MS");
 List<Locale> actual = LocaleUtils.localeLookupList(locale1, locale2);
 assertEquals(Arrays.asList(locale1, new Locale("en", "US"), new Locale("en", ""), new Locale("", ""), locale2), actual);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> actual = LocaleUtils.availableLocaleSet();
 Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocale_NullInput() {
 boolean actual = LocaleUtils.isAvailableLocale(null);
 assertFalse(actual);
 }

 @Test
 public void testIsAvailableLocale_InvalidLocaleInput() {
 boolean actual = LocaleUtils.isAvailableLocale(new Locale("", ""));
 assertFalse(actual);
 }

 @Test
 public void testIsAvailableLocale_ValidLocaleInput() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("es"), new Locale("fa"), new Locale("it"), new Locale("pt"), new Locale("de"));
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> expected = Arrays.asList(new Locale("US"), new Locale("GB"), new Locale("CA"), new Locale("IE"), new Locale("AU"), new Locale("ZA"), new Locale("NZ"), new Locale("JM"), new Locale("SG"), new Locale("TT"));
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleUtilsInitialization() {
 assertEquals(cLanguagesByCountryMock, LocaleUtils.cLanguagesByCountry);
 assertEquals(cCountriesByLanguageMock, LocaleUtils.cCountriesByLanguage);
 }
}