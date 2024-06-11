import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CreatorCollectorTest {
    @Test
    public void testDefaultCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();
        CreatorCollector collector = new CreatorCollector(null, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        Object obj = instantiator.createValue("{}");
        // Add assertions for default creator here
    }

    @Test
    public void testStringCreator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();
        CreatorCollector collector = new CreatorCollector(null, false);
        String creatorName = "myStringCreator";
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        when(creator.getParameterName()).thenReturn(creatorName);
        collector.addStringCreator(creator, false);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);
        Object obj = instantiator.createValue("{\"creator\":\"" + creatorName + "\"}");
        // Add assertions for string creator here
    }

    // Add test cases for other creators and edge cases here
}