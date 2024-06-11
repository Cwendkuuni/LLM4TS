import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.CharSequenceUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 @Test
 public void testTranslateSimpleNumericEntity() throws Exception {
 String input = "&amp; &#65; &#66; &#128; &#xFF; &#x41; &#x1F4;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char cChar = (char) c;
 System.out.print(cChar);
 }

 @Override
 public void write(char[] c, int off, int len) throws IOException {
 for (int i = off; i < off + len; i++) {
 System.out.print(c[i]);
 }
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, outputWriter);
 assertEquals("&amp; A B &#128; F x &#x41; ð,", input.substring(0, input.length() - length));
 }

 @Test(expected = IOException.class)
 public void testTranslateMalformedNumericEntity() throws Exception {
 String input = "&amp; #xabcd; &#x";
 Writer outputWriter = new Writer() {}; // Empty writer to force exception
 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, outputWriter);
 }

 @Test
 public void testTranslateComplexNumericEntity() throws Exception {
 String input = "&amp; #xABCD; &#xFFFF; &#x10FFFF; &#x110000;";
 Writer outputWriter = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char cChar = (char) c;
 System.out.print(cChar);
 }

 @Override
 public void write(char[] c, int off, int len) throws IOException {
 for (int i = off; i < off + len; i++) {
 System.out.print(c[i]);
 }
 }
 };

 NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
 int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, outputWriter);
 assertEquals("&amp; &#xABCD; FF FF surrogate-pair FF FF surrogate-pair FF FF surrogate-pair FF FF surrogate-pair FF FF surrogate-pair", input.substring(0, input.length() - length));
 }

}