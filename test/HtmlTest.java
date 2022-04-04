package test;

import org.junit.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class HtmlTest {

    @Test
    public void test(){
        System.out.println(URLDecoder.decode("/F%3A"));
    }
}
