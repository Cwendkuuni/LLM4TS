import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {

  private static class TestMethodCompilerPass extends MethodCompilerPass {

    private SignatureStore signatureStore = new SignatureStore() {
      private Multimap<String, Node> actualSignatures = HashMultimap.create();

      @Override
      public void reset() {
        actualSignatures.clear();
      }

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        actualSignatures.put(functionName, functionNode);
      }

      @Override
      public void removeSignature(String functionName) {
        actualSignatures.remove(functionName);
      }

      public Multimap<String, Node> getActualSignatures() {
        return actualSignatures;
      }
    };

    @Override
    abstract Callback getActingCallback();

    @Override
    SignatureStore getSignatureStore() {
      return signatureStore;
    }

    @Override
    Callback getActingCallback() {
      return new Callback() {
        @Override
        public void processNode(NodeTraversal nodeTraversal, Node currentNode) {
          // No-op
        }
      };
    }
  }

  @ org.junit.Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler() {
      public Node parse(String input, String sourceFileName) {
        return null;
      }

      public NodeTraversal createTraverser(Node root, Node externs) {
        return new NodeTraversal(root, externs, null);
      }
    };

    testPass = new TestMethodCompilerPass(compiler);
  }

  private TestMethodCompilerPass testPass;

  @org.junit.Test
  public void testProcessExterns() {
    String externsSource =
        "var Foo = {\n" +
        "  bar: function() {},\n" +
        "  baz: function() {}\n" +
        "};";

    Node externs = testPass.parse(externsSource, "externs.js");

    testPass.process(externs, null);

    assertEquals(2, testPass.externMethods.size());
    assertEquals("bar", testPass.externMethods.iterator().next());
    assertEquals("baz", testPass.externMethods.iterator().next());

    Multimap<String, Node> actualSignatures = testPass.getSignatureStore().getActualSignatures();
    assertEquals(2, actualSignatures.size());

    Node barSignature = actualSignatures.get("bar").iterator().next();
    Node bazSignature = actualSignatures.get("baz").iterator().next();

    assertSame(Token.FUNCTION, barSignature.getType());
    assertSame(Token.FUNCTION, bazSignature.getType());

    assertNull(testPass.getSignatureStore().getSignature(TestMethodCompilerPassTest.this.getClass().getSimpleName() + ".testMethod"));
  }

  @org.junit.Test
  public void testProcessSource() {
    String sourceCode =
        "function Foo() {\n" +
        "  this.bar = function() {};\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.baz = function() {};";

    Node root = testPass.parse(sourceCode, "test.js");

    testPass.process(null, root);

    assertEquals(2, testPass.externMethods.size());
    assertEquals("bar", testPass.externMethods.iterator().next());
    assertEquals("baz", testPass.externMethods.iterator().next());

    Multimap<String, Node> actualSignatures = testPass.getSignatureStore().getActualSignatures();
    assertEquals(2, actualSignatures.size());

    Node barFunction = actualSignatures.get("bar").iterator().next();
    Node prototypeBazFunction = actualSignatures.get("baz").get(0);
    Node staticBazFunction = actualSignatures.get("baz").get(1);

    assertSame(Token.FUNCTION, barFunction.getType());
    assertSame(Token.FUNCTION, prototypeBazFunction.getType());
    assertSame(Token.FUNCTION, staticBazFunction.getType());

    assertNull(testPass.getSignatureStore().getSignature(TestMethodCompilerPassTest.this.getClass().getSimpleName() + ".testMethod"));
  }

  @org.junit.Test
  public void testIgnoreExternMethodsWithoutSignatures() {
    String externsSource =
        "var Foo = {\n" +
        "  bar: function() {},\n" +
        "  baz:\n" +
        "};";

    Node externs = testPass.parse(externsSource, "externs.js");

    testPass.process(externs, null);

    assertEquals(1, testPass.externMethods.size());
    assertEquals("bar", testPass.externMethods.iterator().next());

    Multimap<String, Node> actualSignatures = testPass.getSignatureStore().getActualSignatures();
    assertEquals(0, actualSignatures.size());

    assertNull(testPass.getSignatureStore().getSignature(TestMethodCompilerPassTest.this.getClass().getSimpleName() + ".testMethod"));
  }

  @org.junit.Test
  public void testMethodDefinitions() {
    String sourceCode =
        "function Foo() {\n" +
        "  this.bar = function() {};\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.baz = function() {};\n" +
        "\n" +
        "function Bar() {\n" +
        "  this.qux = function() {};\n" +
        "}";

    Node root = testPass.parse(sourceCode, "test.js");

    testPass.process(null, root);

    Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
    expectedMethodDefinitions.put("Foo.bar", testPass.methodDefinitions.get("Foo.bar"));
    expectedMethodDefinitions.put("Foo.prototype.baz", testPass.methodDefinitions.get("Foo.prototype.baz"));
    expectedMethodDefinitions.put("Bar.qux", testPass.methodDefinitions.get("Bar.qux"));

    Multimap<String, Node> actualMethodDefinitions = testPass.methodDefinitions;

    assertEquals(expectedMethodDefinitions.size(), actualMethodDefinitions.size());

    for (String functionName : expectedMethodDefinitions.keySet()) {
      assertSame(expectedMethodDefinitions.get(functionName).iterator().next(), actualMethodDefinitions.get(functionName).iterator().next());
    }
  }

  @org.junit.Test
  public void testNonMethodProperties() {
    String sourceCode = "function Foo() { this.bar = 42; }";

    Node root = testPass.parse(sourceCode, "test.js");

    testPass.process(null, root);

    assertTrue(testPass.nonMethodProperties.contains("bar"));
  }

  @org.junit.Test
  public void testExternMethodsWithSignatures() {
    String externsSource =
        "var Foo = {\n" +
        "  bar: function(x: number) {},\n" +
        "  baz: function(x: number, y: number) {}\n" +
        "};";

    Node externs = testPass.parse(externsSource, "externs.js");

    testPass.process(externs, null);

    assertEquals(2, testPass.externMethods.size());
    assertEquals("bar", testPass.externMethods.iterator().next());
    assertEquals("baz", testPass.externMethods.iterator().next());

    Multimap<String, Node> actualSignatures = testPass.getSignatureStore().getActualSignatures();
    assertEquals(2, actualSignatures.size());

    Node barFunction = actualSignatures.get("bar").iterator().next();
    Node prototypeBazFunction = actualSignatures.get("baz").get(0);
    Node staticBazFunction = actualSignatures.get("baz").get(1);

    List<Node> barParameters = Lists.newArrayList(barFunction.getFirstChild().getFirstChild());
    List<Node> bazParameters = Lists.newArrayList(
        prototypeBazFunction.getFirstChild().getFirstChild(),
        prototypeBazFunction.getFirstChild().getNext().getFirstChild()
    );

    assertEquals(1, barParameters.size());
    assertEquals(Token.IDENT, barParameters.get(0).getType());
    assertEquals("x", barParameters.get(0).getString());

    assertEquals(2, bazParameters.size());

    assertEquals(Token.IDENT, bazParameters.get(0).getType());
    assertEquals("x", bazParameters.get(0).getString());

    assertEquals(Token.IDENT, bazParameters.get(1).getType());
    assertEquals("y", bazParameters.get(1).getString());
  }

  @org.junit.Test
  public void testNoSignatures() {
    String externsSource = "var Foo = { bar: function(), baz: function() {} };";

    Node externs = testPass.parse(externsSource, "externs.js");

    testPass.process(externs, null);

    assertFalse(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.contains("bar"));
    assertTrue(testPass.externMethodsWithoutSignatures.contains("baz"));

    Multimap<String, Node> actualSignatures = testPass.getSignatureStore().getActualSignatures();
    assertEquals(0, actualSignatures.size());
  }
}