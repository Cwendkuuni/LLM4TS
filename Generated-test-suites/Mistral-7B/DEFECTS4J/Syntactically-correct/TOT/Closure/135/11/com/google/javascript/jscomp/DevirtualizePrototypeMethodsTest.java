import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.NodeType.*;

public class DevirtualizePrototypeMethodsTest {

  private static final String CLASS_NAME = "DevirtualizePrototypeMethods";
  private AbstractCompiler compiler;

  @org.junit.Before
  public void setup() {
    compiler = new AbstractCompiler();
  }

  @Test
  public void testDevirtualizePrototypeMethods() {
    String testSource = "" +
        "var a = { prototype: { accumulate: function(value) { this.total += value; return this.total } } };" +
        "var b = { prototype: { accumulate: function() { return this.total * 2 } } };" +
        "a.total = 0; b.total = 0;" +
        "a.accumulate(1);" +
        "assertEquals(1, a.total);" +
        "assertEquals(0, b.total);" +
        "b.accumulate(1);" +
        "assertEquals(2, a.total);" +
        "assertEquals(2, b.total);";

    Node root = Parser.parse(testSource);
    compiler.process(null, root);
    Evaluator evaluator = new Evaluator(compiler);
    evaluator.setCurrentScope(root);
    evaluator.evaluate(root);

    Node aAccumulate = findMethod(root, "a", "prototype", "accumulate");
    Node bAccumulate = findMethod(root, "b", "prototype", "accumulate");

    // Check that method definitions were transformed
    assertEquals(EXPR_ASSIGN, aAccumulate.getType());
    assertEquals(EXPR_ASSIGN, bAccumulate.getType());
    assertEquals(CALL_EXPR, aAccumulate.getLastChild().getType());
    assertEquals(CALL_EXPR, bAccumulate.getLastChild().getType());
    assertEquals("JSCompiler_StaticMethods_accumulate",
        aAccumulate.getLastChild().getFirstChild().getString());
    assertEquals("JSCompiler_StaticMethods_accumulate",
        bAccumulate.getLastChild().getFirstChild().getString());

    // Check that method calls were transformed
    Node aCall = findCall(root, "a.accumulate", 2);
    Node bCall = findCall(root, "b.accumulate", 1);

    assertEquals(CALL_EXPR, aCall.getType());
    assertEquals(CALL_EXPR, bCall.getType());
    assertEquals("JSCompiler_StaticMethods_accumulate",
        aCall.getLastChild().getFirstChild().getString());
    assertEquals("JSCompiler_StaticMethods_accumulate",
        bCall.getLastChild().getFirstChild().getString());
    assertEquals(EXPR_ASSIGN, aCall.getLastChild().getLastChild().getType());
    assertEquals(EXPR_IDENT, bCall.getLastChild().getLastChild().getType());
    assertEquals("a", aCall.getLastChild().getLastChild().getFirstChild().getString());
    assertEquals("b", bCall.getLastChild().getLastChild().getFirstChild().getString());

    // Check transformation does not affect simple variable assignments
    Node simpleAssignment = findAssignment(root, "x = 1");
    assertEquals(EXPR_ASSIGN, simpleAssignment.getType());
  }

  private Node findMethod(Node node, String objName, String propName, String methodName) {
    Node obj = findIdent(node, objName);
    Node method = null;
    for (Node child = obj.getFirstChild(); child != null; child = child.getNext()) {
      if (getPropName(child).equals(propName)) {
        method = getChild(child, 1);
        if (method.getLastChild().getString().equals(methodName)) {
          return method;
        }
      }
    }
    fail("Method not found: " + objName + "." + propName + "." + methodName);
    return null;
  }

  private Node findCall(Node node, String methodName, int argumentCount) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.getType() == CALL_EXPR) {
        Node callee = child.getFirstChild();
        if (callee.getType() == GET_PROP) {
          String name = callee.getLastChild().getString();
          Node method = findMethod(node, name.substring(0, name.lastIndexOf(".")),
              name.substring(0, name.lastIndexOf(".")), methodName);
          if (method != null && method.getLastChild().getLastChildCount() == argumentCount) {
            return child;
          }
        }
      }
    }
    fail("Call site not found: " + methodName + "(" + argumentCount + ")");
    return null;
  }

  private Node findAssignment(Node node, String assignmentString) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.getType() == EXPR_ASSIGN) {
        if (child.getLastChild().toString().equals(assignmentString)) {
          return child;
        }
      }
    }
    fail("Assignment not found: " + assignmentString);
    return null;
  }

  private Node findIdent(Node node, String name) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.getType() == EXPR_IDENT && child.getString().equals(name)) {
        return child;
      }
    }
    fail("Identifier not found: " + name);
    return null;
  }

  private Node getPropName(Node node) {
    return node.getFirstChild();
  }

  private Node getChild(Node node, int index) {
    Node current = node;
    for (int i = 0; i < index; i++) {
      current = current.getNext();
    }
    return current;
  }
}