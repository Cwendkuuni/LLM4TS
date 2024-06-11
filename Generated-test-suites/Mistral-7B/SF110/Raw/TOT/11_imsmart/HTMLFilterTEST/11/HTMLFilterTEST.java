import org.junit.Test; import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

 @Test
 public void testFilterNullMessage() {
 String expected = null;
 String actual = HTMLFilter.filter(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterEmptyMessage() {
 String expected = "";
 String actual = HTMLFilter.filter("");
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterSimpleMessage() {
 String message = "This is a simple message.";
 String expected = "This is a simple message.";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test
 public void testFilterMessageWithSpecialCharacters() {
 String message = "<html><body><p>&quot;This is a message with special characters: &amp;<>&quot;.</p></body></html>";
 String expected = "&lt;html&gt;&lt;body&gt;&lt;p&gt;&#34;This is a message with special characters: &amp;lt;&gt;&amp;#34;.&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;";
 String actual = HTMLFilter.filter(message);
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterNullContent() {
 char[] content = null;
 String expectedMessage = "NullPointerException: Cannot invoke char[].length() because the value of content is null";
 String actualMessage = "";
 try {
 HTMLOutputFilter.filter(content);
 } catch (NullPointerException e) {
 actualMessage = e.getMessage();
 }
 assertEquals(expectedMessage, actualMessage);
 }
}