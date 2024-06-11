import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.utils.Scope.*;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods_Public() {
        DetailAST mods = new DetailAST();
        DetailAST publicToken = new DetailAST();
        publicToken.setText("public");
        mods.appendChild(publicToken);

        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(mods);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_Protected() {
        DetailAST mods = new DetailAST();
        DetailAST protectedToken = new DetailAST();
        protectedToken.setText("protected");
        mods.appendChild(protectedToken);

        Scope expected = Scope.PROTECTED;
        Scope actual = ScopeUtils.getScopeFromMods(mods);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_Private() {
        DetailAST mods = new DetailAST();
        DetailAST privateToken = new DetailAST();
        privateToken.setText("private");
        mods.appendChild(privateToken);

        Scope expected = Scope.PRIVATE;
        Scope actual = ScopeUtils.getScopeFromMods(mods);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetScopeFromMods_NoModifiers() {
        DetailAST mods = new DetailAST();

        Scope expected = Scope.PACKAGE;
        Scope actual = ScopeUtils.getScopeFromMods(mods);

        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testGetScopeFromMods_InvalidModifier() {
        DetailAST mods = new DetailAST();
        DetailAST invalidToken = new DetailAST();
        invalidToken.setText("invalid");
        mods.appendChild(invalidToken);

        ScopeUtils.getScopeFromMods(mods);
    }

    @Test
    public void testGetSurroundingScope_InClass() {
        DetailAST node = new DetailAST();
        node.setType(15);

        Scope expected = Scope.CLASS;
        Scope actual = ScopeUtils.getSurroundingScope(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InInterface() {
        DetailAST node = new DetailAST();
        node.setType(15);
        DetailAST interfaceToken = new DetailAST();
        interfaceToken.setText("interface");
        node.appendChild(interfaceToken);

        Scope expected = Scope.INTERFACE;
        Scope actual = ScopeUtils.getSurroundingScope(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InAnnotation() {
        DetailAST node = new DetailAST();
        node.setType(157);

        Scope expected = Scope.ANNOTATION;
        Scope actual = ScopeUtils.getSurroundingScope(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InAnonymousClass() {
        DetailAST node = new DetailAST();
        node.setType(136);

        Scope expected = Scope.ANONINNER;
        Scope actual = ScopeUtils.getSurroundingScope(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSurroundingScope_InMethod() {
        DetailAST node = new DetailAST();
        node.setType(14);

        Scope expected = null;
        Scope actual = ScopeUtils.getSurroundingScope(node);

        assertNull(actual);
    }

    @Test
    public void testIsInInterfaceBlock_InInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.setType(15);

        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceBlock_NotInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.setType(14);

        boolean expected = false;
        boolean actual = ScopeUtils.isInInterfaceBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInAnnotationBlock_InAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(157);

        boolean expected = true;
        boolean actual = ScopeUtils.isInAnnotationBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInAnnotationBlock_NotInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(14);

        boolean expected = false;
        boolean actual = ScopeUtils.isInAnnotationBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_InInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.setType(15);

        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_InAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(157);

        boolean expected = true;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_NotInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(14);

        boolean expected = false;
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInEnumBlock_InEnumBlock() {
        DetailAST node = new DetailAST();
        node.setType(154);

        boolean expected = true;
        boolean actual = ScopeUtils.isInEnumBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInEnumBlock_NotInEnumBlock() {
        DetailAST node = new DetailAST();
        node.setType(15);

        boolean expected = false;
        boolean actual = ScopeUtils.isInEnumBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInCodeBlock_InCodeBlock() {
        DetailAST node = new DetailAST();
        node.setType(9);

        boolean expected = true;
        boolean actual = ScopeUtils.isInCodeBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInCodeBlock_NotInCodeBlock() {
        DetailAST node = new DetailAST();
        node.setType(15);

        boolean expected = false;
        boolean actual = ScopeUtils.isInCodeBlock(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsOuterMostType_OuterMostType() {
        DetailAST node = new DetailAST();
        node.setType(15);

        boolean expected = true;
        boolean actual = ScopeUtils.isOuterMostType(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsOuterMostType_NotOuterMostType() {
        DetailAST node = new DetailAST();
        node.setType(14);

        boolean expected = false;
        boolean actual = ScopeUtils.isOuterMostType(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsLocalVariableDef_LocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(10);

        boolean expected = true;
        boolean actual = ScopeUtils.isLocalVariableDef(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsLocalVariableDef_NotLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(15);

        boolean expected = false;
        boolean actual = ScopeUtils.isLocalVariableDef(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsClassFieldDef_ClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        node.setProperty(DetailAST.PROPERTY_IS_CLASS_FIELD, true);

        boolean expected = true;
        boolean actual = ScopeUtils.isClassFieldDef(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsClassFieldDef_NotClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10);

        boolean expected = false;
        boolean actual = ScopeUtils.isClassFieldDef(node);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInScope_InScope() {
        DetailAST ast = new DetailAST();
        DetailAST node = new DetailAST();
        node.setType(15);

        Scope scope = Scope.CLASS;

        boolean expected = true;
        boolean actual = ScopeUtils.isInScope(ast, scope);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsInScope_NotInScope() {
        DetailAST ast = new DetailAST();
        DetailAST node = new DetailAST();
        node.setType(15);

        Scope scope = Scope.INTERFACE;

        boolean expected = false;
        boolean actual = ScopeUtils.isInScope(ast, scope);

        assertEquals(expected, actual);
    }
}