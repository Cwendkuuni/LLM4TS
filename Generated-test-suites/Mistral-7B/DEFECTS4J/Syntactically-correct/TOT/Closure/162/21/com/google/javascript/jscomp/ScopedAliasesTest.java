import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testScopedAliases() {
    // Given a node with a goog.scope block
    String script =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    // When the processor processes the node
    Node root = Node.parse(script);
    scopedAliases.process(null, root);

    // Then the goog.scope block should be removed and the aliases should be applied
    Node expected = Node.parse("goog.dom.createElement(goog.dom.TagName.DIV)"); ;
    assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testImproperUsageOfGoogScope() {
    // Given a node with an improper usage of goog.scope
    String script =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    dom.createElement(DIV);" +
        "  });" +
        "});";

    // When the processor processes the node
    Node root = Node.parse(script);
    scopedAliases.process(null, root);

    // Then the processor should report an error
    SourcePosition position = new SourcePosition<>();
    assertEquals("The call to goog.scope must be alone in a single statement.",
        scopedAliases.compiler.getLastError().getMessage(position));
  }

  @Test
  public void testBadParametersInGoogScope() {
    // Given a node with bad parameters in goog.scope
    String script =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function(a, b) {" +
        "    dom.createElement(DIV);" +
        "  });" +
        "});";

    // When the processor processes the node
    Node root = Node.parse(script);
    scopedAliases.process(null, root);

    // Then the processor should report an error
    SourcePosition position = new SourcePosition<>();
    assertEquals("The call to goog.scope must take only a single parameter.  It must" +
        " be an anonymous function that itself takes no parameters.",
        scopedAliases.compiler.getLastError().getMessage(position));
  }

  @Test
  public void testGoogScopeRefersToThis() {
    // Given a node with goog.scope referring to 'this'
    String script =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  this.dom = dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    this.createElement(DIV);" +
        "  });" +
        "});";

    // When the processor processes the node
    Node root = Node.parse(script);
    scopedAliases.process(null, root);

    // Then the processor should report an error
    SourcePosition position = new SourcePosition<>();
    assertEquals("The body of a goog.scope function cannot reference 'this'",
        scopedAliases.compiler.getLastError().getMessage(position));
  }

  @Test
  public void testGoogScopeUsesReturn() {
    // Given a node with goog.scope using 'return'
    String script =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    return dom.createElement(DIV);" +
        "  });" +
        "});";

    // When the processor processes the node
    Node root = Node.parse(script);
    scopedAliases.process(null, root);

    // Then the processor should report an error
    SourcePosition position = new SourcePosition<>();
    assertEquals("The body of a goog.scope function cannot use 'return'",
        scopedAliases.compiler.getLastError().getMessage(position));
  }

  @Test
  public void testGoogScopeUsesThrow() {
    // Given a node with goog.scope using 'throw'
    String script =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    throw new Error('Test Error')" +
        "  });" +
        "});";

    // When the processor processes the node
    Node root = Node.parse(script);
    scopedAliases.process(null, root);

    // Then the processor should report an error
    SourcePosition position = new SourcePosition<>();
    assertEquals("The body of a goog.scope function cannot use 'throw'",
        scopedAliases.compiler.getLastError().getMessage(position));
  }

  @Test
  public void testAliasRedefined() {
    // Given a node with an alias redefined
    String script =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var dom2 = goog.dom;" +
        "  var DIV2 = dom2.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    var DIV = 'new Div';" +
        "  });" +
        "});";

    // When the processor processes the node
    Node root = Node.parse(script);
    scopedAliases.process(null, root);

    // Then the processor should report an error
    SourcePosition position = new SourcePosition<>();
    assertEquals("The alias DIV is assigned a value more than once.",
        scopedAliases.compiler.getLastError().getMessage(position));
  }
}