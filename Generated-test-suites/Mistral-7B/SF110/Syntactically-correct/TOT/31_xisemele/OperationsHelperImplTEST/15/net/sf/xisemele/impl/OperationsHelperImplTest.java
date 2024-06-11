import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

public class OperationsHelperImplTest {

 private Document document;
 private OperationsHelper helper;

 @Before
 public void setUp() {
 document = createDocument();
 helper = new OperationsHelperImpl();
 }

 @Test
 public void testChildren() {
 Element root = document.createElement("root");
 root.appendChild(createElement("child1"));
 root.appendChild(createElement("child2"));
 root.appendChild(createElement("grandchild1"));
 root.appendChild(createElement("grandchild2"));
 Node node = root;
 List<Node> children = helper.children(node);
 assertEquals(4, children.size());
 assertEquals("child1", children.get(0).getNodeName());
 assertEquals("child2", children.get(1).getNodeName());
 assertEquals("grandchild1", children.get(2).getNodeName());
 assertEquals("grandchild2", children.get(3).getNodeName());
 }

 @Test
 public void testFind() {
 Node found = helper.find(document, "/root/child2");
 assertEquals("child2", found.getNodeName());
 found = helper.find(document, "/root/grandchild1");
 assertEquals("grandchild1", found.getNodeName());
 found = helper.find(document, "/root/nonexistent");
 assertNull(found);
 }

 @Test
 public void testNodeWithName() {
 List<Node> nodes = helper.children(document.getDocumentElement());
 Node node = helper.nodeWithName(nodes, "root");
 assertEquals("root", node.getNodeName());
 node = helper.nodeWithName(nodes, "nonexistent");
 assertNull(node);
 }

 @Test
 public void testSublistWithName() {
 List<Node> nodes = helper.children(document.getDocumentElement());
 List<Node> sublist = helper.sublistWithName(nodes, "root");
 assertEquals(1, sublist.size());
 assertEquals("root", sublist.get(0).getNodeName());
 sublist = helper.sublistWithName(nodes, "nonexistent");
 assertEquals(0, sublist.size());
 }

 private Element createElement(String tag) {
 Element element = document.createElement(tag);
 return element;
 }

 private Document createDocument() {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder;
 try {
 builder = factory.newDocumentBuilder();
 } catch (ParserConfigurationException e) {
 throw new RuntimeException(e);
 }
 return builder.newDocument();
 }
}