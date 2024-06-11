import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() throws Exception {
        ObjectReader objectIdReader = new ObjectMapper().readerFor(ObjectIdReader.class);
        ObjectIdReader mockObjectIdReader = objectIdReader.readValueAs(ObjectIdReader.class);
        PropertyMetadata metadata = new StandardPropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, metadata);
        assertNotNull(objectIdValueProperty);
    }
}