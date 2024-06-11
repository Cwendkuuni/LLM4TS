import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.TestSourceFile;

import java.util.Collections;
import java.util.Set;

public class MethodCompilerPassTest {

  private static abstract class DummyMethodCompilerPass extends MethodCompilerPass {
    @Override
    abstract SignatureStore getSignatureStore();

    @Override
    abstract Callback getActingCallback();
  }

  private static class TestMethodCompilerPass extends DummyMethodCompilerPass {
    @Override
    SignatureStore getSignatureStore() {
      return new SignatureStore() {
        private final Set<String> signatures = new java.util.HashSet<>();

        @Override
        public void reset() {}

        @Override
        public void addSignature(String functionName, Node functionNode, String sourceFile) {
          signatures.add(functionName);
        }

        @Override
        public void removeSignature(String functionName) {}
      };
    }

    @Override
    Callback getActingCallback() {
      return null;
    }
  }

  @Test
  public void testMethodCompilerPass() throws Exception {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);

    String sourceCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "Foo.prototype.getBar = function() { return 42; }";

    TestSourceFile sourceFile = new TestSourceFile("Test.js", sourceCode);
    Node externs = null;
    Node root = Node.createTreeFromString(sourceCode, "Test.js");

    DummyMethodCompilerPass methodCompilerPass = new TestMethodCompilerPass();
    methodCompilerPass.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, options);

    Set<String> expectedSignatures = Sets.newHashSet("getBar", "bar");
    Set<String> actualSignatures = Sets.newHashSet();
    for (String functionName : methodCompilerPass.methodDefinitions.keySet()) {
      actualSignatures.add(functionName);
    }

    assertEquals(expectedSignatures, actualSignatures);

    passManager.runPasses();
  }

  @Test
  public void testExternMethods() throws Exception {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);

    String sourceCode = "window.foo = function() {}";

    TestSourceFile sourceFile = new TestSourceFile("Test.js", sourceCode);
    Node externs = Node.createTreeFromString(sourceCode, "Test.js");
    Node root = null;

    DummyMethodCompilerPass methodCompilerPass = new TestMethodCompilerPass();
    methodCompilerPass.process(externs, root);

    assertTrue(methodCompilerPass.externMethods.contains("foo"));

    passManager.runPasses();
  }

  @Test
  public void testNonMethodProperties() throws Exception {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);

    String sourceCode = "var a = 42";;

    TestSourceFile sourceFile = new TestSourceFile("Test.js", sourceCode);
    Node externs = null;
    Node root = Node.createTreeFromString(sourceCode, "Test.js");

    DummyMethodCompilerPass methodCompilerPass = new TestMethodCompilerPass();
    methodCompilerPass.process(externs, root);

    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());

    passManager.runPasses();
  }

  @Test
  public void testIgnoredExternMethods() throws Exception {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler, options);

    String sourceCode = "window.foo = function() {}";

    TestSourceFile sourceFile = new TestSourceFile("Test.js", sourceCode);
    Node externs = Node.createTreeFromString(sourceCode, "Test.js");
    Node root = null;

    DummyMethodCompilerPass methodCompilerPass = new TestMethodCompilerPass();
    methodCompilerPass.process(externs, root);
    methodCompilerPass.externMethodsWithoutSignatures.add("foo");

    passManager.runPasses();

    assertEquals(0, methodCompilerPass.methodDefinitions.size());
  }
}