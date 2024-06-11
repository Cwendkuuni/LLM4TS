package org.apache.commons.lang3.text;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class ExtendedMessageFormatTest {

 @Test
 public void testSimpleMessageFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
 format.applyPattern("Hello {0}, your age is {1}.");
 assertEquals("Hello {0}, your age is {1}", format.toPattern());

 String message = format.format(new Object[]{"John", 25});
 assertEquals("Hello John, your age is 25", message);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
 format.applyPattern("Hello {0}, your age is {1}.");
 try {
 format.format(new Object[]{"John", 25, "invalidIndex"});
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCustomFormat() {
 FormatFactory dateFormatFactory = new SimpleDateFormat("yyyy-MM-dd");
 ExtendedMessageFormat format = new ExtendedMessageFormat("Today is {0}, and tomorrow will be {1}", Collections.singletonMap("date", dateFormatFactory));
 format.applyPattern("Today is {0}, and tomorrow will be {1}");

 String message = format.format(new Object[]{"2023-03-15", "2023-03-16"});
 assertEquals("Today is 2023-03-15, and tomorrow will be 2023-03-16", message);
 }

 @Test
 public void testInvalidCustomFormatDescription() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Today is {0}, and tomorrow will be {1}", null);
 try {
 format.applyPattern("Today is {0}, and tomorrow will be {1}, invalidFormat");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormatFactory() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Today is {0}, and tomorrow will be {1}", Collections.singletonMap("date", null));
 try {
 format.applyPattern("Today is {0}, and tomorrow will be {1}");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEmptyLocale() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}", null);
 format.applyPattern("Hello {0}, your age is {1}");
 String message = format.format(new Object[]{"John", 25}, Locale.EMPTY);
 assertEquals("Hello John, your age is 25", message);
 }

 @Test
 public void testSetLocale() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}", null);
 format.applyPattern("Hello {0}, your age is {1}");

 Locale localeFR = new Locale("fr", "FR");
 format.applyPattern("Hello {0}, votre age est {1}", Collections.singletonMap("date", new SimpleDateFormat("yyyy-MM-dd", localeFR)));
 String message = format.format(new Object[]{"John", 25}, localeFR);
 assertEquals("Hello John, votre age est 25", message);
 }

 @Test
 public void testFormatFactoryNullArgument() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}", null);
 try {
 format.applyPattern("Hello {0}, your age is {1}", new HashMap<String, FormatFactory>(){{
 put("date", new SimpleDateFormat("yyyy-MM-dd", null));
 }} );
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormatFactoryInvalidArgument() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}", null);
 try {
 format.applyPattern("Hello {0}, your age is {1}", new HashMap<String, FormatFactory>(){{
 put("date", new SimpleDateFormat("yyyy-MM-dd", null));
 put("invalidFormat", new Object(){{}});
 }} );
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormatFactoryMissingArgument() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}", null);
 try {
 format.applyPattern("Hello {0}, your age is {1}", new HashMap<String, FormatFactory>(){{
 put("date", new SimpleDateFormat("yyyy-MM-dd", Locale.US));
 }} );
 fail();
 } catch (ParseException e) {
 // expected
 }
 }
}