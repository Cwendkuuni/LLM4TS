import org.junit.Test;
import org.junit.Assert;
import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  @Test
  public void testNewInstance() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();

    // Test with a simple Java class
    String result = "Test";
    String expected = "Test";
    String testClass = String.class.getName();
    String instance = (String) allocator.newInstance(String.class);
    Assert.assertEquals(expected, instance);

    // Test with a generic class
    @SuppressWarnings("unchecked")
    Number<Integer> resultGen = 0;
    Number<Integer> expectedGen = 0;
    Class<Number<Integer>> testClassGen = (Class<Number<Integer>>) Class.forName("java.lang.Number");
    Number<Integer> instanceGen = (Number<Integer>) allocator.newInstance(testClassGen);
    Assert.assertEquals(expectedGen, instanceGen);

    // Test with a custom class
    class TestCustom {
      private int value;

      public int getValue() {
        return value;
      }
    }

    int resultCustom = 5;
    int expectedCustom = 5;
    Class<TestCustom> testClassCustom = TestCustom.class;
    TestCustom instanceCustom = (TestCustom) allocator.newInstance(testClassCustom);
    Assert.assertEquals(expectedCustom, instanceCustom.getValue());

    // Test with an interface
    @SuppressWarnings("unchecked")
    TestInterface resultInterface = () -> {};
    TestInterface expectedInterface = () -> {};
    interface TestInterface {
      void testMethod();
    }
    Class<TestInterface> testClassInterface = (Class<TestInterface>) Class.forName("TestInterface");
    TestInterface instanceInterface = (TestInterface) allocator.newInstance(testClassInterface);
    Assert.assertNotNull(instanceInterface);
    // Cannot test methods in interfaces or abstract classes

    // Test with an abstract class
    @SuppressWarnings("unchecked")
    AbstractClass resultAbstract = new AbstractClass() {
      @Override
      public void testMethod() {
        // Overridden method
      }
    };
    AbstractClass expectedAbstract = new AbstractClass() {
      @Override
      public void testMethod() {
        // Overridden method
      }
    };
    Class<AbstractClass> testClassAbstract = AbstractClass.class;
    AbstractClass instanceAbstract = (AbstractClass) allocator.newInstance(testClassAbstract);
    // Cannot test methods in abstract classes

    // Test with a final class
    String resultFinal = "Test";
    String expectedFinal = "Test";
    final class FinalClass {
      private String value;

      public FinalClass(String value) {
        this.value = value;
      }

      public String getValue() {
        return value;
      }
    }
    Class<FinalClass> testClassFinal = FinalClass.class;
    FinalClass instanceFinal = (FinalClass) allocator.newInstance(testClassFinal);
    Assert.assertEquals(expectedFinal, instanceFinal.getValue());

    // Test with an array
    int[] resultArray = new int[5];
    int[] expectedArray = new int[5];
    Class<int[]> testClassArray = int[].class;
    int[] instanceArray = (int[]) allocator.newInstance(testClassArray);
    Assert.assertArrayEquals(expectedArray, instanceArray);

    // Test with a primitive type
    boolean resultPrimitive = true;
    boolean expectedPrimitive = true;
    Class<Boolean> testClassPrimitive = Boolean.class;
    Boolean instancePrimitive = (Boolean) allocator.newInstance(testClassPrimitive);
    Assert.assertEquals(expectedPrimitive, instancePrimitive);

    // Test with a class that has no public constructor
    private class PrivateClass {
      private String value;

      private PrivateClass(String value) {
        this.value = value;
      }

      public String getValue() {
        return value;
      }
    }

    String resultPrivate = "Test";
    String expectedPrivate = "Test";
    Class<PrivateClass> testClassPrivate = PrivateClass.class;
    PrivateClass instancePrivate = (PrivateClass) allocator.newInstance(testClassPrivate);
    Assert.assertEquals(expectedPrivate, instancePrivate.getValue());

    // Test with a class that has a private constructor and is not serializable
    private class PrivateNonSerializable {
      private PrivateNonSerializable() {}
    }

    try {
      Class<PrivateNonSerializable> testClassNonSerializable = PrivateNonSerializable.class;
      allocator.newInstance(testClassNonSerializable);
      Assert.fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceThrowsException() throws Exception {
    UnsafeAllocator allocator = UnsafeAllocator.create();

    Class<?> testClassException = Array.class;
    allocator.newInstance(testClassException);
  }
}