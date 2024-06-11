import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.SourceName;

import java.util.List;
import java.util.Set;

/**
 * Tests for the MethodCompilerPass class.
 */
public class MethodCompilerPassTest {
  private static class TestCompiler extends AbstractCompiler {
    @Override
    public CompilerPass getPass(String passName) {
      if (MethodCompilerPass.class.isInstance(passName)) {
        return new TestMethodCompilerPass(this);
      }
      return super.getPass(passName);
    }
  }

  /** Test method compiler pass */
  private static class TestMethodCompilerPass
      extends MethodCompilerPass {

    TestMethodCompilerPass(AbstractCompiler c) {
      super(c);
    }

    @Override
    SignatureStore getSignatureStore() {
      return new SignatureStore() {
        private Set<String> signatures = Sets.newHashSet();

        @Override
        public void reset() {
          signatures.clear();
        }

        @Override
        public void addSignature(
            String functionName, Node functionNode, String sourceFile) {
          signatures.add(functionName);
        }

        @Override
        public void removeSignature(String functionName) {
          signatures.remove(functionName);
        }

        public boolean hasSignature(String functionName) {
          return signatures.contains(functionName);
        }
      };
    }

    @Override
    Callback getActingCallback() {
      return new Callback() {
        @Override
        public void call(NodeTraversal t) {
          // Do nothing in this test
        }
      };
    }
  }

  @Test
  public void testSimpleMethodDeclaration() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree("function foo() {}");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().contains("foo"));
    assertEquals(1, pass.methodDefinitions.get("foo").size());
  }

  @Test
  public void testMethodInObjectLiteral() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree(
        "var obj = { getBar: function() {} };");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().isEmpty());
    assertEquals(1, pass.methodDefinitions.get("getBar").size());
  }

  @Test
  public void testExternMethodDeclaration() {
    TestCompiler compiler = new TestCompiler();
    Node externs = Node.createTree(
        "function foo() {};" +
        "var obj = { getBar: function() {} };");
    Node root = Node.createTree("function baz() {}");
    compiler.compile(externs, root, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(externs, root);

    assertTrue(pass.getExternMethods().contains("foo"));
    assertTrue(pass.getExternMethods().contains("getBar"));
    assertEquals(1, pass.methodDefinitions.get("foo").size());
    assertEquals(1, pass.methodDefinitions.get("getBar").size());
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedMethod() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree("var foo = undefined;");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    fail("Expected IllegalStateException");
  }

  @Test
  public void testPrototypeMethod() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree(
        "function Foo() {}" +
        "Foo.prototype.getBar = function() {};");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().isEmpty());
    assertEquals(1, pass.methodDefinitions.get("getBar").size());
  }

  @Test
  public void testPrototypeAssignment() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree(
        "function Foo() {}" +
        "Foo.prototype = { getBar: function() {} };");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().isEmpty());
    assertEquals(1, pass.methodDefinitions.get("getBar").size());
  }

  @Test
  public void testPrototypeProperty() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree(
        "function Foo() {}" +
        "Foo.prototype.bar = function() {};");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().isEmpty());
    assertFalse(pass.methodDefinitions.containsKey("bar"));
  }

  @Test
  public void testFunctionWithNoArguments() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree("function foo() {}");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().contains("foo"));
    assertEquals(1, pass.methodDefinitions.get("foo").size());
  }

  @Test
  public void testFunctionWithArguments() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree("function foo(x, y) {}");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().contains("foo"));
    assertEquals(1, pass.methodDefinitions.get("foo").size());
  }

  @Test
  public void testExternMethodWithNoSignature() {
    TestCompiler compiler = new TestCompiler();
    Node externs = Node.createTree(
        "function foo();");
    Node root = Node.createTree("function baz() {}");
    compiler.compile(externs, root, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(externs, root);

    assertTrue(pass.getExternMethods().contains("foo"));
    assertFalse(pass.methodDefinitions.containsKey("foo"));
  }

  @Test
  public void testExternMethodWithSignature() {
    TestCompiler compiler = new TestCompiler();
    Node externs = Node.createTree(
        "function foo(x) {};");
    Node root = Node.createTree("function baz(x, y) {}");
    compiler.compile(externs, root, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(externs, root);

    assertTrue(pass.getExternMethods().contains("foo"));
    assertEquals(1, pass.methodDefinitions.get("foo").size());
  }

  @Test
  public void testMethodWithRestParameter() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree("function foo(...args) {}");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().isEmpty());
    assertEquals(1, pass.methodDefinitions.get("foo").size());
  }

  @Test
  public void testMethodWithDefaultArguments() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree("function foo(x = 1) {}");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().isEmpty());
    assertEquals(1, pass.methodDefinitions.get("foo").size());
  }

  @Test
  public void testMethodWithVariadicAndDefaultArguments() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree("function foo(x, ...args) { x++; }");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().isEmpty());
    assertEquals(1, pass.methodDefinitions.get("foo").size());
  }

  @Test
  public void testPropertyThatIsNotAMethod() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree("var foo = 1;");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertFalse(pass.getExternMethods().contains("foo"));
    assertEquals(0, pass.methodDefinitions.get("foo").size());
  }

  @Test
  public void testMethodInAnonymousFunction() {
    TestCompiler compiler = new TestCompiler();
    Node root = Node.createTree("var foo = function() { return function bar() {} };");
    compiler.compile(root, null, null);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().isEmpty());
    assertEquals(1, pass.methodDefinitions.get("bar").size());
  }

  @Test
  public void testMethodInExportedModule() {
    TestCompiler compiler = new TestCompiler();
    String source = "export function foo() {}";
    SourceName sourceName = new SourceName("test.js", "/test.js");
    Node root = Node.createTreeFromString(source, sourceName);
    compiler.compile(null, root, sourceName);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().contains("foo"));
    assertEquals(1, pass.methodDefinitions.get("foo").size());
  }

  @Test
  public void testMethodInImportedModule() {
    TestCompiler compiler = new TestCompiler();
    String source = "import { foo } from './test.js';";
    SourceName sourceName = new SourceName("main.js", "/main.js");
    Node root = Node.createTreeFromString(source, sourceName);
    compiler.compile(null, root, sourceName);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().isEmpty());
    assertEquals(1, pass.methodDefinitions.get("foo").size());
  }

  @Test
  public void testMethodInClass() {
    TestCompiler compiler = new TestCompiler();
    String source = "class Foo {\n" +
                    "  constructor() {}\n" +
                    "  method() {}\n" +
                    "}";
    SourceName sourceName = new SourceName("test.js", "/test.js");
    Node root = Node.createTreeFromString(source, sourceName);
    compiler.compile(null, root, sourceName);
    MethodCompilerPass pass = compiler.getPass(MethodCompilerPass.class);
    pass.process(null, root);

    assertTrue(pass.getExternMethods().isEmpty());
    assertEquals(1, pass.methodDefinitions.get("method").size());
  }
}