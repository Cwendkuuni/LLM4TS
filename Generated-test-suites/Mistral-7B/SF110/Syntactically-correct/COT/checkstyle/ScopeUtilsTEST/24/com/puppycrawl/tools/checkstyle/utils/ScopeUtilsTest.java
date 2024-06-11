package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createAST ("public class Test { }");
        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        mods = createAST ("protected class Test { }");
        expected = Scope.PROTECTED;
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        mods = createAST ("private class Test { }");
        expected = Scope.PRIVATE;
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        mods = createAST ("package Test; class Test { }");
        expected = Scope.PACKAGE;
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        mods = createAST ("public protected static class Test { }");
        expected = Scope.PUBLIC_PROTECTED_STATIC;
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        mods = createAST ("public abstract class Test { }");
        expected = Scope.PUBLIC_ABSTRACT;
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);

        mods = createAST ("public interface Test { }");
        expected = Scope.PUBLIC_INTERFACE;
        actual = ScopeUtils.getScopeFromMods(mods);
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testGetScopeFromMods_MissingModifiers() {
        DetailAST mods = createAST ("class Test { }");
        ScopeUtils.getScopeFromMods(mods);
    }

    @Test(expected = AssertionError.class)
    public void testGetScopeFromMods_InvalidModifier() {
        DetailAST mods = createAST ("public final abstract class Test { }");
        ScopeUtils.getScopeFromMods(mods);
    }

    @Test
    public void testGetSurroundingScope_InsideMethod() {
        DetailAST node = createAST ("public void test() { }");
        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InsideClass() {
        DetailAST node = createAST ("public class Test { }");
        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InsideInterface() {
        DetailAST node = createAST ("public interface Test { }");
        Scope expected = Scope.PUBLIC_INTERFACE;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InsideAnnotation() {
        DetailAST node = createAST ("@Test public class Test { }");
        Scope expected = Scope.ANNOTATION;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InsideEnumConstant() {
        DetailAST node = createAST ("enum Test { TEST }");
        Scope expected = Scope.ENUM_CONSTANT;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InsideEnumDeclaration() {
        DetailAST node = createAST ("enum Test { }");
        Scope expected = Scope.ENUM;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InsideInnerClass() {
        DetailAST node = createAST ("public class OuterClass { public class InnerClass { } }");
        Scope expected = Scope.ANONINNER;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createAST ("public interface Test { }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createAST ("public class Test implements TestInterface { }");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = createAST ("public interface Test { public void test(); }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createAST ("public interface Test { static void test() { } }");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createAST ("@Test public class Test { }");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node = createAST ("public @interface Test { }");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node = createAST ("public class Test { @Test public void test() { } }");
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createAST ("public interface Test { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createAST ("public @interface Test { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createAST ("public class Test implements TestInterface { }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createAST ("public class Test { @Test public void test() { } }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createAST ("enum Test { TEST }");
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node = createAST ("enum Test { TEST } enum Test2 { TEST2 }");
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node = createAST ("public enum Test { TEST }");
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createAST ("{ }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createAST ("class Test { }");
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = createAST ("public interface Test { }");
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = createAST ("public class Test { }");
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createAST ("public class Test extends OtherClass { }");
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = createAST ("public interface Test { }");
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = createAST ("public class Test implements TestInterface { }");
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createAST ("int test = 0;");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createAST ("public class Test { int test; }");
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createAST ("public class Test { int test; }");
        assertTrue(ScopeUtils.isClassFieldDef(node));

        node = createAST ("public class Test { int test() { } }");
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = createAST ("public class Test { public void test() { int x = 0; } }");
        Scope scope = Scope.PUBLIC;
        assertTrue(ScopeUtils.isInScope(ast, scope));

        scope = Scope.PRIVATE;
        assertFalse(ScopeUtils.isInScope(ast, scope));

        scope = Scope.PROTECTED;
        assertFalse(ScopeUtils.isInScope(ast, scope));

        scope = Scope.ANONINNER;
        assertFalse(ScopeUtils.isInScope(ast, scope));

        scope = Scope.STATIC;
        DetailAST staticNode = ast.findFirstToken(TokenTypes.KEYWORD_STATIC);
        assertTrue(ScopeUtils.isInScope(staticNode, scope));

        scope = Scope.PUBLIC_INTERFACE;
        DetailAST interfaceNode = ast.findFirstToken(TokenTypes.INTERFACE);
        assertTrue(ScopeUtils.isInScope(interfaceNode, scope));

        scope = Scope.ENUM;
        DetailAST enumNode = ast.findFirstToken(TokenTypes.ENUM);
        assertTrue(ScopeUtils.isInScope(enumNode, scope));

        scope = Scope.ENUM_CONSTANT;
        DetailAST enumConstantNode = ast.findFirstChild().findFirstToken(TokenTypes.IDENTIFIER);
        assertTrue(ScopeUtils.isInScope(enumConstantNode, scope));

        scope = Scope.PACKAGE;
        DetailAST packageNode = ast.getParent().findFirstToken(TokenTypes.PACKAGE);
        assertTrue(ScopeUtils.isInScope(packageNode, scope));
    }

    private DetailAST createAST(String code) {
        ANTLRInputStream input = new ANTLRInputStream(new StringReader(code));
        CheckstyleLexer lexer = new CheckstyleLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DetailParser parser = new DetailParser(tokens);
        return parser.detail();
    }
}