import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.BaseTest;
import com.google.javascript.rhino.testing.TypeChecker;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest extends BaseTest {

  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();

    assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
  }

  @Test
  public void testBuildRecordWithOneProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType stringType = registry.getStringtype();
    Node propertyNode = newTextNode("propertyName");

    RecordProperty property = builder.addProperty("propertyName", stringType, propertyNode).build();
    JSType recordType = builder.build();

    RecordType expectedRecordType = new RecordType(registry, Collections.singletonMap("propertyName", property));
    assertEquals(expectedRecordType, recordType);
  }

  @Test
  public void testBuildRecordWithDuplicateProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType stringType = registry.getStringtype();
    Node propertyNode = newTextNode("propertyName");

    builder.addProperty("propertyName", stringType, propertyNode);
    assertNull(builder.addProperty("propertyName", stringType, propertyNode));

    try {
      builder.build();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testBuildRecordWithInvalidPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType numberType = registry.getNumberType();
    Node propertyNode = newTextNode("propertyName");

    try {
      builder.addProperty("propertyName", new JSType() {}, propertyNode);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      builder.build();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testCheckRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType stringType = registry.getStringtype();
    Node propertyNode = newTextNode("propertyName");

    RecordProperty property = builder.addProperty("propertyName", stringType, propertyNode).build();
    JSType recordType = builder.build();

    JSType checkedRecordType = TypeChecker.checkRecordType(recordType,
        Collections.singletonMap("propertyName", property));

    assertEquals(recordType, checkedRecordType);
  }

  @Test
  public void testCheckRecordTypeWithMissingProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType stringType = registry.getStringtype();
    Node propertyNode = newTextNode("propertyName");

    RecordProperty property = builder.addProperty("propertyName", stringType, propertyNode).build();
    JSType recordType = builder.build();

    Map<String, RecordProperty> expectedProperties = Collections.singletonMap("propertyName", property);
    JSType expectedRecordType = new RecordType(registry, expectedProperties);

    try {
      JSType checkedRecordType = TypeChecker.checkRecordType(recordType, Collections.emptyMap());
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testCheckRecordTypeWithExtraProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType stringType = registry.getStringtype();
    Node propertyNode1 = newTextNode("propertyName1");
    Node propertyNode2 = newTextNode("propertyName2");

    RecordProperty property1 = builder.addProperty("propertyName1", stringType, propertyNode1).build();
    builder.addProperty("propertyName2", stringType, propertyNode2);

    RecordType expectedRecordType = new RecordType(registry,
        Collections.singletonMap("propertyName1", property1));

    try {
      JSType checkedRecordType = TypeChecker.checkRecordType(expectedRecordType,
          Collections.singletonMap("propertyName2", property1));
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
}