package test;

import org.junit.Test;
import util.requestUtil;
import java.util.regex.*;

public class URLTest {
    @Test
    public void test(){
        Pattern r = Pattern.compile("<span class=\"nobr\"><nobr><a href=\"(.*?)\"");
        String response = requestUtil.get("http://127.0.0.1/F%3A/%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0", "");
//        System.out.println(response);
        Matcher matcher = r.matcher(response);
        while (matcher.find( )) {
            System.out.println("Found value: " + matcher.group(1) );
        }
    }
}
