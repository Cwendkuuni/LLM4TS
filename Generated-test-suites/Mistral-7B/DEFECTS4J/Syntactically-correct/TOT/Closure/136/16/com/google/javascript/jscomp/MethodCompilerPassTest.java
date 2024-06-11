import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass.AbstractCompiler;
import com.google.javascript.jscomp.MethodCompilerPass.SignatureStoreEmpty;

public class MethodCompilerPassTest {
  @Test
  public void testBasicFunctionDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    MethodCompilerPass pass = new FooMethodCompilerPass(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    assertEquals(1, methodDefinitions.size());
    assertEquals("foo", methodDefinitions.keySet().iterator().next());
    assertSame(externs, methodDefinitions.get("foo").getParent());

    assertTrue(signatureStore.containsKey("foo"));
    assertEquals(root, signatureStore.get("foo").getFirstChild().getParent());
  }

  @Test
  public void testFunctionDeclarationWithNoSignatureInExterns() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    MethodCompilerPass pass = new FooMethodCompilerPass(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    externs.addChild(Node.property("foo", Node.string("function foo() {}")));

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    assertTrue(methodDefinitions.isEmpty());
    assertFalse(signatureStore.containsKey("foo"));
  }

  @Test
  public void testMethodDeclarationWithSignatureInExterns() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    MethodCompilerPass pass = new FooMethodCompilerPass(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    externs.addChild(
        Node.property("foo",
            Node.objectLiteral()
                .addChild(Node.key("value"), Node.functionLiteral("function foo(x)"))));

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    assertEquals(1, methodDefinitions.size());
    assertEquals("foo", methodDefinitions.keySet().iterator().next());
    assertSame(externs, methodDefinitions.get("foo").getParent());

    assertTrue(signatureStore.containsKey("foo"));
    assertEquals(
        Node.functionLiteral("function foo(x)"), signatureStore.get("foo"));
  }

  @Test
  public void testStaticMethodDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    MethodCompilerPass pass = new FooMethodCompilerPass(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    externs.addChild(Node.property("Foo", Node.objectLiteral()));
    externs.addChild(
        externs.getLastChild().addChild(Node.key("prototype"), Node.objectLiteral()));
    externs.getLastChild().addChild(
        Node.key("bar"), Node.functionLiteral("function Foo.bar() {}"));

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    assertEquals(1, methodDefinitions.size());
    assertEquals("Foo.bar", methodDefinitions.keySet().iterator().next());
    assertSame(externs, methodDefinitions.get("Foo.bar").getParent());

    assertTrue(signatureStore.containsKey("Foo.bar"));
    assertEquals(
        Node.functionLiteral("function Foo.bar() {}"), signatureStore.get("Foo.bar"));
  }

  @Test
  public void testPrototypeMethodDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    MethodCompilerPass pass = new FooMethodCompilerPass(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    externs.addChild(Node.property("Foo", Node.objectLiteral()));
    externs.getLastChild().addChild(Node.key("prototype"), Node.objectLiteral());
    externs.getLastChild().addChild(Node.key("prototype"), Node.property("bar", Node.functionLiteral("function() {}")));

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    assertEquals(1, methodDefinitions.size());
    assertEquals("Foo.prototype.bar", methodDefinitions.keySet().iterator().next());
    assertSame(externs, methodDefinitions.get("Foo.prototype.bar").getParent());

    assertTrue(signatureStore.containsKey("Foo.prototype.bar"));
    assertEquals(
        Node.functionLiteral("function() {}"), signatureStore.get("Foo.prototype.bar"));
  }

  @Test
  public void testMethodDeclarationWithNoSignature() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    MethodCompilerPass pass = new FooMethodCompilerPass(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    externs.addChild(Node.property("Foo", Node.objectLiteral()));
    externs.getLastChild().addChild(Node.key("prototype"), Node.objectLiteral());
    externs.getLastChild().getLastChild().addChild(Node.key("bar"));

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    assertTrue(methodDefinitions.isEmpty());
    assertFalse(signatureStore.containsKey("Foo.prototype.bar"));
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedMethod() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    MethodCompilerPass pass = new FooMethodCompilerPass(compiler);

    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    externs.addChild(Node.property("Foo", Node.objectLiteral()));
    externs.getLastChild().addChild(Node.key("prototype"), Node.objectLiteral());
    externs.getLastChild().getLastChild().addChild(Node.key("qux"));

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    assertEquals(0, methodDefinitions.size());
    assertFalse(signatureStore.containsKey("Foo.prototype.qux"));
  }

  @Test
  public void testMethodWithEmptySignatureStore() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    MethodCompilerPass pass = new FooMethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStoreEmpty();
      }
    };

    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    SignatureStore signatureStore = pass.getSignatureStore();

    assertTrue(methodDefinitions.isEmpty());
    assertFalse(signatureStore.containsKey("foo"));
  }
}

class FooMethodCompilerPass extends MethodCompilerPass {
  FooMethodCompilerPass(AbstractCompiler compiler) {
    super(compiler);
  }

  @Override
  Callback getActingCallback() {
    return new DoNothingCallback();
  }

  @Override
  SignatureStore getSignatureStore() {
    return new SignatureStoreEmpty();
  }

  static class DoNothingCallback extends AbstractPostOrderCallback {
    // empty implementation
  }

  static class SignatureStoreEmpty implements SignatureStore {
    private Map<String, Node> signatures = new java.util.HashMap<>();

    @Override
    public void reset() {
      signatures.clear();
    }

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.put(functionName, functionNode);
    }

    @Override
    public void removeSignature(String functionName) {
      signatures.remove(functionName);
    }
  }
}