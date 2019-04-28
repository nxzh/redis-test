package fun.code4.session;

import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
  @RequestMapping(
      value = "/login",
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity login(@RequestBody User user, HttpSession session) {
    System.out.println(session);
    Object a = session.getAttribute("aaa");
    if (a == null) {
      session.setAttribute("aaa", "bbb");

    } else {
      System.out.println(a);
    }
    if ("111".equals(user.getPassword()) && "aaa".equals(user.getUsername())) {

      return ResponseEntity.ok().build();
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
