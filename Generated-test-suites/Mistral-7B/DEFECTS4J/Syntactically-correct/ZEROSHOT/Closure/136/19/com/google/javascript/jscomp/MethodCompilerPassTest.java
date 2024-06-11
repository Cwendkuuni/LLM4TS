import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

public class MethodCompilerPassTest {
  private Compiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      abstract Callback getActingCallback() {
        // no-op
      }

      @Override
      abstract SignatureStore getSignatureStore() {
        return signatureStore;
      }
    };
    signatureStore = new SignatureStore() {
      private Multimap<String, Node> signatures = Multimap.create();

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
  }

  @Test
  public void testEmptySourceFile() {
    Node root = null;
    Node externs = Node.EMPTY_NODE;
    methodCompilerPass.process(externs, root);
    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
    assertEquals(0, signatureStore.getSignatures().size());
  }

  @Test
  public void testExternsFileWithFunctions() {
    String source = "Foo.bar = function() {};\n" +
                    "Foo.baz = function() {};\n";
    Node externs = Node.sourceNode(source);
    Node root = Node.EMPTY_NODE;
    methodCompilerPass.process(externs, root);

    Set<String> expectedExternMethods = Sets.newHashSet("bar", "baz");
    Set<String> expectedNonMethodProperties = Sets.newHashSet();
    Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
    expectedMethodDefinitions.put("bar", Node.FUNCTION_DECLARATION);
    expectedMethodDefinitions.put("baz", Node.FUNCTION_DECLARATION);

    assertEquals(expectedExternMethods, methodCompilerPass.externMethods);
    assertEquals(expectedNonMethodProperties, methodCompilerPass.nonMethodProperties);
    assertEquals(expectedMethodDefinitions, methodCompilerPass.methodDefinitions);
    assertEquals(expectedMethodDefinitions, signatureStore.getSignatures());
  }

  @Test
  public void testSourceFileWithFunctions() {
    String source = "function Foo() {}\n" +
                    "Foo.prototype.bar = function() {}\n" +
                    "function Foo2() {}\n" +
                    "Foo2.prototype.getBaz = function() {}";
    Node externs = Node.EMPTY_NODE;
    Node root = Node.sourceNode(source);
    methodCompilerPass.process(externs, root);

    Set<String> expectedExternMethods = Sets.newHashSet();
    Set<String> expectedNonMethodProperties = Sets.newHashSet();
    Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
    expectedMethodDefinitions.put("bar", Node.METHOD_DECLARATION);

    assertEquals(expectedExternMethods, methodCompilerPass.externMethods);
    assertEquals(expectedNonMethodProperties, methodCompilerPass.nonMethodProperties);
    assertEquals(expectedMethodDefinitions, methodCompilerPass.methodDefinitions);
    assertEquals(expectedMethodDefinitions, signatureStore.getSignatures());
  }

  @Test(expected = IllegalStateException.class)
  public void testSourceFileWithUndefinedFunction() {
    String source = "Foo.prototype.getBar = function() {}";
    Node externs = Node.EMPTY_NODE;
    Node root = Node.sourceNode(source);
    methodCompilerPass.process(externs, root);

    fail("Expected an IllegalStateException");
  }
}