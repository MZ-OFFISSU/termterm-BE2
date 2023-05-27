package server.api.termterm.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.api.termterm.dto.jwt.TokenDto;
import server.api.termterm.dto.member.BaseMemberInfoDto;
import server.api.termterm.dto.member.KakaoMemberInfoDto;
import server.api.termterm.response.BizException;
import server.api.termterm.response.auth.AuthResponseType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService extends SocialAuthService{
    private static final Integer CONN_TIMEOUT = 15 * 1000;  // 15초
    private static final String KAKAO_TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_MEMBERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";

    @Value("${auth.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${auth.kakao.redirect-uri}")
    private String REDIRECT_URI;

    protected HttpURLConnection getURLConnectionForToken(){
        try {
            URL url = new URL(KAKAO_TOKEN_REQUEST_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(CONN_TIMEOUT);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            return urlConnection;
        }catch (MalformedURLException e){
            throw new BizException(AuthResponseType.MALFORMED_URL);
        }
        catch (IOException e){
            throw new BizException(AuthResponseType.KAKAO_CONNECTION_ERROR);
        }
    }

    protected String getTokenJsonStringFromKakao(HttpURLConnection urlConnection, String code){
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            String q = "grant_type=authorization_code"
                    + "&client_id=" + CLIENT_ID
                    + "&redirect_uri=" + REDIRECT_URI
                    + "&code=" + code;

            bw.write(q);
            bw.flush();

            // 실제 서버로 Request 요청 하는 부분. (응답 코드를 받는다. 200 성공, 나머지 에러)
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != 200) {
                throw new BizException(AuthResponseType.KAKAO_CONNECTION_ERROR);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }

            return result;
        }catch (IOException e){
            throw new BizException(AuthResponseType.KAKAO_CONNECTION_ERROR);
        }
    }

    protected JSONObject parseResponse(String result){
        try {
            JSONParser parser = new JSONParser();

            return (JSONObject) parser.parse(result);
        }catch (ParseException e){
            throw new BizException(AuthResponseType.FAIL_PARSE);
        }

    }

    protected HttpURLConnection getURLConnectionForMemberInfo(String accessToken){
        try {
            URL url = new URL(KAKAO_MEMBERINFO_REQUEST_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            urlConnection.getResponseCode();

            return urlConnection;
        }catch (MalformedURLException e){
            throw new BizException(AuthResponseType.MALFORMED_URL);
        }
        catch (IOException e){
            throw new BizException(AuthResponseType.KAKAO_CONNECTION_ERROR);
        }
    }

    protected String getMemberInfoJSONFromKakao(HttpURLConnection urlConnection){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            String res = "";
            while ((line = br.readLine()) != null) {
                res += line;
            }

            return res;
        }catch (IOException e){
            throw new BizException(AuthResponseType.KAKAO_CONNECTION_ERROR);
        }
    }

    @Override
    public TokenDto getToken(String code) {
        HttpURLConnection urlConnection = getURLConnectionForToken();
        String result = getTokenJsonStringFromKakao(urlConnection, code);
        JSONObject elem = parseResponse(result);

        return TokenDto.builder()
                .access_token(elem.get("access_token").toString())
                .refresh_token(elem.get("refresh_token").toString())
                .build();
    }

    @Override
    public BaseMemberInfoDto getMemberInfo(TokenDto tokenDto) {
        HttpURLConnection urlConnection = getURLConnectionForMemberInfo(tokenDto.getAccess_token());
        String result = getMemberInfoJSONFromKakao(urlConnection);
        JSONObject elem = parseResponse(result);
        JSONObject kakaoAccount = (JSONObject) elem.get("kakao_account");
        JSONObject profile = (JSONObject) kakaoAccount.get("profile");

        return KakaoMemberInfoDto.builder()
                .socialId(elem.get("id").toString())
                .email(kakaoAccount.get("email").toString())
                .name(profile.get("nickname").toString())
                .profileImg(profile.get("thumbnail_image_url").toString())
                .isDefaultImage((Boolean) profile.get("is_default_image"))
                .nickname(profile.get("nickname").toString())
                .build();
    }
}
