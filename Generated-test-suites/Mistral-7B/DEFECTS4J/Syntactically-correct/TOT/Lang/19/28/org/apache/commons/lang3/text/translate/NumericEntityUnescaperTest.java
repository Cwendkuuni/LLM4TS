import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateSingleCharacterEntity() {
 String input = "&amp;";
 StringBuilder output = new StringBuilder();
 unescaper.translate(input, 0, output, 0, input.length());
 assertEquals("&amp;", input);
 assertEquals("&amp;", output.toString());
 }

 @Test
 public void testTranslateSingleCharacterEntityWithSemicolon() {
 String input = "&amp;#128;";
 StringBuilder output = new StringBuilder();
 unescaper.translate(input, 0, output, 0, input.length());
 assertEquals("&amp;#128;", input);
 assertEquals((char) 128, output.charAt(0));
 }

 @Test
 public void testTranslateMultipleCharacterEntity() {
 String input = "&hellip;";
 StringBuilder output = new StringBuilder();
 unescaper.translate(input, 0, output, 0, input.length());
 assertEquals("…", input);
 assertEquals("…", output.toString());
 }

 @Test
 public void testTranslateMultipleCharacterEntityWithSemicolon() {
 String input = "&hellip;";
 StringBuilder output = new StringBuilder();
 unescaper.translate(input, 0, output, 0, input.length());
 assertEquals("…", input);
 assertEquals("…", output.toString());
 }

 @Test(expected = IOException.class)
 public void testInvalidEntity() {
 String input = "&xxx;";
 StringBuilder output = new StringBuilder();
 unescaper.translate(input, 0, output, 0, input.length());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidHexEntity() {
 String input = "&xXX;";
 StringBuilder output = new StringBuilder();
 unescaper.translate(input, 0, output, 0, input.length());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidNonHexEntity() {
 String input = "&&#xAB;CD;";
 StringBuilder output = new StringBuilder();
 unescaper.translate(input, 0, output, 0, input.length());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidHexValueOutsideRange() {
 String input = "&x10FFFF;";
 StringBuilder output = new StringBuilder();
 unescaper.translate(input, 0, output, 0, input.length());
 }

 @Test
 public void testTranslateMultipleEntities() {
 String input = "&amp;&nbsp;&#x00A0;&#x1234;&#xFFFD;&#x1F000;&#x1F6FF;";
 StringBuilder output = new StringBuilder();
 unescaper.translate(input, 0, output, 0, input.length());
 assertEquals("&amp;&nbsp;&#4160;&#12343;&#65533;&#1048575;&#1114111;", input);
 assertEquals("&amp; &nbsp; &#4160; &#12343; &#65533; &#1048575; &#1114111;", output.toString());
 }

 @Test
 public void testTranslateMultipleEntitiesWithSemicolon() {
 String input = "&amp;&nbsp;&#x00A0;&#x1234;&#xFFFD;&#x1F000;&#x1F6FF;";
 StringBuilder output = new StringBuilder();
 unescaper.translate(input, 0, output, 0, input.length());
 assertEquals("&amp;&nbsp;&#4160;&#12343;&#65533;&#1048575;&#1114111;", input);
 assertEquals("&amp; &nbsp; &#4160; &#12343; &#65533; &#1048575; &#1114111;", output.toString());
 }
}