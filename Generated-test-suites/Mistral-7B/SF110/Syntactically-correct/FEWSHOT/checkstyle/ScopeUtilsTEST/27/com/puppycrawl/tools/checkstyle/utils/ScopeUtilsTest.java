package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = AstFactory.newList();
        ast.add(AstFactory.newToken(TokenTypes.MODIFIERS, "public"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "MyClass"));

        Scope result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PUBLIC, result);

        ast.setChild(0, AstFactory.newToken(TokenTypes.MODIFIERS, "protected"));
        result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PROTECTED, result);

        ast.setChild(0, AstFactory.newToken(TokenTypes.MODIFIERS, "private"));
        result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PRIVATE, result);

        ast.setChild(0, AstFactory.newToken(TokenTypes.MODIFIERS, "default"));
        result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, result);

        ast.setChild(0, AstFactory.newToken(TokenTypes.MODIFIERS, ""));
        result = ScopeUtils.getScopeFromMods(ast);
        assertEquals(Scope.PACKAGE, result);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = AstFactory.newList();
        ast.add(AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyClass"));
        ast.add(AstFactory.newToken(TokenTypes.MODIFIERS, "public"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "MyClass"));
        ast.add(AstFactory.newToken(TokenTypes.LEFT_BRACE, "{"));

        DetailAST node = AstFactory.newToken(TokenTypes.IDENTIFIER, "field");
        ast.add(node);

        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.CLASS, result);

        node = AstFactory.newToken(TokenTypes.IDENTIFIER, "innerClass");
        ast.add(node);
        ast.add(AstFactory.newToken(TokenTypes.LEFT_BRACE, "{"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "innerField"));
        ast.add(AstFactory.newToken(TokenTypes.SEMICOLON, ";"));
        ast.add(AstFactory.newToken(TokenTypes.RIGHT_BRACE, "}"));

        result = ScopeUtils.getSurroundingScope(ast.findFirstToken(TokenTypes.IDENTIFIER, "innerField"));
        assertEquals(Scope.ANONINNER, result);

        node = AstFactory.newToken(TokenTypes.IDENTIFIER, "outerClass");
        ast.setChild(0, node);

        result = ScopeUtils.getSurroundingScope(node);
        assertEquals(Scope.PACKAGE, result);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = AstFactory.newList();
        ast.add(AstFactory.newToken(TokenTypes.INTERFACE_DEF, "interface MyInterface"));
        ast.add(AstFactory.newToken(TokenTypes.LEFT_BRACE, "{"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "method"));
        ast.add(AstFactory.newToken(TokenTypes.SEMICOLON, ";"));
        ast.add(AstFactory.newToken(TokenTypes.RIGHT_BRACE, "}"));

        DetailAST node = ast.findFirstToken(TokenTypes.IDENTIFIER, "method");
        assertTrue(ScopeUtils.isInInterfaceBlock(node));

        node = AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyClass");
        ast.setChild(0, node);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));

        node = AstFactory.newToken(TokenTypes.ANNOTATION, "@interface MyAnnotation { int value; }");
        ast.add(node);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = AstFactory.newList();
        ast.add(AstFactory.newToken(TokenTypes.ANNOTATION, "@annotation MyAnnotation { int value; }"));

        DetailAST node = ast.findFirstToken(TokenTypes.IDENTIFIER, "MyAnnotation");
        assertTrue(ScopeUtils.isInAnnotationBlock(node));

        node = AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyClass");
        ast.setChild(0, node);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));

        node = AstFactory.newToken(TokenTypes.INTERFACE_DEF, "interface MyInterface");
        ast.add(node);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = AstFactory.newList();
        ast.add(AstFactory.newToken(TokenTypes.ANNOTATION, "@annotation MyAnnotation { int value; }"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "MyAnnotation"));

        DetailAST node = ast.getFirstChild();
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        node = ast.getChild(1);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        ast.setChild(0, AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyClass"));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));

        ast.setChild(1, AstFactory.newToken(TokenTypes.IDENTIFIER, "MyClass"));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = AstFactory.newList();
        ast.add(AstFactory.newToken(TokenTypes.ENUM_DEF, "enum MyEnum"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "MyEnum"));
        ast.add(AstFactory.newToken(TokenTypes.LEFT_BRACE, "{"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "VALUE1"));
        ast.add(AstFactory.newToken(TokenTypes.SEMICOLON, ";"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "VALUE2"));
        ast.add(AstFactory.newToken(TokenTypes.SEMICOLON, ";"));
        ast.add(AstFactory.newToken(TokenTypes.RIGHT_BRACE, "}"));

        DetailAST node = ast.findFirstToken(TokenTypes.IDENTIFIER, "MyEnum");
        assertTrue(ScopeUtils.isInEnumBlock(node));

        node = AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyClass");
        ast.setChild(0, node);
        assertFalse(ScopeUtils.isInEnumBlock(node));

        node = AstFactory.newToken(TokenTypes.INTERFACE_DEF, "interface MyInterface");
        ast.add(node);
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = AstFactory.newList();
        ast.add(AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyClass"));
        ast.add(AstFactory.newToken(TokenTypes.LEFT_BRACE, "{"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "field"));
        ast.add(AstFactory.newToken(TokenTypes.SEMICOLON, ";"));
        ast.add(AstFactory.newToken(TokenTypes.RIGHT_BRACE, "}"));

        DetailAST node = ast.findFirstToken(TokenTypes.IDENTIFIER, "field");
        assertTrue(ScopeUtils.isInCodeBlock(node));

        node = AstFactory.newToken(TokenTypes.ANNOTATION, "@interface MyAnnotation { int value; }");
        ast.setChild(0, node);
        assertFalse(ScopeUtils.isInCodeBlock(node));

        node = AstFactory.newToken(TokenTypes.ENUM_DEF, "enum MyEnum");
        ast.add(node);
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = AstFactory.newList();
        ast.add(AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyClass"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "MyClass"));
        ast.add(AstFactory.newToken(TokenTypes.LEFT_BRACE, "{"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "field"));
        ast.add(AstFactory.newToken(TokenTypes.SEMICOLON, ";"));
        ast.add(AstFactory.newToken(TokenTypes.RIGHT_BRACE, "}"));

        DetailAST node = ast.getFirstChild();
        assertTrue(ScopeUtils.isOuterMostType(node));

        node = AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyInnerClass");
        ast.setChild(0, node);
        assertFalse(ScopeUtils.isOuterMostType(node));

        node = AstFactory.newToken(TokenTypes.INTERFACE_DEF, "interface MyInterface");
        ast.add(node);
        assertFalse(ScopeUtils.isOuterMostType(node));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = AstFactory.newList();
        ast.add(AstFactory.newToken(TokenTypes.VARIABLE_DEF, "int x;"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "x"));

        DetailAST node = ast.getFirstChild();
        assertTrue(ScopeUtils.isLocalVariableDef(node));

        ast.setChild(0, AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyClass"));
        assertFalse(ScopeUtils.isLocalVariableDef(node));

        node = AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyClass");
        ast.setChild(1, node);
        node = ast.getFirstChild();
        assertTrue(ScopeUtils.isLocalVariableDef(node));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = AstFactory.newList();
        ast.add(AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyClass"));
        ast.add(AstFactory.newToken(TokenTypes.MODIFIERS, "public static final int FIELD"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "FIELD"));
        ast.add(AstFactory.newToken(TokenTypes.SEMICOLON, ";"));

        DetailAST node = ast.getFirstChild();
        assertFalse(ScopeUtils.isLocalVariableDef(node));
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = ast.getChild(1);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
        assertTrue(ScopeUtils.isClassFieldDef(node));

        node = AstFactory.newToken(TokenTypes.VARIABLE_DEF, "int x;");
        ast.setChild(1, node);
        node = ast.getChild(2);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
        assertFalse(ScopeUtils.isClassFieldDef(node));

        node = AstFactory.newToken(TokenTypes.VARIABLE_DEF, "int x = 1;");
        ast.setChild(1, node);
        node = ast.getChild(2);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = AstFactory.newList();
        ast.add(AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyClass"));
        ast.add(AstFactory.newToken(TokenTypes.MODIFIERS, "public static"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "MyClass"));
        ast.add(AstFactory.newToken(TokenTypes.LEFT_BRACE, "{"));
        ast.add(AstFactory.newToken(TokenTypes.MODIFIERS, "public static final int FIELD1"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "FIELD1"));
        ast.add(AstFactory.newToken(TokenTypes.SEMICOLON, ";"));
        ast.add(AstFactory.newToken(TokenTypes.MODIFIERS, "private int FIELD2"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "FIELD2"));
        ast.add(AstFactory.newToken(TokenTypes.SEMICOLON, ";"));
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "method"));
        ast.add(AstFactory.newToken(TokenTypes.LPAREN, "("));
        ast.add(AstFactory.newToken(TokenTypes.RPAREN, ")"));
        ast.add(AstFactory.newToken(TokenTypes.SEMICOLON, ";"));
        ast.add(AstFactory.newToken(TokenTypes.RIGHT_BRACE, "}"));

        DetailAST field1Node = ast.findFirstToken(TokenTypes.IDENTIFIER, "FIELD1");
        assertTrue(ScopeUtils.isInScope(field1Node, Scope.CLASS));

        DetailAST field2Node = ast.findFirstToken(TokenTypes.IDENTIFIER, "FIELD2");
        assertTrue(ScopeUtils.isInScope(field2Node, Scope.CLASS));

        DetailAST methodNode = ast.findFirstToken(TokenTypes.IDENTIFIER, "method");
        assertFalse(ScopeUtils.isInScope(methodNode, Scope.CLASS));

        DetailAST classNode = AstFactory.newToken(TokenTypes.CLASS_DEF, "class MyInnerClass");
        ast.add(classNode);
        DetailAST innerFieldNode = AstFactory.newToken(TokenTypes.VARIABLE_DEF, "int innerField = 1;");
        ast.add(AstFactory.newToken(TokenTypes.IDENTIFIER, "MyInnerClass"));
        ast.add(classNode);
        ast.add(innerFieldNode);

        assertTrue(ScopeUtils.isInScope(innerFieldNode, Scope.ANONINNER));
        assertFalse(ScopeUtils.isInScope(innerFieldNode, Scope.CLASS));
    }
}