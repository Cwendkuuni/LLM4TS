import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(null, null, null);

    // Test a valid goog.scope call
    Node root = Node.newNode("(function() {\n" +
        "  var test = goog.dom;\n" +
        "  test.createElement('div');\n" +
        "})()");
    scopedAliases.process(null, root);
    assertTrue(scopedAliases.hasErrors());

    // Test an invalid goog.scope call
    Node invalidScopeCall = Node.newNode("(function() {\n" +
        "  var test = goog.scope(function() {\n" +
        "    goog.dom.createElement('div'); // This should cause an error\n" +
        "  });\n" +
        "})()");
    scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, invalidScopeCall);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Test an invalid goog.scope call with multiple statements
    Node invalidScopeCallMultipleStatements = Node.newNode("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    var test1 = goog.dom;\n" +
        "    test1.createElement('div');\n" +
        "    var test2 = goog.dom;\n" +
        "    test2.createElement('div');\n" +
        "  });\n" +
        "})()");
    scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, invalidScopeCallMultipleStatements);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Test a goog.scope call with a function having 'this' keyword
    Node scopeCallWithThisKeyword = Node.newNode("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    function test() {\n" +
        "      console.log(this); // This should cause an error\n" +
        "    }\n" +
        "    test();\n" +
        "  });\n" +
        "})()");
    scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, scopeCallWithThisKeyword);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Test a goog.scope call with a function having 'return' statement
    Node scopeCallWithReturnStatement = Node.newNode("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    return 5;\n" +
        "  });\n" +
        "})()");
    scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, scopeCallWithReturnStatement);
    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    // Test a valid use of a scoped alias
    Node validUseOfScopedAlias = Node.newNode("(function() {\n" +
        "  goog.scope(function() {\n" +
        "    var test = goog.dom;\n" +
        "    test.createElement('div');\n" +
        "  });\n" +
        "})()\n" +
        "goog.dom.createElement('div');");
    scopedAliases = new ScopedAliases(null, null, null);
    scopedAliases.process(null, validUseOfScopedAlias);
    assertFalse(scopedAliases.hasErrors());
  }
}