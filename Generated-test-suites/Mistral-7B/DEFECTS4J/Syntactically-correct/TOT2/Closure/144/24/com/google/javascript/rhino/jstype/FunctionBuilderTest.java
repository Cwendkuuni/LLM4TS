import static org.junit.Assert.assertEquals;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {
  @Test
  public void testWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);
    String expectedName = "testFunction";
    FunctionBuilder functionBuilderWithName = functionBuilder.withName(expectedName);
    FunctionType functionType = functionBuilderWithName.build();
    assertEquals(expectedName, functionType.getReferenceName());
  }
}