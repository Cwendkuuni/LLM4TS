import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoFourArgsConstruction() {
        PropertyName name = new PropertyName("testProp");
        Class<String> scope = String.class;
        Class<RuntimeObjectIdGenerator> gen = RuntimeObjectIdGenerator.class;
        Class<SimpleObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, false, resolver);

        assertEquals("ObjectIdInfo: propName=testProp, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.introspect.RuntimeObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }
}