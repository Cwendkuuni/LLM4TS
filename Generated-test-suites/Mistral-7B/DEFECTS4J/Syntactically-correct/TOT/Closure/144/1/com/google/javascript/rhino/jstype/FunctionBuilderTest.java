import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

 @Test
 public void testFunctionBuilderWithBasicParameters() {
 JSTypeRegistry registry = new JSTypeRegistry();
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder("param1", registry);
 FunctionParamBuilder paramBuilder2 = new FunctionParamBuilder("param2", registry);
 Node parametersNode = functionBuilder.withParams(paramBuilder1, paramBuilder2).build();
 FunctionType functionType = functionBuilder.withName("testFunction")
 .withParams(parametersNode)
 .withReturnType(String.class)
 .build();
 assertEquals("testFunction", functionType.getReferenceName());
 assertNotNull(functionType.getParametersNode());
 assertNotNull(functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithInferredReturnType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 FunctionParamBuilder paramBuilder1 = new FunctionParamBuilder("param1", registry);
 Node parametersNode = functionBuilder.withParams(paramBuilder1).build();
 functionBuilder.withName("testFunction")
 .withParams(parametersNode)
 .withInferredReturnType(Number.class)
 .build();
 FunctionType functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 assertNotNull(functionType.getParametersNode());
 assertNotNull(functionType.getReturnType());
 assertEquals(Number.class, functionType.getReturnType());
 }

 @Test
 public void testFunctionBuilderWithTypeOfThis() {
 JSTypeRegistry registry = new JSTypeRegistry();
 ObjectType objectType = new ObjectType(registry, "ObjectType");
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction")
 .withTypeOfThis(objectType)
 .build();
 FunctionType functionType = functionBuilder.build();
 assertEquals("testFunction", functionType.getReferenceName());
 assertNull(functionType.getTypeOfThis());
 assertEquals(objectType, functionType.getTypeOfThis());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFunctionBuilderWithNullName() {
 JSTypeRegistry registry = new JSTypeRegistry();
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName(null);
 functionBuilder.build();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFunctionBuilderWithNullParametersNode() {
 JSTypeRegistry registry = new JSTypeRegistry();
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction")
 .withParamsNode(null);
 functionBuilder.build();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFunctionBuilderWithNullReturnType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 FunctionBuilder functionBuilder = new FunctionBuilder(registry);
 functionBuilder.withName("testFunction")
 .withParams(new NodeFactory().createList(new ObjectType(registry, "ObjectType")))
 .withReturnType(null);
 functionBuilder.build();
 }
}