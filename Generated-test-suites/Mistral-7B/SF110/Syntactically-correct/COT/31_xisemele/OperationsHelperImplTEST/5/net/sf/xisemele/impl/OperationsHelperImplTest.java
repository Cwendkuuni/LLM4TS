import org.junit.Test;
import org.junit.Assert;
import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.Arrays;import static org.junit.Assert.assertNotNull;import static org.junit.Assert.assertNull;import static org.junit.Assert.assertEquals;

public class OperationsHelperImplTest {

 private static final String XML_SAMPLE = "<?xml version=\"1.0\"?>\n" +
         "<root>\n" +
         "<child1>\n" +
         "<grandChild1/>\n" +
         "<grandChild2/>\n" +
         "</child1>\n" +
         "<child2>\n" +
         "<grandChild3/>\n" +
         "</child2>\n" +
         "</root>";

 @Test
 public void testChildren() {
     Document document = createDocument(XML_SAMPLE);
     Node root = document.getDocumentElement();
     List<Node> children = new OperationsHelperImpl().children(root);
     assertNotNull(children);
     assertEquals(2, children.size());
     assertEquals("child1", children.get(0).getNodeName());
     assertEquals("child2", children.get(1).getNodeName());
 }

 @Test
 public void testFindNullDocument() {
     assertNull(new OperationsHelperImpl().find(null, "/"));
 }

 @Test
 public void testFindNullPath() {
     Document document = createDocument(XML_SAMPLE);
     assertNull(new OperationsHelperImpl().find(document, null));
 }

 @Test
 public void testFindInvalidPath() {
     Document document = createDocument(XML_SAMPLE);
     assertNull(new OperationsHelperImpl().find(document, "/child1/invalid"));
 }

 @Test
 public void testFind() {
     Document document = createDocument(XML_SAMPLE);
     Node root = document.getDocumentElement();
     Node found = new OperationsHelperImpl().find(document, "/child1/grandChild1");
     assertNotNull(found);
     assertEquals("grandChild1", found.getNodeName());
 }

 @Test
 public void testNodeWithNameNullNodes() {
     List<Node> nodes = new ArrayList<>();
     assertNull(new OperationsHelperImpl().nodeWithName(nodes, "child1"));
 }

 @Test
 public void testNodeWithNameInvalidName() {
     Document document = createDocument(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     assertNull(new OperationsHelperImpl().nodeWithName(nodes, "invalid"));
 }

 @Test
 public void testNodeWithNameValidName() {
     Document document = createDocument(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     Node node = new OperationsHelperImpl().nodeWithName(nodes, "child1");
     assertNotNull(node);
     assertEquals("child1", node.getNodeName());
 }

 @Test
 public void testSublistWithNameNullNodes() {
     List<Node> nodes = new ArrayList<>();
     assertNotNull(new OperationsHelperImpl().sublistWithName(nodes, "child1"));
     assertEquals(0, new OperationsHelperImpl().sublistWithName(nodes, "child1").size());
 }

 @Test
 public void testSublistWithNameInvalidName() {
     Document document = createDocument(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     assertNotNull(new OperationsHelperImpl().sublistWithName(nodes, "invalid"));
     assertEquals(0, new OperationsHelperImpl().sublistWithName(nodes, "invalid").size());
 }

 @Test
 public void testSublistWithNameValidName() {
     Document document = createDocument(XML_SAMPLE);
     List<Node> nodes = new OperationsHelperImpl().children(document.getDocumentElement());
     List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, "child1");
     assertNotNull(sublist);
     assertEquals(2, sublist.size());
     assertEquals("grandChild1", sublist.get(0).getNodeName());
     assertEquals("grandChild2", sublist.get(1).getNodeName());
 }

 private Document createDocument(String xml) {
     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     DocumentBuilder builder = factory.newDocumentBuilder();
     return builder.parse(new ByteArrayInputStream(xml.getBytes()));
 }

}