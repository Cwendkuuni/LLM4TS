import static org.junit.Assert.*;
import static com.google.javascript.rhino.dts.Reflection.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.registry.JSTypeRegistry;
import com.google.javascript.rhino.test.BaseTestCase;

public class RecordTypeTest extends BaseTestCase {

  private static JSTypeRegistry registry;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", intType));
    properties.put("b", new RecordProperty("b", stringType));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("{a: int, b: string}", recordType.toString());
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", intType));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", intType));
    propertiesB.put("b", new RecordProperty("b", stringType));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsNotSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", intType));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", stringType));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", intType));
    propertiesA.put("b", new RecordProperty("b", stringType));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", intType));
    propertiesB.put("c", new RecordProperty("c", booleanType));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);
    assertEquals("{a: int}", greatestSubtype.toString());
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", intType));
    propertiesA.put("b", new RecordProperty("b", stringType));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", new RecordProperty("b", stringType));
    propertiesB.put("a", new RecordProperty("a", intType));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
  }

  @Test
  public void testRecordTypeWithInferredProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", intType, true));
    properties.put("b", new RecordProperty("b", stringType, true));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("{a: number, b: string}", recordType.toString());
  }

  @Test
  public void testIsFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", intType));

    RecordType recordType = new RecordType(registry, properties);
    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());
  }

  @Test(expected = IllegalStateException.class)
  public void testNullRecordProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", null);
    RecordType recordType = new RecordType(registry, properties);
  }
}