package server.api.termterm.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import server.api.termterm.dto.jwt.TokenDto;
import server.api.termterm.dto.member.BaseMemberInfoDto;
import server.api.termterm.dto.member.GoogleMemberInfoDto;
import server.api.termterm.response.base.BizException;
import server.api.termterm.response.auth.AuthResponseType;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleService extends SocialAuthService{
    private static final String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com";

    @Value("${auth.google.client-id}")
    private String CLIENT_ID;

    @Value("${auth.google.client-secret}")
    private String CLIENT_SECRET;

    @Value("${auth.google.redirect-uri}")
    private String REDIRECT_URI;

    private final RestTemplate restTemplate;

    private Map<String, Object> getParamMaps(String code){
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("redirect_uri", REDIRECT_URI);

        return params;
    }

    private String getTokenStringJSONFromGoogle(Map<String, Object> params){
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL + "/token", params, String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new BizException(AuthResponseType.GOOGLE_CONNECTION_ERROR);
        }
        return responseEntity.getBody();
    }

    private JSONObject parseResponse(String result){
        try {
            JSONParser parser = new JSONParser();

            return (JSONObject) parser.parse(result);
        }catch (ParseException e){
            throw new BizException(AuthResponseType.FAIL_PARSE);
        }
    }

    @Override
    public TokenDto getToken(String code) {
        Map<String, Object> params = getParamMaps(code);
        String result = getTokenStringJSONFromGoogle(params);
        JSONObject elem = parseResponse(result);

        return TokenDto.builder()
                .access_token(elem.get("id_token").toString())
                .build();
    }

    private String getMemberInfoStringFromGoogle(String token){
        try {
            String requestUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_TOKEN_REQUEST_URL + "/tokeninfo").queryParam("id_token", token).toUriString();
            return restTemplate.getForObject(requestUrl, String.class);
        }catch (Exception e){
            throw new BizException(AuthResponseType.GOOGLE_CONNECTION_ERROR);
        }
    }

    @Override
    public BaseMemberInfoDto getMemberInfo(TokenDto tokenDto) {
        String memberInfoString = getMemberInfoStringFromGoogle(tokenDto.getAccess_token());
        JSONObject elem = parseResponse(memberInfoString);

        return GoogleMemberInfoDto.builder()
                .socialId(elem.get("sub").toString())
                .email(elem.get("email").toString())
                .name(elem.get("name").toString())
                .profileImg(elem.get("picture").toString())
                .nickname(elem.get("name").toString())
                .build();
    }
}
