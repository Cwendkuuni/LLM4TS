import static com.google.javascript.jscomp.CompilerPass.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.IDepender;
import com.google.javascript.jscomp.JsCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private IDepender depender;
  private SourceFile sourceFile1;
  private SourceFile sourceFile2;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    compiler = new JsCompiler(options);
    depender = new IDepender() {
      @Override
      public void addDependency(String fromModule, String toModule) {
        // Empty implementation for testing
      }
    };
    compiler.setDepender(depender);

    sourceFile1 = new SourceFile("module1.js",
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() {};");
    sourceFile2 = new SourceFile("module2.js",
        "function Foo2() {}\n" +
        "Foo2.prototype.bar = function() {};");
  }

  @Test
  public void testMoveMethodToNextModule() {
    compiler.addSource(sourceFile1);
    compiler.addSource(sourceFile2);

    compiler.process();

    Node rootNode1 = compiler.getRootNode(sourceFile1);
    Node rootNode2 = compiler.getRootNode(sourceFile2);

    // Check that the method is present in both modules
    Node barMethod1 = rootNode1.mayContainName("bar");
    Node barMethod2 = rootNode2.mayContainName("bar");
    assertNotNull(barMethod1);
    assertNotNull(barMethod2);

    // Check that the method bodies are stubs in the first module
    assertEquals(STUB_METHOD_NAME, barMethod1.getClass().getName());
    // Check that the method bodies are the actual functions in the second module
    assertEquals(Node.FUNCTION_DECL, barMethod2.getClass().getType());

    // Test method call in the first module
    Node callToBar1 = IR.call(barMethod1, null);
    assertNotNull(callToBar1);

    // Test method call in the second module
    Node callToBar2 = IR.call(barMethod2, null);
    assertNotNull(callToBar2);

    // Test that the call to the method in the first module actually calls the method in the second module
    Node expectedResult = IR.string("result");
    Node callToBar1WithArg = IR.call(callToBar1, expectedResult);
    Node callToBar2WithArg = IR.call(barMethod2, expectedResult);
    assertEquals(callToBar2WithArg, callToBar1WithArg);
  }
}