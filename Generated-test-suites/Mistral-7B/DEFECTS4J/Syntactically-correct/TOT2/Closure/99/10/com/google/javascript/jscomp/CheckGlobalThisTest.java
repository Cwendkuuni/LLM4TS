import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCase;
import com.google.javascript.jscomp.testing.TestSourceFile;

public class CheckGlobalThisTest extends TestCase {

  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions(new SourceFile("test.js"));
    options.setWarningLevel(WARNING, DiagnosticType.JSC_USED_GLOBAL_THIS);

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setOptions(options);

    Node sourceNode = new TestSourceFile("test.js")
        .getTree();

    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, WARNING));
    traversal.traverse(sourceNode);

    assertTrue(compiler.getDiagnostics().isEmpty());

    testConstructorFunction();
    testFunctionWithThisAnnotation();
    testFunctionWithNoThisAnnotation();
    testFunctionWithGlobalThis();
  }

  public void testConstructorFunction() {
    // Test code for constructor function
  }

  public void testFunctionWithThisAnnotation() {
    // Test code for function annotated with @this
  }

  public void testFunctionWithNoThisAnnotation() {
    // Test code for function where there's no logical place to put a @this annotation
  }

  public void testFunctionWithGlobalThis() {
    // Test code for function that uses global this
  }
}