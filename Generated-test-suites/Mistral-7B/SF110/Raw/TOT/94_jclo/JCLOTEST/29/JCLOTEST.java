import org.junit.Test;
import static org.junit.Assert.*;
import edu.mscd.cs.jclo.JCLO;

public class JCLOTest {

 private Object testObject;
 private JCLO jclo;

 @Before
 public void setUp() {
 testObject = new Object() {
 };
 jclo = new JCLO(testObject);
 }

 @Test
 public void testJCLOConstructorWithObject() {
 jclo = new JCLO(testObject);
 assertEquals("Object class", jclo.getClass().getName(), "JCLO");
 }

 @Test
 public void testJCLOConstructorWithObjectAndAliases() {
 String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
 jclo = new JCLO(testObject, aliases);
 assertEquals("Object class", jclo.getClass().getName(), "JCLO");
 assertTrue(jclo.hasAliases());
 assertEquals("aliasA", jclo.getAliasForKey("a"));
 assertEquals("aliasB", jclo.getAliasForKey("b"));
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObject() {
 String prefix = "test_";
 jclo = new JCLO(prefix, testObject);
 assertEquals("Prefix and Object", jclo.getPrefix(), prefix);
 }

 @Test
 public void testJCLOConstructorWithPrefixAndObjectAndAliases() {
 String prefix = "test_";
 String[][] aliases = new String[][]{{"a", "aliasA"}, {"b", "aliasB"}};
 jclo = new JCLO(prefix, testObject, aliases);
 assertEquals("Prefix, Object and Aliases", jclo.getPrefix(), prefix);
 assertTrue(jclo.hasAliases());
 assertEquals("aliasA", jclo.getAliasForKey("a"));
 assertEquals("aliasB", jclo.getAliasForKey("b"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJCLOWithInvalidPrefixAndObject() {
 String prefix = "test_";
 jclo = new JCLO(prefix, testObject, null);
 jclo.parse(new String[]{"--noPrefix", "--invalidKey"});
 fail("JCLO should have thrown IllegalArgumentException with 'No such option: ''invalidKey'''");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJCLOWithInvalidOptions() {
 String[] invalidOptions = new String[]{"--invalidOption1", "--invalidOption2"};
 jclo = new JCLO(testObject);
 jclo.parse(invalidOptions);
 fail("JCLO should have thrown IllegalArgumentException with 'No such option: ''invalidOption1''' and 'No such option: ''invalidOption2'''");
 }

 @Test
 public void testJCLOGetValue() {
 Field testField = TestObject.class.getDeclaredField("testField");
 testField.setAccessible(true);
 testField.set(testObject, "testValue");
 jclo.parse(new String[]{"--", "--testField=testValue"});
 assertEquals("testValue", jclo.getValue("testField"));
 }

 @Test
 public void testJCLOGetValueWithPrefix() {
 Field testField = TestObject.class.getDeclaredField("testField");
 testField.setAccessible(true);
 testField.set(testObject, "testValue");
 jclo = new JCLO("prefix_", testObject, null);
 jclo.parse(new String[]{"--prefix_testField=testValue"});
 assertEquals("testValue", jclo.getValue("testField"));
 }

 @Test
 public void testJCLOGetValueWithAliases() {
 String[][] aliases = new String[][]{{"testField", "aliasTestField"}};
 jclo = new JCLO(testObject, aliases);
 jclo.parse(new String[]{"--aliasTestField=testValue"});
 assertEquals("testValue", jclo.getValue("testField"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJCLOGetValueWithInvalidKey() {
 jclo.parse(new String[]{"--", "--invalidKey=testValue"});
 fail("JCLO should have thrown IllegalArgumentException with 'No such option: ''invalidKey'''");
 }

 @Test
 public void testJCLOSetValue() {
 Field testField = TestObject.class.getDeclaredField("testField");
 testField.setAccessible(true);
 Object oldValue = testField.get(testObject);
 jclo = new JCLO(testObject);
 jclo.parse(new String[]{"--testField=newTestValue"});
 assertEquals("newTestValue", testField.get(testObject));
 }

 @Test
 public void testJCLOUsage() {
 jclo = new JCLO(testObject);
 String usage = jclo.usage();
 String expectedUsage = "--\n" +
 "--testField=VALUE\n" +
 "--testField_=VALUE\n" +
 "--prefix_testField=VALUE\n" +
 "--prefix_testField_=VALUE\n" +
 "--aliasTestField=VALUE\n" +
 "--aliasTestField_=VALUE\n" +
 "--noPrefix\n" +
 "--doubleDashes\n" +
 "--noEquals\n";
 assertEquals(expectedUsage, usage);
 }

}

class TestObject {
 private String testField;

 public String getTestField() {
 return testField;
 }

 public void setTestField(String testField) {
 this.testField = testField;
 }
}