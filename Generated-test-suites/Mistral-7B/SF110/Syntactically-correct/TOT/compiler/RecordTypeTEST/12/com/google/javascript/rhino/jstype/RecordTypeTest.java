import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

public class RecordTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeWithDeclaredProperties() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, properties, true);

    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    RecordType expectedRecordType = recordTypeBuilder.build();

    // Test record type equivalence
    assertThat(recordType).isEqualTo(expectedRecordType);

    // Test subtype relationship with ObjectType
    assertThat(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE))).isTrue();

    // Test subtype relationship with another RecordType
    RecordType anotherRecordType = new RecordType(registry, properties, false);
    RecordType expectedAnotherRecordType = new RecordType(registry, properties, false);
    assertThat(anotherRecordType.isSubtype(recordType)).isFalse();
    assertThat(recordType.isSubtype(anotherRecordType)).isTrue();

    // Test getGreatestSubtypeHelper method
    JSType greatestSubtype = recordType.getGreatestSubtype(anotherRecordType);
    JSType expectedGreatestSubtype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertThat(greatestSubtype).isEqualTo(expectedGreatestSubtype);

    // Test property access
    Node nameNode = new Node("name");
    JSType nameType = recordType.getPropertyType("name");
    Object name = recordType.getProperty(nameNode);
    assertThat(name).isNull();

    recordType = defineProperty(recordType, "name", String.class, nameNode);
    name = recordType.getProperty(nameNode);
    assertThat(name).isNull();

    String nameValue = "John Doe";
    Node nameValueNode = new Node(nameValue);
    recordType = defineProperty(recordType, "name", String.class, nameValueNode, nameValue);
    name = recordType.getProperty(nameNode);
    assertThat(name).isEqualTo(nameValue);

    // Test property type invariance
    RecordType recordTypeWithDifferentStringType = new RecordType(registry, properties, true);
    recordTypeWithDifferentStringType = defineProperty(recordTypeWithDifferentStringType, "name", Object.class, nameNode, nameValue);
    assertThat(recordType.isEquivalentTo(recordTypeWithDifferentStringType)).isFalse();

    // Test property access with non-existent property
    String nonExistentProperty = "nonExistentProperty";
    Node nonExistentPropertyNode = new Node(nonExistentProperty);
    Object nonExistentPropertyValue = recordType.getProperty(nonExistentPropertyNode);
    assertThat(nonExistentPropertyValue).isNull();
  }

  private RecordType defineProperty(RecordType recordType, String propertyName, JSType propertyType, Node propertyNode) {
    return defineProperty(defineProperty(recordType, propertyName, propertyType), propertyNode, propertyName);
  }

  private RecordType defineProperty(RecordType recordType, String propertyName, JSType propertyType, Node propertyNode, Object propertyValue) {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode, propertyValue);
    return recordTypeBuilder.build();
  }
}