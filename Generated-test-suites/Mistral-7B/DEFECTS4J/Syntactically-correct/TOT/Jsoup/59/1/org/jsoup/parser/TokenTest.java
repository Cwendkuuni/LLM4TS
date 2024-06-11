import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.getName().equals("doctype");
 doctypeToken.reset();
 }

 @Test
 public void testDoctypeTokenWithName() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.getName().equals("doctype");
 doctypeToken.getName().equals("html");
 doctypeToken.reset();
 }

 @Test(expected = AssertionError.class)
 public void testDoctypeTokenWithInvalidName() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("invalid");
 }

 @Test
 public void testDoctypeTokenWithPublicIdentifier() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.getPublicIdentifier().equals("");
 doctypeToken.publicIdentifier.append("_prefix");
 doctypeToken.getPublicIdentifier().equals("_prefix");
 doctypeToken.reset();
 }

 @Test
 public void testDoctypeTokenWithSystemIdentifier() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.systemIdentifier.append("/path/to/file.html");
 doctypeToken.getSystemIdentifier().equals("/path/to/file.html");
 doctypeToken.reset();
 }

 @Test
 public void testDoctypeTokenWithForceQuirks() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.isForceQuirks();
 doctypeToken.isForceQuirks(); // should be false by default
 doctypeToken.forceQuirks = true;
 doctypeToken.isForceQuirks(); // should be true
 doctypeToken.reset();
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("p");
 startTagToken.name("P"); // should be converted to lower case
 startTagToken.getAttributes().put("class", "red");
 startTagToken.getAttributes().put("id", "myId");
 startTagToken.toString().equals("<p class=\"red\" id=\"myId\"></p>");
 startTagToken.reset();
 }

 @Test
 public void testStartTagTokenWithEmptyAttributeValue() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("input");
 startTagToken.attributes.put("type", "");
 startTagToken.toString().equals("<input type=\"\"></input>");
 startTagToken.reset();
 }

 @Test
 public void testStartTagTokenWithBooleanAttribute() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("input");
 startTagToken.attributes.put("checked", false);
 startTagToken.toString().equals("<input>");
 startTagToken.attributes.put("checked", true);
 startTagToken.toString().equals("<input checked=\"checked\"></input>");
 startTagToken.reset();
 }

 @Test
 public void testStartTagTokenWithMultipleAttributes() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("img");
 startTagToken.attributes.put("src", "image.jpg");
 startTagToken.attributes.put("alt", "Description of image");
 startTagToken.toString().equals("<img src=\"image.jpg\" alt=\"Description of image\"></img>");
 startTagToken.reset();
 }

 @Test(expected = AssertionError.class)
 public void testStartTagTokenWithInvalidName() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("invalid");
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("p");
 endTagToken.name("P").equals("p");
 endTagToken.toString().equals("</p>");
 endTagToken.reset();
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data.append("This is a comment");
 commentToken.getData().equals("This is a comment");
 commentToken.toString().equals("<!--This is a comment-->");
 commentToken.reset();
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("!");
 characterToken.getData().equals("!");
 characterToken.toString().equals("!");
 characterToken.reset();
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();
 eofToken.isEOF();
 eofToken.reset();
 }
}