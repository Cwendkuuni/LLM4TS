import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.Handler.LoggingHandler;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ScopedAliasesTest {
  private SourceFile sourceFile;
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Before
  public void setUp() throws IOException {
    compiler = new Compiler();
    compiler.setOption(AliasTransformation.ENABLED, true);
    compiler.setOption(AliasTransformation.HANDLER, LoggingHandler.class.getName());
    sourceFile = new SourceFile("Test.java", "");
  }

  @After
  public void tearDown() {
    compiler.dispose();
  }

  /**
   * Test that a simple usage of goog.scope() with a single parameter results in
   * correct alias transformation.
   */
  @Test
  public void testSimpleUsage() throws Exception {
    String code = "goog.scope(function() {" +
                   "  function add(x, y) {" +
                   "    return x + y;" +
                   "  }" +
                   "  goog.dom.createElement('DIV');" +
                   "  var result = add(2, 3);" +
                   "});";

    Node rootNode = parser.parse(compiler, sourceFile, code, null);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable,
        new LoggingHandler());
    pass.process(null, rootNode);

    List<String> expectedAliases = Lists.newArrayList("goog.dom", "DIV");
    List<Node> actualAliases = Lists.newArrayList();
    for (AliasUsage alias : pass.getAliasUsages()) {
      actualAliases.add(alias.aliasReference);
    }

    assertEquals(expectedAliases.size(), actualAliases.size());
    for (String alias : expectedAliases) {
      boolean found = false;
      for (Node node : actualAliases) {
        if (node.getString().equalsIgnoreCase(alias)) {
          found = true;
          break;
        }
      }
      assertTrue(found);
    }

    Node resultNode = findNode(rootNode, "result");
    Node addNode = findNode(rootNode, "add");
    Node addCallNode = findNode(rootNode, "add", "add");
    Node addReturnNode = findNode(rootNode, "add", "add", "return");

    assertEquals(5, rootNode.getChildCount());
    assertEquals(1, addNode.getChildCount());
    assertEquals(2, addCallNode.getChildCount());
    assertEquals(1, addReturnNode.getChildCount());

    assertEquals(addNode, addCallNode.getLastChild());
    assertEquals(addReturnNode, addCallNode.getPrevSibling());

    Node googScopeNode = findNode(rootNode, SCOPING_METHOD_NAME);
    Node functionNode = findNode(rootNode, SCOPING_METHOD_NAME, "function");
    Node scopeBlockNode = findNode(rootNode, SCOPING_METHOD_NAME, "function", "{" );

    assertEquals(1, googScopeNode.getChildCount());
    assertEquals(1, functionNode.getChildCount());
    assertEquals(1, scopeBlockNode.getChildCount());

    assertEquals(resultNode, functionNode.getLastChild());
    assertEquals(googScopeNode, scopeBlockNode.getParent());
  }

  /**
   * Test that a goog.scope() with multiple statements results in correct alias
   * transformations.
   */
  @Test
  public void testMultipleStatements() throws Exception {
    String code = "goog.scope(function() {" +
                   "  function add(x, y) {" +
                   "    return x + y;" +
                   "  }" +
                   "  goog.dom.createElement('DIV');" +
                   "  goog.dom.createElement('DIV');" +
                   "  var result = add(2, 3);" +
                   "});";

    Node rootNode = parser.parse(compiler, sourceFile, code, null);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable,
        new LoggingHandler());
    pass.process(null, rootNode);

    List<String> expectedAliases = Lists.newArrayList("goog.dom", "DIV");
    List<Node> actualAliases = Lists.newArrayList();
    for (AliasUsage alias : pass.getAliasUsages()) {
      actualAliases.add(alias.aliasReference);
    }

    assertEquals(expectedAliases.size(), actualAliases.size());
    for (String alias : expectedAliases) {
      boolean found = false;
      for (Node node : actualAliases) {
        if (node.getString().equalsIgnoreCase(alias)) {
          found = true;
          break;
        }
      }
      assertTrue(found);
    }

    Node firstDivNode = findNode(rootNode, "goog.dom.createElement", "DIV");
    Node secondDivNode = findNode(rootNode, "goog.dom.createElement", "DIV");

    assertTrue(firstDivNode != null);
    assertTrue(secondDivNode != null);
    assertTrue(firstDivNode != secondDivNode);
  }

  /**
   * Test that a goog.scope() with an invalid usage (multiple statements, no
   * parameters, or a non-anonymous function) results in an error.
   */
  @Test(expected = AssertionError.class)
  public void testInvalidUsage() throws Exception {
    String code = "goog.scope({});";

    Node rootNode = parser.parse(compiler, sourceFile, code, null);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable,
        new LoggingHandler());
    pass.process(null, rootNode);
  }

  /**
   * Test that a goog.scope() with a function that references 'this' results in
   * an error.
   */
  @Test(expected = AssertionError.class)
  public void testThisReference() throws Exception {
    String code = "goog.scope(function() {" +
                   "  this.add = function(x, y) {" +
                   "    return x + y;" +
                   "  }" +
                   "});";

    Node rootNode = parser.parse(compiler, sourceFile, code, null);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable,
        new LoggingHandler());
    pass.process(null, rootNode);
  }

  /**
   * Test that a goog.scope() with a function that uses 'return' results in an error.
   */
  @Test(expected = AssertionError.class)
  public void testReturnStatement() throws Exception {
    String code = "goog.scope(function() {" +
                   "  return 1;" +
                   "});";

    Node rootNode = parser.parse(compiler, sourceFile, code, null);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable,
        new LoggingHandler());
    pass.process(null, rootNode);
  }

  /**
   * Test that a goog.scope() with a function that uses 'throw' results in an error.
   */
  @Test(expected = AssertionError.class)
  public void testThrowStatement() throws Exception {
    String code = "goog.scope(function() {" +
                   "  throw new Error('Test')" +
                   "});";

    Node rootNode = parser.parse(compiler, sourceFile, code, null);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable,
        new LoggingHandler());
    pass.process(null, rootNode);
  }

  /**
   * Test that a goog.scope() with a function that redefines an alias results in
   * an error.
   */
  @Test(expected = AssertionError.class)
  public void testAliasRedefinition() throws Exception {
    String code = "goog.scope(function() {" +
                   "  var goog = {'dom': function() {}};" +
                   "  goog.dom.createElement('DIV');" +
                   "});";

    Node rootNode = parser.parse(compiler, sourceFile, code, null);
    HotSwapCompilerPass pass = new ScopedAliases(compiler, preprocessorSymbolTable,
        new LoggingHandler());
    pass.process(null, rootNode);
  }

  private Node findNode(Node node, String... ids) {
    for (Node current = node; current != null; current = current.getNext()) {
      if (ids.length == 0) {
        return current;
      }
      String id = ids[0];
      if (id.equals(current.getString())) {
        ids = Arrays.copyOfRange(ids, 1, ids.length);
        if (ids.length == 0) {
          return current;
        }
      }
    }
    return null;
  }
}