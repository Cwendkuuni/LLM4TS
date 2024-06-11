import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JDOMAttributeIterator.class})
public class JDOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private QName name;
 private Element element;

 @Before
 public void setUp() {
 String xmlns = "http://www.w3.org/2000/xmlns/";
 String prefix = "xml";
 String localName = "test";

 name = new QName(xmlns, prefix, localName);

 element = new Element("root");
 element.addNamespaceDeclaration(xmlns, prefix);
 element.setName("root");

 PowerMockito.mockStatic(JDOMAttributeIterator.class);
 }

 @Test
 public void testEmptyAttributeList() {
 NodeIterator iterator = JDOMAttributeIterator.newInstance(PowerMockito.mock(NodePointer.class), name);
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testSingleAttribute() {
 Attribute attribute = new Attribute("test", "testValue");
 element.addAttribute(attribute);

 PowerMockito.when(JDOMAttributeIterator.newInstance(PowerMockito.eq(PowerMockito.mock(NodePointer.class)), PowerMockito.eq(name)))
 .thenReturn(new JDOMAttributeIterator(PowerMockito.mock(NodePointer.class), name) {
 @Override
 List attributes = Collections.singletonList(attribute);
 });

 nodePointer = PowerMockito.mock(NodePointer.class);
 PowerMockito.when(nodePointer.getNode()).thenReturn(element);

 iterator = JDOMAttributeIterator.newInstance(nodePointer, name);
 Attribute attributeFromIterator = (Attribute) iterator.getNodePointer().getNode();
 assertEquals(attribute.getValue(), attributeFromIterator.getValue());
 }

 @Test
 public void testMultipleAttributes() {
 Attribute attribute1 = new Attribute("test1", "testValue1");
 Attribute attribute2 = new Attribute("test2", "testValue2");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);

 PowerMockito.when(JDOMAttributeIterator.newInstance(PowerMockito.eq(PowerMockito.mock(NodePointer.class)), PowerMockito.eq(name)))
 .thenReturn(new JDOMAttributeIterator(PowerMockito.mock(NodePointer.class), name) {
 @Override
 List attributes = new ArrayList() {{
 add(attribute1);
 add(attribute2);
 }};
 });

 nodePointer = PowerMockito.mock(NodePointer.class);
 PowerMockito.when(nodePointer.getNode()).thenReturn(element);

 iterator = JDOMAttributeIterator.newInstance(nodePointer, name);
 Attribute attributeFromIterator1 = (Attribute) iterator.getNodePointer().getNode();
 Attribute attributeFromIterator2 = iterator.getNodePointer().getNextSibling().getNodePointer().getNode();
 assertEquals(attribute1.getValue(), attributeFromIterator1.getValue());
 assertEquals(attribute2.getValue(), attributeFromIterator2.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWrongNamespace() {
 String wrongNs = "wrongNs";
 String wrongPrefix = "wrongPrefix";
 String localName = "test";

 name = new QName(wrongNs, wrongPrefix, localName);

 PowerMockito.when(JDOMAttributeIterator.newInstance(PowerMockito.eq(PowerMockito.mock(NodePointer.class)), PowerMockito.eq(name)))
 .thenReturn(new JDOMAttributeIterator(PowerMockito.mock(NodePointer.class), name) {
 @Override
 List attributes = new ArrayList();
 });

 nodePointer = PowerMockito.mock(NodePointer.class);
 PowerMockito.when(nodePointer.getNode()).thenReturn(element);

 JDOMAttributeIteratorIterator = JDOMAttributeIterator.newInstance(nodePointer, name);
 assertNull(iterator.getNodePointer());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextSiblingAfterLastAttribute() {
 PowerMockito.when(JDOMAttributeIterator.newInstance(PowerMockito.eq(PowerMockito.mock(NodePointer.class)), PowerMockito.eq(name)))
 .thenReturn(new JDOMAttributeIterator(PowerMockito.mock(NodePointer.class), name) {
 @Override
 List attributes = new ArrayList() {{
 add(new Attribute("test1", "testValue1"));
 }};
 });

 nodePointer = PowerMockito.mock(NodePointer.class);
 PowerMockito.when(nodePointer.getNode()).thenReturn(element);
 PowerMockito.when(nodePointer.getNextSibling()).thenReturn(null);

 JDOMAttributeIterator iterator = JDOMAttributeIterator.newInstance(nodePointer, name);
 iterator.next();
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testGetPosition() {
 PowerMockito.when(JDOMAttributeIterator.newInstance(PowerMockito.mock(NodePointer.class), PowerMockito.eq(name)))
 .thenReturn(new JDOMAttributeIterator(PowerMockito.mock(NodePointer.class), name) {
 @Override
 List attributes = new ArrayList() {{
 add(new Attribute("test1", "testValue1"));
 add(new Attribute("test2", "testValue2"));
 }};
 });

 nodePointer = PowerMockito.mock(NodePointer.class);
 PowerMockito.when(nodePointer.getNode()).thenReturn(element);
 iterator = JDOMAttributeIterator.newInstance(nodePointer, name);
 iterator.next();
 iterator.next();
 assertEquals(2, iterator.getPosition());
 }

 @Test
 public void testSetPosition() {
 PowerMockito.when(JDOMAttributeIterator.newInstance(PowerMockito.mock(NodePointer.class), PowerMockito.eq(name)))
 .thenReturn(new JDOMAttributeIterator(PowerMockito.mock(NodePointer.class), name) {
 @Override
 List attributes = new ArrayList() {{
 add(new Attribute("test1", "testValue1"));
 add(new Attribute("test2", "testValue2"));
 }};
 });

 nodePointer = PowerMockito.mock(NodePointer.class);
 PowerMockito.when(nodePointer.getNode()).thenReturn(element);
 iterator = JDOMAttributeIterator.newInstance(nodePointer, name);
 iterator.next();
 iterator.next();
 assertEquals(2, iterator.getPosition());
 iterator.setPosition(1);
 assertEquals(1, iterator.getPosition());
 }
}