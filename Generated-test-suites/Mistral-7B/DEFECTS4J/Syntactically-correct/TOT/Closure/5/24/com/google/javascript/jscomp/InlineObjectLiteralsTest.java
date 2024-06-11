import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.NodeFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerResult result;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.setOptions(new CompilerOptions() {{
      setSkipExternalLibraries(true);
    }});
    nodeFactory = compiler.getOptions().nodeFactory;
  }

  @After
  public void tearDown() {
    compiler = null;
    result = null;
  }

  @Test
  public void testInlineObjectLiterals() {
    String testCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {'c': 2, d: 3}" +
        "  };" +
        "  var result = obj.b.c;" +
        "  return result;" +
        "}";

    Node root = nodeFactory.parse(testCode);

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "temp");
    inlineObjectLiterals.process(null, root);

    result = compiler.compile(root);

    assertNotNull(result);
    Node resultRoot = result.getOutput();
    assertNotNull(resultRoot);

    // Check that the object literal has been expanded into individual variables
    Node objectLiteral = resultRoot.findFirstDescendant(node -> node.isObjLit());
    assertNull(objectLiteral);

    // Check that the variables have been declared and initialized correctly
    Node varA = resultRoot.findFirstDescendant(node -> node.isVar() && node.getName().equals("temp_a"));
    Node varB = resultRoot.findFirstDescendant(node -> node.isVar() && node.getName().equals("temp_b_c"));
    Node varC = resultRoot.findFirstDescendant(node -> node.isVar() && node.getName().equals("temp_b_d"));
    Node assignResult = resultRoot.findFirstDescendant(node -> node.isAssign() && node.getLastChild().isName() && node.getLastChild().getName().equals("result"));

    assertNotNull(varA);
    assertNotNull(varB);
    assertNotNull(varC);
    assertNotNull(assignResult);

    // Check that the variable values match the expected values
    assertEquals(1, varA.getDoubleValue());
    assertEquals(2, varB.getDoubleValue());
    assertNull(varC.getValue()); // varC should be undefined as it's not explicitly initialized

    // Check that the return statement still returns the expected value
    Node returnStatement = resultRoot.findFirstDescendant(node -> node.isReturnStatement());
    assertNotNull(returnStatement);
    assertEquals(2, returnStatement.getFirstChild().getDoubleValue());
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReferentialAssignment() {
    String testCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {" +
        "      c: obj.a," +
        "      d: 2" +
        "    }" +
        "  };" +
        "  return obj.b.c;" +
        "}";

    Node root = nodeFactory.parse(testCode);

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "temp");
    inlineObjectLiterals.process(null, root);

    result = compiler.compile(root);

    assertNotNull(result);
    Node resultRoot = result.getOutput();
    assertNotNull(resultRoot);

    // Check that the object literal has not been expanded into individual variables
    Node objectLiteral = resultRoot.findFirstDescendant(node -> node.isObjLit());
    assertNotNull(objectLiteral);

    // Check that the return statement still returns the expected value
    Node returnStatement = resultRoot.findFirstDescendant(node -> node.isReturnStatement());
    assertNotNull(returnStatement);
    assertEquals(1, returnStatement.getFirstChild().getDoubleValue()); // The self-referential assignment should not be expanded
  }

  @Test
  public void testInlineObjectLiteralsWithDeletedProperty() {
    String testCode = "function foo() {" +
        "  delete foo.obj.b;" +
        "  return foo.obj.b;" +
        "}";

    Node root = nodeFactory.parse(testCode);

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "temp");
    inlineObjectLiterals.process(null, root);

    result = compiler.compile(root);

    assertNotNull(result);
    Node resultRoot = result.getOutput();
    assertNotNull(resultRoot);

    // Check that the object literal has not been expanded into individual variables
    Node objectLiteral = resultRoot.findFirstDescendant(node -> node.isObjLit());
    assertNotNull(objectLiteral);

    // Check that the return statement throws an error
    Node returnStatement = resultRoot.findFirstDescendant(node -> node.isReturnStatement());
    assertNotNull(returnStatement);
    assertEquals(Token.ERROR, returnStatement.getType());
  }
}