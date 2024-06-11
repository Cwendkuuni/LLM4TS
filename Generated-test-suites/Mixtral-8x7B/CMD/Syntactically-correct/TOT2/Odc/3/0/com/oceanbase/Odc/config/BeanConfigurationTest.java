import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

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
 assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapper() throws Exception {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);

 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());

 assertEquals(objectMapper.getSerializerProvider().getActiveView(), customOutputSerializer);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(httpFirewall);
 assertTrue(httpFirewall instanceof DefaultHttpFirewall);

 DefaultHttpFirewall defaultHttpFirewall = (DefaultHttpFirewall) httpFirewall;
 assertTrue(defaultHttpFirewall.getAllowUrlEncodedSlash());
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 assertTrue(cookieSerializer instanceof DefaultCookieSerializer);

 DefaultCookieSerializer defaultCookieSerializer = (DefaultCookieSerializer) cookieSerializer;
 assertEquals("JSESSIONID", defaultCookieSerializer.getCookieName());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertNotNull(nullCloudMetadataClient);
 }

 @Test
 public void testNullCloudMetadataClientSupportsCloudMetadata() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testNullCloudMetadataClientSupportsTenantInstance() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertFalse(nullCloudMetadataClient.supportsTenantInstance());
 }

 @Test
 public void testNullCloudMetadataClientNeedsOBTenantName() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertFalse(nullCloudMetadataClient.needsOBTenantName());
 }

 @Test
 public void testNullCloudMetadataClientNeedsSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertFalse(nullCloudMetadataClient.needsSysTenantUser());
 }

 @Test
 public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
 }

 @Test
 public void testNullCloudMetadataClientOceanbaseAccessMode() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
 }

 @Test
 public void testNullCloudMetadataClientListInstances() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
 }

 @Test
 public void testNullCloudMetadataClientListTenants() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
 }

 @Test
 public void testNullCloudMetadataClientGetTenant() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
 }

 @Test
 public void testNullCloudMetadataClientListDatabaseUsers() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
 }

 @Test
 public void testNullCloudMetadataClientGetTenantEndpoint() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");

 assertNotNull(endpoint);
 assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
 }

 @Test
 public void testNullCloudMetadataClientGetSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
 }
}