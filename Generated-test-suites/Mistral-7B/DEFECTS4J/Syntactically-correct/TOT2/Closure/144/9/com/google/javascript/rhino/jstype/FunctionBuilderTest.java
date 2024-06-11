import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {

  @Test
  public void testWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String name = "testFunction";
    FunctionBuilder expectedFunctionBuilder = functionBuilder.withName(name);
    FunctionType expectedFunctionType = expectedFunctionBuilder.build();
    FunctionType actualFunctionType = functionBuilder.build();
    assertEquals(name, expectedFunctionType.getReferenceName());
    assertEquals(actualFunctionType.getReferenceName(), expectedFunctionType.getReferenceName());
  }
}