package com.puppycrawl.tools.checkstyle.utils;

import static com.puppycrawl.tools.checkstyle.api.Scope.*;
import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = createMockMods("public protected private");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(mods));

        DetailAST modsEmpty = createMockMods("");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(modsEmpty));

        DetailAST modsProtected = createMockMods("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(modsProtected));

        DetailAST modsPrivate = createMockMods("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(modsPrivate));

        DetailAST modsPublicAndProtected = createMockMods("public protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(modsPublicAndProtected));

        DetailAST modsPublicAndPrivate = createMockMods("public private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(modsPublicAndPrivate));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = createMockNode(10, "classA");
        DetailAST mods = createMockMods("public");
        DetailAST parentClass = createMockNode(14, "classB");
        DetailAST parentInterface = createMockNode(15, "interfaceI");
        DetailAST parentAnnotation = createMockNode(157, "annotationAnno");
        DetailAST parentAnonInner = createMockNode(136, "anonInner");
        DetailAST parentEnum = createMockNode(154, "enumE");
        DetailAST parentCodeBlock = createMockNode(9, "codeBlock");

        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(ast));

        ast.setParent(mods);
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(ast));

        mods.setParent(parentClass);
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(ast));

        parentClass.setParent(parentInterface);
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(ast));

        parentInterface.setParent(parentAnnotation);
        assertEquals(Scope.ANNOTATION, ScopeUtils.getSurroundingScope(ast));

        parentAnnotation.setParent(parentAnonInner);
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(ast));

        parentAnonInner.setParent(parentEnum);
        assertEquals(Scope.ENUM, ScopeUtils.getSurroundingScope(ast));

        parentEnum.setParent(parentCodeBlock);
        assertEquals(Scope.CODEBLOCK, ScopeUtils.getSurroundingScope(ast));

        ast.setParent(parentClass);
        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(ast));

        ast.setParent(parentInterface);
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(ast));

        ast.setParent(parentAnnotation);
        assertEquals(Scope.ANNOTATION, ScopeUtils.getSurroundingScope(ast));

        ast.setParent(parentAnonInner);
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(ast));

        ast.setParent(parentEnum);
        assertEquals(Scope.ENUM, ScopeUtils.getSurroundingScope(ast));

        ast.setParent(parentCodeBlock);
        assertEquals(Scope.CODEBLOCK, ScopeUtils.getSurroundingScope(ast));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = createMockNode(15, "interfaceI");
        assertTrue(ScopeUtils.isInInterfaceBlock(ast));

        ast = createMockNode(10, "classA");
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));

        DetailAST parentInterface = createMockNode(15, "interfaceI");
        ast.setParent(parentInterface);
        assertTrue(ScopeUtils.isInInterfaceBlock(ast));

        DetailAST parentClass = createMockNode(14, "classB");
        parentInterface.setParent(parentClass);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = createMockNode(157, "annotationAnno");
        assertTrue(ScopeUtils.isInAnnotationBlock(ast));

        ast = createMockNode(10, "classA");
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));

        DetailAST parentAnnotation = createMockNode(157, "annotationAnno");
        ast.setParent(parentAnnotation);
        assertTrue(ScopeUtils.isInAnnotationBlock(ast));

        DetailAST parentClass = createMockNode(14, "classB");
        parentAnnotation.setParent(parentClass);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = createMockNode(15, "interfaceI");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        ast = createMockNode(157, "annotationAnno");
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        DetailAST astClass = createMockNode(14, "classA");
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(astClass));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = createMockNode(154, "enumE");
        assertTrue(ScopeUtils.isInEnumBlock(ast));

        ast = createMockNode(10, "classA");
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        DetailAST parentEnum = createMockNode(154, "enumE");
        ast.setParent(parentEnum);
        assertTrue(ScopeUtils.isInEnumBlock(ast));

        DetailAST parentClass = createMockNode(14, "classB");
        parentEnum.setParent(parentClass);
        assertFalse(ScopeUtils.isInEnumBlock(ast));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = createMockNode(9, "codeBlock");
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        ast = createMockNode(10, "classA");
        assertFalse(ScopeUtils.isInCodeBlock(ast));

        DetailAST parentCodeBlock = createMockNode(9, "codeBlock");
        ast.setParent(parentCodeBlock);
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        DetailAST parentClass = createMockNode(14, "classB");
        parentCodeBlock.setParent(parentClass);
        assertFalse(ScopeUtils.isInCodeBlock(ast));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = createMockNode(14, "classA");
        assertTrue(ScopeUtils.isOuterMostType(ast));

        ast.setParent(createMockNode(14, "classB"));
        assertFalse(ScopeUtils.isOuterMostType(ast));

        DetailAST astInterface = createMockNode(15, "interfaceI");
        assertFalse(ScopeUtils.isOuterMostType(astInterface));

        DetailAST astAnnotation = createMockNode(157, "annotationAnno");
        assertFalse(ScopeUtils.isOuterMostType(astAnnotation));

        DetailAST astEnum = createMockNode(154, "enumE");
        assertFalse(ScopeUtils.isOuterMostType(astEnum));

        DetailAST astAnonInner = createMockNode(136, "anonInner");
        assertFalse(ScopeUtils.isOuterMostType(astAnonInner));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = createMockNode(10, "varA");
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        ast = createMockNode(21, "varB");
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        DetailAST astClassField = createMockNode(10, "fieldA");
        assertFalse(ScopeUtils.isLocalVariableDef(astClassField));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = createMockNode(10, "fieldA");
        assertTrue(ScopeUtils.isClassFieldDef(ast));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = createMockNode(10, "classA");
        DetailAST mockScopeClass = createMockNode(14, "classB");
        DetailAST mockScopePackage = createMockNode(1, "packageP");

        assertFalse(ScopeUtils.isInScope(ast, PACKAGE));
        assertFalse(ScopeUtils.isInScope(ast, CLASS));
        assertTrue(ScopeUtils.isInScope(ast, mockScopeClass));
        assertFalse(ScopeUtils.isInScope(ast, mockScopePackage));
    }

    private DetailAST createMockMods(String modifiers) {
        DetailAST mods = new AstFactory().newList();
        DetailAST mod = new AstFactory().newSimpleNode(Tokens.MODIFIERS, modifiers);
        mods.add(mod);
        return mods;
    }

    private DetailAST createMockNode(int type, String name) {
        DetailAST node = new AstFactory().newSimpleNode(type, name);
        return node;
    }
}