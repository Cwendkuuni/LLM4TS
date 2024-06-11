import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

 private static final String TEST_INPUT = "&amp;&#160;&#x1F4;,&#032;&#064;&#x03B4;&#128;,&#x2202;&#x2212;&#x2211;,&#8203;&#8203;&#033;,&#x2713;&#x2713;&#x2713;,&#x27E8;&#x27E8;&#x27E8;";

 @Test
 public void testTranslateValidEntities() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String output = "";
 try (StringWriter sw = new StringWriter()) {
 Writer writer = new PrintWriter(sw);
 for (int i = 0; i < TEST_INPUT.length(); ++i) {
 int len = unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return TEST_INPUT.length();
 }

 @Override
 public char charAt(int index) {
 return TEST_INPUT.charAt(index);
 }
 }, i, writer);
 output += sw.toString().substring(0, len);
 }
 }
 assertEquals("&amp;&nbsp;&#xA0;&#x1F4;,&#32;&#40;&#x3B4;&#224;,&#x2202;&#x2212;&#x2211;,&#224;&#224;&#35;,&#x2713;&#x2713;&#x2713;,&#247;&#247;&#247;,&#254;&#254;&#254;", output);
 }

 @Test
 public void testTranslateInvalidEntities() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
 String input = "&abc&def";
 String output = unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return input.length();
 }

 @Override
 public char charAt(int index) {
 return input.charAt(index);
 }
 }, 0, new StringWriter()).toString();
 assertEquals("", output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateMissingSemiColon() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
 String input = "&abc";
 String output = unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return input.length();
 }

 @Override
 public char charAt(int index) {
 return input.charAt(index);
 }
 }, 0, new StringWriter()).toString();
 fail("Expected IllegalArgumentException for missing semicolon");
 }

 @Test
 public void testTranslateValidEntitiesWithHex() throws Exception {
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
 String output = "";
 try (StringWriter sw = new StringWriter()) {
 Writer writer = new PrintWriter(sw);
 for (int i = 0; i < TEST_INPUT.length(); ++i) {
 int len = unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return TEST_INPUT.length();
 }

 @Override
 public char charAt(int index) {
 return TEST_INPUT.charAt(index);
 }
 }, i, writer);
 output += sw.toString().substring(0, len);
 }
 }
 char hexChar = 0xFFFD;
 String hexInput = "&#x" + Integer.toHexString(hexChar) + ";";
 len = unescaper.translate(new CharSequence() {
 @Override
 public int length() {
 return hexInput.length();
 }

 @Override
 public char charAt(int index) {
 return hexInput.charAt(index);
 }
 }, 0, new StringWriter()).length();
 assertEquals(2 + (hexInput.length() / 2), len);
 }
}