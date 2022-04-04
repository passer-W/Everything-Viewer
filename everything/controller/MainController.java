package everything.controller;

import everything.Main;
import everything.classes.Everything;
import everything.classes.File;
import everything.util.EverythingUtil;
import everything.util.RequestUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import util.FileUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    protected TitledPane urlPane;

    @FXML
    protected TextField url;

    @FXML
    protected Button linkBtn;


    @FXML
    protected TreeView dirTree;

    @FXML
    protected StackPane fileManagerStackPane;

    @FXML
    protected GridPane fileContentGridPane;

    @FXML
    protected GridPane fileListGridPane;

    @FXML
    protected ComboBox currentPathCombo;

    @FXML
    protected TableView fileListTableView;

    @FXML
    protected TableColumn fileNameCol;

    @FXML
    protected TableColumn fileSizeCol;

    @FXML
    protected TableColumn fileTimeCol;

    @FXML
    protected Button cancelFileContentBtn;

    @FXML
    protected Button saveFileContentBtn;

    @FXML
    protected ComboBox charsetCombo;

    @FXML
    protected Label status;


    @FXML
    protected TextField filePathText;

    @FXML
    protected TextArea fileContentTextArea;

    @FXML
    protected Button openPathBtn;

    private boolean isConntected = false;

    // 当前展开路径
    protected String path = "";

    // 查看文件路径
    protected String filePath = "";

    protected TreeItem viewDirItem;

    protected String charSet = "UTF-8";



    private void setStatus(String str) {
        status.setText(str);
    }

    private void submit() {
        if (url.getText().equals("")) {
            url.setText("http://127.0.0.1");
        }
        Everything.everything.setUrl(url.getText());
        dirTree.getRoot().getChildren().clear();
        path = "";
        viewDirItem = dirTree.getRoot();
        fileListTableView.getItems().clear();
        try {
            refreshFDList();
            urlPane.setExpanded(false);
        } catch (Exception e) {
            setStatus("连接失败");
        }
    }


    private void refreshFDList() {
        try {
            EverythingUtil.getFDList(path);
            setDirTree();
            setFileListTableView();
            setStatus("目录加载完成");
            isConntected = true;
        } catch (Exception e) {
            setStatus("目录加载失败");
            isConntected = false;
        }
    }

    /**
     * 设置左侧
     */
    private void setDirTree() {
        viewDirItem.getChildren().clear();
        for (File dir : Everything.everything.getDirList()) {
            TreeItem dirItem;
            if (dir.getName().indexOf(":") != -1) {
                dirItem = new TreeItem(dir.getName(), EverythingUtil.getImage(("drive.png")));
            } else {
                dirItem = new TreeItem(dir.getName(), EverythingUtil.getImage(("folder.png")));
            }
            viewDirItem.getChildren().add(dirItem);
        }
    }

    /**
     * 设置右侧
     */
    private void setFileListTableView() {
        if (!Everything.everything.getPath().equals("")) {
            fileListTableView.setItems(FXCollections.observableArrayList(new File("..", "", "", true, -1)));


            for (File dir : Everything.everything.getDirList()) {
                fileListTableView.getItems().add(dir);
            }
            for (File file : Everything.everything.getFileList()) {
                fileListTableView.getItems().add(file);
            }
        }
    }

    /**
     * 设置当前路径
     */
    private void setPath() {
        path = "";
        try {
            TreeItem pathItem = viewDirItem;
            while (!pathItem.getValue().equals("")) {
                path = EverythingUtil.joinPath(new String[]{(String) pathItem.getValue(), path});
                pathItem = pathItem.getParent();
            }
            currentPathCombo.setValue(path);
        } catch (Exception e) {
            viewDirItem = dirTree.getRoot();
        }
    }

    /**
     * 打开新路径
     */
    private void openPath() {
        path = (String) currentPathCombo.getValue();
        TreeItem rootItem = dirTree.getRoot();
        TreeItem tempItem;
        boolean findFlag;
        for (String p : path.split("/")) {
            findFlag = false;
            for (int i = 0; i < rootItem.getChildren().size(); i++) {
                if (((TreeItem) rootItem.getChildren().get(i)).getValue().toString().equals(p)) {
                    rootItem = (TreeItem) rootItem.getChildren().get(i);
                    findFlag = true;
                }
            }
            if (!findFlag) {
                tempItem = new TreeItem(p, EverythingUtil.getImage("folder.png"));
                rootItem.getChildren().add(tempItem);
                rootItem = tempItem;
            }
        }
        dirTree.getSelectionModel().select(rootItem);
        refreshFDList();
    }

    private void viewFile() {
        try {
            fileListGridPane.setVisible(false);
            fileContentGridPane.setVisible(true);
            filePathText.setText(filePath);
            String fileContent = EverythingUtil.getFile(filePath, charSet);
            fileContentTextArea.setText(fileContent);
            setStatus("文件加载完成");
        } catch (Exception e) {
            setStatus("文件加载失败");
        }
    }

    /**
     * 绑定 Column
     */
    private void bindColumn() {

        fileNameCol.setCellValueFactory(new PropertyValueFactory<File, TableCell>("fileCell"));
        fileNameCol.setCellFactory(new Callback<TableColumn<File, TableCell>, TableCell<File, TableCell>>() {
            @Override
            public TableCell<File, TableCell> call(TableColumn<File, TableCell> param) {
                TableCell<File, TableCell> tc = new TableCell<File, TableCell>() {
                    @Override
                    public void updateItem(TableCell item, boolean empty) {
                        if (item != null) {
                            setText(item.getText());
                            setGraphic(item.getGraphic());
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }

                };
                tc.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getButton().equals(MouseButton.SECONDARY)) {
                            if (getSelectedFile().isDir()) {
                                tc.setContextMenu(getContextMenu("dir"));
                            } else {
                                tc.setContextMenu(getContextMenu("file"));
                            }
                        }
                    }
                });
                tc.setAlignment(Pos.BOTTOM_LEFT);
                return tc;
            }
        });

        fileNameCol.setComparator(new Comparator<TableCell>() {
            @Override
            public int compare(TableCell o1, TableCell o2) {
                if (o1.getText().equals("..")) {
                    return 1;
                } else {
                    if ((o1.getGraphic().toString().indexOf("folder.png") != -1 && o2.getGraphic().toString().indexOf("folder.png") != -1) ||
                            (o1.getGraphic().toString().indexOf("folder.png") == -1 && o2.getGraphic().toString().indexOf("folder.png") == -1)) {
                        return o1.getText().compareTo(o2.getText());
                    } else {
                        return (o1.getGraphic().toString().indexOf("folder.png") == -1 ? 1 : 1);
                    }
                }
            }
        });

        fileSizeCol.setCellValueFactory(new PropertyValueFactory<File, String>("size"));
        fileSizeCol.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                TableCell tc = new TableCell<File, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        setText(item);
                    }
                };
                tc.setAlignment(Pos.CENTER);
                tc.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getButton().equals(MouseButton.SECONDARY)) {
                            if (getSelectedFile().isDir()) {
                                tc.setContextMenu(getContextMenu("dir"));
                            } else {
                                tc.setContextMenu(getContextMenu("file"));
                            }
                        }
                    }
                });
                return tc;
            }
        });
        fileSizeCol.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.equals("") || o2.equals("")) {
                    return o1.equals("") ? -1 : 1;
                } else {
                    return Integer.parseInt(o1.split("KB")[0].replaceAll(" ", "").replaceAll(",", "")) - Integer.parseInt(o2.split("KB")[0].replaceAll(" ", "").replaceAll(",", ""));
                }
            }
        });


        fileTimeCol.setCellValueFactory(new PropertyValueFactory<File, String>("time"));
        fileTimeCol.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                TableCell tc = new TableCell<File, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        setText(item);
                    }
                };
                tc.setAlignment(Pos.CENTER);
                tc.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getButton().equals(MouseButton.SECONDARY)) {
                            if (getSelectedFile().isDir()) {
                                tc.setContextMenu(getContextMenu("dir"));
                            } else {
                                tc.setContextMenu(getContextMenu("file"));
                            }
                        }
                    }
                });
                return tc;
            }
        });
    }

    private File getSelectedFile(){
        return (File) fileListTableView.getSelectionModel().getSelectedItem();
    }

    private void exportFile(String downPath){
        try {
            filePath = EverythingUtil.joinPath(new String[]{path, getSelectedFile().getName()});
            String fileContent = EverythingUtil.getFile(filePath, charSet);
            FileUtil.writeFile(downPath, fileContent);
            setStatus("下载成功");
        } catch (Exception e){
            setStatus("下载失败");
        }
    }


    private ContextMenu getContextMenu(String type){
        // 文件窗口添加右键菜单
        ContextMenu contextMenu = new ContextMenu();
        // 菜单项
        MenuItem copyBg = new MenuItem("复制url");
        copyBg.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(EverythingUtil.joinPath(new String[]{Everything.everything.getUrl(), path, getSelectedFile().getName()}));
                clipboard.setContent(clipboardContent);
                setStatus("复制url成功: " + clipboardContent.getString());
            }
        });
        // 菜单项
        MenuItem downBg = new MenuItem("下载至本地");
        downBg.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("选择下载路径");
                fileChooser.setInitialFileName(getSelectedFile().getName());
                java.io.File file = fileChooser.showSaveDialog(Main.getStage());
                if(file!=null){
                    exportFile(file.getAbsolutePath());
                } else{
                    setStatus("下载取消");
                }
            }
        });
        if(type.equals("dir")){
            downBg.setDisable(true);
        }
        contextMenu.getItems().addAll(copyBg, downBg);
        return contextMenu;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        // 显示目录窗口，隐去文件窗口
        fileContentGridPane.setVisible(false);


        // 目录窗口相关初始化
        dirTree.setRoot(new TreeItem(""));
        viewDirItem = dirTree.getRoot();
        // column绑定
        bindColumn();
        // 连接url
        url.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                submit();
            }
        });
        linkBtn.setOnAction(event -> {
            submit();
        });
        //当节点被选中时触发监听器
        dirTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                viewDirItem = (TreeItem) dirTree.getSelectionModel().getSelectedItem();
                setPath();
                // 刷新当前路径下文件
                refreshFDList();
                viewDirItem.setExpanded(true);
            }
        });

        // 右侧目录被选中后，自动切换展开目录
        fileListTableView.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    File selectFile = getSelectedFile();
                    if (selectFile.isDir()) {
                        if (selectFile.getIndex() == -1) {
                            dirTree.getSelectionModel().select(viewDirItem.getParent());
                        } else {
                            dirTree.getSelectionModel().select(viewDirItem.getChildren().get(selectFile.getIndex()));
                        }
                    } else {
                        if (selectFile.getTotalSize() < 1024) {
                            filePath = EverythingUtil.joinPath(new String[]{path, selectFile.getName()});
                            viewFile();
                        } else {
                            setStatus("文件大于1MB，请在浏览器下载");
                        }
                    }
                }
            }
        });
        // 直接跳转路径
        currentPathCombo.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                openPath();
            }
        });
        openPathBtn.setOnAction(event -> {
            openPath();
        });


        // 文件窗口相关初始化
        cancelFileContentBtn.setOnAction(event -> {
            fileContentGridPane.setVisible(false);
            fileListGridPane.setVisible(true);
        });
        saveFileContentBtn.setOnAction(event -> {
            fileContentGridPane.setVisible(false);
            fileListGridPane.setVisible(true);
        });
        charsetCombo.getItems().addAll(FXCollections.observableArrayList("自动", "UTF-8", "GBK"));
        charsetCombo.getSelectionModel().select(0);
        charsetCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (charsetCombo.getSelectionModel().getSelectedIndex() > 1) {
                    charSet = "GBK";
                } else {
                    charSet = "UTF-8";
                }
                viewFile();
            }
        });


    }
}
