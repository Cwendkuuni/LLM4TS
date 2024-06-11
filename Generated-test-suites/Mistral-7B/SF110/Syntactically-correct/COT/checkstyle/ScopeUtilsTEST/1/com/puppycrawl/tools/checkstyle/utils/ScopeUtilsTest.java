import org.junit.Test;
import org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import static com.puppycrawl.tools.checkstyle.utils.ScopeUtils.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        Token publicToken = new SimpleToken(TokenTypes.KEYWORD, "public");
        Token packageToken = new SimpleToken(TokenTypes.IDENTIFIER, "package");
        Token protectedToken = new SimpleToken(TokenTypes.KEYWORD, "protected");
        Token privateToken = new SimpleToken(TokenTypes.KEYWORD, "private");

        mods.addChild(packageToken);
        mods.addChild(publicToken);
        mods.addChild(protectedToken);
        mods.addChild(privateToken);

        DetailAST node = new DetailAST();
        node.setParent(mods);

        Scope expectedScope = Scope.PUBLIC;
        Scope actualScope = getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetScopeFromMods_NoPublicOrDefaultScope() {
        DetailAST mods = new DetailAST();
        Token protectedToken = new SimpleToken(TokenTypes.KEYWORD, "protected");
        Token privateToken = new SimpleToken(TokenTypes.KEYWORD, "private");

        mods.addChild(protectedToken);
        mods.addChild(privateToken);

        DetailAST node = new DetailAST();
        node.setParent(mods);

        Scope expectedScope = Scope.PROTECTED;
        Scope actualScope = getScopeFromMods(mods);
        assertEquals(expectedScope, actualScope);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetScopeFromMods_NullMods() {
        ScopeUtils.getScopeFromMods(null);
    }

    @Test
    public void testGetScopeFromMods_NoMods() {
        DetailAST node = new DetailAST();
        Scope expectedScope = Scope.PACKAGE;
        Scope actualScope = getScopeFromMods(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        node.setType(15);
        DetailAST parent = new DetailAST();
        parent.setType(14);
        parent.addChild(node);

        Scope expectedScope = Scope.CLASS;
        Scope actualScope = getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope_AnonymousInnerClass() {
        DetailAST node = new DetailAST();
        node.setType(136);
        DetailAST parent = new DetailAST();
        parent.setType(14);
        parent.addChild(node);

        Scope expectedScope = Scope.ANONINNER;
        Scope actualScope = getSurroundingScope(node);
        assertEquals(expectedScope, actualScope);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSurroundingScope_NullNode() {
        ScopeUtils.getSurroundingScope(null);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        parent.addChild(node);

        boolean expectedResult = true;
        boolean actualResult = isInInterfaceBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157);
        parent.addChild(node);

        boolean expectedResult = true;
        boolean actualResult = isInAnnotationBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        parent.addChild(node);

        boolean expectedResult = false;
        boolean actualResult = isInInterfaceOrAnnotationBlock(node);
        assertEquals(expectedResult, actualResult);

        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(157);
        parent2.addChild(node2);

        expectedResult = true;
        actualResult = isInInterfaceOrAnnotationBlock(node2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_NullNode() {
        boolean expectedResult = false;
        boolean actualResult = isInInterfaceOrAnnotationBlock(null);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInInterfaceBlock_NotInInterfaceBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14);
        parent.addChild(node);

        boolean expectedResult = false;
        boolean actualResult = isInInterfaceBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInAnnotationBlock_NotInAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154);
        parent.addChild(node);

        boolean expectedResult = false;
        boolean actualResult = isInAnnotationBlock(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsInInterfaceBlock_NullParent() {
        DetailAST node = new DetailAST();
        isInInterfaceBlock(node, null);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PACKAGE;

        DetailAST node = new DetailAST();
        node.setParent(ast);

        boolean expectedResult = true;
        boolean actualResult = isInScope(node, scope);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsInScope_NotInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PACKAGE;

        DetailAST node = new DetailAST();
        node.setParent(ast);

        Scope surroundingScope = Scope.CLASSES;
        boolean expectedResult = false;
        boolean actualResult = isInScope(node, surroundingScope);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        boolean expectedResult = true;
        boolean actualResult = isOuterMostType(node);
        assertEquals(expectedResult, actualResult);

        DetailAST parent = new DetailAST();
        DetailAST grandParent = new DetailAST();
        parent.setParent(grandParent);
        parent.setType(15);
        parent.addChild(node);

        expectedResult = false;
        actualResult = isOuterMostType(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsOuterMostType_NullNode() {
        ScopeUtils.isOuterMostType(null);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(10);

        boolean expectedResult = false;
        boolean actualResult = isLocalVariableDef(node);
        assertEquals(expectedResult, actualResult);

        DetailAST parent = new DetailAST();
        parent.setType(7);
        parent.addChild(node);

        expectedResult = true;
        actualResult = isLocalVariableDef(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10);

        boolean expectedResult = false;
        boolean actualResult = isClassFieldDef(node);
        assertEquals(expectedResult, actualResult);

        DetailAST parent = new DetailAST();
        parent.setType(156);
        parent.addChild(node);

        expectedResult = true;
        actualResult = isClassFieldDef(node);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsClassFieldDef_NullNode() {
        ScopeUtils.isClassFieldDef(null);
    }
}