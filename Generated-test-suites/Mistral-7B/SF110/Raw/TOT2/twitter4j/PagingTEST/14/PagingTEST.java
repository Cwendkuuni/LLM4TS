import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1L, 2L);
        List<HttpParameter> httpParameters = paging.asPostParameterList(Paging.SMCP, "count");
        assertNotNull(httpParameters);
    }
}