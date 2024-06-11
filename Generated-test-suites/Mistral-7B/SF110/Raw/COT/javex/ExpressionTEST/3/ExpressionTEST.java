import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {
   @Test
   public void testExpressionEmptyConstructor() {
      Expression expr = new Expression();
      assertNotNull(expr);
   }
}