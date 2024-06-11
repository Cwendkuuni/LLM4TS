import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 20, 1L, 2L);
        List<HttpParameter> httpParameters = paging.asPostParameterList();
        assertNotNull(httpParameters);
    }
}