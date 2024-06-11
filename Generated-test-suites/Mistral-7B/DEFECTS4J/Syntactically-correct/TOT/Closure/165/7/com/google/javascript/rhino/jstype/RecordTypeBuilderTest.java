import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    JSType numberType = registry.getNumberType();
    JSType stringType = registry.getStringType();

    Node propertyNode1 = newObjectLiteral(
        ID("name1").set(stringLiteral("property1")),
        ID("type1").set(numberType)
    );

    RecordProperty property1 = builder.addProperty(
        "property1", numberType, propertyNode1).build().getProperty(0);

    Node propertyNode2 = newObjectLiteral(
        ID("name2").set(stringLiteral("property2")),
        ID("type2").set(stringType)
    );

    RecordProperty property2 = builder.addProperty(
        "property2", stringType, propertyNode2).build().getProperty(1);

    RecordType recordType = builder.build();

    JSType record = recordType.getInstanceType();

    assertEquals("The record should have 2 properties", 2, recordType.getMembers().size());
    assertEquals("The first property should be named 'property1' and be of type Number",
        "property1", property1.getName());
    assertEquals("The first property should be of type Number", numberType, property1.getType());
    assertEquals("The second property should be named 'property2' and be of type String",
        "property2", property2.getName());
    assertEquals("The second property should be of type String", stringType, property2.getType());

    Node recordInstance = newObjectLiteral();
    recordInstance = setProperty(recordInstance, property1.getName(), numberLiteral(1));
    recordInstance = setProperty(recordInstance, property2.getName(), stringLiteral("test"));

    assertEquals("The record instance should have 2 properties", 2, record.getMembers().size());
    assertEquals("The value of the first property should be 1", 1, getPropertyValue(recordInstance, property1.getName()).toNumber().intValue());
    assertEquals("The value of the second property should be 'test'", "test", getPropertyValue(recordInstance, property2.getName()).toString());
  }
}