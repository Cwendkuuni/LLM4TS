import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<!-- This is a comment -->", "", Parser.htmlParser());
 }

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment");
 assertEquals("This is a comment", comment.getData());
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testCommentCreationWithBaseUri() {
 Comment comment = new Comment("This is a comment", "http://example.com");
 assertEquals("This is a comment", comment.getData());
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 assertTrue(xmlComment.isXmlDeclaration());
 Comment nonXmlComment = new Comment("This is a non-XML comment");
 assertFalse(nonXmlComment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() {
 Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 XmlDeclaration xmlDeclaration = xmlComment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.xml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCommentEmptyData() {
 Comment emptyComment = new Comment("");
 fail("Empty comment data should not be allowed.");
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 Appendable accum = Jsoup.output().appendable();
 document.childNodes().get(0).outerHtml(accum, 0, null);
 String expected = "<!--This is a comment-->";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testCommentOuterHtmlWithPrettyPrint() throws IOException {
 Document prettyPrintDocument = Jsoup.parse("<!-- This is a comment -->", "", Parser.htmlParser()).outputSettings(new Document.OutputSettings().prettyPrint(true));
 Appendable accum = Jsoup.output().appendable();
 prettyPrintDocument.childNodes().get(0).outerHtml(accum, 0, null);
 String expected = "\n<!--\n This is a comment \n-->\n";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testCommentOuterHtmlWithTail() throws IOException {
 document.childNodes().get(0).outerHtmlHead(Jsoup.output().appendable(), 0, null);
 document.childNodes().get(0).outerHtmlTail(Jsoup.output().appendable(), 0, null);
 String expected = "<!--This is a comment-->";
 assertEquals(expected, document.toString());
 }
}