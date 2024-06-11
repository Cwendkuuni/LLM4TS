import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.rhino.NodeTree.isAssign;
import static com.google.javascript.rhino.NodeTree.isGetProp;
import static com.google.javascript.rhino.NodeTree.isVar;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Map;

public class InlineObjectLiteralsTest {

  private static Compiler compiler;
  private static CompilerPass pass;

  @org.junit.BeforeClass
  public static void setUp() {
    compiler = new Compiler();
    pass = new InlineObjectLiterals(compiler, new DummyNameIdSupplier());
  }

  @org.junit.Test
  public void testInlineObjectLiterals_Simple() {
    String js = "function f() {" +
                "  var x = {" +
                "    a: 1," +
                "    b: 2" +
                "  };" +
                "  var y = x.a;" +
                "  return y;" +
                "}";

    Node root = compiler.parseTree(js);
    pass.process(null, root);

    Node f = NodeUtil.findFunction(root, "f");
    Node body = f.getFirstChild();

    // The assignment to x should have been replaced with individual assignments.
    Node xAssign = NodeUtil.findVariableAssignment(body, "x");
    assertFalse(xAssign instanceof Assign);

    // The variable y should have been replaced with a single variable.
    Node y = NodeUtil.findVariable(body, "y");
    assertTrue(isVar(y));

    // The return statement should still work correctly.
    Node returnNode = NodeUtil.findReturnStatement(body);
    Node value = returnNode.getFirstChild();
    assertTrue(isVar(value));

    // Make sure that the variable assignments still evaluate to the same value.
    Node x = NodeUtil.findVariable(body, "x");
    Node a = NodeUtil.findVariable(body, "a");
    Node assign = NodeUtil.findAssignment(body, a);
    Node valueNode = assign.getLastChild();
    Node xValue = x.getFirstChild();
    assertTrue(NodeUtil.isNodeEquivalent(valueNode, xValue));
  }

  @org.junit.Test
  public void testInlineObjectLiterals_SelfReference() {
    String js = "function f() {" +
                "  var x = {" +
                "    a: function() {" +
                "      return this.b;" +
                "    }," +
                "    b: x.a" +
                "  };" +
                "  return x.a();" +
                "}";

    Node root = compiler.parseTree(js);
    pass.process(null, root);

    Node f = NodeUtil.findFunction(root, "f");
    Node body = f.getFirstChild();

    // The assignment to x.b should have resulted in an error.
    Node bRef = NodeUtil.findGetProp(body, "x", "b");
    Node x = NodeUtil.findVariable(body, "x");
    Node xAssign = NodeUtil.findAssignment(body, x);
    Node aRef = NodeUtil.findGetProp(body, x, "a");
    Node aNode = aRef.getLastChild();
    Node bNode = bRef.getLastChild();
    Node xValue = xAssign.getLastChild();
    Node aValue = aNode.getLastChild();

    // Check that the self reference didn't make it through.
    assertFalse(NodeUtil.isNodeEquivalent(bNode, aValue));

    // Make sure that the function still works correctly.
    Node returnNode = NodeUtil.findReturnStatement(body);
    Node value = returnNode.getFirstChild();
    assertTrue(isFunction(value));
  }

  @org.junit.Test
  public void testInlineObjectLiterals_DeepObject() {
    String js = "function f() {" +
                "  var x = {" +
                "    a: {" +
                "      b: 1," +
                "      c: {" +
                "        d: 2" +
                "      }" +
                "    }," +
                "    e: 3" +
                "  };" +
                "  var y = x.a.c.d;" +
                "  return y;" +
                "}";

    Node root = compiler.parseTree(js);
    pass.process(null, root);

    Node f = NodeUtil.findFunction(root, "f");
    Node body = f.getFirstChild();

    // The assignment to x should have been replaced with individual assignments.
    Node xAssign = NodeUtil.findVariableAssignment(body, "x");
    assertFalse(xAssign instanceof Assign);

    // The variable y should have been replaced with a single variable.
    Node y = NodeUtil.findVariable(body, "y");
    assertTrue(isVar(y));

    // The return statement should still work correctly.
    Node returnNode = NodeUtil.findReturnStatement(body);
    Node value = returnNode.getFirstChild();
    assertTrue(isNumber(value));
  }

