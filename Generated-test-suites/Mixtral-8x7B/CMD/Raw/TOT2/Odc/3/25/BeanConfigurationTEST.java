import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

    private HttpFirewall httpFirewall;

    private CloudMetadataClient cloudMetadataClient;

    private CookieSerializer cookieSerializer;

    @Before
    public void setup() {
        passwordEncoder = new BCryptPasswordEncoder();

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());

        objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

        httpFirewall = new DefaultHttpFirewall();
        httpFirewall.setAllowUrlEncodedSlash(true);

        cloudMetadataClient = new NullCloudMetadataClient();

        cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookieName("JSESSIONID");
    }

    @Test
    public void testPasswordEncoder() {
        String rawPassword = "test";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        assertEquals(true, matches);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(objectMapper.canSerialize(I18nOutputSerializer.class));
    }

    @Test
    public void testHttpFirewall() {
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
        assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testEdgeCaseListTenants() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        List<OBTenant> tenants = new ArrayList<>();
        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.listTenants("instanceId", "tenantId", tenants));
    }

    @Test
    public void testErrorCaseGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.getTenant("invalidInstanceId", "invalidTenantId"));
    }

    @Test
    public void testErrorCaseListDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.listDatabaseUsers("invalidInstanceId", "invalidTenantId"));
    }

    @Test
    public void testEdgeCaseGetTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("anyInstanceId", "anyTenantId");
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }

    @Test
    public void testErrorCaseGetSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.getSysTenantUser("invalidInstanceId"));
    }
}