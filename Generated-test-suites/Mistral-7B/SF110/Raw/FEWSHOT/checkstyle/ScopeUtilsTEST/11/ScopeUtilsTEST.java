package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST astMods = createMockAST("public class TestClass { }");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(astMods));

        astMods = createMockAST("protected class TestClass { }");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(astMods));

        astMods = createMockAST("private class TestClass { }");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(astMods));

        astMods = createMockAST("class TestClass { }");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(astMods));
    }

    @Test
    public void testGetSurroundingScope_inClass() {
        DetailAST astNode = createMockAST("public class TestClass { }");
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(astNode));
    }

    @Test
    public void testGetSurroundingScope_inMethod() {
        DetailAST astNode = createMockAST("public class TestClass { void test() { } }");
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(astNode));
    }

    @Test
    public void testGetSurroundingScope_inMethodWithModifiers() {
        DetailAST astNode = createMockAST("public class TestClass { protected void test() { } }");
        assertEquals(Scope.PROTECTED, ScopeUtils.getSurroundingScope(astNode));
    }

    @Test
    public void testGetSurroundingScope_inInterface() {
        DetailAST astNode = createMockAST("public interface TestInterface { }");
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(astNode));
    }

    @Test
    public void testGetSurroundingScope_inEnum() {
        DetailAST astNode = createMockAST("enum TestEnum { TEST }");
        assertEquals(Scope.ENUM, ScopeUtils.getSurroundingScope(astNode));
    }

    @Test
    public void testGetSurroundingScope_inAnnonymousClass() {
        DetailAST astNode = createMockAST("public class TestClass { void test() { new Object() { void method() { } } } }");
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(astNode));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST astNode = createMockAST("public interface TestInterface { }");
        assertTrue(ScopeUtils.isInInterfaceBlock(astNode));

        astNode = createMockAST("public class TestClass implements TestInterface { }");
        assertFalse(ScopeUtils.isInInterfaceBlock(astNode));

        astNode = createMockAST("public interface TestInterface { int a = 1; }");
        assertTrue(ScopeUtils.isInInterfaceBlock(astNode));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST astNode = createMockAST("@Test public class TestClass { }");
        assertTrue(ScopeUtils.isInAnnotationBlock(astNode));

        astNode = createMockAST("public @interface Test { int a = 1; }");
        assertTrue(ScopeUtils.isInAnnotationBlock(astNode));

        astNode = createMockAST("public class TestClass { @Test public void test() { } }");
        assertFalse(ScopeUtils.isInAnnotationBlock(astNode));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST astNode = createMockAST("public interface TestInterface { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode));

        astNode = createMockAST("@Test public class TestClass { }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode));

        astNode = createMockAST("public class TestClass { }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST astNode = createMockAST("enum TestEnum { TEST }");
        assertTrue(ScopeUtils.isInEnumBlock(astNode));

        astNode = createMockAST("public enum TestEnum { TEST }");
        assertTrue(ScopeUtils.isInEnumBlock(astNode));

        astNode = createMockAST("public class TestClass { enum TestEnum { TEST } }");
        assertFalse(ScopeUtils.isInEnumBlock(astNode));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST astNode = createMockAST("public class TestClass { void test() { } }");
        assertTrue(ScopeUtils.isInCodeBlock(astNode));

        astNode = createMockAST("public class TestClass { }");
        assertFalse(ScopeUtils.isInCodeBlock(astNode));

        astNode = createMockAST("public interface TestInterface { }");
        assertFalse(ScopeUtils.isInCodeBlock(astNode));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST astNode = createMockAST("public class TestClass { }");
        assertTrue(ScopeUtils.isOuterMostType(astNode));

        astNode = createMockAST("public class TestClass { public class InnerClass { } }");
        assertFalse(ScopeUtils.isOuterMostType(astNode));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST astNode = createMockAST("public class TestClass { int a; }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { int a = 1; }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { void test() { int a; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { void test() { int a = 1; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { void test() { int a = 1; int b; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { void test() { int a; int b = 2; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { void test() { int a; int b; int c; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { void test() { int a; int b; int c = 2; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { int a; int b; }");
        assertFalse(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { void test() { int a; int b = 1; } }");
        assertFalse(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { int a; void test() { int b; } }");
        assertFalse(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public interface TestInterface { int a; }");
        assertFalse(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { int a; enum TestEnum { TEST } }");
        assertFalse(ScopeUtils.isLocalVariableDef(astNode));

        astNode = createMockAST("public class TestClass { int a; public class InnerClass { int b; } }");
        assertFalse(ScopeUtils.isLocalVariableDef(astNode));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST astNode = createMockAST("public class TestClass { int a; }");
        assertTrue(ScopeUtils.isClassFieldDef(astNode));

        astNode = createMockAST("public class TestClass { void test() { int a; } }");
        assertFalse(ScopeUtils.isClassFieldDef(astNode));

        astNode = createMockAST("public class TestClass { int a = 1; }");
        assertTrue(ScopeUtils.isClassFieldDef(astNode));

        astNode = createMockAST("public class TestClass { int a; int b; }");
        assertTrue(ScopeUtils.isClassFieldDef(astNode));

        astNode = createMockAST("public class TestClass { int a; enum TestEnum { TEST } }");
        assertTrue(ScopeUtils.isClassFieldDef(astNode));

        astNode = createMockAST("public class TestClass { public class InnerClass { int a; } }");
        assertFalse(ScopeUtils.isClassFieldDef(astNode));
    }

    @Test
    public void testIsInScope() {
        DetailAST astNode = createMockAST("public class TestClass { int a; }");
        DetailAST astScope = createMockAST("package foo;");
        assertTrue(ScopeUtils.isInScope(astNode, astScope));

        astNode = createMockAST("public class TestClass { int a; }");
        DetailAST astScope2 = createMockAST("package bar;");
        assertFalse(ScopeUtils.isInScope(astNode, astScope2));

        astNode = createMockAST("public class TestClass { int a; }");
        DetailAST astScope3 = createMockAST("public class TestClass { int b; }");
        assertFalse(ScopeUtils.isInScope(astNode, astScope3));

        astNode = createMockAST("public class TestClass { int a; }");
        DetailAST astScope4 = createMockAST("public interface TestInterface { }");
        assertFalse(ScopeUtils.isInScope(astNode, astScope4));

        astNode = createMockAST("public class TestClass { int a; }");
        DetailAST astScope5 = createMockAST("public class TestClass { }");
        assertTrue(ScopeUtils.isInScope(astNode, astScope5));

        astNode = createMockAST("public class TestClass { int a; }");
        DetailAST astScope6 = createMockAST("public class TestClass { int a; }");
        assertTrue(ScopeUtils.isInScope(astNode, astScope6));

        astNode = createMockAST("public class TestClass { int a; }");
        DetailAST astScope7 = createMockAST("public interface TestInterface { int a; }");
        assertFalse(ScopeUtils.isInScope(astNode, astScope7));

        astNode = createMockAST("public class TestClass { int a; }");
        DetailAST astScope8 = createMockAST("public enum TestEnum { TEST }");
        assertFalse(ScopeUtils.isInScope(astNode, astScope8));

        astNode = createMockAST("public class TestClass { int a; }");
        DetailAST astScope9 = createMockAST("public class TestClass { public class InnerClass { int a; } }");
        assertFalse(ScopeUtils.isInScope(astNode, astScope9));
    }

    private DetailAST createMockAST(String code) {
        return new DetailAST() {};
    }
}