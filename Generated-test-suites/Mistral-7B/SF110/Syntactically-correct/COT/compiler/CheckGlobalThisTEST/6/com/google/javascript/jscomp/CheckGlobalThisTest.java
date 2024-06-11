import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFlag.IS_FUNCTION;
import static com.google.javascript.rhino.NodeFlag.IS_GET;
import static com.google.javascript.rhino.NodeFlag.IS_ASSIGN;
import static com.google.javascript.jscomp.CheckGlobalThis.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraversePrototypeFunctions() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node root = Node.newProgram(1, null, new Node[0]);
    Node obj = Node.newObjectLiteral(1, null, new Node[0]);
    Node prototype = Node.newObjectLiteral(1, null, new Node[0]);
    Node constructor = Node.newFunction(1, null, new Node[0]);
    Node prototypeFunction = Node.newFunction(1, null, new Node[0]);
    Node assign = Node.newAssign(1, null, obj, constructor);
    Node prototypeProperty = Node.newGetProp(1, prototype, "prototype");
    Node prototypeFunctionProperty = Node.newGetProp(1, prototypeFunction, "prototype");

    // Prototype functions should be traversed
    prototypeFunction.setFlag(IS_FUNCTION);
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse(prototypeFunction);
    assertTrue(prototypeFunction.getJSDocInfo().isConstructor());

    prototypeFunctionProperty.setFlag(IS_FUNCTION);
    traversal.traverse(prototypeFunctionProperty);
    assertTrue(prototypeFunctionProperty.getJSDocInfo().isConstructor());

    constructor.setFlag(IS_FUNCTION);
    constructor.setJSDocInfo(new JSDocInfo(constructor, null, null, null, null, null, null, null, null, null, null));
    constructor.getJSDocInfo().setInterface(true);
    traversal.traverse(constructor);
    assertFalse(traversal.isErrorThrown());

    traversal.reset();
    constructor.getJSDocInfo().setInterface(false);
    traversal.traverse(constructor);
    assertFalse(traversal.isErrorThrown());

    prototype.setFlag(IS_OBJECT_LITERAL);
    prototype.setJSDocInfo(new JSDocInfo(prototype, null, null, null, null, null, null, null, null, null, null));
    prototype.getJSDocInfo().setLendsName("prototype");
    traversal.traverse(prototype);
    assertFalse(traversal.isErrorThrown());

    traversal.reset();
    prototype.getJSDocInfo().setLendsName(null);
    traversal.traverse(prototype);
    assertFalse(traversal.isErrorThrown());

    // Function assigned to prototype property should be traversed
    assign.setFlag(IS_ASSIGN);
    Node lhs = assign.getFirstChild();
    lhs.setFlag(IS_GET);
    lhs.getLastChild().setString("prototype");
    Node rhs = assign.getLastChild();
    rhs.setFlag(IS_FUNCTION);
    NodeUtil.setParent(rhs, assign);
    traversal.traverse(rhs);
    assertTrue(rhs.getJSDocInfo().isConstructor());
  }

  @Test
  public void testShouldNotTraverseFunctionsOutsideGlobalContext() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node root = Node.newProgram(1, null, new Node[0]);

    Node notInGlobalContext = Node.newFunction(1, null, new Node[0]);
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse(notInGlobalContext);
    assertFalse(traversal.isErrorThrown());
  }

  @Test
  public void testShouldTraverseFunctionWithNoLhs() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node root = Node.newProgram(1, null, new Node[0]);
    Node functionWithoutLhs = Node.newFunction(1, null, new Node[0]);
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse(functionWithoutLhs);
    assertFalse(traversal.isErrorThrown());
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node root = Node.newProgram(1, null, new Node[0]);
    Node thisKeyword = Node.newThis();
    Node assign = Node.newAssign(1, null, Node.newName(1, null, "a"), thisKeyword);
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse(assign);
    assertTrue(traversal.isErrorThrown());
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThisKeywordOnLeftSideOfPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node root = Node.newProgram(1, null, new Node[0]);
    Node thisKeyword = Node.newThis();
    Node propertyAccess = Node.newGetProp(1, Node.newName(1, null, "a"), "prototype");
    Node property = Node.newName(1, null, "prototype");
    Node getProp = Node.newGetProp(1, thisKeyword, property);
    Node setProp = Node.newSetProp(1, Node.newName(1, null, "a"), getProp);
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse(setProp);
    assertTrue(traversal.isErrorThrown());
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node root = Node.newProgram(1, null, new Node[0]);
    Node thisKeyword = Node.newThis();
    Node assign = Node.newAssign(1, null, Node.newName(1, null, "a"), thisKeyword);
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse(assign);
    assertFalse(traversal.isErrorThrown());
  }

  @Test
  public void testShouldNotReportThisKeywordOnGetPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node root = Node.newProgram(1, null, new Node[0]);
    Node thisKeyword = Node.newThis();
    Node getProp = Node.newGetProp(1, Node.newName(1, null, "a"), "prototype");
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse(getProp);
    assertFalse(traversal.isErrorThrown());
  }

  @Test
  public void testShouldReportThisKeywordOnSetPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    Node root = Node.newProgram(1, null, new Node[0]);
    Node thisKeyword = Node.newThis();
    Node getProp = Node.newGetProp(1, Node.newName(1, null, "a"), "prototype");
    Node setProp = Node.newSetProp(1, Node.newName(1, null, "a"), getProp);
    NodeTraversal traversal = new NodeTraversal(root, checkGlobalThis);
    traversal.traverse(setProp);
    assertTrue(traversal.isErrorThrown());
  }
}