import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique_SimpleFunction() {
    String jsCode = "function testFunction() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  function testInnerFunction() {\n" +
        "    var c = 3;\n" +
        "  }\n" +
        "}\n";

    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal);

    Node testFunctionNode = traversal.getRootNode().getFirstChild();
    assertEquals("testFunction$0", testFunctionNode.getQualifiedName());

    Node testInnerFunctionNode = testFunctionNode.getFirstChild().getLastChild();
    assertEquals("testInnerFunction$1", testInnerFunctionNode.getQualifiedName());

    Node aNode = testFunctionNode.getFirstChild().getNext().getNext();
    assertEquals("a$0", aNode.getQualifiedName());

    Node bNode = testFunctionNode.getFirstChild().getNext().getNext().getNext();
    assertEquals("b$1", bNode.getQualifiedName());

    Node cNode = testInnerFunctionNode.getFirstChild();
    assertEquals("c$2", cNode.getQualifiedName());
  }

  @Test
  public void testMakeDeclaredNamesUnique_FunctionWithParameters() {
    String jsCode = "function testFunction(a, b) {\n" +
        "  function testInnerFunction(c, d) {\n" +
        "    var e = 5;\n" +
        "  }\n" +
        "}\n";

    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal);

    Node testFunctionNode = traversal.getRootNode().getFirstChild();
    assertEquals("testFunction$0", testFunctionNode.getQualifiedName());

    Node testInnerFunctionNode = testFunctionNode.getFirstChild();
    assertEquals("testInnerFunction$1", testInnerFunctionNode.getQualifiedName());

    Node aNode = testFunctionNode.getFirstChild().getNext();
    assertEquals("a$0", aNode.getQualifiedName());

    Node bNode = testFunctionNode.getFirstChild().getNext().getNext();
    assertEquals("b$1", bNode.getQualifiedName());

    Node cNode = testInnerFunctionNode.getFirstChild();
    assertEquals("c$2", cNode.getQualifiedName());

    Node dNode = testInnerFunctionNode.getFirstChild().getNext().getNext();
    assertEquals("d$3", dNode.getQualifiedName());

    Node eNode = testInnerFunctionNode.getFirstChild().getNext().getNext().getNext();
    assertEquals("e$4", eNode.getQualifiedName());
  }

  @Test
  public void testMakeDeclaredNamesUnique_VariableWithSameNameInSameScope() {
    String jsCode = "function testFunction() {\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +
        "}\n";

    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal);

    Node testFunctionNode = traversal.getRootNode().getFirstChild();
    assertEquals("testFunction$0", testFunctionNode.getQualifiedName());

    Node firstA = testFunctionNode.getFirstChild().getNext();
    assertEquals("a$0", firstA.getQualifiedName());

    Node secondA = firstA.getNext();
    assertEquals("a$1", secondA.getQualifiedName());
  }

  @Test
  public void testMakeDeclaredNamesUnique_VariableWithSameNameInDifferentScopes() {
    String jsCode = "function testFunction() {\n" +
        "  var a = 1;\n" +
        "  function innerFunction() {\n" +
        "    var a = 2;\n" +
        "  }\n" +
        "}\n";

    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal);

    Node testFunctionNode = traversal.getRootNode().getFirstChild();
    assertEquals("testFunction$0", testFunctionNode.getQualifiedName());

    Node innerFunctionNode = testFunctionNode.getFirstChild();
    assertEquals("innerFunction$1", innerFunctionNode.getQualifiedName());

    Node firstA = testFunctionNode.getFirstChild().getNext();
    assertEquals("a$0", firstA.getQualifiedName());

    Node innerFunctionA = innerFunctionNode.getFirstChild();
    assertEquals("a$2", innerFunctionA.getQualifiedName());
  }

  @Test(expected = AssertionError.class)
  public void testMakeDeclaredNamesUnique_MissingFunctionDeclaration() {
    String jsCode = "function testFunction() {\n" +
        "  testInnerFunction();\n" +
        "}\n";

    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal);

    fail("Expected an AssertionError due to missing function declaration.");
  }

  @Test
  public void testMakeDeclaredNamesUnique_NestedFunctions() {
    String jsCode = "function testFunction() {\n" +
        "  function outerFunction() {\n" +
        "    function innerFunction() {\n" +
        "    }\n" +
        "  }\n" +
        "}\n";

    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal);

    Node testFunctionNode = traversal.getRootNode().getFirstChild();
    assertEquals("testFunction$0", testFunctionNode.getQualifiedName());

    Node outerFunctionNode = testFunctionNode.getFirstChild();
    assertEquals("outerFunction$1", outerFunctionNode.getQualifiedName());

    Node innerFunctionNode = outerFunctionNode.getFirstChild();
    assertEquals("innerFunction$2", innerFunctionNode.getQualifiedName());
  }

  @Test
  public void testMakeDeclaredNamesUnique_NestedFunctionsWithSameName() {
    String jsCode = "function testFunction() {\n" +
        "  function outerFunction() {\n" +
        "    function outerFunction() {\n" +
        "    }\n" +
        "  }\n" +
        "}\n";

    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal);

    Node testFunctionNode = traversal.getRootNode().getFirstChild();
    assertEquals("testFunction$0", testFunctionNode.getQualifiedName());

    Node outerFunctionNode = testFunctionNode.getFirstChild();
    assertEquals("outerFunction$3", outerFunctionNode.getQualifiedName());

    Node innerFunctionNode = outerFunctionNode.getFirstChild();
    assertEquals("outerFunction$4", innerFunctionNode.getQualifiedName());
  }

  @Test
  public void testMakeDeclaredNamesUnique_RecursiveFunction() {
    String jsCode = "function testFunction() {\n" +
        "  function innerFunction() {\n" +
        "    innerFunction();\n" +
        "  }\n" +
        "  innerFunction();\n" +
        "}\n";

    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    try {
      makeDeclaredNamesUnique.traverse(traversal);
      fail("Expected an AssertionError due to recursive function.");
    } catch (AssertionError e) {
      // Expected
    }

    // Make sure the test possibly fails since the recursive function isn't renamed.
    Node testFunctionNode = traversal.getRootNode().getFirstChild();
    assertEquals("testFunction$0", testFunctionNode.getQualifiedName());

    Node innerFunctionNode = testFunctionNode.getFirstChild();
    assertEquals("innerFunction$1", innerFunctionNode.getQualifiedName());
  }

  @Test
  public void testMakeDeclaredNamesUnique_CatchBlock() {
    String jsCode = "try {\n" +
        "  throw 'testError';\n" +
        "} catch (e) {\n" +
        "  function innerFunction() {\n" +
        "  }\n" +
        "}\n";

    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal);

    Node testFunctionNode = traversal.getRootNode();
    assertEquals("testFunction$0", testFunctionNode.getQualifiedName());

    Node catchBlockNode = testFunctionNode.getFirstChild().getNext();
    assertEquals("catchBlock$1", catchBlockNode.getQualifiedName());

    Node innerFunctionNode = catchBlockNode.getFirstChild();
    assertEquals("innerFunction$2", innerFunctionNode.getQualifiedName());
  }

  @Test
  public void testMakeDeclaredNamesUnique_FunctionWithVarAndParamSameName() {
    String jsCode = "function testFunction(a) {\n" +
        "  var a = 1;\n" +
        "}\n";

    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal);

    Node testFunctionNode = traversal.getRootNode().getFirstChild();
    assertEquals("testFunction$0", testFunctionNode.getQualifiedName());

    Node functionParamNode = testFunctionNode.getFirstChild();
    assertEquals("a$0", functionParamNode.getQualifiedName());

    Node localVarNode = testFunctionNode.getFirstChild().getNext();
    assertEquals("a$1", localVarNode.getQualifiedName());
  }

  @Test
  public void testMakeDeclaredNamesUnique_GlobalVarAndFunctionWithSameName() {
    String jsCode = "var a = 1;\n" +
        "function testFunction() {\n" +
        "  var a = 2;\n" +
        "}\n";

    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal);

    Node testFunctionNode = traversal.getRootNode().getFirstChild();
    assertEquals("testFunction$0", testFunctionNode.getQualifiedName());

    Node globalVarNode = traversal.getRootNode().getLastChild();
    assertEquals("a$0", globalVarNode.getQualifiedName());

    Node localVarNode = testFunctionNode.getFirstChild().getNext();
    assertEquals("a$1", localVarNode.getQualifiedName());
  }

  @Test
  public void testMakeDeclaredNamesUnique_GlobalVarAndFunctionWithSameName_Revert() {
    String jsCode = "var a = 1;\n" +
        "function testFunction() {\n" +
        "  var a = 2;\n" +
        "}\n";

    // Rename the global variable 'a' to 'a_original'
    NodeTraversal traversal = new NodeTraversal(new TokenStream(jsCode), null);
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(traversal);

    Node testFunctionNode = traversal.getRootNode().getFirstChild();
    assertEquals("testFunction$0", testFunctionNode.getQualifiedName());

    Node globalVarNode = traversal.getRootNode().getLastChild();
    assertEquals("a_original$0", globalVarNode.getQualifiedName());

    Node localVarNode = testFunctionNode.getFirstChild().getNext();
    assertEquals("a$1", localVarNode.getQualifiedName());

    // Revert the global variable 'a' back to its original name
    MakeDeclaredNamesUnique revert = new MakeDeclaredNamesUnique(makeDeclaredNamesUnique.rootRenamer);
    revert.traverse(traversal);

    assertEquals("a$0", globalVarNode.getQualifiedName());
  }
}