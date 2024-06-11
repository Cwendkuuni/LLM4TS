import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaBuilderFactory;
import com.fasterxml.jackson.databind.jsonschema.SchemaValidator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.TestBase;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest extends TestBase {

    private ObjectNode delegateSchema;
    private StdDelegatingSerializer<Object> serializer;

    @BeforeClass
    public void setup() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        delegateSchema = mapper.schemaFor(String.class);
    }

    @Test
    public void testBasic() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final SerializerProvider provider = mapper.getSerializerProvider();

        final String testString = "Hello, World!";
        final Converter<Object, String> converter = mapper.getTypeFactory()
                .constructConverter(String.class, Object.class);

        final JavaType delegateType = mapper.getTypeFactory()
                .constructType(String.class);
        final JsonSerializer<String> stringSerializer = mapper.findValueSerializer(delegateType);

        serializer = new StdDelegatingSerializer<Object>(converter, delegateType, stringSerializer);

        final ObjectNode result = mapper.writeValueAsTree(new Object() {
        }, provider);

        final ObjectNode rootNode = result.path("delegateValue");

        final String resultString = rootNode.asText();
        Assert.assertEquals(resultString, testString);
        Assert.assertEquals(mapper.writerWithRootValueSerializer(String.class).writeValueAsString(testString),
                resultString);

        final SchemaValidator validator = new SchemaValidator(schemaFor(String.class), result);
        validator.validate();
    }

    @Test
    public void testCustomConverter() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final SerializerProvider provider = mapper.getSerializerProvider();

        final String testString = "Test";
        final Converter<Object, Integer> converter = mapper.getTypeFactory()
                .constructConverter(Integer.class, Object.class);

        final JavaType delegateType = mapper.getTypeFactory()
                .constructType(String.class);
        final JsonSerializer<String> stringSerializer = mapper.findValueSerializer(delegateType);

        final StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(converter,
                delegateType, stringSerializer);

        final ObjectNode result = mapper.writeValueAsTree(new Object() {
        }, provider);

        final ObjectNode rootNode = result.path("delegateValue");
        final int resultInt = mapper.readerForUpdating(rootNode).readValueAs(Integer.class);
        Assert.assertEquals(resultInt, testString.length());
        Assert.assertEquals(mapper.writerWithRootValueSerializer(String.class).writeValueAsString(testString),
                mapper.writerWithView(String.class).writeValueAsString(result));

        final SchemaValidator validator = new SchemaValidator(delegateSchema, result);
        validator.validate();
    }

    @Test
    public void testClassBased() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final SerializerProvider provider = mapper.getSerializerProvider();

        final int testInt = 42;
        final Converter<Object, Integer> converter = mapper.getTypeFactory()
                .constructConverter(Integer.class, Object.class);

        final JavaType delegateType = mapper.getTypeFactory()
                .constructType(Integer.class);
        final JsonSerializer<Integer> intSerializer = mapper.findValueSerializer(delegateType);

        serializer = new StdDelegatingSerializer<Object>(Integer.class, converter, intSerializer);

        final ObjectNode result = mapper.writeValueAsTree(new Object() {
        }, provider);

        final ObjectNode rootNode = result.path("delegateValue");
        final int resultInt = mapper.readerForUpdating(rootNode).readValueAs(Integer.class);
        Assert.assertEquals(resultInt, testInt);
        Assert.assertEquals(mapper.writerWithRootValueSerializer(Integer.class).writeValueAsString(testInt),
                result.toString());

        final SchemaValidator validator = new SchemaValidator(schemaFor(Integer.class), result);
        validator.validate();
    }

    @Test
    public void testContextualization() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final SerializerProvider provider = mapper.getSerializerProvider();

        final String testString = "Hello, World!";
        final Converter<Object, String> converter = mapper.getTypeFactory()
                .constructConverter(String.class, Object.class);

        final JavaType delegateType = mapper.getTypeFactory()
                .constructType(String.class);
        final JsonSerializer<String> stringSerializer = mapper.findValueSerializer(delegateType);

        serializer = new StdDelegatingSerializer<Object>(converter, delegateType, stringSerializer);

        final ObjectNode testNode = mapper.createObjectNode();
        testNode.put("test", testString);

        final ObjectNode result = mapper.writerForTree(testNode)
                .withObjectWriter(mapper.writerWithRootValueSerializer(String.class))
                .writeValueAsTree(new Object());

        final ObjectNode rootNode = result.path("delegateValue");

        final String resultString = rootNode.asText();
        Assert.assertEquals(resultString, testString);

        final SchemaValidator validator = new SchemaValidator(delegateSchema, result);
        validator.validate();
    }

    @Test
    public void testResolve() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final SerializerProvider provider = mapper.getSerializerProvider();

        final String testString = "Test";
        final Converter<Object, Integer> converter = mapper.getTypeFactory()
                .constructConverter(Integer.class, Object.class);

        final JavaType delegateType = mapper.getTypeFactory()
                .constructType(String.class);
        final JsonSerializer<String> stringSerializer = mapper.findValueSerializer(delegateType);

        final StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(converter, delegateType,
                stringSerializer);

        final ObjectNode result = mapper.writeValueAsTree(new Object() {
        }, provider);

        final ObjectNode rootNode = result.path("delegateValue");
        final String resultString = rootNode.asText();
        Assert.assertEquals(resultString, testString);
        Assert.assertEquals(mapper.writerWithRootValueSerializer(String.class).writeValueAsString(testString),
                resultString);

        final StdDelegatingSerializer<Integer> integerSerializer = serializer.withDelegate(converter,
                delegateType, integerSerializer);

        final int testInt = 42;
        final ObjectNode testNode = mapper.createObjectNode();
        testNode.put("test", testInt);

        final ObjectNode integerResult = mapper.writerForTree(testNode)
                .withObjectWriter(mapper.writerWithRootValueSerializer(Integer.class))
                .writeValueAsTree(integerSerializer);

        final ObjectNode integerRootNode = integerResult.path("delegateValue");
        final int resultInt = mapper.readerForUpdating(integerRootNode).readValueAs(Integer.class);
        Assert.assertEquals(resultInt, testInt);
        Assert.assertEquals(mapper.writerWithRootValueSerializer(Integer.class).writeValueAsString(testInt),
                integerRootNode.asText());

        final SchemaValidator intValidator = new SchemaValidator(schemaFor(Integer.class), integerResult);
        intValidator.validate();
    }

    @Test
    public void testTypeSerialization() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final SerializerProvider provider = mapper.getSerializerProvider();

        final String testString = "Test";
        final Converter<Object, Integer> converter = mapper.getTypeFactory()
                .constructConverter(Integer.class, Object.class);

        final JavaType delegateType = mapper.getTypeFactory()
                .constructType(String.class);
        final JsonSerializer<String> stringSerializer = mapper.findValueSerializer(delegateType);

        final StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(converter, delegateType,
                stringSerializer);

        final ObjectNode testNode = mapper.createObjectNode();
        testNode.put("test", testString);

        final ObjectNode result = mapper.writerForTree(testNode)
                .withObjectWriter(mapper.writerWithRootValueSerializer(String.class))
                .writeValueAsTree(new Object());

        final ObjectNode rootNode = result.path("delegateValue");

        final ObjectWriter valueWriter = mapper.writerWithView(String.class);
        final ObjectNode serializedTypeNode = mapper.readTree(valueWriter.writeValueAsString(delegateType));

        final SchemaValidator validator = new SchemaValidator(schemaFor(delegateType.getRawClass()), result);
        validator.validate();

        final String resultString = rootNode.asText();
        Assert.assertEquals(resultString, testString);

        final ObjectNode typeNode = result.path("_class");
        final ObjectNode typeNodeValue = typeNode.get(0);
        final String resultTypeName = typeNodeValue.asText();

        final TypeFactory typeFactory = mapper.getTypeFactory();
        final JavaType expectedType = typeFactory.constructType(ClassKey.fromInternal(resultTypeName));

        final SchemaValidator typeValidator = new SchemaValidator(schemaFor(expectedType.getRawClass()), serializedTypeNode);
        typeValidator.validate();
    }

    @Test(expectedExceptions = JsonGenerationException.class)
    public void testNullSerialization() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final SerializerProvider provider = mapper.getSerializerProvider();

        final ObjectNode testNode = mapper.createObjectNode();

        final String testString = null;
        final Converter<Object, String> converter = mapper.getTypeFactory()
                .constructConverter(String.class, Object.class);

        final JavaType delegateType = mapper.getTypeFactory()
                .constructType(String.class);
        final JsonSerializer<String> stringSerializer = mapper.findValueSerializer(delegateType);

        final StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(converter, delegateType,
                stringSerializer);

        final ObjectNode result = mapper.writerForTree(testNode)
                .withObjectWriter(mapper.writerWithRootValueSerializer(String.class))
                .writeValueAsTree(new Object());

        final ObjectNode rootNode = result.path("delegateValue");
        final String resultString = rootNode.asText();

        serializer.serialize(testString, provider.getJsonGenerator(), provider);
    }

    private SchemaValidator schemaFor(Type type) {
        final ObjectMapper mapper = new ObjectMapper();
        final SchemaBuilderFactory schemaBuilderFactory = mapper.schemaFor(type);
        return schemaBuilderFactory.build();
    }

}