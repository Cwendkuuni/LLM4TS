package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

 @Test
 public void testEqualStrings() {
 CoreOperationCompare op = new CoreOperationCompare(
 new StringLiteralExpression("Hello"), new StringLiteralExpression("Hello"));
 EvalContext context = new EvalContext();
 assertTrue(op.evaluate(context));
 }

 @Test
 public void testNotEqualStrings() {
 CoreOperationCompare op = new CoreOperationCompare(
 new StringLiteralExpression("Hello"), new StringLiteralExpression("World"));
 EvalContext context = new EvalContext();
 assertFalse(op.evaluate(context));
 }

 @Test
 public void testEqualNumbers() {
 CoreOperationCompare op = new CoreOperationCompare(
 new NumberLiteralExpression(1), new NumberLiteralExpression(1));
 EvalContext context = new EvalContext();
 assertTrue(op.evaluate(context));
 }

 @Test
 public void testNotEqualNumbers() {
 CoreOperationCompare op = new CoreOperationCompare(
 new NumberLiteralExpression(1), new NumberLiteralExpression(2));
 EvalContext context = new EvalContext();
 assertFalse(op.evaluate(context));
 }

 @Test
 public void testEqualBoolean() {
 CoreOperationCompare op = new CoreOperationCompare(
 new BooleanLiteralExpression(true), new BooleanLiteralExpression(true));
 EvalContext context = new EvalContext();
 assertTrue(op.evaluate(context));
 }

 @Test
 public void testNotEqualBoolean() {
 CoreOperationCompare op = new CoreOperationCompare(
 new BooleanLiteralExpression(true), new BooleanLiteralExpression(false));
 EvalContext context = new EvalContext();
 assertFalse(op.evaluate(context));
 }

 @Test
 public void testEqualCollectionsWithSameElements() {
 List<Integer> list1 = new ArrayList<Integer>() {{
 add(1);
 add(2);
 add(3);
 }};
 List<Integer> list2 = new ArrayList<Integer>() {{
 add(1);
 add(2);
 add(3);
 }};
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableRefExpression("$list1"), new VariableRefExpression("$list2"));
 op.setVariables(new Variables(new Variables.Variable[] {
 new Variables.Variable("list1", list1), new Variables.Variable("list2", list2) }));
 EvalContext context = new EvalContext();
 context.setVariables(op.getVariables());
 assertTrue(op.evaluate(context));
 }

 @Test
 public void testNotEqualCollectionsWithDifferentElements() {
 List<Integer> list1 = new ArrayList<Integer>() {{
 add(1);
 add(2);
 add(3);
 }};
 List<Integer> list2 = new ArrayList<Integer>() {{
 add(1);
 add(4);
 add(3);
 }};
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableRefExpression("$list1"), new VariableRefExpression("$list2"));
 op.setVariables(new Variables(new Variables.Variable[] {
 new Variables.Variable("list1", list1), new Variables.Variable("list2", list2) }));
 EvalContext context = new EvalContext();
 context.setVariables(op.getVariables());
 assertFalse(op.evaluate(context));
 }

 @Test
 public void testEqualPointers() {
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableRefExpression("$p1"), new VariableRefExpression("$p2"));
 op.setVariables(new Variables(new Variables.Variable[] {
 new Variables.Variable("p1", new Pointer(new InitialContext(), "a/b/c")),
 new Variables.Variable("p2", new Pointer(new InitialContext(), "a/b/c")) }));
 EvalContext context = new EvalContext();
 context.setVariables(op.getVariables());
 assertTrue(op.evaluate(context));
 }

 @Test
 public void testNotEqualPointers() {
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableRefExpression("$p1"), new VariableRefExpression("$p2"));
 op.setVariables(new Variables(new Variables.Variable[] {
 new Variables.Variable("p1", new Pointer(new InitialContext(), "a/b/c")),
 new Variables.Variable("p2", new Pointer(new InitialContext(), "a/d/e")) }));
 EvalContext context = new EvalContext();
 context.setVariables(op.getVariables());
 assertFalse(op.evaluate(context));
 }

 @Test
 public void testEqualCollectionAndPointer() {
 List<Pointer> list1 = new ArrayList<Pointer>() {{
 add(new Pointer(new InitialContext(), "a/b/c"));
 add(new Pointer(new InitialContext(), "a/b/d"));
 }};
 CoreOperationCompare op = new CoreOperationCompare(
 new VariableRefExpression("$list1"), new VariableRefExpression("$p1"));
 op.setVariables(new Variables(new Variables.Variable[] {
 new Variables.Variable("list1", list1), new Variables.Variable("p1", new Pointer(new InitialContext(), "a/b/c")) }));
 EvalContext context = new EvalContext();
 context.setVariables(op.getVariables());
 assertTrue(op.evaluate(context));
 }

 @Test(expected = AssertionError.class)
 public void testNullLeft() {
 CoreOperationCompare op = new CoreOperationCompare(
 new ConstantExpression(null), new StringLiteralExpression("Hello"));
 EvalContext context = new EvalContext();
 op.evaluate(context);
 // This test should throw an AssertionError due to null left operand
 }

 @Test(expected = AssertionError.class)
 public void testNullRight() {
 CoreOperationCompare op = new CoreOperationCompare(
 new StringLiteralExpression("Hello"), new ConstantExpression(null));
 EvalContext context = new EvalContext();
 op.evaluate(context);
 // This test should throw an AssertionError due to null right operand
 }
}