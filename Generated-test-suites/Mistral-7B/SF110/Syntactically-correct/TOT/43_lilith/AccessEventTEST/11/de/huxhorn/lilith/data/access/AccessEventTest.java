import org.junit.Assert;
import org.junit.Test;
import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.LoggerContext;
import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {

    @Test
    public void testAccessEventConstructorAndGetters() {
        Long timeStamp = 123L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com" + requestURI;
        String remoteHost = "127.0.0.1";
        String remoteUser = "testuser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testserver.com";
        String remoteAddress = "127.0.0.1";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
        int statusCode = 200;

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(timeStamp);
        accessEvent.setLoggerContext(loggerContext);
        accessEvent.setRequestURI(requestURI);
        accessEvent.setRequestURL(requestURL);
        accessEvent.setRemoteHost(remoteHost);
        accessEvent.setRemoteUser(remoteUser);
        accessEvent.setProtocol(protocol);
        accessEvent.setMethod(method);
        accessEvent.setServerName(serverName);
        accessEvent.setRemoteAddress(remoteAddress);
        accessEvent.setRequestHeaders(requestHeaders);
        accessEvent.setResponseHeaders(responseHeaders);
        accessEvent.setRequestParameters(requestParameters);
        accessEvent.setLocalPort(localPort);
        accessEvent.setStatusCode(statusCode);

        Assert.assertEquals(timeStamp, accessEvent.getTimeStamp());
        Assert.assertEquals(loggerContext, accessEvent.getLoggerContext());
        Assert.assertEquals(requestURI, accessEvent.getRequestURI());
        Assert.assertEquals(requestURL, accessEvent.getRequestURL());
        Assert.assertEquals(remoteHost, accessEvent.getRemoteHost());
        Assert.assertEquals(remoteUser, accessEvent.getRemoteUser());
        Assert.assertEquals(protocol, accessEvent.getProtocol());
        Assert.assertEquals(method, accessEvent.getMethod());
        Assert.assertEquals(serverName, accessEvent.getServerName());
        Assert.assertEquals(remoteAddress, accessEvent.getRemoteAddress());
        Assert.assertEquals(requestHeaders, accessEvent.getRequestHeaders());
        Assert.assertEquals(responseHeaders, accessEvent.getResponseHeaders());
        Assert.assertEquals(requestParameters, accessEvent.getRequestParameters());
        Assert.assertEquals(localPort, accessEvent.getLocalPort());
        Assert.assertEquals(statusCode, accessEvent.getStatusCode());
    }

    @Test
    public void testAccessEventEquals() {
        Long timeStamp1 = 123L;
        Long timeStamp2 = 123L;
        LoggerContext loggerContext1 = new LoggerContext();
        LoggerContext loggerContext2 = new LoggerContext();
        String requestURI1 = "/test";
        String requestURI2 = "/test";
        String requestURL1 = "http://test.com" + requestURI1;
        String requestURL2 = "http://test.com" + requestURI2;
        String remoteHost1 = "127.0.0.1";
        String remoteHost2 = "127.0.0.1";
        String remoteUser1 = "testuser";
        String remoteUser2 = "testuser";
        String protocol1 = "HTTP/1.1";
        String protocol2 = "HTTP/1.1";
        String method1 = "GET";
        String method2 = "GET";
        String serverName1 = "testserver.com";
        String serverName2 = "testserver.com";
        String remoteAddress1 = "127.0.0.1";
        String remoteAddress2 = "127.0.0.1";
        Map<String, String> requestHeaders1 = new HashMap<>();
        Map<String, String> requestHeaders2 = new HashMap<>();
        Map<String, String> responseHeaders1 = new HashMap<>();
        Map<String, String> responseHeaders2 = new HashMap<>();
        Map<String, String[]> requestParameters1 = new HashMap<>();
        Map<String, String[]> requestParameters2 = new HashMap<>();
        int localPort1 = 8080;
        int localPort2 = 8080;
        int statusCode1 = 200;
        int statusCode2 = 200;

        AccessEvent accessEvent1 = new AccessEvent();
        accessEvent1.setTimeStamp(timeStamp1);
        accessEvent1.setLoggerContext(loggerContext1);
        accessEvent1.setRequestURI(requestURI1);
        accessEvent1.setRequestURL(requestURL1);
        accessEvent1.setRemoteHost(remoteHost1);
        accessEvent1.setRemoteUser(remoteUser1);
        accessEvent1.setProtocol(protocol1);
        accessEvent1.setMethod(method1);
        accessEvent1.setServerName(serverName1);
        accessEvent1.setRemoteAddress(remoteAddress1);
        accessEvent1.setRequestHeaders(requestHeaders1);
        accessEvent1.setResponseHeaders(responseHeaders1);
        accessEvent1.setRequestParameters(requestParameters1);
        accessEvent1.setLocalPort(localPort1);
        accessEvent1.setStatusCode(statusCode1);

        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent2.setTimeStamp(timeStamp2);
        accessEvent2.setLoggerContext(loggerContext2);
        accessEvent2.setRequestURI(requestURI2);
        accessEvent2.setRequestURL(requestURL2);
        accessEvent2.setRemoteHost(remoteHost2);
        accessEvent2.setRemoteUser(remoteUser2);
        accessEvent2.setProtocol(protocol2);
        accessEvent2.setMethod(method2);
        accessEvent2.setServerName(serverName2);
        accessEvent2.setRemoteAddress(remoteAddress2);
        accessEvent2.setRequestHeaders(requestHeaders2);
        accessEvent2.setResponseHeaders(responseHeaders2);
        accessEvent2.setRequestParameters(requestParameters2);
        accessEvent2.setLocalPort(localPort2);
        accessEvent2.setStatusCode(statusCode2);

        AccessEvent equalAccessEvent = new AccessEvent();
        equalAccessEvent.setTimeStamp(timeStamp1);
        equalAccessEvent.setLoggerContext(loggerContext1);
        equalAccessEvent.setRequestURI(requestURI1);
        equalAccessEvent.setRequestURL(requestURL1);
        equalAccessEvent.setRemoteHost(remoteHost1);
        equalAccessEvent.setRemoteUser(remoteUser1);
        equalAccessEvent.setProtocol(protocol1);
        equalAccessEvent.setMethod(method1);
        equalAccessEvent.setServerName(serverName1);
        equalAccessEvent.setRemoteAddress(remoteAddress1);
        equalAccessEvent.setRequestHeaders(requestHeaders1);
        equalAccessEvent.setResponseHeaders(responseHeaders1);
        equalAccessEvent.setRequestParameters(requestParameters1);
        equalAccessEvent.setLocalPort(localPort1);
        equalAccessEvent.setStatusCode(statusCode1);

        AccessEvent notEqualAccessEvent1 = new AccessEvent();
        notEqualAccessEvent1.setTimeStamp(timeStamp1);
        notEqualAccessEvent1.setLoggerContext(loggerContext2);
        notEqualAccessEvent1.setRequestURI(requestURI1);
        notEqualAccessEvent1.setRequestURL(requestURL1);
        notEqualAccessEvent1.setRemoteHost(remoteHost1);
        notEqualAccessEvent1.setRemoteUser(remoteUser1);
        notEqualAccessEvent1.setProtocol(protocol2);
        notEqualAccessEvent1.setMethod(method1);
        notEqualAccessEvent1.setServerName(serverName1);
        notEqualAccessEvent1.setRemoteAddress(remoteAddress1);
        notEqualAccessEvent1.setRequestHeaders(requestHeaders1);
        notEqualAccessEvent1.setResponseHeaders(responseHeaders2);
        notEqualAccessEvent1.setRequestParameters(requestParameters1);
        notEqualAccessEvent1.setLocalPort(localPort1);
        notEqualAccessEvent1.setStatusCode(statusCode1);

        AccessEvent notEqualAccessEvent2 = new AccessEvent();
        notEqualAccessEvent2.setTimeStamp(timeStamp1);
        notEqualAccessEvent2.setLoggerContext(loggerContext1);
        notEqualAccessEvent2.setRequestURI(requestURI2);
        notEqualAccessEvent2.setRequestURL(requestURL2);
        notEqualAccessEvent2.setRemoteHost(remoteHost1);
        notEqualAccessEvent2.setRemoteUser(remoteUser1);
        notEqualAccessEvent2.setProtocol(protocol1);
        notEqualAccessEvent2.setMethod(method2);
        notEqualAccessEvent2.setServerName(serverName1);
        notEqualAccessEvent2.setRemoteAddress(remoteAddress2);
        notEqualAccessEvent2.setRequestHeaders(requestHeaders1);
        notEqualAccessEvent2.setResponseHeaders(responseHeaders1);
        notEqualAccessEvent2.setRequestParameters(requestParameters2);
        notEqualAccessEvent2.setLocalPort(localPort2);
        notEqualAccessEvent2.setStatusCode(statusCode2);

        AccessEvent nullAccessEvent = new AccessEvent();
        nullAccessEvent = null;

        Assert.assertTrue(accessEvent1.equals(equalAccessEvent));
        Assert.assertFalse(accessEvent1.equals(notEqualAccessEvent1));
        Assert.assertFalse(accessEvent1.equals(notEqualAccessEvent2));
        Assert.assertFalse(accessEvent1.equals(nullAccessEvent));
    }

    @Test
    public void testAccessEventHashCode() {
        Long timeStamp = 123L;
        LoggerContext loggerContext = new LoggerContext();
        String requestURI = "/test";
        String requestURL = "http://test.com" + requestURI;
        String remoteHost = "127.0.0.1";
        String remoteUser = "testuser";
        String protocol = "HTTP/1.1";
        String method = "GET";
        String serverName = "testserver.com";
        String remoteAddress = "127.0.0.1";
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        Map<String, String[]> requestParameters = new HashMap<>();
        int localPort = 8080;
        int statusCode = 200;

        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(timeStamp);
        accessEvent.setLoggerContext(loggerContext);
        accessEvent.setRequestURI(requestURI);
        accessEvent.setRequestURL(requestURL);
        accessEvent.setRemoteHost(remoteHost);
        accessEvent.setRemoteUser(remoteUser);
        accessEvent.setProtocol(protocol);
        accessEvent.setMethod(method);
        accessEvent.setServerName(serverName);
        accessEvent.setRemoteAddress(remoteAddress);
        accessEvent.setRequestHeaders(requestHeaders);
        accessEvent.setResponseHeaders(responseHeaders);
        accessEvent.setRequestParameters(requestParameters);
        accessEvent.setLocalPort(localPort);
        accessEvent.setStatusCode(statusCode);

        long hashCode = accessEvent.hashCode();

        Assert.assertEquals(hashCode, accessEvent.hashCode());
    }
}