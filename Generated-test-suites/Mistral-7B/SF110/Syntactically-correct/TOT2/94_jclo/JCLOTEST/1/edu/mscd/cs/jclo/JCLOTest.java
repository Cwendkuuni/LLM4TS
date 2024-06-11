import static org.junit.Assert.*;
import edu.mscd.cs.jclo.JCLO;

public class JCLOTest {

    @Test
    public void testDefaultConstructor() {
        JCLO jclo = new JCLO(null);
        assertNull(jclo.getValue("nonexistentField"));
    }
}