import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.MethodCompilerPass;

public class MethodCompilerPassTest {

  public static class TestMethodCompilerPass extends MethodCompilerPass {

    @Override
    SignatureStore getSignatureStore() {
      return new SignatureStore() {
        private final Set<String> signatures = new java.util.LinkedHashSet<>();

        @Override
        public void reset() {
          signatures.clear();
        }

        @Override
        public void addSignature(String functionName, Node functionNode, String sourceFile) {
          signatures.add(functionName);
        }

        @Override
        public void removeSignature(String functionName) {
          signatures.remove(functionName);
        }

        public Set<String> getSignatures() {
          return signatures;
        }
      };
    }

    @Override
    Callback getActingCallback() {
      // No-op, as this is just a test harness
      return null;
    }
  }

  @org.junit.Test
  public void testMethodCompilerPass() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    Node externs = Node.externalSource("externs.js", "");
    Node root = Node.externalSource("source.js", "");

    TestMethodCompilerPass testPass = new TestMethodCompilerPass(compiler);
    testPass.process(externs, root);

    // Check that we found the expected signatures from the externs
    SignatureStore signatureStore = testPass.getSignatureStore();
    Set<String> signatures = signatureStore.getSignatures();
    assertEquals("Signatures from externs should match expected ones",
        Sets.newHashSet("foo", "bar"), signatures);

    // Check that we didn't incorrectly mark non-method properties as methods
    assertEquals("Non-method properties should not be in methodDefinitions",
        Sets.newHashSet(), testPass.methodDefinitions.keys());

    // Check that we found the expected signatures from the source
    Node sourceNode = Node.sourceString("source.js",
        "function foo(a, b) {}\n" +
        "function bar(a, b) {}\n" +
        "function baz() {}\n");
    testPass.process(null, sourceNode);

    signatures = signatureStore.getSignatures();
    assertEquals("Signatures from source should match expected ones",
        Sets.newHashSet("foo", "bar"), signatures);

    // Check that signatures were correctly associated with method nodes
    assertEquals("Method 'foo' should have been associated with correct node",
        testPass.methodDefinitions.get("foo"), testPass.compiler.getRootNode().getChildAt(0));

    assertEquals("Method 'bar' should have been associated with correct node",
        testPass.methodDefinitions.get("bar"), testPass.compiler.getRootNode().getChildAt(1));

    assertNull("Method 'baz' should not have been found", testPass.methodDefinitions.get("baz"));

    // Check that extern methods without signatures are ignored
    testPass.process(externs, root);
    testPass.reset();
    testPass.process(externs, root);
    signatures = signatureStore.getSignatures();
    assertFalse("Non-signature method should not be in signatures",
        signatures.contains("ignoredMethod"));

    // Check that extern methods with signatures are included
    externs = Node.externalSource("externs.js",
        "extern function ignoredMethod();\n" +
        "extern function foo(a: number, b: number): number;\n");
    testPass.process(externs, root);
    signatures = signatureStore.getSignatures();
    assertTrue("Signature method should be in signatures",
        signatures.contains("foo"));
  }
}