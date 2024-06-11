import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Traversal;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.JUnit4;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  @Mock
  private AbstractCompiler compiler;

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private AliasTransformationHandler transformationHandler;

  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testBasicScopedAlias() {
    Node rootNode = NodeFactory.createNodeFromString(
        "goog.scope(function() {" +
        "  var x = goog.x;" +
        "})");

    scopedAliases.process(null, rootNode);

    assertThat(transformationHandler.getAliasTransformationCount(), is(1));
    ArgumentCaptor<AliasTransformation> captor = ArgumentCaptor.forClass(AliasTransformation.class);
    verify(transformationHandler).logAliasTransformation(
        eq("test_file.js"), captor.capture());
    AliasTransformation aliasTransformation = captor.getValue();
    assertEquals("x", aliasTransformation.getAliasName());
    assertEquals("goog.x", aliasTransformation.getQualifiedName());
  }

  @Test
  public void testMultipleScopedAliases() {
    Node rootNode = NodeFactory.createNodeFromString(
        "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  var y = goog.y;" +
        "})");

    scopedAliases.process(null, rootNode);

    assertThat(transformationHandler.getAliasTransformationCount(), is(2));
    ArgumentCaptor<AliasTransformation> captor1 = ArgumentCaptor.forClass(AliasTransformation.class);
    ArgumentCaptor<AliasTransformation> captor2 = ArgumentCaptor.forClass(AliasTransformation.class);
    verify(transformationHandler, times(2)).logAliasTransformation(
        eq("test_file.js"), captor1.capture());
    verify(transformationHandler, times(2)).logAliasTransformation(
        eq("test_file.js"), captor2.capture());
    AliasTransformation aliasTransformationX = captor1.getValue();
    AliasTransformation aliasTransformationY = captor2.getValue();
    assertEquals("x", aliasTransformationX.getAliasName());
    assertEquals("goog.x", aliasTransformationX.getQualifiedName());
    assertEquals("y", aliasTransformationY.getAliasName());
    assertEquals("goog.y", aliasTransformationY.getQualifiedName());
  }

  @Test(expected = AssertionError.class)
  public void testDuplicateScopedAlias() {
    Node rootNode = NodeFactory.createNodeFromString(
        "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  var x = goog.x;" +
        "})");

    scopedAliases.process(null, rootNode);
  }

  @Test
  public void testScopedAliasWithThrows() {
    Node rootNode = NodeFactory.createNodeFromString(
        "goog.scope(function() {" +
        "  throw new Error('Test error');" +
        "})");

    try {
      scopedAliases.process(null, rootNode);
      fail("Expected an error.");
    } catch (CompilerException e) {
      assertEquals(GOOG_SCOPE_USES_THROW, e.getDiagnostics().get(0).getType());
    }
  }

  @Test
  public void testScopedAliasWithReturn() {
    Node rootNode = NodeFactory.createNodeFromString(
        "goog.scope(function() {" +
        "  return 42;" +
        "})");

    try {
      scopedAliases.process(null, rootNode);
      fail("Expected an error.");
    } catch (CompilerException e) {
      assertEquals(GOOG_SCOPE_USES_RETURN, e.getDiagnostics().get(0).getType());
    }
  }

  @Test
  public void testScopedAliasWithThis() {
    Node rootNode = NodeFactory.createNodeFromString(
        "goog.scope(function() {" +
        "  this.x = 42;" +
        "})");

    try {
      scopedAliases.process(null, rootNode);
      fail("Expected an error.");
    } catch (CompilerException e) {
      assertEquals(GOOG_SCOPE_REFERENCES_THIS, e.getDiagnostics().get(0).getType());
    }
  }

  @Test
  public void testScopedAliasWithImproperUsage() {
    Node rootNode = NodeFactory.createNodeFromString(
        "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "  })" +
        "})");

    try {
      scopedAliases.process(null, rootNode);
      fail("Expected an error.");
    } catch (CompilerException e) {
      assertEquals(GOOG_SCOPE_USED_IMPROPERLY, e.getDiagnostics().get(0).getType());
    }
  }

  @Test
  public void testScopedAliasWithParameter() {
    Node rootNode = NodeFactory.createNodeFromString(
        "goog.scope(function(p) {" +
        "  var x = goog.x;" +
        "})");

    try {
      scopedAliases.process(null, rootNode);
      fail("Expected an error.");
    } catch (CompilerException e) {
      assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, e.getDiagnostics().get(0).getType());
    }
  }
}