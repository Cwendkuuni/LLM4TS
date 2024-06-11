import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testValuedEnumConstructor() {
        String name = "TestEnum";
        int value = 10;
        ValuedEnum testInstance = new ValuedEnum(name, value);
        Assert.assertEquals(name, testInstance.getName());
        Assert.assertEquals(value, testInstance.getValue());
    }
}