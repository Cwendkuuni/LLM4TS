import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;

public class AsPropertyTypeDeserializerTest {

 @Test
 public void testConstructionWithValidArguments() throws Exception {
  ObjectMapper mapper = new ObjectMapper();
  JavaType jsonType = mapper.getTypeFactory().constructType(String.class);
  TypeIdResolver idResolver = mapper.getTypeIdResolver();
  JavaType defaultImpl = mapper.getTypeFactory().constructType(Integer.class);

  AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(jsonType, idResolver, "typePropertyName", true, defaultImpl);
  assertNotNull(asPropertyTypeDeserializer);
 }
}