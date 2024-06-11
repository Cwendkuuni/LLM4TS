package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.Scope;

public final class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST astMods = createMockAST("public class Test { int p; protected int pp; private int ppp; }");
        Scope expectedScope = Scope.PROTECTED;
        Scope actualScope = ScopeUtils.getScopeFromMods(astMods.findFirstChild("modifierSequence"));
        assertEquals(expectedScope, actualScope);

        astMods = createMockAST("public interface Test { int p; }");
        expectedScope = Scope.PUBLIC;
        actualScope = ScopeUtils.getScopeFromMods(astMods.findFirstChild("modifierSequence"));
        assertEquals(expectedScope, actualScope);

        astMods = createMockAST("public class Test { private int p; }");
        expectedScope = Scope.PRIVATE;
        actualScope = ScopeUtils.getScopeFromMods(astMods.findFirstChild("modifierSequence"));
        assertEquals(expectedScope, actualScope);

        astMods = createMockAST("public class Test { int p; }");
        expectedScope = Scope.PACKAGE;
        actualScope = ScopeUtils.getScopeFromMods(astMods.findFirstChild("modifierSequence"));
        assertEquals(expectedScope, actualScope);

        astMods = createMockAST("public static void main(String[] args) { int p; }");
        expectedScope = Scope.PUBLIC;
        actualScope = ScopeUtils.getScopeFromMods(astMods.findFirstChild("modifierSequence"));
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST astNode = createMockAST("public class Outer { public class Inner { int p; } }");
        Scope expectedScope = Scope.ANONINNER;
        Scope actualScope = ScopeUtils.getSurroundingScope(astNode.findFirstChild("classDef"));
        assertEquals(expectedScope, actualScope);

        astNode = createMockAST("public interface TestInterface { int p; } public class TestClass { int p; }");
        expectedScope = Scope.PUBLIC;
        actualScope = ScopeUtils.getSurroundingScope(astNode.findFirstChild("classDef"));
        assertEquals(expectedScope, actualScope);

        astNode = createMockAST("public interface TestInterface { int p; } public interface TestInterface2 { int p; }");
        expectedScope = Scope.PUBLIC;
        actualScope = ScopeUtils.getSurroundingScope(astNode.findFirstChild("interfaceDef"));
        assertEquals(expectedScope, actualScope);

        astNode = createMockAST("public class TestClass { int p; } public interface TestInterface { int p; }");
        expectedScope = Scope.PACKAGE;
        actualScope = ScopeUtils.getSurroundingScope(astNode.findFirstChild("interfaceDef"));
        assertEquals(expectedScope, actualScope);

        astNode = createMockAST("public class TestClass { int p; } public class InnerClass { int p; }");
        expectedScope = Scope.PACKAGE;
        actualScope = ScopeUtils.getSurroundingScope(astNode.findFirstChild("classDef"));
        assertEquals(expectedScope, actualScope);

        astNode = createMockAST("public class TestClass { int p; } public class TestClass2 { int p; }");
        expectedScope = Scope.PACKAGE;
        actualScope = ScopeUtils.getSurroundingScope(astNode.findFirstChild("classDef"));
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST astNode = createMockAST("public interface TestInterface { int p; }");
        assertTrue(ScopeUtils.isInInterfaceBlock(astNode.findFirstChild("interfaceDef")));

        astNode = createMockAST("public class TestClass { int p; } public interface TestInterface { int p; }");
        assertFalse(ScopeUtils.isInInterfaceBlock(astNode.findFirstChild("classDef")));

        astNode = createMockAST("public class TestClass { int p; } public interface TestInterface { int p; } public interface TestInterface2 { int p; }");
        assertFalse(ScopeUtils.isInInterfaceBlock(astNode.findFirstChild("classDef")));

        astNode = createMockAST("public interface TestInterface { int p; } public interface TestInterface2 { int p; } public class TestClass { int p; }");
        assertFalse(ScopeUtils.isInInterfaceBlock(astNode.findFirstChild("classDef")));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST astNode = createMockAST("@interface TestAnnotation {}");
        assertTrue(ScopeUtils.isInAnnotationBlock(astNode.findFirstChild("annotation")));

        astNode = createMockAST("public class TestClass { @TestAnnotation int p; }");
        assertFalse(ScopeUtils.isInAnnotationBlock(astNode.findFirstChild("classDef")));

        astNode = createMockAST("public interface TestInterface { @TestAnnotation int p; }");
        assertFalse(ScopeUtils.isInAnnotationBlock(astNode.findFirstChild("interfaceDef")));

        astNode = createMockAST("public class TestClass { int p; @TestAnnotation int q; }");
        assertFalse(ScopeUtils.isInAnnotationBlock(astNode.findFirstChild("classDef")));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST astNode = createMockAST("@interface TestAnnotation {}");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode.findFirstChild("annotation")));

        astNode = createMockAST("public class TestClass { @TestAnnotation int p; }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode.findFirstChild("classDef")));

        astNode = createMockAST("public interface TestInterface { int p; @TestAnnotation int q; }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode.findFirstChild("interfaceDef")));

        astNode = createMockAST("public interface TestInterface { @TestAnnotation int p; }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode.findFirstChild("interfaceDef")));

        astNode = createMockAST("public interface TestInterface { int p; } @TestAnnotation");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode.findLastChild("annotation")));

        astNode = createMockAST("public class TestClass { int p; @TestAnnotation int q; }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode.findFirstChild("classDef")));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST astNode = createMockAST("public enum TestEnum { VALUE }");
        assertTrue(ScopeUtils.isInEnumBlock(astNode.findFirstChild("enumBodyDeclaration")));

        astNode = createMockAST("public class TestClass { enum TestEnum { VALUE } }");
        assertFalse(ScopeUtils.isInEnumBlock(astNode.findFirstChild("classDef")));

        astNode = createMockAST("public interface TestInterface { enum TestEnum { VALUE } }");
        assertFalse(ScopeUtils.isInEnumBlock(astNode.findFirstChild("interfaceDef")));

        astNode = createMockAST("public interface TestInterface { enum TestEnum { VALUE } } public class TestClass { }");
        assertFalse(ScopeUtils.isInEnumBlock(astNode.findFirstChild("classDef")));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST astNode = createMockAST("public class TestClass { int p; }");
        assertTrue(ScopeUtils.isInCodeBlock(astNode.findFirstChild("compilationUnit")));

        astNode = createMockAST("public class TestClass { int p; } public class InnerClass { int q; }");
        assertTrue(ScopeUtils.isInCodeBlock(astNode.findFirstChild("compilationUnit")));

        astNode = createMockAST("public class TestClass { int p; } public interface TestInterface { int q; }");
        assertTrue(ScopeUtils.isInCodeBlock(astNode.findFirstChild("compilationUnit")));

        astNode = createMockAST("public class TestClass { int p; } public interface TestInterface { int q; } public class InnerClass { int r; }");
        assertTrue(ScopeUtils.isInCodeBlock(astNode.findFirstChild("compilationUnit")));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST astNode = createMockAST("public class TestClass { int p; }");
        assertTrue(ScopeUtils.isOuterMostType(astNode.findFirstChild("classDef")));

        astNode = createMockAST("public class TestClass { int p; } public class InnerClass { int q; }");
        assertFalse(ScopeUtils.isOuterMostType(astNode.findFirstChild("classDef")));

        astNode = createMockAST("public interface TestInterface { int p; } public class TestClass { int q; }");
        assertFalse(ScopeUtils.isOuterMostType(astNode.findFirstChild("classDef")));

        astNode = createMockAST("public interface TestInterface { int p; } public interface TestInterface2 { int p; } public class TestClass { int q; }");
        assertFalse(ScopeUtils.isOuterMostType(astNode.findFirstChild("classDef")));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST astNode = createMockAST("public class TestClass { int p; void test() { int q; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode.findFirstChild("variableDeclarationList").findFirstChild("variableDeclarator")));

        astNode = createMockAST("public class TestClass { int p; void test() { int q = 0; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode.findFirstChild("variableDeclarationList").findFirstChild("variableDeclarator")));

        astNode = createMockAST("public class TestClass { int p; void test() { int q[] = new int[42]; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode.findFirstChild("variableDeclarationList").findFirstChild("variableDeclarator")));

        astNode = createMockAST("public class TestClass { int p; void test() { int q[] = new int[42]; int r[]; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode.findFirstChild("variableDeclarationList").findLastChild("variableDeclarator")));

        astNode = createMockAST("public class TestClass { int p; void test() { int q[]; int r[]; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode.findFirstChild("variableDeclarationList").getFirstChild("variableDeclarator")));

        astNode = createMockAST("public class TestClass { int p; void test() { int q[]; int r[]; } }");
        assertTrue(ScopeUtils.isLocalVariableDef(astNode.findFirstChild("variableDeclarationList").getLastChild("variableDeclarator")));

        astNode = createMockAST("public class TestClass { int p; void test() { int q[]; int r[]; } }");
        assertFalse(ScopeUtils.isLocalVariableDef(astNode.findFirstChild("classDef")));

        astNode = createMockAST("public class TestClass { int p; void test() { int q[]; int r[]; } int a; }");
        assertFalse(ScopeUtils.isLocalVariableDef(astNode.findLastChild("variableDeclarationList").getLastChild("variableDeclarator")));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST astNode = createMockAST("public class TestClass { int p; }");
        assertTrue(ScopeUtils.isClassFieldDef(astNode.findFirstChild("variableDeclarationList").getFirstChild("variableDeclarator")));

        astNode = createMockAST("public class TestClass { int p; }");
        assertFalse(ScopeUtils.isClassFieldDef(astNode.findFirstChild("classDef")));

        astNode = createMockAST("public class TestClass { int p; } void test() { int q; }");
        assertFalse(ScopeUtils.isClassFieldDef(astNode.findFirstChild("methodDeclaration").getFirstChild("variableDeclarationList").getFirstChild("variableDeclarator")));

        astNode = createMockAST("public class TestClass { int p; } void test() { int q; int r; }");
        assertFalse(ScopeUtils.isClassFieldDef(astNode.findFirstChild("methodDeclaration").getFirstChild("variableDeclarationList").getLastChild("variableDeclarator")));

        astNode = createMockAST("public class TestClass { int p; } void test() { int q[]; int r[]; }");
        assertFalse(ScopeUtils.isClassFieldDef(astNode.findFirstChild("methodDeclaration").getFirstChild("variableDeclarationList").getFirstChild("variableDeclarator")));

        astNode = createMockAST("public class TestClass { int p; } void test() { int q[]; int r[]; }");
        assertFalse(ScopeUtils.isClassFieldDef(astNode.findFirstChild("methodDeclaration").getFirstChild("variableDeclarationList").getLastChild("variableDeclarator")));
    }

    @Test
    public void testIsInScope() {
        DetailAST astNode = createMockAST("public class Outer { public class Inner { int p; } }");
        assertTrue(ScopeUtils.isInScope(astNode.findFirstChild("classDef"), Scope.ANONINNER));

        astNode = createMockAST("public interface TestInterface { int p; } public class TestClass { int p; }");
        assertFalse(ScopeUtils.isInScope(astNode.findFirstChild("classDef"), Scope.PUBLIC));

        astNode = createMockAST("public interface TestInterface { int p; } public class TestClass { int p; }");
        assertTrue(ScopeUtils.isInScope(astNode.findFirstChild("classDef"), Scope.PACKAGE));

        astNode = createMockAST("public class TestClass { int p; } public class InnerClass { int q; }");
        assertFalse(ScopeUtils.isInScope(astNode.findFirstChild("classDef"), Scope.PRIVATE));

        astNode = createMockAST("public class TestClass { int p; } public class InnerClass { int q; }");
        assertTrue(ScopeUtils.isInScope(astNode.findFirstChild("classDef"), Scope.PACKAGE));
    }

    private DetailAST createMockAST(String astText) {
        return new DetailAST() {
            @Override
            public String getText() {
                return astText;
            }

            @Override
            public int getType() {
                return -1;
            }

            @Override
            public DetailAST findFirstChild(String name) {
                return null;
            }

            @Override
            public DetailAST getParent() {
                return null;
            }

            @Override
            public DetailAST getNextSibling() {
                return null;
            }
        };
    }
}