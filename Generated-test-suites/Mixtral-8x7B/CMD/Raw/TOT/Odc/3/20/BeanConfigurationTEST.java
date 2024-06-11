import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

public class BeanConfigurationTest {

@Mock
private SensitivePropertyHandler sensitivePropertyHandler;

private BeanConfiguration beanConfiguration;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
beanConfiguration = new BeanConfiguration();
ReflectionTestUtils.setField(beanConfiguration, "sensitivePropertyHandler", sensitivePropertyHandler);
}

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
assertNotNull(passwordEncoder);
assertEquals(true, passwordEncoder.matches("password", "$2a$10$gFyVeTQXeIFU6/r3.zPOr.lxWxWxh8A1hWQe2tRQ66vV/QZM7mFX6"));
}

@Test
public void testObjectMapper() throws Exception {
ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
assertNotNull(objectMapper);

// Test custom serializer
I18nOutputSerializer i18nOutputSerializer = new I18nOutputSerializer();
ObjectMapper.DefaultTyping typing = ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE;
objectMapper.getDeserializationConfig().withTyping(typing);
objectMapper.getSerializationConfig().withTyping(typing);
objectMapper.addSerializer(I18nOutputSerializer.class, i18nOutputSerializer);

// Test serialization
String serialized = objectMapper.writeValueAsString(new Internationalizable() {
@Override
public String getLocale() {
return "en";
}

@Override
public String getContent() {
return "Hello World!";
}
});

assertEquals("{\"locale\":\"en\",\"content\":\"Hello World!\"}", serialized);
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
assertNotNull(httpFirewall);
assertEquals(true, httpFirewall.shouldDecodeRequest(null, null, "/test%25"));
}

@Test
public void testCloudMetadataClient() {
CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
assertNotNull(cloudMetadataClient);
assertEquals(false, cloudMetadataClient.supportsCloudMetadata());
}

@Test
public void testCookieSerializer() {
CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
assertNotNull(cookieSerializer);
assertEquals("JSESSIONID", cookieSerializer.getCookieName());
}

class NullCloudMetadataClientStub extends NullCloudMetadataClient {
@Override
public List<OBInstance> listInstances() {
List<OBInstance> instances = new ArrayList<>();
instances.add(new OBInstance());
return instances;
}
}

@Test
public void testObjectMapperWithCloudMetadataClient() throws Exception {
ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
assertNotNull(objectMapper);

// Mock CloudMetadataClient
NullCloudMetadataClientStub cloudMetadataClient = new NullCloudMetadataClientStub();
ReflectionTestUtils.setField(beanConfiguration, "cloudMetadataClient", cloudMetadataClient);

// Test serialization
OBInstance obInstance = new OBInstance();
ReflectionTestUtils.setField(obInstance, "id", "123");

OBTenant obTenant = new OBTenant();
ReflectionTestUtils.setField(obTenant, "id", "456");

OBDatabaseUser obDatabaseUser = new OBDatabaseUser();
ReflectionTestUtils.setField(obDatabaseUser, "id", "789");

List<OBInstance> instances = new ArrayList<>();
instances.add(obInstance);

List<OBTenant> tenants = new ArrayList<>();
tenants.add(obTenant);

List<OBDatabaseUser> databaseUsers = new ArrayList<>();
databaseUsers.add(obDatabaseUser);

when(cloudMetadataClient.listInstances()).thenReturn(instances);
when(cloudMetadataClient.listTenants(anyString())).thenReturn(tenants);
when(cloudMetadataClient.listDatabaseUsers(anyString(), anyString())).thenReturn(databaseUsers);

String serialized = objectMapper.writeValueAsString(cloudMetadataClient);

assertNotNull(serialized);
assertEquals(true, serialized.contains("123"));
assertEquals(true, serialized.contains("456"));
assertEquals(true, serialized.contains("789"));
}

@Test(expected = UnsupportedException.class)
public void testGetSysTenantUser() throws Exception {
NullCloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
ReflectionTestUtils.setField(beanConfiguration, "cloudMetadataClient", cloudMetadataClient);

beanConfiguration.getSysTenantUser("test-instance");
}
}