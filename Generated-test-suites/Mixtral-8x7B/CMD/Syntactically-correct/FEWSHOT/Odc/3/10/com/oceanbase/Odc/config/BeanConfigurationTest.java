import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactories;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

@Mock
private SensitivePropertyHandler sensitivePropertyHandler;

@InjectMocks
private BeanConfiguration beanConfiguration;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
}

@Test
public void testObjectMapper() {
ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
CustomOutputSerializer customOutputSerializer = (CustomOutputSerializer) ReflectionTestUtils.getField(objectMapper,
"customOutputSerializer");
assertFalse(customOutputSerializer.getSerializers().isEmpty());
assertEquals(1, customOutputSerializer.getSerializers().size());
assertEquals(I18nOutputSerializer.class, customOutputSerializer.getSerializers().get(0).getKey().getClass());
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
assertTrue(httpFirewall instanceof DefaultHttpFirewall);
assertTrue(((DefaultHttpFirewall) httpFirewall).isAllowUrlEncodedSlash());
}

@Test
public void testCloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
assertFalse(cloudMetadataClient.supportsCloudMetadata());
assertFalse(cloudMetadataClient.supportsTenantInstance());
assertFalse(cloudMetadataClient.needsOBTenantName());
assertFalse(cloudMetadataClient.needsSysTenantUser());
assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
}

@Test
public void testCookieSerializer() {
CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
assertEquals("JSESSIONID", cookieSerializer.getCookieName());
}

@Test
public void testNullCloudMetadataClient() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
assertFalse(nullCloudMetadataClient.supportsTenantInstance());
assertFalse(nullCloudMetadataClient.needsOBTenantName());
assertFalse(nullCloudMetadataClient.needsSysTenantUser());
assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
assertThrows(UnsupportedException.class,
()-> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
}

}