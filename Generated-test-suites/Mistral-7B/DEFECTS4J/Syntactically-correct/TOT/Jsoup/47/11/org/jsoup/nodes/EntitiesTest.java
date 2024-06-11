import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test positive case - named entity in full entity set
 String namedEntity = "amp";
 assertTrue(Entities.isNamedEntity(namedEntity));

 // Test negative case - non-named entity
 String nonNamedEntity = "xyz";
 assertFalse(Entities.isNamedEntity(nonNamedEntity));
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Test positive case - named entity in base entity set
 String baseNamedEntity = "lt";
 assertTrue(Entities.isBaseNamedEntity(baseNamedEntity));

 // Test negative case - named entity not in base entity set
 String namedEntityNotInBase = "amp";
 assertFalse(Entities.isBaseNamedEntity(namedEntityNotInBase));
 }

 @Test
 public void testGetCharacterByName() {
 // Test positive case - named entity exists in full entity set
 String namedEntity = "amp";
 Character expectedCharacter = '&';
 assertEquals(expectedCharacter, Entities.getCharacterByName(namedEntity));

 // Test negative case - named entity does not exist in full entity set
 String nonExistentNamedEntity = "xyz";
 assertNull(Entities.getCharacterByName(nonExistentNamedEntity));
 }

 @Test
 public void testEscapeMethodWithDefaultSettings() {
 // Test escape method with default settings
 String inputString = "&amp; < > \"";
 String expectedOutput = "&amp; &lt; &gt; &quot;";
 String actualOutput = Entities.escape(inputString, null); // null implies default settings
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEscapeMethodWithXHTMLSettings() {
 // Test escape method with XHTML settings
 String inputString = "&amp; < > \"";
 String expectedOutput = "&amp; &lt; &gt; &quot;";
 Document.OutputSettings xhtmlSettings = new Document.OutputSettings();
 xhtmlSettings.setEscapeMode(Entities.EscapeMode.xhtml);
 String actualOutput = Entities.escape(inputString, xhtmlSettings);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeMethodWithInvalidEscapeMode() {
 // Test exception when passing an invalid EscapeMode
 Document.OutputSettings invalidSettings = new Document.OutputSettings();
 invalidSettings.setEscapeMode("invalidMode");
 String inputString = "&amp; < > \"";
 String actualOutput = Entities.escape(inputString, invalidSettings);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testUnescapeMethod() {
 // Test unescape method with strict false
 String inputString = "&lt;helloworld&gt;";
 String expectedOutput = "<helloworld>";
 String actualOutput = Entities.unescape(inputString, false);
 assertEquals(expectedOutput, actualOutput);

 // Test unescape method with strict true
 String strictInputString = "&lt;helloworld&gt;";
 String strictExpectedOutput = "<helloworld>";
 String strictActualOutput = Entities.unescape(strictInputString, true);
 assertEquals(expectedOutput, strictActualOutput);
 }
}