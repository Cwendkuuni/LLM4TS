import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper;

 @Before
 public void setUp() {
 unescaper = new NumericEntityUnescaper();
 }

 @Test
 public void testTranslateNumericEntityWithDecimal() {
 CharSequence input = "&num16;";
 int expectedLength = 1;
 int actualLength;

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 assertEquals((char) c, (char) '£', 0); // Assert that the correct character was written
 }

 @Override
 public void write(char[] cbuf, int off, int len) throws IOException {
 actualLength = len;
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 }) {
 actualLength = unescaper.translate(input, 0, out);
 assertEquals(expectedLength, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithHex() {
 CharSequence input = "&#x00A3;";
 int expectedLength = 1;
 int actualLength;

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 assertEquals((char) 171, c, 0); // Assert that the correct character was written
 }

 @Override
 public void write(char[] cbuf, int off, int len) throws IOException {
 actualLength = len;
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 }) {
 actualLength = unescaper.translate(input, 0, out);
 assertEquals(expectedLength, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithDecimalAndFollowingCharacter() {
 CharSequence input = "&num16;a";
 int expectedLength = 2;
 int actualLength;

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 assertEquals((char) '£', c, 0); // Assert that the correct character was written
 }

 @Override
 public void write(char[] cbuf, int off, int len) throws IOException {
 actualLength = len;
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 }) {
 actualLength = unescaper.translate(input, 0, out);
 assertEquals(expectedLength, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithHexAndFollowingCharacter() {
 CharSequence input = "&#x00A3;a";
 int expectedLength = 2;
 int actualLength;

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 assertEquals((char) 171, c, 0); // Assert that the correct character was written
 }

 @Override
 public void write(char[] cbuf, int off, int len) throws IOException {
 actualLength = len;
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 }) {
 actualLength = unescaper.translate(input, 0, out);
 assertEquals(expectedLength, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithInvalidDecimal() {
 CharSequence input = "&numa6;";
 int expectedLength = 0;
 int actualLength;

 try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 // Do nothing
 }

 @Override
 public void write(char[] cbuf, int off, int len) throws IOException {
 actualLength = len;
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 }) {
 actualLength = unescaper.translate(input, 0, out);
 assertEquals(expectedLength, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithInvalidHex() {
 CharSequence input = "&#xFoo;";
 int expectedLength = 0;
 int actualLength;
try (Writer out = new Writer() {
 @Override
 public void write(int c, int length) throws IOException {
 // Do nothing
 }

 @Override
 public void write(char[] cbuf, int off, int len) throws IOException {
 actualLength = len;
 }

 @Override
 public void close() throws IOException {
 // Do nothing
 }
 }) {
 actualLength = unescaper.translate(input, 0, out);
 assertEquals(expectedLength, actualLength);
 }
 }
}