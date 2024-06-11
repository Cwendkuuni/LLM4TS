import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

 private OperationsHelper operationsHelper = new OperationsHelperImpl();

 @Test
 public void testChildren() {
 Node node = createTestDOM().getDocumentElement();
 List<Node> children = operationsHelper.children(node);
 assertEquals(5, children.size());
 }

 @Test
 public void testChildrenNullNode() {
 Node node = null;
 List<Node> children = operationsHelper.children(node);
 assertNull(children);
 }

 @Test
 public void testChildrenEmptyNode() {
 Node node = createTestDOM().getDocumentElement();
 node.setTextContent("");
 List<Node> children = operationsHelper.children(node);
 assertEquals(0, children.size());
 }

 @Test
 public void testFind() {
 Document document = createTestDOM();
 String path = "html/head/title";
 Node expected = document.getElementsByTagName("title").item(0);
 Node actual = operationsHelper.find(document, path);
 assertEquals(expected, actual);
 }

 @Test
 public void testFindNullDocument() {
 Document document = null;
 String path = "html/head/title";
 Node actual = operationsHelper.find(document, path);
 assertNull(actual);
 }

 @Test
 public void testFindInvalidPath() {
 Document document = createTestDOM();
 String path = "html/head/title/invalid";
 Node actual = operationsHelper.find(document, path);
 assertNull(actual);
 }

 @Test
 public void testFindEmptyDocument() {
 Document document = createEmptyDOM();
 String path = "html/head/title";
 Node actual = operationsHelper.find(document, path);
 assertNull(actual);
 }

 @Test
 public void testNodeWithName() {
 List<Node> nodes = createTestDOM().getElementsByTagName("p");
 Node expected = nodes.get(0);
 Node actual = operationsHelper.nodeWithName(nodes, "p");
 assertEquals(expected, actual);
 }

 @Test
 public void testNodeWithNameNullNodes() {
 List<Node> nodes = null;
 String name = "p";
 Node actual = operationsHelper.nodeWithName(nodes, name);
 assertNull(actual);
 }

 @Test
 public void testNodeWithNameInvalidName() {
 List<Node> nodes = createTestDOM().getElementsByTagName("p");
 String name = "div";
 Node actual = operationsHelper.nodeWithName(nodes, name);
 assertNull(actual);
 }

 @Test
 public void testSublistWithName() {
 List<Node> nodes = createTestDOM().getElementsByTagName("p");
 String name = "p";
 List<Node> expected = new ArrayList<Node>();
 expected.add(nodes.get(0));
 expected.add(nodes.get(1));
 List<Node> actual = operationsHelper.sublistWithName(nodes, name);
 assertEquals(expected, actual);
 }

 @Test
 public void testSublistWithNameEmptyNodes() {
 List<Node> nodes = new ArrayList<Node>();
 String name = "p";
 List<Node> actual = operationsHelper.sublistWithName(nodes, name);
 assertEmpty(actual);
 }

 @Test
 public void testSublistWithNameInvalidName() {
 List<Node> nodes = createTestDOM().getElementsByTagName("p");
 String name = "div";
 List<Node> actual = operationsHelper.sublistWithName(nodes, name);
 assertEmpty(actual);
 }

 private Document createTestDOM() {
 File xmlFile = new File("test.xml"); // Assuming test.xml exists with the given structure
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 Document document;
 try {
 document = factory.newDocumentBuilder().parse(xmlFile);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 return document;
 }

 private Document createEmptyDOM() {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 Document document;
 try {
 document = factory.newDocumentBuilder().newDocument();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 return document;
}
}