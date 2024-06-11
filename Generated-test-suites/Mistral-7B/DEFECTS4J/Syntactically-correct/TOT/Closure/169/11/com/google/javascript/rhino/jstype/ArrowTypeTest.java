import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.error.ErrorReporter;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.NoType;
import com.google.javascript.rhino.jstype.NullType;
import com.google.javascript.rhino.jstype.NumberType;
import com.google.javascript.rhino.jstype.UnknownType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 errorReporter = new ErrorReporter();
 }

 @After
 public void tearDown() {
 registry.dispose();
 errorReporter.dispose();
 }

 @Test
 public void testEmptyArrowType() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 assertEquals("ArrowType[,]", arrowType.toStringHelper(true));
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testArrowTypeWithOneParameterAndReturnType() {
 Node parameters = registry.createNode("int");
 ArrowType arrowType = new ArrowType(registry, parameters, new NumberType(registry, "int"), false);
 assertEquals("ArrowType[int, int]", arrowType.toStringHelper(true));
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testArrowTypeWithVarArgsParameterAndReturnType() {
 Node parameters = registry.createNode("int");
 parameters.setVarArgs(true);
 ArrowType arrowType = new ArrowType(registry, parameters, new NumberType(registry, "int"), false);
 assertEquals("ArrowType[int, ...]", arrowType.toStringHelper(true));
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testArrowTypeWithOptionalParameterAndReturnType() {
 Node parameters = registry.createNode("int?");
 ArrowType arrowType = new ArrowType(registry, parameters, new NumberType(registry, "int"), false);
 assertEquals("ArrowType[int?, int]", arrowType.toStringHelper(true));
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testArrowTypeWithParameterAndInferredReturnType() {
 Node parameters = registry.createNode("int");
 ArrowType arrowType = new ArrowType(registry, parameters, null, true);
 assertEquals("ArrowType[int, ?]", arrowType.toStringHelper(true));
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testArrowTypeWithParameterAndUnknownReturnType() {
 Node parameters = registry.createNode("int");
 ArrowType arrowType = new ArrowType(registry, parameters, null, false);
 assertEquals("ArrowType[int, ?]", arrowType.toStringHelper(true));
 assertTrue(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testArrowTypeWithParametersAndKnownReturnType() {
 Node parameters = registry.createNode("int");
 Node returnTypeParameters = registry.createNode("int");
 ArrowType arrowType = new ArrowType(registry, parameters, new NumberType(registry, "int"), false);
 assertEquals("ArrowType[int, int]", arrowType.toStringHelper(true));
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testArrowTypeWithVarArgsAndKnownReturnType() {
 Node parameters = registry.createNode("int");
 parameters.setVarArgs(true);
 ArrowType arrowType = new ArrowType(registry, parameters, new NumberType(registry, "int"), false);
 assertEquals("ArrowType[int, ...]", arrowType.toStringHelper(true));
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testArrowTypeWithOptionalParameterAndKnownReturnType() {
 Node parameters = registry.createNode("int?");
 ArrowType arrowType = new ArrowType(registry, parameters, new NumberType(registry, "int"), false);
 assertEquals("ArrowType[int?, int]", arrowType.toStringHelper(true));
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testArrowTypeWithParametersAndInferredReturnType() {
 Node parameters = registry.createNode("int");
 ArrowType arrowType = new ArrowType(registry, parameters, null, true);
 assertEquals("ArrowType[int, ?]", arrowType.toStringHelper(true));
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }

 @Test
 public void testArrowTypeWithParametersAndNoTypeReturnType() {
 Node parameters = registry.createNode("int");
 ArrowType arrowType = new ArrowType(registry, parameters, NoType.NO_TYPE, false);
 assertEquals("ArrowType[int, NoType]", arrowType.toStringHelper(true));
 assertFalse(arrowType.hasUnknownParamsOrReturn());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLeastSupertype() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 arrowType.getLeastSupertype(registry.createNullType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetGreatestSubtype() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 arrowType.getGreatestSubtype(registry.createNullType());
 }

 @Test
 public void testCheckEquivalence() {
 ArrowType arrowType1 = new ArrowType(registry, null, null);
 ArrowType arrowType2 = new ArrowType(registry, null, null);
 assertTrue(arrowType1.checkEquivalenceHelper(arrowType2, true));
 ArrowType arrowType3 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 ArrowType arrowType4 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 assertTrue(arrowType3.checkEquivalenceHelper(arrowType4, true));
 ArrowType arrowType5 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType6 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 assertTrue(arrowType5.checkEquivalenceHelper(arrowType6, true));
 ArrowType arrowType7 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType8 = new ArrowType(registry, null, new StringType(registry), false);
 assertFalse(arrowType7.checkEquivalenceHelper(arrowType8, true));
 ArrowType arrowType9 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType10 = new ArrowType(registry, null, new FunctionType(registry, new JSType[0], new JSType[0]), false);
 assertFalse(arrowType9.checkEquivalenceHelper(arrowType10, true));
 }

 @Test
 public void testHasEqualParameters() {
 ArrowType arrowType1 = new ArrowType(registry, null, null);
 ArrowType arrowType2 = new ArrowType(registry, null, null);
 assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
 ArrowType arrowType3 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 ArrowType arrowType4 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 assertTrue(arrowType3.hasEqualParameters(arrowType4, true));
 ArrowType arrowType5 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 ArrowType arrowType6 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 ArrowType arrowType7 = new ArrowType(registry, null, new NumberType(registry, "int?"), null);
 ArrowType arrowType8 = new ArrowType(registry, null, new NumberType(registry, "int?"), null);
 assertTrue(arrowType5.hasEqualParameters(arrowType6, true));
 assertFalse(arrowType5.hasEqualParameters(arrowType7, true));
 }

 @Test
 public void testCheckArrowEquivalence() {
 ArrowType arrowType1 = new ArrowType(registry, null, null);
 ArrowType arrowType2 = new ArrowType(registry, null, null);
 assertEquals(arrowType1, arrowType2.checkArrowEquivalenceHelper(arrowType2, true));
 ArrowType arrowType3 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 ArrowType arrowType4 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 assertEquals(arrowType3, arrowType4.checkArrowEquivalenceHelper(arrowType4, true));
 ArrowType arrowType5 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType6 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 assertEquals(arrowType5, arrowType6.checkArrowEquivalenceHelper(arrowType6, true));
 ArrowType arrowType7 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType8 = new ArrowType(registry, null, new StringType(registry), false);
 assertNull(arrowType7.checkArrowEquivalenceHelper(arrowType8, true));
 ArrowType arrowType9 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType10 = new ArrowType(registry, null, new FunctionType(registry, new JSType[0], new JSType[0]), false);
 assertNull(arrowType9.checkArrowEquivalenceHelper(arrowType10, true));
 }

 @Test
 public void testEquality() {
 ArrowType arrowType1 = new ArrowType(registry, null, null);
 ArrowType arrowType2 = new ArrowType(registry, null, null);
 assertTrue(arrowType1.equals(arrowType2));
 ArrowType arrowType3 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 ArrowType arrowType4 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 assertTrue(arrowType3.equals(arrowType4));
 ArrowType arrowType5 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType6 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 assertTrue(arrowType5.equals(arrowType6));
 ArrowType arrowType7 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType8 = new ArrowType(registry, null, new StringType(registry), false);
 assertFalse(arrowType7.equals(arrowType8));
 ArrowType arrowType9 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType10 = new ArrowType(registry, null, new FunctionType(registry, new JSType[0], new JSType[0]), false);
 assertFalse(arrowType9.equals(arrowType10));
 }

 @Test
 public void testResolvableArrowType() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 arrowType.resolveInternal(errorReporter, registry.createNoCurrentScope());
 assertEquals(UnknownType.UNKNOWN_TYPE, arrowType.returnType);
 ArrowType arrowType2 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 arrowType2.resolveInternal(errorReporter, registry.createNoCurrentScope());
 assertEquals(NumberType.INT_TYPE, arrowType2.returnType);
 }

 @Test
 public void testIsSubtype() {
 ArrowType arrowType1 = new ArrowType(registry, null, null);
 ArrowType arrowType2 = new ArrowType(registry, null, null);
 assertTrue(arrowType1.isSubtype(arrowType2));
 ArrowType arrowType3 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 ArrowType arrowType4 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 assertTrue(arrowType3.isSubtype(arrowType4));
 ArrowType arrowType5 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType6 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 assertTrue(arrowType5.isSubtype(arrowType6));
 ArrowType arrowType7 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType8 = new ArrowType(registry, null, new StringType(registry), false);
 assertFalse(arrowType7.isSubtype(arrowType8));
 ArrowType arrowType9 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType10 = new ArrowType(registry, null, new FunctionType(registry, new JSType[0], new JSType[0]), false);
 assertFalse(arrowType9.isSubtype(arrowType10));
 ArrowType arrowType11 = new ArrowType(registry, null, null, true);
 ArrowType arrowType12 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 assertTrue(arrowType11.isSubtype(arrowType12));
 ArrowType arrowType13 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType14 = new ArrowType(registry, null, new FunctionType(registry, new JSType[0], new JSType[0]), false);
 assertFalse(arrowType13.isSubtype(arrowType14));
 }

 @Test
 public void testHashCode() {
 ArrowType arrowType1 = new ArrowType(registry, null, null);
 ArrowType arrowType2 = new ArrowType(registry, null, null);
 assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
 ArrowType arrowType3 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 ArrowType arrowType4 = new ArrowType(registry, null, new NumberType(registry, "int"), null);
 assertEquals(arrowType3.hashCode(), arrowType4.hashCode());
 ArrowType arrowType5 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType6 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 assertEquals(arrowType5.hashCode(), arrowType6.hashCode());
 ArrowType arrowType7 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType8 = new ArrowType(registry, null, new StringType(registry), false);
 assertNotEquals(arrowType7.hashCode(), arrowType8.hashCode());
 ArrowType arrowType9 = new ArrowType(registry, null, new NumberType(registry, "int"), false);
 ArrowType arrowType10 = new ArrowType(registry, null, new FunctionType(registry, new JSType[0], new JSType[0]), false);
 assertNotEquals(arrowType9.hashCode(), arrowType10.hashCode());
 }
}