import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.ModelTestUtils;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

 private SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
 private CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
 String password = "test123";
 String encodedPassword = passwordEncoder.encode(password);
 assertNotNull(encodedPassword);
 assertTrue(passwordEncoder.matches(password, encodedPassword));
 }

 @Test
 public void testObjectMapper() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper(JacksonFactory.unsafeJsonMapper());
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 objectMapper.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt));
 objectMapper.registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
 String json = "{\"key\":\"value\"}";
 Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
 });
 assertEquals("value", map.get("key"));
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
 defaultHttpFirewall.setAllowUrlEncodedSlash(true);
 assertNotNull(defaultHttpFirewall.getAllowedUrlEncodedSlash());
 assertTrue(defaultHttpFirewall.getAllowedUrlEncodedSlash());
 }

 @Test
 public void testCloudMetadataClient() {
 when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
 when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
 when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
 when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
 when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
 when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);
 when(cloudMetadataClient.listInstances()).thenReturn(new ArrayList<>());
 when(cloudMetadataClient.listTenants(anyString())).then(new Answer<List<OBTenant>>() {
 @Override
 public List<OBTenant> answer(InvocationOnMock invocation) throws Throwable {
 return new ArrayList<>();
 }
 });
 when(cloudMetadataClient.getTenant(anyString(), anyString())).thenThrow(new UnsupportedException("getTenant not supported in non-could"));
 when(cloudMetadataClient.listDatabaseUsers(anyString(), anyString())).thenReturn(new ArrayList<>());
 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 when(cloudMetadataClient.getTenantEndpoint(anyString(), anyString())).thenReturn(endpoint);
 when(cloudMetadataClient.getSysTenantUser(anyString())).thenThrow(new UnsupportedException("CloudMetadata not supported"));
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = new DefaultCookieSerializer();
 cookieSerializer.setCookieName("JSESSIONID");
 assertEquals("JSESSIONID", cookieSerializer.getCookieName());
 }
}