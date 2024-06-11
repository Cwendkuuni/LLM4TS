package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;

public class LocaleUtilsTest {

 private static final Locale DEFAULT_LOCALE = Locale.getDefault();

 @Test
 public void testToLocaleEmpty() {
 assertNull(LocaleUtils.toLocale(""));
 }

 @Test
 public void testToLocaleInvalidFormatEmpty() {
 assertNull(LocaleUtils.toLocale(null));
 assertNull(LocaleUtils.toLocale(""));
 assertNull(LocaleUtils.toLocale(" "));
 }

 @Test
 public void testToLocaleInvalidFormatNoLanguage() {
 assertNull(LocaleUtils.toLocale("_"));
 assertNull(LocaleUtils.toLocale("--"));
 assertNull(LocaleUtils.toLocale("--foo"));
 assertNull(LocaleUtils.toLocale("_foo"));
 assertNull(LocaleUtils.toLocale("_foo_"));
 assertNull(LocaleUtils.toLocale("_foo_bar"));
 }

 @Test
 public void testToLocaleInvalidFormatNoCountry() {
 assertNull(LocaleUtils.toLocale("en"));
 assertNull(LocaleUtils.toLocale("en_"));
 assertNull(LocaleUtils.toLocale("en_foo"));
 assertNull(LocaleUtils.toLocale("en_foo_"));
 assertNull(LocaleUtils.toLocale("en_foo_bar"));
 }

 @Test
 public void testToLocaleValidFormat() {
 final String language = "en";
 final String country = "US";
 final String variant = "en_US";

 assertEquals(new Locale(language, country, variant), LocaleUtils.toLocale(language + "_" + country + "_" + variant));
 assertEquals(new Locale(language, country), LocaleUtils.toLocale(language + "_" + country));
 assertEquals(new Locale(language, ""), LocaleUtils.toLocale(language + "_"));
 assertEquals(new Locale("", country), LocaleUtils.toLocale("_" + country));
 assertEquals(new Locale("", "", ""), LocaleUtils.toLocale(""));
 }

 @Test
 public void testToLocaleMixedCase() {
 final String language = "en";
 final String country = "US";
 final String variant = "en_US";

 assertEquals(new Locale(language, country, variant), LocaleUtils.toLocale(language.toUpperCase() + "_" + country.toUpperCase() + "_" + variant));
 assertEquals(new Locale(language, country), LocaleUtils.toLocale(language.toUpperCase() + "_" + country.toUpperCase()));
 assertEquals(new Locale(language, ""), LocaleUtils.toLocale(language.toUpperCase() + "_"));
 assertEquals(new Locale("", country), LocaleUtils.toLocale("_" + country.toUpperCase()));
 assertEquals(new Locale("", "", ""), LocaleUtils.toLocale(""));
 }

 @Test
 public void testToLocaleLeadingHyphens() {
 final String language = "en";
 final String country = "US";
 final String variant = "en_US";

 assertEquals(new Locale("", country), LocaleUtils.toLocale("--" + country));
 assertEquals(new Locale(language, country), LocaleUtils.toLocale("-" + language + "_" + country));
 assertEquals(new Locale(language, ""), LocaleUtils.toLocale("-" + language));
 }

 @Test
 public void testLocaleLookupListNullLocale() {
 assertNotNull(LocaleUtils.localeLookupList(null));
 assertFalse(LocaleUtils.localeLookupList(null).isEmpty());
 }

