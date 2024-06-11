import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createObject;
import static com.google.javascript.rhino.NodeFactory.createProperty;
import static com.google.javascript.rhino.NodeFactory.createNamedProperty;
import static com.google.javascript.rhino.NodeFactory.createInt;
import static com.google.javascript.rhino.NodeFactory.createNumber;
import static com.google.javascript.rhino.JSTypeFactory.*;
import static com.google.javascript.rhino.jstype.RecordTypeBuilder.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.RecordType;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    nodeFactory = NodeFactory.getInstance();
  }

  @Test
  public void testEmptyRecord() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    JSType recordType = recordTypeBuilder.build();
    assertEquals(JSTYPE_OBJECT, recordType.getClass());
  }

  @Test
  public void testRecordWithOneProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String propertyName = "propName";
    JSType propertyType = BOOLEAN;
    Node propertyNode = createNamedProperty(createString(propertyName), createInt(0));

    RecordTypeBuilder builder = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
    JSType recordType = builder.build();

    RecordType record = (RecordType) recordType;
    Map<String, JSType> properties = record.getProperties();
    assertEquals(1, properties.size());
    assertEquals(propertyType, properties.get(propertyName));
  }

  @Test
  public void testRecordWithTwoProperties() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String propertyName1 = "propName1";
    JSType propertyType1 = NUMBER;
    Node propertyNode1 = createNamedProperty(createString(propertyName1), createNumber(1.0));

    RecordTypeBuilder builder1 = recordTypeBuilder.addProperty(propertyName1, propertyType1, propertyNode1);

    String propertyName2 = "propName2";
    JSType propertyType2 = BOOLEAN;
    Node propertyNode2 = createNamedProperty(createString(propertyName2), createInt(0));

    RecordTypeBuilder builder2 = builder1.addProperty(propertyName2, propertyType2, propertyNode2);
    JSType recordType = builder2.build();

    RecordType record = (RecordType) recordType;
    Map<String, JSType> properties = record.getProperties();
    assertEquals(2, properties.size());
    assertEquals(propertyType1, properties.get(propertyName1));
    assertEquals(propertyType2, properties.get(propertyName2));
  }

  @Test(expected = IllegalStateException.class)
  public void testAddingDuplicateProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String propertyName = "propName";
    JSType propertyType = BOOL;
    Node propertyNode = createNamedProperty(createString(propertyName), createInt(0));

    RecordTypeBuilder builder = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
    builder.addProperty(propertyName, propertyType, propertyNode);
  }

  @Test
  public void testRecordWithEmptyProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String propertyName = "propName";
    Node propertyNode = createObject();

    RecordTypeBuilder builder = recordTypeBuilder.addProperty(propertyName, UNDEFINED, propertyNode);
    JSType recordType = builder.build();

    RecordType record = (RecordType) recordType;
    Map<String, JSType> properties = record.getProperties();
    assertEquals(1, properties.size());
    assertEquals(UNDEFINED, properties.get(propertyName));
  }

  @Test
  public void testRecordWithNativeObjectProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    String propertyName = "propName";
    Node propertyNode = createObject();

    RecordTypeBuilder builder = recordTypeBuilder.addProperty(propertyName, JSTYPE_OBJECT, propertyNode);
    JSType recordType = builder.build();

    RecordType record = (RecordType) recordType;
    Map<String, JSType> properties = record.getProperties();
    assertEquals(1, properties.size());
    assertEquals(JSTYPE_OBJECT, properties.get(propertyName));
  }
}