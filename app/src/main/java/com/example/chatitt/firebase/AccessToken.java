package com.example.chatitt.firebase;

import static com.example.chatitt.ultilities.Constants.TAG;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AccessToken {
    private static final String SCOPES = "https://www.googleapis.com/auth/firebase.messaging";

    private static final String jsonString = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"chatitt-5fe8f\",\n" +
            "  \"private_key_id\": \"83718dab04b348792358ce2d3cf4b3ad1dc3c628\",\n" +
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCaBUJ1VqD9qu6L\\nuqjqHC1yPeDtLgaEAQ/kjz/axhn7EluYIBVF+mCrt6GdvmnYGkEt5PMSTtt76lqL\\ncRWvxMjwIrdFp/X8R2D6BPr9TaiUMOFpFeMsZwtVhB+Fc0aliv4iw6Y+SlA1Vnn9\\nBgJ9Itm9L83ZioiWf1e068qfn9PnMilFHuwD/0JNpevfFWdcIHcc0evFzKcVzAT2\\nH4UXaE65nyzGCTu5A67xcaFOYjFl/yWx7gLpDZJejQbCPo1WANBuyeUCnV74n/+t\\nl4i4hkxJCAqd8fMBMz/hubwTdiZvMVIyP1/a0ZOOaugYTiH8T/lFbtqfxKqZjokS\\nxNcZRnx9AgMBAAECggEAJJJSJP2EbadMTDZuzGQTcHh786WFm4uLBAj1rxNx8Nl7\\nkUQw+PRKRwvr3sJrbIpTZrvh8btyx1ou1MRt01YlJkv/+5S1xokGYGeVV1c8y69N\\nnr+5OP5gxRYBzBTCtiUqUgPl8g2APSIDBk8KgT8ot5Dlzp1j8GRdZZLFgqCXVuKG\\nb4TrHqQLdJP90i+GxlnlsCSIsjOrlDJ+RSQjL1DVdcQwtgk6FkwrXj5KO8uxArNO\\nMwcbDWXpHDHkzyD6G9QzK418JfjPyLGkF425N44YLYBQ3KNYbEoPddz4tryXnIBD\\nEFWm/5f6Bzt9yVgLKympQrKLzX6xCz2Ee26hZeFG+wKBgQDMB6DYcwHQz0PtV6VP\\ngWzbKhr2FrIbMzlkgeP7XMMSnC2UQ6+B3TEx234x+/G71tzheo3RHFZpw6zBdHh2\\nN6oI+JnE0GK54YcsqK2iHXcvvPC8TXeGihdePSPo8qyMF7dGFTwT5JCgHA6Y9BRs\\n+zvHeUtiaII43ZxALA6WMuq0zwKBgQDBQKD3Zsp15oabd6GPwiVXbfxLY5rNCFnY\\n4NWcgi11t05d2ZUJnK37P7E+xi42/XTRikZbbbIoTIVDt0bSlEc/4tGUUtGihheV\\n9WlqSDDhnEdejVfNRr5g/YDVCG9p4D426J0EKpWUlFTOp6EiJN7amAAw06IdFEUi\\nxvW9e2Rk8wKBgQCk0njGwsmxtO6L+uTpwIV+25niOV1yicQQhLn24u4Xx6EuiCtt\\nPpIyU72zhXO4KhBP+qA8UKciU9fohD2YSsmK68HU8/79aMq5ch6quyEAqKLVi8NG\\nJaJRGBU9Z2Wq2rHM5PSeoWOQZHoKaAtkKuUU9NDIU+L3K1fBo5W5ErL2kwKBgD2a\\n+LVlk00ae/a7oElPvs+sRC1UR1jtPJcitXX7GbrZAKZyYkx6IgxiNYKpcroc+kyC\\nJAOpaI3oNuNrXaxrkHsRN3ZtIF8LGCG55iz3bn29cBL4Q/EwiCgrKBLBaaL61Vpz\\ntPpLCPwgfRiT344e6twMcKMgFCHpAFEh09zfpRV5AoGAGPHq7xHFkAdCjHdga2P+\\nOvLuZOzCgilIFbHI+409fvIfi4SW9Y31UD5PqrHUSoaS2IKsrOG392ili28Pgeep\\ngeiNkkLkkapXCJGNbKXiXFEy3DxDMoiv0/KVclahegDpKZ0ZHM/yDk5qgTsPRCMT\\nDn9M1DMzeNXVndBeaabhs4E=\\n-----END PRIVATE KEY-----\\n\",\n" +
            "  \"client_email\": \"firebase-adminsdk-qcg70@chatitt-5fe8f.iam.gserviceaccount.com\",\n" +
            "  \"client_id\": \"100541716460342107931\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-qcg70%40chatitt-5fe8f.iam.gserviceaccount.com\",\n" +
            "  \"universe_domain\": \"googleapis.com\"\n" +
            "}";
    public static String getAccessToken()  {
        try{
            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));

            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).createScoped(Arrays.asList(SCOPES));
            googleCredentials.refresh();

            Log.d(TAG, googleCredentials.getAccessToken().getTokenValue());
            return googleCredentials.getAccessToken().getTokenValue();
        }catch (IOException e){
            Log.e(TAG, ""+ e.getMessage());
            return null;
        }
    }
}
