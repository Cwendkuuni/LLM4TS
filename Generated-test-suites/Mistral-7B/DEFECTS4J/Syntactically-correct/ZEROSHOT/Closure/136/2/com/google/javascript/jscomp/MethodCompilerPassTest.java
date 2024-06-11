import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Var.VarType;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;

public class MethodCompilerPassTest {

  private Compiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      abstract Callback getActingCallback() {
        return null;
      }

      @Override
      abstract SignatureStore getSignatureStore() {
        return signatureStore;
      }
    };
    signatureStore = new SignatureStore() {
      private Multimap<String, Node> signatures = HashMultimap.create();

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        signatures.put(functionName, functionNode);
      }

      @Override
      public void removeSignature(String functionName) {
        signatures.remove(functionName);
      }

      @Override
      public void reset() {
        signatures.clear();
      }

      public Multimap<String, Node> getSignatures() {
        return signatures;
      }
    };
  }

  @Test
  public void testEmpty() {
    Node root = null;
    Node externs = null;
    methodCompilerPass.process(externs, root);
    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
  }

  @Test
  public void testExterns() {
    String code = "foo.bar = function() {}";
    Node externs = Node.newNode(Token.SCRIPT, code);
    Node root = Node.newNode(Token.SCRIPT, "");
    methodCompilerPass.process(externs, root);

    Multimap<String, Node> signatures = methodCompilerPass.getSignatures();
    assertEquals(1, signatures.size());
    assertEquals("foo.bar", signatures.keys().iterator().next());
    Node methodNode = signatures.get("foo.bar").iterator().next();
    assertEquals(Token.FUNCTION, methodNode.getType());

    assertTrue(methodCompilerPass.externMethods.contains("foo.bar"));
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("foo.bar", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertEquals(methodNode, methodCompilerPass.methodDefinitions.get("foo.bar").iterator().next());
  }

  @Test
  public void testGetPropPrototype() {
    String code = "Foo.prototype.bar = function() {}";
    Node externs = null;
    Node root = Node.newNode(Token.SCRIPT, code);
    methodCompilerPass.process(externs, root);

    Multimap<String, Node> signatures = methodCompilerPass.getSignatures();
    assertEquals(1, signatures.size());
    assertEquals("Foo.prototype.bar", signatures.keys().iterator().next());
    Node methodNode = signatures.get("Foo.prototype.bar").iterator().next();
    assertEquals(Token.FUNCTION, methodNode.getType());

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("Foo.prototype.bar", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertEquals(methodNode, methodCompilerPass.methodDefinitions.get("Foo.prototype.bar").iterator().next());
  }

  @Test
  public void testPrototypeAssignment() {
    String code = "Foo.prototype = function() {}";
    Node externs = null;
    Node root = Node.newNode(Token.SCRIPT, code);
    methodCompilerPass.process(externs, root);

    Multimap<String, Node> signatures = methodCompilerPass.getSignatures();
    assertEquals(0, signatures.size());

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(0, methodCompilerPass.methodDefinitions.size());
  }

  @Test
  public void testObjectLiteral() {
    String code = "var Foo = { bar: function() {} }";
    Node externs = null;
    Node root = Node.newNode(Token.SCRIPT, code);
    methodCompilerPass.process(externs, root);

    Multimap<String, Node> signatures = methodCompilerPass.getSignatures();
    assertEquals(1, signatures.size());
    assertEquals("Foo.bar", signatures.keys().iterator().next());
    Node methodNode = signatures.get("Foo.bar").iterator().next();
    assertEquals(Token.FUNCTION, methodNode.getType());

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("Foo.bar", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertEquals(methodNode, methodCompilerPass.methodDefinitions.get("Foo.bar").iterator().next());
  }

  @Test
  public void testObjectLiteralWithPrototype() {
    String code = "var Foo = { prototype: { bar: function() {} } }";
    Node externs = null;
    Node root = Node.newNode(Token.SCRIPT, code);
    methodCompilerPass.process(externs, root);

    Multimap<String, Node> signatures = methodCompilerPass.getSignatures();
    assertEquals(1, signatures.size());
    assertEquals("Foo.prototype.bar", signatures.keys().iterator().next());
    Node methodNode = signatures.get("Foo.prototype.bar").iterator().next();
    assertEquals(Token.FUNCTION, methodNode.getType());

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("Foo.prototype.bar", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertEquals(methodNode, methodCompilerPass.methodDefinitions.get("Foo.prototype.bar").iterator().next());
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedFunction() {
    String code = "var x = Foo.bar;";
    Node externs = null;
    Node root = Node.newNode(Token.SCRIPT, code);
    methodCompilerPass.process(externs, root);

    fail("Should have thrown an IllegalStateException");
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedFunctionInExterns() {
    String code = "foo.bar = undefined;";
    Node externs = Node.newNode(Token.SCRIPT, code);
    Node root = Node.newNode(Token.SCRIPT, "");
    methodCompilerPass.process(externs, root);

    fail("Should have thrown an IllegalStateException");
  }

  @Test
  public void testNonMethodProperty() {
    String code = "var x = 42;";
    Node externs = null;
    Node root = Node.newNode(Token.SCRIPT, code);
    methodCompilerPass.process(externs, root);

    assertEquals(ImmutableSet.of("x"), methodCompilerPass.nonMethodProperties);
    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
  }

  @Test
  public void testMultipleMethodDefinitions() {
    String code = "var Foo = { bar: function() {}, baz: function() {} }";
    Node externs = null;
    Node root = Node.newNode(Token.SCRIPT, code);
    methodCompilerPass.process(externs, root);

    Multimap<String, Node> signatures = methodCompilerPass.getSignatures();
    assertEquals(2, signatures.size());
    assertEquals("Foo.bar", signatures.keys().iterator().next());
    Node barMethodNode = signatures.get("Foo.bar").iterator().next();
    assertEquals(Token.FUNCTION, barMethodNode.getType());
    assertEquals("Foo.baz", signatures.keys().iterator().next());
    Node bazMethodNode = signatures.get("Foo.baz").iterator().next();
    assertEquals(Token.FUNCTION, bazMethodNode.getType());

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(2, methodCompilerPass.methodDefinitions.size());
    assertEquals(Sets.newHashSet("Foo.bar", "Foo.baz"), methodCompilerPass.methodDefinitions.keySet());
    assertEquals(Lists.newArrayList(barMethodNode, bazMethodNode), methodCompilerPass.methodDefinitions.values());
  }

  @Test
  public void testMultipleMethodDefinitionsWithPrototype() {
    String code = "var Foo = { prototype: { bar: function() {}, baz: function() {} } }";
    Node externs = null;
    Node root = Node.newNode(Token.SCRIPT, code);
    methodCompilerPass.process(externs, root);

    Multimap<String, Node> signatures = methodCompilerPass.getSignatures();
    assertEquals(2, signatures.size());
    assertEquals("Foo.prototype.bar", signatures.keys().iterator().next());
    Node barMethodNode = signatures.get("Foo.prototype.bar").iterator().next();
    assertEquals(Token.FUNCTION, barMethodNode.getType());
    assertEquals("Foo.prototype.baz", signatures.keys().iterator().next());
    Node bazMethodNode = signatures.get("Foo.prototype.baz").iterator().next();
    assertEquals(Token.FUNCTION, bazMethodNode.getType());

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(2, methodCompilerPass.methodDefinitions.size());
    assertEquals(Sets.newHashSet("Foo.prototype.bar", "Foo.prototype.baz"), methodCompilerPass.methodDefinitions.keySet());
    assertEquals(Lists.newArrayList(barMethodNode, bazMethodNode), methodCompilerPass.methodDefinitions.values());
  }

  @Test
  public void testMethodWithVarInFunction() {
    String code = "function foo() { var x = 42; }";
    Node externs = null;
    Node root = Node.newNode(Token.SCRIPT, code);
    methodCompilerPass.process(externs, root);

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
  }

  @Test
  public void testMethodWithVarInFunctionInExterns() {
    String code = "function foo() { var x = 42; }";
    Node externs = Node.newNode(Token.SCRIPT, code);
    Node root = Node.newNode(Token.SCRIPT, "");
    methodCompilerPass.process(externs, root);

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
  }

  @Test
  public void testMethodWithVarInObjectLiteral() {
    String code = "var Foo = { bar: function() { var x = 42; } }";
    Node externs = null;
    Node root = Node.newNode(Token.SCRIPT, code);
    methodCompilerPass.process(externs, root);

    Multimap<String, Node> signatures = methodCompilerPass.getSignatures();
    assertEquals(1, signatures.size());
    assertEquals("Foo.bar", signatures.keys().iterator().next());
    Node methodNode = signatures.get("Foo.bar").iterator().next();
    assertEquals(Token.FUNCTION, methodNode.getType());

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("Foo.bar", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertEquals(methodNode, methodCompilerPass.methodDefinitions.get("Foo.bar").iterator().next());
  }

  @Test
  public void testMethodWithVarInObjectLiteralInExterns() {
    String code = "function foo() {} var Foo = { bar: foo }";
    Node externs = Node.newNode(Token.SCRIPT, code);
    Node root = Node.newNode(Token.SCRIPT, "");
    methodCompilerPass.process(externs, root);

    Multimap<String, Node> signatures = methodCompilerPass.getSignatures();
    assertEquals(1, signatures.size());
    assertEquals("Foo.bar", signatures.keys().iterator().next());
    Node methodNode = signatures.get("Foo.bar").iterator().next();
    assertEquals(Token.GETPROP, methodNode.getType());
    assertEquals("foo", methodNode.getFirstChild().getString());

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("Foo.bar", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertEquals(methodNode, methodCompilerPass.methodDefinitions.get("Foo.bar").iterator().next());
  }
}

class SignatureStore {
  // SignatureStore implementation here
}