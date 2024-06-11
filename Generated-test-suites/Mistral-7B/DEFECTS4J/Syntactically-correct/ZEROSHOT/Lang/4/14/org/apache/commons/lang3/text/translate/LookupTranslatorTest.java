import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

 private CharSequenceTranslator translator;

 @Test
 public void testEmptyConstructor() {
 translator = new LookupTranslator(null);
 assertNull(translator.getLookupMap());
 }

 @Test
 public void testNullLookup() {
 CharSequence[][] lookup = null;
 translator = new LookupTranslator(lookup);
 assertNull(translator.getLookupMap());
 }

 @Test
 public void testEmptyLookup() {
 CharSequence[][] lookup = new CharSequence[0][2];
 translator = new LookupTranslator(lookup);
 assertTrue(translator.getLookupMap().isEmpty());
 }

 @Test
 public void testLookupTranslatorWithValidLookupTable() {
 CharSequence[][] lookup = new CharSequence[][] {new CharSequence[] {"abc", "def"}, new CharSequence[] {"xyz", "pqr"}};
 translator = new LookupTranslator(lookup);
 HashMap<CharSequence, CharSequence> expectedMap = new HashMap<>();
 expectedMap.put(CharSequence.valueOf("abc"), CharSequence.valueOf("def"));
 expectedMap.put(CharSequence.valueOf("xyz"), CharSequence.valueOf("pqr"));
 assertEquals(expectedMap, translator.getLookupMap());
 }

 @Test
 public void testTranslateWithMatchingSubsequence() throws IOException {
 CharSequence[][] lookup = new CharSequence[][] {new CharSequence[] {"abc", "def"}, new CharSequence[] {"xyz", "pqr"}};
 translator = new LookupTranslator(lookup);
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // Do nothing
 }

 @Override
 public void write(String s, int off, int len) throws IOException {
 assertEquals("def", s.substring(off, off + len));
 }
 };
 String input = "abc123defxyz";
 int index = 1;
 int length = 3;
 int expectedOutputLength = 3;
 translator.translate(CharSequence.valueOf(input), index, out);
 assertEquals(expectedOutputLength, out.size());
 }

 @Test
 public void testTranslateWithSubsequenceNotPresent() throws IOException {
 CharSequence[][] lookup = new CharSequence[][] {new CharSequence[] {"abc", "def"}, new CharSequence[] {"xyz", "pqr"}};
 translator = new LookupTranslator(lookup);
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // Do nothing
 }

 @Override
 public void write(String s, int off, int len) throws IOException {
 fail();
 }
 };
 String input = "abc123defxyz";
 int index = 1;
 int length = 4;
 translator.translate(CharSequence.valueOf(input), index, out);
 assertTrue(out.size() == 0);
 }

 @Test
 public void testTranslateWithSubsequenceLongerThanInput() throws IOException {
 CharSequence[][] lookup = new CharSequence[][] {new CharSequence[] {"abc", "def"}, new CharSequence[] {"xyz", "pqr"}};
 translator = new LookupTranslator(lookup);
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // Do nothing
 }

 @Override
 public void write(String s, int off, int len) throws IOException {
 fail();
 }
 };
 String input = "abc";
 int index = 0;
 int length = 5;
 translator.translate(CharSequence.valueOf(input), index, out);
 assertTrue(out.size() == 0);
 }

 @Test
 public void testTranslateWithSubsequenceShorterThanInput() throws IOException {
 CharSequence[][] lookup = new CharSequence[][] {new CharSequence[] {"abc", "def"}, new CharSequence[] {"xyz", "pqr"}};
 translator = new LookupTranslator(lookup);
 Writer out = new Writer() {
 @Override
 public void write(int c) throws IOException {
 // Do nothing
 }

 @Override
 public void write(String s, int off, int len) throws IOException {
 fail();
 }
 };
 String input = "abcdef";
 int index = 3;
 int length = 3;
 translator.translate(CharSequence.valueOf(input), index, out);
 assertTrue(out.size() == 0);
 }
}