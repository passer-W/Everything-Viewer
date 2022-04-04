package everything.classes;

import java.util.List;

public class Everything {
    public final static Everything everything = new Everything();

    private String url;
    private List<File> dirList;
    private List<File> fileList;
    private String path = "";

    public void setPath(String path) {
        this.path = path;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setDirList(List<File> dirList) {
        this.dirList = dirList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public String getUrl() {
        return url;
    }

    public List<File> getDirList() {
        return dirList;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "Everything{" +
                "url='" + url + '\'' +
                ", dirList=" + dirList +
                ", fileList=" + fileList +
                '}';
    }
}
