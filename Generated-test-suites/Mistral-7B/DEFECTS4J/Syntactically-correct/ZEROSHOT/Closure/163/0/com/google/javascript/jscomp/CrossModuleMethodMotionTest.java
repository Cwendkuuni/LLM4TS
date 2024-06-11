import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPassTest.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTest;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Tests for CrossModuleMethodMotion class
 */
public class CrossModuleMethodMotionTest {

  @SuppressWarnings("unchecked")
  @org.junit.Before
  public void setUp() {
    CompilerPass pass = new CrossModuleMethodMotion(new StubCompiler(),
        new CrossModuleMethodMotion.IdGenerator(), true);
    CompilerPassTest.setPass(pass);
  }

  @org.junit.Test
  public void testCrossModuleMethodMotion() {
    String sourceCode =
        "function Foo() {}"
        + "Foo.prototype.bar = function() { return 'foo'; };"
        + "function Baz() {}"
        + "Baz.prototype.bar = function() { return 'baz'; };";

    Node root = parse(sourceCode);
    AbstractCompiler compiler = new StubCompiler();
    compiler.compile(root, new JSModule("test"), null, null);

    Collection<JSModule> modules = compiler.getModules();
    JSModule fooModule = null;
    JSModule bazModule = null;

    for (JSModule module : modules) {
      if (module.getName().equals("Foo")) {
        fooModule = module;
      }
      if (module.getName().equals("Baz")) {
        bazModule = module;
      }
    }

    assertNotNull(fooModule);
    assertNotNull(bazModule);

    Node fooProto = findPrototype(fooModule, "Foo");
    Node bazProto = findPrototype(bazModule, "Baz");

    List<Node> fooMethods = new ArrayList<>();
    List<Node> bazMethods = new ArrayList<>();

    findChildrenOfType(fooProto, Node.CALL, fooMethods);
    findChildrenOfType(bazProto, Node.CALL, bazMethods);

    assertEquals(1, fooMethods.size());
    assertEquals(1, bazMethods.size());

    Node fooBar = fooMethods.get(0);
    Node bazBar = bazMethods.get(0);

    assertEquals("bar", fooBar.getFirstChild().getString());
    assertEquals("bar", bazBar.getFirstChild().getString());

    assertEquals(fooBar, bazBar);

    assertEquals("function () { return 'foo'; }", fooBar.toString());
    assertEquals("function () { return 'baz'; }", bazBar.toString());

    // Check that the methods are not equal to their originals in the prototype
    assertNotSame(fooProto, fooBar.getParent());
    assertNotSame(bazProto, bazBar.getParent());
  }

  private Node findPrototype(JSModule module, String name) {
    for (Node child : module.getTopLevelNodes()) {
      if (child.isObjectLiteral() && child.getFirstChild().getString().equals(name)) {
        return (Node) child.getFirstChild().getFirstChild();
      }
    }
    return null;
  }

  private void findChildrenOfType(Node node, int type, List<Node> result) {
    if (node == null) {
      return;
    }
    for (Node child : node.getChildren()) {
      if (child.getNodeType() == type) {
        result.add(child);
      }
      findChildrenOfType(child, type, result);
    }
  }
}