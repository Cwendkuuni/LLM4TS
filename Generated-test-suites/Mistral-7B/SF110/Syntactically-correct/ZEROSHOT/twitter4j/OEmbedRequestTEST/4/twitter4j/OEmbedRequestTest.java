package twitter4j.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import twitter4j.Align;
import twitter4j.OEmbedRequest;
import twitter4j.HttpParameter;

public class OEmbedRequestTest {

    @Test
    public void testOEmbedRequestInstance() {
        Long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        assertEquals(statusId, request.getStatusId());
        assertEquals(url, request.getUrl());
        assertTrue(request.isHideMedia());
        assertTrue(request.isHideThread());
        assertFalse(request.isOmitScript());
        assertNull(request.getAlign());
        assertArrayEquals(new String[0], request.getRelated());
        assertNull(request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        Long statusId = 1L;
        String url = "http://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        request.setMaxWidth(100);
        request.MaxWidth(200);
        assertEquals(200, request.getMaxWidth());

        request.setHideMedia(false);
        request.HideMedia(true);
        assertTrue(request.isHideMedia());

        request.setHideThread(false);
        request.HideThread(true);
        assertTrue(request.isHideThread());

        request.setOmitScript(true);
        request.omitScript(false);
        assertFalse(request.isOmitScript());

        Align align = Align.LEFT;
        request.setAlign(align);
        request.align(Align.RIGHT);
        assertEquals(Align.RIGHT, request.getAlign());

        String[] related = new String[]{"related1", "related2"};
        request.setRelated(related);
        request.related(new String[]{"related3", "related4"});
        assertArrayEquals(new String[]{"related3", "related4"}, request.getRelated());

        String lang = "en";
        request.setLang(lang);
        request.lang("fr");
        assertEquals("fr", request.getLang());

        HttpParameter[] httpParameters = request.asHttpParameterArray();
        List<HttpParameter> httpParameterList = Arrays.asList(httpParameters);
        List<String> names = Collections.singletonList("id");
        List<String> values = Arrays.asList(String.valueOf(statusId));
        List<HttpParameter> expectedHttpParameters = createHttpParameters(names, values);
        assertEquals(expectedHttpParameters, httpParameterList);
    }

    @Test
    public void testEqualsHashcode() {
        Long statusId1 = 1L;
        String url1 = "http://example.com";
        OEmbedRequest request1 = new OEmbedRequest(statusId1, url1);

        Long statusId2 = 2L;
        String url2 = "http://example.com";
        OEmbedRequest request2 = new OEmbedRequest(statusId2, url1);

        Long statusId3 = 1L;
        String url3 = "http://example.com";
        OEmbedRequest request3 = new OEmbedRequest(statusId3, url1);

        Long statusId4 = 1L;
        String url4 = "http://example.com";
        OEmbedRequest request4 = new OEmbedRequest(statusId4, url2);

        OEmbedRequest request5 = new OEmbedRequest(statusId1, url1);

        OEmbedRequest request6 = new OEmbedRequest(statusId1, url1);
        request6.setMaxWidth(100);
        request6.MaxWidth(200);
        OEmbedRequest request7 = new OEmbedRequest(statusId1, url1);
        request7.setMaxWidth(200);
        OEmbedRequest request8 = new OEmbedRequest(statusId1, url1);
        request8.setMaxWidth(100);

        OEmbedRequest request9 = new OEmbedRequest(statusId1, url2);

        List<HttpParameter> httpParameters1 = request1.asHttpParameterArrayList();
        List<HttpParameter> httpParameters2 = request2.asHttpParameterArrayList();
        List<HttpParameter> httpParameters3 = request3.asHttpParameterArrayList();
        List<HttpParameter> httpParameters4 = request4.asHttpParameterArrayList();
        List<HttpParameter> httpParameters5 = request5.asHttpParameterArrayList();
        List<HttpParameter> httpParameters6 = request6.asHttpParameterArrayList();
        List<HttpParameter> httpParameters7 = request7.asHttpParameterArrayList();
        List<HttpParameter> httpParameters8 = request8.asHttpParameterArrayList();
        List<HttpParameter> httpParameters9 = request9.asHttpParameterArrayList();

        List<String> names1 = Arrays.asList("id");
        List<String> values1 = Arrays.asList(String.valueOf(statusId1));
        List<HttpParameter> expectedHttpParameters1 = createHttpParameters(names1, values1);

        assertEquals(request1, request1);
        assertEquals(request1, request5);
        assertEquals(request1, request6);
        assertEquals(request1, request7);
        assertEquals(request1, request8);
        assertNotEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, request4);
        assertNotEquals(request1, request9);

        assertEquals(request1.hashCode(), request1.hashCode());
        assertEquals(request1.hashCode(), request5.hashCode());
        assertEquals(request1.hashCode(), request6.hashCode());
        assertEquals(request1.hashCode(), request7.hashCode());
        assertEquals(request1.hashCode(), request8.hashCode());
        assertNotEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
        assertNotEquals(request1.hashCode(), request4.hashCode());
        assertNotEquals(request1.hashCode(), request9.hashCode());

        assertEquals(httpParameters1, expectedHttpParameters1);
        assertEquals(httpParameters1, httpParameters5.subList(0, 1));
        assertEquals(httpParameters1, httpParameters6.subList(0, 1));
        assertEquals(httpParameters1, httpParameters7.subList(0, 1));
        assertEquals(httpParameters1, httpParameters8.subList(0, 1));
        assertNotEquals(httpParameters1, httpParameters2);
        assertNotEquals(httpParameters1, httpParameters3);
        assertNotEquals(httpParameters1, httpParameters4);
        assertNotEquals(httpParameters1, httpParameters9);
    }

    @Test
    public void testToString() {
        Long statusId = 1L;
        String url = "http://example.com";
        String expectedString = "OEmbedRequest{statusId=1, url='http://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang=''}";
        OEmbedRequest request = new OEmbedRequest(statusId, url);
        String actualString = request.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testAlignEnum() {
        Align align = Align.LEFT;
        String expectedString = "ALIGN=left";
        OEmbedRequest request = new OEmbedRequest(1L, "http://example.com");
        request.setAlign(align);
        HttpParameter[] httpParameters = request.asHttpParameterArray();
        for (HttpParameter httpParameter : httpParameters) {
            if ("align".equals(httpParameter.getName())) {
                assertEquals(expectedString, httpParameter.toString());
                return;
            }
        }
        fail("Expected align parameter not found.");
    }

    private List<HttpParameter> createHttpParameters(List<String> names, List<String> values) {
        List<HttpParameter> httpParameters = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            String value = values.get(i);
            HttpParameter httpParameter = new HttpParameter(name, value);
            httpParameters.add(httpParameter);
        }
        return httpParameters;
    }

    private List<HttpParameter> asHttpParameterArrayList(HttpParameter[] httpParameters) {
        return Arrays.asList(httpParameters);
    }

    private String[] asStringArray(List<String> strings) {
        return strings.toArray(new String[0]);
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (expected == null) {
            assertEquals(null, actual);
        } else if (expected.getClass().isArray()) {
            assertArrayEquals((Object[])expected, (Object[])actual);
        } else {
            assertEquals(expected.toString(), actual.toString());
        }
    }
}