import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType doctype = (DocumentType) doc.document().child(0);

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr(NAME));
 assertEquals("PUBLIC", doctype.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithEmptyIds() {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType doctype = (DocumentType) doc.document().child(0);

 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr(PUBLIC_ID));
 assertNull(doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithSingleId() {
 Document doc = Jsoup.parse("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">");
 DocumentType doctype = (DocumentType) doc.document().child(0);

 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithBothIds() {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType doctype = (DocumentType) doc.document().child(0);

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("HTML 4.01 Transitional", doctype.attr(NAME));
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType doctype = (DocumentType) doc.document().child(0);

 StringBuilder output = new StringBuilder();
 doctype.outerHtmlHead(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", output.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithCustomIndentation() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType doctype = (DocumentType) doc.document().child(0);

 StringBuilder output = new StringBuilder();
 doctype.outerHtmlHead(output, 2, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("<!DOCTYPE html\n        PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", output.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType doctype = (DocumentType) doc.document().child(0);

 StringBuilder output = new StringBuilder();
 doctype.outerHtmlTail(output, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("", output.toString());
 }
}