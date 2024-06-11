import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10);
        List<HttpParameter> result = paging.asPostParameterList();
        assertEquals(4, result.size());
        assertArrayEquals(new String[]{"page", "1", "count", "10"}, getParameterNamesAndValues(result).toArray());
    }

    private Object[] getParameterNamesAndValues(List<HttpParameter> parameters) {
        Object[] result = new Object[parameters.size() * 2];
        for (int i = 0; i < parameters.size(); i++) {
            result[i * 2] = parameters.get(i).getName();
            result[i * 2 + 1] = parameters.get(i).getValue();
        }
        return result;
    }
}