import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;

 private BeanConfiguration beanConfiguration;
 private PasswordEncoder passwordEncoder;
 private ObjectMapper objectMapper;
 private HttpFirewall httpFirewall;
 private CloudMetadataClient cloudMetadataClient;
 private CookieSerializer cookieSerializer;

 @Before
 public void setUp() {
 passwordEncoder = new BCryptPasswordEncoder();
 objectMapper = mock(ObjectMapper.class);
 httpFirewall = mock(DefaultHttpFirewall.class);
 cloudMetadataClient = mock(CloudMetadataClient.class);
 cookieSerializer = mock(DefaultCookieSerializer.class);

 beanConfiguration = new BeanConfiguration();
 beanConfiguration.setPasswordEncoder(passwordEncoder);
 beanConfiguration.setObjectMapper(objectMapper);
 beanConfiguration.setHttpFirewall(httpFirewall);
 beanConfiguration.setCloudMetadataClient(cloudMetadataClient);
 beanConfiguration.setCookieSerializer(cookieSerializer);
 }

 @Test
 public void testPasswordEncoder() {
 String rawPassword = "test";
 String encodedPassword = passwordEncoder.encode(rawPassword);

 assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
 }

 @Test
 public void testObjectMapper() throws Exception {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());

 when(objectMapper.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt)))
 .thenReturn(objectMapper);
 when(objectMapper.registerModule(JacksonModules.customOutputHandling(customOutputSerializer))).thenReturn(objectMapper);

 beanConfiguration.objectMapper(sensitivePropertyHandler);
 }

 @Test
 public void testHttpFirewall() {
 when(httpFirewall.setAllowUrlEncodedSlash(true)).thenReturn(httpFirewall);

 beanConfiguration.defaultHttpFirewall();
 }

 @Test
 public void testCloudMetadataClient() {
 when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
 when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
 when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
 when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
 when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
 when(cloudMetadataClient.oceanbaseAccessMode())
 .thenReturn(OceanBaseAccessMode.DIRECT);

 List<OBInstance> instances = new ArrayList<>();
 OBInstance instance = new OBInstance();
 instances.add(instance);
 when(cloudMetadataClient.listInstances()).thenReturn(instances);

 List<OBTenant> tenants = new ArrayList<>();
 OBTenant tenant = new OBTenant();
 tenants.add(tenant);
 when(cloudMetadataClient.listTenants("instanceId"))
 .thenReturn(tenants);

 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 when(cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"))
 .thenReturn(endpoint);

 when(cloudMetadataClient.getSysTenantUser("instanceId"))
 .thenThrow(new UnsupportedException("CloudMetadata not supported"));

 beanConfiguration.cloudMetadataClient();
 }

 @Test
 public void testCookieSerializer() {
 when(cookieSerializer.setCookieName("JSESSIONID")).thenReturn(cookieSerializer);

 beanConfiguration.cookieSerializer();
 }
}