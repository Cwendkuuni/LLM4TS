import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testIsConcrete() {
        JavaType javaType = new JavaType(Object.class, 0, null, null, false);
        assertTrue(javaType.isConcrete());
    }
}