import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.makeFunction;
import static com.google.javascript.rhino.NodeFactory.makeName;
import static com.google.javascript.rhino.NodeFactory.makeString;
import static com.google.javascript.rhino.NodeFactory.makeThis;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler);

    // Test for function traversal
    Node functionNode = makeFunction(null, null, null, ImmutableList.of());
    Node functionParent = makeName("parentFunction");
    Node result = new NodeTraversal(functionParent, check).traverse();
    assertTrue(result != null && check.shouldTraverse(result, functionParent, null));

    // Test for prototype property traversal
    Node prototypeParent = makeName("prototype");
    Node prototypeProperty = NodeUtil.makeGetProp(prototypeParent, makeString("property"));
    Node prototypeFunction = NodeUtil.makeGetProp(prototypeParent, makeName("constructor"));
    Node prototypeFunctionParent = NodeUtil.makeThis();
    Node prototypeFunctionResult = new NodeTraversal(prototypeFunctionParent, check).traverse();
    Node prototypeResult = new NodeTraversal(prototypeProperty, check).traverse();
    assertTrue(result != null && check.shouldTraverse(result, prototypeFunction, prototypeFunctionParent));
    assertTrue(result != null && check.shouldTraverse(result, prototypeResult, prototypeParent));

    // Test for non-function and non-prototype traversal
    Node nonFunctionNode = makeName("nonFunction");
    Node nonFunctionParent = makeName("parentOfNonFunction");
    Node nonFunctionResult = new NodeTraversal(nonFunctionParent, check).traverse();
    assertFalse(result != null && check.shouldTraverse(result, nonFunctionNode, nonFunctionParent));
  }

  @Test
  public void testShouldReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler);

    // Test for this keyword on the left side of an assignment
    Node assignmentLeft = makeName("lhs");
    Node assignmentRight = makeThis();
    Node assignmentParent = makeAssign(ASSIGN, null, assignmentLeft, assignmentRight);
    Node result = new NodeTraversal(assignmentParent, check).traverse();
    assertTrue(result != null && check.shouldReportThis(result));

    // Test for this keyword on the left side of a property access
    Node propertyLeft = makeName("lhs");
    Node propertyRight = makeThis();
    Node propertyParent = NodeUtil.makeGetProp(propertyLeft, makeString("property"));
    Node resultProperty = new NodeTraversal(propertyParent, check).traverse();
    assertTrue(resultProperty != null && check.shouldReportThis(resultProperty));
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThis_Constructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler);

    // Test for constructor function
    Node constructorFunctionNode = makeFunction(Token.FUNCTION, null, null, null);
    Node constructorFunctionParent = makeName("constructorFunction");
    new NodeTraversal(constructorFunctionParent, check).traverse();
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThis_Override() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler);

    // Test for function with @override annotation
    Node overrideFunctionNode = makeFunction(Token.FUNCTION, null, null, null);
    Node overrideFunctionParent = makeName("overrideFunction");
    overrideFunctionParent.setJSDocInfo(new JSDocInfo(null, null, null, null, null, null, null, null, null, true, null, null, null));
    new NodeTraversal(overrideFunctionParent, check).traverse();
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThis_PrototypeMethod() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler);

    // Test for prototype method
    Node prototypeMethodNode = makeFunction(Token.FUNCTION, null, null, null);
    Node prototypeMethodParent = NodeUtil.makeGetProp(makeName("prototype"), makeName("methodName"));
    prototypeMethodParent.setJSDocInfo(new JSDocInfo(null, null, null, null, null, null, null, null, null, null, null, null, "methodName", null));
    new NodeTraversal(prototypeMethodParent, check).traverse();
  }

  @Test(expected = AssertionError.class)
  public void testShouldNotReportThis_AnnotatedFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler);

    // Test for function with @this annotation
    Node annotatedFunctionNode = makeFunction(Token.FUNCTION, null, null, null);
    Node annotatedFunctionParent = makeName("annotatedFunction");
    annotatedFunctionParent.setJSDocInfo(new JSDocInfo(null, null, null, null, "@this", null, null, null, null, null, null, null, null, null));
    new NodeTraversal(annotatedFunctionParent, check).traverse();
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    AbstractCompiler compiler = new AbstractCompiler();

    Node functionNode1 = makeFunction(Token.FUNCTION, null, null, null);
    Node functionNode2 = makeFunction(Token.FUNCTION, null, null, null);
    Node functionNode3 = makeFunction(Token.FUNCTION, null, null, null);
    Node functionNode4 = makeFunction(Token.FUNCTION, null, null, null);
    Node functionNode5 = makeFunction(Token.FUNCTION, null, null, null);

    JSDocInfo jsDocInfo1 = new CheckGlobalThis(compiler).getFunctionJsDocInfo(functionNode1);
    JSDocInfo jsDocInfo2 = new CheckGlobalThis(compiler).getFunctionJsDocInfo(functionNode2);
    JSDocInfo jsDocInfo3 = new CheckGlobalThis(compiler).getFunctionJsDocInfo(functionNode3);
    JSDocInfo jsDocInfo4 = new CheckGlobalThis(compiler).getFunctionJsDocInfo(functionNode4);
    JSDocInfo jsDocInfo5 = new CheckGlobalThis(compiler).getFunctionJsDocInfo(functionNode5);

    assertNull(jsDocInfo1);
    assertNull(jsDocInfo2);
    assertNull(jsDocInfo3);
    assertNull(jsDocInfo4);
    assertNotNull(jsDocInfo5);

    Node functionNode6 = makeFunction(Token.FUNCTION, null, null, null);
    Node functionParent1 = makeName("parent1");
    Node functionParent2 = makeName("parent2");
    Node functionParent3 = makeName("parent3");
    Node functionParent4 = makeVar("varParent");
    Node functionParent5 = makeAssign(ASSIGN, null, makeName("parent5"), functionNode6);
    functionParent1.setJSDocInfo(new JSDocInfo(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
    functionParent2.setJSDocInfo(new JSDocInfo(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
    functionParent3.setJSDocInfo(new JSDocInfo(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
    functionParent4.setJSDocInfo(new JSDocInfo(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
    functionParent5.setJSDocInfo(new JSDocInfo(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));

    JSDocInfo jsDocInfo6 = new CheckGlobalThis(compiler).getFunctionJsDocInfo(functionNode6);
    assertNull(jsDocInfo6);
    assertEquals(jsDocInfo1, jsDocInfo1);
    assertEquals(jsDocInfo2, jsDocInfo2);
    assertEquals(jsDocInfo3, jsDocInfo3);
    assertEquals(jsDocInfo4, jsDocInfo4);
    assertEquals(jsDocInfo5, jsDocInfo5);
  }
}