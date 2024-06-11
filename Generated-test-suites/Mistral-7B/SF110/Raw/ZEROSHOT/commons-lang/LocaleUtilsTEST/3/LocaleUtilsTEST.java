package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

 private static final String INVALID_LOCALE = "invalidLocale";
 private static final String INVALID_LOCALE_SHORT = "invalidLocale_";
 private static final String INVALID_LOCALE_LONG = "_invalidLocale_long";
 private static final String VALID_LOCALE = "en_US";
 private static final String VALID_LOCALE_SHORT = "en";
 private static final String VALID_LOCALE_LONG = "_en_US";
 private static final String VALID_LOCALE_EMPTY = "";
 private static final String VALID_LOCALE_EMPTY_SHORT = "_";
 private static final String VALID_LOCALE_LANG_ONLY = "fr";
 private static final String VALID_LOCALE_COUNTRY_ONLY = "CH";
 private static final String VALID_LOCALE_LANG_COUNTRY = "fr_CH";
 private static final String VALID_LOCALE_LANG_VARIANT = "fr_CH_joo";

 @Test
 public void testToLocale_Null() {
 assertNull(LocaleUtils.toLocale(null));
 }

 @Test
 public void testToLocale_Empty() {
 assertEquals(new Locale("", ""), LocaleUtils.toLocale(VALID_LOCALE_EMPTY));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoLanguage() {
 assertNull(LocaleUtils.toLocale(INVALID_LOCALE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_NoCountry() {
 assertNull(LocaleUtils.toLocale(VALID_LOCALE_LANG_ONLY));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidLanguage() {
 assertNull(LocaleUtils.toLocale("ab_AB"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidCountry() {
 assertNull(LocaleUtils.toLocale("en_XX"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_InvalidLanguageCountry() {
 assertNull(LocaleUtils.toLocale("XX_YY"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_MissingLanguageOrCountry() {
 assertNull(LocaleUtils.toLocale(VALID_LOCALE_EMPTY_SHORT));
 assertNull(LocaleUtils.toLocale(VALID_LOCALE_SHORT));
 assertNull(LocaleUtils.toLocale(VALID_LOCALE_LONG));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_MissingLanguageOrCountry_FirstPart() {
 assertNull(LocaleUtils.toLocale("_XX"));
 assertNull(LocaleUtils.toLocale("XX"));
 assertNull(LocaleUtils.toLocale("XX_"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_MissingLanguageOrCountry_SecondPart() {
 assertNull(LocaleUtils.toLocale("en_"));
 assertNull(LocaleUtils.toLocale("en_ "));
 assertNull(LocaleUtils.toLocale("en_ "));
 }

 @Test
 public void testToLocale_ValidLocale() {
 assertEquals(new Locale("", VALID_LOCALE), LocaleUtils.toLocale(VALID_LOCALE));
 }

 @Test
 public void testToLocale_ValidLocaleShort() {
 assertEquals(new Locale("", VALID_LOCALE_SHORT), LocaleUtils.toLocale(VALID_LOCALE_SHORT));
 }

 @Test
 public void testToLocale_ValidLocaleLong() {
 assertEquals(new Locale("", VALID_LOCALE_LONG), LocaleUtils.toLocale(VALID_LOCALE_LONG));
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
 assertFalse(LocaleUtils.localeLookupList(null).isEmpty());
 }

 @Test
 public void testLocaleLookupList() {
 List<Locale> locales = LocaleUtils.localeLookupList(new Locale("fr", "CH"));
 assertEquals(3, locales.size());
 locales = LocaleUtils.localeLookupList(new Locale("fr", "CH"), new Locale("en", "US"));
 assertEquals(4, locales.size());
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocales = LocaleUtils.availableLocaleList();
 assertFalse(availableLocales.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
 assertFalse(availableLocales.isEmpty());
 }

 @Test
 public void testIsAvailableLocale() {
 assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> frenchLocales = LocaleUtils.languagesByCountry("CH");
 assertEquals(3, frenchLocales.size());
 List<Locale> germanLocales = LocaleUtils.languagesByCountry("DE");
 assertEquals(2, germanLocales.size());
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> germanyLocales = LocaleUtils.countriesByLanguage("de");
 assertEquals(3, germanyLocales.size());
 List<Locale> chineseLocales = LocaleUtils.countriesByLanguage("zh");
 assertEquals(3, chineseLocales.size());
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 List<Locale> locales = LocaleUtils.localeLookupList(null);
 assertEquals(5, locales.size());
 }
}