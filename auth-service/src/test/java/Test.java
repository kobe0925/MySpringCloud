import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/9/8 17:08
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class Test {

    @org.junit.Test
    public void test01(){
        System.out.println(BCrypt.hashpw("123", BCrypt.gensalt()));
    }
}
