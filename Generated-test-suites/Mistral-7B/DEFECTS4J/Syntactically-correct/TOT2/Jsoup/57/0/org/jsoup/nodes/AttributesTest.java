import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.junit.Assert.*;

public class AttributesTest {

   @Test
   public void testGetMethod() {
      Attributes attributes = new Attributes();
      attributes.put("testKey", "testValue");
      String actual = attributes.get("testKey");
      String expected = "testValue";
      assertEquals(expected, actual);
   }

   @Test
   public void testPutMethodWithExistingKey() {
      Attributes attributes = new Attributes();
      attributes.put("testKey", "testValue1");
      attributes.put("testKey", "testValue2");
      String actual = attributes.get("testKey");
      String expected = "testValue2";
      assertEquals(expected, actual);
   }

   @Test
   public void testPutMethodWithNullValue() {
      Attributes attributes = new Attributes();
      attributes.put("testKey", "testValue1");
      attributes.put("testKey", null);
      String actual = attributes.get("testKey");
      assertEquals("", actual);
   }

   // Test cases for other methods ...

}