package everything.classes;

import everything.util.EverythingUtil;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.plaf.TreeUI;

public class File {
    private String name;
    private String size;
    private String time;
    private boolean isDir;
    private TableCell fileCell;
    private int index;


    public File(String name, String size, String time, boolean isDir, int index) {
        this.name = name;
        this.size = size;
        this.time = time;
        this.isDir = isDir;
        this.index = index;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getTime() {
        return time;
    }

    public TableCell getFileCell() {
        fileCell = new TableCell();
        if (isDir) {
            fileCell.setGraphic(EverythingUtil.getImage("folder.png"));
        } else {
            fileCell.setGraphic(EverythingUtil.getImage("file.png"));
        }
        fileCell.setText(name);
        return fileCell;
    }

    public int getTotalSize(){
        return Integer.parseInt(size.split("KB")[0].replaceAll(" ", "").replaceAll(",", ""));
    }

    public int getIndex() {
        return index;
    }
}
