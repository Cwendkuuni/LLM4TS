package edu.mscd.cs.jclo;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.mscd.cs.jclo.JCLO;

public class JCLOTest {

    @Test
    public void testJCLOConstructionWithObjectAndAliases() {
        Object testObject = new Object();
        String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
        JCLO jclo = new JCLO(testObject, aliases);
        assertNotNull(jclo.getValue("a"));
        assertNotNull(jclo.getValue("aliasA"));
        assertNotNull(jclo.getValue("b"));
        assertNotNull(jclo.getValue("aliasB"));
    }

    // Add test cases for other methods and constructors here
}