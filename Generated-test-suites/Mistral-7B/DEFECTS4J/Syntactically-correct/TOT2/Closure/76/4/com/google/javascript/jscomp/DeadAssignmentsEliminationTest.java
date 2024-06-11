import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

public class DeadAssignmentsEliminationTest extends CompilationTestBase {

 private AbstractCompiler compiler;
 private NodeFactory nodeFactory;

 @BeforeClass
 public static void setUpBeforeClass() {
 Compiler.initialize();
 }

 @Before
 public void setUp() {
 compiler = new Compiler();
 nodeFactory = NodeFactory.getFactory();
 }

 @Test
 public void testDeadAssignmentsElimination() {
 String jsCode = "function foo(x) {\n" +
 "  var a = 1;\n" +
 "  var b = 2;\n" +
 "  x = a;\n" + // Dead assignment
 "  a = b;\n" +
 "  x = 3;\n" +
 "  return x;\n" +
 "}";

 Node root = parseJs(jsCode);
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 NodeTraversal nodeTraversal = new NodeTraversal(root);
 deadAssignmentsElimination.process(null, root);
 NodeTraversal.traverse(compiler, root, nodeTraversal);
 String expectedCode = "function foo(x) {\n" +
 "  var a = 2;\n" +
 "  x = a;\n" +
 "  x = 3;\n" +
 "  return x;\n" +
 "}";
 assertCompilesTo(expectedCode, root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithFunctionExpression() {
 String jsCode = "function foo(x) {\n" +
 "  function bar() {\n" +
 "    var a = 1;\n" +
 "    x = a;\n" + // Dead assignment
 "  }\n" +
 "  return bar;\n" +
 "}";

 Node root = parseJs(jsCode);
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 NodeTraversal nodeTraversal = new NodeTraversal(root);
 deadAssignmentsElimination.process(null, root);
 NodeTraversal.traverse(compiler, root, nodeTraversal);
 String expectedCode = "function foo(x) {\n" +
 "  function bar() {\n" +
 "  }\n" +
 "  return bar;\n" +
 "}";
 assertCompilesTo(expectedCode, root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithReturnStatement() {
 String jsCode = "function foo(x) {\n" +
 "  var a = 1;\n" +
 "  return a;\n" +
 "  x = 3;\n" + // Dead assignment
 "}";

 Node root = parseJs(jsCode);
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 NodeTraversal nodeTraversal = new NodeTraversal(root);
 deadAssignmentsElimination.process(null, root);
 NodeTraversal.traverse(compiler, root, nodeTraversal);
 String expectedCode = "function foo(x) {\n" +
 "  var a = 1;\n" +
 "  return a;\n" +
 "}";
 assertCompilesTo(expectedCode, root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithComplexControlStructures() {
 String jsCode = "function foo(x) {\n" +
 "  if (y > 10) {\n" +
 "    if (z > 5) {\n" +
 "      var a = 1;\n" +
 "      x = a;\n" + // Dead assignment
 "      a = 2;\n" +
 "      if (x > 10) {\n" +
 "        x = 3;\n" +
 "      }\n" +
 "    }\n" +
 "  }\n" +
 "  return x;\n" +
 "}";

 Node root = parseJs(jsCode);
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 NodeTraversal nodeTraversal = new NodeTraversal(root);
 deadAssignmentsElimination.process(null, root);
 NodeTraversal.traverse(compiler, root, nodeTraversal);
 String expectedCode = "function foo(x) {\n" +
 "  if (y > 10) {\n" +
 "    if (z > 5) {\n" +
 "      if (x > 10) {\n" +
 "        x = 3;\n" +
 "      }\n" +
 "    }\n" +
 "  }\n" +
 "  return x;\n" +
 "}";
 assertCompilesTo(expectedCode, root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithLoop() {
 String jsCode = "function foo(x) {\n" +
 "  for (var i = 0; i < 10; i++) {\n" +
 "    var a = 1;\n" +
 "    x = a;\n" + // Dead assignment
 "  }\n" +
 "  return x;\n" +
 "}";

 Node root = parseJs(jsCode);
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 NodeTraversal nodeTraversal = new NodeTraversal(root);
 deadAssignmentsElimination.process(null, root);
 NodeTraversal.traverse(compiler, root, nodeTraversal);
 String expectedCode = "function foo(x) {\n" +
 "  for (var i = 0; i < 10; i++) {\n" +
 "  }\n" +
 "  return x;\n" +
 "}";
 assertCompilesTo(expectedCode, root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithMultipleAssignments() {
 String jsCode = "function foo(x) {\n" +
 "  var a = 1;\n" +
 "  var b = 2;\n" +
 "  x = a;\n" + // Dead assignment
 "  x = b;\n" +
 "  return x;\n" +
 "}";

 Node root = parseJs(jsCode);
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 NodeTraversal nodeTraversal = new NodeTraversal(root);
 deadAssignmentsElimination.process(null, root);
 NodeTraversal.traverse(compiler, root, nodeTraversal);
 String expectedCode = "function foo(x) {\n" +
 "  var a = 2;\n" +
 "  return a;\n" +
 "}";
 assertCompilesTo(expectedCode, root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithNestedAssignments() {
 String jsCode = "function foo(x) {\n" +
 "  var a = 1;\n" +
 "  var b = 2;\n" +
 "  x = a;\n" + // Dead assignment
 "  a = b;\n" +
 "  b = 3;\n" +
 "  return x;\n" +
 "}";

 Node root = parseJs(jsCode);
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 NodeTraversal nodeTraversal = new NodeTraversal(root);
 deadAssignmentsElimination.process(null, root);
 NodeTraversal.traverse(compiler, root, nodeTraversal);
 String expectedCode = "function foo(x) {\n" +
 "  var a = b;\n" +
 "  var b = 3;\n" +
 "  return a;\n" +
 "}";
 assertCompilesTo(expectedCode, root);
 }

 @Test
 public void testDeadAssignmentsEliminationWithReturnAssignment() {
 String jsCode = "function foo(x) {\n" +
 "  return x = 3;\n" + // Dead assignment
 "}";

 Node root = parseJs(jsCode);
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 NodeTraversal nodeTraversal = new NodeTraversal(root);
 deadAssignmentsElimination.process(null, root);
 NodeTraversal.traverse(compiler, root, nodeTraversal);
 String expectedCode = "return 3";
 assertCompilesTo(expectedCode, root);
 }

 @Test(expected = AssertionError.class)
 public void testDeadAssignmentsEliminationWithInvalidAssignment() {
 String jsCode = "function foo(x) {\n" +
 "  x = x + x;\n" + // Valid assignment
 "  x = x;\n" + // Dead assignment
 "}";

 Node root = parseJs(jsCode);
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 NodeTraversal nodeTraversal = new NodeTraversal(root);
 deadAssignmentsElimination.process(null, root);
 NodeTraversal.traverse(compiler, root, nodeTraversal);
 fail("Dead code elimination should have eliminated the dead assignment.");
 }

 @Test(expected = AssertionError.class)
 public void testDeadAssignmentsEliminationWithAssignmentInCondition() {
 String jsCode = "function foo(x) {\n" +
 "  if (x = 1) {\n" +
 "    return 1;\n" +
 "  }\n" +
 "  return 0;\n" +
 "}";

 Node root = parseJs(jsCode);
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 NodeTraversal nodeTraversal = new NodeTraversal(root);
 deadAssignmentsElimination.process(null, root);
 NodeTraversal.traverse(compiler, root, nodeTraversal);
 fail("Dead code elimination should not have changed the condition.");
 }

 @Test(expected = AssertionError.class)
 public void testDeadAssignmentsEliminationWithAssignmentInLoopCondition() {
 String jsCode = "function foo(x) {\n" +
 "  for (var i = 0; i = 1; i < 10) {\n" +
 "    return 1;\n" +
 "  }\n" +
 "}";

 Node root = parseJs(jsCode);
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);
 NodeTraversal nodeTraversal = new NodeTraversal(root);
 deadAssignmentsElimination.process(null, root);
 NodeTraversal.traverse(compiler, root, nodeTraversal);
 fail("Dead code elimination should not have changed the loop condition.");
 }

}