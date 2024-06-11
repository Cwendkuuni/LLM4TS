import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.CallbackType.VISIT;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;

import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {
  private AbstractCompiler compiler;
  private MethodCompilerPass pass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    compiler = new Compiler();
    pass = new TestMethodCompilerPass(compiler);
    signatureStore = new SignatureStore() {
      private Multimap<String, Node> signatures = HashMultimap.create();

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

      public Multimap<String, Node> getSignatures() {
        return signatures;
      }
    };
    pass.setSignatureStore(signatureStore);
  }

  @Test
  public void testEmptyExterns() {
    Node root = Node.root();
    Node externs = Node.objectLiteral();
    pass.process(externs, root);
    assertTrue(pass.externMethods.isEmpty());
    assertTrue(pass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(pass.methodDefinitions.isEmpty());
  }

  @Test
  public void testSimpleFunctionDeclarationInExterns() {
    Node root = Node.root();
    Node externs = Node.objectLiteral();
    externs = externs.addChild(Node.property("foo", Node.string("function foo() {}")));
    pass.process(externs, root);
    assertEquals(1, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());
    assertTrue(pass.methodDefinitions.isEmpty());
  }

  @Test
  public void testFunctionDeclarationWithSignatureInExterns() {
    Node root = Node.root();
    Node externs = Node.objectLiteral();
    externs = externs.addChild(Node.property("foo", Node.string("function foo(x: number) {}")));
    pass.process(externs, root);
    assertEquals(1, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());
    Multimap<String, Node> signatures = pass.getSignatureStore().getSignatures();
    assertEquals(1, signatures.size());
    Node fooSignature = signatures.get("foo").get(0);
    assertEquals(VISIT, fooSignature.getUserData(CompilerPass.TraversalCallbackKey));
  }

  @Test
  public void testSimpleFunctionDeclarationInCode() {
    Node root = Node.root();
    Node functionNode = Node.function("foo", new ArrayList<>());
    root = Node.assign(Node.getProp(root, "prototype"), functionNode);
    pass.process(null, root);
    assertEquals(1, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());
    assertTrue(pass.methodDefinitions.containsKey("foo"));
    Node fooMethodDefinition = pass.methodDefinitions.get("foo").get(0);
    assertEquals(VISIT, fooMethodDefinition.getUserData(CompilerPass.TraversalCallbackKey));
  }

  @Test
  public void testFunctionDeclarationWithSignatureInCode() {
    Node root = Node.root();
    Node functionNode = Node.function("foo", new ArrayList<>());
    Node functionNameNode = Node.string("foo");
    root = Node.assign(Node.getProp(root, "prototype"), Node.assign(functionNameNode, functionNode));
    pass.process(null, root);
    assertEquals(1, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());
    Multimap<String, Node> signatures = pass.getSignatureStore().getSignatures();
    assertEquals(1, signatures.size());
    Node fooSignature = signatures.get("foo").get(0);
    assertEquals(VISIT, fooSignature.getUserData(CompilerPass.TraversalCallbackKey));
  }

  @Test
  public void testFunctionDeclarationWithMultipleSignaturesInCode() {
    Node root = Node.root();
    Node functionNode1 = Node.function("foo", new ArrayList<>());
    Node functionNameNode1 = Node.string("foo");
    root = Node.assign(Node.getProp(root, "prototype"), Node.assign(functionNameNode1, functionNode1));
    Node functionNode2 = Node.function("foo", List.of(Node.string("x"), Node.numberLiteral(1)));
    Node functionNameNode2 = Node.string("foo");
    root = Node.assign(Node.getProp(root, "prototype"), Node.assign(functionNameNode2, functionNode2));
    pass.process(null, root);
    assertEquals(1, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());
    Multimap<String, Node> signatures = pass.getSignatureStore().getSignatures();
    assertEquals(2, signatures.size());
    Node fooSignature1 = signatures.get("foo").get(0);
    Node fooSignature2 = signatures.get("foo").get(1);
    assertEquals(VISIT, fooSignature1.getUserData(CompilerPass.TraversalCallbackKey));
    assertEquals(VISIT, fooSignature2.getUserData(CompilerPass.TraversalCallbackKey));
  }

  @Test
  public void testFunctionDeclarationWithoutSignatureInCode() {
    Node root = Node.root();
    Node functionNode = Node.function("foo", new ArrayList<>());
    root = Node.assign(Node.getProp(root, "prototype"), functionNode);
    pass.process(null, root);
    assertEquals(1, pass.externMethodsWithoutSignatures.size());
    assertEquals("foo", pass.externMethodsWithoutSignatures.iterator().next());
    assertTrue(pass.methodDefinitions.containsKey("foo"));
    Node fooMethodDefinition = pass.methodDefinitions.get("foo").get(0);
    assertEquals(VISIT, fooMethodDefinition.getUserData(CompilerPass.TraversalCallbackKey));
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedFunctionInExterns() {
    Node root = Node.root();
    Node externs = Node.objectLiteral();
    externs = externs.addChild(Node.property("foo", Node.undefined()));
    pass.process(externs, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testFunctionCallInExterns() {
    Node root = Node.root();
    Node externs = Node.objectLiteral();
    externs = externs.addChild(Node.property("foo", Node.function("", new ArrayList<>()).setExpression(true)));
    externs = externs.addChild(Node.property("bar", Node.call(Node.getProp(externs, "foo"), new ArrayList<>()).setExpression(true)));
    pass.process(externs, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testFunctionCallInCode() {
    Node root = Node.root();
    Node functionNode = Node.function("foo", new ArrayList<>());
    root = Node.assign(Node.getProp(root, "prototype"), functionNode);
    Node callNode = Node.call(Node.getProp(root, "foo"), new ArrayList<>());
    pass.process(null, root);
  }

  @Test
  public void testPropertyDeclarationInExterns() {
    Node root = Node.root();
    Node externs = Node.objectLiteral();
    externs = externs.addChild(Node.property("foo", Node.string("")));
    pass.process(externs, root);
    assertEquals(1, pass.nonMethodProperties.size());
    assertEquals("foo", pass.nonMethodProperties.iterator().next());
    assertTrue(pass.methodDefinitions.isEmpty());
  }

  @Test
  public void testPropertyDeclarationInCode() {
    Node root = Node.root();
    Node propertyNode = Node.property("foo", Node.string(""));
    root = Node.assign(propertyNode, Node.getProp(root, "prototype"));
    pass.process(null, root);
    assertEquals(1, pass.nonMethodProperties.size());
    assertEquals("foo", pass.nonMethodProperties.iterator().next());
    assertTrue(pass.methodDefinitions.isEmpty());
  }

  @Test
  public void testPropertyDeclarationWithSameNameAsFunction() {
    Node root = Node.root();
    Node functionNode = Node.function("foo", new ArrayList<>());
    root = Node.assign(Node.getProp(root, "prototype"), functionNode);
    Node propertyNode = Node.property("foo", Node.string(""));
    root = Node.assign(propertyNode, Node.getProp(root, "prototype"));
    pass.process(null, root);
    assertEquals(1, pass.methodDefinitions.size());
    assertEquals("foo", pass.methodDefinitions.keySet().iterator().next());
  }

  @Test
  public void testPropertyDeclarationWithSameNameAsFunctionInExterns() {
    Node root = Node.root();
    Node externs = Node.objectLiteral();
    externs = externs.addChild(
        Node.property("foo", Node.function("", new ArrayList<>()).setExpression(true)));
    externs = externs.addChild(Node.property("foo", Node.string("")));
    pass.process(externs, root);
    assertEquals(1, pass.methodDefinitions.size());
    assertEquals("foo", pass.methodDefinitions.keySet().iterator().next());
  }
}

class TestMethodCompilerPass extends MethodCompilerPass {
  TestMethodCompilerPass(AbstractCompiler compiler) {
    super(compiler);
  }

  @Override
  abstract Callback getActingCallback();

  @Override
  abstract SignatureStore getSignatureStore();

  @Override
  Callback getActingCallback() {
    // This test pass doesn't do anything; it just checks that other test
    // passes add signatures correctly.
    return new NoOpCallback();
  }

  @Override
  SignatureStore getSignatureStore() {
    return signatureStore;
  }
}

class NoOpCallback extends AbstractPostOrderCallback {

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {}
}