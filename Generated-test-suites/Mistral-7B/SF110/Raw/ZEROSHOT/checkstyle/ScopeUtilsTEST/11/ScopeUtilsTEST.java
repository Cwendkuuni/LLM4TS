package com.puppycrawl.tools.checkstyle.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST();
        ast.setType(1); // root node
        DetailAST mods = new DetailAST();
        mods.setType(5); // ModifierList
        DetailAST publicMod = new DetailAST();
        publicMod.setText("public");
        DetailAST protectedMod = new DetailAST();
        protectedMod.setText("protected");
        DetailAST privateMod = new DetailAST();
        privateMod.setText("private");

        mods.addChild(publicMod);
        mods.addChild(protectedMod);
        mods.addChild(privateMod);

        ast.addChild(mods);

        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(mods));

        mods.removeChild(publicMod);
        mods.addChild(new DetailAST().setText("final"));

        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(mods));
    }

    @Test
    public void testGetScopeFromModsWithMultipleScopes() {
        DetailAST ast = new DetailAST();
        ast.setType(1); // root node
        DetailAST mods = new DetailAST();
        mods.setType(5); // ModifierList
        DetailAST publicMod = new DetailAST();
        publicMod.setText("public");
        DetailAST protectedMod = new DetailAST();
        protectedMod.setText("protected");
        DetailAST packageMod = new DetailAST();
        packageMod.setText("package");
        DetailAST privateMod = new DetailAST();
        privateMod.setText("private");

        mods.addChild(publicMod);
        mods.addChild(protectedMod);
        mods.addChild(packageMod);
        mods.addChild(privateMod);

        ast.addChild(mods);

        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(mods));
    }

    @Test
    public void testGetScopeFromModsWithoutAnyScope() {
        DetailAST ast = new DetailAST();
        ast.setType(1); // root node
        DetailAST mods = new DetailAST();
        mods.setType(5); // ModifierList
        ast.addChild(mods);

        assertNull(ScopeUtils.getScopeFromMods(mods));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = new DetailAST();
        ast.setType(15); // InterfaceDeclaration
        DetailAST parent = new DetailAST();
        parent.setType(1); // root node
        parent.addChild(ast);

        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(ast));
    }

    @Test
    public void testGetSurroundingScopeFromClass() {
        DetailAST ast = new DetailAST();
        ast.setType(14); // ClassDeclaration
        DetailAST parent = new DetailAST();
        parent.setType(1); // root node
        parent.addChild(ast);

        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(ast));
    }

    @Test
    public void testGetSurroundingScopeFromAnonymousClass() {
        DetailAST ast = new DetailAST();
        ast.setType(136); // AnonymousClassDeclaration
        DetailAST parent = new DetailAST();
        parent.setType(1); // root node
        parent.addChild(ast);

        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(ast));
    }

    @Test
    public void testGetSurroundingScopeFromMethod() {
        DetailAST ast = new DetailAST();
        ast.setType(3); // MethodDeclaration
        DetailAST parent = new DetailAST();
        parent.setType(11); // ClassBody
        DetailAST classBody = new DetailAST();
        classBody.setType(1); // root node
        classBody.addChild(parent);
        classBody.addChild(ast);

        assertEquals(Scope.CLASS, ScopeUtils.getSurroundingScope(ast));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = new DetailAST();
        ast.setType(15); // InterfaceDeclaration
        assertTrue(ScopeUtils.isInInterfaceBlock(ast));

        ast.setType(14); // ClassDeclaration
        assertFalse(ScopeUtils.isInInterfaceBlock(ast));

        DetailAST interfaceMethod = new DetailAST();
        interfaceMethod.setType(3); // MethodDeclaration
        ast.addChild(interfaceMethod);

        assertFalse(ScopeUtils.isInInterfaceBlock(interfaceMethod));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = new DetailAST();
        ast.setType(157); // Annotation
        assertTrue(ScopeUtils.isInAnnotationBlock(ast));

        ast.setType(14); // ClassDeclaration
        assertFalse(ScopeUtils.isInAnnotationBlock(ast));

        DetailAST annotationMethod = new DetailAST();
        annotationMethod.setType(3); // MethodDeclaration
        DetailAST annotation = new DetailAST();
        annotation.setType(157); // Annotation
        annotation.addChild(annotationMethod);
        ast.addChild(annotation);

        assertFalse(ScopeUtils.isInAnnotationBlock(annotationMethod));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = new DetailAST();
        ast.setType(15); // InterfaceDeclaration
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        ast.setType(14); // ClassDeclaration
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast));

        DetailAST interfaceMethod = new DetailAST();
        interfaceMethod.setType(3); // MethodDeclaration
        ast.addChild(interfaceMethod);

        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(interfaceMethod));

        DetailAST annotation = new DetailAST();
        annotation.setType(157); // Annotation
        ast.addChild(annotation);

        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(annotation));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = new DetailAST();
        ast.setType(154); // EnumDeclaration
        assertTrue(ScopeUtils.isInEnumBlock(ast));

        ast.setType(14); // ClassDeclaration
        assertFalse(ScopeUtils.isInEnumBlock(ast));

        DetailAST enumConstant = new DetailAST();
        enumConstant.setType(10); // VariableDeclaration
        ast.addChild(enumConstant);

        assertFalse(ScopeUtils.isInEnumBlock(enumConstant));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = new DetailAST();
        ast.setType(9); // CompilationUnit
        assertTrue(ScopeUtils.isInCodeBlock(ast));

        DetailAST method = new DetailAST();
        method.setType(3); // MethodDeclaration
        ast.addChild(method);

        assertFalse(ScopeUtils.isInCodeBlock(method));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = new DetailAST();
        ast.setType(14); // ClassDeclaration
        assertTrue(ScopeUtils.isOuterMostType(ast));

        DetailAST classInsideClass = new DetailAST();
        classInsideClass.setType(14); // ClassDeclaration
        ast.addChild(classInsideClass);

        assertFalse(ScopeUtils.isOuterMostType(classInsideClass));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = new DetailAST();
        ast.setType(10); // VariableDeclaration
        assertTrue(ScopeUtils.isLocalVariableDef(ast));

        ast.setType(1); // root node
        DetailAST variableDeclaration = new DetailAST();
        variableDeclaration.setType(10); // VariableDeclaration
        ast.addChild(variableDeclaration);

        assertFalse(ScopeUtils.isLocalVariableDef(ast));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = new DetailAST();
        ast.setType(10); // VariableDeclaration
        assertFalse(ScopeUtils.isClassFieldDef(ast));

        DetailAST classField = new DetailAST();
        classField.setType(10); // VariableDeclaration
        DetailAST classDeclaration = new DetailAST();
        classDeclaration.setType(14); // ClassDeclaration
        classDeclaration.addChild(classField);

        assertTrue(ScopeUtils.isClassFieldDef(classField));
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ast.setType(1); // root node
        DetailAST classDeclaration = new DetailAST();
        classDeclaration.setType(14); // ClassDeclaration
        DetailAST classBody = new DetailAST();
        classBody.setType(11); // ClassBody
        DetailAST methodDeclaration = new DetailAST();
        methodDeclaration.setType(3); // MethodDeclaration
        DetailAST methodBody = new DetailAST();
        methodBody.setType(96); // MethodBody
        DetailAST localVariable = new DetailAST();
        localVariable.setType(21); // VariableDeclaration

        ast.addChild(classDeclaration);
        classDeclaration.addChild(classBody);
        classBody.addChild(methodDeclaration);
        methodDeclaration.addChild(methodBody);
        methodBody.addChild(localVariable);

        assertTrue(ScopeUtils.isInScope(localVariable, Scope.CLASS));
        assertFalse(ScopeUtils.isInScope(localVariable, Scope.INTERFACE));
        assertFalse(ScopeUtils.isInScope(localVariable, Scope.ANONINNER));
        assertFalse(ScopeUtils.isInScope(localVariable, Scope.PACKAGE));
        assertFalse(ScopeUtils.isInScope(localVariable, Scope.PRIVATE));
        assertFalse(ScopeUtils.isInScope(localVariable, Scope.PROTECTED));
        assertFalse(ScopeUtils.isInScope(localVariable, Scope.PUBLIC));
    }
}