import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

// Test case for toLocale method
@Test
public void testToLocale() {
Locale expectedLocale = new Locale("en", "US");
Locale actualLocale = LocaleUtils.toLocale("en_US");
assertEquals(expectedLocale, actualLocale);

Locale expectedLocale2 = new Locale("fr", "CA");
Locale actualLocale2 = LocaleUtils.toLocale("fr_CA");
assertEquals(expectedLocale2, actualLocale2);

// Test case for invalid input
try {
Locale actualLocale3 = LocaleUtils.toLocale(null);
fail("Expected IllegalArgumentException but was null");
} catch (IllegalArgumentException e) {}

try {
Locale actualLocale4 = LocaleUtils.toLocale("en_");
fail("Expected IllegalArgumentException but was en_");
} catch (IllegalArgumentException e) {}

try {
Locale actualLocale5 = LocaleUtils.toLocale("en_US_");
fail("Expected IllegalArgumentException but was en_US_");
} catch (IllegalArgumentException e) {}

try {
Locale actualLocale6 = LocaleUtils.toLocale("en_US_varient");
fail("Expected IllegalArgumentException but was en_US_variant");
} catch (IllegalArgumentException e) {}
}

// Test case for localeLookupList method
@Test
public void testLocaleLookupList() {
Locale expectedLocale = new Locale("en", "US");
Locale defaultLocale = new Locale("es", "ES");

List<Locale> localeList = LocaleUtils.localeLookupList(expectedLocale);
assertEquals(3, localeList.size());
assertTrue(localeList.contains(expectedLocale));
assertTrue(localeList.contains(new Locale("en", "US")));
assertTrue(localeList.contains(new Locale("en", "")));

localeList = LocaleUtils.localeLookupList(null, defaultLocale);
assertEquals(5, localeList.size());
assertTrue(localeList.contains(defaultLocale));
}

// Test case for availableLocaleList method
@Test
public void testAvailableLocaleList() {
List<Locale> localeList = LocaleUtils.availableLocaleList();
assertEquals(true, localeList.contains(new Locale("en", "US")));
assertEquals(true, localeList.contains(new Locale("fr", "FR")));
assertEquals(true, localeList.contains(new Locale("es", "ES")));
assertEquals(true, localeList.contains(new Locale("ja", "JP")));
assertEquals(true, localeList.contains(new Locale("de", "DE")));
}

// Test case for isAvailableLocale method
@Test
public void testIsAvailableLocale() {
Locale locale = new Locale("en", "US");
assertTrue(LocaleUtils.isAvailableLocale(locale));

Locale locale2 = new Locale("en", "US_variant");
assertFalse(LocaleUtils.isAvailableLocale(locale2));
}

// Test case for languagesByCountry method
@Test
public void testLanguagesByCountry() {
String countryCode = "US";
List<Locale> localeList = LocaleUtils.languagesByCountry(countryCode);
assertEquals(true, localeList.contains(new Locale("en", "US")));
assertEquals(true, localeList.contains(new Locale("en", "CA")));
assertEquals(true, localeList.contains(new Locale("en", "GB")));
}

// Test case for countriesByLanguage method
@Test
public void testCountriesByLanguage() {
String languageCode = "en";
List<Locale> localeList = LocaleUtils.countriesByLanguage(languageCode);
assertEquals(true, localeList.contains(new Locale("US", "en")));
assertEquals(true, localeList.contains(new Locale("CA", "en")));
assertEquals(true, localeList.contains(new Locale("GB", "en")));
}
}