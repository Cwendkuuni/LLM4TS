import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {
  private JSTypeRegistry registry;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));
    properties.put("b", new RecordProperty("b", registry.getStringType()));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals("{ a: Number, b: String }", recordType.getSourceName());
    assertEquals( registry.getRecordType(Map.of("a", registry.getNumberType(), "b", registry.getStringType())), recordType);
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getStringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNumberType()));
    properties2.put("b", new RecordProperty("b", registry.getStringType()));
    properties2.put("c", new RecordProperty("c", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));

    properties2.remove("c");

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getStringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("b", new RecordProperty("b", registry.getStringType()));
    properties2.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));

    properties2.put("c", new RecordProperty("c", registry.getBooleanType()));

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getStringType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("b", new RecordProperty("b", registry.getStringType()));
    properties2.put("c", new RecordProperty("c", registry.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    assertEquals(registry.getRecordType(Map.of("a", registry.getNumberType(), "b", registry.getStringType())), greatestSubtype);
  }

  @Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    Node propertyNode = Node.newIdent("propertyNode");

    recordType.defineProperty("a", registry.getNumberType(), false, propertyNode);

    assertNotNull(recordType.getPropertyNode("a"));
    assertEquals(propertyNode, recordType.getPropertyNode("a"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWhileFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    // Freeze the record type.
    recordType.setFrozen(true);

    Node propertyNode = Node.newIdent("propertyNode");

    recordType.defineProperty("a", registry.getNumberType(), false, propertyNode);
  }
}