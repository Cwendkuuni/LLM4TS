package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.AST;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST();
        ast.setType(5); // MODIFIERS
        ast.addChild(createToken("public"));
        ast.addChild(createToken("class"));
        ast.addChild(createToken("MyClass"));

        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, actual);

        // Test other scopes
        ast.removeFirstChild();
        ast.addChild(createToken("protected"));
        expected = Scope.PROTECTED;
        actual = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, actual);

        ast.removeFirstChild();
        ast.addChild(createToken("private"));
        expected = Scope.PRIVATE;
        actual = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, actual);

        ast.removeFirstChild();
        expected = Scope.PACKAGE;
        actual = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, actual);

        // Test no modifiers
        ast.removeAllChildren();
        expected = Scope.PACKAGE;
        actual = ScopeUtils.getScopeFromMods(ast);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromModsWithNullAST() {
        ScopeUtils.getScopeFromMods(null);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST astNode = createNode("class MyClass { int x; }");

        Scope expected = Scope.PACKAGE;
        Scope actual = ScopeUtils.getSurroundingScope(astNode.findFirstToken(1)); // findFirstToken(1) is for finding the first token of the class declaration
        assertEquals(expected, actual);

        expected = Scope.PUBLIC;
        astNode = createNode("public class MyClass { int x; }");
        actual = ScopeUtils.getSurroundingScope(astNode.findFirstToken(1));
        assertEquals(expected, actual);

        expected = Scope.PROTECTED;
        astNode = createNode("protected class MyClass { int x; }");
        actual = ScopeUtils.getSurroundingScope(astNode.findFirstToken(1));
        assertEquals(expected, actual);

        expected = Scope.PRIVATE;
        astNode = createNode("private class MyClass { int x; }");
        actual = ScopeUtils.getSurroundingScope(astNode.findFirstToken(1));
        assertEquals(expected, actual);

        expected = Scope.ANONINNER;
        astNode = createNode("class OuterClass { void method() { class InnerClass { int x; } } }");
        actual = ScopeUtils.getSurroundingScope(astNode.findFirstChild(2).findFirstChild(2)); // findFirstChild(2) is for finding the first child of OuterClass, findFirstChild(2).findFirstChild(2) is for finding the InnerClass
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSurroundingScopeWithNullNode() {
        ScopeUtils.getSurroundingScope(null);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST astNode = createNode("interface MyInterface { int x; }");
        assertTrue(ScopeUtils.isInInterfaceBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the interface declaration

        astNode = createNode("class MyClass implements MyInterface { int x; }");
        assertFalse(ScopeUtils.isInInterfaceBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the class declaration
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInInterfaceBlockWithNullNode() {
        ScopeUtils.isInInterfaceBlock(null);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST astNode = createNode("@MyAnnotation {}");
        assertTrue(ScopeUtils.isInAnnotationBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the @ symbol

        astNode = createNode("public class MyClass { @MyAnnotation int x; }");
        assertFalse(ScopeUtils.isInAnnotationBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the class declaration
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInAnnotationBlockWithNullNode() {
        ScopeUtils.isInAnnotationBlock(null);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST astNode = createNode("interface MyInterface { int x; }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the interface declaration

        astNode = createNode("@MyAnnotation public class MyClass { int x; }");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the @ symbol

        astNode = createNode("public class MyClass { int x; }");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the class declaration
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInInterfaceOrAnnotationBlockWithNullNode() {
        ScopeUtils.isInInterfaceOrAnnotationBlock(null);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST astNode = createNode("enum MyEnum { VALUE1, VALUE2 }");
        assertTrue(ScopeUtils.isInEnumBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the enum keyword

        astNode = createNode("public class MyClass { enum MyEnum { VALUE1, VALUE2 } }");
        assertFalse(ScopeUtils.isInEnumBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the class declaration
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInEnumBlockWithNullNode() {
        ScopeUtils.isInEnumBlock(null);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST astNode = createNode("class MyClass { int x; }");
        assertTrue(ScopeUtils.isInCodeBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the class declaration

        astNode = createNode("public class MyClass { int x; }");
        assertTrue(ScopeUtils.isInCodeBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the class declaration

        astNode = createNode("interface MyInterface { int x; }");
        assertFalse(ScopeUtils.isInCodeBlock(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the interface declaration
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInCodeBlockWithNullNode() {
        ScopeUtils.isInCodeBlock(null);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST astNode = createNode("class OuterClass { class InnerClass { int x; } }");
        assertFalse(ScopeUtils.isOuterMostType(astNode.findFirstChild(1).findFirstChild(1))); // findFirstChild(1) is for finding the first child of OuterClass, findFirstChild(1).findFirstChild(1) is for finding InnerClass

        astNode = createNode("class MyClass { int x; }");
        assertTrue(ScopeUtils.isOuterMostType(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the class declaration
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsOuterMostTypeWithNullNode() {
        ScopeUtils.isOuterMostType(null);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST astNode = createNode("class MyClass { int x; void method() { int y; } }");

        assertTrue(ScopeUtils.isLocalVariableDef(astNode.findFirstChild(1).findFirstChild(1))); // findFirstChild(1) is for finding the first child of MyClass, findFirstChild(1).findFirstChild(1) is for finding the x variable

        assertTrue(ScopeUtils.isLocalVariableDef(astNode.findFirstChild(2).findFirstChild(1).findFirstChild(1))); // findFirstChild(2) is for finding the method, findFirstChild(1) is for finding the first child of the method, findFirstChild(1) is for finding the y variable

        assertFalse(ScopeUtils.isLocalVariableDef(astNode.findFirstToken(1))); // findFirstToken(1) is for finding the first token of the class declaration
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsLocalVariableDefWithNullNode() {
        ScopeUtils.isLocalVariableDef(null);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST astNode = createNode("class MyClass { int x; }");

        assertTrue(ScopeUtils.isClassFieldDef(astNode.findFirstChild(1).findFirstChild(1))); // findFirstChild(1) is for finding the first child of MyClass, findFirstChild(1).findFirstChild(1) is for finding the x field

        assertFalse(ScopeUtils.isClassFieldDef(astNode.findFirstChild(1).findFirstChild(2))); // findFirstChild(1) is for finding the first child of MyClass, findFirstChild(1).findFirstChild(2) is for finding the method
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsClassFieldDefWithNullNode() {
        ScopeUtils.isClassFieldDef(null);
    }

    @Test
    public void testIsInScope() {
        DetailAST astNode = createNode("class OuterClass { class InnerClass { int x; } }");
        assertTrue(ScopeUtils.isInScope(astNode.findFirstChild(1).findFirstChild(1), Scope.PACKAGE));

        assertFalse(ScopeUtils.isInScope(astNode.findFirstChild(1).findFirstChild(1), Scope.PUBLIC));

        assertTrue(ScopeUtils.isInScope(astNode.findFirstChild(1).findFirstChild(1), Scope.PROTECTED));

        assertTrue(ScopeUtils.isInScope(astNode.findFirstChild(1).findFirstChild(1), Scope.PRIVATE));

        assertTrue(ScopeUtils.isInScope(astNode.findFirstChild(1).findFirstChild(1), Scope.ANONINNER));

        assertTrue(ScopeUtils.isInScope(astNode.findFirstChild(1).findFirstChild(1), Scope.INNERCLASS));

        // Test with different scopes and nodes
        astNode = createNode("class MyClass { int x; }");
        assertTrue(ScopeUtils.isInScope(astNode.findFirstToken(1), Scope.PACKAGE));

        assertFalse(ScopeUtils.isInScope(astNode.findFirstToken(1), Scope.PUBLIC));

        assertTrue(ScopeUtils.isInScope(astNode.findFirstToken(1), Scope.PROTECTED));

        assertTrue(ScopeUtils.isInScope(astNode.findFirstToken(1), Scope.PRIVATE));

        assertFalse(ScopeUtils.isInScope(astNode.findFirstToken(1), Scope.ANONINNER));

        assertFalse(ScopeUtils.isInScope(astNode.findFirstToken(1), Scope.INNERCLASS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInScopeWithNullNodeOrScope() {
        ScopeUtils.isInScope(null, Scope.PACKAGE);
        ScopeUtils.isInScope(astNode.findFirstToken(1), null);
    }

    // Add test cases for edge cases and exception handling scenarios here if needed

    private DetailAST createNode(String javaCode) {
        // Create ANTLR AST from given Java code
        // ...
        return ast;
    }

    private Token createToken(String tokenName) {
        // Create ANTLR token with given token name
        // ...
        return token;
    }
}