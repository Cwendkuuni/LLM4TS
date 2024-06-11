import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

@Mock
private SensitivePropertyHandler sensitivePropertyHandler;

private BeanConfiguration beanConfiguration;

@Before
public void setUp() {
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
public void testObjectMapper() throws Exception {
ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
assertNotNull(objectMapper);

CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
.addSerializer(Internationalizable.class, new I18nOutputSerializer());

assertEquals(true, objectMapper.getClass().equals(JacksonFactory.unsafeJsonMapper().getClass()));
assertEquals(true, objectMapper.getSerializerProvider().getSerializers().findSerializer(Internationalizable.class)
.getClass()
.equals(new I18nOutputSerializer().getClass()));
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
assertNotNull(defaultHttpFirewall);
assertEquals(true, defaultHttpFirewall instanceof DefaultHttpFirewall);
assertEquals(true, defaultHttpFirewall.getAllowUrlEncodedSlash());
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
assertEquals(true, cookieSerializer instanceof DefaultCookieSerializer);
assertEquals("JSESSIONID", cookieSerializer.getCookieName());
}

@Test
public void testNullCloudMetadataClient() {
NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
assertNotNull(nullCloudMetadataClient);
assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

List<OBInstance> instances = new ArrayList<>();
OBInstance obInstance = new OBInstance();
obInstance.setId("testInstanceId");
instances.add(obInstance);

assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("testInstanceId"));
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("testInstanceId", "testTenantId"));
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("testInstanceId", "testTenantId"));
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenantEndpoint("testInstanceId", "testTenantId"));
assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("testInstanceId"));
}
}