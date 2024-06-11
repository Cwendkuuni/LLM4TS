import static com.google.javascript.jscomp.CompilerPass.*;
import static com.google.javascript.rhino.NodeProperty.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private CompilerPass pass;
  private AbstractCompiler compiler;
  private IdGenerator idGenerator;
  private JSModuleGraph moduleGraph;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
    idGenerator = new IdGenerator();
    moduleGraph = compiler.getModuleGraph();
    pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
  }

  @Test
  public void testMoveMethodToDeeperModule() {
    String sourceCode =
        "class Foo {" +
        "  static function bar() {" +
        "    return 1;" +
        "  }" +
        "}" +
        "class Bar extends Foo {}" +
        "class Test {}";

    Node externs = Node.externalMember("Foo", "bar", NODE_FUNCTION, null);
    Node root = compiler.parseJavaScript(sourceCode, null);

    pass.process(externs, root);
    compiler.processRoot(root);

    Node bar = findNodeByQualifiedName(moduleGraph, "Bar.prototype.bar");
    assertTrue(bar.isFunction());
    assertEquals(
        "function JSCompiler_stubMethod(JSCompiler_stubMethod_id) {" +
        "  return function() {" +
        "    return JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(" +
        "        this, arguments);" +
        "  };" +
        "}",
        bar.getChild(0).toString());

    Node unstubbedBar = findNodeByQualifiedName(moduleGraph, "Foo.prototype.bar");
    assertTrue(unstubbedBar.isFunction());
  }

  @Test
  public void testMoveMethodToSameModule() {
    String sourceCode =
        "class Foo {" +
        "  static function bar() {" +
        "    return 1;" +
        "  }" +
        "  static function baz() {" +
        "    return 2;" +
        "  }" +
        "}" +
        "class Bar extends Foo {}";

    Node externs = Node.externalMember("Foo", "bar, baz", NODE_OBJECT, null);
    Node root = compiler.parseJavaScript(sourceCode, null);

    pass.process(externs, root);
    compiler.processRoot(root);

    Node bar = findNodeByQualifiedName(moduleGraph, "Bar.prototype.bar");
    assertTrue(bar.isFunction());
    assertEquals(
        "function Foo.bar() {" +
        "  return 1;" +
        "}",
        bar.toString());

    Node baz = findNodeByQualifiedName(moduleGraph, "Bar.prototype.baz");
    assertTrue(baz.isFunction());
    assertEquals(
        "function Foo.baz() {" +
        "  return 2;" +
        "}",
        baz.toString());
  }

  @Test
  public void testMoveMethodWithGetSet() {
    String sourceCode =
        "class Foo {" +
        "  static get foo() {" +
        "    return 1;" +
        "  }" +
        "  static set foo(value) {" +
        "    this.bar = value;" +
        "  }" +
        "  static function bar() {" +
        "    return 2;" +
        "  }" +
        "}" +
        "class Bar extends Foo {}";

    Node externs = Node.externalMember("Foo", "bar", NODE_FUNCTION, null);
    Node root = compiler.parseJavaScript(sourceCode, null);

    pass.process(externs, root);
    compiler.processRoot(root);

    Node fooGet = findNodeByQualifiedName(moduleGraph, "Bar.prototype.foo");
    assertTrue(fooGet.isGetProp());
    assertEquals(
        "function get Foo() {" +
        "  return 1;" +
        "}",
        fooGet.getChild(0).toString());

    Node fooSet = findNodeByQualifiedName(moduleGraph, "Bar.prototype.foo");
    assertTrue(fooSet.isSetterDef());
    assertEquals(
        "function set Foo(value) {" +
        "  this.bar = value;" +
        "}",
        fooSet.getChild(0).toString());

    Node bar = findNodeByQualifiedName(moduleGraph, "Bar.prototype.bar");
    assertTrue(bar.isFunction());
    assertEquals(
        "function Foo.bar() {" +
        "  return 2;" +
        "}",
        bar.toString());
  }

  @Test
  public void testMoveMethodToDeeperModuleWithError() {
    String sourceCode =
        "class Foo {" +
        "  static function bar() {" +
        "    return 1;" +
        "  }" +
        "}" +
        "class Bar {" +
        "  static function baz() {" +
        "    return Foo.bar();" +
        "  }" +
        "}" +
        "class Test {}";

    Node externs = Node.externalMember("Foo", "bar", NODE_FUNCTION, null);
    Node root = compiler.parseJavaScript(sourceCode, null);

    pass.process(externs, root);
    compiler.processRoot(root);

    try {
      compiler.processRoot(root);
      fail("Expected error to be thrown.");
    } catch (RuntimeException e) {
      logger.info("Expected error: " + e.getMessage());
    }
  }

  private Node findNodeByQualifiedName(JSModuleGraph moduleGraph, String name) {
    for (Node module : moduleGraph) {
      for (Node child : module) {
        if (child.isObject() && getProp(child, "name").getValue().equals(name)) {
          return child;
        }
        Node result = findNodeByQualifiedName(moduleGraph, name, child);
        if (result != null) {
          return result;
        }
      }
    }
    return null;
  }

  private Node findNodeByQualifiedName(JSModuleGraph moduleGraph, String name, Node start) {
    if (start.isObject() && getProp(start, "name").getValue().equals(name)) {
      return start;
    }

    for (Node child : start.getChildren()) {
      Node result = findNodeByQualifiedName(moduleGraph, name, child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}