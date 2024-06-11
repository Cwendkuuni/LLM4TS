import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ScopeTest {

  @Test
  public void testConstructor() {
    Scope scope = new Scope(null, null);
    assertSame(Scope.class, scope.getClass());
    assertNull(scope.getParent());
    assertNull(scope.getRootNode());
    assertTrue(scope.isBottom());
    assertTrue(scope.getDepth() == 0);
    assertSame(ObjectType.NULL, scope.getTypeOfThis());

    Scope parentScope = new Scope(null, null);
    Scope scope1 = new Scope(parentScope, null);
    assertSame(parentScope, scope1.getParent());
    assertSame(null, scope1.getRootNode());
    assertFalse(scope1.isBottom());
    assertTrue(scope1.getDepth() == 1);
    assertSame(ObjectType.NULL, scope1.getTypeOfThis());

    Node rootNode = new Node(Token.FUNCTION);
    Scope scope2 = new Scope(parentScope, rootNode);
    assertSame(parentScope, scope2.getParent());
    assertSame(rootNode, scope2.getRootNode());
    assertFalse(scope2.isBottom());
    assertTrue(scope2.getDepth() == 2);
    assertSame(ObjectType.NULL, scope2.getTypeOfThis());

    ObjectType thisType = new ObjectType("Object");
    Scope scope3 = new Scope(parentScope, rootNode, thisType);
    assertSame(parentScope, scope3.getParent());
    assertSame(rootNode, scope3.getRootNode());
    assertFalse(scope3.isBottom());
    assertTrue(scope3.getDepth() == 2);
    assertSame(thisType, scope3.getTypeOfThis());
  }

  @Test
  public void testDeclare() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    String name = "name";
    Var var = scope.declare(name, null, null, null);
    assertTrue(var != null);
    assertTrue(scope.getVarCount() == 1);
    assertTrue(scope.isDeclared(name, false));
    assertTrue(var.isLocal());
    assertNull(var.getType());
    assertNull(var.getParentNode());
    assertNull(var.getNameNode());
    assertSame(scope, var.getScope());
    assertSame(name, var.getName());
    assertFalse(var.isDefine());
    assertFalse(var.isExtern());
    assertFalse(var.isConst());
    assertFalse(var.isNoShadow());
    assertNull(var.getInitialValue());
    assertFalse(var.isTypeInferred());
    assertNull(var.getInputName());
    assertTrue(var.equals(scope.getVar(name)));
    assertSame(scope, var.getScope());

    var = scope.declare(name, null, null, null, false);
    assertTrue(var != null);
    assertTrue(scope.getVarCount() == 1);
    assertTrue(scope.isDeclared(name, false));
    assertTrue(var.isLocal());
    assertNull(var.getType());
    assertNull(var.getParentNode());
    assertNull(var.getNameNode());
    assertSame(scope, var.getScope());
    assertSame(name, var.getName());
    assertFalse(var.isDefine());
    assertFalse(var.isExtern());
    assertFalse(var.isConst());
    assertFalse(var.isNoShadow());
    assertNull(var.getInitialValue());
    assertFalse(var.isTypeInferred());
    assertNull(var.getInputName());
    assertTrue(var.equals(scope.getVar(name)));
    assertSame(scope, var.getScope());

    JSType type = new ObjectType("Object");
    var = scope.declare(name, null, type, null);
    assertTrue(var != null);
    assertTrue(scope.getVarCount() == 1);
    assertTrue(scope.isDeclared(name, false));
    assertTrue(var.isLocal());
    assertSame(type, var.getType());
    assertNull(var.getParentNode());
    assertNull(var.getNameNode());
    assertSame(scope, var.getScope());
    assertSame(name, var.getName());
    assertFalse(var.isDefine());
    assertFalse(var.isExtern());
    assertFalse(var.isConst());
    assertFalse(var.isNoShadow());
    assertNull(var.getInitialValue());
    assertTrue(var.isTypeInferred());
    assertNull(var.getInputName());
    assertTrue(var.equals(scope.getVar(name)));
    assertSame(scope, var.getScope());

    CompilerInput input = new CompilerInput("test.js");
    var = scope.declare(name, null, type, input);
    assertTrue(var != null);
    assertTrue(scope.getVarCount() == 1);
    assertTrue(scope.isDeclared(name, false));
    assertTrue(var.isLocal());
    assertSame(type, var.getType());
    assertNull(var.getParentNode());
    assertNull(var.getNameNode());
    assertSame(scope, var.getScope());
    assertSame(name, var.getName());
    assertFalse(var.isDefine());
    assertFalse(var.isExtern());
    assertFalse(var.isConst());
    assertFalse(var.isNoShadow());
    assertNull(var.getInitialValue());
    assertTrue(var.isTypeInferred());
    assertSame(input, var.getInput());
    assertSame(name, var.getName());
    assertTrue(var.equals(scope.getVar(name)));
    assertSame(scope, var.getScope());

    var = scope.declare(name, null, type, input, false);
    assertTrue(var != null);
    assertTrue(scope.getVarCount() == 1);
    assertTrue(scope.isDeclared(name, false));
    assertTrue(var.isLocal());
    assertSame(type, var.getType());
    assertNull(var.getParentNode());
    assertNull(var.getNameNode());
    assertSame(scope, var.getScope());
    assertSame(name, var.getName());
    assertFalse(var.isDefine());
    assertFalse(var.isExtern());
    assertFalse(var.isConst());
    assertFalse(var.isNoShadow());
    assertNull(var.getInitialValue());
    assertFalse(var.isTypeInferred());
    assertSame(input, var.getInput());
    assertSame(name, var.getName());
    assertTrue(var.equals(scope.getVar(name)));
    assertSame(scope, var.getScope());
  }

  @Test
  public void testUndeleclare() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    String name = "name";
    Var var = scope.declare(name, null, null, null);
    assertTrue(var != null);
    assertTrue(scope.getVarCount() == 1);
    assertTrue(scope.isDeclared(name, false));
    assertTrue(var.isLocal());
    assertNull(var.getType());
    assertNull(var.getParentNode());
    assertNull(var.getNameNode());
    assertSame(scope, var.getScope());
    assertSame(name, var.getName());
    assertFalse(var.isDefine());
    assertFalse(var.isExtern());
    assertFalse(var.isConst());
    assertFalse(var.isNoShadow());
    assertNull(var.getInitialValue());
    assertFalse(var.isTypeInferred());
    assertNull(var.getInputName());
    assertTrue(var.equals(scope.getVar(name)));

    scope.undeclare(var);
    assertTrue(scope.getVarCount() == 0);
    assertFalse(scope.isDeclared(name, false));
    assertNull(scope.getVar(name));
  }

  @Test
  public void testGetVar() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    String name = "name";
    Var var1 = scope.declare(name, null, null, null);
    Var var2 = scope.declare(name, null, null, null);
    Var var3 = parentScope.declare(name, null, null, null);

    assertSame(var1, scope.getVar(name));
    assertFalse(var2.equals(scope.getVar(name)));
    assertSame(var3, parentScope.getVar(name));
  }

  @Test
  public void testIsDeclared() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    String name = "name";

    assertFalse(scope.isDeclared(name, false));

    Var var = scope.declare(name, null, null, null);
    assertTrue(scope.isDeclared(name, false));
    assertTrue(scope.isDeclared(name, true));

    Var varParent = parentScope.declare(name, null, null, null);
    assertTrue(scope.isDeclared(name, true));
  }

  @Test
  public void testGetVars() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    String name1 = "name1";
    String name2 = "name2";

    Var var1 = scope.declare(name1, null, null, null);
    Var var2 = scope.declare(name2, null, null, null);

    Iterator<Var> iterator = scope.getVars();

    Var var1Iterator = iterator.next();
    Var var2Iterator = iterator.next();

    assertSame(var1, var1Iterator);
    assertSame(var2, var2Iterator);
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testGetVarCount() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    String name1 = "name1";
    String name2 = "name2";

    Var var1 = scope.declare(name1, null, null, null);
    Var var2 = scope.declare(name2, null, null, null);

    int count = scope.getVarCount();
    assertTrue(count == 2);
  }

  @Test
  public void testIsGlobal() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    assertTrue(scope.isLocal());
    assertTrue(parentScope.isGlobal());
  }

  @Test
  public void testIsExtern() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    CompilerInput input = new CompilerInput("test.js");
    String name = "name";

    Var var = scope.declare(name, null, null, input);
    assertTrue(var.isExtern());

    input = new CompilerInput("test2.js");
    Var var2 = scope.declare(name, null, null, input);
    assertFalse(var2.isExtern());
  }

  @Test
  public void testIsConst() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    Node nameNode = new Node(Token.NAME, "name");
    Node nameNodeConst = new Node(Token.NAME, "nameconst");

    Var var = scope.declare(nameNode.getString(), nameNode, null, null);
    Var varConst = scope.declare(nameNodeConst.getString(), nameNodeConst, null, null);

    assertTrue(var.isConst());
    assertFalse(varConst.isConst());
  }

  @Test
  public void testIsNoShadow() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    String name = "name";
    JSDocInfo info = new JSDocInfo();
    info.setNoShadow(true);
    Var var = scope.declare(name, null, null, null, null, info);
    assertTrue(var.isNoShadow());

    Var var2 = scope.declare(name, null, null, null);
    assertFalse(var2.isNoShadow());
  }

  @Test
  public void testTypeOfThis() {
    Scope parentScope = new Scope(null, null);
    Scope scope = new Scope(parentScope, null);

    ObjectType thisType = new ObjectType("Object");
    Scope scope1 = new Scope(parentScope, null, thisType);

    assertTrue(scope.getTypeOfThis() == null);
    assertTrue(scope1.getTypeOfThis() == thisType);
  }
}