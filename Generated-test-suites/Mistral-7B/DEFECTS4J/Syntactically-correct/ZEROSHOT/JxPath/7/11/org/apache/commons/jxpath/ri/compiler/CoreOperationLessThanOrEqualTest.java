package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqual() {
 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
 new NumberNode(new InfoSet(), 5.0), new NumberNode(new InfoSet(), 5.0));
 EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
 Object result = operation.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanOrEqualNotSatisfied() {
 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
 new NumberNode(new InfoSet(), 5.0), new NumberNode(new InfoSet(), 6.0));
 EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
 Object result = operation.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanOrEqualNull() {
 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
 new NumberNode(new InfoSet(), 5.0), null);
 EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
 try {
 operation.computeValue(context);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testLessThanOrEqualWithString() {
 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
 new NumberNode(new InfoSet(), 5.0), new NumberNode(new InfoSet(), "5.0"));
 EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
 Object result = operation.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanOrEqualWithStringNotSatisfied() {
 CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
 new NumberNode(new InfoSet(), 5.0), new NumberNode(new InfoSet(), "6.0"));
 EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
 Object result = operation.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }
}