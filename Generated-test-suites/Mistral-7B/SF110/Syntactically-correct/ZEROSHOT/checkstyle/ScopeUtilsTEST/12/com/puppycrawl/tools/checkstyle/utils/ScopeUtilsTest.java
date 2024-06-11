import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.*;

public class ScopeUtilsTest {

 @Test
 public void testGetScopeFromMods() {
 DetailAST detailAST = createModsAST("public protected private");
 Scope expectedScope = Scope.PROTECTED;
 Scope actualScope = ScopeUtils.getScopeFromMods(detailAST);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_EmptyMods() {
 DetailAST detailAST = createModsAST("");
 Scope expectedScope = Scope.PACKAGE;
 Scope actualScope = ScopeUtils.getScopeFromMods(detailAST);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_NoMatchingModifiers() {
 DetailAST detailAST = createModsAST("static final");
 Scope expectedScope = Scope.PACKAGE;
 Scope actualScope = ScopeUtils.getScopeFromMods(detailAST);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_MultipleMatchingModifiers() {
 DetailAST detailAST = createModsAST("public protected private final");
 Scope expectedScope = Scope.PROTECTED;
 Scope actualScope = ScopeUtils.getScopeFromMods(detailAST);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_InClassBlock() {
 DetailAST detailAST = createClassBlockAST();
 Scope expectedScope = Scope.CLASS;
 Scope actualScope = ScopeUtils.getSurroundingScope(detailAST);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_InInterfaceBlock() {
 DetailAST detailAST = createInterfaceBlockAST();
 Scope expectedScope = Scope.INTERFACE;
 Scope actualScope = ScopeUtils.getSurroundingScope(detailAST);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_InAnnotationBlock() {
 DetailAST detailAST = createAnnotationBlockAST();
 Scope expectedScope = Scope.ANNOTATION;
 Scope actualScope = ScopeUtils.getSurroundingScope(detailAST);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_InEnumBlock() {
 DetailAST detailAST = createEnumBlockAST();
 Scope expectedScope = Scope.ENUM;
 Scope actualScope = ScopeUtils.getSurroundingScope(detailAST);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_InInnerClass() {
 DetailAST detailAST = createInnerClassAST();
 Scope expectedScope = Scope.ANONINNER;
 Scope actualScope = ScopeUtils.getSurroundingScope(detailAST);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_InEmptyAST() {
 DetailAST detailAST = createEmptyAST();
 Scope expectedScope = null;
 Scope actualScope = ScopeUtils.getSurroundingScope(detailAST);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testIsInInterfaceBlock_InInterfaceBlock() {
 DetailAST detailAST = createInterfaceBlockAST();
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceBlock(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceBlock_NotInInterfaceBlock() {
 DetailAST detailAST = createClassBlockAST();
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInInterfaceBlock(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_InAnnotationBlock() {
 DetailAST detailAST = createAnnotationBlockAST();
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInAnnotationBlock(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInAnnotationBlock_NotInAnnotationBlock() {
 DetailAST detailAST = createClassBlockAST();
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInAnnotationBlock(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_InInterfaceBlock() {
 DetailAST detailAST = createInterfaceBlockAST();
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_InAnnotationBlock() {
 DetailAST detailAST = createAnnotationBlockAST();
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_NotInInterfaceOrAnnotationBlock() {
 DetailAST detailAST = createClassBlockAST();
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInEnumBlock_InEnumBlock() {
 DetailAST detailAST = createEnumBlockAST();
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInEnumBlock(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInEnumBlock_NotInEnumBlock() {
 DetailAST detailAST = createClassBlockAST();
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInEnumBlock(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInCodeBlock_InCodeBlock() {
 DetailAST detailAST = createCodeBlockAST();
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInCodeBlock(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInCodeBlock_NotInCodeBlock() {
 DetailAST detailAST = createEmptyAST();
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInCodeBlock(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsOuterMostType_InOuterMostType() {
 DetailAST detailAST = createOuterMostTypeAST();
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isOuterMostType(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsOuterMostType_NotInOuterMostType() {
 DetailAST detailAST = createNestedTypeAST();
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isOuterMostType(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsLocalVariableDef_InLocalVariableDef() {
 DetailAST detailAST = createLocalVariableDefAST();
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isLocalVariableDef(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsLocalVariableDef_NotInLocalVariableDef() {
 DetailAST detailAST = createClassFieldDefAST();
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isLocalVariableDef(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsClassFieldDef_InClassFieldDef() {
 DetailAST detailAST = createClassFieldDefAST();
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isClassFieldDef(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsClassFieldDef_NotInClassFieldDef() {
 DetailAST detailAST = createLocalVariableDefAST();
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isClassFieldDef(detailAST);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInScope_InScope() {
 DetailAST detailAST = createLocalVariableDefAST();
 Scope expectedScope = Scope.LOCAL;
 DetailAST testAST = createClassFieldDefAST();
 boolean actualResult = ScopeUtils.isInScope(testAST, expectedScope);
 assertTrue(actualResult);
 }

 @Test
 public void testIsInScope_NotInScope() {
 DetailAST detailAST = createLocalVariableDefAST();
 Scope expectedScope = Scope.CLASS;
 DetailAST testAST = createClassBlockAST();
 boolean actualResult = ScopeUtils.isInScope(testAST, expectedScope);
 assertFalse(actualResult);
 }

 private DetailAST createModsAST(String modifiers) {
 DetailAST detailAST = new DetailAST();
 detailAST.setType(TokenTypes.MODIFIERS);
 DetailAST modifiersAST = new DetailAST();
 modifiersAST.setType(TokenTypes.MODIFIER);
 modifiersAST.setText(modifiers);
 detailAST.addChild(modifiersAST);
 return detailAST;
 }

 private DetailAST createClassBlockAST() {
 DetailAST detailAST = new DetailAST();
 detailAST.setType(TokenTypes.CLASS_DECL);
 return detailAST;
 }

 private DetailAST createInterfaceBlockAST() {
 DetailAST detailAST = new DetailAST();
 detailAST.setType(TokenTypes.INTERFACE_DECL);
 return detailAST;
 }

 private DetailAST createAnnotationBlockAST() {
 DetailAST detailAST = new DetailAST();
 detailAST.setType(TokenTypes.ANNOTATION);
 return detailAST;
 }

 private DetailAST createEnumBlockAST() {
 DetailAST detailAST = new DetailAST();
 detailAST.setType(TokenTypes.ENUM);
 return detailAST;
 }

 private DetailAST createInnerClassAST() {
 DetailAST detailAST = new DetailAST();
 detailAST.setType(TokenTypes.CLASS_DECL);
 DetailAST innerClassModifiers = new DetailAST();
 innerClassModifiers.setType(TokenTypes.MODIFIERS);
 DetailAST innerClassAnonymous = new DetailAST();
 innerClassAnonymous.setType(TokenTypes.ANON_CLASS);
 detailAST.addChild(innerClassModifiers);
 detailAST.addChild(innerClassAnonymous);
 return detailAST;
 }

 private DetailAST createEmptyAST() {
 DetailAST detailAST = new DetailAST();
 return detailAST;
 }

 private DetailAST createCodeBlockAST() {
 DetailAST detailAST = new DetailAST();
 detailAST.setType(TokenTypes.BLOCK);
 return detailAST;
 }

 private DetailAST createOuterMostTypeAST() {
 DetailAST detailAST = new DetailAST();
 detailAST.setType(TokenTypes.CLASS_DECL);
 detailAST.setName("OuterMostType");
 return detailAST;
 }

 private DetailAST createNestedTypeAST() {
 DetailAST detailAST = new DetailAST();
 detailAST.setType(TokenTypes.CLASS_DECL);
 detailAST.setName("NestedType");
 DetailAST outerMostType = new DetailAST();
 outerMostType.setType(TokenTypes.CLASS_DECL);
 outerMostType.setName("OuterMostType");
 detailAST.addChild(outerMostType);
 return detailAST;
 }

 private DetailAST createLocalVariableDefAST() {
 DetailAST detailAST = new DetailAST();
 detailAST.setType(TokenTypes.VAR_DECL);
 DetailAST variableName = new DetailAST();
 variableName.setType(TokenTypes.IDENT);
 variableName.setText("variableName");
 detailAST.addChild(variableName);
 DetailAST variableType = new DetailAST();
 variableType.setType(TokenTypes.TYPE);
 variableType.setText("int");
 detailAST.addChild(variableType);
 return detailAST;
 }

 private DetailAST createClassFieldDefAST() {
 DetailAST detailAST = new DetailAST();
 detailAST.setType(TokenTypes.FIELD_DECL);
 DetailAST fieldName = new DetailAST();
 fieldName.setType(TokenTypes.IDENT);
 fieldName.setText("fieldName");
 detailAST.addChild(fieldName);
 DetailAST fieldType = new DetailAST();
 fieldType.setType(TokenTypes.TYPE);
 fieldType.setText("int");
 detailAST.addChild(fieldType);
 return detailAST;
 }
}