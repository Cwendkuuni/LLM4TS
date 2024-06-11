
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.SimpleTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyContextTypeDeserializer;
import com.fasterxml.jackson.databind.type.ArrayTypeDeserializer;
import com.fasterxml.jackson.databind.type.ObjectMapper.DefaultTyping;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    static class TestContext extends DeserializationContext {
        public TestContext() { super(null); }

        @Override
        public DeserializationConfig getConfig() { return null; }

        @Override
        public ObjectReader getRootValueFactory() { return null; }

        @Override
        public void handleUnexpectedToken(JsonParser p) throws IOException {
            p.skipChildren();
        }

        @Override
        public EmptyContextTypeDeserializer getEmptyValueDeserializer() { return null; }

        @Override
        public TypeDeserializer deserializerForProperty(BeanProperty property) throws IOException { return null; }

        @Override
        public TypeDeserializer deserializerForRootValue() throws IOException { return null; }

        @Override
        public Object deserializeValue(String text, Class<?> valueClass, Type deserializationType) throws IOException { return null; }
    }

    @Test
    public void testNullifyingDeserializer() throws IOException {
        Expert1 expert1 = new Expert1();
        Expert2 expert2 = new Expert2();
        Expert3 expert3 = new Expert3();

        // Expert 1 test cases
        expert1.testDeserialize();
        expert1.testDeserializeWithNullParser();

        // Expert 2 test cases
        expert1.testDeserializeWithType_NullInput();
        expert1.testDeserializeWithType_Array();
        expert1.testDeserializeWithType_Object();
        expert1.testDeserializeWithType_UnexpectedToken();

        // Expert 3 test cases
        expert1.testDeserialize_Recursive();
        expert1.testDeserializeWithType_Recursive();
    }
}
