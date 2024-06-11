import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class FunctionBuilderTest {
  @Test
  public void testBuildFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String name = "testFunction";
    Node sourceNode = null; // You can create a dummy Node instance if needed
    FunctionParamBuilder paramsBuilder = new FunctionParamBuilder();
    paramsBuilder.withName("param1").withType(new IntType());
    Node parametersNode = paramsBuilder.build();
    JSType returnType = new VoidType();

    FunctionBuilder function = functionBuilder
        .withName(name)
        .withSourceNode(sourceNode)
        .withParams(paramsBuilder)
        .withReturnType(returnType)
        .build();

    FunctionType expectedFunctionType = new FunctionType(registry, name, sourceNode,
        new ArrowType(registry, parametersNode, returnType, false),
        null, null, false, false);

    FunctionType actualFunctionType = function.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }
}