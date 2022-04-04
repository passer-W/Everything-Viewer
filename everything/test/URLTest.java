package everything.test;

import everything.classes.Everything;
import everything.util.EverythingUtil;
import org.junit.Test;

public class URLTest {

    @Test
    public void test() throws Exception {
        Everything.everything.setUrl("http://127.0.0.1/D%3A");
        EverythingUtil.getFDList("");
        System.out.println(Everything.everything);
    }
}
