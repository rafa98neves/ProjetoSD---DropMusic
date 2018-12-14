package action;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import uc.sd.apis.DropBoxApi2;

import java.util.Map;
import java.util.Scanner;

public class DropBoxAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private static final String API_APP_KEY = "ay4b0xio8wtgpja";
    private static final String API_APP_SECRET = "z7dzhg7ihti9w3x";
    public static String dropUrl;
    // Access codes #2: per user per application
    private static final String API_USER_TOKEN = "";

    @Override
    public String execute() {
        Scanner in = new Scanner(System.in);

        OAuthService service = new ServiceBuilder()
                .provider(DropBoxApi2.class)
                .apiKey(API_APP_KEY)
                .apiSecret(API_APP_SECRET)
                .callback("http://localhost:8080/LogInDropBox_Callback.action")
                .build();

        try {
            if ( API_USER_TOKEN.equals("") ) {
                setDropUrl(service.getAuthorizationUrl(null));
                return "redirect";
            }
        } catch(OAuthException e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect";
    }

    private static void listFiles(OAuthService service, Token accessToken) {
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.dropboxapi.com/2/files/list_folder", service);
        request.addHeader("authorization", "Bearer " + accessToken.getToken());
        request.addHeader("Content-Type",  "application/json");
        request.addPayload("{\n" +
                "    \"path\": \"\",\n" +
                "    \"recursive\": false,\n" +
                "    \"include_media_info\": false,\n" +
                "    \"include_deleted\": false,\n" +
                "    \"include_has_explicit_shared_members\": false,\n" +
                "    \"include_mounted_folders\": true\n" +
                "}");

        Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println("HTTP RESPONSE: =============");
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        System.out.println("END RESPONSE ===============");


        JSONObject rj = (JSONObject) JSONValue.parse(response.getBody());
        JSONArray contents = (JSONArray) rj.get("entries");
        for (int i=0; i<contents.size(); i++) {
            JSONObject item = (JSONObject) contents.get(i);
            String path = (String) item.get("name");
            System.out.println(" - " + path);
        }
    }

    private static void addFile(String path, OAuthService service, Token accessToken) {
        // TODO
    }

    private static void deleteFile(String path, OAuthService service, Token accessToken) {
        // TODO
    }

    public void setDropUrl(String dropUrl){
        this.dropUrl = dropUrl;
    }
    public String getDropUrl(){
        return dropUrl;
    }
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
