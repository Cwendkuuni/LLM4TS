import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.StaticScope;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.StaticType;
import com.google.javascript.rhino.jstype.TypeRegistry;
import com.google.javascript.rhino.jstype.types.JSTypeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ScopeTest {

  private static final String VAR_NAME = "testVar";
  private static final String FUNCTION_NAME = "testFunction";
  private static final int DEPTH = 3;
  private static final ObjectType THIS_TYPE = mock(ObjectType.class);
  private static final Node ROOT_NODE = createMockNode(Token.FUNCTION);
  private Scope scope;
  private CompilerInput input;
  private ErrorReporter errorReporter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    errorReporter = mock(ErrorReporter.class);
    input = new CompilerInput("test.js", "", null);
    TypeRegistry typeRegistry = new TypeRegistry();
    typeRegistry.registerType("Object", new JSTypeFactory() {
      @Override public ObjectType createType() {
        return new ObjectType(JSType.OBJECT, null);
      }
    });
    typeRegistry.registerType("Function", new JSTypeFactory<FunctionType>() {
      @Override public FunctionType createType() {
        return new FunctionType(JSType.FUNCTION, new ObjectType(JSType.OBJECT, null), null);
      }
    });
    scope = new Scope(null, ROOT_NODE, typeRegistry, THIS_TYPE, errorReporter);
  }

  @Test
  public void testConstructorWithRootNode() {
    assertEquals(0, scope.getDepth());
    assertEquals(null, scope.getParent());
    assertEquals(THIS_TYPE, scope.getTypeOfThis());
    assertFalse(scope.isBottom());
  }

  @Test
  public void testConstructorWithParentAndRootNode() {
    Scope parentScope = createMockScope();
    when(parentScope.getDepth()).thenReturn(DEPTH - 1);
    when(parentScope.getTypeOfThis()).thenReturn(THIS_TYPE);

    scope = new Scope(parentScope, ROOT_NODE, errorReporter);

    assertEquals(DEPTH, scope.getDepth());
    assertEquals(parentScope, scope.getParent());
    assertEquals(THIS_TYPE, scope.getTypeOfThis());
    assertFalse(scope.isBottom());
  }

  @Test
  public void testConstructorWithBottomType() {
    scope = new Scope(ROOT_NODE, new ObjectType(JSType.VOID, null));

    assertEquals(0, scope.getDepth());
    assertNull(scope.getParent());
    assertSame(THIS_TYPE, scope.getTypeOfThis());
    assertTrue(scope.isBottom());
  }

  @Test
  public void testDeclareVariableWithInferredType() {
    JSType type = new StaticType(JSType.NUMBER);
    Scope.Var variable = scope.declare(VAR_NAME, createMockNameNode(), type, input);

    assertEquals(VAR_NAME, variable.getName());
    assertEquals(input, variable.getInput());
    assertSame(type, variable.getType());
    assertTrue(variable.isTypeInferred());
  }

  @Test
  public void testDeclareVariableWithDeclaredType() {
    JSType type = new StaticType(JSType.STRING);
    Scope.Var variable = scope.declare(VAR_NAME, createMockNameNode(), type, input, false);

    assertEquals(VAR_NAME, variable.getName());
    assertEquals(input, variable.getInput());
    assertSame(type, variable.getType());
    assertFalse(variable.isTypeInferred());
  }

  @Test
  public void testUndelecareVariable() {
    Scope.Var variable = scope.declare(VAR_NAME, createMockNameNode(), JSType.UNDEFINED, input);
    scope.undeclare(variable);

    assertFalse(scope.getVars().hasNext());
  }

  @Test
  public void testGetVar() {
    Scope.Var variable = scope.declare(VAR_NAME, createMockNameNode(), JSType.UNDEFINED, input);

    Scope.Var result = scope.getVar(VAR_NAME);

    assertEquals(variable, result);
  }

  @Test
  public void testGetVarNull() {
    assertNull(scope.getVar("nonexistent"));
  }

  @Test
  public void testIsDeclared() {
    scope.declare(VAR_NAME, createMockNameNode(), JSType.UNDEFINED, input);

    assertTrue(scope.isDeclared(VAR_NAME, true));
    assertFalse(scope.isDeclared("nonexistent", true));
  }

  @Test
  public void testGetVars() {
    Scope.Var variable1 = scope.declare(VAR_NAME + "_1", createMockNameNode(), JSType.UNDEFINED, input);
    Scope.Var variable2 = scope.declare(VAR_NAME + "_2", createMockNameNode(), JSType.UNDEFINED, input);

    assertEquals(2, scope.getVars().size());
    Iterator<Scope.Var> iterator = scope.getVars();

    Scope.Var result1 = iterator.next();
    assertEquals(VAR_NAME + "_1", result1.getName());
    assertEquals(variable1, result1);

    Scope.Var result2 = iterator.next();
    assertEquals(VAR_NAME + "_2", result2.getName());
    assertEquals(variable2, result2);
  }

  @Test
  public void testGetVarCount() {
    Scope.Var variable1 = scope.declare(VAR_NAME + "_1", createMockNameNode(), JSType.UNDEFINED, input);
    Scope.Var variable2 = scope.declare(VAR_NAME + "_2", createMockNameNode(), JSType.UNDEFINED, input);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertFalse(scope.isLocal());
  }

  @Test
  public void testJoin() {
    Scope childScope = createMockScope();
    when(childScope.getDepth()).thenReturn(DEPTH + 1);
    when(childScope.getTypeOfThis()).thenReturn(THIS_TYPE);

    Scope parentScope = new Scope(null, ROOT_NODE, errorReporter);
    parentScope.declare(VAR_NAME, createMockNameNode(), JSType.UNDEFINED, input);
    parentScope.declare(FUNCTION_NAME, createMockNameNode(), new FunctionType(JSType.FUNCTION, new ObjectType(JSType.OBJECT, null), null), input);

    parentScope = new Scope(parentScope, ROOT_NODE, errorReporter);
    parentScope.declare(VAR_NAME + "_p", createMockNameNode(), JSType.UNDEFINED, input);

    Scope joinedScope = new Scope(childScope, ROOT_NODE, errorReporter);

    StaticScope<JSType> result = joinedScope.join(parentScope);

    assertEquals(parentScope, result);

    assertEquals(DEPTH + 1, result.getDepth());
    assertEquals(parentScope, result.getParentScope());
    assertEquals(THIS_TYPE, result.getTypeOfThis());

    Scope.Var variable = result.getSlot(VAR_NAME);
    assertEquals(scope.getSlot(VAR_NAME), variable);

    variable = result.getSlot(VAR_NAME + "_p");
    assertNotNull(variable);
  }

  @Test
  public void testGetSlot() {
    Scope.Var variable = scope.declare(VAR_NAME, createMockNameNode(), JSType.UNDEFINED, input);

    Scope.Var result = scope.getSlot(VAR_NAME);

    assertEquals(variable, result);
  }

  @Test
  public void testGetOwnSlotNull() {
    assertNull(scope.getOwnSlot("nonexistent"));
  }

  @Test
  public void testGetOwnSlot() {
    Scope.Var variable = scope.declare(VAR_NAME, createMockNameNode(), JSType.UNDEFINED, input);

    Scope.Var result = scope.getOwnSlot(VAR_NAME);

    assertEquals(variable, result);
  }

  private Node createMockNameNode() {
    Node nameNode = mock(Node.class);
    when(nameNode.getFirstChild()).thenReturn(null);
    when(nameNode.getName()).thenReturn("testName");
    return nameNode;
  }

  private Node createMockNode(int type) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(type);
    return node;
  }

  private Scope createMockScope() {
    Scope scope = mock(Scope.class);
    when(scope.getParent()).thenReturn(null);
    when(scope.getTypeOfThis()).thenReturn(THIS_TYPE);
    when(scope.getDepth()).thenReturn(DEPTH);
    return scope;
  }
}