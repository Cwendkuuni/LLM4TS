package com.example.junit.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.*;
import antlr.collections.*;
import com.puppycrawl.tools.checkstyle.utils.testutils.*;

import org.junit.*;

public class ScopeUtilsTest {

    @BeforeClass
    public static void setup() {
        CheckstyleUtils.initCheckstyle();
    }

    @AfterClass
    public static void teardown() {
        CheckstyleUtils.restoreDefaults();
    }

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = AstFactory.newList();
        mods.add(createToken("public"));
        mods.add(createToken("protected"));
        mods.add(createToken("private"));

        Scope expectedScope = Scope.PRIVATE;
        Scope actualScope = ScopeUtils.getScopeFromMods(mods);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScopeWithPublicModifiers() {
        DetailAST node = new SimpleNodeModule().createSequenceNode(
                new SimpleNodeModule().createLeafNode("Modifiers", "public"),
                new SimpleNodeModule().createLeafNode("TypeDeclaration")
        );
        Scope expectedScope = Scope.PUBLIC;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScopeWithProtectedModifiers() {
        DetailAST node = new SimpleNodeModule().createSequenceNode(
                new SimpleNodeModule().createLeafNode("Modifiers", "protected"),
                new SimpleNodeModule().createLeafNode("TypeDeclaration")
        );
        Scope expectedScope = Scope.PROTECTED;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScopeWithPrivateModifiers() {
        DetailAST node = new SimpleNodeModule().createSequenceNode(
                new SimpleNodeModule().createLeafNode("Modifiers", "private"),
                new SimpleNodeModule().createLeafNode("TypeDeclaration")
        );
        Scope expectedScope = Scope.PRIVATE;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScopeWithNoModifiers() {
        DetailAST node = new SimpleNodeModule().createLeafNode("TypeDeclaration");
        Scope expectedScope = Scope.PACKAGE;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);

        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testIsInInterfaceBlock_Interface() {
        DetailAST node = new SimpleNodeModule().createLeafNode("InterfaceDeclaration");
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInInterfaceBlock(node);

        assertTrue(expectedResult == actualResult);
    }

    @Test
    public void testIsInInterfaceBlock_Class() {
        DetailAST node = new SimpleNodeModule().createLeafNode("ClassDeclaration");
        boolean expectedResult = false;
        boolean actualResult = ScopeUtils.isInInterfaceBlock(node);

        assertTrue(expectedResult == actualResult);
    }

    @Test
    public void testIsInAnnotationBlock_Annotation() {
        DetailAST node = new SimpleNodeModule().createLeafNode("Annotation");
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInAnnotationBlock(node);

        assertTrue(expectedResult == actualResult);
    }

    @Test
    public void testIsInAnnotationBlock_Class() {
        DetailAST node = new SimpleNodeModule().createLeafNode("ClassDeclaration");
        boolean expectedResult = false;
        boolean actualResult = ScopeUtils.isInAnnotationBlock(node);

        assertTrue(expectedResult == actualResult);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new SimpleNodeModule().createLeafNode("MethodInvocation", "getScopeFromMods(aMods)");
        Scope scope = Scope.PACKAGE;

        assertTrue(ScopeUtils.isInScope(ast, scope));
    }

    @Test
    public void testIsInScope_WrongScope() {
        DetailAST ast = new SimpleNodeModule().createLeafNode("MethodInvocation", "getScopeFromMods(aMods)");
        Scope scope = Scope.PRIVATE;

        assertFalse(ScopeUtils.isInScope(ast, scope));
    }

    private DetailAST createToken(String text) {
        return new SimpleNodeModule().createLeafNode("Identifier", text);
    }
}