package project.floread.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import project.floread.config.auth.LoginUser;
import project.floread.config.auth.dto.SessionUser;
import project.floread.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class IndexController {
    //@Value("${file.dir}")
    private String fileDir;
    //페이지에 관련된 컨트롤러는 모두 IndexController를 사용합니다.

    //머스테치의 파일 위치는 기본적으로 src/main/resources/templates입니다
    //이 위치에 머스테치 파일을 두면 스프링 부트에서 자동으로 로딩합니다.
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        //Model = 서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있습니다.
        //여기서는 postsService.findAllDesc()로 가져온 결과를 posts로 index.mustache에 전달합니다.

        //머스테치 스타터 덕분에 컨트롤러에서 문자열을 반환할 때 앞의 경로와 뒤의 파일 확장자는 자동으로 지정됩니다.
        //앞의 경로는 src/main/resources/templates로, 뒤의 파일 확장자는 ,mustache가 붙는 것입니다.
        //즉 여기선 "index"를 반환하므로, src/main/resources/templates/index.mustache로 전환되어 View Resolver가 처리하게 됩니다.
        //앞서 작성된 CustomOAuth2UserService에서 로그인 성공시 세션에 SessionUser를 저장하도록 구성했습니다.
        //before)즉, 로그인 성공 시 httpSession.getAttribute("user")에서 값을 가져올 수 있습니다.
        //after)기존에 (User) httpSession.getAttribute("user)로 가져오던 세션 정보 값이 개선 되었습니다.
        //이제는 어느 컨트롤러든지 @LoginUser만 사용하면 세션 정보를 가져올 수 있게 되었습니다.
        if (user != null) {
            model.addAttribute("googleName", user.getName());
        }
        return "index";
    }


}