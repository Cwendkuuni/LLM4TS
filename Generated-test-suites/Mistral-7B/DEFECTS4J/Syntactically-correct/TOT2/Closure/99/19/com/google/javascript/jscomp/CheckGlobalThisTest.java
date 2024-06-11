import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createAssign;
import static com.google.javascript.rhino.NodeFactory.createGetProp;

import org.junit.Test;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {
  @Test
  public void testCheckGlobalThis_Constructor() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new Compiler(null, null), CheckLevel.WARNING);
    JSDocInfo jsDoc = getFunctionJsDocInfo(createName("MyConstructor"));
    Node constructorNode = createFunction("MyConstructor", null, new Node[0], null);
    constructorNode.setJSDocInfo(jsDoc);

    assertFalse(checkGlobalThis.shouldTraverse(null, constructorNode, null));
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new Compiler(null, null), CheckLevel.WARNING);
    Node prototypeNode = createName("prototype");
    Node getterNode = createName("getMyProperty");
    Node functionNode = createFunction("getMyProperty", null, new Node[0], null);
    Node prototypeFunctionNode = createAssign(prototypeNode, createGetProp(prototypeNode, getterNode));
    prototypeFunctionNode.setJSDocInfo(getFunctionJsDocInfo(functionNode));

    assertTrue(checkGlobalThis.shouldTraverse(null, functionNode, prototypeFunctionNode));
  }

  @Test
  public void testCheckGlobalThis_FunctionAnnotatedWithThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new Compiler(null, null), CheckLevel.WARNING);

    JSDocInfo jsDoc = getFunctionJsDocInfo(createName("myFunction"));
    jsDoc.setThisType("MyType");
    Node functionNode = createFunction("myFunction", null, new Node[0], null);
    functionNode.setJSDocInfo(jsDoc);

    assertTrue(checkGlobalThis.shouldTraverse(null, functionNode, null));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new Compiler(null, null), CheckLevel.WARNING);

    Node functionNode = createFunction("myFunction", null, new Node[0], null);

    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, null));
  }

  @Test(expected = AssertionError.class)
  public void testCheckGlobalThis_GlobalThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new Compiler(null, null), CheckLevel.WARNING);

    Node globalThisNode = createName("this");

    checkGlobalThis.visit(null, globalThisNode, null);
  }

  private JSDocInfo getFunctionJsDocInfo(Node node) {
    // ...
  }
}