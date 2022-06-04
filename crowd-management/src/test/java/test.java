import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author wj
 * @descript
 * @date 2022/6/4 - 21:35
 */

public class test {

    @Test
    public void Test(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passHash = encoder.encode("123456");
        System.out.println(passHash);
    }
}
