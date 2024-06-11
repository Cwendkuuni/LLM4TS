import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.scribe.model.Response;
import org.scribe.utils.StreamUtils;

public class ResponseTest {

    @Test
    public void testGetBody_SuccessCase() {
        // Test case for getBody() method
    }

    @Test(expected = NullPointerException.class)
    public void testGetBody_NullBody() {
        // Test case for getBody() method when body is null
    }

    @Test
    public void testGetCode_SuccessCase() {
        // Test case for getCode() method when response code is 200
    }

    @Test
    public void testGetCode_UnknownHostException() {
        // Test case for getCode() method when UnknownHostException is thrown
    }

    @Test
    public void testGetHeaders_SuccessCase() {
        // Test case for getHeaders() method when headers are present
    }

    @Test
    public void testGetHeaders_NullHeaderMap() {
        // Test case for getHeaders() method when header map is null
    }

    static class MockHttpURLConnection implements HttpURLConnection {

        private final int responseCode;
        private final byte[] responseBody;
        private final Map<String, List<String>> headerFields;

        public MockHttpURLConnection(int responseCode, byte[] responseBody, Map<String, List<String>> headerFields) {
            this.responseCode = responseCode;
            this.responseBody = responseBody;
            this.headerFields = headerFields;
        }

        @Override
        public int getResponseCode() {
            return responseCode;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(responseBody);
        }

        @Override
        public Map<String, List<String>> getHeaderFields() {
            return headerFields;
        }
    }

    static class ResponseUnderTest extends Response {

        public ResponseUnderTest(HttpURLConnection connection) throws IOException {
            super(connection);
        }
    }

    @Test
    public void testParseHeaders() {
        // Test case for parseHeaders() method
    }

    @Test
    public void testWasSuccessful() {
        // Test case for wasSuccessful() method
    }
}