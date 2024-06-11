package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_EmptyString() {
 assertNull(LocaleUtils.toLocale(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_Null() {
 LocaleUtils.toLocale(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_OneSegment() {
 try {
 LocaleUtils.toLocale("en");
 } catch (IllegalArgumentException e) {
 // pass
 }
 assertNull(e.getCause());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_TwoSegments() {
 try {
 LocaleUtils.toLocale("en_US");
 } catch (IllegalArgumentException e) {
 // pass
 }
 assertNull(e.getCause());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_ThreeSegments_InvalidCountry() {
 try {
 LocaleUtils.toLocale("en_US_InvalidCountry");
 } catch (IllegalArgumentException e) {
 // pass
 }
 assertNull(e.getCause());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_ThreeSegments_InvalidLanguage() {
 try {
 LocaleUtils.toLocale("InvalidLanguage_US");
 } catch (IllegalArgumentException e) {
 // pass
 }
 assertNull(e.getCause());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_ThreeSegments_EmptyLanguage() {
 try {
 LocaleUtils.toLocale("_US");
 } catch (IllegalArgumentException e) {
 // pass
 }
 assertNull(e.getCause());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_ThreeSegments_EmptyCountry() {
 try {
 LocaleUtils.toLocale("en_");
 } catch (IllegalArgumentException e) {
 // pass
 }
 assertNull(e.getCause());
 }

 @Test
 public void testToLocale_ValidFormat() {
 assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidFormat_FourSegments() {
 try {
 LocaleUtils.toLocale("en_US_XX_YY");
 } catch (IllegalArgumentException e) {
 // pass
 }
 assertNull(e.getCause());
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("en", "US");
 List<Locale> list = LocaleUtils.localeLookupList(locale);
 assertTrue(list.contains(locale));
 assertEquals(3, list.size());
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
 List<Locale> list = LocaleUtils.localeLookupList(null);
 assertTrue(list.isEmpty());
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 Locale defaultLocale = Locale.getDefault();
 List<Locale> list = LocaleUtils.localeLookupList(defaultLocale);
 assertTrue(list.contains(defaultLocale));
 assertEquals(4, list.size());
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> list = LocaleUtils.availableLocaleList();
 assertFalse(list.isEmpty());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> set = LocaleUtils.availableLocaleSet();
 assertFalse(set.isEmpty());
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_NullLocale() {
 assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);
 assertFalse(languages.isEmpty());
 String[] expected = { "en", "es", "fr", "ja" };
 assertNotNull(Arrays.toString(LocaleUtils.availableLocaleList().toArray(new Locale[0])));
 List<Locale> availableLocales = Arrays.asList(LocaleUtils.availableLocaleList().toArray(new Locale[0]));
 for (Locale locale : languages) {
 assertTrue(availableLocales.contains(locale));
 }
 assertEquals(4, languages.size());
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);
 assertFalse(countries.isEmpty());
 String[] expected = { "US", "CA", "GB" };
 assertNotNull(Arrays.toString(LocaleUtils.availableLocaleList().toArray(new Locale[0])));
 List<Locale> availableLocales = Arrays.asList(LocaleUtils.availableLocaleList().toArray(new Locale[0]));
 for (Locale locale : countries) {
 assertTrue(availableLocales.contains(locale));
 }
 assertEquals(3, countries.size());
 }

 @Test
 public void testLocaleLookupList_LocaleWithVariant() {
 Locale locale = new Locale("es", "ES", "Castilian");
 List<Locale> list = LocaleUtils.localeLookupList(locale);
 assertTrue(list.contains(locale));
 assertEquals(5, list.size());
 }

 @Test
 public void testLocaleLookupList_LocaleWithDefaultVariant() {
 Locale locale = new Locale("es", "ES");
 List<Locale> list = LocaleUtils.localeLookupList(locale);
 assertTrue(list.contains(locale));
 assertEquals(4, list.size());
 }
}