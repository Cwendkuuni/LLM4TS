package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

 AttributeContext ac;

 @Before
 public void setUp() {
 ac = new AttributeContext(null, new NodeNameTest(new QName("testAttribute")));
 }

 // Test cases go here

 @After
 public void tearDown() {
 ac = null;
 }

}