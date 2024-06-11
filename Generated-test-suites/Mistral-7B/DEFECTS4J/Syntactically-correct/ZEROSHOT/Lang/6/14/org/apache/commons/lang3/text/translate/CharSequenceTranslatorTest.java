import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharacterEscapes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CharSequenceTranslatorTest {

 private final CharSequenceTranslator translator = new CharacterEscapes();

 @Test
 public void testTranslateCharSequence() {
 char sequence[] = {'a', 'b', 'c', '\\u00E9'};
 String result = translator.translate(new String(sequence));
 assertEquals("a b c \u00E9", result);
 }

 @Test
 public void testTranslateWriter() throws IOException {
 StringWriter writer = new StringWriter();
 char sequence[] = {'a', 'b', 'c', '\\u00E9'};
 translator.translate(new String(sequence), writer);
 String result = writer.toString();
 assertEquals("a b c \u00E9", result);
 }

 @Test
 public void testTranslate() throws IOException {
 CharSequence input = "abc\\u00E9";
 StringWriter writer = new StringWriter();
 int index = 0;
 int consumed = 0;
 while (consumed < input.length()) {
 consumed += translator.translate(input, index, writer);
 index += consumed;
 }
 String result = writer.toString();
 assertEquals("abc\u00E9", result);
 }

 @Test
 public void testTranslateWith() {
 CharSequenceTranslator translator1 = new CharacterEscapes();
 CharSequenceTranslator translator2 = new UpperCaseTranslator();
 CharSequenceTranslator translator3 = new LowerCaseTranslator();
 CharSequenceTranslator compositeTranslator = translator1.with(translator2, translator3);
 String input = "AbCdEf";
 String result = compositeTranslator.translate(input);
 assertEquals("ABCDef", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNullWriter() {
 CharSequence sequence = "test";
 CharSequenceTranslator translator = new CharacterEscapes();
 try {
 translator.translate(sequence, null);
 } catch (IllegalArgumentException e) {
 assertEquals("The Writer must not be null", e.getMessage());
 }
 }

 @Test
 public void testTranslateWithNullInput() {
 CharSequenceTranslator translator = new CharacterEscapes();
 String result = translator.translate(null);
 assertNull(result);
 }
}

class UpperCaseTranslator extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence cs, int start, Writer out) throws IOException {
 int len = cs.length();
 for (int i = start; i < len; i++) {
 char c = cs.charAt(i);
 out.write(Character.toUpperCase(c));
 }
 return len - start;
 }
}

class LowerCaseTranslator extends CharSequenceTranslator {
 @Override
 public int translate(CharSequence cs, int start, Writer out) throws IOException {
 int len = cs.length();
 for (int i = start; i < len; i++) {
 char c = cs.charAt(i);
 out.write(Character.toLowerCase(c));
 }
 return len - start;
 }
}