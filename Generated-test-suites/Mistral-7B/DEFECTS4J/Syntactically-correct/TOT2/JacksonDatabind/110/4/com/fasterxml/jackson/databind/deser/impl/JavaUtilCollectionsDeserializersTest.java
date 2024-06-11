import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JavaUtilCollectionsDeserializersTest {

    @org.junit.Test
    public void testFindForCollection() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType javaType = typeFactory.constructType(new TypeReference<List<String>>() {}.getType());

        when(ctxt.findValueValidator()).thenReturn(mock(com.fasterxml.jackson.databind.JsonDeserializer.None.class));

        JsonDeserializer<?> jsonDeserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        assertNotNull(jsonDeserializer);
    }
}