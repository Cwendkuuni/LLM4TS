package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testEmptyConstructor() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
 assertNotNull(expr);
 }

 @Test
 public void testConstructorWithArguments() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 assertNotNull(expr);
 }

 @Test
 public void testConstructorWithIncorrectArgumentTypes() {
 Executable test = () -> new CoreOperationRelationalExpression(new Expression[]{new OperatorExpression("+"), new ConstantExpression(2)});
 assertThrows(IllegalArgumentException.class, test);
 }

 @Test
 public void testConstructorWithInvalidOperationType() {
 Executable test = () -> new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 assertThrows(IllegalArgumentException.class, () -> ((CoreOperationRelationalExpression) test.getResult()).setOperationType("InvalidOperationType"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullArguments() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[] {null});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyArgumentList() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithLessThanTwoArguments() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[] {new ConstantExpression(1)});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithIncorrectNumberOfArguments() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2), new ConstantExpression(3)});
 }

 @Test
 public void testGetPrecedence() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 int prev = expr.getPrecedence();
 assertEquals(3, prev);
 }

 @Test
 public void testIsSymmetric() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 boolean isSymmetric = expr.isSymmetric();
 assertFalse(isSymmetric);
 }

 @Test
 public void testSetOperationTypeWithNullArgument() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 Executable test = () -> expr.setOperationType(null);
 assertThrows(NullPointerException.class, test);
 }

 @Test
 public void testSetOperationTypeWithInvalidOperationType() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 Executable test = () -> expr.setOperationType("InvalidOperationType");
 assertThrows(IllegalArgumentException.class, test);
 }

 @Test
 public void testSetOperationTypeWithValidOperationType() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 assertNotNull(expr.getOperationType());
 }

 @Test
 public void testSetOperationTypeWithSameOperationType() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(2), new ConstantExpression(1)});
 CoreOperationRelationalExpression expr3 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 assertEquals(expr, expr3);
 }

 @Test
 public void testSetOperationTypeWithDifferentOperationTypes() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(2), new ConstantExpression(1)});
 CoreOperationRelationalExpression expr3 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 assertNotEquals(expr, expr3);
 }

 @Test
 public void testSetOperationTypeWithSameExpressionObjects() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[]{expr.getArguments()[0], expr.getArguments()[1]});
 assertEquals(expr, expr2);
 }

 @Test
 public void testSetOperationTypeWithDifferentExpressionObjects() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(3)});
 assertNotEquals(expr, expr2);
 }

 @Test
 public void testSetOperationTypeWithSameExpressionObjectsAndDifferentTypes() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr2.setOperationType("!=");
 assertEquals(expr.getClass(), expr2.getClass());
 assertNotEquals(expr, expr2);
 }

 @Test
 public void testSetOperationTypeWithSameExpressionObjectsAndSameType() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr2.setOperationType(">");
 assertEquals(expr, expr2);
 }

 @Test
 public void testSetOperationTypeWithDifferentExpressionObjectsAndDifferentTypes() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(3), new ConstantExpression(4)});
 expr2.setOperationType(">=");
 assertEquals(expr.getClass(), expr2.getClass());
 assertNotEquals(expr, expr2);
 }

 @Test
 public void testSetOperationTypeWithDifferentExpressionObjectsAndSameTypes() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr2.setOperationType(">");
 assertEquals(expr, expr2);
 }

 @Test
 public void testSetOperationTypeWithDifferentExpressionObjectsAndDifferentTypesButSameExpressions() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr2.setOperationType(">=");
 CoreOperationRelationalExpression expr3 = new CoreOperationRelationalExpression(new Expression[]{expr.getArguments()[0], expr.getArguments()[1]});
 assertEquals(expr3, expr2.getArguments()[0]);
 assertEquals(expr3, expr.getArguments()[0]);
 assertNotEquals(expr, expr2);
 }

 @Test
 public void testSetOperationTypeWithDifferentExpressionObjectsAndSameTypesButDifferentExpressions() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(3), new ConstantExpression(4)});
 expr2.setOperationType(">");
 CoreOperationRelationalExpression expr3 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 CoreOperationRelationalExpression expr4 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(3), new ConstantExpression(4)});
 assertEquals(expr3, expr4);
 assertNotEquals(expr, expr2);
 }

 @Test
 public void testSetOperationTypeWithSameExpressionButDifferentTypes() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr.setOperationType(">");
 CoreOperationRelationalExpression expr2 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr2.setOperationType("!=");
 CoreOperationRelationalExpression expr3 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 CoreOperationRelationalExpression expr4 = new CoreOperationRelationalExpression(new Expression[]{new ConstantExpression(1), new ConstantExpression(2)});
 expr4.setOperationType(">");
 assertNotEquals(expr, expr2);
 assertEquals(expr3, expr4);
 }

 // Test file completed
}