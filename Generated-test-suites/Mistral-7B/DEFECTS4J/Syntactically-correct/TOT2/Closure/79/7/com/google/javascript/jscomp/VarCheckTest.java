import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VarCheck;

public class VarCheckTest {

  @Test
  public void testValidVariableDeclaration() {
    // Given a valid variable declaration
    CompilerInput input = new CompilerInput("test.js", "input");
    Node root = Node.newProgram(input);
    VarCheck varCheck = new VarCheck(null);

    // When the VarCheck passes
    NodeTraversal traversal = new NodeTraversal(input, null, varCheck);
    traversal.traverse(root);

    // Then no errors are reported
    assertTrue(varCheck.sanityCheck());
  }

  @Test
  public void testUndefinedVariable() {
    // Given a variable that is not declared
    CompilerInput input = new CompilerInput("test.js", "input");
    Node root = Node.newProgram(input);
    Node varNode = Node.newName("undefinedVar");

    // When the VarCheck passes
    VarCheck varCheck = new VarCheck(null);
    NodeTraversal traversal = new NodeTraversal(input, null, varCheck);
    traversal.traverse(root);

    // Then an error is reported
    assertEquals("variable undefinedVar is undefined",
        varCheck.getErrors().get(0).getMessage());
  }

  @Test
  public void testValidFunctionDeclaration() {
    // Given a valid function declaration
    CompilerInput input = new CompilerInput("test.js", "input");
    Node root = Node.newProgram(input);
    Node functionNode = Node.newFunctionDeclaration("myFunction");
    root.addChildToBack(functionNode);

    // When the VarCheck passes
    VarCheck varCheck = new VarCheck(null);
    NodeTraversal traversal = new NodeTraversal(input, null, varCheck);
    traversal.traverse(root);

    // Then no errors are reported
    assertTrue(varCheck.sanityCheck());
  }

  @Test
  public void testMissingModuleDependency() {
    // Given a module that references another module without declaring it
    CompilerInput input1 = new CompilerInput("module1.js", "input1");
    Node root1 = Node.newProgram(input1);
    Node refNode = Node.newGetProperty(Node.newName("otherModule"), "someFunction");
    root1.addChildToBack(refNode);

    CompilerInput input2 = new CompilerInput("module2.js", "input2");
    Node root2 = Node.newProgram(input2);
    Node functionNode = Node.newFunctionDeclaration("someFunction");
    root2.addChildToBack(functionNode);

    // When the VarCheck passes
    VarCheck varCheck = new VarCheck(null);
    NodeTraversal traversal = new NodeTraversal(input1, input2, varCheck);
    traversal.traverseRoots(root1, root2);

    // Then a warning is reported
    assertEquals("missing module dependency; module module1.js should depend " +
        "on module2.js because it references someFunction",
        varCheck.getWarnings().get(0).getMessage());
  }
}