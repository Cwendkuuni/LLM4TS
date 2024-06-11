import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

 private PasswordEncoder passwordEncoder;
 private SensitivePropertyHandler sensitivePropertyHandler;
 private ObjectMapper objectMapper;
 private HttpFirewall defaultHttpFirewall;
 private CloudMetadataClient cloudMetadataClient;
 private CookieSerializer cookieSerializer;

 @Before
 public void setup() {
 passwordEncoder = new BCryptPasswordEncoder();
 sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
 objectMapper = JacksonFactory.unsafeJsonMapper();
 defaultHttpFirewall = new DefaultHttpFirewall();
 cloudMetadataClient = mock(CloudMetadataClient.class);
 cookieSerializer = new DefaultCookieSerializer();
 }

 @Test
 public void testPasswordEncoder() {
 assertEquals(true, passwordEncoder.matches("password", "$2a$10$l5Up7z8U10NE/GGw2yKpHehZTnG/K8mQhZ7jymXeJzWvNqfq0cQaC"));
 }

 @Test
 public void testObjectMapper() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 objectMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
 .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
 }

 @Test
 public void testDefaultHttpFirewall() {
 defaultHttpFirewall.setAllowUrlEncodedSlash(true);
 assertEquals(true, defaultHttpFirewall.getAllowUrlEncodedSlash());
 }

 @Test
 public void testCloudMetadataClient() {
 when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
 when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
 when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
 when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
 when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
 when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);
 when(cloudMetadataClient.listInstances()).thenReturn(new ArrayList<OBInstance>());
 when(cloudMetadataClient.listTenants("instanceId")).thenReturn(new ArrayList<OBTenant>());
 when(cloudMetadataClient.getTenant("instanceId", "tenantId")).thenThrow(new UnsupportedException("getTenant not supported in non-cloud"));
 when(cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId")).thenReturn(new ArrayList<OBDatabaseUser>());
 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 when(cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId")).thenReturn(endpoint);
 when(cloudMetadataClient.getSysTenantUser("instanceId")).thenThrow(new UnsupportedException("CloudMetadata not supported"));
 }

 @Test
 public void testCookieSerializer() {
 cookieSerializer.setCookieName("JSESSIONID");
 assertEquals("JSESSIONID", cookieSerializer.getCookieName());
 }

 @Test
 public void testNullCloudMetadataClient() {
 // This is just a placeholder for the NullCloudMetadataClient class, it doesn't contain any testable methods
 }
}