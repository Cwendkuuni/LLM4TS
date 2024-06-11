import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

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

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Test
    public void testPasswordEncoderBean() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapperBean() throws Exception {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = (CustomOutputSerializer) ReflectionTestUtils.getField(
                objectMapper, "customOutputSerializer");
        assertNotNull(customOutputSerializer);

        Class<?> internationalizableClass = Class.forName("com.oceanbase.odc.common.i18n.Internationalizable");
        assertTrue(customOutputSerializer.getSerializers().containsKey(internationalizableClass));

        I18nOutputSerializer i18nOutputSerializer = (I18nOutputSerializer) customOutputSerializer.getSerializers()
                .get(internationalizableClass);
        assertNotNull(i18nOutputSerializer);
    }

    @Test
    public void testDefaultHttpFirewallBean() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);

        DefaultHttpFirewall defaultHttpFirewall = (DefaultHttpFirewall) httpFirewall;
        assertTrue(defaultHttpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClientBean() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializerBean() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);

        DefaultCookieSerializer defaultCookieSerializer = (DefaultCookieSerializer) cookieSerializer;
        assertEquals("JSESSIONID", defaultCookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();

        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        List<OBInstance> obInstances = new ArrayList<>();
        when(sensitivePropertyHandler.decrypt("test")).thenReturn("decryptedTest");
        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(
                        JacksonModules.customOutputHandling(new CustomOutputSerializer().addSerializer(
                                Internationalizable.class, new I18nOutputSerializer())));
        ReflectionTestUtils.setField(nullCloudMetadataClient, "objectMapper", objectMapper);

        try {
            nullCloudMetadataClient.listInstances();
            nullCloudMetadataClient.listTenants("testInstanceId");
            nullCloudMetadataClient.getTenant("testInstanceId", "testTenantId");
            nullCloudMetadataClient.listDatabaseUsers("testInstanceId", "testTenantId");
            nullCloudMetadataClient.getTenantEndpoint("testInstanceId", "testTenantId");
            nullCloudMetadataClient.getSysTenantUser("testInstanceId");
        } catch (UnsupportedException e) {
            // expected
        }
    }
}