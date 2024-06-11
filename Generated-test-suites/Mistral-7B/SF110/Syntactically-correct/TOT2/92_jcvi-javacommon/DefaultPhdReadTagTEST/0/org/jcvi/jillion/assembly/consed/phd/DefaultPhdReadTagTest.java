import org.junit.Test;
import org.junit.Assert.*;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        String type = "DNA";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
        String returnedType = phdReadTag.getType();
        assertEquals(type, returnedType);
    }
}