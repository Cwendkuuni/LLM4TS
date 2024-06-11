import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourceName;
import com.google.javascript.rhino.compiler.CompilerInput;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;

import java.util.Map;

public class ScopeTest {

    @Test
    public void testScopeCreation() {
        Node rootNode = new Node(FUNCTION());
        Scope scope = new Scope(null, rootNode);
        assertFalse(scope.isBottom());
        assertEquals(0, scope.getDepth());
        assertNull(scope.getParent());
        assertNull(scope.getTypeOfThis());

        Node globalRootNode = new Node(BLOCK());
        Scope globalScope = new Scope(globalRootNode, null);
        assertTrue(globalScope.isBottom());
        assertEquals(0, globalScope.getDepth());
        assertNull(globalScope.getParent());
        assertNotNull(globalScope.getTypeOfThis());
    }

    @Test
    public void testDeclareVariable() {
        Node rootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("x")),
                VAR_DECLARATION(ID("y"))));
        Scope scope = new Scope(null, rootNode);

        Var xVar = scope.declare("x", rootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));
        Var yVar = scope.declare("y", rootNode.getChild(1), INT, new CompilerInput(new SourceName("test.js"), 0));

        assertEquals("x", xVar.getName());
        assertEquals(0, xVar.getIndex());
        assertEquals(INT, xVar.getType());
        assertEquals(Scope.State.DECLARED, xVar.getState());

        assertEquals("y", yVar.getName());
        assertEquals(1, yVar.getIndex());
        assertEquals(INT, yVar.getType());
        assertEquals(Scope.State.DECLARED, yVar.getState());

        assertNull(scope.getVar("z"));
    }

    @Test
    public void testUndelecareVariable() {
        Node rootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("x")),
                VAR_DECLARATION(ID("y"))));
        Scope scope = new Scope(null, rootNode);

        Var xVar = scope.declare("x", rootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));
        Var yVar = scope.declare("y", rootNode.getChild(1), INT, new CompilerInput(new SourceName("test.js"), 0));
        scope.undeclare(xVar);

        assertNull(scope.getVar("x"));
        assertEquals("y", yVar.getName());
    }

    @Test
    public void testGetVar() {
        Node rootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("x")),
                VAR_DECLARATION(ID("y"))));
        Scope scope = new Scope(null, rootNode);

        Var xVar = scope.declare("x", rootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));
        Var yVar = scope.declare("y", rootNode.getChild(1), INT, new CompilerInput(new SourceName("test.js"), 0));

        assertEquals(xVar, scope.getVar("x"));
        assertEquals(yVar, scope.getVar("y"));
        assertNull(scope.getVar("z"));
    }

    @Test
    public void testIsDeclared() {
        Node rootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("x")),
                VAR_DECLARATION(ID("y"))));
        Scope scope = new Scope(null, rootNode);

        Var xVar = scope.declare("x", rootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));
        Var yVar = scope.declare("y", rootNode.getChild(1), INT, new CompilerInput(new SourceName("test.js"), 0));

        assertTrue(scope.isDeclared("x"));
        assertTrue(scope.isDeclared("y"));
        assertFalse(scope.isDeclared("z"));

        Node parentRootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("z"))));
        Scope parentScope = new Scope(scope, parentRootNode);

        Var zVar = parentScope.declare("z", parentRootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));

        assertTrue(parentScope.isDeclared("z"));
        assertFalse(parentScope.isDeclared("x"));
        assertFalse(parentScope.isDeclared("y"));
    }

    @Test
    public void testGetVars() {
        Node rootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("x")),
                VAR_DECLARATION(ID("y"))));
        Scope scope = new Scope(null, rootNode);

        Var xVar = scope.declare("x", rootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));
        Var yVar = scope.declare("y", rootNode.getChild(1), INT, new CompilerInput(new SourceName("test.js"), 0));

        Iterator<Var> varIterator = scope.getVars().iterator();
        Var x = varIterator.next();
        Var y = varIterator.next();

        assertEquals(xVar, x);
        assertEquals(yVar, y);
        assertFalse(varIterator.hasNext());
    }

    @Test
    public void testGetVarCount() {
        Node rootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("x")),
                VAR_DECLARATION(ID("y"))));
        Scope scope = new Scope(null, rootNode);

        Var xVar = scope.declare("x", rootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));
        Var yVar = scope.declare("y", rootNode.getChild(1), INT, new CompilerInput(new SourceName("test.js"), 0));

        assertEquals(2, scope.getVarCount());
    }

    @Test
    public void testIsGlobalScope() {
        Node rootNode = new Node(BLOCK());
        Scope scope = new Scope(rootNode, null);
        assertTrue(scope.isGlobal());

        Node functionRootNode = new Node(FUNCTION());
        Scope functionScope = new Scope(null, functionRootNode);
        assertFalse(functionScope.isGlobal());
    }

    @Test
    public void testIsLocalScope() {
        Node rootNode = new Node(BLOCK());
        Scope scope = new Scope(rootNode, null);
        assertFalse(scope.isLocal());

        Node functionRootNode = new Node(FUNCTION());
        Scope functionScope = new Scope(null, functionRootNode);
        assertTrue(functionScope.isLocal());
    }

    @Test
    public void testGetParentScope() {
        Node rootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("x")),
                VAR_DECLARATION(ID("y"))));
        Scope parentScope = new Scope(null, rootNode);

        Node functionRootNode = new Node(FUNCTION());
        Scope childScope = new Scope(parentScope, functionRootNode);

        assertEquals(parentScope, childScope.getParentScope());
        assertNull(parentScope.getParentScope());
    }

    @Test
    public void testHasVarWithName() {
        Node rootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("x")),
                VAR_DECLARATION(ID("y"))));
        Scope scope = new Scope(null, rootNode);

        Var xVar = scope.declare("x", rootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));
        Var yVar = scope.declare("y", rootNode.getChild(1), INT, new CompilerInput(new SourceName("test.js"), 0));

        assertTrue(scope.hasVarWithName("x"));
        assertTrue(scope.hasVarWithName("y"));
        assertFalse(scope.hasVarWithName("z"));
    }

    @Test
    public void testHasVarWithNameRecursively() {
        Node rootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("x")),
                VAR_DECLARATION(ID("y"))));
        Scope parentScope = new Scope(null, rootNode);

        Node functionRootNode = new Node(FUNCTION());
        Scope childScope = new Scope(parentScope, functionRootNode);

        Var xVar = parentScope.declare("x", rootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));
        Var yVar = parentScope.declare("y", rootNode.getChild(1), INT, new CompilerInput(new SourceName("test.js"), 0));

        Var xFunctionVar = childScope.declare("x", functionRootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));

        assertTrue(childScope.hasVarWithName("x"));
        assertEquals(xFunctionVar, childScope.getVar("x"));
        assertFalse(childScope.hasVarWithName("y"));
    }

    @Test
    public void testGetVarWithName() {
        Node rootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("x")),
                VAR_DECLARATION(ID("y"))));
        Scope scope = new Scope(null, rootNode);

        Var xVar = scope.declare("x", rootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));
        Var yVar = scope.declare("y", rootNode.getChild(1), INT, new CompilerInput(new SourceName("test.js"), 0));

        Var x = scope.getVarWithName("x");
        Var y = scope.getVarWithName("y");

        assertEquals(xVar, x);
        assertEquals(yVar, y);
        assertNull(scope.getVarWithName("z"));
    }

    @Test
    public void testGetVarWithNameRecursively() {
        Node rootNode = new Node(VAR_DECLARATIONS(
                VAR_DECLARATION(ID("x")),
                VAR_DECLARATION(ID("y"))));
        Scope parentScope = new Scope(null, rootNode);

        Node functionRootNode = new Node(FUNCTION());
        Scope childScope = new Scope(parentScope, functionRootNode);

        Var xVar = parentScope.declare("x", rootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));
        Var yVar = parentScope.declare("y", rootNode.getChild(1), INT, new CompilerInput(new SourceName("test.js"), 0));

        Var xFunctionVar = childScope.declare("x", functionRootNode.getChild(0), INT, new CompilerInput(new SourceName("test.js"), 0));

        Var x = childScope.getVarWithName("x");
        Var y = childScope.getVarWithName("y");

        assertEquals(xFunctionVar, x);
        assertNull(childScope.getVarWithName("y"));
    }

    @Test
    public void testGetTypeOfThis() {
        Node rootNode = new Node(FUNCTION());
        Scope scope = new Scope(null, rootNode);

        ObjectType objectType = new ObjectType("Object");
        scope.setTypeOfThis(objectType);

        assertEquals(objectType, scope.getTypeOfThis());
    }
}