import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.annotation.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JDOMAttributeIterator.class)
public class JDOMAttributeIteratorTest {

 private NodeIterator iterator;
 private Element rootElement;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 rootElement = new Element("root");
 rootElement.addAttribute("attr1", "value1");
 rootElement.addAttribute("attr2", "value2");

 when(JDOMAttributeIterator.class.newInstance(any(NodePointer.class), any(QName.class))).thenAnswer(invocationOnMock -> new JDOMAttributeIterator(invocationOnMock.getArgument(0), invocationOnMock.getArgument(1)));
 }

 @Test
 public void testIteratorEmpty() {
 NodeIterator iterator = JDOMAttributeIterator.newInstance(new JDOMPointer(new Element("root")), new QName("", ""));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorSingleAttribute() {
 NodeIterator iterator = JDOMAttributeIterator.newInstance(new JDOMPointer(rootElement), new QName("", "attr1"));
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(rootElement.getAttribute("attr1"), next.getNode());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorMultipleAttributes() {
 NodeIterator iterator = JDOMAttributeIterator.newInstance(new JDOMPointer(rootElement), new QName("", "*"));
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(rootElement.getAttribute("attr1"), next.getNode());
 assertTrue(iterator.hasNext());
 next = iterator.next();
 assertEquals(rootElement.getAttribute("attr2"), next.getNode());
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorNullParent() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("", ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorNullName() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(rootElement), null);
 }

 @Test(expected = IllegalStateException.class)
 public void testIteratorMovedPosition() {
 NodeIterator iterator = JDOMAttributeIterator.newInstance(new JDOMPointer(rootElement), new QName("", "*"));
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.previous();
 iterator.next(); // should return the same attribute as before
 assertSame(rootElement.getAttribute("attr2"), iterator.next().getNode());
 }
}