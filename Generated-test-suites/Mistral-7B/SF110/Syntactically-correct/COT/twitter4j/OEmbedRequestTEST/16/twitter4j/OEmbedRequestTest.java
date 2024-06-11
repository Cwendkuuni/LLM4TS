package twitter4j; // make sure the test package matches the production package

import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;

public class OEmbedRequestTest {

 private final long validStatusId = 1234567890L;
 private final String validUrl = "https://example.com";
 private final int validMaxWidth = 400;
 private final boolean hideMediaDefault = true;
 private final boolean hideThreadDefault = true;
 private final boolean omitScriptDefault = false;
 private final Align alignDefault = Align.NONE;
 private final String[] relatedDefault = new String[0];
 private final String validLang = "en";

 @Test
 public void testOEmbedRequestConstruction() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(validStatusId, validUrl);
 assertEquals(validStatusId, oEmbedRequest.getStatusId());
 assertEquals(validUrl, oEmbedRequest.getUrl());
 assertTrue(oEmbedRequest.isHideMedia());
 assertTrue(oEmbedRequest.isHideThread());
 assertFalse(oEmbedRequest.isOmitScript());
 assertNull(oEmbedRequest.getAlign());
 assertArrayEquals(relatedDefault, oEmbedRequest.getRelated());
 assertNull(oEmbedRequest.getLang());
 }

 @Test
 public void testSettersAndGetters() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(validStatusId, validUrl);
 oEmbedRequest.setMaxWidth(validMaxWidth);
 OEmbedRequest maxWidthSetter = oEmbedRequest.MaxWidth(validMaxWidth);
 assertEquals(validMaxWidth, maxWidthSetter.getMaxWidth());

 oEmbedRequest.setHideMedia(false);
 OEmbedRequest hideMediaSetter = oEmbedRequest.HideMedia(false);
 assertFalse(hideMediaSetter.isHideMedia());

 oEmbedRequest.setHideThread(false);
 OEmbedRequest hideThreadSetter = oEmbedRequest.HideThread(false);
 assertFalse(hideThreadSetter.isHideThread());

 oEmbedRequest.setOmitScript(true);
 OEmbedRequest omitScriptSetter = oEmbedRequest.omitScript(true);
 assertTrue(omitScriptSetter.isOmitScript());

 oEmbedRequest.setAlign(Align.LEFT);
 OEmbedRequest alignSetter = oEmbedRequest.align(Align.LEFT);
 assertEquals(Align.LEFT, alignSetter.getAlign());

 String[] relatedArray = {"related1", "related2"};
 oEmbedRequest.setRelated(relatedArray);
 OEmbedRequest relatedSetter = oEmbedRequest.related(relatedArray);
 assertArrayEquals(relatedArray, relatedSetter.getRelated());

 oEmbedRequest.setLang(validLang);
 OEmbedRequest langSetter = oEmbedRequest.lang(validLang);
 assertEquals(validLang, langSetter.getLang());
 }

 @Test
 public void testAsHttpParameterArray() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(validStatusId, validUrl);
 HttpParameter[] httpParameters = oEmbedRequest.asHttpParameterArray();
 assertEquals(12, httpParameters.length);

 // Testing for id and url parameters
 HttpParameter idParameter = null;
 HttpParameter urlParameter = null;
 for (HttpParameter httpParameter : httpParameters) {
 if ("id".equals(httpParameter.getName())) {
 idParameter = httpParameter;
 } else if ("url".equals(httpParameter.getName())) {
 urlParameter = httpParameter;
 }
 }
 assertTrue(idParameter != null);
 assertTrue(urlParameter != null);
 assertEquals(validStatusId, Long.parseLong(idParameter.getValue()));
 assertEquals(validUrl, urlParameter.getValue());

 // Testing for other parameters
 assertEquals(validMaxWidth, oEmbedRequest.getMaxWidth());
 assertTrue(oEmbedRequest.isHideMedia());
 assertTrue(oEmbedRequest.isHideThread());
 assertFalse(oEmbedRequest.isOmitScript());
 assertNull(oEmbedRequest.getAlign());
 assertArrayEquals(relatedDefault, oEmbedRequest.getRelated());
 assertNull(oEmbedRequest.getLang());

 for (HttpParameter httpParameter : httpParameters) {
 if ("maxwidth".equals(httpParameter.getName())) {
 assertEquals(Integer.toString(validMaxWidth), httpParameter.getValue());
 } else if ("hide_media".equals(httpParameter.getName())) {
 assertEquals(Boolean.toString(oEmbedRequest.isHideMedia()), httpParameter.getValue());
 } else if ("hide_thread".equals(httpParameter.getName())) {
 assertEquals(Boolean.toString(oEmbedRequest.isHideThread()), httpParameter.getValue());
 } else if ("omit_script".equals(httpParameter.getName())) {
 assertEquals(Boolean.toString(oEmbedRequest.isOmitScript()), httpParameter.getValue());
 } else if ("align".equals(httpParameter.getName())) {
 assertEquals(alignDefault.name().toLowerCase(), httpParameter.getValue());
 } else if ("related".equals(httpParameter.getName())) {
 String joinedRelated = StringUtil.join(oEmbedRequest.getRelated());
 assertEquals(joinedRelated, httpParameter.getValue());
 } else if ("lang".equals(httpParameter.getName())) {
 assertEquals(validLang, httpParameter.getValue());
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWhenValueIsNull() {
 List<HttpParameter> params = new ArrayList<>();
 OEmbedRequest oEmbedRequest = new OEmbedRequest(validStatusId, validUrl);
 oEmbedRequest.appendParameter("name", null, params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWhenNameIsEmpty() {
 List<HttpParameter> params = new ArrayList<>();
 OEmbedRequest oEmbedRequest = new OEmbedRequest(validStatusId, validUrl);
 oEmbedRequest.appendParameter("", null, params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWhenNameIsNull() {
 List<HttpParameter> params = new ArrayList<>();
 OEmbedRequest oEmbedRequest = new OEmbedRequest(validStatusId, validUrl);
 oEmbedRequest.appendParameter(null, "value", params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWhenValueIsEmpty() {
 List<HttpParameter> params = new ArrayList<>();
 OEmbedRequest oEmbedRequest = new OEmbedRequest(validStatusId, validUrl);
 oEmbedRequest.appendParameter("name", "", params);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGettersWhenMaxWidthIsNegative() {
 OEmbedRequest oEmbedRequest = new OEmbedRequest(validStatusId, validUrl);
 oEmbedRequest.setMaxWidth(-1);
 }

 @Test
 public void testHashCodeAndEquals() {
 OEmbedRequest oEmbedRequest1 = new OEmbedRequest(validStatusId, validUrl);
 OEmbedRequest oEmbedRequest2 = new OEmbedRequest(validStatusId, validUrl);

 assertEquals(oEmbedRequest1, oEmbedRequest2);
 assertTrue(oEmbedRequest1.hashCode() == oEmbedRequest2.hashCode());

 OEmbedRequest oEmbedRequest3 = new OEmbedRequest(validStatusId + 1, validUrl);
 assertFalse(oEmbedRequest1.equals(oEmbedRequest3));
 assertFalse(oEmbedRequest3.equals(oEmbedRequest1));
 }
}