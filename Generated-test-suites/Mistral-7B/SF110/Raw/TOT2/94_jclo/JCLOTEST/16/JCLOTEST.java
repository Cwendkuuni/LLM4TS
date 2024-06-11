import org.junit.Assert;
import org.junit.Test;
import edu.mscd.cs.jclo.JCLO;

public class JCLOTest {

    @Test
    public void testDefaultConstructor() {
        JCLO jclo = new JCLO(null);
        Assert.assertNotNull(jclo.object);
        Assert.assertNull(jclo.prefix);
        Assert.assertNull(jclo.aliases);
        Assert.assertEquals(0, jclo.fields.length);
    }

    // Test cases for other methods will follow here
}