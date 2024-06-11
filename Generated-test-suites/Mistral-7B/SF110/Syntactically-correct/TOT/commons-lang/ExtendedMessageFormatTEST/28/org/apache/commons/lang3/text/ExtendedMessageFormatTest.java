package org.apache.commons.lang3.test;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 private ExtendedMessageFormat messageFormat;
 private FormatFactory dummyFormatFactory;

 @Before
 public void setUp() {
 dummyFormatFactory = new DummyFormatFactory();
 messageFormat = new ExtendedMessageFormat("{0}, {1}, {2}", new HashMap<String, FormatFactory>() {{
 put("dummy", dummyFormatFactory);
 }});
 }

 @Test
 public void testEmptyPattern() {
 String formattedMessage = messageFormat.format(new Object[]{"", "", ""});
 assertEquals("", formattedMessage);
 }

 @Test
 public void testSimpleFormat() {
 messageFormat = new ExtendedMessageFormat("{0}, {1}, {2}", Locale.US);
 String formattedMessage = messageFormat.format(new Object[]{"apple", "banana", "cherry"});
 assertEquals("apple, banana, cherry", formattedMessage);
 }

 @Test
 public void testCustomFormat() {
 messageFormat = new ExtendedMessageFormat("{0}, {{dummy,date,long}}, {2}", Locale.US, new HashMap<String, FormatFactory>() {{
 put("dummy", dummyFormatFactory);
 }});
 String dateFormatPattern = "yyyy-MM-dd";
 dummyFormatFactory.registerFormat("date", new SimpleDateFormat(dateFormatPattern));
 Object[] args = {"apple", new Date(), "cherry"};
 String formattedMessage = messageFormat.format(args);
 assertEquals("apple, 2022-12-31, cherry", formattedMessage);
 }

 @Test
 public void testInvalidFormatArgumentIndex() {
 try {
 messageFormat = new ExtendedMessageFormat("{0}, {3}, {2}", Locale.US);
 messageFormat.applyPattern("{0}, {3}, {2}");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testUnrecognizedFormatElement() {
 try {
 messageFormat = new ExtendedMessageFormat("{0}, {foo}, {2}", Locale.US);
 messageFormat.applyPattern("{0}, {foo}, {2}");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFormatWithEscapedQuote() {
 messageFormat = new ExtendedMessageFormat("{0}, {1}, {{1}}", Locale.US);
 String formattedMessage = messageFormat.format(new Object[]{"John Doe's", "name", "is 'Doe'"});
 assertEquals("John Doe's, name, 'Doe''s", formattedMessage);
 }

 @Test
 public void testFormatWithNestedFormats() {
 messageFormat = new ExtendedMessageFormat("{0}, {{1,choice,0#No|1#Yes}}, {2}", Locale.US);
 messageFormat.applyPattern("{0}, {1,choice,0#No|1#Yes}, {2}");
 Object[] args = {"John", 1, "Doe"};
 String formattedMessage = messageFormat.format(args);
 assertEquals("John, Yes, Doe", formattedMessage);
 }

 @Test(expected = ParseException.class)
 public void testInvalidDateFormat() {
 messageFormat = new ExtendedMessageFormat("{0}, {{dummy,date,foo}}, {2}", Locale.US, new HashMap<String, FormatFactory>() {{
 put("dummy", dummyFormatFactory);
 }});
 dummyFormatFactory.registerFormat("date", new SimpleDateFormat("yyyy-MM-ddd"));
 Object[] args = {"apple", new Date(), "cherry"};
 messageFormat.applyPattern("{0}, {{dummy,date,foo}}, {2}");
}

}

class DummyFormatFactory implements FormatFactory {

 private static final long serialVersionUID = -7401463232519228792L;

 @Override
 public Format getFormat(String name, String pattern, Locale locale) {
 switch (name) {
 case "date":
 return new SimpleDateFormat(pattern);
 default:
 return null;
 }
 }

}