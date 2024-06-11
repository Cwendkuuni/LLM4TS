import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.*;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.jstype.RecordType.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", INT));
    properties.put("b", new RecordProperty("b", STRING));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry, recordType.getRegistry());
    assertEquals(Maps.newTreeMap(properties), recordType.getProperties());
    assertEquals("RecordType", recordType.getClassName());
  }

  @Test
  public void testRecordTypeIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", INT));
    propertiesA.put("b", new RecordProperty("b", STRING));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", INT));
    propertiesB.put("b", new RecordProperty("b", STRING));
    propertiesB.put("c", new RecordProperty("c", BOOLEAN));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", INT));
    propertiesC.put("b", new RecordProperty("b", STRING));
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    Map<String, RecordProperty> propertiesD = Maps.newHashMap();
    propertiesD.put("b", new RecordProperty("b", STRING));
    propertiesD.put("a", new RecordProperty("a", INT));
    RecordType recordTypeD = new RecordType(registry, propertiesD);

    Map<String, RecordProperty> propertiesE = Maps.newHashMap();
    propertiesE.put("c", new RecordProperty("c", BOOLEAN));
    propertiesE.put("a", new RecordProperty("a", INT));
    RecordType recordTypeE = new RecordType(registry, propertiesE);

    assertEquals(recordTypeA, recordTypeA);
    assertEquals(recordTypeA, recordTypeA.toMaybeRecordType());

    assertEquals(recordTypeA, recordTypeB.toMaybeRecordType());
    assertEquals(recordTypeB, recordTypeB.toMaybeRecordType());
    assertFalse(recordTypeA.equals(recordTypeB));

    assertEquals(recordTypeA, recordTypeC.toMaybeRecordType());
    assertFalse(recordTypeA.equals(recordTypeC));

    assertEquals(recordTypeA, recordTypeD.toMaybeRecordType());
    assertEquals(recordTypeD, recordTypeD.toMaybeRecordType());
    assertFalse(recordTypeA.equals(recordTypeD));

    assertFalse(recordTypeA.equals(recordTypeE));
    assertFalse(recordTypeE.equals(recordTypeA));

    assertTrue(recordTypeA.isEquivalentTo(recordTypeC));
    assertTrue(recordTypeC.isEquivalentTo(recordTypeA));

    assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
    assertFalse(recordTypeB.isEquivalentTo(recordTypeA));

    assertFalse(recordTypeA.isEquivalentTo(recordTypeD));
    assertFalse(recordTypeD.isEquivalentTo(recordTypeA));

    assertFalse(recordTypeA.isEquivalentTo(recordTypeE));
    assertFalse(recordTypeE.isEquivalentTo(recordTypeA));

    assertFalse(recordTypeB.isEquivalentTo(recordTypeC));
    assertFalse(recordTypeC.isEquivalentTo(recordTypeB));

    assertFalse(recordTypeB.isEquivalentTo(recordTypeD));
    assertFalse(recordTypeD.isEquivalentTo(recordTypeB));

    assertFalse(recordTypeB.isEquivalentTo(recordTypeE));
    assertFalse(recordTypeE.isEquivalentTo(recordTypeB));
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", INT));
    propertiesA.put("b", new RecordProperty("b", STRING));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", new RecordProperty("b", STRING));
    propertiesB.put("a", new RecordProperty("a", INT));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("c", new RecordProperty("c", BOOLEAN));
    propertiesC.put("a", new RecordProperty("a", INT));
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    Map<String, RecordProperty> propertiesD = Maps.newHashMap();
    propertiesD.put("a", new RecordProperty("a", INT));
    propertiesD.put("c", new RecordProperty("c", BOOLEAN));
    RecordType recordTypeD = new RecordType(registry, propertiesD);

    Map<String, RecordProperty> propertiesE = Maps.newHashMap();
    propertiesE.put("a", new RecordProperty("a", INT));
    RecordType recordTypeE = new RecordType(registry, propertiesE);

    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeC.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(recordTypeD));
    assertFalse(recordTypeD.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeE));
    assertTrue(recordTypeE.isSubtype(recordTypeA));
    assertTrue(recordTypeB.isSubtype(recordTypeE));
    assertTrue(recordTypeE.isSubtype(recordTypeB));
    assertFalse(recordTypeC.isSubtype(recordTypeE));
    assertFalse(recordTypeE.isSubtype(recordTypeC));
    assertFalse(recordTypeD.isSubtype(recordTypeE));
    assertFalse(recordTypeE.isSubtype(recordTypeD));
  }

  @Test
  public void testRecordTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", INT));
    propertiesA.put("b", new RecordProperty("b", STRING));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", INT));
    propertiesB.put("b", new RecordProperty("b", STRING));
    propertiesB.put("c", new RecordProperty("c", BOOLEAN));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", INT));
    propertiesC.put("d", new RecordProperty("d", INT));
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    Map<String, RecordProperty> propertiesD = Maps.newHashMap();
    propertiesD.put("b", new RecordProperty("b", STRING));
    propertiesD.put("c", new RecordProperty("c", BOOLEAN));
    RecordType recordTypeD = new RecordType(registry, propertiesD);

    Map<String, RecordProperty> propertiesE = Maps.newHashMap();
    propertiesE.put("a", new RecordProperty("a", INT));
    propertiesE.put("b", new RecordProperty("b", STRING));
    propertiesE.put("d", new RecordProperty("d", INT));
    RecordType recordTypeE = new RecordType(registry, propertiesE);

    assertEquals(recordTypeB, recordTypeA.getLeastSupertype(recordTypeB));
    assertEquals(recordTypeA, recordTypeB.getLeastSupertype(recordTypeA));
    assertEquals(recordTypeB, recordTypeA.getLeastSupertype(recordTypeC));
    assertNotSame(recordTypeA, recordTypeA.getLeastSupertype(recordTypeA));
    assertEquals(recordTypeE, recordTypeA.getLeastSupertype(recordTypeE));
    assertEquals(recordTypeC, recordTypeA.getLeastSupertype(recordTypeC));
    assertEquals(recordTypeD, recordTypeB.getLeastSupertype(recordTypeD));
    assertEquals(recordTypeE, recordTypeB.getLeastSupertype(recordTypeE));
    assertEquals(recordTypeB, recordTypeC.getLeastSupertype(recordTypeB));
    assertEquals(recordTypeE, recordTypeC.getLeastSupertype(recordTypeE));
    assertEquals(recordTypeD, recordTypeD.getLeastSupertype(recordTypeD));
    assertEquals(recordTypeE, recordTypeD.getLeastSupertype(recordTypeE));
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", INT));
    propertiesA.put("b", new RecordProperty("b", STRING));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", INT));
    propertiesB.put("b", new RecordProperty("b", STRING));
    propertiesB.put("c", new RecordProperty("c", BOOLEAN));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", INT));
    propertiesC.put("d", new RecordProperty("d", INT));
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    Map<String, RecordProperty> propertiesD = Maps.newHashMap();
    propertiesD.put("b", new RecordProperty("b", STRING));
    propertiesD.put("c", new RecordProperty("c", BOOLEAN));
    RecordType recordTypeD = new RecordType(registry, propertiesD);

    Map<String, RecordProperty> propertiesE = Maps.newHashMap();
    propertiesE.put("a", new RecordProperty("a", INT));
    propertiesE.put("b", new RecordProperty("b", STRING));
    propertiesE.put("d", new RecordProperty("d", INT));
    RecordType recordTypeE = new RecordType(registry, propertiesE);

    assertEquals(recordTypeA, recordTypeA.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeA, recordTypeB.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeA, recordTypeC.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeA, recordTypeD.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeA, recordTypeE.getGreatestSubtype(recordTypeA));
    assertNotSame(recordTypeA, recordTypeA.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeA, recordTypeA.getGreatestSubtype(recordTypeB));
    assertEquals(recordTypeA, recordTypeB.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeA, recordTypeC.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeA, recordTypeD.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeA, recordTypeE.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeB, recordTypeB.getGreatestSubtype(recordTypeB));
    assertNotSame(recordTypeB, recordTypeB.getGreatestSubtype(recordTypeB));
    assertEquals(recordTypeB, recordTypeA.getGreatestSubtype(recordTypeB));
    assertEquals(recordTypeC, recordTypeC.getGreatestSubtype(recordTypeC));
    assertNotSame(recordTypeC, recordTypeC.getGreatestSubtype(recordTypeC));
    assertEquals(recordTypeC, recordTypeA.getGreatestSubtype(recordTypeC));
    assertEquals(recordTypeD, recordTypeD.getGreatestSubtype(recordTypeD));
    assertNotSame(recordTypeD, recordTypeD.getGreatestSubtype(recordTypeD));
    assertEquals(recordTypeD, recordTypeA.getGreatestSubtype(recordTypeD));
    assertEquals(recordTypeE, recordTypeE.getGreatestSubtype(recordTypeE));
    assertNotSame(recordTypeE, recordTypeE.getGreatestSubtype(recordTypeE));
    assertEquals(recordTypeE, recordTypeA.getGreatestSubtype(recordTypeE));
  }
}