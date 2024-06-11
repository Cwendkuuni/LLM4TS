import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {
  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;

  @Before
  public void setUp() {
    compiler = new Compiler();
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        // No implementation required for testing
        return null;
      }

      @Override
      SignatureStore getSignatureStore() {
        // No implementation required for testing
        return null;
      }
    };
  }

  @Test
  public void testMethodDeclarationWithSignature() {
    Node rootNode = createNodeFromString(
        "function Foo() {\n" +
        "  this.bar = function(x) {\n" +
        "    return x * 2;\n" +
        "  };\n" +
        "}\n");
    methodCompilerPass.process(null, rootNode);

    assertEquals("bar", methodCompilerPass.methodDefinitions.keySet().iterator().next());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
  }

  @Test
  public void testMethodDeclarationWithoutSignature() {
    Node rootNode = createNodeFromString(
        "function Foo() {\n" +
        "  this.qux;\n" +
        "}\n");
    methodCompilerPass.process(null, rootNode);

    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("qux"));
  }

  @Test
  public void testMethodDeclarationWithExternalSignature() {
    Node rootNode = createNodeFromString(
        "function Foo() {\n" +
        "}\n" +
        "Foo.prototype.quux = function() {\n" +
        "  return 'Hello, World!';\n" +
        "};\n");
    methodCompilerPass.process(null, rootNode);

    assertEquals("quux", methodCompilerPass.methodDefinitions.keySet().iterator().next());
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedMethod() {
    Node rootNode = createNodeFromString(
        "function Foo() {\n" +
        "}\n" +
        "Foo.prototype.quux;\n");
    methodCompilerPass.process(null, rootNode);

    // This should throw an IllegalStateException since 'quux' was not found
  }

  @Test
  public void testMethodDeclarationWithInvalidName() {
    Node rootNode = createNodeFromString(
        "function Foo() {\n" +
        "  this.__proto__.quux = function() {\n" +
        "    return 'Hello, World!';\n" +
        "  };\n" +
        "}\n");
    methodCompilerPass.process(null, rootNode);

    assertTrue(methodCompilerPass.nonMethodProperties.contains("__proto__"));
  }

  private Node createNodeFromString(String code) {
    return RhinoApi.eval(RhinoApi.getGlobalContext(), code);
  }
}