import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.junit.Assert;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "DNA";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
        String returnedType = phdReadTag.getType();
        Assert.assertEquals(type, returnedType);
    }
}