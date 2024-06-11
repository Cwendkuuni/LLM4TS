import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.StaticScope;
import com.google.javascript.jscomp.TypeRegistry;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.functions.FunctionType;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Test class for the Scope class.
 */
public class ScopeTest {

  private Node rootNode;
  private Scope parentScope;
  private Scope scopeUnderTest;
  private ErrorReporter errorReporter;
  private TypeRegistry typeRegistry;
  private AbstractCompiler compiler;

  @Override
  public void setUp() {
    rootNode = mock(Node.class);
    parentScope = mock(Scope.class);
    scopeUnderTest = new Scope(parentScope, rootNode);
    errorReporter = mock(ErrorReporter.class);
    typeRegistry = mock(TypeRegistry.class);
    compiler = mock(AbstractCompiler.class);
  }

  @org.junit.Test
  public void shouldCreateAScopeWithParentAndRootNode() {
    // Given
    Node parentRootNode = mock(Node.class);
    Scope parentScope = mock(Scope.class);

    // When
    Scope scope = new Scope(parentScope, rootNode);

    // Then
    assertEquals(parentScope, scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @org.junit.Test
  public void shouldCreateAGlobalScopeWithRootNode() {
    // Given

    // When
    Scope scope = new Scope(rootNode, typeRegistry, errorReporter, compiler);

    // Then
    assertNull(scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
  }

  @org.junit.Test
  public void shouldCreateABottomScopeWithRootNode() {
    // Given

    // When
    Scope scope = new Scope(rootNode, new ObjectType(null, new FunctionType(null, null)));

    // Then
    assertNull(scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
    assertTrue(scope.isBottom());
  }

  @org.junit.Test
  public void shouldReturnTheParentScope() {
    // Given

    // When
    Scope scope = new Scope(parentScope, rootNode);

    // Then
    assertEquals(parentScope, scope.getParent());
  }

  @org.junit.Test
  public void shouldReturnTheRootNode() {
    // Given

    // When
    Scope scope = new Scope(parentScope, rootNode);

    // Then
    assertEquals(rootNode, scope.getRootNode());
  }

  @org.junit.Test
  public void shouldReturnTheTypeOfThis() {
    // Given
    ObjectType thisType = mock(ObjectType.class);

    // When
    Scope scope = new Scope(parentScope, rootNode);
    when(rootNode.getJSType()).thenReturn(new FunctionType(null, null));
    doReturn(thisType).when(scope).getTypeOfThis();

    // Then
    assertEquals(thisType, scope.getTypeOfThis());
  }

  @org.junit.Test
  public void shouldReturnFalseForIsBottom() {
    // Given
    Scope scope = new Scope(parentScope, rootNode);

    // Then
    assertFalse(scope.isBottom());
  }

  @org.junit.Test
  public void shouldReturnTrueForIsBottomIfScopeIsBottom() {
    // Given
    Scope scope = new Scope(parentScope, rootNode, new ObjectType(null, null));

    // Then
    assertTrue(scope.isBottom());
  }

  @org.junit.Test
  public void shouldReturnTheParentScopeDepth() {
    // Given
    int parentScopeDepth = 3;
    when(parentScope.getDepth()).thenReturn(parentScopeDepth);

    // When
    Scope scope = new Scope(parentScope, rootNode);

    // Then
    assertEquals(parentScopeDepth + 1, scope.getDepth());
  }

  @org.junit.Test
  public void shouldDeclareAVar() {
    // Given
    String variableName = "variableName";
    Node variableNameNode = mock(Node.class);
    JSType variableType = mock(JSType.class);
    CompilerInput input = mock(CompilerInput.class);

    // When
    Scope scope = new Scope(parentScope, rootNode);
    Var variable = scope.declare(variableName, variableNameNode, variableType, input);

    // Then
    assertTrue(scope.vars.containsKey(variableName));
    assertEquals(variable, scope.vars.get(variableName));
    verify(variableNameNode).setParent(scope.rootNode);
  }

  @org.junit.Test
  public void shouldDeclareAVarWithInferredType() {
    // Given
    String variableName = "variableName";
    Node variableNameNode = mock(Node.class);
    JSType variableType = mock(JSType.class);
    CompilerInput input = mock(CompilerInput.class);

    // When
    Scope scope = new Scope(parentScope, rootNode);
    Var variable = scope.declare(variableName, variableNameNode, variableType, input, true);

    // Then
    assertTrue(scope.vars.containsKey(variableName));
    assertEquals(variable, scope.vars.get(variableName));
    verify(variableNameNode).setParent(scope.rootNode);
  }

  @org.junit.Test
  public void shouldReturnVarWhenGettingDeclaredVar() {
    // Given
    String variableName = "variableName";
    Node variableNameNode = mock(Node.class);
    JSType variableType = mock(JSType.class);
    CompilerInput input = mock(CompilerInput.class);
    Var variable = mock(Var.class);

    // When
    Scope scope = new Scope(parentScope, rootNode);
    when(scope.vars.get(variableName)).thenReturn(variable);

    // Then
    assertEquals(variable, scope.getVar(variableName));
  }

  @org.junit.Test
  public void shouldReturnNullWhenGettingUndeclaredVar() {
    // Given
    String variableName = "variableName";

    // When
    Scope scope = new Scope(parentScope, rootNode);

    // Then
    assertNull(scope.getVar(variableName));
  }

  @org.junit.Test
  public void shouldReturnVarWhenGettingDeclaredVarFromParentScope() {
    // Given
    String variableName = "variableName";
    Node variableNameNode = mock(Node.class);
    JSType variableType = mock(JSType.class);
    CompilerInput input = mock(CompilerInput.class);
    Var variable = mock(Var.class);

    // When
    Scope parentScope = mock(Scope.class);
    when(parentScope.vars.get(variableName)).thenReturn(variable);
    Scope scope = new Scope(parentScope, rootNode);

    // Then
    assertEquals(variable, scope.getVar(variableName));
  }

  @org.junit.Test
  public void shouldReturnNumberOfDeclaredVars() {
    // Given
    int numberOfVars = 5;

    // When
    Scope scope = new Scope(parentScope, rootNode);
    for (int i = 0; i < numberOfVars; i++) {
      String variableName = "variableName" + i;
      Node variableNameNode = mock(Node.class);
      JSType variableType = mock(JSType.class);
      CompilerInput input = mock(CompilerInput.class);
      scope.declare(variableName, variableNameNode, variableType, input);
    }

    // Then
    assertEquals(numberOfVars, scope.getVarCount());
  }

  @org.junit.Test
  public void shouldReturnIteratorOverDeclaredVars() {
    // Given
    int numberOfVars = 5;

    // When
    Scope scope = new Scope(parentScope, rootNode);
    for (int i = 0; i < numberOfVars; i++) {
      String variableName = "variableName" + i;
      Node variableNameNode = mock(Node.class);
      JSType variableType = mock(JSType.class);
      CompilerInput input = mock(CompilerInput.class);
      scope.declare(variableName, variableNameNode, variableType, input);
    }

    // Then
    Iterator<Var> iterator = scope.getVars();
    int counter = 0;
    while (iterator.hasNext()) {
      iterator.next();
      counter++;
    }

    assertEquals(numberOfVars, counter);
  }

  @org.junit.Test
  public void shouldReturnFalseIfVariableIsNotDeclared() {
    // Given
    String variableName = "variableName";
    Scope scope = new Scope(parentScope, rootNode);

    // Then
    assertFalse(scope.isDeclared(variableName, false));
  }

  @org.junit.Test
  public void shouldReturnTrueIfVariableIsDeclaredInParentScope() {
    // Given
    String variableName = "variableName";
    Node variableNameNode = mock(Node.class);
    JSType variableType = mock(JSType.class);
    CompilerInput input = mock(CompilerInput.class);
    Var variable = mock(Var.class);
    Scope parentScope = mock(Scope.class);
    when(parentScope.vars.get(variableName)).thenReturn(variable);

    // When
    Scope scope = new Scope(parentScope, rootNode);

    // Then
    assertTrue(scope.isDeclared(variableName, true));
  }

  @org.junit.Test
  public void shouldReturnTrueIfVariableIsDeclaredInCurrentScope() {
    // Given
    String variableName = "variableName";
    Node variableNameNode = mock(Node.class);
    JSType variableType = mock(JSType.class);
    CompilerInput input = mock(CompilerInput.class);
    Var variable = mock(Var.class);
    Scope scope = new Scope(parentScope, rootNode);
    when(scope.vars.get(variableName)).thenReturn(variable);

    // When

    // Then
    assertTrue(scope.isDeclared(variableName, false));
  }
}