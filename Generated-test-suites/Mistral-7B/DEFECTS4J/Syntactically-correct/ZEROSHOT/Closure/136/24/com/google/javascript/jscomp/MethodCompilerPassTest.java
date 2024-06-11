import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.AbstractPostOrderCallbackFlag.*;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.Token;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MethodCompilerPass.class})
public class MethodCompilerPassTest {

  private static class DummyMethodCompilerPass extends MethodCompilerPass {
    @Override
    SignatureStore getSignatureStore() {
      return Mockito.mock(SignatureStore.class);
    }

    @Override
    Callback getActingCallback() {
      throw new UnsupportedOperationException();
    }
  }

  private static class DummySignatureStore implements SignatureStore {
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
  }

  @Test
  public void testMethodCompilerPass() throws Exception {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    PowerMockito.when(compiler.getOptions()).thenReturn(options);

    String sourceCode =
        "function foo() {} " +
        "function bar(x) {} " +
        "function baz() {} " +
        "Foo.prototype.qux = function() {} " +
        "Foo.prototype.quux = function quux() {} " +
        "Foo.prototype = {quuz: function() {}} " +
        "var Foo = {quux: function() {}} ";

    Node root = TestSourceFile.getTree(sourceCode);

    DummyMethodCompilerPass methodCompilerPass = new DummyMethodCompilerPass(compiler);
    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(methodCompilerPass);
    passManager.runPasses(root, options);

    Multimap<String, Node> expectedSignatures = methodCompilerPass.getSignatureStore().getSignatures();
    assertEquals(6, expectedSignatures.size());
    assertEquals("foo", expectedSignatures.get("foo").get(0).getIdentifier().getName());
    assertEquals("bar", expectedSignatures.get("bar").get(0).getIdentifier().getName());
    assertEquals("baz", expectedSignatures.get("baz").get(0).getIdentifier().getName());
    assertEquals("qux", expectedSignatures.get("qux").get(0).getIdentifier().getName());
    assertEquals("quux", expectedSignatures.get("quux").get(0).getIdentifier().getName());
    assertEquals("quuz", expectedSignatures.get("quuz").get(0).getIdentifier().getName());

    assertEquals(Set.of("prototype"), methodCompilerPass.externMethods);
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
  }
}