  @org.junit.Test
  public void testInlineObjectLiterals_NoAssignment() {
    String js = "function f() {" +
                "  var x = {" +
                "    a: 1," +
                "    b: 2" +
                "  }";

    Node root = compiler.parseTree(js);
    pass.process(null, root);

    Node f = NodeUtil.findFunction(root, "f");
    Node body = f.getFirstChild();

    // The assignment to x should not be present.
    Node xAssign = NodeUtil.findVariableAssignment(body, "x");
    assertTrue(xAssign == null);
  }

  @org.junit.Test
  public void testInlineObjectLiterals_MultipleAssignments() {
    String js = "function f() {" +
                "  var x = {" +
                "    a: 1," +
                "    b: 2" +
                "  };" +
                "  x.a = 3;" +
                "  x.b = 4;" +
                "  var y = x.a;" +
                "  return y;" +
                "}";

    Node root = compiler.parseTree(js);
    pass.process(null, root);

    Node f = NodeUtil.findFunction(root, "f");
    Node body = f.getFirstChild();

    // The assignments to x.a and x.b should have been replaced with
    // individual assignments.
    Node xAssign = NodeUtil.findVariableAssignment(body, "x");
    assertFalse(xAssign instanceof Assign);

    // The variable y should have been replaced with a single variable.
    Node y = NodeUtil.findVariable(body, "y");
    assertTrue(isVar(y));

    // The return statement should still work correctly.
    Node returnNode = NodeUtil.findReturnStatement(body);
    Node value = returnNode.getFirstChild();
    assertTrue(isNumber(value));
  }

  @org.junit.Test
  public void testInlineObjectLiterals_VariableReused() {
    String js = "function f() {" +
                "  var x = 1;" +
                "  var x = {" +
                "    a: 2" +
                "  };" +
                "  return x.a;" +
                "}";

    Node root = compiler.parseTree(js);
    pass.process(null, root);

    Node f = NodeUtil.findFunction(root, "f");
    Node body = f.getFirstChild();

    // The first variable assignment should have been replaced.
    Node xAssign = NodeUtil.findVariableAssignment(body, "x");
    assertFalse(xAssign instanceof Assign);

    // The variable x should still be accessible with its original name.
    Node xVar = NodeUtil.findVariable(body, "x");
    assertTrue(isVar(xVar));

    // The return statement should still work correctly.
    Node returnNode = NodeUtil.findReturnStatement(body);
    Node value = returnNode.getFirstChild();
    assertTrue(isNumber(value));
  }

  private static NodeUtil nodeUtil = new NodeUtil();
  private static Map<String, Node> findVariable(Node node, String name) {
    return nodeUtil.findVariables(node, name).get(0);
  }

  private static Node findGetProp(Node node, String varName, String propName) {
    List<Node> list = nodeUtil.findVariableGetProperties(node, varName);
    for (Node n : list) {
      Node prop = n.getLastChild();
      if (prop.getString().equals(propName)) {
        return n;
      }
    }
    return null;
  }

  private static Node findAssignment(Node node, String varName) {
    List<Node> list = nodeUtil.findVariableAssignments(node, varName);
    return list.isEmpty() ? null : list.get(0);
  }

  private static Node findFunction(Node node, String name) {
    List<Node> list = nodeUtil.findFunctions(node, name);
    return list.isEmpty() ? null : list.get(0);
  }

  private static List<Node> findVariables(Node node, String name) {
    return nodeUtil.findVariables(node, name);
  }

  private static List<Node> findVariableGetProperties(Node node, String name) {
    return nodeUtil.findVariableGetProperties(node, name);
  }

  private static List<Node> findVariableAssignments(Node node, String name) {
    return nodeUtil.findVariableAssignments(node, name);
  }

  private static List<Node> findFunctions(Node node, String name) {
    return nodeUtil.findFunctions(node, name);
  }

  private static boolean isVar(Node node) {
    return node instanceof Var;
  }

  private static boolean isAssign(Node node) {
    return node instanceof Assign;
  }

  private static boolean isFunction(Node node) {
    return node instanceof Function;
  }

  private static boolean isNumber(Node node) {
    return node instanceof NumberLiteral;
  }

  @org.junit.AfterClass
  public static void tearDown() {
    compiler = null;
    pass = null;
  }
}

class DummyNameIdSupplier implements Supplier<String> {
  private int id = 0;

  @Override
  public String get() {
    return "dummy_" + ++id;
  }
}