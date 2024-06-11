import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Parameters;
import org.robolectric.shadows.shadow.function.FunctionShadow;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.shadow.api.ShadowLog.d;

@RunWith(org.robolectric.RobolectricTestRunner.class)
@Config(manifestFile = "test-manifest.xml")
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeTraversal traverser;

  @Parameters(name = "{index}: level")
  @Test
  public void testCheckGlobalThis(CheckLevel level) throws Exception {
    SourceFile sourceFile = new SourceFile("test.js", "function evil() { this.useful = undefined; }");
    compiler = new AbstractCompiler(sourceFile, level);
    compiler.init();
    compiler.parse();
    Node rootNode = compiler.getRoot();

    traverser = new NodeTraversal(rootNode, compiler);
    traverser.traverse();

    int diagnosticCount = compiler.getDiagnostics().size();
    assertFalse("No diagnostics expected", diagnosticCount > 0);

    // Test a safe use of 'this'
    SourceFile safeFile = new SourceFile("safe.js", "function Safe() { this.useful = 42; }");
    AbstractCompiler safeCompiler = new AbstractCompiler(safeFile, level);
    safeCompiler.init();
    safeCompiler.parse();
    Node safeRootNode = safeCompiler.getRoot();

    safeTraverser = new NodeTraversal(safeRootNode, safeCompiler);
    safeTraverser.traverse();

    int safeDiagnosticCount = safeCompiler.getDiagnostics().size();
    assertTrue("Expected no diagnostics", safeDiagnosticCount == 0);

    // Test an unsafe use of 'this'
    SourceFile unsafeFile = new SourceFile("unsafe.js", "function evil() { this.useful = undefined; }");
    AbstractCompiler unsafeCompiler = new AbstractCompiler(unsafeFile, level);
    unsafeCompiler.init();
    unsafeCompiler.parse();
    Node unsafeRootNode = unsafeCompiler.getRoot();

    unsafeTraverser = new NodeTraversal(unsafeRootNode, unsafeCompiler);
    unsafeTraverser.traverse();

    int unsafeDiagnosticCount = unsafeCompiler.getDiagnostics().size();
    assertEquals("Expected one diagnostic", 1, unsafeDiagnosticCount);

    Node diagnostic = unsafeCompiler.getDiagnostics().get(0);
    assertEquals("Expected JSC_USED_GLOBAL_THIS diagnostic",
        DiagnosticType.warning(DiagnosticType.JSC_USED_GLOBAL_THIS), diagnostic.getType());
    assertEquals("Expected diagnostic message to contain 'dangerous use of the global 'this' object'",
        "dangerous use of the global 'this' object", diagnostic.getMessage());
    assertEquals("Expected diagnostic to be on evil() function", "evil", diagnostic.getSourceName());
    assertEquals("Expected diagnostic to be on line 1", 1, diagnostic.getLineNumber());
  }

  @Test
  public void testCheckGlobalThisWithMockedAbstractCompiler() throws Exception {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler mockCompiler = Mockito.mock(AbstractCompiler.class);
    NodeTraversal traverser = new NodeTraversal(Mockito.mock(Node.class), mockCompiler);

    JSDocInfo jsDoc = Mockito.mock(JSDocInfo.class);
    Mockito.when(jsDoc.isConstructor()).thenReturn(false);
    Mockito.when(jsDoc.isInterface()).thenReturn(false);
    Mockito.when(jsDoc.hasThisType()).thenReturn(false);
    Mockito.when(jsDoc.isOverride()).thenReturn(false);
    Mockito.when(mockCompiler.getRoot()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(mockCompiler.getRoot().getJSDocInfo()).thenReturn(jsDoc);

    Node node = Mockito.mock(Node.class);
    Mockito.when(node.getType()).thenReturn(Token.THIS);
    Mockito.when(node.getParent()).thenReturn(Mockito.mock(Node.class));

    traverser.shouldTraverse(Mockito.argThat(n -> n == node), Mockito.eq(null), Mockito.eq(null));

    Mockito.verify(mockCompiler, Mockito.times(1)).report(Mockito.argThat(report -> {
      DiagnosticType diagnosticType = report.getType();
      assertEquals("Expected JSC_USED_GLOBAL_THIS diagnostic",
          DiagnosticType.warning(DiagnosticType.JSC_USED_GLOBAL_THIS), diagnosticType);
      return true;
    }));
  }

  private NodeTraversal safeTraverser;
  private NodeTraversal unsafeTraverser;

  @Test
  public void testShouldNotTraversePrototypeProperties() throws Exception {
    SourceFile sourceFile = new SourceFile("test.js", "function evil() { this.prototype.useful = undefined; }");
    AbstractCompiler compiler = new AbstractCompiler(sourceFile, CheckLevel.WARNING);
    compiler.init();
    compiler.parse();
    Node rootNode = compiler.getRoot();

    NodeTraversal traverser = new NodeTraversal(rootNode, compiler);
    traverser.traverse();

    int diagnosticCount = compiler.getDiagnostics().size();
    assertFalse("No diagnostics expected", diagnosticCount > 0);
  }

  @Test
  public void testShouldNotTraversePrototypeSubProperties() throws Exception {
    SourceFile sourceFile = new SourceFile("test.js", "function evil() { this.prototype.sub.useful = undefined; }");
    AbstractCompiler compiler = new AbstractCompiler(sourceFile, CheckLevel.WARNING);
    compiler.init();
    compiler.parse();
    Node rootNode = compiler.getRoot();

    NodeTraversal traverser = new NodeTraversal(rootNode, compiler);
    traverser.traverse();

    int diagnosticCount = compiler.getDiagnostics().size();
    assertFalse("No diagnostics expected", diagnosticCount > 0);
  }
}