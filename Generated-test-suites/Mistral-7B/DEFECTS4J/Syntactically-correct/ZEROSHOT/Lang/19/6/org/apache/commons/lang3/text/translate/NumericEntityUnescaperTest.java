import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslateNumericEntityWithHexValue() {
 CharSequence input = "&#[xCA]FE;";
 int actualLength = 0;
 try (Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 actualLength++;
 System.out.print(ch);
 }

 @Override
 public void close() throws IOException {
 // close writer
 }
 }) {
 int result = unescaper.translate(input, 0, writer);
 assertEquals("Expected 3 characters to be written: FE", 3, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithDecimalValue() {
 CharSequence input = "&#[1234];";
 int actualLength = 0;
 try (Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 actualLength++;
 System.out.print(ch);
 }

 @Override
 public void close() throws IOException {
 // close writer
 }
 }) {
 int result = unescaper.translate(input, 0, writer);
 assertEquals("Expected 1 character to be written: 1234", 1, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithHexValueOutsideBounds() {
 CharSequence input = "&#[xCA]FFFELSE;";
 int actualLength = 0;
 try (Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 actualLength++;
 System.out.print(ch);
 }

 @Override
 public void close() throws IOException {
 // close writer
 }
 }) {
 int result = unescaper.translate(input, 0, writer);
 assertEquals("Expected 3 characters to be written: CA FE", 3, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithInvalidHexValue() {
 CharSequence input = "&#[xCA]AB;";
 int actualLength = 0;
 try (Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 actualLength++;
 System.out.print(ch);
 }

 @Override
 public void close() throws IOException {
 // close writer
 }
 }) {
 int result = unescaper.translate(input, 0, writer);
 assertEquals("Expected 0 characters to be written", 0, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithInvalidDecimalValue() {
 CharSequence input = "&#[12345];";
 int actualLength = 0;
 try (Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 actualLength++;
 System.out.print(ch);
 }

 @Override
 public void close() throws IOException {
 // close writer
 }
 }) {
 int result = unescaper.translate(input, 0, writer);
 assertEquals("Expected 1 character to be written: 12345", 1, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithInvalidDecimalValueOutsideBounds() {
 CharSequence input = "&#[12345678];";
 int actualLength = 0;
 try (Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 actualLength++;
 System.out.print(ch);
 }

 @Override
 public void close() throws IOException {
 // close writer
 }
 }) {
 int result = unescaper.translate(input, 0, writer);
 assertEquals("Expected 1 character to be written: 12345", 1, actualLength);
 }
 }

 @Test
 public void testTranslateEmptySequence() {
 int actualLength = 0;
 try (Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // do nothing
 }

 @Override
 public void close() throws IOException {
 // close writer
 }
 }) {
 int result = unescaper.translate(CharSequence.class.newInstance(), 0, writer);
 assertEquals("Expected 0 characters to be written", 0, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithInvalidCharacterSequence() {
 CharSequence input = "&%$#";
 int actualLength = 0;
 try (Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 actualLength++;
 System.out.print(ch);
 }

 @Override
 public void close() throws IOException {
 // close writer
 }
 }) {
 int result = unescaper.translate(input, 0, writer);
 assertEquals("Expected 0 characters to be written", 0, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithInvalidCharacterSequenceWithSemicolon() {
 CharSequence input = "&%$#;";
 int actualLength = 0;
 try (Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 actualLength++;
 System.out.print(ch);
 }

 @Override
 public void close() throws IOException {
 // close writer
 }
 }) {
 int result = unescaper.translate(input, 0, writer);
 assertEquals("Expected 0 characters to be written", 0, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithInvalidCharacterSequenceWithSemicolonInside() {
 CharSequence input = "&%$#;%";
 int actualLength = 0;
 try (Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 actualLength++;
 System.out.print(ch);
 }

 @Override
 public void close() throws IOException {
 // close writer
 }
 }) {
 int result = unescaper.translate(input, 0, writer);
 assertEquals("Expected 0 characters to be written", 0, actualLength);
 }
 }

 @Test
 public void testTranslateNumericEntityWithMultipleEntities() {
 CharSequence input = "abc&#[123]def&#[456]ghi";
 int actualLength = 0;
 try (Writer writer = new Writer() {
 @Override
 public void write(int c) throws IOException {
 char ch = (char) c;
 actualLength++;
 System.out.print(ch);
 }

 @Override
 public void close() throws IOException {
 // close writer
 }
 }) {
 int result = unescaper.translate(input, 0, writer);
 assertEquals("Expected 6 characters to be written: 123 456", 6, actualLength);
 }
 }
}