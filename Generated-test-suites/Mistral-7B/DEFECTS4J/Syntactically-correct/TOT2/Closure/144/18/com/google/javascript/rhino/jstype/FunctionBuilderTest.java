import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {
  private FunctionBuilder functionBuilder;
  private JSTypeRegistry registry;
  private FunctionParamBuilder paramBuilder;
  private Node sourceNode;
  private Node paramsNode;
  private ObjectType typeOfThis;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    paramBuilder = mock(FunctionParamBuilder.class);
    sourceNode = mock(Node.class);
    paramsNode = mock(Node.class);
    typeOfThis = mock(ObjectType.class);
    functionBuilder = new FunctionBuilder(registry);
  }

  @Test
  public void testFunctionBuilderWithRegistry() {
    when(registry.createFunctionType(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(new FunctionType(registry, "testFunction", sourceNode, null, null, null, null, null, null, null, null, null, null));
    FunctionType functionType = functionBuilder.build();
    assertNotNull(functionType);
  }
}