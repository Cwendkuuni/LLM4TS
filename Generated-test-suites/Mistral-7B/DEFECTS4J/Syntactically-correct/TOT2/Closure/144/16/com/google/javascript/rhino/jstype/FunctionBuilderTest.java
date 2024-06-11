import static org.junit.Assert.assertNotNull;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class FunctionBuilderTest {
  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    Node sourceNode = new Node("function() {}");
    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder();
    Node parametersNode = functionParamBuilder.build();
    JSType returnType = new IntType();
    FunctionBuilder functionUnderTest = functionBuilder
        .withName("testFunction")
        .withSourceNode(sourceNode)
        .withParams(functionParamBuilder)
        .withReturnType(returnType)
        .build();

    FunctionType functionType = functionUnderTest.build();
    assertNotNull(functionType);
  }
}