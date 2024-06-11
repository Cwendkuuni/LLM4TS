import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import twitter4j.internal.http.HttpParameter;
import twitter4j.internal.http.HttpParameterList;
import twitter4j.Paging;

public class PagingTest {

    @Test
    static void testAsPostParameterList() {
        // Test case from Expert 1
    }

    @Test
    static void testAsPostParameterArray() {
        // Test case from Expert 1
    }

    @Test
    static void testAsPostParameterList_supportedParams() {
        // Test case from Expert 1
    }

    @Test
    static void testAsPostParameterList_supportedParams_perPageParamName() {
        // Test case from Expert 1
    }

    @Test
    static void testAsPostParameterArray_supportedParams() {
        // Test case from Expert 1
    }

    @Test
    static void testAsPostParameterArray_supportedParams_perPageParamName() {
        // Test case from Expert 1
    }

    @Test
    static void testAddPostParameter_supportedParams() {
        // Test case from Expert 1
        // Test case for empty supportedParams array
        char[] supportedParams = new char[0];
        List<HttpParameter> pagingParams = new ArrayList<>();
        Paging paging = new Paging(-1);

        assertThrows(IllegalStateException.class, () -> {
            paging.addPostParameter(supportedParams, 's', pagingParams, "since_id", 123);
        });

        assertThrows(IllegalStateException.class, () -> {
            paging.addPostParameter(supportedParams, 'm', pagingParams, "max_id", 123);
        });

        assertThrows(IllegalStateException.class, () -> {
            paging.addPostParameter(supportedParams, 'c', pagingParams, "count", 123);
        });

        assertThrows(IllegalStateException.class, () -> {
            paging.addPostParameter(supportedParams, 'p', pagingParams, "page", 123);
        });
    }

    @Test
    static void testAddPostParameter_unsupportedParam() {
        // Test case for unsupported parameter
        char[] supportedParams = new char[] {'s', 'm'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        Paging paging = new Paging(-1);

        assertThrows(IllegalStateException.class, () -> {
            paging.addPostParameter(supportedParams, 'x', pagingParams, "unsupported", 123);
        });
    }

    @Test
    static void testAddPostParameter_negativeParamValue() {
        // Test case for negative parameter value
        char[] supportedParams = new char[] {'s', 'm', 'c', 'p'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        Paging paging = new Paging(-1);

        assertThrows(IllegalArgumentException.class, () -> {
            paging.addPostParameter(supportedParams, 'c', pagingParams, "count", -1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            paging.addPostParameter(supportedParams, 'p', pagingParams, "page", -1);
        });
    }

    @Test
    static void testAddPostParameter_supportedParams_emptyList() {
        // Test case for empty list
        char[] supportedParams = new char[] {'s', 'm', 'c', 'p'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        Paging paging = new Paging(-1);

        paging.addPostParameter(supportedParams, 's', pagingParams, "since_id", 123);
        paging.addPostParameter(supportedParams, 'm', pagingParams, "max_id", 456);
        paging.addPostParameter(supportedParams, 'c', pagingParams, "count", 10);
        paging.addPostParameter(supportedParams, 'p', pagingParams, "page", 1);

        assertEquals(0, pagingParams.size());
    }

    @Test
    static void testAddPostParameter_supportedParams_perPageParamName_emptyList() {
        // Test case for empty list with perPageParamName
        char[] supportedParams = new char[] {'s', 'm', 'c', 'p'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        Paging paging = new Paging(-1);

        paging.addPostParameter(supportedParams, 's', pagingParams, "since_id", 123);
        paging.addPostParameter(supportedParams, 'm', pagingParams, "max_id", 456);
        paging.addPostParameter(supportedParams, 'p', pagingParams, "page", 1);

        List<HttpParameter> expected = new ArrayList<>();
        assertEquals(expected, pagingParams);
    }
}