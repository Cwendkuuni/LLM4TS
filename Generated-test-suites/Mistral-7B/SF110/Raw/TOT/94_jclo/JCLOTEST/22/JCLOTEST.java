import org.junit.Test;
import static org.junit.Assert.*;
import edu.mscd.cs.jclo.JCLO;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setUp() {
 testObject = new Object() {};
 jclo = new JCLO(testObject);
 }

 @Test
 public void testBasicUsage() {
 // Set values using constructor
 jclo = new JCLO("prefix", testObject, new String[][] {{"key1", "value1"}, {"key2", "value2"}});
 assertEquals("value1", jclo.getValue("key1"));
 assertEquals("value2", jclo.getValue("key2"));

 // Set values using parse method
 jclo.parse(new String[] {"--key1=value1", "--key2=value2"});
 assertEquals("value1", jclo.getValue("key1"));
 assertEquals("value2", jclo.getValue("key2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingField() {
 jclo = new JCLO(testObject);
 assertNull(jclo.getValue("missingField"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidUsageType() {
 jclo = new JCLO(testObject);
 jclo.parse(new String[] {"--invalidType=value"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingValue() {
 jclo = new JCLO(testObject, new String[][] {{"key1", ""}});
 assertNull(jclo.getValue("key1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidKey() {
 jclo = new JCLO(testObject);
 jclo.parse(new String[] {"--invalidKey=value"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingEquals() {
 jclo = new JCLO(testObject);
 jclo.parse(new String[] {"--key1=value", "--key2"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEquals() {
 jclo = new JCLO(testObject);
 jclo.parse(new String[] {"--key1=valuevalue", "--key2=value"});
 }

 @Test
 public void testGetUsage() {
 jclo = new JCLO(testObject);
 jclo.parse(new String[] {"--key1=value1", "--key2=value2"});
 assertEquals("-key1 value1\n" +
                 "-key2 value2\n", jclo.usage());
 }
}