import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    CompilerOptions options = TestCompiler.getDefaultOptions();
    options.aliasTransformations = List.of(AliasTransformation.GOOG_SCOPE);

    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(
        new TestCompiler(), null, transformationHandler);

    Node root = TestCompiler.parseString(
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "dom.createElement(DIV);" +
            "goog.scope(function() {" +
            "  var dom2 = goog.dom;" +
            "  var DIV2 = dom2.TagName.DIV;" +
            "  dom2.createElement(DIV2);" +
            "});" +
            "});");

    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    Node domDefinition = aliasDefinitions.get(0);
    assertEquals("dom", domDefinition.getString());
    Node dom2Definition = aliasDefinitions.get(1);
    assertEquals("dom2", dom2Definition.getString());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(2, scopeCall.getChildCount());
    Node functionNode = scopeCall.getChildAtIndex(1);
    assertNotNull(functionNode);
    assertFalse(functionNode.hasChildren());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(3, aliasUsages.size());

    AliasUsage domUsage = aliasUsages.get(0);
    assertInstanceOf(AliasedNode.class, domUsage);
    Node domUsageNode = ((AliasedNode) domUsage).getAliasDefinition();
    assertEquals("dom", domUsageNode.getString());

    AliasUsage dom2Usage = aliasUsages.get(1);
    assertInstanceOf(AliasedNode.class, dom2Usage);
    Node dom2UsageNode = ((AliasedNode) dom2Usage).getAliasDefinition();
    assertEquals("dom2", dom2UsageNode.getString());

    AliasUsage domElementUsage = aliasUsages.get(2);
    assertInstanceOf(AliasedTypeNode.class, domElementUsage);
    Node domElementUsageNode = ((AliasedTypeNode) domElementUsage).getAliasDefinition();
    assertEquals("dom.Element", domElementUsageNode.getString());

    // Test errors
    Node badScopeCall = TestCompiler.parseString("goog.scope(function() {})").getFirstChild();
    scopedAliases.process(null, badScopeCall);
    assertTrue(scopedAliases.hasErrors());
    List<DiagnosticType> errors = scopedAliases.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));

    Node badScopeCallWithMultipleStatements =
        TestCompiler.parseString("goog.scope(function() {" +
            "var x = 1;" +
            "})").getFirstChild();
    scopedAliases.process(null, badScopeCallWithMultipleStatements);
    assertTrue(scopedAliases.hasErrors());
    errors = scopedAliases.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));

    Node badScopeCallWithThis =
        TestCompiler.parseString("goog.scope(function() {" +
            "this.x = 1;" +
            "})").getFirstChild();
    scopedAliases.process(null, badScopeCallWithThis);
    assertTrue(scopedAliases.hasErrors());
    errors = scopedAliases.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, errors.get(0));

    Node badScopeCallWithReturn =
        TestCompiler.parseString("goog.scope(function() {" +
            "return 1;" +
            "})").getFirstChild();
    scopedAliases.process(null, badScopeCallWithReturn);
    assertTrue(scopedAliases.hasErrors());
    errors = scopedAliases.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, errors.get(0));

    Node badScopeCallWithThrow =
        TestCompiler.parseString("goog.scope(function() {" +
            "throw new Error('test');" +
            "})").getFirstChild();
    scopedAliases.process(null, badScopeCallWithThrow);
    assertTrue(scopedAliases.hasErrors());
    errors = scopedAliases.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_THROW, errors.get(0));

    Node aliasDefinitionWithSameName =
        TestCompiler.parseString("goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var dom = 'test';" +
            "})").getFirstChild();
    scopedAliases.process(null, aliasDefinitionWithSameName);
    assertTrue(scopedAliases.hasErrors());
    errors = scopedAliases.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, errors.get(0));

    Node aliasDefinitionWithNonAliasLocal =
        TestCompiler.parseString("goog.scope(function() {" +
            "var x = 1; " +
            "var dom = goog.dom;" +
            "})").getFirstChild();
    scopedAliases.process(null, aliasDefinitionWithNonAliasLocal);
    assertTrue(scopedAliases.hasErrors());
    errors = scopedAliases.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, errors.get(0));

    List<SourcePosition> positions = Lists.newArrayList();
    for (DiagnosticType error : errors) {
      SourcePosition position = scopedAliases.getSourceRegion(error.getNode());
      positions.add(position);
    }
    SourcePosition[] expectedPositions = new SourcePosition[] {
      new SourcePosition<AliasTransformation>() {},
      new SourcePosition<AliasTransformation>() {
        @Override
        public int getLineNumber() {
          return 5;
        }
      },
      new SourcePosition<AliasTransformation>() {
        @Override
        public int getLineNumber() {
          return 5;
        }
      },
      new SourcePosition<AliasTransformation>() {},
      new SourcePosition<AliasTransformation>() {
        @Override
        public int getLineNumber() {
          return 5;
        }
      },
      new SourcePosition<AliasTransformation>() {
        @Override
        public int getLineNumber() {
          return 6;
        }
      },
      new SourcePosition<AliasTransformation>() {
        @Override
        public int getLineNumber() {
          return 6;
        }
      },
      new SourcePosition<AliasTransformation>() {
        @Override
        public int getLineNumber() {
          return 7;
        }
      },
      new SourcePosition<AliasTransformation>() {},
      new SourcePosition<AliasTransformation>() {
        @Override
        public int getLineNumber() {
          return 8;
        }
      },
      new SourcePosition<AliasTransformation>() {
        @Override
        public int getLineNumber() {
          return 8;
        }
      },
      new SourcePosition<AliasTransformation>() {
        @Override
        public int getLineNumber() {
          return 9;
        }
      }
    };
    assertArrayEquals(expectedPositions, positions.toArray(new SourcePosition[0]));
  }

  @Test
  public void testScopedAliasesWithImports() {
    CompilerOptions options = TestCompiler.getDefaultOptions();
    options.aliasTransformations = List.of(AliasTransformation.GOOG_SCOPE);

    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    ScopedAliases scopedAliases = new ScopedAliases(
        new TestCompiler(), null, transformationHandler);

    Node root = TestCompiler.parseString(
        "goog.scope(function() {" +
            "import java.util.Map;" +
            "var myMap = new Map();" +
            "})").getFirstChild();

    scopedAliases.process(null, root);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitions.size());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(2, scopeCall.getChildCount());
    Node functionNode = scopeCall.getChildAtIndex(1);
    assertNotNull(functionNode);
    assertFalse(functionNode.hasChildren());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(0, aliasUsages.size());

    // Test errors
    Node badScopeCall = TestCompiler.parseString("goog.scope(function() {})").getFirstChild();
    scopedAliases.process(null, badScopeCall);
    assertTrue(scopedAliases.hasErrors());
    List<DiagnosticType> errors = scopedAliases.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));

    Node aliasDefinitionWithSameName =
        TestCompiler.parseString("goog.scope(function() {" +
            "import java.util.Map;" +
            "var myMap = new Map();" +
            "var myMap = 'test';" +
            "})").getFirstChild();
    scopedAliases.process(null, aliasDefinitionWithSameName);
    assertTrue(scopedAliases.hasErrors());
    errors = scopedAliases.getDiagnostics();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, errors.get(0));

    List<SourcePosition> positions = Lists.newArrayList();
    for (DiagnosticType error : errors) {
      SourcePosition position = scopedAliases.getSourceRegion(error.getNode());
      positions.add(position);
    }
    SourcePosition[] expectedPositions = new SourcePosition[] {};
    assertArrayEquals(expectedPositions, positions.toArray(new SourcePosition[0]));
  }
}