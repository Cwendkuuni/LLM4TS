import com.fasterxml.jackson.core.JsonGenerationSchema;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.base.GeneratorBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GeneratorBaseTest {

    private ObjectMapper mapper = JsonMapper.builder()
            .build();

    @Test
    public void testIsEnabled() throws Exception {
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask(), null) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };

        assertTrue(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(generator.isEnabled(GeneratorBase.Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testDisableFeatures() throws Exception {
        GeneratorBase generator = new GeneratorBase(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | GeneratorBase.Feature.ESCAPE_NON_ASCII.getMask(), null) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };

        generator.disable(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(generator.isEnabled(GeneratorBase.Feature.WRITE_NUMBERS_AS_STRINGS));

        generator.disable(GeneratorBase.Feature.ESCAPE_NON_ASCII);
        assertFalse(generator.isEnabled(GeneratorBase.Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testVersion() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };

        assertEquals("unknown", generator.version().getVersion());

        generator = new GeneratorBase(JsonGenerationSchema.Feature.AUTO_CLOSE_SOURCE.getMask(), null) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };

        assertEquals("2.12.3", generator.version().getVersion());
    }

    @Test
    public void testWriteString() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };

        generator.writeString("Hello World!");

        String json = mapper.writeValueAsString(new String("Hello World!"));
        assertEquals(json, generator.getOutputContext().getValueAsString());
    }

    @Test
    public void testWriteNull() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };

        generator.writeNull();
        assertNull(generator.getOutputContext().getValueAsString());
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteAfterClosed() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };

        generator.close();
        generator.writeString("Hello World!");
    }

    @Test
    public void testUseDefaultPrettyPrinter() throws Exception {
        GeneratorBase generator = new GeneratorBase(0, null) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };

        generator.useDefaultPrettyPrinter();
        assertEquals("", generator.getOutputContext().getValueAsString());

        generator = new GeneratorBase(0, null) {
            @Override
            protected PrettyPrinter _constructDefaultPrettyPrinter() {
                return new DefaultPrettyPrinter();
            }
        };

        generator.useDefaultPrettyPrinter();
        String json = mapper.writeValueAsString(new String("{\"name\":\"John\",\"age\":30}"));
        assertEquals(json, generator.getOutputContext().getValueAsString());
    }
}