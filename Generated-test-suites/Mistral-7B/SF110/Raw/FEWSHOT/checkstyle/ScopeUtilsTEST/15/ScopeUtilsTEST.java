package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST modsAST = createAST("public class Foo { }");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(modsAST));

        modsAST = createAST("protected class Foo { }");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(modsAST));

        modsAST = createAST("private class Foo { }");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(modsAST));

        modsAST = createAST("class Foo { }");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(modsAST));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST nodeAST = createAST("public class Foo { }");
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(nodeAST));

        nodeAST = createAST("public class Foo { public void bar() { } }");
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(nodeAST));

        nodeAST = createAST("public class Foo { protected void bar() { } }");
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(nodeAST));

        nodeAST = createAST("public class Foo { private int x; }");
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(nodeAST));

        nodeAST = createAST("public class Foo { public int x = 42; }");
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(nodeAST));

        nodeAST = createAST("public interface Foo { }");
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(nodeAST));

        nodeAST = createAST("public enum Foo { VALUE }");
        assertEquals(Scope.ENUM, ScopeUtils.getSurroundingScope(nodeAST));

        nodeAST = createAST("public class Foo { static class Inner { } }");
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(nodeAST));

        nodeAST = createAST("public int main(String[] args) { }");
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(nodeAST));

        nodeAST = createAST("public interface InterfaceName { int CONSTANT = 42; }");
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(nodeAST));

        nodeAST = createAST("@annotation class Foo { }");
        assertEquals(Scope.ANNOTATION, ScopeUtils.getSurroundingScope(nodeAST));

        nodeAST = createAST("public class Foo { int x; @annotation private int y; }");
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(nodeAST.getFirstChild()));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST nodeAST = createAST("public interface Foo { }");
        assertTrue(ScopeUtils.isInInterfaceBlock(nodeAST));

        nodeAST = createAST("public class Foo { }");
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeAST));

        nodeAST = createAST("public interface Foo { int x; }");
        assertTrue(ScopeUtils.isInInterfaceBlock(nodeAST));

        nodeAST = createAST("public class Foo { public interface NestedInterface { } }");
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeAST));

        nodeAST = createAST("public class Foo implements InterfaceName { }");
        assertFalse(ScopeUtils.isInInterfaceBlock(nodeAST));

        nodeAST = createAST("@interface MyAnnotation { int value(); }");
        assertTrue(ScopeUtils.isInInterfaceBlock(nodeAST));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST nodeAST = createAST("@interface MyAnnotation { int value(); }");
        assertTrue(ScopeUtils.isInAnnotationBlock(nodeAST));

        nodeAST = createAST("public class Foo { }");
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeAST));

        nodeAST = createAST("public interface Foo { }");
        assertFalse(ScopeUtils.isInAnnotationBlock(nodeAST));

        nodeAST = createAST("public class Foo { @MyAnnotation(value = 42) int x; }");
        assertTrue(ScopeUtils.isInAnnotationBlock(nodeAST));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST nodeAST = createAST("public interface Foo { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeAST));

        nodeAST = createAST("public class Foo { }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeAST));

        nodeAST = createAST("@interface MyAnnotation { int value(); }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeAST));

        nodeAST = createAST("public class Foo { @MyAnnotation(value = 42) int x; }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(nodeAST));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST nodeAST = createAST("public enum Foo { VALUE }");
        assertTrue(ScopeUtils.isInEnumBlock(nodeAST));

        nodeAST = createAST("public class Foo { }");
        assertFalse(ScopeUtils.isInEnumBlock(nodeAST));

        nodeAST = createAST("public interface Foo { }");
        assertFalse(ScopeUtils.isInEnumBlock(nodeAST));

        nodeAST = createAST("public enum Foo { VALUE1, VALUE2 }");
        assertTrue(ScopeUtils.isInEnumBlock(nodeAST));

        nodeAST = createAST("public enum Foo { VALUE1, VALUE2 } { INNER_CLASS }");
        assertFalse(ScopeUtils.isInEnumBlock(nodeAST));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST nodeAST = createAST("public class Foo { void bar() { } }");
        assertTrue(ScopeUtils.isInCodeBlock(nodeAST));

        nodeAST = createAST("public class Foo { }");
        assertFalse(ScopeUtils.isInCodeBlock(nodeAST));

        nodeAST = createAST("public interface Foo { }");
        assertFalse(ScopeUtils.isInCodeBlock(nodeAST));

        nodeAST = createAST("public class Foo { int x; }");
        assertTrue(ScopeUtils.isInCodeBlock(nodeAST));

        nodeAST = createAST("public class Foo { static int x; }");
        assertTrue(ScopeUtils.isInCodeBlock(nodeAST));

        nodeAST = createAST("public class Foo { int x; static int y; }");
        assertTrue(ScopeUtils.isInCodeBlock(nodeAST));

        nodeAST = createAST("public class Foo { void bar() { int x; } }");
        assertTrue(ScopeUtils.isInCodeBlock(nodeAST));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST nodeAST = createAST("public class Foo { }");
        assertTrue(ScopeUtils.isOuterMostType(nodeAST));

        nodeAST = createAST("public class Foo { public class Inner { } }");
        assertFalse(ScopeUtils.isOuterMostType(nodeAST));

        nodeAST = createAST("public interface Foo { }");
        assertFalse(ScopeUtils.isOuterMostType(nodeAST));

        nodeAST = createAST("public enum Foo { VALUE }");
        assertFalse(ScopeUtils.isOuterMostType(nodeAST));

        nodeAST = createAST("public class Foo implements InterfaceName { }");
        assertFalse(ScopeUtils.isOuterMostType(nodeAST));

        nodeAST = createAST("public interface InterfaceName { }");
        assertFalse(ScopeUtils.isOuterMostType(nodeAST));

        nodeAST = createAST("public enum InterfaceName { VALUE }");
        assertFalse(ScopeUtils.isOuterMostType(nodeAST));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST nodeAST = createAST("public class Foo { int x; }");
        assertTrue(ScopeUtils.isLocalVariableDef(nodeAST.getFirstChild()));

        nodeAST = createAST("public class Foo { void bar() { int x; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(nodeAST.getChild(1).getFirstChild()));

        nodeAST = createAST("public class Foo { void bar() { int x = 42; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(nodeAST.getChild(1)));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST nodeAST = createAST("public class Foo { int x; }");
        assertTrue(ScopeUtils.isClassFieldDef(nodeAST.getFirstChild()));

        nodeAST = createAST("public class Foo { int x = 42; }");
        assertFalse(ScopeUtils.isClassFieldDef(nodeAST.getFirstChild()));
    }

    @Test
    public void testIsInScope() {
        DetailAST nodeAST = createAST("public class Foo { int x; }");
        assertTrue(ScopeUtils.isInScope(nodeAST, Scope.CLASS));

        nodeAST = createAST("public class Foo { int x; public int y; }");
        assertTrue(ScopeUtils.isInScope(nodeAST, Scope.CLASS));

        nodeAST = createAST("public interface Foo { int x; }");
        assertTrue(ScopeUtils.isInScope(nodeAST, Scope.INTERFACE));

        nodeAST = createAST("public class Foo { interface InterfaceName { int x; } }");
        assertFalse(ScopeUtils.isInScope(nodeAST, Scope.INTERFACE));

        nodeAST = createAST("public class Foo { interface InterfaceName { int x; } }");
        assertTrue(ScopeUtils.isInScope(nodeAST, Scope.CLASS));

        nodeAST = createAST("public interface Foo { interface InterfaceName { int x; } }");
        assertFalse(ScopeUtils.isInScope(nodeAST, Scope.INTERFACE));

        nodeAST = createAST("public interface Foo { interface InterfaceName { int x; } }");
        assertTrue(ScopeUtils.isInScope(nodeAST, Scope.INTERFACE));
    }

    private DetailAST createAST(String code) {
        final StringASTLexer lexer = new StringASTLexer();
        final AstFactory astFactory = new SimpleASTFactory();
        final CSTParser parser = new CSTParser(lexer, astFactory);
        return parser.parse(new StringReader(code)).getRootNode();
    }
}