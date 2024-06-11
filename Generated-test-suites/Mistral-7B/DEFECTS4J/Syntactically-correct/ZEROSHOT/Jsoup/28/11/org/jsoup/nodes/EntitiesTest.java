import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.*;
import static org.junit.Assert.*;

public class EntitiesTest {

 private static Map<String, Character> full;
 private static Map<Character, String> xhtmlByVal;
 private static Map<Character, String> base;
 private static Map<Character, String> baseByVal;
 private static Map<Character, String> fullByVal;

 @BeforeClass
 public static void setup() {
 full = Entities.full;
 xhtmlByVal = Entities.xhtmlByVal;
 base = Entities.base;
 baseByVal = Entities.baseByVal;
 fullByVal = Entities.fullByVal;
 }

 @Test
 public void testIsNamedEntity() {
 // Test isNamedEntity method
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("amp"));
 assertTrue(Entities.isNamedEntity("quot"));
 assertTrue(Entities.isNamedEntity("apos"));
 assertTrue(Entities.isNamedEntity("gt"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test getCharacterByName method
 assertEquals( '<', Entities.getCharacterByName("lt"));
 assertEquals( '&', Entities.getCharacterByName("amp"));
 assertEquals( '\'', Entities.getCharacterByName("apos"));
 assertEquals( '>', Entities.getCharacterByName("gt"));
 assertEquals( '\u00022', Entities.getCharacterByName("quot"));
 }

 @Test
 public void testEscape() {
 // Test escape method with default CharsetEncoder and base EscapeMode
 String input = "Test < & > ' \"";
 String expectedOutput = "Test &lt; &amp; &gt; &apos; &quot;";
 String output = Entities.escape(input, null, Entities.EscapeMode.base);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEscapeWithCustomCharsetEncoder() {
 // Test escape method with custom CharsetEncoder and extended EscapeMode
 CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
 String input = "Test < & > ' \"";
 String expectedOutput = "Test &#x3C; &#x26; &#x3E; &#x27; &#x22;";
 String output = Entities.escape(input, encoder, Entities.EscapeMode.extended);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescape() {
 // Test unescape method with strict unescaping
 String input = "Test &lt; &amp; &gt; &apos; &quot;";
 String expectedOutput = "Test < & > ' \"";
 String output = Entities.unescape(input, true);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescape() {
 // Test unescape method without strict unescaping
 String input = "Test &lt; &amp; &gt; &apos; &quot;";
 String expectedOutput = "Test < & > ' \"";
 String output = Entities.unescape(input);
 assertEquals(expectedOutput, output);
 }

}