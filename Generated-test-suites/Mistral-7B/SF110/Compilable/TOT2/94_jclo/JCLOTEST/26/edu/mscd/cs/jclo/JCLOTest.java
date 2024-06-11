package edu.mscd.cs.jclo;
import org.junit.Assert;
import org.junit.Test;
import edu.mscd.cs.jclo.JCLO;

public class JCLOTest {
    @Test
    public void testJCLO() {
        JCLO jclo = new JCLO(new Object());
        Assert.assertNotNull(jclo);
    }
}