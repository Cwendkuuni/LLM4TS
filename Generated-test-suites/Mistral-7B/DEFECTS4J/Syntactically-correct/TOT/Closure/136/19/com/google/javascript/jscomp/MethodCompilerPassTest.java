import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.testing.AbstractTestCompiler;
import com.google.javascript.rhino.Token;

public class MethodCompilerPassTest {

  @Test
  public void testMethodCompilerPass() throws Exception {
    String sourceCode =
        "function Foo() {\n"
        + "  this.prototype = {\n"
        + "    getBar: function() {\n"
        + "      return 'Hello from getBar';\n"
        + "    },\n"
        + "    getBaz: function(x, y) {\n"
        + "      return x + y;\n"
        + "    },\n"
        + "    getQux: function() {\n"
        + "      throw new Error('getQux should not be invoked');\n"
        + "    }\n"
        + "  };\n"
        + "}\n";

    AbstractCompiler compiler = new AbstractTestCompiler();
    CompilerOptions options = new CompilerOptions();
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

      public Node getSignature(String functionName) {
        return storedSignatures.get(functionName).get(0);
      }
    };

    MethodCompilerPass methodCompilerPass = new MyMethodCompilerPass(compiler, signatureStore);
    Node abstractSyntaxTree = AbstractTestCompiler.getAbstractSyntaxTree(sourceCode);
    methodCompilerPass.process(null, abstractSyntaxTree);

    Node getBarFunction = methodCompilerPass.getSignatureStore().getSignature("Foo.prototype.getBar");
    Node getBazFunction = methodCompilerPass.getSignatureStore().getSignature("Foo.prototype.getBaz");
    Node getQuxFunction = methodCompilerPass.getSignatureStore().getSignature("Foo.prototype.getQux");

    assertNotNull(getBarFunction);
    assertNotNull(getBazFunction);
    assertNull(getQuxFunction);

    Node expectedGetBarSignature = AbstractTestCompiler.getExpectedAbstractSyntaxTree("expectedGetBarSignature.js");
    assertEquals(expectedGetBarSignature.toSource(), getBarFunction.toSource());

    Node expectedGetBazSignature = AbstractTestCompiler.getExpectedAbstractSyntaxTree("expectedGetBazSignature.js");
    assertEquals(expectedGetBazSignature.toSource(), getBazFunction.toSource());

    // Test that getMethodDefinitions correctly stores method definitions
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals(getBarFunction, methodCompilerPass.methodDefinitions.get("Foo.prototype.getBar").get(0));
  }

  @Test
  public void testExternMethodsAreIgnored() throws Exception {
    String sourceCode =
        "extern function Foo.prototype.getBar();\n"
        + "function Foo() {\n"
        + "}\n";

    AbstractCompiler compiler = new AbstractTestCompiler();
    CompilerOptions options = new CompilerOptions();
    SignatureStore signatureStore = new SignatureStore() {
      // An empty store to make sure getMethodDefinitions doesn't throw an exception
    };

    MethodCompilerPass methodCompilerPass = new MyMethodCompilerPass(compiler, signatureStore);
    Node abstractSyntaxTree = AbstractTestCompiler.getAbstractSyntaxTree(sourceCode);
    methodCompilerPass.process(null, abstractSyntaxTree);

    assertEquals(0, methodCompilerPass.methodDefinitions.size());
  }
}

class MyMethodCompilerPass extends MethodCompilerPass {
  MyMethodCompilerPass(AbstractCompiler compiler, SignatureStore signatureStore) {
    super(compiler);
    this.signatureStore = signatureStore;
  }

  @Override
  Callback getActingCallback() {
    return new DoNothingCallback();
  }

  @Override
  SignatureStore getSignatureStore() {
    return signatureStore;
  }

  SignatureStore signatureStore;
}

class DoNothingCallback extends Callback {
  // No-op implementation
}