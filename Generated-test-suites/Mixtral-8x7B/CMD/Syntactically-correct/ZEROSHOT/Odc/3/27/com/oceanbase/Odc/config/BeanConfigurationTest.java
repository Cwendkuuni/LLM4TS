import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
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

public class BeanConfigurationTest {

private BeanConfiguration beanConfiguration;

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
assertNotNull(passwordEncoder);
assertEquals(true, passwordEncoder instanceof BCryptPasswordEncoder);
}

@Test
public void testObjectMapper() {
ObjectMapper objectMapper = beanConfiguration.objectMapper(mock(SensitivePropertyHandler.class));
assertNotNull(objectMapper);
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
assertNotNull(defaultHttpFirewall);
assertEquals(true, defaultHttpFirewall instanceof DefaultHttpFirewall);
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

}

class NullCloudMetadataClient implements CloudMetadataClient {

@Override
public boolean supportsCloudMetadata() {
return false;
}

@Override
public boolean supportsTenantInstance() {
return false;
}

@Override
public boolean needsOBTenantName() {
return false;
}

@Override
public boolean needsSysTenantUser() {
return false;
}

@Override
public boolean includeClusterNameForJdbcUsername() {
return true;
}

@Override
public OceanBaseAccessMode oceanbaseAccessMode() {
return OceanBaseAccessMode.DIRECT;
}

@Override
public List<OBInstance> listInstances() {
throw new UnsupportedException("CloudMetadata not supported");
}

@Override
public List<OBTenant> listTenants(String instanceId) {
throw new UnsupportedException("CloudMetadata not supported");
}

@Override
public OBTenant getTenant(String instanceId, String tenantId) {
throw new UnsupportedException("getTenant not supported in non-cloud");
}

@Override
public List<OBDatabaseUser> listDatabaseUsers(String instanceId, String tenantId) {
throw new UnsupportedException("CloudMetadata not supported");
}

@Override
public OBTenantEndpoint getTenantEndpoint(String instanceId, String tenantId) {
OBTenantEndpoint endpoint = new OBTenantEndpoint();
endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
return endpoint;
}

@Override
public OBDatabaseUser getSysTenantUser(String instanceId) {
throw new UnsupportedException("CloudMetadata not supported");
}

}

class TestBeanConfiguration extends BeanConfiguration {

@Override
protected SensitivePropertyHandler getSensitivePropertyHandler() {
return null;
}

}