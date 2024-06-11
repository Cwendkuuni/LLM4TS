import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.error.ErrorReporter;
import com.google.javascript.rhino.types.StaticScope;
import static org.junit.Assert.*;

public class ArrowTypeTest {

 @Test
 public void testArrowTypeCreation() {
 ArrowType arrowType = new ArrowType(new JSTypeRegistry(), null, null);
 ArrowType arrowTypeWithParams = new ArrowType(new JSTypeRegistry(), Node.newList(), getNativeType(Integer.TYPE));
 ArrowType arrowTypeWithInferredReturnType = new ArrowType(new JSTypeRegistry(), Node.newList(), null, true);
 ArrowType arrowTypeWithParamsAndInferredReturnType = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x")), Node.newName("void"), true);

 assertEquals("ArrowType with no parameters or return type", arrowType.getClass(), arrowType.getClass());
 assertEquals("ArrowType with parameters and a defined return type", arrowTypeWithParams.getClass(), ArrowType.class);
 assertEquals("ArrowType with inferred return type", arrowTypeWithInferredReturnType.getClass(), ArrowType.class);
 assertEquals("ArrowType with parameters, inferred return type, and a single parameter named 'x'", arrowTypeWithParamsAndInferredReturnType.getClass(), ArrowType.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeCreationWithInvalidParamTypes() {
 ArrowType arrowTypeWithInvalidParamType = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.newList(Node.newName("string")), false);
 }

 @Test
 public void testArrowTypeIsSubtype() {
 ArrowType arrowTypeWithParams = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getNumberType(), false);
 ArrowType arrowTypeWithParamsAndReturnType = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getBooleanType(), false);
 ArrowType arrowTypeWithSameParamsAndReturnType = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getNumberType(), false);
 ArrowType arrowTypeWithDifferentParams = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getNumberType(), false);
 ArrowType arrowTypeWithDifferentReturnType = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getBooleanType(), false);
 ArrowType arrowTypeWithVarArgs = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.newList(Node.newName("x"), Node.newName("y")), false);
 ArrowType arrowTypeWithVarArgsReturnType = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getArrayType(Node.getNullType(), Node.INT_INFINITY), false);
 ArrowType arrowTypeWithInferredReturnType = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), null, true);
 ArrowType arrowTypeWithNoParamsOrReturnType = new ArrowType(new JSTypeRegistry(), null, null);

 assertTrue("ArrowType with defined parameters and defined return type is a subtype of ArrowType with the same parameters and defined return type", arrowTypeWithParamsIsSubtype(arrowTypeWithParamsAndReturnType, arrowTypeWithParams));
 assertTrue("ArrowType with defined parameters and defined return type is not a subtype of ArrowType with different parameters", !arrowTypeWithParamsIsSubtype(arrowTypeWithParams, arrowTypeWithDifferentParams));
 assertTrue("ArrowType with defined parameters and defined return type is not a subtype of ArrowType with different return type", !arrowTypeWithParamsIsSubtype(arrowTypeWithParams, arrowTypeWithDifferentReturnType));
 assertTrue("ArrowType with defined parameters and defined return type is not a subtype of ArrowType with var-args", !arrowTypeWithParamsIsSubtype(arrowTypeWithParams, arrowTypeWithVarArgs));
 assertTrue("ArrowType with defined parameters and inferred return type is a subtype of ArrowType with the same defined parameters and defined return type", arrowTypeWithParamsIsSubtype(arrowTypeWithInferredReturnType, arrowTypeWithParams));
 assertTrue("ArrowType with defined parameters and inferred return type is not a subtype of ArrowType with different parameters", !arrowTypeWithParamsIsSubtype(arrowTypeWithInferredReturnType, arrowTypeWithDifferentParams));
 assertTrue("ArrowType with defined parameters and inferred return type is not a subtype of ArrowType with different return type", !arrowTypeWithParamsIsSubtype(arrowTypeWithInferredReturnType, arrowTypeWithDifferentReturnType));
 assertTrue("ArrowType with defined parameters and inferred return type is a subtype of ArrowType with no parameters or return type", arrowTypeWithParamsIsSubtype(arrowTypeWithNoParamsOrReturnType, arrowTypeWithInferredReturnType));
 assertTrue("ArrowType with var-args for both parameters and return type is a subtype of ArrowType with var-args for parameters and a defined return type", arrowTypeWithVarArgsIsSubtype(arrowTypeWithVarArgsReturnType, arrowTypeWithParamsAndInferredReturnType));
 assertTrue("ArrowType with var-args for both parameters and return type is a subtype of ArrowType with no parameters or return type", arrowTypeWithVarArgsIsSubtype(arrowTypeWithVarArgsReturnType, arrowTypeWithNoParamsOrReturnType));
 }

 private boolean arrowTypeWithParamsIsSubtype(ArrowType arrowTypeWithParams, ArrowType arrowTypeToTest) {
 return arrowTypeWithParams.isSubtype(arrowTypeToTest);
 }

 private boolean arrowTypeWithVarArgsIsSubtype(ArrowType arrowTypeWithVarArgs, ArrowType arrowTypeToTest) {
 ArrowType arrowTypeWithParams = (ArrowType) arrowTypeWithVarArgs.getParameters().getFirstChild().getJSType();
 return arrowTypeWithParamsIsSubtype(arrowTypeWithParams, arrowTypeToTest);
 }

 @Test
 public void testHasEqualParameters() {
 ArrowType arrowTypeWithParams1 = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getNumberType(), false);
 ArrowType arrowTypeWithParams2 = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getNumberType(), false);
 ArrowType arrowTypeWithParams3 = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getBooleanType(), false);
 ArrowType arrowTypeWithVarArgs1 = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getArrayType(Node.getNullType(), Node.INT_INFINITY), false);
 ArrowType arrowTypeWithVarArgs2 = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getArrayType(Node.getNullType(), Node.INT_INFINITY), false);
 ArrowType arrowTypeWithDifferentParams1 = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y"), Node.newName("z")), Node.getNumberType(), false);
 ArrowType arrowTypeWithDifferentParams2 = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y"), Node.newName("z")), Node.getBooleanType(), false);

 assertTrue("ArrowType with equal parameters is considered equal", arrowTypeWithParams1.hasEqualParameters(arrowTypeWithParams2));
 assertFalse("ArrowType with different parameters is not considered equal", arrowTypeWithParams1.hasEqualParameters(arrowTypeWithParams3));
 assertFalse("ArrowType with same parameters but different types is not considered equal", arrowTypeWithParams1.hasEqualParameters(arrowTypeWithVarArgs1));
 assertFalse("ArrowType with different parameters is not considered equal", arrowTypeWithParams1.hasEqualParameters(arrowTypeWithDifferentParams1));
 assertFalse("ArrowType with same parameters but different return types is not considered equal", arrowTypeWithParams1.hasEqualParameters(arrowTypeWithDifferentParams2));
 }

 @Test
 public void testToStringHelper() {
 ArrowType arrowTypeWithParams = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getNumberType(), false);
 ArrowType arrowTypeWithInferredReturnType = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), null, true);
 String arrowTypeWithParamsToString = arrowTypeWithParams.toStringHelper(false).toString();
 String arrowTypeWithInferredReturnTypeToString = arrowTypeWithInferredReturnType.toStringHelper(false).toString();

 assertTrue("ArrowType with defined parameters and defined return type has the correct toString representation", arrowTypeWithParamsToString.contains("ArrowType[Number]"));
 assertTrue("ArrowType with inferred return type has the correct toString representation", arrowTypeWithInferredReturnTypeToString.contains("ArrowType[unknown]"));
 }

 @Test
 public void testResolveInternal() {
 ArrowType arrowTypeWithParamsAndUnknownReturnType = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), null, false);
 ErrorReporter errorReporter = new ErrorReporter();
 StaticScope<JSType> scope = new StaticScope<>();

 arrowTypeWithParamsAndUnknownReturnType.resolveInternal(errorReporter, scope);

 assertTrue("ArrowType with unknown return type has a resolved return type of 'unknown'", arrowTypeWithParamsAndUnknownReturnType.returnType.isUnknownType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLeastSupertype() {
 ArrowType arrowTypeWithParams = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getNumberType(), false);
 arrowTypeWithParams.getLeastSupertype(null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetGreatestSubtype() {
 ArrowType arrowTypeWithParams = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getNumberType(), false);
 arrowTypeWithParams.getGreatestSubtype(null);
 }

 @Test
 public void testGetPossibleToBooleanOutcomes() {
 ArrowType arrowTypeWithParams = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getBooleanType(), false);
 BooleanLiteralSet booleanLiteralSet = arrowTypeWithParams.getPossibleToBooleanOutcomes();

 assertEquals("ArrowType with defined return type of Boolean has exactly one possible Boolean outcome: true", BooleanLiteralSet.TRUE, booleanLiteralSet);
 }

 @Test
 public void testVisit() {
 ArrowType arrowTypeWithParams = new ArrowType(new JSTypeRegistry(), Node.newList(Node.newName("x"), Node.newName("y")), Node.getNumberType(), false);
 Visitor<Object> visitor = new Visitor<Object>() {
 @Override
 public Object visitArrowType(ArrowType arrowType, StaticScope<JSType> scope) {
 return arrowType;
 }
 };
 Object object = arrowTypeWithParams.visit(visitor);

 assertEquals("ArrowType is not modified by a visit", arrowTypeWithParams, object);
 }
}