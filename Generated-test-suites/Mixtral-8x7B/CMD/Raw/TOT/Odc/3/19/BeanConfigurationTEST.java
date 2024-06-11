import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
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
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoderBean() {
 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
 assertEquals(passwordEncoder.getClass(), BeanConfiguration.passwordEncoder().getClass());
 }

 @Test
 public void testObjectMapperBean() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(s -> {}))
 .registerModule(customOutputSerializer);
 assertEquals(objectMapper.getClass(), BeanConfiguration.objectMapper(mock(SensitivePropertyHandler.class)).getClass());
 }

 @Test
 public void testDefaultHttpFirewallBean() {
 HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
 defaultHttpFirewall.setAllowUrlEncodedSlash(true);
 assertEquals(defaultHttpFirewall.getClass(), BeanConfiguration.defaultHttpFirewall().getClass());
 }

 @Test
 public void testCloudMetadataClientBean() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 }

 @Test
 public void testCookieSerializerBean() {
 CookieSerializer cookieSerializer = new DefaultCookieSerializer();
 cookieSerializer.setCookieName("JSESSIONID");
 assertEquals(cookieSerializer.getClass(), BeanConfiguration.cookieSerializer().getClass());
 }

 @Test
 public void testNullCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
 }
 }

 @Test
 public void testUnsupportedException() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
 }
 }

 @Test
 public void testOBTenantEndpoint() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
 }
 }

 @Test
 public void testOBDatabaseUser() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 OBDatabaseUser user = nullCloudMetadataClient.getSysTenantUser("instanceId");
 assertEquals(null, user);
 }
 }

 @Test
 public void testListDatabaseUsers() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 List<OBDatabaseUser> users = nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
 assertEquals(new ArrayList<OBDatabaseUser>(), users);
 }
 }

 @Test
 public void testListTenants() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 List<OBTenant> tenants = nullCloudMetadataClient.listTenants("instanceId");
 assertEquals(new ArrayList<OBTenant>(), tenants);
 }
 }

 @Test
 public void testListInstances() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 List<OBInstance> instances = nullCloudMetadataClient.listInstances();
 assertEquals(new ArrayList<OBInstance>(), instances);
 }
 }

 @Test
 public void testGetTenant() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant(anyString(), anyString()));
 }
 }

 @Test
 public void testIncludeClusterNameForJdbcUsername() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
 }
 }

 @Test
 public void testOceanbaseAccessMode() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
 }
 }

 @Test
 public void testNeedsSysTenantUser() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
 }
 }

 @Test
 public void testNeedsOBTenantName() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
 }
 }

 @Test
 public void testSupportsTenantInstance() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
 }
 }

 @Test
 public void testSupportsCloudMetadata() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 if (cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient) {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient;
 assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
 }
 }
}