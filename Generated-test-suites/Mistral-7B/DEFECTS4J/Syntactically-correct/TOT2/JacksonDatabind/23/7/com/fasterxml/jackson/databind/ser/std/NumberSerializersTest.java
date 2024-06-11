import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaGenerator;
import com.fasterxml.jackson.databind.jsonschema.SchemaPrinter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testAddAll() throws Exception {
        ObjectNode jsonSchema = JsonSchemaGenerator.construct(mapper, JsonSchema.Stability.STABLE);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema);
        ObjectNode rootNode = mapper.readTree(json);
        JsonNode root = rootNode.get("$schema");
        JsonNode titleNode = rootNode.get("definitions").get("NumberSerializers");
        JsonNode addAllNode = titleNode.get("properties").get("allDeserializers");
        JsonNode expectedDeserializers = mapper.readTree(getClass().getResource("/expected_deserializers.json").getFile());
        JsonNode actualDeserializers = addAllNode.get("additionalProperties");
        assertEquals(expectedDeserializers, actualDeserializers);
    }
}