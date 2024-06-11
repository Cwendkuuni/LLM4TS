import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MethodCompilerPassTest extends CompilerPassTestBase {

  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return new DummyCallback();
      }

      @Override
      SignatureStore getSignatureStore() {
        if (signatureStore == null) {
          signatureStore = mock(SignatureStore.class);
        }
        return signatureStore;
      }
    };
  }

  @Test
  public void testExternMethodsWithSignatures() {
    Node externs = createNode("externs", "function foo() {};");
    Node root = createNode("root", "function bar() { foo(); }");;

    methodCompilerPass.process(externs, root);

    verify(signatureStore, times(1)).addSignature("foo", anyNode(), anyString());
    verify(signatureStore, times(1)).addSignature("bar", anyNode(), anyString());
  }

  @Test
  public void testExternMethodsWithoutSignatures() {
    Node externs = createNode("externs", "function foo();");
    Node root = createNode("root", "function bar() { foo(); }");

    methodCompilerPass.process(externs, root);

    verify(signatureStore, never()).addSignature("foo", anyNode(), anyString());
    verify(externMethodsWithoutSignatures, contains("foo"));
  }

  @Test
  public void testMethodsDefinedInSourceCode() {
    Node externs = createNode("externs", "");
    Node root = createNode("root", "function Foo() { this.bar = function() {}; }");

    methodCompilerPass.process(externs, root);

    verify(signatureStore, times(1)).addSignature("bar", anyNode(), anyString());
  }

  @Test
  public void testMethodsDefinedInObjectLiteral() {
    Node externs = createNode("externs", "");
    Node root = createNode("root", "function Foo() { this.prototype = { bar: function() {} }; }");

    methodCompilerPass.process(externs, root);

    verify(signatureStore, times(1)).addSignature("bar", anyNode(), anyString());
  }

  @Test
  public void testMethodsWithPrototype() {
    Node externs = createNode("externs", "");
    Node root = createNode("root", "function Foo() { this.prototype = { getBar: function() {} }; }");

    methodCompilerPass.process(externs, root);

    verify(signatureStore, times(1)).addSignature("getBar", anyNode(), anyString());
  }

  @Test
  public void testMethodsDefinedInObjectLiteralWithPrototype() {
    Node externs = createNode("externs", "");
    Node root = createNode("root", "function Foo() { this.prototype = { getBar: function() {} }; this.prototype.getBaz = function() {}; }");

    methodCompilerPass.process(externs, root);

    verify(signatureStore, times(1)).addSignature("getBar", anyNode(), anyString());
    verify(signatureStore, times(1)).addSignature("getBaz", anyNode(), anyString());
  }

  @Test
  public void testNonMethodProperties() {
    Node externs = createNode("externs", "");
    Node root = createNode("root", "function Foo() { this.bar = 1; }");

    methodCompilerPass.process(externs, root);

    verify(nonMethodProperties, contains("bar"));
  }

  @Test
  public void testEmptyExterns() {
    Node externs = null;
    Node root = createNode("root", "function bar() {}");

    methodCompilerPass.process(externs, root);

    // No-op since there are no externs
  }

  @Test
  public void testEmptySourceCode() {
    Node externs = createNode("externs", "function foo() {}");
    Node root = null;

    methodCompilerPass.process(externs, root);

    // No-op since there is no source code
  }
}

class DummyCallback implements Callback {}