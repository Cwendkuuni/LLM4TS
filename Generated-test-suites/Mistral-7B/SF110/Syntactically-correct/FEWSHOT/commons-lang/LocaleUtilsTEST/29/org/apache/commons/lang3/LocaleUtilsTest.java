package org.apache.commons.lang3;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocaleEmpty() {
 assertNull(LocaleUtils.toLocale(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat1() {
 LocaleUtils.toLocale("foo");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat2() {
 LocaleUtils.toLocale("_foo");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat3() {
 LocaleUtils.toLocale("foo_");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat4() {
 LocaleUtils.toLocale("foo_bar");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat5() {
 LocaleUtils.toLocale("foo__bar");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat6() {
 LocaleUtils.toLocale("---foo---");
 }

 @Test
 public void testToLocaleValidFormat1() {
 assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
 }

 @Test
 public void testToLocaleValidFormat2() {
 assertEquals(new Locale("en_US"), LocaleUtils.toLocale("en_US"));
 }

 @Test
 public void testToLocaleValidFormat3() {
 assertEquals(new Locale("de_DE"), LocaleUtils.toLocale("de_DE"));
 }

 @Test
 public void testToLocaleValidFormat4() {
 assertEquals(new Locale("fr_CA"), LocaleUtils.toLocale("fr_CA"));
 }

 @Test
 public void testToLocaleValidFormat5() {
 assertEquals(new Locale("es_ES"), LocaleUtils.toLocale("es_ES"));
 }

 @Test
 public void testToLocaleValidFormat6() {
 assertEquals(new Locale("es_MX"), LocaleUtils.toLocale("es_MX"));
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("en_US");
 List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("en_US"), new Locale(""), new Locale("", ""));
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListNull() {
 List<Locale> actual = LocaleUtils.localeLookupList(null);
 assertTrue(Collections.isEmpty(actual));
 }

 @Test
 public void testLocaleLookupListDefaultLocale() {
 Locale locale = null;
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = new ArrayList<Locale>(1);
 expected.add(Locale.getDefault());
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListEmptyLocale() {
 Locale locale = new Locale("");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 assertTrue(Collections.isEmpty(actual));
 }

 @Test
 public void testLocaleLookupListSimpleLanguage() {
 Locale locale = new Locale("en");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("en_US"), new Locale(""), new Locale("", ""));
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListSimpleCountry() {
 Locale locale = new Locale("", "US");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = Arrays.asList(new Locale("en_US"), new Locale("en"), new Locale("", ""));
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListLanguageAndCountry() {
 Locale locale = new Locale("en_GB");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = Arrays.asList(new Locale("en_GB"), new Locale("en"), new Locale("en_US"), new Locale("", ""));
 assertEquals(expected, actual);
 }

 @Test
 public void testLocaleLookupListLanguageAndCountryAndVariant() {
 Locale locale = new Locale("en_GB_variant");
 List<Locale> actual = LocaleUtils.localeLookupList(locale);
 List<Locale> expected = Arrays.asList(new Locale("en_GB_variant"), new Locale("en_GB"), new Locale("en"), new Locale("en_US"), new Locale("", ""));
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleList() {
 Collection<Locale> expected = LocaleUtils.availableLocaleList();
 Collection<Locale> actual = Locale.getAvailableLocales();
 assertEquals(expected, actual);
 }

 @Test
 public void testAvailableLocaleSet() {
 Collection<Locale> expected = LocaleUtils.availableLocaleSet();
 Collection<Locale> actual = Locale.getAvailableLocales();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsAvailableLocale() {
 assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
 }

 @Test
 public void testIsAvailableLocaleFalse() {
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("foobar")));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> expected = Arrays.asList(new Locale("en_GB"), new Locale("en_IE"), new Locale("en_CA"), new Locale("en_AU"), new Locale("en_NZ"), new Locale("en_JM"), new Locale("en_ZA"), new Locale("en_SG"), new Locale("en_TT"), new Locale("en_HK"), new Locale("en_SG"), new Locale("en_IN"), new Locale("en_PK"), new Locale("en_ID"), new Locale("en_MY"), new Locale("en_PH"), new Locale("en_LK"), new Locale("en_BN"), new Locale("en_MM"), new Locale("en_BD"), new Locale("en_SL"), new Locale("en_GB_JM"), new Locale("en_GB_IE"), new Locale("en_GB_CA"), new Locale("en_GB_AU"), new Locale("en_GB_NZ"), new Locale("en_GB_JE"), new Locale("en_GB_CI"), new Locale("en_GB_IM"), new Locale("en_GB_GS"), new Locale("en_GB_GG"), new Locale("en_GB_PK"), new Locale("en_GB_IN"), new Locale("en_GB_ID"), new Locale("en_GB_MY"), new Locale("en_GB_PH"), new Locale("en_GB_LK"), new Locale("en_GB_BN"), new Locale("en_GB_MM"), new Locale("en_GB_BD"), new Locale("en_GB_BIH"), new Locale("en_GB_SRB"), new Locale("en_GB_MNE"), new Locale("en_GB_ALB"));
 List<Locale> actual = LocaleUtils.languagesByCountry("GB");
 assertEquals(expected, actual);
 }

 @Test
 public void testLanguagesByCountryEmpty() {
 assertTrue(Collections.isEmpty(LocaleUtils.languagesByCountry(null)));
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> expected = Arrays.asList(new Locale("de_AT"), new Locale("de_CH"), new Locale("de_DE"), new Locale("de_LI"), new Locale("de_LU"), new Locale("de_CH_SZ"), new Locale("de_AT_AUT"), new Locale("de_AT_BW"), new Locale("de_AT_CA"), new Locale("de_AT_CV"), new Locale("de_AT_FO"), new Locale("de_AT_HE"), new Locale("de_AT_NB"), new Locale("de_AT_SX"), new Locale("de_AT_ST"), new Locale("de_AT_TI"), new Locale("de_AT_VA"), new Locale("de_AT_WK"), new Locale("de_AT_ZF"), new Locale("de_CH_BS"), new Locale("de_CH_FR"), new Locale("de_CH_GR"), new Locale("de_CH_IT"), new Locale("de_CH_LU"), new Locale("de_CH_TI"), new Locale("de_CH_TG"), new Locale("de_CH_UR"), new Locale("de_LI_NL"), new Locale("de_LU_LU"), new Locale("de_LU_FR"), new Locale("de_LU_BE"), new Locale("de_LU_BL"), new Locale("de_LU_BR"), new Locale("de_LU_DJ"), new Locale("de_LU_GL"), new Locale("de_LU_MH"), new Locale("de_LU_RO"), new Locale("de_LU_BE_NL"), new Locale("de_LU_BE_FR"), new Locale("de_LU_BE_LU"), new Locale("de_LU_BE_LX"), new Locate("de_LU_BE_LU_NL"), new Locale("de_LU_BE_LU_RO"), new Locale("de_LU_BE_LU_BE"), new Locale("de_LU_BE_LU_BE_CH"), new Locale("de_LU_BE_LU_BE_NL"), new Locale("de_LU_BE_LU_BE_LX"));
 List<Locale> actual = LocaleUtils.countriesByLanguage("de");
 assertEquals(expected, actual);
 }

 @Test
 public void testCountriesByLanguageEmpty() {
 assertTrue(Collections.isEmpty(LocaleUtils.countriesByLanguage(null)));
 }
}