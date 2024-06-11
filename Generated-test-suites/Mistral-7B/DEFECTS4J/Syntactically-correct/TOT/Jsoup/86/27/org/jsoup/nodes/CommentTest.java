import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class CommentTest {

 private Comment comment;

 @Before
 public void setUp() {
 comment = new Comment("This is a comment");
 }

 @Test
 public void testNodeName() {
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetData() {
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testIsXmlDeclarationFalse() {
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationTrue() {
 String xmlComment = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment xmlCommentNode = new Comment(xmlComment);
 assertTrue(xmlCommentNode.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationNull() {
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationValidXmlDeclaration() {
 String xmlComment = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n";
 Comment xmlCommentNode = new Comment(xmlComment);
 XmlDeclaration decl = xmlCommentNode.asXmlDeclaration();
 assertNotNull(decl);
 assertEquals("version=\"1.0\" encoding=\"UTF-8\"", decl.xmlVersion());
 assertEquals("UTF-8", decl.encoding());
 assertNotNull(decl.attributes());
 }

 @Test
 public void testOuterHtml() throws IOException {
 Appendable accum = new StringBuilder();
 comment.outerHtmlHead(accum, 0, Document.OutputSettings.prettyPrint());
 comment.outerHtmlTail(accum, 0, Document.OutputSettings.prettyPrint());
 assertEquals("<!--This is a comment-->", comment.outerHtml());
 }

 @Test
 public void testToString() {
 assertEquals("<!--This is a comment-->", comment.toString());
 }
}