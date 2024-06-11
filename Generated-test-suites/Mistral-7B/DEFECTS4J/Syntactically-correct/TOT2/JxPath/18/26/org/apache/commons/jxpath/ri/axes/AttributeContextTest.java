// Combined JUnit test file
package org.junit.tests.apachecommons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Category(org.junit.experimental.categories.SmallTest.class)
public class AttributeContextTest {

 private AttributeContext ac;
 private EvalContext parentContext;

 @Before
 public void setUp() {
 parentContext = new EvalContext();
 ac = new AttributeContext(parentContext, new NodeNameTest(new QName("test:attr")));
 }

 @After
 public void tearDown() {
 ac = null;
 parentContext = null;
 }

 @Test
 public void testAttributeContextConstructor() {
 AttributeContext ac = new AttributeContext(null, new NodeNameTest(new QName("test:attr")));
 assertNotNull(ac);
 }

 @Test
 public void testGetCurrentNodePointerNormalUsage() {
 NodePointer nodePointer = ac.getCurrentNodePointer();
 assertNotNull(nodePointer);
 }

 @Test
 public void testReset() {
 ac.reset();
 assertFalse(ac.isStarted());
 }

 @Test
 public void testSetPositionNormalUsage() {
 ac.setPosition(1);
 NodePointer nodePointer = ac.getCurrentNodePointer();
 assertNotNull(nodePointer);
 }

 @Test(expected = NoSuchElementException.class)
 public void testNextNodeNoAttributes() {
 ac.nextNode();
 ac.nextNode();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextNodeInvalidNodeTest() {
 ac.nextNode();
 ac.nodeTest = null;
 ac.nextNode();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextNodeNullParentContext() {
 ac.parentContext = null;
 ac.nextNode();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextNodeNullNodeTest() {
 ac.nodeTest = null;
 ac.nextNode();
 }

 @Test(expected = NullPointerException.class)
 public void testNextNodeNullIterator() {
 ac.iterator = null;
 ac.nextNode();
 }

}