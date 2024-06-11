package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_NullInput() {
 assertNull(LocaleUtils.toLocale(null));
 }

 @Test
 public void testToLocale_EmptyString() {
 assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
 }

 @Test
 public void testToLocale_InvalidFormat_NoLanguage() {
 assertNull(LocaleUtils.toLocale("_"));
 assertNull(LocaleUtils.toLocale("_FR"));
 assertNull(LocaleUtils.toLocale("en_"));
 }

 @Test
 public void testToLocale_InvalidFormat_NoCountry() {
 assertNull(LocaleUtils.toLocale("en"));
 assertNull(LocaleUtils.toLocale("en_"));
 assertNull(LocaleUtils.toLocale("en_FR"));
 assertNull(LocaleUtils.toLocale("en_FR_"));
 }

 @Test
 public void testToLocale_InvalidFormat_MissingCountry() {
 assertNull(LocaleUtils.toLocale("en_fr"));
 assertNull(LocaleUtils.toLocale("en_fr_"));
 }

 @Test
 public void testToLocale_ValidFormat() {
 assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
 assertEquals(Locale.FRANCE, Locale.FRANCE);
 assertEquals(Locale.GERMANY, Locale.GERMANY);
 assertEquals(Locale.ITALY, Locale.ITALY);
 }

 @Test
 public void testToLocale_InvalidFormat_ExtraCharacters() {
 assertNull(LocaleUtils.toLocale("en_US#"));
 assertNull(LocaleUtils.toLocale("en_US_#"));
 assertNull(LocaleUtils.toLocale("en_US_#fr"));
 }

 @Test
 public void testToLocale_InvalidFormat_ExtraHyphens() {
 assertNull(LocaleUtils.toLocale("en--US"));
 assertNull(LocaleUtils.toLocale("en-US--"));
 assertNull(LocaleUtils.toLocale("en--US--fr"));
 }

 @Test
 public void testLocaleLookupList_NullInput() {
 assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
 }

 @Test
 public void testLocaleLookupList_ValidInput() {
 List<Locale> expectedList = Arrays.asList(Locale.US, new Locale("fr", "CA"), Locale.FRANCE, new Locale("es", "ES"), Locale.ITALY);
 assertEquals(expectedList, LocaleUtils.localeLookupList(Locale.US));
 }

 @Test
 public void testLocaleLookupList_ValidInput_WithDefaultLocale() {
 List<Locale> expectedList = Arrays.asList(Locale.US, new Locale("fr", "CA"), Locale.FRANCE, new Locale("es", "ES"), Locale.ITALY, Locale.getDefault());
 assertEquals(expectedList, LocaleUtils.localeLookupList(Locale.US, Locale.getDefault()));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedList = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
 assertEquals(expectedList, LocaleUtils.availableLocaleList());
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expectedSet = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
 assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
 }

 @Test
 public void testIsAvailableLocale_NullInput() {
 assertFalse(LocaleUtils.isAvailableLocale(null));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
 assertFalse(LocaleUtils.isAvailableLocale(new Locale("Invalid", "Invalid")));
 }

 @Test
 public void testIsAvailableLocale_ValidLocale() {
 assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
 assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
 assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMANY));
 assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALY));
 }

 @Test
 public void testLanguagesByCountry_NullInput() {
 assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
 }

 @Test
 public void testLanguagesByCountry_EmptyCountryCode() {
 assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(""));
 }

 @Test
 public void testLanguagesByCountry_ValidCountryCode() {
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "CA"), new Locale("en", "AU"));
 assertEquals(expectedList, LocaleUtils.languagesByCountry("US"));
 }

 @Test
 public void testCountriesByLanguage_NullInput() {
 assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
 }

 @Test
 public void testCountriesByLanguage_EmptyLanguageCode() {
 assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(""));
 }

 @Test
 public void testCountriesByLanguage_ValidLanguageCode() {
 List<Locale> expectedList = Arrays.asList(Locale.US, new Locale("CA", "fr"), new Locale("MX", "es"), new Locale("IT", "IT"), new Locale("ES", "ES"), new Locale("AR", "AR"));
 assertEquals(expectedList, LocaleUtils.countriesByLanguage("en"));
 }
}