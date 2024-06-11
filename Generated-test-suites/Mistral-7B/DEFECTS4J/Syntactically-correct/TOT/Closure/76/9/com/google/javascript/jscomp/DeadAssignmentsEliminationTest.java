package com.google.javascript.jscomp;

import com.google.common.base.Predicates;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import static com.google.javascript.jscomp.NodeUtil.isAssign;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest extends CompilationTestBase {

 private DeadAssignmentsElimination elimination;

 @Before
 public void setUp() {
 compiler = new Compiler();
 elimination = new DeadAssignmentsElimination(compiler);
 }

 @Test
 public void testDeadAssignment() {
 String source = "function foo() {" +
 "  x = 1;" +
 "  if (y === 1) {" +
 "    x = 2;" +
 "  }" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  x = 1;" +
 "  if (y === 1) {" +
 "  }" +
 "  return 1; // Dead assignment eliminated" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithElse() {
 String source = "function foo() {" +
 "  x = 1;" +
 "  if (y === 1) {" +
 "    x = 2;" +
 "  } else {" +
 "    x = 3;" +
 "  }" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  x = 1;" +
 "  if (y === 1) {" +
 "  } else {" +
 "    x = 3;" +
 "  }" +
 "  return 1; // Dead assignment eliminated" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithRedundantElse() {
 String source = "function foo() {" +
 "  x = 1;" +
 "  if (y === 1) {" +
 "    x = 2;" +
 "  } else {" +
 "    x = 2;" +
 "  }" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  x = 1;" +
 "  if (y === 1) {" +
 "    x = 2;" +
 "  }" +
 "  return 2; // Dead assignment eliminated" +
 "}", source);
 }

 @Test
 public void testAssignmentInCondition() {
 String source = "function foo() {" +
 "  if ((x = 1) === 1) {" +
 "    x = 2;" +
 "  }" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  if (1 === 1) {" +
 "    x = 2;" +
 "  }" +
 "  return 1; // Dead assignment eliminated" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithFunction() {
 String source = "function foo() {" +
 "  function bar() {" +
 "    x = 1;" +
 "    if (y === 1) {" +
 "      x = 2;" +
 "    }" +
 "    return x;" +
 "  }" +
 "  return bar();" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  function bar() {" +
 "    x = 1;" +
 "    if (y === 1) {" +
 "    }" +
 "    return 1; // Dead assignment eliminated" +
 "  }" +
 "  return bar();" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithConditionalExpression() {
 String source = "function foo() {" +
 "  x = 1;" +
 "  return y === 1 ? x = 2 : x = 3;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  x = 1;" +
 "  return y === 1 ? undefined : 3; // Dead assignment eliminated" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithReturn() {
 String source = "function foo() {" +
 "  x = 1;" +
 "  return x = 2;" +
 "}";

 Node root = parse(source);
 try {
 elimination.process(null, root);
 failure("Dead assignment inside a return statement is not eliminated.");
 } catch (Compiler.CompilationError e) {
 // Expected
 }
 }

 @Test
 public void testDeadAssignmentWithIncrement() {
 String source = "function foo() {" +
 "  x = 1;" +
 "  x++; // Dead increment is not eliminated" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  x = 1;" +
 "  x++; // Dead increment is not eliminated" +
 "  return 2; // x = 1 + 1; is not eliminated" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithDecrement() {
 String source = "function foo() {" +
 "  x = 1;" +
 "  x--; // Dead decrement is not eliminated" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  x = 1;" +
 "  x--; // Dead decrement is not eliminated" +
 "  return 0; // x = 1 - 1; is not eliminated" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithAssignmentOperator() {
 String source = "function foo() {" +
 "  x = 1;" +
 "  x = x + 1; // Dead assignment is not eliminated" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  x = 1;" +
 "  x = 1 + 1; // Dead assignment is not eliminated" +
 "  return 2; // x = 1 + 1; is not eliminated" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithConditionalAssignment() {
 String source = "function foo() {" +
 "  x = 1;" +
 "  y = 2;" +
 "  x = y > 1 ? 1 : 2; // Dead assignment is not eliminated" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  x = 1;" +
 "  y = 2;" +
 "  x = y > 1 ? 1 : 2; // Dead assignment is not eliminated" +
 "  return 1; // Dead assignment is not eliminated" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithEmptyStatement() {
 String source = "function foo() {" +
 "  x = 1;" +
 "  if (true) {" +
 "    ; // Empty statement" +
 "  }" +
 "  x = 2; // Dead assignment is not eliminated" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  x = 1;" +
 "  if (true) {" +
 "  }" +
 "  return 1; // Dead assignment is not eliminated" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithVariableDeclaration() {
 String source = "function foo() {" +
 "  var x = 1;" +
 "  x = 2; // Dead assignment is not eliminated" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  var x = 1;" +
 "  x = 2; // Dead assignment is not eliminated" +
 "  return 2;", source);
 }

 @Test
 public void testDeadAssignmentWithWhileLoop() {
 String source = "function foo() {" +
 "  x = 1;" +
 "  while (true) {" +
 "    x = 2;" +
 "  }" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  x = 1;" +
 "  while (true) {" +
 "  }" +
 "  return 1; // Dead assignment is not eliminated" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithForLoop() {
 String source = "function foo() {" +
 "  for (var i = 0; i < 10; i++) {" +
 "    x = 1;" +
 "  }" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  for (var i = 0; i < 10; i++) {" +
 "  }" +
 "  return 1; // Dead assignment is not eliminated" +
 "}", source);
 }

 @Test
 public void testDeadAssignmentWithForInLoop() {
 String source = "function foo() {" +
 "  for (var i in [\"foo\", \"bar\"])" +
 "  {" +
 "    x = 1;" +
 "  }" +
 "  return x;" +
 "}";

 Node root = parse(source);
 elimination.process(null, root);
 expected("function foo() {" +
 "  for (var i in [\"foo\", \"bar\"])" +
 "  {" +
 "  }" +
 "  return 1; // Dead assignment is not eliminated" +
 "}", source);
 }
}