import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

public class BeanConfigurationTest {

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;

 private BeanConfiguration beanConfiguration;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 beanConfiguration = new BeanConfiguration();
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 assertEquals(true, passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapper() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
 .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 assertEquals(true, defaultHttpFirewall instanceof DefaultHttpFirewall);
 ((DefaultHttpFirewall) defaultHttpFirewall).setAllowUrlEncodedSlash(true);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 assertEquals(true, cloudMetadataClient instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 assertEquals("JSESSIONID", cookieSerializer.getCookieName());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertNotNull(nullCloudMetadataClient);
 }

 @Test
 public void testUnsupportedException() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertThrows(UnsupportedException.class,
 () -> cloudMetadataClient.listInstances());
 }

 @Test
 public void testGetTenantEndpoint() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 when(cloudMetadataClient.getTenantEndpoint(anyString(), anyString()))
 .thenReturn(endpoint);
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient(
 cloudMetadataClient);
 assertNotNull(nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
 }

 @Test
 public void testListDatabaseUsers() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 List<OBDatabaseUser> obDatabaseUsers = new ArrayList<>();
 when(cloudMetadataClient.listDatabaseUsers(anyString(), anyString()))
 .thenReturn(obDatabaseUsers);
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient(
 cloudMetadataClient);
 assertNotNull(nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
 }

 @Test
 public void testListTenants() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 List<OBTenant> obTenants = new ArrayList<>();
 when(cloudMetadataClient.listTenants(anyString()))
 .thenReturn(obTenants);
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient(
 cloudMetadataClient);
 assertNotNull(nullCloudMetadataClient.listTenants("instanceId"));
 }

 @Test
 public void testListInstances() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 List<OBInstance> obInstances = new ArrayList<>();
 when(cloudMetadataClient.listInstances())
 .thenReturn(obInstances);
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient(
 cloudMetadataClient);
 assertNotNull(nullCloudMetadataClient.listInstances());
 }

 @Test
 public void testGetTenant() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 OBTenant obTenant = new OBTenant();
 when(cloudMetadataClient.getTenant(anyString(), anyString()))
 .thenReturn(obTenant);
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient(
 cloudMetadataClient);
 assertNotNull(nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
 }

 @Test
 public void testGetSysTenantUser() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 when(cloudMetadataClient.getSysTenantUser(anyString()))
 .thenReturn(null);
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient(
 cloudMetadataClient);
 assertNull(nullCloudMetadataClient.getSysTenantUser("instanceId"));
 }

 @Test
 public void testSupportsCloudMetadata() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testSupportsTenantInstance() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
 }

 @Test
 public void testNeedsOBTenantName() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
 }

 @Test
 public void testNeedsSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
 }

 @Test
 public void testIncludeClusterNameForJdbcUsername() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
 }

 @Test
 public void testOceanbaseAccessMode() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
 }
}