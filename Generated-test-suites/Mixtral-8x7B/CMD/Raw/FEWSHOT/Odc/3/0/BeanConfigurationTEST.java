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
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

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

private BeanConfiguration beanConfiguration;
private SensitivePropertyHandler sensitivePropertyHandler;
private CloudMetadataClient cloudMetadataClient;

@Before
public void setUp() {
beanConfiguration = new BeanConfiguration();
sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
cloudMetadataClient = mock(CloudMetadataClient.class);
}

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
}

@Test
public void testObjectMapper() {
CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
.addSerializer(Internationalizable.class, new I18nOutputSerializer());
ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
.registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
ObjectMapper result = beanConfiguration.objectMapper(sensitivePropertyHandler);
assertEquals(objectMapper.getClass(), result.getClass());
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
assertEquals(DefaultHttpFirewall.class, defaultHttpFirewall.getClass());
assertEquals(true, defaultHttpFirewall.getAllowUrlEncodedSlash());
}

@Test
public void testCloudMetadataClient() {
CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
assertEquals(NullCloudMetadataClient.class, client.getClass());
}

@Test
public void testCookieSerializer() {
CookieSerializer serializer = beanConfiguration.cookieSerializer();
assertEquals(DefaultCookieSerializer.class, serializer.getClass());
assertEquals("JSESSIONID", serializer.getCookieName());
}

@Test
public void testNullCloudMetadataClient() {
NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
assertEquals(NullCloudMetadataClient.class, client.getClass());
when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(
OceanBaseAccessMode.DIRECT);
when(cloudMetadataClient.listInstances()).thenThrow(
new UnsupportedException("CloudMetadata not supported"));
when(cloudMetadataClient.listTenants("instanceId"))
.thenThrow(new UnsupportedException("CloudMetadata not supported"));
when(cloudMetadataClient.getTenant("instanceId", "tenantId"))
.thenThrow(new UnsupportedException("getTenant not supported in non-cloud"));
when(cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"))
.thenThrow(new UnsupportedException("CloudMetadata not supported"));
when(cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"))
.thenReturn(new OBTenantEndpoint());
when(cloudMetadataClient.getSysTenantUser("instanceId"))
.thenThrow(new UnsupportedException("CloudMetadata not supported"));
client.setCloudMetadataClient(cloudMetadataClient);
client.init();
assertEquals(false, client.supportsCloudMetadata());
assertEquals(false, client.supportsTenantInstance());
assertEquals(false, client.needsOBTenantName());
assertEquals(false, client.needsSysTenantUser());
assertEquals(true, client.includeClusterNameForJdbcUsername());
assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
}

@Test(expected = UnsupportedException.class)
public void testNullCloudMetadataClientListInstances() {
NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
List<OBInstance> instances = new ArrayList<>();
client.setCloudMetadataClient(cloudMetadataClient);
client.listInstances();
}

@Test(expected = UnsupportedException.class)
public void testNullCloudMetadataClientListTenants() {
NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
client.setCloudMetadataClient(cloudMetadataClient);
client.listTenants("instanceId");
}

@Test(expected = UnsupportedException.class)
public void testNullCloudMetadataClientGetTenant() {
NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
client.setCloudMetadataClient(cloudMetadataClient);
client.getTenant("instanceId", "tenantId");
}

@Test(expected = UnsupportedException.class)
public void testNullCloudMetadataClientListDatabaseUsers() {
NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
client.setCloudMetadataClient(cloudMetadataClient);
client.listDatabaseUsers("instanceId", "tenantId");
}

@Test(expected = UnsupportedException.class)
public void testNullCloudMetadataClientGetTenantEndpoint() {
NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
client.setCloudMetadataClient(cloudMetadataClient);
client.getTenantEndpoint("instanceId", "tenantId");
}

@Test(expected = UnsupportedException.class)
public void testNullCloudMetadataClientGetSysTenantUser() {
NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
client.setCloudMetadataClient(cloudMetadataClient);
client.getSysTenantUser("instanceId");
}

@Test
public void testNullCloudMetadataClientNothingToDo() {
NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();
client.setCloudMetadataClient(cloudMetadataClient);
client.nothingToDo();
}
}