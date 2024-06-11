import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeOperations;

public class MethodCompilerPassTest {
  private Compiler compiler;
  private Parser parser;
  private Node rootNode;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    parser = new Parser(new SourceFile("test.js"));
  }

  @Test
  public void testMethodCompilerPass() {
    String code = "function Foo() {}" +
        "Foo.prototype.bar = function() {}" +
        "Foo.prototype.qux = function(x) {}" +
        "function Foo2() {}" +
        "Foo2.prototype.baz = function() {}" +
        "Foo2.prototype.qux = function() {}";
    rootNode = parser.parse(code);
    compiler.compile(rootNode, null, null);

    MethodCompilerPass methodCompilerPass = new MyMethodCompilerPass(compiler);
    methodCompilerPass.process(null, rootNode);

    // Test that all methods were identified correctly
    assertEquals(5, methodCompilerPass.methodDefinitions.size());
    assertEquals("Foo.bar", methodCompilerPass.methodDefinitions.get("Foo.bar").toString());
    assertEquals("Foo.qux", methodCompilerPass.methodDefinitions.get("Foo.qux").toString());
    assertEquals("Foo2.baz", methodCompilerPass.methodDefinitions.get("Foo2.baz").toString());
    assertEquals("Foo2.qux", methodCompilerPass.methodDefinitions.get("Foo2.qux").toString());
    assertEquals("Function.prototype.call", methodCompilerPass.methodDefinitions.get("Function.prototype.call").toString());

    // Test that extern methods were identified correctly
    assertTrue(methodCompilerPass.externMethods.containsAll(Sets.newHashSet("Foo", "Foo2")));

    // Test that non-method properties were correctly excluded
    assertFalse(methodCompilerPass.methodDefinitions.containsKey("length"));
  }

  @Test
  public void testMissingFunctionSignature() {
    String code = "function Foo() {}" +
        "Foo.prototype.bar = 42"; // Missing function signature
    rootNode = parser.parse(code);
    compiler.compile(rootNode, null, null);

    MethodCompilerPass methodCompilerPass = new MyMethodCompilerPass(compiler);
    methodCompilerPass.process(null, rootNode);

    // Test that the missing method definition was excluded
    assertFalse(methodCompilerPass.methodDefinitions.containsKey("Foo.bar"));
  }

  @Test
  public void testMethodWithEmptySignature() {
    String code = "function Foo() {}" +
        "Foo.prototype.bar = function() {}" +
        "Foo.prototype.baz = function() {}" + // Empty signature
        "Foo.prototype.qux = function(x) {}";
    rootNode = parser.parse(code);
    compiler.compile(rootNode, null, null);

    MethodCompilerPass methodCompilerPass = new MyMethodCompilerPass(compiler);
    methodCompilerPass.process(null, rootNode);

    // Test that the empty method definition was included in externMethodsWithoutSignatures
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("Foo.baz"));
  }
}

class MyMethodCompilerPass extends MethodCompilerPass {
  public MyMethodCompilerPass(Compiler compiler) {
    super(compiler);
  }

  @Override
  Callback getActingCallback() {
    // No-op
    return new Callback() {
      @Override
      public void call(NodeTraversal nodeTraversal) {}
    };
  }

  @Override
  SignatureStore getSignatureStore() {
    // No-op
    return new SignatureStore() {
      @Override
      public void reset() {}

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        // No-op
      }

      @Override
      public void removeSignature(String functionName) {
        // No-op
      }
    };
  }
}