package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createMockMods("public protected private");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(mods));

        mods = createMockMods("private public protected");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(mods));

        mods = createMockMods("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(mods));

        mods = createMockMods("");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(mods));

        mods = createMockMods(null);
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(mods));
    }

    @Test
    public void testGetSurroundingScope_PublicClass() {
        DetailAST node = createMockNode(14, "public class Test");
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_ProtectedClass() {
        DetailAST node = createMockNode(15, "protected class Test");
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_PrivateClass() {
        DetailAST node = createMockNode(15, "private class Test");
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_Interface() {
        DetailAST node = createMockNode(15, "interface Test");
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_AnonymousInnerClass() {
        DetailAST node = createMockNode(136, "new Test() { }");
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_Empty() {
        DetailAST node = createMockNode(15, "");
        assertNull(ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testGetSurroundingScope_Null() {
        DetailAST node = null;
        assertNull(ScopeUtils.getSurroundingScope(node));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createMockNode(15, "interface Test { }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createMockNode(14, "public class Test implements TestInterface { }");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createMockNode(157, "@annotation Test { }");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node = createMockNode(14, "public class Test { @annotation TestAnnotation }");
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createMockNode(15, "interface Test { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createMockNode(157, "@annotation Test { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createMockNode(14, "public class Test { }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createMockNode(154, "enum Test { VALUE }");
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node = createMockNode(14, "public class Test { enum TestEnum { VALUE } }");
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createMockNode(9, "public static void main(String[] args) { }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createMockNode(8, "public static void main(String[] args) { }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createMockNode(11, "public static void main(String[] args) { }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createMockNode(12, "public static void main(String[] args) { }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createMockNode(14, "public class Test { }");
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = createMockNode(14, "public class Test { }");
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createMockNode(15, "public class Test { class NestedTest { } }");
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createMockNode(10, "int a;");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createMockNode(7, "int a;");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createMockNode(35, "int a;");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createMockNode(156, "int a;");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createMockNode(21, "int a;");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createMockNode(10, "class Test { int a; }");
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createMockNode(10, "class Test { int a; }");
        assertTrue(ScopeUtils.isClassFieldDef(node));

        node = createMockNode(21, "class Test { int a; }");
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = createMockNode(14, "public class Test { }");
        DetailAST node = createMockNode(10, "int a;", ast);
        assertTrue(ScopeUtils.isInScope(ast, Scope.CLASS));
        assertTrue(ScopeUtils.isInScope(node, Scope.CLASS));

        ast = createMockNode(15, "public interface Test { }");
        node = createMockNode(10, "int a;", ast);
        assertTrue(ScopeUtils.isInScope(ast, Scope.INTERFACE));
        assertTrue(ScopeUtils.isInScope(node, Scope.INTERFACE));

        ast = createMockNode(154, "public enum Test { VALUE }");
        node = createMockNode(10, "int a;", ast);
        assertTrue(ScopeUtils.isInScope(ast, Scope.ENUM));
        assertTrue(ScopeUtils.isInScope(node, Scope.ENUM));

        ast = createMockNode(136, "new Test() { int a; }");
        node = createMockNode(10, "int a;", ast);
        assertTrue(ScopeUtils.isInScope(ast, Scope.ANONINNER));
        assertTrue(ScopeUtils.isInScope(node, Scope.ANONINNER));
    }

    private DetailAST createMockMods(String... modifiers) {
        DetailAST mods = new DetailAST();
        for (String modifier : modifiers) {
            DetailAST modifierNode = new DetailAST();
            modifierNode.setText(modifier);
            mods.addChild(modifierNode);
        }
        return mods;
    }

    private DetailAST createMockNode(int type, String text, DetailAST... children) {
        DetailAST node = new DetailAST();
        node.setType(type);
        node.setText(text);
        if (children != null) {
            for (DetailAST child : children) {
                node.addChild(child);
            }
        }
        return node;
    }
}