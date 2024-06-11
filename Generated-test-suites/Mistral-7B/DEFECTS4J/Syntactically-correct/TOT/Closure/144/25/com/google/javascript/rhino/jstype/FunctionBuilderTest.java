import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

public class FunctionBuilderTest {

 private FunctionBuilder functionBuilder;
 private JSTypeRegistry registry;

 @Before
 public void setup() {
 registry = new JSTypeRegistry();
 functionBuilder = new FunctionBuilder(registry);
 }

 @Test
 public void testFunctionBuilderWithNameAndReturnType() {
 // Test case for FunctionBuilder withName and withReturnType
 functionBuilder.withName("addFn")
 .withReturnType(registry.createNumberType());
 FunctionType functionType = functionBuilder.build();
 assertEquals("addFn", functionType.getReferenceName());
 assertEquals(registry.createNumberType(), functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameParametersAndReturnType() {
 // Test case for FunctionBuilder withName, withParams, and withReturnType
 FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder(registry);
 FunctionParamType paramType1 = paramBuilder1.withName("num1").withType(registry.createNumberType()).build();
 FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder(registry);
 FunctionParamType paramType2 = paramBuilder2.withName("num2").withType(registry.createNumberType()).build();
 Node parametersNode = new CallNode(new IdentifierNode("Array"), "slice", new IntegerLiteralNode(0), new IntegerLiteralNode(2), new ArrayLiteralNode(paramType1.getType(), paramType2.getType()));
 functionBuilder.withName("addFn")
 .withParams(functionBuilder -> functionBuilder.withParamsNode(parametersNode))
 .withReturnType(registry.createNumberType());
 FunctionType functionType = functionBuilder.build();
 assertEquals("addFn", functionType.getReferenceName());
 assertEquals(new ArrowType(registry, parametersNode, registry.createNumberType(), false), functionType.getPrototype());
 assertEquals(registry.createNumberType(), functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithNameReturnTypeAndInferredReturnType() {
 // Test case for FunctionBuilder withName, withReturnType, and inferredReturnType
 functionBuilder.withName("addFn")
 .withReturnType(registry.createNumberType())
 .withInferredReturnType(registry.createStringType());
 FunctionType functionType = functionBuilder.build();
 assertEquals("addFn", functionType.getReferenceName());
 assertNotNull(functionType.getReturnType());
 assertFalse(functionType.getReturnType().isPrimitive());
 assertEquals(registry.createStringType(), functionType.getReturnType().getBoxedType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFunctionBuilderNullSourceNode() {
 functionBuilder.withName("addFn")
 .withSourceNode(null)
 .withReturnType(registry.createNumberType());
 }
}