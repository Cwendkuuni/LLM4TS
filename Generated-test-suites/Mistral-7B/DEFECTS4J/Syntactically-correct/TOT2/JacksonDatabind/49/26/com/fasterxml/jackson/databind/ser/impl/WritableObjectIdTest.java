import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonGeneratorFactory;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WritableObjectIdTest {
    private JsonGenerator generator;
    private SerializerProvider provider;
    private ObjectMapper objectMapper;
    private ObjectIdWriter objectIdWriter;
    private ObjectIdGenerator<Long> generatorForTests;

    @Before
    public void setUp() throws Exception {
        generatorForTests = new ObjectIdGenerator<Long>() {
            private long counter = 0;
            @Override
            public Long generateId(Object forPojo) {
                return counter++;
            }
        };

        generator = JsonGeneratorFactory.instance.createGenerator(System.out);
        provider = new SerializerProvider() {};
        objectMapper = new ObjectMapper();
        objectIdWriter = new ObjectIdWriter(new StubSerializer());
    }

    @Test
    public void testWriteAsId_NullGenerator() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            writableObjectId.writeAsId(generator, provider, objectIdWriter);
        });
    }

    @Test
    public void testWriteAsId_NullProvider() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generatorForTests);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            writableObjectId.writeAsId(null, provider, objectIdWriter);
        });
    }

    @Test
    public void testWriteAsId_NullW() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generatorForTests);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            writableObjectId.writeAsId(generator, null, objectIdWriter);
        });
    }

    @Test
    public void testWriteAsId_IdIsWritten() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generatorForTests);
        writableObjectId.id = 1L;
        writableObjectId.idWritten = true;

        Assert.assertFalse(writableObjectId.writeAsId(generator, provider, objectIdWriter), "ID should not be written again");
    }

    @Test
    public void testWriteAsId_NullId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generatorForTests);

        Assert.assertFalse(writableObjectId.writeAsId(generator, provider, objectIdWriter), "Null ID should not be written");
    }

    @Test
    public void testGenerateId_NullGenerator() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        Object forPojo = new Object();

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            writableObjectId.generateId(forPojo);
        });
    }

    @Test
    public void testGenerateId_NullForPojo() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generatorForTests);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            writableObjectId.generateId(null);
        });
    }

    @Test
    public void testWriteAsField_NullGenerator() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generatorForTests);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            writableObjectId.writeAsField(null, provider, objectIdWriter);
        });
    }

    @Test
    public void testWriteAsField_NullProvider() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generatorForTests);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            writableObjectId.writeAsField(generator, null, objectIdWriter);
        });
    }

    @Test
    public void testWriteAsField_NullW() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generatorForTests);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            writableObjectId.writeAsField(generator, provider, null);
        });
    }

    @Test
    public void testWriteAsField_IdIsWritten() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generatorForTests);
        writableObjectId.id = 1L;
        writableObjectId.idWritten = true;

        Assert.assertFalse(writableObjectId.writeAsField(generator, provider, objectIdWriter), "ID should not be written again");
    }

    @Test
    public void testWriteAsField_NullId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(generatorForTests);

        Assert.assertFalse(writableObjectId.writeAsField(generator, provider, objectIdWriter), "Null ID should not be written");
    }

    @Test
    public void testWriteAsId() throws IOException {
        ObjectIdGenerator<Long> generator = new ObjectIdGenerator<Long>() {
            private long counter = 0;
            @Override
            public Long generateId(Object forPojo) {
                return counter++;
            }
        };

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator gen = JsonGeneratorFactory.instance.createGenerator(System.out);
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(new StubSerializer());

        writableObjectId.writeAsId(gen, new SerializerProvider(), objectIdWriter);

        Assertions.assertTrue(gen.haswriteObjectId(), "Object Id should be written");
    }

    @Test
    public void testGenerateId() {
        ObjectIdGenerator<Long> generator = new ObjectIdGenerator<Long>() {
            private long counter = 0;
            @Override
            public Long generateId(Object forPojo) {
                return counter++;
            }
        };

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        Object forPojo = new Object();

        Object id = writableObjectId.generateId(forPojo);

        Assertions.assertNotNull(id, "An ID should be generated");
        Assertions.assertEquals(id.getClass(), Long.class, "ID should be of type Long");
    }

    @Test
    public void testWriteAsField() throws IOException {
        ObjectIdGenerator<Long> generator = new ObjectIdGenerator<Long>() {
            private long counter = 0;
            @Override
            public Long generateId(Object forPojo) {
                return counter++;
            }
        };

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider provider = objectMapper.getSerializers();
        JsonGenerator gen = JsonGeneratorFactory.instance.createGenerator(System.out);
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(new StubSerializer());

        writableObjectId.writeAsField(gen, provider, objectIdWriter);

        Assertions.assertTrue(gen.haswrittenStartObject(), "Start Object should be written");
        Assertions.assertTrue(gen.haswrittenFieldName("id"), "Field name 'id' should be written");
        Assertions.assertTrue(gen.haswrittenEndObject(), "End Object should be written");
    }
}