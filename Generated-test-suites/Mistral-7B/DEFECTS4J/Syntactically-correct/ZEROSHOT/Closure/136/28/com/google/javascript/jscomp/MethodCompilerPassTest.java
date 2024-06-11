import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.tests.BaseTest;

public class MethodCompilerPassTest extends BaseTest {

  private static class TestMethodCompilerPass extends MethodCompilerPass {

    @Override
    SignatureStore getSignatureStore() {
      return new SignatureStorage();
    }

    @Override
    Callback getActingCallback() {
      return new DoNothingCallback();
    }

    private static class SignatureStorage implements SignatureStore {
      private Multimap<String, Node> signatures = HashMultimap.create();

      @Override
      public void reset() {}

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        signatures.put(functionName, functionNode);
      }

      @Override
      public void removeSignature(String functionName) {
        signatures.remove(functionName);
      }

      public Node getSignature(String functionName) {
        return signatures.get(functionName).get(0);
      }
    }

    private static class DoNothingCallback implements Callback {
      @Override
      public void call(NodeTraversal traverser, Node node, Node parent) {}
    }

  }

  private CompilerOptions options;
  private AbstractCompiler compiler;
  private TestMethodCompilerPass testPass;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    options = new CompilerOptions();
    compiler = new Compiler(options);
    testPass = new TestMethodCompilerPass(compiler);
  }

  @org.junit.Test
  public void testEmpty() {
    Node root = parseJs("function foo() {}");
    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(0, testPass.methodDefinitions.size());
  }

  @org.junit.Test
  public void testExterns() {
    Node externs = parseJs("function foo() {} function bar() {}");

    Node root = parseJs("function test() {}");

    testPass.process(externs, root);

    assertTrue(testPass.externMethods.contains("foo"));
    assertTrue(testPass.externMethods.contains("bar"));
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(0, testPass.methodDefinitions.size());
  }

  @org.junit.Test
  public void testExternsWithSignatures() {
    Node externs = parseJs(
        "function foo(a) {} function bar(a, b) {} function baz() {}");

    Node root = parseJs("function test() {}");

    testPass.process(externs, root);

    assertTrue(testPass.externMethods.contains("foo"));
    assertTrue(testPass.externMethods.contains("bar"));
    assertFalse(testPass.externMethods.contains("baz"));
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(2, testPass.methodDefinitions.size());

    Node fooSignature = testPass.methodDefinitions.get("foo").get(0);
    assertEquals(2, fooSignature.getNumChildren());
    assertEquals(Token.FUNCTION, fooSignature.getType());

    Node barSignature = testPass.methodDefinitions.get("bar").get(0);
    assertEquals(3, barSignature.getNumChildren());
    assertEquals(Token.FUNCTION, barSignature.getType());
  }

  @org.junit.Test
  public void testExternsWithUnsignedMethods() {
    Node externs = parseJs("function foo() {} function bar() {}");

    Node root = parseJs("function test() {} foo();");

    testPass.process(externs, root);

    assertTrue(testPass.externMethods.contains("foo"));
    assertTrue(testPass.externMethodsWithoutSignatures.contains("foo"));
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(0, testPass.methodDefinitions.size());
  }

  @org.junit.Test
  public void testNonMethodProperty() {
    Node root = parseJs("var x = 42;");

    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.contains("x"));
    assertEquals(0, testPass.methodDefinitions.size());
  }

  @org.junit.Test
  public void testNonExistentMethod() {
    Node root = parseJs("function foo() {} fooBar();");

    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(0, testPass.methodDefinitions.size());

    Node fooSignature = testPass.methodDefinitions.get("foo").get(0);
    assertEquals(1, fooSignature.getNumChildren());
    assertEquals(Token.FUNCTION, fooSignature.getType());

    assertNull(testPass.getSignatureStore().getSignature("fooBar"));
  }

  @org.junit.Test
  public void testPrototypes() {
    Node root = parseJs(
        "function Foo() { this.bar = function() {}} Foo.prototype.getBar = function() {};");

    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(1, testPass.methodDefinitions.size());

    Node prototypeSignature = testPass.methodDefinitions.get("getBar").get(0);
    assertEquals(1, prototypeSignature.getNumChildren());
    assertEquals(Token.FUNCTION, prototypeSignature.getType());

    Node prototype = prototypeSignature.getFirstChild();
    assertEquals("getBar", prototype.getString());
  }

  @org.junit.Test
  public void testPrototypesWithExtern() {
    Node externs = parseJs("function Foo() {} function Foo.prototype.getBar() {}");

    Node root = parseJs(
        "function Foo() { this.bar = function() {}} Foo.prototype.getBar = function() {};");

    testPass.process(externs, root);

    assertTrue(testPass.externMethods.contains("Foo"));
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(1, testPass.methodDefinitions.size());

    Node prototypeSignature = testPass.methodDefinitions.get("getBar").get(0);
    assertEquals(1, prototypeSignature.getNumChildren());
    assertEquals(Token.FUNCTION, prototypeSignature.getType());

    Node prototype = prototypeSignature.getFirstChild();
    assertEquals("getBar", prototype.getString());
  }

  @org.junit.Test
  public void testPrototypeWithMissingPrototype() {
    Node root = parseJs(
        "function Foo() { this.bar = function() {}} Foo.prototype.getBar = function() {}; "
        + "Foo.prototype = {};");

    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(0, testPass.methodDefinitions.size());
  }

  @org.junit.Test
  public void testPrototypeWithMissingMethod() {
    Node root = parseJs(
        "function Foo() { this.bar = function() {}} Foo.prototype.getBar = function() {}; "
        + "Foo.prototype.getBaz = function() {};");

    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(1, testPass.methodDefinitions.size());

    Node barSignature = testPass.methodDefinitions.get("getBar").get(0);
    assertEquals(1, barSignature.getNumChildren());
    assertEquals(Token.FUNCTION, barSignature.getType());

    assertNull(testPass.getSignatureStore().getSignature("getBaz"));
  }

  @org.junit.Test
  public void testPrototypeWithMissingPrototypeAndMethod() {
    Node root = parseJs(
        "function Foo() { this.bar = function() {}} Foo.prototype.getBar = function() {}; "
        + "Foo.prototype = {}; Foo.prototype.getBaz = function() {};");

    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(0, testPass.methodDefinitions.size());
  }

  @org.junit.Test
  public void testPrototypeWithMissingPrototypeAndMultipleMethods() {
    Node root = parseJs(
        "function Foo() { this.bar = function() {}} Foo.prototype.getBar = function() {}; "
        + "Foo.prototype = {}; Foo.prototype.getBar = function() {}; "
        + "Foo.prototype.getBaz = function() {};");

    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(0, testPass.methodDefinitions.size());
  }

  @org.junit.Test
  public void testPrototypeWithPrototypeAndMissingMethod() {
    Node root = parseJs(
        "function Foo() { this.bar = function() {}} Foo.prototype = {"
        + "getBar: function() {}, getBaz: function() {}};");

    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(1, testPass.methodDefinitions.size());

    Node getBarSignature = testPass.methodDefinitions.get("getBar").get(0);
    assertEquals(1, getBarSignature.getNumChildren());
    assertEquals(Token.FUNCTION, getBarSignature.getType());

    Node getBarPrototype = getBarSignature.getFirstChild();
    assertEquals("getBar", getBarPrototype.getString());

    assertNull(testPass.getSignatureStore().getSignature("getBaz"));
  }

  @org.junit.Test
  public void testPrototypeWithPrototypeAndMultipleMethods() {
    Node root = parseJs(
        "function Foo() { this.bar = function() {}} Foo.prototype = {"
        + "getBar: function() {}, getBaz: function() {}};");

    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(2, testPass.methodDefinitions.size());

    Node getBarSignature = testPass.methodDefinitions.get("getBar").get(0);
    assertEquals(1, getBarSignature.getNumChildren());
    assertEquals(Token.FUNCTION, getBarSignature.getType());

    Node getBarPrototype = getBarSignature.getFirstChild();
    assertEquals("getBar", getBarPrototype.getString());

    Node getBazSignature = testPass.methodDefinitions.get("getBaz").get(0);
    assertEquals(1, getBazSignature.getNumChildren());
    assertEquals(Token.FUNCTION, getBazSignature.getType());

    Node getBazPrototype = getBazSignature.getFirstChild();
    assertEquals("getBaz", getBazPrototype.getString());
  }

  @org.junit.Test
  public void testPrototypeWithPrototypeAndMissingPrototype() {
    Node root = parseJs(
        "function Foo() { this.bar = function() {}} Foo.prototype = {}; Foo.prototype.getBar = function() {};");

    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(1, testPass.methodDefinitions.size());

    Node getBarSignature = testPass.methodDefinitions.get("getBar").get(0);
    assertEquals(1, getBarSignature.getNumChildren());
    assertEquals(Token.FUNCTION, getBarSignature.getType());

    assertNull(testPass.getSignatureStore().getSignature("getBar"));
  }

  @org.junit.Test
  public void testPrototypeWithPrototypeAndMultipleMissingMethods() {
    Node root = parseJs(
        "function Foo() { this.bar = function() {}} Foo.prototype = {"
        + "getBar: function() {}, getBaz: function() {}}; Foo.prototype.getQuux = function() {};");

    testPass.process(null, root);

    assertTrue(testPass.externMethods.isEmpty());
    assertTrue(testPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(testPass.nonMethodProperties.isEmpty());
    assertEquals(0, testPass.methodDefinitions.size());
  }

}