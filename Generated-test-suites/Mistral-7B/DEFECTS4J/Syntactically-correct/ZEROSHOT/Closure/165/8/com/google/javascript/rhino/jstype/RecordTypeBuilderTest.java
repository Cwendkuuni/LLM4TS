import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddPropertyWithEmptyBuilder() {
    assertNull(recordTypeBuilder.build());
  }

  @Test
  public void testAddPropertyWithDuplicateName() {
    JSType firstType = mock(JSType.class);
    Node firstPropertyNode = mock(Node.class);

    recordTypeBuilder.addProperty("name1", firstType, firstPropertyNode);
    JSType secondType = mock(JSType.class);
    Node secondPropertyNode = mock(Node.class);

    assertNull(recordTypeBuilder.addProperty("name1", secondType, secondPropertyNode));
  }

  @Test
  public void testAddPropertyWithSuccess() {
    JSType type = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(type);

    RecordProperty recordProperty = recordTypeBuilder.addProperty("name", type, propertyNode);

    assertNotNull(recordProperty);
    assertNull(recordTypeBuilder.build());
  }

  @Test
  public void testBuildWithEmptyBuilder() {
    assertNotNull(recordTypeBuilder.build());
  }

  @Test
  public void testBuildWithProperties() {
    JSType type1 = mock(JSType.class);
    Node propertyNode1 = mock(Node.class);
    RecordProperty recordProperty1 = new RecordProperty(type1, propertyNode1);

    JSType type2 = mock(JSType.class);
    Node propertyNode2 = mock(Node.class);
    RecordProperty recordProperty2 = new RecordProperty(type2, propertyNode2);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(type1);

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name1", recordProperty1);
    properties.put("name2", recordProperty2);

    RecordType recordType = new RecordType(registry, properties);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(recordType);

    JSType result = recordTypeBuilder.build();

    assertNotNull(result);
  }
}