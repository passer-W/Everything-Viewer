package everything.util;

import everything.classes.Everything;
import everything.classes.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EverythingUtil {
    /**
     * 设置当前连接url
     */
    public static void setUrl(String url) {
        Everything.everything.setUrl(url);
    }

    /**
     * 设置当前所在路径
     */
    public static void setPath(String path) {
        Everything.everything.setPath(path);
    }

    /**
     * 获取响应信息中的所有文件夹和文件信息
     */
    public static void getFDList(String path) throws Exception{
        setPath(path);
        List<File> dirList = new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        String resp = RequestUtil.get(EverythingUtil.joinPath(Everything.everything.getUrl(), path.replaceAll(" ", "%20")), "");
        String dirPattern = "<a href=\"(.*?)\"><img class=\"icon\" src=\"/folder.gif\" alt=\"\">(.*?)</a>.*?<td class=\"modifieddata\"><span class=\"nobr\"><nobr><span class=\"nobr\"><nobr>(.*?)</nobr>";
        String filePattern = "<a href=\"(.*?)\"><img class=\"icon\" src=\"/file.gif\" alt=\"\">(.*?)</a>.*?<td class=\"sizedata\"><span class=\"nobr\"><nobr>(.*?)</nobr>.*?<td class=\"modifieddata\"><span class=\"nobr\"><nobr><span class=\"nobr\"><nobr>(.*?)</nobr>";

        Pattern dirCompiler = Pattern.compile(dirPattern);
        Matcher dirMatcher = dirCompiler.matcher(resp);
        int count = 0;
        while (dirMatcher.find()) {
            dirList.add(new File((dirMatcher.group(2)), "", (dirMatcher.group(3)), true, count));
            count ++;
        }

        Pattern fileCompiler = Pattern.compile(filePattern);
        Matcher fileMatcher = fileCompiler.matcher(resp);
        while (fileMatcher.find()) {
            fileList.add(new File((fileMatcher.group(2)), (fileMatcher.group(3)), (fileMatcher.group(4)), false, -1));
        }
        Everything.everything.setFileList(fileList);
        Everything.everything.setDirList(dirList);
    }

    public static String joinPath(String root, String path){
        if(root.endsWith("/")){
            return root + path;
        } else{
            return root + "/" + path;
        }
    }

    public static String getFile(String path, String charSet) throws Exception{
        String resp = RequestUtil.get(joinPath(Everything.everything.getUrl(), path), "");
        if(charSet.equals("GBK")){
            resp = new String(resp.getBytes(StandardCharsets.UTF_8), "GBK");
        }
        return resp;
    }

    public static ImageView getImage(String imageName){
        return new ImageView(new Image(joinPath("/resources", imageName)));
    }
}
