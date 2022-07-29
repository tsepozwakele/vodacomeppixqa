
package ilab.application.specific.functions.API;

import ilab.generic.functions.DataFunctions;
import ilab.generic.functions.commonBase;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.commons.lang3.*;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import okhttp3.*;

@SuppressWarnings("deprecation")
public class API extends commonBase {

    private String[] arrResponse;
    private String awsCookie;
    private String jsessionIdcookie;

    public API(DataFunctions data) {
        this.data = data;
    }
    // private static Logger log = Logging.getLogger(true);

    /*****************************************************************************
     * Function Name: WebServiceContentTextSOAP Description: Send the SOAP Request
     * usning an end point and payload and return a response text and code
     * 
     /* @throws Exception
     ******************************************************************************/
    @SuppressWarnings("resource")
    public String[] WebServiceContentTextSOAP(String RequestPath, String RequestUrl, String ContentType)
            throws Exception {

        // Create a StringEntity for the SOAP XML.
        StringEntity stringEntity = new StringEntity(RequestPath, "UTF-8");
        stringEntity.setChunked(true);

        // Request parameters and other properties.
        HttpPost httpPost = new HttpPost(RequestUrl);
        httpPost.setEntity(stringEntity);
        httpPost.addHeader("Content-Type", ContentType);
        // Execute and get the response.
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpPost);
        arrResponse = new String[2];
        if (response != null) {
            HttpEntity entity = response.getEntity();
            arrResponse[0] = EntityUtils.toString(entity);
            arrResponse[1] = String.valueOf(response.getStatusLine().getStatusCode());
        }
        return arrResponse;
    }

    /*****************************************************************************
     * Function Name: GetLoginCookies Description: Send the SOAP Request using an
     * end point and login credentials and creates cookies to be used for
     * authorization
     * 
     /* @throws Exception
     ******************************************************************************/
    private void GetLoginCookies(String RequestPath, String RequestUrl, String userName, String password)
            throws Exception {

        OkHttpClient client = new OkHttpClient.Builder().followRedirects(false).build();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "password=" + password + "&username=" + userName);
        Request request = new Request.Builder().url(RequestUrl + RequestPath).post(body).build();

        Response response = client.newCall(request).execute();
        List<String> loginCookieList = response.headers().values("Set-Cookie");
        // Assigns only the cookie values to fields:
        awsCookie = (loginCookieList.get(0).split(";"))[0];
        jsessionIdcookie = (loginCookieList.get(1).split(";"))[0];
    }

    /*****************************************************************************
     * Function Name: ConfirmCookies Description: Send the SOAP Request using stored
     * cookies to be validated and accept confirmation to authorize cookies
     * 
     /* @throws Exception
     ******************************************************************************/
    private void ConfirmCookies(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().followRedirects(false).build();

        Request request = new Request.Builder().url(url
                + "/authorizationserver/oauth/authorize?scope=openid&response_type=code%20id_token&client_id=occOpenId&redirect_url="
                + url + "/shopritewebservices/v2&state=10810").get()
                .addHeader("cookie", awsCookie + ";" + jsessionIdcookie).build();

        Response response = client.newCall(request).execute();
        List<String> loginCookieList = response.headers().values("Set-Cookie");
        awsCookie = (loginCookieList.get(0).split(";"))[0];
    }

    /*****************************************************************************
     * Function Name: AuthenticateCookies Description: Send the SOAP Request using
     * stored cookies once confirmed for them to actually be authorized. Once
     * authorized returns authorization code needed to authenticate for api's
     * 
     /* @throws Exception
     ******************************************************************************/
    private String AuthenticateCookies(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().followRedirects(false).build();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "user_oauth_approval=true&authorize=Authorize");
        Request request = new Request.Builder().url(url + "/authorizationserver/oauth/authorize").post(body)
                .addHeader("cookie", awsCookie + ";" + jsessionIdcookie).build();

        Response response = client.newCall(request).execute();
        String location = response.header("Location");
        return StringUtils.substringBetween(location, "code=", "&state");
    }

    /*****************************************************************************
     * Function Name: GetIdtokenUsingAuthCode Description: Send the SOAP Request
     * using auth code for api. Returns id_token required for customer API access
     * 
     /* @throws Exception
     ******************************************************************************/

    private String[] GetAuthtokensUsingAuthCode(String url, String authorizationCode, String apiUsername,
            String apiPassword) throws IOException, ParseException {
        OkHttpClient client = new OkHttpClient.Builder().followRedirects(true).build();
        String credential = Credentials.basic(apiUsername, apiPassword);

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=authorization_code&code=" + authorizationCode
                + "&redirect_url=" + url + "/shopritewebservices/v2");
        Request request = new Request.Builder().url(url + "/authorizationserver/oauth/token").method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Authorization", credential)
                .build();
        Response response = client.newCall(request).execute();
        JSONParser parser = new JSONParser();
        JSONObject bodyObject = (JSONObject) parser.parse(Objects.requireNonNull(response.body()).string());

        arrResponse = new String[2];
        arrResponse[0] = bodyObject.get("access_token").toString();
        arrResponse[1] = bodyObject.get("id_token").toString();

        return arrResponse;
    }

    /**
     * Generates auth tokens required by API's that use customer credentials.
     * 
     /* @param authApiUrl       - api authorization host
     /* @param customerUsername - customer username
     /* @param customerPassword - customer password
     /* @param clientId         - api client id
     /* @param apiPassword      - password to access api
     /* @return String[] access_token and id_token from API response
     /* @throws Exception
     /* @throws IOException
     /* @throws JSONException
     /* @throws ParseException
     */
    public String[] GetOIDCTokensForCustomer(String authApiUrl, String customerUsername, String customerPassword,
            String clientId, String apiPassword) throws Exception, IOException, JSONException, ParseException {
        GetLoginCookies("/authorizationserver/login.do", authApiUrl, customerUsername, customerPassword);
        ConfirmCookies(authApiUrl);
        String authorizationCode = AuthenticateCookies(authApiUrl);
        return GetAuthtokensUsingAuthCode(authApiUrl, authorizationCode, clientId, apiPassword);
    }

    /*****************************************************************************
     * Function Name: WebServiceContentTextSOAP Description: Send the SOAP Request
     * usning an end point and payload and return a response text and code
     * 
     /* @throws Exception
     ******************************************************************************/
    @SuppressWarnings("resource")
    public HttpResponse WebServiceContentTextSOAPinXMLformat(String RequestPath, String RequestUrl, String ContentType)
            throws Exception {

        // Create a StringEntity for the SOAP XML.
        StringEntity stringEntity = new StringEntity(RequestPath, "UTF-8");
        stringEntity.setChunked(true);

        // Request parameters and other properties.
        HttpPost httpPost = new HttpPost(RequestUrl);
        httpPost.setEntity(stringEntity);
        httpPost.addHeader("Content-Type", ContentType);
        // Execute and get the response.
        DefaultHttpClient httpClient = new DefaultHttpClient();
        return httpClient.execute(httpPost);
    }

    /**
     * API REST using parameters:
     /* @param RequestUrl    - full API path including parameters
     /* @param RequestMethod - GET, POST, PUT, DELETE, PATCH
     /* @param ContentType   - application/json
     /* @param GetJsonObj    - Loads data to generate body
     /* @param userName
     /* @param Password
     /* @param accessToken
     /* @param idToken
     /* @return String [] - [0] Response Body, [1] Response Code
     /* @throws Exception
     */
    public String[] WebServiceContentREST(String RequestUrl, String RequestMethod, String ContentType,
            String GetJsonObj, String userName, String Password) throws Exception {

        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(30, TimeUnit.SECONDS) // write timeout
                .readTimeout(30, TimeUnit.SECONDS); // read timeout
        okhttpClientBuilder.followRedirects(true);

        OkHttpClient client = okhttpClientBuilder.build();
        String[] responseBody = new String[2];
        MediaType mediaType = MediaType.parse(ContentType);
        RequestBody body = null;
        
        if (GetJsonObj != null) {
            body = RequestBody.create(mediaType, GetJsonObj);
        }
        
        Headers.Builder builder = new Headers.Builder();
        builder.add("Content-Type", ContentType);
        //builder.add(userName, Password);
        
        Headers headers = builder.build();
        Request request = new Request.Builder().url(RequestUrl).method(RequestMethod, body).headers(headers).build();
        Response response = client.newCall(request).execute();
        
        responseBody[1] = Integer.toString(response.code());
        responseBody[0] = Objects.requireNonNull(response.body()).string();
        
        return responseBody;
    }

    /**
     * Formats and writes json string to file in output location:
     * /JSONOutputData/sScenaTestName - response.json
     * 
     /* @param jsonString
     /* @param sScenaTestName -
     /* @return
     /* @throws IOException
     /* @throws ParseException
     */
    public String WriteJsonOutputBodyToFile(String jsonString, String sScenaTestName)throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonBody = (JSONObject) parser.parse(jsonString);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStringFormatted = gson.toJson(jsonBody);
        System.out.println(jsonStringFormatted);
        // Write JSON file
        FileWriter file = new FileWriter(System.getProperty("user.dir") + "/JSONOutputData/" + sScenaTestName + "-response.json");
        file.write(jsonStringFormatted);
        file.close();
        return file.toString();
    }

    /**
     * Reads json file and gets value from object key provided
     * 
     /* @param jsonObjectKey
     /* @param sScenaTestName
     /* @return
     */
    public String GetResponseObjectValueFromFile(String jsonObjectKey, String sScenaTestName) {
        JSONParser parser = new JSONParser();
        String value = null;
        try (Reader reader = new FileReader(
                System.getProperty("user.dir") + "/JSONOutputData/" + sScenaTestName + "-response.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            // TODO update to search through all json paths. Currently only replaces at top
            // level
            value = (String) jsonObject.get(jsonObjectKey);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static final Configuration configuration = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider()).mappingProvider(new JacksonMappingProvider()).build();

    /**
     * Replaces JSON body values based of Hashmap provided
     * 
     /* @param jsonBody : { "otp": 737796, "target": { "reference":
     *                 "1827eea2-15d0-446e-828c-4a8c2a7a0af5", "identifier":
     *                 "+27170647961", "type": "SMS" } }
     /* @param map      : Map<String, Object> map = new HashMap<String, Object>();
     *                 map.put("target.reference",
     *                 5f17dafb-f99b-4e27-b148-b0db05915f26); map.put("otp", 12345);
     * 
     /* @return : { "otp": 12345, "target": { "reference":
     *         "5f17dafb-f99b-4e27-b148-b0db05915f26", "identifier": "+27170647961",
     *         "type": "SMS" } }
     /* @throws ParseException
     */
    public String ReplaceBodyValues(String jsonBody, Map<String, Object> map) throws ParseException {
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        String updatedJson = null;
        while (it.hasNext()) {
            Entry<String, Object> pairs = it.next();
            updatedJson = JsonPath.using(configuration).parse(jsonBody).set("$." + pairs.getKey(), pairs.getValue())
                    .jsonString();
            jsonBody = updatedJson;
        }
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(updatedJson);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStringFormatted = gson.toJson(jsonObject);
        System.out.println(jsonStringFormatted);
        return jsonStringFormatted;
    }
}