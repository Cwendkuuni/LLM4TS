package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.AST;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST modsAST = createModsAST("public protected private");
        Scope expectedScope = Scope.PROTECTED;
        Scope actualScope = ScopeUtils.getScopeFromMods(modsAST);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetScopeFromMods_emptyMods() {
        DetailAST modsAST = createModsAST("");
        Scope expectedScope = Scope.PACKAGE;
        Scope actualScope = ScopeUtils.getScopeFromMods(modsAST);
        assertEquals(expectedScope, actualScope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromMods_nullMods() {
        DetailAST modsAST = null;
        ScopeUtils.getScopeFromMods(modsAST);
    }

    @Test
    public void testGetSurroundingScope_publicClass() {
        DetailAST node = createNodeAST("public class MyClass { }");
        Scope expectedScope = Scope.PUBLIC;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_protectedClass() {
        DetailAST node = createNodeAST("protected class MyClass { }");
        Scope expectedScope = Scope.PROTECTED;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_privateClass() {
        DetailAST node = createNodeAST("private class MyClass { }");
        Scope expectedScope = Scope.PRIVATE;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_packageClass() {
        DetailAST node = createNodeAST("class MyClass { }");
        Scope expectedScope = Scope.PACKAGE;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_interface() {
        DetailAST node = createNodeAST("interface MyInterface { }");
        Scope expectedScope = Scope.INTERFACE;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_annotation() {
        DetailAST node = createNodeAST("@MyAnnotation {}");
        Scope expectedScope = Scope.ANNOTATION;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_innerClass() {
        DetailAST node = createNodeAST("class OuterClass { class InnerClass { } }");
        Scope expectedScope = Scope.ANONINNER;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testIsInInterfaceBlock_interfaceNode() {
        DetailAST node = createNodeAST("interface MyInterface {}");
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInInterfaceBlock_classNode() {
        DetailAST node = createNodeAST("class MyClass {}");
        boolean expectedResult = false;
        boolean actualResult = ScopeUtils.isInInterfaceBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInAnnotationBlock_annotationNode() {
        DetailAST node = createNodeAST("@MyAnnotation {}");
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInAnnotationBlock_classNode() {
        DetailAST node = createNodeAST("class MyClass {}");
        boolean expectedResult = false;
        boolean actualResult = ScopeUtils.isInAnnotationBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_interfaceNode() {
        DetailAST node = createNodeAST("interface MyInterface {}");
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_classNode() {
        DetailAST node = createNodeAST("class MyClass {}");
        boolean expectedResult = false;
        boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInEnumBlock_enumNode() {
        DetailAST node = createNodeAST("enum MyEnum {}");
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInEnumBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInEnumBlock_classNode() {
        DetailAST node = createNodeAST("class MyClass {}");
        boolean expectedResult = false;
        boolean actualResult = ScopeUtils.isInEnumBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInCodeBlock_codeBlockNode() {
        DetailAST node = createNodeAST("public void myMethod() { }");
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInCodeBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInCodeBlock_commentNode() {
        DetailAST node = createNodeAST("// This is a comment");
        boolean expectedResult = false;
        boolean actualResult = ScopeUtils.isInCodeBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsOuterMostType_outermostTypeNode() {
        DetailAST node = createNodeAST("package mypackage; class MyClass {}");
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isOuterMostType(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsOuterMostType_innerClassNode() {
        DetailAST node = createNodeAST("class OuterClass { class InnerClass {} }");
        boolean expectedResult = false;
        boolean actualResult = ScopeUtils.isOuterMostType(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsLocalVariableDef_localVariableNode() {
        DetailAST node = createNodeAST("int myVariable = 10;");
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsLocalVariableDef_classFieldNode() {
        DetailAST node = createNodeAST("int myField;");
        boolean expectedResult = false;
        boolean actualResult = ScopeUtils.isLocalVariableDef(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsClassFieldDef_classFieldNode() {
        DetailAST node = createNodeAST("int myField;");
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isClassFieldDef(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInScope_inScopeNode() {
        DetailAST node = createNodeAST("public class MyClass { int myField; }");
        Scope expectedScope = Scope.PUBLIC;
        ScopeUtils.setGlobalScope(expectedScope);
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertTrue(actualScope.isIn(expectedScope));
    }

    @Test
    public void testIsInScope_outOfScopeNode() {
        DetailAST node = createNodeAST("// This is a comment");
        Scope expectedScope = Scope.PUBLIC;
        ScopeUtils.setGlobalScope(expectedScope);
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        assertFalse(actualScope.isIn(expectedScope));
    }

    private DetailAST createNodeAST(String code) {
        DetailAST rootNode = new DetailAST();
        ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(code.getBytes()));
        CheckstyleLexer lexer = new CheckstyleLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ParseTree tree = new CheckstyleParser(tokens).parse();
        rootNode.setLine(tree.getToken(0).getLine());
        rootNode.setColumn(tree.getToken(0).getColumn());
        rootNode.setTokenList(new ArrayList<>());
        for (Token token : tree.getTokens()) {
            rootNode.getTokenList().add(new DetailAST(token));
        }
        return rootNode;
    }

    private DetailAST createModsAST(String modifiers) {
        DetailAST rootNode = new DetailAST();
        DetailAST modsNode = new DetailAST(new ModifierDeclarationListDef());
        DetailAST modifierNode = null;
        for (String modifier : modifiers.split("\\s+")) {
            modifierNode = new DetailAST(new ModifierDeclaration());
            modifierNode.setImage(modifier);
            modsNode.addChild(modifierNode);
            rootNode.addChild(modsNode);
        }
        rootNode.setTokenList(new ArrayList<>());
        rootNode.addChild(new DetailAST(new EmptyStmt()));
        return rootNode;
    }

    private void setGlobalScope(Scope scope) {
        ScopeUtils.setGlobalScope(scope);
    }
}