import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 // Test creating a DocumentType with valid arguments
 String name = "html";
 String publicId = "-//W3C//DTD HTML 4.01//EN";
 String systemId = "http://www.w3.org/TR/html4/strict.dtd";
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 // Test nodeName method
 assertEquals("#doctype", documentType.nodeName());

 // Test outerHTML method
 StringBuilder accum = new StringBuilder();
 documentType.outerHtml(accum, 0, null);
 String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyName() {
 try {
 String name = "";
 String publicId = "myPublicId";
 String systemId = "mySystemId";
 String baseUri = "http://example.com";
 new DocumentType(name, publicId, systemId, baseUri);
 fail("Expected IllegalArgumentException for empty name");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testDocumentTypeCreationWithNullName() {
 try {
 String name = null;
 String publicId = "myPublicId";
 String systemId = "mySystemId";
 String baseUri = "http://example.com";
 new DocumentType(name, publicId, systemId, baseUri);
 fail("Expected NullPointerException for null name");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testDocumentTypeCreationWithNullPublicId() {
 String name = "html";
 String publicId = null;
 String systemId = "mySystemId";
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 assertNull(documentType.attr("publicId"));
 }

 @Test
 public void testDocumentTypeCreationWithNullSystemId() {
 String name = "html";
 String publicId = "myPublicId";
 String systemId = null;
 String baseUri = "http://example.com";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 assertNull(documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithNullBaseUri() {
 String name = "html";
 String publicId = "myPublicId";
 String systemId = "mySystemId";
 String baseUri = null;
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 assertEquals("", documentType.attr("baseUri"));
 }

 @Test
 public void testDocumentTypeToString() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 assertEquals("DocumentType[name=html]", documentType.toString());
 }

 @Test
 public void testDocumentTypeClone() throws CloneNotSupportedException {
 DocumentType originalDocumentType = new DocumentType("html", null, null, null);
 DocumentType clonedDocumentType = (DocumentType) originalDocumentType.cloneNode(true);
 assertEquals(originalDocumentType, clonedDocumentType);
 }

 @Test
 public void testDocumentTypeEquals() {
 DocumentType documentType1 = new DocumentType("html", null, null, null);
 DocumentType documentType2 = new DocumentType("html", null, null, null);
 assertEquals(documentType1, documentType2);
 }

 @Test
 public void testDocumentTypeHashCode() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 int hashCode = documentType.hashCode();
 DocumentType documentType2 = new DocumentType("html", null, null, null);
 assertEquals(hashCode, documentType2.hashCode());
 }

 @Test
 public void testDocumentTypeGetParentNodeReturnsNull() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 assertNull(documentType.getParentNode());
 }

 @Test
 public void testDocumentTypeGetChildrenReturnsEmptyList() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 assertEquals(0, documentType.childNodes().size());
 }

 @Test
 public void testDocumentTypeGetPreviousSiblingReturnsNull() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 assertNull(documentType.previousSibling());
 }

 @Test
 public void testDocumentTypeGetNextSiblingReturnsNull() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 assertNull(documentType.nextSibling());
 }

 @Test
 public void testDocumentTypeInsertBefore() {
 Document document = new Document();
 Element rootElement = new Element("html");
 document.setRootElement(rootElement);

 DocumentType documentType = new DocumentType("html", null, null, null);
 TextNode textNode = new TextNode("Hello World!");

 rootElement.insertBefore(documentType, textNode);
 assertEquals(documentType, textNode.previousSibling());
 assertEquals(textNode, documentType.nextSibling());
 }

 @Test
 public void testDocumentTypeReplaceChildren() {
 Document document = new Document();
 Element rootElement = new Element("html");
 document.setRootElement(rootElement);

 DocumentType documentType = new DocumentType("html", null, null, null);
 TextNode textNode1 = new TextNode("Hello");
 TextNode textNode2 = new TextNode("World!");

 rootElement.appendChild(textNode1);
 rootElement.appendChild(textNode2);

 rootElement.replaceChildren(documentType);
 assertEquals(documentType, rootElement.getFirstChild());
 }

 @Test
 public void testDocumentTypeRemove() {
 Document document = new Document();
 Element rootElement = new Element("html");
 document.setRootElement(rootElement);

 DocumentType documentType = new DocumentType("html", null, null, null);
 TextNode textNode1 = new TextNode("Hello");
 TextNode textNode2 = new TextNode("World!");

 rootElement.appendChild(textNode1);
 rootElement.appendChild(documentType);
 rootElement.appendChild(textNode2);

 rootElement.removeChild(documentType);
 assertEquals(textNode1, rootElement.getChildNodes().get(0));
 assertEquals(textNode2, rootElement.getChildNodes().get(1));
 }
}