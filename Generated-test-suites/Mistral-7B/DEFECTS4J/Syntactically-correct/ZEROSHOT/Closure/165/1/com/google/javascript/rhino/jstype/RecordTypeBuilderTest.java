import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeOperators;
import com.google.javascript.rhino.JSTypeRegistry;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class RecordTypeBuilderTest {

  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    builder = new RecordTypeBuilder(registry);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testEmptyRecord() {
    JSType result = builder.build();
    assertNotNull(result);
    assertNotNull(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
  }

  @Test
  public void testNonEmptyRecord() {
    String propertyName = "testProperty";
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    when(registry.getUnknownType()).thenReturn(mock(JSType.class));
    when(propertyType.getName()).thenReturn("testPropertyType");

    builder.addProperty(propertyName, propertyType, propertyNode);

    JSType result = builder.build();
    assertNotNull(result);
    assertEquals(RecordType.class, result.getClass());

    Map<String, RecordTypeBuilder.RecordProperty> properties = ((RecordType) result)
        .getProperties();
    RecordTypeBuilder.RecordProperty actualProperty = properties.get(propertyName);
    assertNotNull(actualProperty);
    assertEquals(propertyName, actualProperty.getName());
    assertEquals(propertyType, actualProperty.getType());
    assertEquals(propertyNode, actualProperty.getPropertyNode());
  }

  @Test
  public void testAddDuplicateProperty() {
    String propertyName = "testProperty";
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    builder.addProperty(propertyName, propertyType, propertyNode);

    assertNull(builder.addProperty(propertyName, propertyType, propertyNode));
  }

  @Test
  public void testAddPropertyWithNullName() {
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    assertNull(builder.addProperty(null, propertyType, propertyNode));
  }

  @Test
  public void testAddPropertyWithNullType() {
    String propertyName = "testProperty";
    Node propertyNode = mock(Node.class);

    assertNull(builder.addProperty(propertyName, null, propertyNode));
  }

  @Test
  public void testAddPropertyWithNullNode() {
    String propertyName = "testProperty";
    JSType propertyType = mock(JSType.class);

    assertNull(builder.addProperty(propertyName, propertyType, null));
  }

  @Test
  public void testToString() {
    String propertyName1 = "property1";
    String propertyName2 = "property2";
    JSType propertyType1 = mock(JSType.class);
    JSType propertyType2 = mock(JSType.class);
    Node propertyNode1 = NodeOperators.newStringNode("property1");
    Node propertyNode2 = NodeOperators.newStringNode("property2");

    when(registry.getUnknownType()).thenReturn(mock(JSType.class));
    when(propertyType1.getName()).thenReturn("type1");
    when(propertyType2.getName()).thenReturn("type2");

    builder.addProperty(propertyName1, propertyType1, propertyNode1);
    builder.addProperty(propertyName2, propertyType2, propertyNode2);

    String expectedResult = String.format(
        "Record<%s: %s, %s: %s>", propertyName1, propertyType1.getName(),
        propertyName2, propertyType2.getName());

    assertEquals(expectedResult, builder.toString());
  }
}