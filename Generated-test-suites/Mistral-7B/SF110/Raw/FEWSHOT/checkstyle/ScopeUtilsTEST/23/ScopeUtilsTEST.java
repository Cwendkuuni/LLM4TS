package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createAST("public class Foo { int x; }");
        Scope scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PUBLIC, scope);

        mods = createAST("protected class Foo { int x; }");
        scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PROTECTED, scope);

        mods = createAST("private class Foo { int x; }");
        scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PRIVATE, scope);

        mods = createAST("public static class Foo { int x; }");
        scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PUBLIC, scope);

        mods = createAST("class Foo { int x; }");
        scope = ScopeUtils.getScopeFromMods(mods);
        assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = createAST("public class Foo { int x; }");
        Scope scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        node = createAST("public class Foo { public int x; }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        node = createAST("public class Foo { protected int x; }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PROTECTED, scope);

        node = createAST("public class Foo { private int x; }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PRIVATE, scope);

        node = createAST("public class Foo { int x; public int y; }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        node = createAST("public interface Foo { int x; }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        node = createAST("public abstract class Foo { int x; }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        node = createAST("public enum Foo { BAR }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        node = createAST("public class Foo { int x; void foo() { } }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        node = createAST("public class Foo { int x; static void foo() { } }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        node = createAST("public class Foo { int x; interface Bar { } }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        node = createAST("public class Foo { int x; @interface Bar { } }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        node = createAST("public class Foo { int x; void foo() { int y; } }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PUBLIC, scope);

        node = createAST("public static void main(String[] args) { int x; }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);

        node = createAST("public static void main(String[] args) { int x = 0; }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);

        node = createAST("public interface Foo { int x(); }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);

        node = createAST("public interface Foo { int x(); int y(); }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);

        node = createAST("public abstract class Foo { int x(); }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);

        node = createAST("public abstract class Foo { int x(); int y(); }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);

        node = createAST("public enum Foo { BAR, Baz }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);

        node = createAST("public enum Foo { BAR, Baz } enum Bar { FOO }");
        scope = ScopeUtils.getSurroundingScope(node);
        assertNull(scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = createAST("public interface Foo { int x; }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createAST("public class Foo { int x; }");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = createAST("public class Foo implements Foo { int x; }");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = createAST("public interface Foo { int x(); }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createAST("public interface Foo { void foo() { } }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createAST("public interface Foo { enum Bar { FOO } }");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = createAST("public interface Foo extends Bar { int x; }");
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = createAST("@interface Foo { int x(); }");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node = createAST("public class Foo { int x; @interface Bar { int y(); } }");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node = createAST("public class Foo { int x; }");
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = createAST("public interface Foo { int x; }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createAST("public class Foo { int x; }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createAST("public class Foo implements Foo { int x; }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createAST("public interface Foo { int x(); }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createAST("public interface Foo { void foo() { } }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createAST("public interface Foo { enum Bar { FOO } }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = createAST("public interface Foo extends Bar { int x; }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = createAST("public enum Foo { BAR }");
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node = createAST("public enum Foo { BAR, Baz }");
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node = createAST("public class Foo { int x; enum Bar { FOO } }");
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = createAST("public class Foo { int x; }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createAST("public class Foo { int x; { int y; } }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createAST("public class Foo { int x; static int y; }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createAST("public class Foo { int x; interface Bar { } }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createAST("public class Foo { int x; @interface Bar { } }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createAST("public class Foo { int x; enum Bar { } }");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = createAST("public interface Foo { int x; }");
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = createAST("public interface Foo { int x; int y; }");
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = createAST("public abstract class Foo { int x; }");
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = createAST("public abstract class Foo { int x; int y; }");
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = createAST("public enum Foo { BAR, Baz }");
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = createAST("public enum Foo { BAR, Baz } enum Bar { FOO }");
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = createAST("public class Foo { int x; }");
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createAST("public class Foo { int x; class Bar { int y; } }");
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = createAST("public interface Foo { int x; }");
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createAST("public interface Foo { int x; interface Bar { } }");
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = createAST("public abstract class Foo { int x; }");
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createAST("public abstract class Foo { int x; abstract class Bar { } }");
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = createAST("public enum Foo { BAR, Baz }");
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = createAST("public enum Foo { BAR, Baz } enum Bar { FOO }");
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = createAST("public class Foo { int x; void foo() { int y; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createAST("public class Foo { int x; int y; }");
        assertFalse(ScopeUtils.isLocalVariableDef(node));

        node = createAST("public class Foo { int x; void foo() { } }");
        assertFalse(ScopeUtils.isLocalVariableDef(node));

        node = createAST("public class Foo { int x; void foo() { int y = 0; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createAST("public class Foo { int x; void foo() { int y = 0; int z; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createAST("public class Foo { int x; void foo() { int y = 0; int z = 0; int w; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        node = createAST("public class Foo { int x; void foo() { int y; int z; } }");
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = createAST("public class Foo { int x; }");
        assertTrue(ScopeUtils.isClassFieldDef(node));

        node = createAST("public class Foo { int x; void foo() { } }");
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = createAST("public class Foo { int x; int y; }");
        assertTrue(ScopeUtils.isClassFieldDef(node));

        node = createAST("public class Foo { int x; void foo() { int y; } }");
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = createAST("public class Foo { int x; }");
        Scope scope = Scope.PACKAGE;
        assertTrue(ScopeUtils.isInScope(ast, scope));

        ast = createAST("public class Foo { int x; }");
        scope = Scope.PUBLIC;
        assertTrue(ScopeUtils.isInScope(ast, scope));

        ast = createAST("public class Foo { int x; }");
        scope = Scope.PROTECTED;
        assertFalse(ScopeUtils.isInScope(ast, scope));

        ast = createAST("public class Foo { protected int x; }");
        assertTrue(ScopeUtils.isInScope(ast, scope));

        ast = createAST("public class Foo { private int x; }");
        assertTrue(ScopeUtils.isInScope(ast, scope));

        ast = createAST("public class Foo { int x; }");
        scope = Scope.PRIVATE;
        assertFalse(ScopeUtils.isInScope(ast, scope));

        ast = createAST("public class Foo { int x; }");
        scope = Scope.ANONINNER;
        assertFalse(ScopeUtils.isInScope(ast, scope));

        ast = createAST("public class Foo { int x; void foo() { int y; } }");
        scope = Scope.ANONINNER;
        assertFalse(ScopeUtils.isInScope(ast, scope));

        ast = createAST("public class Foo { int x; interface Bar { } }");
        scope = Scope.ANONINNER;
        assertFalse(ScopeUtils.isInScope(ast, scope));

        ast = createAST("public class Foo { int x; @interface Bar { } }");
        scope = Scope.ANONINNER;
        assertFalse(ScopeUtils.isInScope(ast, scope));

        ast = createAST("public class Foo { int x; enum Bar { } }");
        scope = Scope.ANONINNER;
        assertFalse(ScopeUtils.isInScope(ast, scope));
    }

    private DetailAST createAST(String code) {
        return (DetailAST) CheckstyleTestUtils.parseCheckstyleAST(code);
    }
}