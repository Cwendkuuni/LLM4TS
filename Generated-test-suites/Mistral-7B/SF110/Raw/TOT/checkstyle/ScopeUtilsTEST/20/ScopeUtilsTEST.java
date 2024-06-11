package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import antlr.collections.AST;
import antlr.token.Token;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = createDetailAST("public class Test { }");
        Scope expected = Scope.PUBLIC;
        Scope result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, result);

        ast = createDetailAST("protected class Test { }");
        expected = Scope.PROTECTED;
        result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, result);

        ast = createDetailAST("private class Test { }");
        expected = Scope.PRIVATE;
        result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, result);

        ast = createDetailAST("class Test { }");
        result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, result);
    }

    @Test
    public void testGetScopeFromMods_empty() {
        DetailAST ast = createDetailAST("");
        Scope result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, result);
    }

    @Test
    public void testGetScopeFromMods_noModifiers() {
        DetailAST ast = createDetailAST("public class Test { int x; }");
        Scope result = ScopeUtils.getScopeFromMods(ast.findFirstToken(1));
        assertEquals(Scope.PUBLIC, result);
    }

    @Test
    public void testGetScopeFromMods_multipleModifiers() {
        DetailAST ast = createDetailAST("public protected static final int X = 1;");
        Scope expected = Scope.PUBLIC;
        Scope result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, result);

        expected = Scope.PROTECTED;
        ast = createDetailAST("protected public static final int X = 1;");
        result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, result);

        expected = Scope.PROTECTED;
        result = ScopeUtils.getScopeFromMods(ast.findFirstToken(1));
        assertEquals(expected, result);

        expected = Scope.PUBLIC;
        result = ScopeUtils.getScopeFromMods(ast.findFirstToken(2));
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_class() {
        DetailAST node = createDetailAST("public class Test { }");
        Scope expected = Scope.PACKAGE;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_package() {
        DetailAST node = createDetailAST("package com.example; public class Test { }");
        Scope expected = Scope.PACKAGE;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_method() {
        DetailAST node = createDetailAST("public class Test { int test() { } }");
        Scope expected = Scope.PUBLIC;
        Scope result = ScopeUtils.getSurroundingScope(node.findFirstToken(1));
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_interface() {
        DetailAST node = createDetailAST("interface Test { }");
        Scope expected = Scope.INTERFACE;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_annotation() {
        DetailAST node = createDetailAST("@interface Test { }");
        Scope expected = Scope.ANNOTATION;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_switch() {
        DetailAST node = createDetailAST("switch (x) { }");
        Scope expected = Scope.ANONINNER;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_for() {
        DetailAST node = createDetailAST("for (int i = 0; i < 10; i++) { }");
        Scope expected = Scope.ANONINNER;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_if() {
        DetailAST node = createDetailAST("if (x > 0) { }");
        Scope expected = Scope.ANONINNER;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_try() {
        DetailAST node = createDetailAST("try { } catch (Exception e) { } finally { }");
        Scope expected = Scope.ANONINNER;
        Scope result = ScopeUtils.getSurroundingScope(node.findFirstToken(1));
        assertEquals(expected, result);

        expected = Scope.ANONINNER;
        result = ScopeUtils.getSurroundingScope(node.findFirstToken(2));
        assertEquals(expected, result);

        expected = Scope.ANONINNER;
        result = ScopeUtils.getSurroundingScope(node.findFirstToken(4));
        assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_labeledStatement() {
        DetailAST node = createDetailAST("outer: for (int i = 0; i < 10; i++) { inner: for (int j = 0; j < 10; j++) { } }");
        Scope expected = Scope.ANONINNER;
        Scope result = ScopeUtils.getSurroundingScope(node.findFirstToken(1));
        assertEquals(expected, result);

        expected = Scope.ANONINNER;
        result = ScopeUtils.getSurroundingScope(node.findFirstToken(2));
        assertEquals(expected, result);
    }

    @Test
    public void testIsInInterfaceBlock_interface() {
        DetailAST node = createDetailAST("interface Test { }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInInterfaceBlock_class() {
        DetailAST node = createDetailAST("public interface Test { }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createDetailAST("public class Test implements Test { }");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInInterfaceBlock_method() {
        DetailAST node = createDetailAST("public interface Test { void test(); }");
        DetailAST method = node.findFirstToken(1).getNextSibling();
        assertFalse(ScopeUtils.isInInterfaceBlock(method));
    }

    @Test
    public void testIsInAnnotationBlock_annotation() {
        DetailAST node = createDetailAST("@annotation Test { }");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock_class() {
        DetailAST node = createDetailAST("public @interface Test { }");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_class() {
        DetailAST node = createDetailAST("public interface Test { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createDetailAST("public class Test implements Test { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_method() {
        DetailAST node = createDetailAST("public interface Test { void test(); }");
        DetailAST method = node.findFirstToken(1).getNextSibling();
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(method));
    }

    @Test
    public void testIsInEnumBlock_enum() {
        DetailAST node = createDetailAST("enum Test { VALUE }");
        assertTrue(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInEnumBlock_class() {
        DetailAST node = createDetailAST("public enum Test { VALUE }");
        assertTrue(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock_codeBlock() {
        DetailAST node = createDetailAST("{ int x = 1; }");
        assertTrue(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsInCodeBlock_statement() {
        DetailAST node = createDetailAST("int x = 1;");
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType_class() {
        DetailAST node = createDetailAST("public class Test { }");
        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsOuterMostType_interface() {
        DetailAST node = createDetailAST("public interface Test { }");
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsOuterMostType_method() {
        DetailAST node = createDetailAST("public class Test { void test() { } }");
        assertFalse(ScopeUtils.isOuterMostType(node.findFirstToken(1)));

        assertTrue(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef_localVariableDef() {
        DetailAST node = createDetailAST("int x;");
        assertTrue(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsLocalVariableDef_field() {
        DetailAST node = createDetailAST("int x;");
        assertFalse(ScopeUtils.isLocalVariableDef(node.findFirstToken(1)));
    }

    @Test
    public void testIsClassFieldDef_field() {
        DetailAST node = createDetailAST("int Test.x;");
        assertTrue(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsClassFieldDef_localVariableDef() {
        DetailAST node = createDetailAST("int x;");
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST node = createDetailAST("public class Test { int x; }");
        DetailAST field = node.findFirstToken(0);
        Scope expected = Scope.PUBLIC;
        Scope result = ScopeUtils.getScopeFromMods(field);
        assertTrue(ScopeUtils.isInScope(node, expected));
        assertEquals(expected, result);
    }

    @Test
    public void testIsInScope_wrongScope() {
        DetailAST node = createDetailAST("public class Test { int x; }");
        DetailAST field = node.findFirstToken(0);
        Scope expected = Scope.PROTECTED;
        assertFalse(ScopeUtils.isInScope(node, expected));
    }

    @Test
    public void testIsInScope_wrongNode() {
        DetailAST node = createDetailAST("public class Test { int x; }");
        DetailAST method = node.findFirstToken(1);
        assertFalse(ScopeUtils.isInScope(node, Scope.PUBLIC));
    }

    private DetailAST createDetailAST(String code) {
        Token[] tokens = new ANTLRStringStream(code).getTokens();
        DefaultAST root = new DefaultAST();
        CommonTokenStream tokensStream = new CommonTokenStream(new ANTLRFileStream(code));
        ParseTree tree = new CheckstyleBaseListener().parse(tokensStream);
        root.addChildren(tree.getTree());
        return root;
    }
}