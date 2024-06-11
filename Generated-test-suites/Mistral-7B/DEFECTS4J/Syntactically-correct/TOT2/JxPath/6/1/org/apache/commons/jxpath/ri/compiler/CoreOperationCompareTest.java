import org.apache.commons.jxpath.Expression;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CoreOperationCompareTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertNotNull(coreOperationCompare);
    }

    // ...
}