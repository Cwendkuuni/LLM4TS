import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

@InjectMocks
private BeanConfiguration beanConfiguration;

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
assertNotNull(passwordEncoder);
assertEquals(true, passwordEncoder instanceof BCryptPasswordEncoder);
}

@Test
public void testObjectMapper() {
ObjectMapper objectMapper = beanConfiguration.objectMapper(new SensitivePropertyHandler());
assertNotNull(objectMapper);
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
assertNotNull(httpFirewall);
assertEquals(true, httpFirewall instanceof DefaultHttpFirewall);
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
public void testNullCloudMetadataClientSupportsCloudMetadata() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
}

@Test
public void testNullCloudMetadataClientSupportsTenantInstance() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
}

@Test
public void testNullCloudMetadataClientNeedsOBTenantName() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
}

@Test
public void testNullCloudMetadataClientNeedsSysTenantUser() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
}

@Test
public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
}

@Test
public void testNullCloudMetadataClientOceanbaseAccessMode() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
}

@Test
public void testListInstances() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
}

@Test
public void testListTenants() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
}

@Test
public void testGetTenant() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
}

@Test
public void testListDatabaseUsers() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
}

@Test
public void testGetTenantEndpoint() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
assertNotNull(endpoint);
assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
}

@Test
public void testGetSysTenantUser() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
}

}