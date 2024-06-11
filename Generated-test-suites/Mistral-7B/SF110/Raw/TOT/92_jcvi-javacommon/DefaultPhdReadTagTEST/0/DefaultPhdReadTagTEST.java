import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DefaultPhdReadTagTest {
    @Test
    void testGetType() {
        String type = "TestType";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
        assertEquals(type, phdReadTag.getType());
    }
}