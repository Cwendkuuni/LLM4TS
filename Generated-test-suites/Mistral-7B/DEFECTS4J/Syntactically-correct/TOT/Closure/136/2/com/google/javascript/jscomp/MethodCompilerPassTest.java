import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.testing.TypeCheckerTester;

public class MethodCompilerPassTest {

  public static void main(String[] args) throws Exception {
    runTest("test1.js");
    runTest("test2.js");
    runTest("test3.js");
  }

  private static void runTest(String fileName) throws Exception {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node sourceNode = TypeCheckerTester.loadModule(compiler, fileName);

    MethodCompilerPass methodCompilerPass = new NormalMethodCompilerPass(compiler);
    methodCompilerPass.process(null, sourceNode);

    assertMethodsDefined("test1.js", methodCompilerPass.methodDefinitions);
    assertMethodsDefined("test2.js", methodCompilerPass.methodDefinitions);
    assertMethodsDefined("test3.js", methodCompilerPass.methodDefinitions);
  }

  private static void assertMethodsDefined(String fileName, Multimap<String, Node> methodDefinitions) {
    SignatureStore signatureStore = new SignatureStore() {
      private Multimap<String, Node> storedSignatures = HashMultimap.create();

      @Override
      public void reset() {
        storedSignatures.clear();
      }

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        storedSignatures.put(functionName, functionNode);
      }

      @Override
      public void removeSignature(String functionName) {
        storedSignatures.remove(functionName);
      }

      @Override
      public Node getFunctionNode(String functionName) {
        return storedSignatures.get(functionName).get(0);
      }

      @Override
      public String getSourceFile(String functionName) {
        return fileName;
      }
    };

    MethodCompilerPass testee = new TestMethodCompilerPass(compiler, signatureStore);
    testee.process(null, sourceNode);

    assertEquals(3, methodDefinitions.size());

    Node method1 = methodDefinitions.get("test1.func1").get(0);
    Node method2 = methodDefinitions.get("test2.func2").get(0);
    Node method3 = methodDefinitions.get("test3.func3").get(0);

    assertEquals("test1.func1(arg1, arg2)", method1.toStringTree(compiler));
    assertEquals("test2.func2(arg1)", method2.toStringTree(compiler));
    assertEquals("test3.func3(arg1, arg2, arg3)", method3.toStringTree(compiler));

    assertFalse(methodDefinitions.containsKey("nonExistentMethod"));
    assertNull(methodDefinitions.get("nonExistentMethod").get(0));

    assertSame(method1, testee.getFunctionNode("test1.func1"));
    assertSame(method2, testee.getFunctionNode("test2.func2"));
    assertSame(method3, testee.getFunctionNode("test3.func3"));
  }

  static class TestMethodCompilerPass extends MethodCompilerPass {

    TestMethodCompilerPass(Compiler compiler, SignatureStore signatureStore) {
      super(compiler);
      this.signatureStore = signatureStore;
    }

    SignatureStore signatureStore;

    @Override
    SignatureStore getSignatureStore() {
      return signatureStore;
    }

    @Override
    Callback getActingCallback() {
      // No action needed in this test
      return null;
    }
  }
}