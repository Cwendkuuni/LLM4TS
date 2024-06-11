import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

public class FunctionBuilderTest {
  @Test
  public void testFunctionBuilderWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    String expectedName = "testFunction";
    FunctionBuilder functionBuilder = builder.withName(expectedName).build();
    assertNotNull(functionBuilder);
    assertEquals(expectedName, functionBuilder.getName());
  }

  @Test
  public void testFunctionBuilderWithSourceNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    Node expectedSourceNode = new Node();
    FunctionBuilder functionBuilder = builder.withSourceNode(expectedSourceNode).build();
    assertNotNull(functionBuilder);
    assertEquals(expectedSourceNode, functionBuilder.getSourceNode());
  }

  // Add test cases for other methods with similar pattern
}