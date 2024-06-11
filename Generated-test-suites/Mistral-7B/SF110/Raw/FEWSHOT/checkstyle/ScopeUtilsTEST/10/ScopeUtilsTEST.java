package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.Scope;
import antlr.collections.AST;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        mods.addChild(new SimpleNode("public"));
        Scope scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PUBLIC, scope);

        mods.removeChild(0);
        mods.addChild(new SimpleNode("protected"));
        scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PROTECTED, scope);

        mods.removeChild(0);
        mods.addChild(new SimpleNode("private"));
        scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PRIVATE, scope);

        mods.removeChild(0);
        scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PACKAGE, scope);

        mods.addChild(new SimpleNode("public"));
        mods.addChild(new SimpleNode("protected"));
        scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.addChild(node);
        DetailAST grandParent = new DetailAST();
        grandParent.addChild(parent);

        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, scope);

        parent.setType(15); // class
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.CLASS, scope);

        parent.setType(154); // interface
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.INTERFACE, scope);

        parent.setType(157); // annotation
        parent.addChild(new SimpleNode("annotation"));
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANNOTATION, scope);

        grandParent.setType(14); // package
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, scope);

        parent.setType(136); // anonymous inner class
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.ANONINNER, scope);

        node = null;
        scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(15); // class
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node.getParent().setType(154); // interface
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(157); // annotation
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(136); // anonymous inner class
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(15); // class
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node.getParent().setType(154); // interface
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node.getParent().setType(157); // annotation
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(136); // anonymous inner class
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(15); // class
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node.getParent().setType(154); // interface
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node.getParent().setType(157); // annotation
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(136); // anonymous inner class
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(154); // enum
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(15); // class
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(157); // annotation
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(136); // anonymous inner class
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(9); // method
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(8); // constructor
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(11); // class declaration
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node.setParent(new DetailAST());
        node.getParent().setType(12); // package declaration
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        assertTrue(ScopeUtils.isOuterMostType(node));

        node.setParent(new DetailAST());
        node.getParent().setType(14); // package
        assertFalse(ScopeUtils.isOuterMostType(node));

        node.setParent(new DetailAST());
        node.getParent().setType(15); // class
        assertFalse(ScopeUtils.isOuterMostType(node));

        node.setParent(new DetailAST());
        node.getParent().setType(154); // interface
        assertFalse(ScopeUtils.isOuterMostType(node));

        node.setParent(new DetailAST());
        node.getParent().setType(157); // annotation
        assertFalse(ScopeUtils.isOuterMostType(node));

        node.setParent(new DetailAST());
        node.getParent().setType(136); // anonymous inner class
        assertFalse(ScopeUtils.isOuterMostType(node));

        node.setParent(null);
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        assertFalse(ScopeUtils.isLocalVariableDef(node));

        node.setType(10); // variable declaration
        node.addChild(new SimpleNode("int"));
        DetailAST parent = new DetailAST();
        parent.setType(7); // local class declaration
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node.setType(10); // variable declaration
        node.addChild(new SimpleNode("int"));
        parent.setType(35); // local variable
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node.setType(21); // assignment
        node.addChild(new SimpleNode("x"));
        parent.setType(96); // local variable declaration
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node.setType(21); // assignment
        node.addChild(new SimpleNode("x"));
        parent.setType(9); // method
        node.setParent(parent);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node.setType(10); // variable declaration
        node.addChild(new SimpleNode("int"));
        DetailAST parent = new DetailAST();
        parent.setType(15); // class
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node.setType(10); // variable declaration
        node.addChild(new SimpleNode("int"));
        parent.setType(15); // class
        parent.addChild(new SimpleNode("field"));
        node.setParent(parent);
        assertTrue(ScopeUtils.isClassFieldDef(node));

        node.setType(10); // variable declaration
        node.addChild(new SimpleNode("int"));
        parent.setType(14); // package
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.addChild(node);

        Scope scope = Scope.PACKAGE;
        assertTrue(ScopeUtils.isInScope(node, scope));

        scope = Scope.CLASS;
        parent.setType(15); // class
        assertTrue(ScopeUtils.isInScope(node, scope));

        scope = Scope.INTERFACE;
        parent.setType(154); // interface
        assertTrue(ScopeUtils.isInScope(node, scope));

        scope = Scope.ANNOTATION;
        parent.setType(157); // annotation
        assertTrue(ScopeUtils.isInScope(node, scope));

        scope = Scope.ANONINNER;
        parent.setType(136); // anonymous inner class
        assertTrue(ScopeUtils.isInScope(node, scope));

        parent.setType(14); // package
        assertFalse(ScopeUtils.isInScope(node, scope));

        node = null;
        assertFalse(ScopeUtils.isInScope(node, scope));
    }
}