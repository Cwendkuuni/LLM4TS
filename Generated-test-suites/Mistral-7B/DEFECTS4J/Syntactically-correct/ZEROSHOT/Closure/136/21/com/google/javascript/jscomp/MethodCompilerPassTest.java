import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class MethodCompilerPassTest {
  private AbstractCompiler compiler;
  private SignatureStore signatureStore;
  private MethodCompilerPass methodCompilerPass;

  @Before
  public void setUp() {
    compiler = Mockito.spy(new CompilerPassConfig.DefaultConfig());
    signatureStore = Mockito.spy(new SignatureStore() {
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
    });
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return Mockito.mock(Callback.class);
      }

      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }
    };
  }

  @Test
  public void testProcess_ExternsAreIgnored() {
    Node externs = createNode("var foo = function() {};");
    Node root = createNode("function bar() {}");

    methodCompilerPass.process(externs, root);

    assertEquals(0, signatureStore.signatures.size());
  }

  @Test
  public void testProcess_FunctionDeclarationsAreProcessed() {
    Node externs = createNode("var foo = function() {};");
    Node root = createNode("function bar() {}", "test.js");
    Node bar = root.getFirstChild();

    methodCompilerPass.process(externs, root);

    assertEquals(1, signatureStore.signatures.size());
    Node expectedSignature = createNode("function bar() {}", "test.js");
    assertEquals(expectedSignature, signatureStore.signatures.get("bar"));;
  }

  @Test
  public void testProcess_PrototypeMethodsAreProcessed() {
    Node externs = createNode("var Foo = function() {};");
    Node root = createNode("Foo.prototype.bar = function() {};");

    methodCompilerPass.process(externs, root);

    assertEquals(1, signatureStore.signatures.size());
    Node expectedSignature = createNode("function () {}", "test.js");
    assertEquals(expectedSignature, signatureStore.signatures.get("bar"));
  }

  @Test
  public void testProcess_PropertiesAreIgnored() {
    Node externs = createNode("var Foo = function() {};");
    Node root = createNode("Foo.prototype.bar = 42;");

    methodCompilerPass.process(externs, root);

    assertEquals(0, signatureStore.signatures.size());
  }

  @Test
  public void testProcess_StaticMethodsAreProcessed() {
    Node externs = createNode("var Foo = function() {};");
    Node root = createNode("Foo.bar = function() {};");

    methodCompilerPass.process(externs, root);

    assertEquals(1, signatureStore.signatures.size());
    Node expectedSignature = createNode("function() {}", "test.js");
    assertEquals(expectedSignature, signatureStore.signatures.get("bar"));
  }

  @Test
  public void testProcess_MethodsWithNoSignaturesAreIgnored() {
    Node externs = createNode("var Foo = function() {};");
    Node root = createNode("Foo.prototype.bar = function() {};");

    methodCompilerPass.process(externs, root);

    assertEquals(0, signatureStore.signatures.size());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("bar"));
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_UndefinedFunctionIsThrownAnError() {
    Node externs = createNode("var Foo = function() {};");
    Node root = createNode("Foo.prototype.undefined = function() {};");

    methodCompilerPass.process(externs, root);

    fail("Expected an IllegalStateException");
  }

  private Node createNode(String code, String sourceFile) {
    RhinoEngine engine = new RhinoEngine();
    return (Node) engine.compile(code).getRootNode();
  }

  private Node createNode(String code) {
    RhinoEngine engine = new RhinoEngine();
    return (Node) engine.compile(code).getRootNode();
  }
}

interface SignatureStore {
  void reset();
  void addSignature(String functionName, Node functionNode, String sourceFile);
  void removeSignature(String functionName);
}