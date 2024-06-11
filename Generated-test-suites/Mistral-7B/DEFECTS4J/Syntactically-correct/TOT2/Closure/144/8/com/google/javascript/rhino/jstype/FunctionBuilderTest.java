import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {

  FunctionBuilder functionBuilder;

  @Before
  public void setup() {
    JSTypeRegistry registry = new JSTypeRegistry();
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testWithName() {
    String name = "testFunction";
    functionBuilder.withName(name);
    FunctionType functionType = functionBuilder.build();
    assertEquals(name, functionType.getReferenceName());
  }

  // Add test cases for other methods here
}