package com.puppycrawl.tools.checkstyle.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        String mods = "public protected private";
        DetailAST astMods = createMockAST(mods);
        Scope expectedScope = Scope.PROTECTED;
        Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetScopeFromMods_noModifiers() {
        String mods = "";
        DetailAST astMods = createMockAST(mods);
        Scope expectedScope = Scope.PACKAGE;
        Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetScopeFromMods_invalidModifier() {
        String mods = "invalid";
        DetailAST astMods = createMockAST(mods);
        Scope expectedScope = Scope.PACKAGE;
        Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetScopeFromMods_multipleModifiers() {
        String mods = "public protected private";
        DetailAST astMods = createMockAST(mods);
        Scope expectedScope = Scope.PROTECTED;
        Scope actualScope = ScopeUtils.getScopeFromMods(astMods);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_classLevel() {
        DetailAST astNode = createMockAST("ClassDef");
        Scope expectedScope = Scope.CLASS;
        Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_packageLevel() {
        DetailAST astNode = createMockAST("PackageDeclaration");
        Scope expectedScope = Scope.PACKAGE;
        Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_interfaceLevel() {
        DetailAST astNode = createMockAST("InterfaceDef");
        Scope expectedScope = Scope.INTERFACE;
        Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_annotationLevel() {
        DetailAST astNode = createMockAST("Annotation");
        Scope expectedScope = Scope.ANNOTATION;
        Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_anonymousInnerClassLevel() {
        DetailAST astNode = createMockAST("AnonymousClassDef");
        Scope expectedScope = Scope.ANONINNER;
        Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_methodLevel() {
        DetailAST astNode = createMockAST("MethodDef");
        Scope expectedScope = null;
        Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
        assertNull(actualScope);
    }

    @Test
    public void testIsInInterfaceBlock_interfaceLevel() {
        DetailAST astNode = createMockAST("InterfaceDef");
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInInterfaceBlock(astNode);
        assertTrue(expectedValue == actualValue);
    }

    @Test
    public void testIsInInterfaceBlock_classLevel() {
        DetailAST astNode = createMockAST("ClassDef");
        boolean expectedValue = false;
        boolean actualValue = ScopeUtils.isInInterfaceBlock(astNode);
        assertFalse(expectedValue == actualValue);
    }

    @Test
    public void testIsInAnnotationBlock_annotationLevel() {
        DetailAST astNode = createMockAST("Annotation");
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInAnnotationBlock(astNode);
        assertTrue(expectedValue == actualValue);
    }

    @Test
    public void testIsInAnnotationBlock_classLevel() {
        DetailAST astNode = createMockAST("ClassDef");
        boolean expectedValue = false;
        boolean actualValue = ScopeUtils.isInAnnotationBlock(astNode);
        assertFalse(expectedValue == actualValue);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_interfaceLevel() {
        DetailAST astNode = createMockAST("InterfaceDef");
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInInterfaceOrAnnotationBlock(astNode);
        assertTrue(expectedValue == actualValue);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_annotationLevel() {
        DetailAST astNode = createMockAST("Annotation");
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInInterfaceOrAnnotationBlock(astNode);
        assertTrue(expectedValue == actualValue);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_classLevel() {
        DetailAST astNode = createMockAST("ClassDef");
        boolean expectedValue = false;
        boolean actualValue = ScopeUtils.isInInterfaceOrAnnotationBlock(astNode);
        assertFalse(expectedValue == actualValue);
    }

    @Test
    public void testIsInEnumBlock_enumLevel() {
        DetailAST astNode = createMockAST("Enum");
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInEnumBlock(astNode);
        assertTrue(expectedValue == actualValue);
    }

    @Test
    public void testIsInEnumBlock_classLevel() {
        DetailAST astNode = createMockAST("ClassDef");
        boolean expectedValue = false;
        boolean actualValue = ScopeUtils.isInEnumBlock(astNode);
        assertFalse(expectedValue == actualValue);
    }

    @Test
    public void testIsInCodeBlock_codeBlockLevel() {
        DetailAST astNode = createMockAST("CodeBlock");
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInCodeBlock(astNode);
        assertTrue(expectedValue == actualValue);
    }

    @Test
    public void testIsInCodeBlock_nonCodeBlockLevel() {
        DetailAST astNode = createMockAST("MethodDef");
        boolean expectedValue = false;
        boolean actualValue = ScopeUtils.isInCodeBlock(astNode);
        assertFalse(expectedValue == actualValue);
    }

    @Test
    public void testIsOuterMostType_outermostTypeLevel() {
        DetailAST astNode = createMockAST("TypeDeclaration");
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isOuterMostType(astNode);
        assertTrue(expectedValue == actualValue);
    }

    @Test
    public void testIsOuterMostType_nonOutermostTypeLevel() {
        DetailAST astNode = createMockAST("MethodDef");
        boolean expectedValue = false;
        boolean actualValue = ScopeUtils.isOuterMostType(astNode);
        assertFalse(expectedValue == actualValue);
    }

    @Test
    public void testIsClassFieldDef_classFieldLevel() {
        DetailAST astNode = createMockAST("ClassFieldDef");
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isClassFieldDef(astNode);
        assertTrue(expectedValue == actualValue);
    }

    @Test
    public void testIsClassFieldDef_localVariableLevel() {
        DetailAST astNode = createMockAST("LocalVariableDef");
        boolean expectedValue = false;
        boolean actualValue = ScopeUtils.isClassFieldDef(astNode);
        assertFalse(expectedValue == actualValue);
    }

    @Test
    public void testIsInScope_classLevel() {
        DetailAST astNode = createMockAST("ClassDef");
        Scope expectedScope = Scope.CLASS;
        DetailAST mockParent = createMockAST("PackageDeclaration");
        DetailAST mockParentOfParent = createMockAST("ClassDef");
        DetailAST mockParentOfParentOfParent = createMockAST("PackageDeclaration");

        when(mockParent.getParent()).thenReturn(mockParentOfParent);
        when(mockParentOfParent.getParent()).thenReturn(mockParentOfParentOfParent);

        Scope actualScope = ScopeUtils.getSurroundingScope(astNode);
        assertTrue(ScopeUtils.isInScope(astNode, expectedScope));
    }

    @Test
    public void testIsInScope_localVariableLevel() {
        DetailAST astNode = createMockAST("LocalVariableDef");
        Scope expectedScope = null;
        DetailAST mockParent = createMockAST("CodeBlock");

        assertFalse(ScopeUtils.isInScope(astNode, expectedScope));
    }

    private DetailAST createMockAST(final String nodeType) {
        DetailAST ast = mock(DetailAST.class);
        when(ast.getType()).thenReturn(nodeTypeToInt(nodeType));
        return ast;
    }

    private int nodeTypeToInt(final String nodeType) {
        switch (nodeType) {
            case "ClassDef":
                return 14;
            case "InterfaceDef":
                return 15;
            case "PackageDeclaration":
                return 154;
            case "Annotation":
                return 157;
            case "MethodDef":
                return 136;
            case "CodeBlock":
                return 9;
            case "AnonymousClassDef":
                return 11;
            case "Enum":
                return 154;
            case "ClassFieldDef":
                return 10;
            case "LocalVariableDef":
                return 21;
            case "TypeDeclaration":
                return 1;
            default:
                throw new IllegalArgumentException("Invalid node type " + nodeType);
        }
    }
}