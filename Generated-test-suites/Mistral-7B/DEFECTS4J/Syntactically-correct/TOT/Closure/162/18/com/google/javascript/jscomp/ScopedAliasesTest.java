import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.AliasTransformationHandler;

/**
 * Test scenarios for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    SourceFile input = new SourceFile("Test.java", "package test;\n" +
        "\n" +
        "class Test {\n" +
        "  static goog.dom.createElement(goog.dom.TagName tag) {\n" +
        "    return goog.scope(function() {\n" +
        "      var dom = goog.dom;\n" +
        "      return dom.createElement(tag);\n" +
        "    })\n" +
        "  }\n" +
        "}\n");

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    Node root = compiler.parse(input, null);

    // Perform alias transformation
    scopedAliasesPass.process(null, root);

    // Check if root node has been transformed
    assertEquals("test.GoogDomCreateElement(goog.dom.TagName tag)",
        root.getQualifiedName());

    // Check if alias usage is correct
    Node aliasUsage = root.getFirstChild();
    Node aliasedNode = aliasUsage.getNext();
    assertEquals("goog.dom.TagName", aliasedNode.getQualifiedName());

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check if alias definitions are removed
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitions.size());

    // Check if scope calls are collapsed
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals("test.GoogDomCreateElement(goog.dom.TagName tag)",
        scopeCalls.get(0).getQualifiedName());

    // Check if errors are reported
    Node errorNode = root.getChildAtIndex(1);
    assertTrue(errorNode.getType() == Token.ERROR);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", errorNode.getFirstChild().getType());
  }
}