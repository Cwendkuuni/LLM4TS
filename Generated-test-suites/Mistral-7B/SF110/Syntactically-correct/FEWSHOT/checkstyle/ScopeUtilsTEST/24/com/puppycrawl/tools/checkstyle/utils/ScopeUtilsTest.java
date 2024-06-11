package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST modsAST = new DetailAST();
        DetailAST publicToken = new DetailAST();
        DetailAST protectedToken = new DetailAST();
        DetailAST privateToken = new DetailAST();

        publicToken.setText("public");
        protectedToken.setText("protected");
        privateToken.setText("private");

        modsAST.addChild(publicToken);
        modsAST.addChild(protectedToken);
        modsAST.addChild(privateToken);

        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(modsAST));

        modsAST.removeChild(0);
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(modsAST));

        modsAST.removeChild(0);
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(modsAST));

        modsAST.removeChild(0);
        assertNull(ScopeUtils.getScopeFromMods(modsAST));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST classAST = new DetailAST();
        DetailAST interfaceBlockAST = new DetailAST();
        DetailAST methodAST = new DetailAST();
        DetailAST annotationBlockAST = new DetailAST();
        DetailAST enumBlockAST = new DetailAST();
        DetailAST codeBlockAST = new DetailAST();

        classAST.setType(9);
        interfaceBlockAST.setType(15);
        methodAST.setType(3);
        annotationBlockAST.setType(157);
        enumBlockAST.setType(154);
        codeBlockAST.setType(11);

        classAST.addChild(interfaceBlockAST);
        interfaceBlockAST.addChild(methodAST);
        interfaceBlockAST.addChild(annotationBlockAST);
        interfaceBlockAST.addChild(enumBlockAST);
        interfaceBlockAST.addChild(codeBlockAST);

        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(methodAST));
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(annotationBlockAST));
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(enumBlockAST));
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(codeBlockAST));
        assertEquals(Scope.INTERFACE, ScopeUtils.getSurroundingScope(classAST));

        methodAST.setParent(null);
        annotationBlockAST.setParent(null);
        enumBlockAST.setParent(null);
        codeBlockAST.setParent(null);

        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(methodAST));
        assertNull(ScopeUtils.getSurroundingScope(annotationBlockAST));
        assertNull(ScopeUtils.getSurroundingScope(enumBlockAST));
        assertNull(ScopeUtils.getSurroundingScope(codeBlockAST));
        assertNull(ScopeUtils.getSurroundingScope(classAST));
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST interfaceBlockAST = new DetailAST();
        DetailAST classAST = new DetailAST();
        DetailAST methodAST = new DetailAST();

        interfaceBlockAST.setType(15);
        classAST.setType(9);
        methodAST.setType(3);

        classAST.addChild(interfaceBlockAST);
        classAST.addChild(methodAST);

        assertTrue(ScopeUtils.isInInterfaceBlock(methodAST));
        assertFalse(ScopeUtils.isInInterfaceBlock(interfaceBlockAST));
        assertFalse(ScopeUtils.isInInterfaceBlock(classAST));
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST annotationBlockAST = new DetailAST();
        DetailAST classAST = new DetailAST();
        DetailAST methodAST = new DetailAST();

        annotationBlockAST.setType(157);
        classAST.setType(9);
        methodAST.setType(3);

        classAST.addChild(methodAST);
        classAST.addChild(annotationBlockAST);

        assertTrue(ScopeUtils.isInAnnotationBlock(methodAST));
        assertTrue(ScopeUtils.isInAnnotationBlock(annotationBlockAST));
        assertFalse(ScopeUtils.isInAnnotationBlock(classAST));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST interfaceAST = new DetailAST();
        DetailAST annotationAST = new DetailAST();
        DetailAST classAST = new DetailAST();
        DetailAST methodAST = new DetailAST();

        interfaceAST.setType(15);
        annotationAST.setType(157);
        classAST.setType(9);
        methodAST.setType(3);

        classAST.addChild(methodAST);
        interfaceAST.addChild(annotationAST);
        classAST.addChild(interfaceAST);

        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(methodAST));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(annotationAST));
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(interfaceAST));
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(classAST));
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST enumBlockAST = new DetailAST();
        DetailAST classAST = new DetailAST();
        DetailAST methodAST = new DetailAST();

        enumBlockAST.setType(154);
        classAST.setType(9);
        methodAST.setType(3);

        classAST.addChild(methodAST);
        classAST.addChild(enumBlockAST);

        assertTrue(ScopeUtils.isInEnumBlock(methodAST));
        assertTrue(ScopeUtils.isInEnumBlock(enumBlockAST));
        assertFalse(ScopeUtils.isInEnumBlock(classAST));
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST codeBlockAST = new DetailAST();
        DetailAST classAST = new DetailAST();
        DetailAST methodAST = new DetailAST();

        codeBlockAST.setType(11);
        classAST.setType(9);
        methodAST.setType(3);

        classAST.addChild(methodAST);
        classAST.addChild(codeBlockAST);

        assertTrue(ScopeUtils.isInCodeBlock(methodAST));
        assertTrue(ScopeUtils.isInCodeBlock(codeBlockAST));
        assertFalse(ScopeUtils.isInCodeBlock(classAST));
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST outermostTypeAST = new DetailAST();
        DetailAST classAST = new DetailAST();

        outermostTypeAST.setType(14);
        classAST.setType(9);
        classAST.addChild(outermostTypeAST);

        assertTrue(ScopeUtils.isOuterMostType(classAST));
        assertTrue(ScopeUtils.isOuterMostType(outermostTypeAST));
        assertFalse(ScopeUtils.isOuterMostType(new DetailAST()));
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST localVariableDefAST = new DetailAST();
        DetailAST variableDeclarationAST = new DetailAST();
        DetailAST variableInitializationAST = new DetailAST();

        localVariableDefAST.setType(7);
        variableDeclarationAST.setType(10);
        variableInitializationAST.setType(156);

        variableDeclarationAST.addChild(localVariableDefAST);
        variableDeclarationAST.addChild(variableInitializationAST);

        assertTrue(ScopeUtils.isLocalVariableDef(localVariableDefAST));
        assertTrue(ScopeUtils.isLocalVariableDef(variableDeclarationAST));
        assertFalse(ScopeUtils.isLocalVariableDef(variableInitializationAST));

        localVariableDefAST.setParent(null);
        variableInitializationAST.setParent(null);

        assertFalse(ScopeUtils.isLocalVariableDef(localVariableDefAST));
        assertFalse(ScopeUtils.isLocalVariableDef(variableInitializationAST));
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST classFieldDefAST = new DetailAST();
        DetailAST variableDeclarationAST = new DetailAST();

        classFieldDefAST.setType(10);
        variableDeclarationAST.setType(10);

        assertFalse(ScopeUtils.isClassFieldDef(classFieldDefAST));
        assertFalse(ScopeUtils.isClassFieldDef(variableDeclarationAST));

        DetailAST localVariableDefAST = new DetailAST();
        localVariableDefAST.setType(10);

        variableDeclarationAST.addChild(localVariableDefAST);

        assertFalse(ScopeUtils.isClassFieldDef(classFieldDefAST));
        assertFalse(ScopeUtils.isClassFieldDef(variableDeclarationAST));
        assertFalse(ScopeUtils.isClassFieldDef(localVariableDefAST));

        DetailAST classAST = new DetailAST();
        classAST.setType(9);
        variableDeclarationAST.setParent(classAST);

        assertTrue(ScopeUtils.isClassFieldDef(classFieldDefAST));
        assertFalse(ScopeUtils.isClassFieldDef(variableDeclarationAST));
        assertFalse(ScopeUtils.isClassFieldDef(classAST));
    }

    @Test
    public void testIsInScope() {
        DetailAST classAST = new DetailAST();
        DetailAST methodAST = new DetailAST();
        DetailAST annotationAST = new DetailAST();
        DetailAST localVariableAST = new DetailAST();
        DetailAST globalVariableAST = new DetailAST();

        classAST.setType(9);
        methodAST.setType(3);
        annotationAST.setType(157);
        localVariableAST.setType(10);
        globalVariableAST.setType(10);

        classAST.addChild(methodAST);
        classAST.addChild(annotationAST);
        classAST.addChild(localVariableAST);
        classAST.addChild(new DetailAST().setText("globalVariable"));

        assertTrue(ScopeUtils.isInScope(methodAST, Scope.CLASSES));
        assertTrue(ScopeUtils.isInScope(annotationAST, Scope.ANNOTATIONS));
        assertFalse(ScopeUtils.isInScope(methodAST, Scope.LOCALS));
        assertFalse(ScopeUtils.isInScope(annotationAST, Scope.LOCALS));
        assertFalse(ScopeUtils.isInScope(localVariableAST, Scope.ANNOTATIONS));
        assertTrue(ScopeUtils.isInScope(localVariableAST, Scope.LOCALS));
        assertTrue(ScopeUtils.isInScope(globalVariableAST, Scope.GLOBALS));
        assertFalse(ScopeUtils.isInScope(classAST, Scope.GLOBALS));
    }
}