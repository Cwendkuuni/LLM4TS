package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_EmptyString() {
 Locale actual = LocaleUtils.toLocale("");
 assertNull(actual);
 }

 @Test
 public void testToLocale_NullString() {
 Locale actual = LocaleUtils.toLocale(null);
 assertNull(actual);
 }

 @Test
 public void testToLocale_ValidLocale() {
 Locale expected = new Locale("en_US");
 Locale actual = LocaleUtils.toLocale("en_US");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_Empty() {
 LocaleUtils.toLocale("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_SingleCharacter() {
 LocaleUtils.toLocale("a");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_TwoCharacters_InvalidLanguage() {
 LocaleUtils.toLocale("aa");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_TwoCharacters_InvalidCountry() {
 LocaleUtils.toLocale("en_b");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_ThreeCharacters_InvalidLanguage() {
 LocaleUtils.toLocale("aaa");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_ThreeCharacters_InvalidCountry_FirstCharNotUppercase() {
 LocaleUtils.toLocale("aa_BB");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_ThreeCharacters_InvalidCountry_SecondCharNotUppercase() {
 LocaleUtils.toLocale("aa_BBb");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_FourCharacters_WithoutUnderbar() {
 LocaleUtils.toLocale("aaBB");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_FourCharacters_WithoutLanguage() {
 LocaleUtils.toLocale("____CC");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_FourCharacters_InvalidCountryCode() {
 LocaleUtils.toLocale("aa__DD");
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
 List<Locale> actual = LocaleUtils.localeLookupList(null);
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testLocaleLookupList_EmptyStringLocale() {
 Locale locale = new Locale("");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 assertEquals(3, actual.size());
 assertTrue(actual.contains(locale));
 assertTrue(actual.contains(new Locale("", "")));
 assertTrue(actual.contains(Locale.getDefault()));
 }

 @Test
 public void testLocaleLookupList_ValidLocale() {
 Locale locale = new Locale("en_US");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 assertEquals(4, actual.size());
 assertTrue(actual.contains(locale));
 assertTrue(actual.contains(new Locale("en", "")));
 assertTrue(actual.contains(new Locale("", "US")));
 assertTrue(actual.contains(Locale.getDefault()));
 }

 @Test
 public void testLocaleLookupList_ValidLocaleWithVariant() {
 Locale locale = new Locale("en_US_POSIX");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 assertEquals(5, actual.size());
 assertTrue(actual.contains(locale));
 assertTrue(actual.contains(new Locale("en", "US")));
 assertTrue(actual.contains(new Locale("en", "")));
 assertTrue(actual.contains(new Locale("", "US")));
 assertTrue(actual.contains(Locale.getDefault()));
 }

 @Test
 public void testLocaleLookupList_NullDefaultLocale() {
 Locale locale = new Locale("en_US");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, null);
 assertEquals(4, actual.size());
 assertTrue(actual.contains(locale));
 assertTrue(actual.contains(new Locale("en", "")));
 assertTrue(actual.contains(new Locale("", "US")));
 assertTrue(actual.contains(Locale.getDefault()));
 }

 @Test
 public void testLocaleLookupList_ValidLocaleWithDefaultLocale() {
 Locale locale = new Locale("en_US");
 Locale defaultLocale = new Locale("fr_FR");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
 assertEquals(5, actual.size());
 assertTrue(actual.contains(locale));
 assertTrue(actual.contains(new Locale("en", "US")));
 assertTrue(actual.contains(new Locale("en", "")));
 assertTrue(actual.contains(new Locale("", "US")));
 assertTrue(actual.contains(defaultLocale));
 }

 @Test
 public void testLocaleLookupList_EmptyDefaultLocale() {
 Locale locale = new Locale("en_US");
 List<Locale> actual = LocaleUtils.localeLookupList(locale, Locale.getDefault());
 assertEquals(4, actual.size());
 assertTrue(actual.contains(locale));
 assertTrue(actual.contains(new Locale("en", "US")));
 assertTrue(actual.contains(new Locale("", "US")));
 assertTrue(actual.contains(Locale.getDefault()));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> actual = LocaleUtils.availableLocaleList();
 List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
 assertEquals(actual, expected);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> actual = LocaleUtils.availableLocaleSet();
 Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
 assertEquals(actual, expected);
 }

 @Test
 public void testIsAvailableLocale_ValidLocale() {
 Locale locale = new Locale("en_US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
 Locale locale = new Locale("aa_bb");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "JM"), new Locale("en", "MH"), new Locale("en", "TT"), new Locale("en", "VI"), new Locale("en_ZA"), new Locale("en_ZW"), new Locale("en_BE"), new Locale("en_AU"), new Locale("en_NZ"), new Locale("en_IE"), new Locale("en_SG"), new Locale("en_HK"), new Locale("en_MY"), new Locale("en_PH"), new Locale("en_IN"), new Locale("en_LK"), new Locale("en_PK"), new Locale("en_BD"), new Locale("en_SI"), new Locale("en_ZA"), new Locale("en_ZW"), new Locale("en_GB"), new Locale("en_JE"), new Locale("en_GG"), new Locale("en_IM"));
 assertEquals(actual, expected);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> expected = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"), new Locale("IE"), new Locale("JM"), new Locale("MH"), new Locale("TT"), new Locale("VI"), new Locale("ZA"), new Locale("ZW"), new Locale("BE"), new Locale("AU"), new Locale("NZ"), new Locale("IE"), new Locale("SG"), new Locale("HK"), new Locale("MY"), new Locale("PH"), new Locale("IN"), new Locale("LK"), new Locale("PK"), new Locale("BD"), new Locale("SI"), new Locale("ZA"), new Locale("ZW"), new Locale("GB"), new Locale("JE"), new Locale("GG"), new Locale("IM"));
 assertEquals(actual, expected);
 }

}