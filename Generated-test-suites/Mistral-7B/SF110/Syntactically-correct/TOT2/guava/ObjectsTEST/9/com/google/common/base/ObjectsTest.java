import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Objects.*;

public class ObjectsTest {

  @Test
  public void testEqual() {
    assertTrue(equal(null, null));
    assertTrue(equal(null, null));
    assertTrue(equal(1, 1));
    assertFalse(equal(1, "1"));
    assertFalse(equal("1", 1));
  }

  @Test
  public void testHashCode() {
    Object[] objects = new Object[10];
    for (int i = 0; i < objects.length; i++) {
      objects[i] = Integer.valueOf(i);
    }
    int hashCode = hashCode(objects);
    int expectedHashCode = Arrays.hashCode(objects);
    assertEquals(expectedHashCode, hashCode);
  }

  @Test
  public void testToStringHelper_omitNullValues_false() {
    Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
        .add("name1", 1)
        .add("name2", null)
        .add("name3", "value3");
    String toString = toStringHelper.toString();
    String expectedToString = "ClassName{" +
        "name1=1, " +
        "name2=null, " +
        "name3=value3" +
        '}';
    assertEquals(expectedToString, toString);
  }

  @Test
  public void testToStringHelper_omitNullValues_true() {
    Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
        .add("name1", 1)
        .add("name2", null)
        .add("name3", "value3")
        .omitNullValues();
    String toString = toStringHelper.toString();
    String expectedToString = "ClassName{" +
        "name1=1," +
        "name3=value3" +
        '}';
    assertEquals(expectedToString, toString);
  }

  @Test
  public void testFirstNonNull() {
    Object first = null;
    Object second = "non-null";
    String result = (String) firstNonNull(first, second);
    assertEquals("non-null", result);
  }
}