 @Test
 public void testLocaleLookupListEmptyLocale() {
 assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(new Locale("")));
 }

 @Test
 public void testLocaleLookupListValidLocale() {
 final String language = "en";
 final String country = "US";
 final String variant = "en_US";
 final Locale locale = new Locale(language, country, variant);
 List<Locale> list = LocaleUtils.localeLookupList(locale);
 assertEquals(locale, list.get(0));
 assertTrue(list.contains(locale));
 assertEquals(locale, list.get(1));
 assertTrue(list.size() == 2);
 }

 @Test
 public void testLocaleLookupListDefaultLocale() {
 List<Locale> list = LocaleUtils.localeLookupList(null);
 assertFalse(list.contains(DEFAULT_LOCALE));
 }

 @Test
 public void testLocaleLookupListDefaultLocaleAdded() {
 List<Locale> list = LocaleUtils.localeLookupList(null);
 list.add(DEFAULT_LOCALE);
 assertTrue(list.contains(DEFAULT_LOCALE));
 }

 @Test
 public void testLocaleLookupListSameLocaleAdded() {
 List<Locale> list = LocaleUtils.localeLookupList(new Locale("en", "US"));
 list.add(new Locale("en", "US"));
 assertEquals(2, list.size());
 }

 @Test
 public void testLocaleLookupListDifferentLocaleAdded() {
 List<Locale> list = LocaleUtils.localeLookupList(new Locale("en", "US"));
 list.add(new Locale("fr", "FR"));
 assertEquals(2, list.size());
 }

 @Test
 public void testAvailableLocaleListEmpty() {
 assertEquals(Collections.emptyList(), LocaleUtils.availableLocaleList());
 }

 @Test
 public void testAvailableLocaleListSize() {
 assertEquals(LocaleUtils.availableLocaleList().size(), LocaleUtils.availableLocaleSet().size());
 }

 @Test
 public void testAvailableLocaleListContains() {
 final List<Locale> list = LocaleUtils.availableLocaleList();
 final Locale locale1 = new Locale("en", "US");
 final Locale locale2 = new Locale("es", "ES");
 assertTrue(list.contains(locale1));
 assertTrue(list.contains(locale2));
 }

 @Test
 public void testAvailableLocaleSetEmpty() {
 assertEquals(Collections.emptySet(), LocaleUtils.availableLocaleSet());
 }

 @Test
 public void testAvailableLocaleSetSize() {
 assertEquals(LocaleUtils.availableLocaleList().size(), LocaleUtils.availableLocaleSet().size());
 }

 @Test
 public void testAvailableLocaleSetContains() {
 final Set<Locale> set = LocaleUtils.availableLocaleSet();
 final Locale locale1 = new Locale("en", "US");
 final Locale locale2 = new Locale("es", "ES");
 assertTrue(set.contains(locale1));
 assertTrue(set.contains(locale2));
 }

 @Test
 public void testIsAvailableLocaleNullLocale() {
 assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testIsAvailableLocaleEmptyLocale() {
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("")));
 }

 @Test
 public void testIsAvailableLocaleValidLocale() {
 final String language = "en";
 final String country = "US";
 final String variant = "en_US";
 final Locale locale = new Locale(language, country, variant);
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocaleInvalidLocaleFormat() {
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_US#")));
 }

 @Test
 public void testLanguagesByCountryEmptyCountry() {
 assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
 }

 @Test
 public void testLanguagesByCountryEmptyLanguage() {
 assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(""));
 }

 @Test
 public void testLanguagesByCountryValidCountry() {
 final String country = "US";
 List<Locale> langs = LocaleUtils.languagesByCountry(country);
 assertEquals(langs.size(), 3); // English (US), English (CA), English (GB)
 }

 @Test
 public void testCountriesByLanguageEmptyLanguage() {
 assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
 }

 @Test
 public void testCountriesByLanguageValidLanguage() {
 final String language = "en";
 List<Locale> countries = LocaleUtils.countriesByLanguage(language);
 assertEquals(countries.size(), 16); // US, CA, GB, AU, IE, NZ, ZA, IN, PK, ID, MY, SG, PH, HK, TW
 }

 @Test
 public void testCountriesByLanguageDifferentLanguage() {
 final String language = "es";
 List<Locale> countries = LocaleUtils.countriesByLanguage(language);
 assertEquals(countries.size(), 21); // ES, AR, BO, BR, CL, CO, CR, CY, DO, EC, GT, HN, MX, PA, PE, PR, UY, VE, WS
 }
}