import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class FunctionBuilderTest {

  @Test
  public void testBuildFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "testFunction";
    Node functionSource = Node.newLiteralNode("function testFunction(param1, param2) { return 1; }");
    FunctionParamBuilder functionParams = new FunctionParamBuilder();
    functionParams.withName("param1").withType(new IntType(registry));
    functionParams.withName("param2").withType(new IntType(registry));

    JSType returnType = new IntType(registry);
    FunctionBuilder function = functionBuilder
        .withName(functionName)
        .withSourceNode(functionSource)
        .withParams(functionParams)
        .withReturnType(returnType)
        .build();

    FunctionType expectedFunctionType = new FunctionType(registry, functionName, functionSource,
        new ArrowType(registry, functionParams.build(), returnType, false), null, null, false, false);

    FunctionType actualFunctionType = function.build();

    assertEquals(expectedFunctionType.getReferenceName(), actualFunctionType.getReferenceName());
    assertEquals(expectedFunctionType.getSource(), actualFunctionType.getSource());
    assertEquals(expectedFunctionType.getParametersNode(), actualFunctionType.getParametersNode());
    assertEquals(expectedFunctionType.getReturnType(), actualFunctionType.getReturnType());
    assertEquals(expectedFunctionType.getTypeOfThis(), actualFunctionType.getTypeOfThis());
    assertEquals(expectedFunctionType.getTemplateTypeName(), actualFunctionType.getTemplateTypeName());
    assertEquals(expectedFunctionType.isConstructor(), actualFunctionType.isConstructor());
    assertEquals(expectedFunctionType.isNativeType(), actualFunctionType.isNativeType());
  }
}