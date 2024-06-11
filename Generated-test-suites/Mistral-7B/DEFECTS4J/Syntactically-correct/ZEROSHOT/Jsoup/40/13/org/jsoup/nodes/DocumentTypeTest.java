import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeTest {

 private Document mockDocument;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mockDocument = Mockito.mock(Document.class);
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "publicID";
 String systemId = "systemID";
 String baseUri = "baseUri";

 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr("name"));
 assertEquals(publicId, documentType.attr("publicId"));
 assertEquals(systemId, documentType.attr("systemId"));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("", "", "", "");

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertNull(documentType.baseUri());
 }

 @Test
 public void testOuterHtmlHead() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
 StringBuilder expectedAccum = new StringBuilder();
 expectedAccum.append("<!DOCTYPE html");
 expectedAccum.append(" PUBLIC \"publicID systemID\"");
 expectedAccum.append('>');

 Mockito.when(mockDocument.settings()).thenReturn(new Document.OutputSettings());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, mockDocument.settings());

 assertEquals(expectedAccum.toString(), accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptyPublicId() {
 DocumentType documentType = new DocumentType("html", "", "systemID", "baseUri");
 StringBuilder expectedAccum = new StringBuilder();
 expectedAccum.append("<!DOCTYPE html");
 expectedAccum.append(" PUBLIC \"\" systemID");
 expectedAccum.append('>');

 Mockito.when(mockDocument.settings()).thenReturn(new Document.OutputSettings());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, mockDocument.settings());

 assertEquals(expectedAccum.toString(), accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptySystemId() {
 DocumentType documentType = new DocumentType("html", "publicID", "", "baseUri");
 StringBuilder expectedAccum = new StringBuilder();
 expectedAccum.append("<!DOCTYPE html");
 expectedAccum.append(" PUBLIC publicID \"\"");
 expectedAccum.append('>');

 Mockito.when(mockDocument.settings()).thenReturn(new Document.OutputSettings());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, mockDocument.settings());

 assertEquals(expectedAccum.toString(), accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptyName() {
 DocumentType documentType = new DocumentType("", "publicID", "systemID", "baseUri");
 StringBuilder expectedAccum = new StringBuilder();
 expectedAccum.append("<!DOCTYPE ");
 expectedAccum.append('>');

 Mockito.when(mockDocument.settings()).thenReturn(new Document.OutputSettings());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, mockDocument.settings());

 assertEquals(expectedAccum.toString(), accum.toString());
 }

 @Test
 public void testOuterHtmlTail() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, mockDocument.settings());

 assertEquals("", accum.toString());
 }
}