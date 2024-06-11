import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  private static class TestCompilerOptions {
    public static final CompilerOptions options = new CompilerOptions();

    static {
      options.setAliasTransformation(NEW_ALIAS_TRANSFORMATION);
      options.setAliasTransformationHandler(Mockito.mock(AliasTransformationHandler.class));
    }
  }

  private static Compiler createCompiler() {
    return new Compiler(TestCompilerOptions.options);
  }

  @Test
  public void testScopedAliases() {
    Compiler compiler = createCompiler();

    String jsCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    Node rootNode = ParserUtil.parseJs(compiler, jsCode);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    // Check for errors
    assertTrue(traversal.hasErrors());
    List<Node> errors = traversal.getErrors();
    assertEquals(3, errors.size());

    // Check error messages
    assertEquals("The call to goog.scope must be alone in a single statement.",
        errors.get(0).getErrorMessage());
    assertEquals("The call to goog.scope must take only a single parameter.",
        errors.get(1).getErrorMessage());
    assertEquals("The body of a goog.scope function cannot use 'return'.",
        errors.get(2).getErrorMessage());

    // Check alias definitions, order, and usage
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    List<Node> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    assertEquals(1, aliasDefinitions.size());
    assertEquals("DOM", aliasDefinitions.get(0).getString());
    assertEquals(1, aliasUsages.size());
    assertEquals("dom", aliasUsages.get(0).toString());
    assertEquals("goog.dom", aliasUsages.get(0).getReplacedNode().getString());
    assertEquals(1, scopeCalls.size());

    // Check alias transformations
    List<AliasTransformation> transformations =
        (List<AliasTransformation>) TestCompilerOptions.options
            .getAliasTransformationHandler().getTransformations();
    assertEquals(1, transformations.size());
    assertEquals("DOM", transformations.get(0).getAlias());
    assertEquals("goog.dom", transformations.get(0).getReplacement());

    // Check transformed code
    Node transformedRootNode = traversal.getTransformedRoot();
    String transformedCode = NodePrinter.print(transformedRootNode);
    String expectedTransformedCode = "goog.dom.createElement(goog.dom.TagName.DIV);";
    assertEquals(expectedTransformedCode, transformedCode);
  }
}