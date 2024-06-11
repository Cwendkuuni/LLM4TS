import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Mock private Compiler compiler;
  @Mock private PreprocessorSymbolTable preprocessorSymbolTable;
  @Mock private AliasTransformationHandler transformationHandler;

  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testValidScopedAliases() {
    // Arrange - create test cases
    Node root = Node.createTree(
        "function testScopedAliases() {" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "    var DIV = goog.dom.TagName.DIV;" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "    var DIV = goog.dom.TagName.DIV;" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "}"
    );

    // Act - run test
    scopedAliases.process(null, root);

    // Assert - check results
    Traversal traversal = scopedAliases.getLastTraversal();
    assertEquals(0, traversal.getErrors().size());
    assertEquals(2, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(2, traversal.getAliasUsages().size());

    Node firstScopeCall = traversal.getScopeCalls().get(0);
    Node secondScopeCall = traversal.getScopeCalls().get(1);
    Node firstScopeBlock = firstScopeCall.getLastChild().getLastChild();
    Node secondScopeBlock = secondScopeCall.getLastChild().getLastChild();

    // Verify that the scoped aliases are transformed correctly
    assertEquals("goog.dom", firstScopeBlock.getFirstChild().getQualifiedName());
    assertEquals("goog.dom.TagName", firstScopeBlock.getChildAtIndex(1).getQualifiedName());
    assertEquals("DIV", secondScopeBlock.getChildAtIndex(1).getString());
  }

  @Test
  public void testImproperlyUsedGoogScope() {
    // Arrange - create test cases
    Node root = Node.createTree(
        "function testImproperlyUsedGoogScope() {" +
        "  goog.scope(function() {" +
        "  });" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  });" +
        "}"
    );

    // Act - run test
    scopedAliases.process(null, root);

    // Assert - check results
    Traversal traversal = scopedAliases.getLastTraversal();
    List<Node> errors = traversal.getErrors();
    assertEquals(1, errors.size());

    Node error = errors.get(0);
    DiagnosticType diagnosticType = error.getDiagnosticType();
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, diagnosticType);

    Node firstScopeCall = traversal.getScopeCalls().get(0);
    Node firstScopeBlock = firstScopeCall.getLastChild().getLastChild();
    assertEquals(1, firstScopeBlock.getChildCount());

    Node improperlyUsedScopeCall = error.getSourceNode();
    assertEquals(improperlyUsedScopeCall, firstScopeCall.getLastChild());
  }

  @Test
  public void testGoogScopeWithBadParameters() {
    // Arrange - create test cases
    Node root = Node.createTree(
        "function testGoogScopeWithBadParameters() {" +
        "  goog.scope(function() {" +
        "  });" +
        "  goog.scope(function(a, b) {" +
        "  });" +
        "}"
    );

    // Act - run test
    scopedAliases.process(null, root);

    // Assert - check results
    Traversal traversal = scopedAliases.getLastTraversal();
    List<Node> errors = traversal.getErrors();
    assertEquals(1, errors.size());

    Node error = errors.get(0);
    DiagnosticType diagnosticType = error.getDiagnosticType();
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, diagnosticType);

    Node badParametersScopeCall = traversal.getScopeCalls().get(1);
    Node badParametersScopeBlock = badParametersScopeCall.getLastChild().getLastChild();
    assertEquals(2, badParametersScopeBlock.getChildCount());
  }

  @Test
  public void testGoogScopeWithThis() {
    // Arrange - create test cases
    Node root = Node.createTree(
        "function testGoogScopeWithThis() {" +
        "  goog.scope(function() {" +
        "    this.value = 42;" +
        "  });" +
        "}"
    );

    // Act - run test
    scopedAliases.process(null, root);

    // Assert - check results
    Traversal traversal = scopedAliases.getLastTraversal();
    List<Node> errors = traversal.getErrors();
    assertEquals(1, errors.size());

    Node error = errors.get(0);
    DiagnosticType diagnosticType = error.getDiagnosticType();
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, diagnosticType);

    Node googScopeCall = traversal.getScopeCalls().get(0).getLastChild();
    Node googScopeBlock = googScopeCall.getLastChild().getLastChild();
    assertEquals(1, googScopeBlock.getChildCount());
    assertEquals("this", googScopeBlock.getChildAtIndex(0).getString());
  }

  @Test
  public void testGoogScopeWithReturn() {
    // Arrange - create test cases
    Node root = Node.createTree(
        "function testGoogScopeWithReturn() {" +
        "  goog.scope(function() {" +
        "    return 42;" +
        "  });" +
        "}"
    );

    // Act - run test
    scopedAliases.process(null, root);

    // Assert - check results
    Traversal traversal = scopedAliases.getLastTraversal();
    List<Node> errors = traversal.getErrors();
    assertEquals(1, errors.size());

    Node error = errors.get(0);
    DiagnosticType diagnosticType = error.getDiagnosticType();
    assertEquals(GOOG_SCOPE_USES_RETURN, diagnosticType);

    Node googScopeCall = traversal.getScopeCalls().get(0).getLastChild();
    Node googScopeBlock = googScopeCall.getLastChild().getLastChild();
    assertEquals(1, googScopeBlock.getChildCount());
    assertEquals(Token.RETURN, googScopeBlock.getFirstChild().getType());
  }

  @Test
  public void testGoogScopeWithThrow() {
    // Arrange - create test cases
    Node root = Node.createTree(
        "function testGoogScopeWithThrow() {" +
        "  try {" +
        "    goog.scope(function() {" +
        "      throw new Error('test');" +
        "    });" +
        "  } catch (e) {" +
        "  }" +
        "}"
    );

    // Act - run test
    scopedAliases.process(null, root);

    // Assert - check results
    Traversal traversal = scopedAliases.getLastTraversal();
    List<Node> errors = traversal.getErrors();
    assertEquals(1, errors.size());

    Node error = errors.get(0);
    DiagnosticType diagnosticType = error.getDiagnosticType();
    assertEquals(GOOG_SCOPE_USES_THROW, diagnosticType);

    Node googScopeCall = traversal.getScopeCalls().get(0).getLastChild();
    Node googScopeBlock = googScopeCall.getLastChild().getLastChild();
    assertEquals(1, googScopeBlock.getChildCount());
    assertEquals(Token.THROW, googScopeBlock.getFirstChild().getType());
  }

  @Test
  public void testGoogScopeWithAliasRedefined() {
    // Arrange - create test cases
    Node root = Node.createTree(
        "function testGoogScopeWithAliasRedefined() {" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "    var DIV = goog.dom.TagName.DIV;" +
        "    var DIV = 42;" +
        "  });" +
        "}"
    );

    // Act - run test
    scopedAliases.process(null, root);

    // Assert - check results
    Traversal traversal = scopedAliases.getLastTraversal();
    List<Node> errors = traversal.getErrors();
    assertEquals(1, errors.size());

    Node error = errors.get(0);
    DiagnosticType diagnosticType = error.getDiagnosticType();
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, diagnosticType);

    Node aliasRedefinedScopeCall = traversal.getScopeCalls().get(0).getLastChild();
    Node aliasRedefinedScopeBlock = aliasRedefinedScopeCall.getLastChild().getLastChild();
    assertEquals(2, aliasRedefinedScopeBlock.getChildCount());
    assertEquals("DIV", aliasRedefinedScopeBlock.getChildAtIndex(1).getString());
  }

  @Test
  public void testGoogScopeWithNonAliasLocal() {
    // Arrange - create test cases
    Node root = Node.createTree(
        "function testGoogScopeWithNonAliasLocal() {" +
        "  goog.scope(function() {" +
        "    var value = 42;" +
        "  });" +
        "}"
    );

    // Act - run test
    scopedAliases.process(null, root);

    // Assert - check results
    Traversal traversal = scopedAliases.getLastTraversal();
    List<Node> errors = traversal.getErrors();
    assertEquals(1, errors.size());

    Node error = errors.get(0);
    DiagnosticType diagnosticType = error.getDiagnosticType();
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, diagnosticType);

    Node nonAliasLocalScopeCall = traversal.getScopeCalls().get(0).getLastChild();
    Node nonAliasLocalScopeBlock = nonAliasLocalScopeCall.getLastChild().getLastChild();
    assertEquals(1, nonAliasLocalScopeBlock.getChildCount());
    assertEquals("value", nonAliasLocalScopeBlock.getChildAtIndex(0).getString());
  }
}