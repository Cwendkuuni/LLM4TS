import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

public class ScopeUtilsTest {
    // Test for getScopeFromMods method
    @Test
    public void testGetScopeFromMods() {
        DetailAST astMods = createMockModifiersAST();
        Scope expectedScope = Scope.PUBLIC;
        Scope actualScope = ScopeUtils.getScopeFromMods(astMods);

        assertEquals(expectedScope, actualScope);
    }

    private DetailAST createMockModifiersAST() {
        // Implementation to create a mock DetailAST for the 'aMods' parameter
    }

    // Other test cases for getScopeFromMods method with different inputs

    // Test for getSurroundingScope method
    @Test
    public void testGetSurroundingScope() {
        DetailAST astNode = createMockNodeAST();
        Scope expectedScope = Scope.CLASS;
        Scope actualScope = ScopeUtils.getSurroundingScope(astNode);

        assertEquals(expectedScope, actualScope);
    }

    // Other test cases for getSurroundingScope method with different inputs

    // Test for isInInterfaceBlock method
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST astNode = createMockNodeAST();
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInInterfaceBlock(astNode);

        assertEquals(expectedResult, actualResult);
    }

    // Other test cases for isInInterfaceBlock method with different inputs

    // Test for isInAnnotationBlock method
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST astNode = createMockNodeAST();
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInAnnotationBlock(astNode);

        assertEquals(expectedResult, actualResult);
    }

    // Other test cases for isInAnnotationBlock method with different inputs

    // Test for isInInterfaceOrAnnotationBlock method
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST astNode = createMockNodeAST();
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(astNode);

        assertEquals(expectedResult, actualResult);
    }

    // Other test cases for isInInterfaceOrAnnotationBlock method with different inputs

    // Test for isInEnumBlock method
    @Test
    public void testIsInEnumBlock() {
        DetailAST astNode = createMockNodeAST();
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInEnumBlock(astNode);

        assertEquals(expectedResult, actualResult);
    }

    // Other test cases for isInEnumBlock method with different inputs

    // Test for isInCodeBlock method
    @Test
    public void testIsInCodeBlock() {
        DetailAST astNode = createMockNodeAST();
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isInCodeBlock(astNode);

        assertEquals(expectedResult, actualResult);
    }

    // Other test cases for isInCodeBlock method with different inputs

    // Test for isOuterMostType method
    @Test
    public void testIsOuterMostType() {
        DetailAST astNode = createMockNodeAST();
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isOuterMostType(astNode);

        assertEquals(expectedResult, actualResult);
    }

    // Other test cases for isOuterMostType method with different inputs

    // Test for isLocalVariableDef method
    @Test
    public void testIsLocalVariableDef() {
        DetailAST astNode = createMockNodeAST();
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isLocalVariableDef(astNode);

        assertEquals(expectedResult, actualResult);
    }

    // Other test cases for isLocalVariableDef method with different inputs

    // Test for isClassFieldDef method
    @Test
    public void testIsClassFieldDef() {
        DetailAST astNode = createMockNodeAST();
        boolean expectedResult = true;
        boolean actualResult = ScopeUtils.isClassFieldDef(astNode);

        assertEquals(expectedResult, actualResult);
    }

    // Other test cases for isClassFieldDef method with different inputs

    // Test for isInScope method
    @Test
    public void testIsInScope() {
        DetailAST astNode = createMockNodeAST();
        Scope expectedScope = Scope.PUBLIC;
        Scope actualScope = Scope.PUBLIC;

        assertTrue(ScopeUtils.isInScope(astNode, expectedScope));
    }

    // Test for isInScope method with different inputs and expected scope not equal to actual scope

    // 
}