package org.apache.commons.lang3.test;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.Validate;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.text.*;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExtendedMessageFormatTest {

 private ExtendedMessageFormat format;
 private FormatFactory dummyFormatFactory;

 @BeforeAll
 public void setUp() {
 dummyFormatFactory = new DummyFormatFactory();
 format = new ExtendedMessageFormat("{0, date, 'dd/MM/yyyy'}");
 }

 @Test
 public void testSimpleFormat() {
 String formatted = format.format(new Date());
 assertEquals("14/06/2023", formatted);
 }

 @Test
 public void testFormatWithCustomPattern() {
 format = new ExtendedMessageFormat("{0, date, 'dd/MM/yyyy', dummy}");
 format.setRegistry(Map.of("dummy", dummyFormatFactory));
 formatted = format.format(new Date());
 assertEquals("14/06/2023", formatted);
 }

 @Test
 public void testInvalidFormat() {
 assertThrows(IllegalArgumentException.class, () -> format.applyPattern("invalidPattern"));
 }

 @Test
 public void testInvalidArgumentIndex() {
 assertThrows(IllegalArgumentException.class, () -> format.applyPattern("-1, date, 'dd/MM/yyyy'"));
 }

 @Test
 public void testMissingFormatFactory() {
 format = new ExtendedMessageFormat("{0, unknown, 'dd/MM/yyyy'}");
 assertThrows(IllegalArgumentException.class, () -> format.applyPattern(""));
 }

 @Test
 public void testMissingFormatFactoryWithCustomPattern() {
 format = new ExtendedMessageFormat("{0, unknown, 'dd/MM/yyyy', dummy}");
 format.setRegistry(Collections.emptyMap());
 assertThrows(IllegalArgumentException.class, () -> format.applyPattern(""));
 }

 @Test
 public void testFormatWithInvalidFormatDescription() {
 format = new ExtendedMessageFormat("{0, date, 'dd/MM/yyyy, invalid'}");
 assertThrows(IllegalArgumentException.class, () -> format.applyPattern(""));
 }

 @Test
 public void testFormatWithMissingArgument() {
 format = new ExtendedMessageFormat("{0, date, 'dd/MM/yyyy'}");
 assertThrows(MissingArgumentException.class, () -> format.format(new Date(), 2));
 }

 @Test
 public void testFormatWithExtraArgument() {
 format = new ExtendedMessageFormat("{0, date, 'dd/MM/yyyy'}");
 assertThrows(IllegalArgumentException.class, () -> format.format(new Date(), 2, new Object[]{1, 2}));
 }

 @Test
 public void testFormatWithFormatFactoryNotFound() {
 format = new ExtendedMessageFormat("{0, unknown, 'dd/MM/yyyy'}");
 format.setRegistry(Map.of("dummy", dummyFormatFactory));
 assertThrows(IllegalArgumentException.class, () -> format.format(new Object[]{0, "unknown"}));
 }

 @Test
 public void testFormatWithFormatFactoryNullFormat() {
 format = new ExtendedMessageFormat("{0, unknown, 'dd/MM/yyyy'}");
 format.setRegistry(Map.of("dummy", new DummyFormatFactory() {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 return null;
 }
 }));
 assertThrows(IllegalArgumentException.class, () -> format.format(new Object[]{0, "unknown"}));
 }

 @AfterAll
 public void tearDown() {
 format = null;
 dummyFormatFactory = null;
 }
}

class DummyFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 if (name.equals("dummy")) {
 return new SimpleDateFormat("dd/MM/yyyy");
 }
 return null;
 }
}