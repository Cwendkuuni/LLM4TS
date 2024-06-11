import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.json.JsonFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler externalTypeHandler;
    private Object testObject;
    private JsonFactory jsonFactory;

    @Before
    public void setUp() throws IOException {
        List<SettableBeanProperty> properties = new ArrayList<>();
        SettableBeanProperty prop1 = new SettableBeanProperty("property1", String.class);
        SettableBeanProperty prop2 = new SettableBeanProperty("property2", StringDeserializer.instance);
        properties.add(prop1);
        properties.add(prop2);

        Builder builder = new Builder();
        builder.addExternal(prop1, StringDeserializer.instance);
        builder.addExternal(prop2, StringDeserializer.instance);
        externalTypeHandler = builder.build();

        testObject = new Object(){};
        jsonFactory = new JsonFactory();
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String json = "{\"property1\":\"test1\",\"typePropertyName\":\"String\"}";
        JsonParser jp = jsonFactory.createParser(json);

        Assert.assertTrue(externalTypeHandler.handleTypePropertyValue(jp, new TestContext(), "property1", testObject));
        Assert.assertEquals("test1", externalTypeHandler.getProperty("property1").get(testObject));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String json = "{\"property1\":\"test1\"}";
        JsonParser jp = jsonFactory.createParser(json);

        Assert.assertTrue(externalTypeHandler.handlePropertyValue(jp, new TestContext(), "property1", testObject));
        Assert.assertEquals("test1", externalTypeHandler.getProperty("property1").get(testObject));
    }

    @Test
    public void testHandleInvalidProperty() throws IOException {
        String json = "{\"nonexistentProperty\":\"test1\"}";
        JsonParser jp = jsonFactory.createParser(json);

        Assert.assertFalse(externalTypeHandler.handlePropertyValue(jp, new TestContext(), "nonexistentProperty", testObject));
        Assert.assertNull(externalTypeHandler.getProperty("nonexistentProperty"));
    }

    @Test
    public void testHandleTypePropertyValueMissingValue() throws IOException {
        String json = "{\"typePropertyName\":\"String\"}";
        JsonParser jp = jsonFactory.createParser(json);

        Assert.assertFalse(externalTypeHandler.handleTypePropertyValue(jp, new TestContext(), "property1", testObject));
        Assert.assertNull(externalTypeHandler.getTypeIds());
    }

    @Test
    public void testHandleTypePropertyValueMissingName() throws IOException {
        String json = "{\"property1\":\"test1\"}";
        JsonParser jp = jsonFactory.createParser(json);

        Assert.assertFalse(externalTypeHandler.handleTypePropertyValue(jp, new TestContext(), "typePropertyName", testObject));
        Assert.assertNull(externalTypeHandler.getTypeIds());
    }

    @Test
    public void testComplete() throws IOException {
        String json = "{\"property1\":\"test1\",\"typePropertyName1\":\"String\",\"typePropertyName2\":\"Integer\"}";
        JsonParser jp = jsonFactory.createParser(json);

        externalTypeHandler.handlePropertyValue(jp, new TestContext(), "property1", testObject);
        externalTypeHandler.handleTypePropertyValue(jp, new TestContext(), "typePropertyName1", testObject);
        externalTypeHandler.handlePropertyValue(jp, new TestContext(), "property2", testObject);
        externalTypeHandler.handleTypePropertyValue(jp, new TestContext(), "typePropertyName2", testObject);

        Object result = externalTypeHandler.complete(jp, new TestContext(), testObject);

        Assert.assertEquals("test1", externalTypeHandler.getProperty("property1").get(testObject));
        Assert.assertNull(externalTypeHandler.getProperty("nonexistentProperty"));
        Assert.assertEquals("test1", testObject.getClass().getField("property1").get(result));
        Assert.assertNull(testObject.getClass().getField("nonexistentProperty"));
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @SuppressWarnings("unchecked")
            @Override
            public Object createUsingDefault(DeserializationContext ctxt, PropertyValueBuffer pvb) throws IOException {
                return new Object(){};
            }
        };

        String json = "[{\"property1\":\"test1\",\"typePropertyName1\":\"String\"},{\"property2\":\"test2\",\"typePropertyName2\":\"Integer\"}]";
        JsonParser jp = jsonFactory.createParser(json);

        externalTypeHandler.handlePropertyValue(jp, new TestContext(), "property1", testObject);
        externalTypeHandler.handleTypePropertyValue(jp, new TestContext(), "typePropertyName1", testObject);
        externalTypeHandler.handlePropertyValue(jp, new TestContext(), "property2", testObject);
        externalTypeHandler.handleTypePropertyValue(jp, new TestContext(), "typePropertyName2", testObject);

        Object result = externalTypeHandler.complete(jp, new TestContext(), null, creator);

        Assert.assertEquals("test1", externalTypeHandler.getProperty("property1").get(testObject));
        Assert.assertNull(externalTypeHandler.getProperty("nonexistentProperty"));
        Assert.assertEquals("test1", testObject.getClass().getField("property1").get(result));
        Assert.assertNull(testObject.getClass().getField("nonexistentProperty"));
        Assert.assertNotNull(result);
    }

    @Test
    public void testCompleteWithMissingTypeIds() throws IOException {
        externalTypeHandler.handlePropertyValue(jsonFactory.createParser("{\"property1\":\"test1\"}"), new TestContext(), "property1", testObject);

        try {
            externalTypeHandler.complete(jsonFactory.createParser("{}"), new TestContext(), testObject);
            Assert.fail();
        } catch (IOException e) {
            //expected
        }
    }

    @Test
    public void testCompleteWithMissingProperties() throws IOException {
        externalTypeHandler.handleTypePropertyValue(jsonFactory.createParser("{\"typePropertyName\":\"String\"}"), new TestContext(), "property1", testObject);

        try {
            externalTypeHandler.complete(jsonFactory.createParser("{}"), new TestContext(), testObject);
            Assert.fail();
        } catch (IOException e) {
            //expected
        }
    }

    static class TestContext extends DeserializationContext {
    }
}