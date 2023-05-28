package server.api.termterm.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class SwaggerRedirector {
    @GetMapping("/api-docs")
    public String api(){
        return "redirect:/swagger-ui/index.html";
    }
}
