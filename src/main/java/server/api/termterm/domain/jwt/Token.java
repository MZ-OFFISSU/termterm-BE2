package server.api.termterm.domain.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
//@RedisHash("refreshToken")
@Builder @AllArgsConstructor @NoArgsConstructor
@Entity
public class Token {

    @Id
    @JsonIgnore
    private Long id;

    private String refresh_token;

//    @TimeToLive(unit = TimeUnit.SECONDS)
    @Value("${jwt.refresh-token-expire-time}")
    private Integer expiration;

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }
}
