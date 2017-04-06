package com.tc.vd.ui.control.monologfx;

import com.tc.vd.utils.FileResUtil;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @MkHeck)
 */
public class MonologFX {

    /**
     * Type of dialog box is one of the following, each with a distinct icon:
     * <p>
     * ACCEPT = check mark icon
     * <p>
     * ERROR = red 'X' icon
     * <p>
     * INFO = blue 'i' (information) icon
     * <p>
     * QUESTION = blue question mark icon
     * <p>
     * If no type is specified in the constructor, the default is INFO.
     */
    public enum Type { ACCEPT, ERROR, INFO, QUESTION };
    public enum ButtonAlignment { LEFT, RIGHT, CENTER };

    private Type type;
    private Scene scene;
    private Stage stage;
    private final BorderPane pane = new BorderPane();
    private final ImageView icon = new ImageView();
    private Node childNode; //子节点
    private final HBox buttonBox = new HBox(10);
    private final List<MonologFXButton> buttons = new ArrayList<>();
    private int buttonCancel = -1;
    private int buttonSelected = -1;
    private ButtonAlignment buttonAlignment = ButtonAlignment.CENTER;
    private float displayTime = 0f;
    private float fadeInOutTime;
    private final List<String> stylesheets = new ArrayList<>();

    private EventHandler<ActionEvent> okEventHandler;//确定处理
    private EventHandler<ActionEvent> noEventHandler;//取消处理

    /**
     * Default constructor for a MonologFX dialog box. Creates an INFO box.
     *
     * @see Type
     */
    public MonologFX() {
        initDialog(Type.INFO);
    }

    /**
     * Constructor for a MonologFX dialog box that accepts one of the enumerated
     * types listed above.
     *
     * @param t The type of MonologFX dialog box to create.
     * @see Type
     */
    public MonologFX(Type t) {
        initDialog(t);
    }

    public void setOkEventHandler(EventHandler<ActionEvent> okEventHandler){
        this.okEventHandler = okEventHandler;
    }

    public void setNoEventHandler(EventHandler<ActionEvent> noEventHandler){
        this.noEventHandler = noEventHandler;
    }

    /**
     * 添加确定按钮
     * @return
     */
    public void addOKButton() {
        MonologFXButton okBtn = new MonologFXButton();
        okBtn.setType(MonologFXButton.Type.OK);
        okBtn.setLabel("_OK");
        okBtn.setCancelButton(true);
        okBtn.setDefaultButton(true);

        //设置单击”确定“按钮处理
        if(null != this.okEventHandler){
            okBtn.setOnActionHanlder(this.okEventHandler);
        }

        addButton(okBtn);
    }

    /**
     * 添加确认取消按钮
     * @return
     */
    public void addYesNoButtons() {
        Button[] btns = new Button[2];
        /*
         * No default or cancel buttons designated, by design.
         * Some cases would require the Yes button to be default & No to cancel,
         * while others would require the opposite. You as the developer can
         * assign default/cancel Yes/No buttons using the full addButtons()
         * method if required. You have the power!
         */
        MonologFXButton yesBtn = new MonologFXButton();
        yesBtn.setType(MonologFXButton.Type.YES);
        yesBtn.setLabel("_Yes");
        yesBtn.setCancelButton(false);
        yesBtn.setDefaultButton(false);

        //设置单击”确定“按钮处理
        if(null != this.okEventHandler){
            yesBtn.setOnActionHanlder(this.okEventHandler);
        }

        addButton(yesBtn);

        MonologFXButton noBtn = new MonologFXButton();
        noBtn.setType(MonologFXButton.Type.NO);
        noBtn.setLabel("_No");
        noBtn.setCancelButton(false);
        noBtn.setDefaultButton(false);

        //设置单击”取消“按钮处理
        if(null != this.noEventHandler){
            noBtn.setOnActionHanlder(this.noEventHandler);
        }

        addButton(noBtn);
    }

    /**
     * Public method used to add a button to a MonologFX dialog.
     *
     * @param btnToAdd A MonologFXButton object.
     *
     * @see MonologFXButton
     */
    public void addButton(MonologFXButton btnToAdd) {
        buttons.add(btnToAdd);

        final Button btn = new Button();

        btn.setMnemonicParsing(true);
        btn.setText(btnToAdd.getLabel());

        if (btnToAdd.getIcon() != null) {
            btn.setGraphic(btnToAdd.getIcon());
        }

        btn.setDefaultButton(btnToAdd.isDefaultButton());
        if (btnToAdd.isCancelButton()) {
            btn.setCancelButton(true);
            buttonCancel = buttons.size() - 1;
        }

        btn.setDisable(false);
        if(null != btnToAdd.getOnActionHandler()){ //鼠标单击处理
            btn.setOnAction(btnToAdd.getOnActionHandler());
        }

        if (btn.isDefaultButton()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    btn.requestFocus();
                }
            });
        }

        EventHandler<ActionEvent> actionHandler = btnToAdd.getOnActionHandler();
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evt) {
                // Iterate through to find correct button.
                for (int i=0; i < buttons.size(); i++) {
                    if (buttons.get(i).getLabel().equalsIgnoreCase(((Button) evt.getSource()).getText())) {
                        buttonSelected = i;
                        break;
                    }
                }

                if(null != actionHandler){
                    actionHandler.handle(evt);
                }

                // Close the dialog
                ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
            }
        });

        buttonBox.getChildren().add(btn);
    }

    /**
     * Allows developer to add stylesheet for MonologFX dialog, supplementing or
     * overriding existing styling.
     *
     * @param stylesheet String variable containing the path/name of the
     * stylesheet to apply to the dialog's scene and contained controls.
     */
    public void addStylesheet(String stylesheet) {
        try {
            String newStyle = this.getClass().getResource(stylesheet).toExternalForm();
            stylesheets.add(newStyle);
        } catch (Exception ex) {
            System.err.println("Unable to find specified stylesheet: " + stylesheet);
            System.err.println("Error message: " + ex.getMessage());
        }
    }

    private void initDialog(Type t) {
        stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);

        setType(t);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
    }

    private void loadIconFromResource(String fileName) {
        String skinPath = System.getProperty("res.css.skin.path");
        String resPath = skinPath + "/images/monologfx/" + fileName;
        try {
            File resFile = FileResUtil.getResFile(resPath);
            Image imgIcon = new Image(new FileInputStream(resFile));
            icon.setPreserveRatio(true);
            icon.setFitHeight(48);
            icon.setImage(imgIcon);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the button alignment for the MonologFX dialog box. Default is CENTER.
     *
     * @param buttonAlignment Valid values are LEFT, RIGHT, and CENTER.
     *
     * @see ButtonAlignment
     */
    public void setButtonAlignment(ButtonAlignment buttonAlignment) {
        this.buttonAlignment = buttonAlignment;
    }

    /**
     * Sets the display time for the MonologFX dialog box. Default is 10
     * seconds.
     *
     * @param displayTime Valid values are any integer value.
     */
    public void setDisplayTime(int displayTime) {
        this.displayTime = displayTime;

        // Fade in/out time = max of 5 seconds
        fadeInOutTime = Math.min(displayTime / 4f, 5f);
    }

    /**
     * Sets the text displayed within the MonologFX dialog box. Word wrap
     * ensures that all text is displayed.
     *
     * @param msg String variable containing the text to display.
     */
    public void setMessage(String msg) {
        final Label message = new Label();
        message.setText(msg);
        message.setWrapText(true);
        this.childNode = message;
    }

    /**
     * 设置显示的内容
     * @param node
     */
    public void setCenterContent(Node node){
        this.childNode = node;
    }

    /**
     * Sets the modality of the MonologFX dialog box.
     *
     * @param isModal Boolean. A true value = APPLICATION_MODAL, false = NONE.
     */
    public void setModal(boolean isModal) {
        stage.initModality((isModal ? Modality.APPLICATION_MODAL : Modality.NONE));
    }

    /**
     * Sets the text displayed in the title bar of the MonologFX dialog box.
     *
     * @param title String containing the text to place in the title bar.
     */
    public void setTitleText(String title) {
        stage.setTitle(title);
    }

    /**
     * Sets the Type of MonologFX dialog box to display.
     *
     * @param typeToSet One of the supported types of dialogs.
     * @see Type
     */
    public void setType(Type typeToSet) {
        type = typeToSet;
    }

    /**
     * Sets the coordinates of the MonologFX dialog.
     * 
     * @param x The x coordinate of the upper-left corner of the dialog.
     * @param y The y coordinate of the upper-left corner of the dialog.
     */
    public void setPos(double x, double y) {
        stage.setX(x);
        stage.setY(y);
    }

    /**
     * Sets the x coordinate of the MonologFX dialog.
     * 
     * @param x The x coordinate of the upper-left corner of the dialog.
     */
    public void setX(double x) {
        stage.setX(x);
    }

    /**
     * Sets the y coordinate of the MonologFX dialog.
     * 
     * @param y The y coordinate of the upper-left corner of the dialog.
     */
    public void setY(double y) {
        stage.setY(y);
    }

    private void populateStage() {
        String iconFile;

        switch (type) {
            case ACCEPT:
                iconFile = "Dialog-accept.jpg";
                //if (buttons.size() == 0) {
                if (buttons.isEmpty()) {
                    addOKButton();
                }
                break;
            case ERROR:
                iconFile = "Dialog-error.jpg";
                //if (buttons.size() == 0) {
                if (buttons.isEmpty()) {
                    addOKButton();
                }
                break;
            case INFO:
                iconFile = "Dialog-info.jpg";
                //if (buttons.size() == 0) {
                if (buttons.isEmpty()) {
                    addOKButton();
                }
                break;
            case QUESTION:
                iconFile = "Dialog-question.jpg";
                break;
            default:
                iconFile = "Dialog-info.jpg";
                break;
        }

        try {
            loadIconFromResource(iconFile);
        } catch (Exception ex) {
            System.err.println("Exception trying to load icon file: " + ex.getMessage());
        }

        BorderPane.setAlignment(icon, Pos.CENTER_LEFT);
        BorderPane.setMargin(icon, new Insets(5));
        pane.setLeft(icon);

        BorderPane.setAlignment(childNode, Pos.CENTER);
        BorderPane.setMargin(childNode, new Insets(5));
        pane.setCenter(childNode);

        switch (buttonAlignment) {
            case LEFT:
                buttonBox.setAlignment(Pos.CENTER_LEFT);
                break;
            case CENTER:
                buttonBox.setAlignment(Pos.CENTER);
                break;
            case RIGHT:
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                break;
        }

        BorderPane.setMargin(buttonBox, new Insets(5));
        pane.setBottom(buttonBox);

        scene = new Scene(pane);
        for (int i=0; i < stylesheets.size(); i++) {
            try {
                scene.getStylesheets().add(stylesheets.get(i));
            } catch (Exception ex) {
                System.err.println("Unable to load specified stylesheet: " + stylesheets.get(i));
                System.err.println(ex.getMessage());
            }
        }
        stage.setScene(scene);
    }

    /**
     * Displays the MonologFX dialog box and waits for user input.
     *
     * @return The type of the button pressed.
     *
     * @see MonologFXButton.Type, show()
     * 
     * @deprecated Please use show() instead.
     */
    public MonologFXButton.Type showDialog() {
        return show();
    }

    /**
     * Displays the MonologFX dialog box and waits for user input.
     *
     * @return The type of the button pressed.
     *
     * @see MonologFXButton.Type
     */
    public MonologFXButton.Type show() {
        populateStage();
        if (type == Type.QUESTION) {
            //if (buttons.size() == 0) {
            if (buttons.isEmpty()) {
                addYesNoButtons();
            }
        }

        stage.setResizable(false);
        stage.sizeToScene();

        if (displayTime <= 0f) {  // Show dialog indefinitely
            // Zero value or nonsensical one: who shows a dialog for -10 seconds?
            // Just show it!
            if (!(stage.getX() > -1 && stage.getY() > -1)) {
                stage.centerOnScreen();
            }

            stage.showAndWait();
        } else {    // Timed dialog
            stage.setOpacity(0d);
            stage.show();
            final DoubleProperty opacity = stage.opacityProperty();

            Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0d)),
                    new KeyFrame(new Duration(fadeInOutTime * 1000),
                            new EventHandler() {
                                @Override
                                public void handle(Event t) {
                                    Timeline display = new Timeline(
                                            new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1d)),
                                            new KeyFrame(new Duration((displayTime - fadeInOutTime * 2) * 1000),
                                                    new EventHandler() {
                                                        @Override
                                                        public void handle(Event t) {
                                                            Timeline fadeOut = new Timeline(
                                                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1d)),
                                                                    new KeyFrame(new Duration(fadeInOutTime * 1000),
                                                                            new EventHandler() {
                                                                                @Override
                                                                                public void handle(Event t) {
                                                                                    stage.hide();
                                                                                }
                                                                            }, new KeyValue(opacity, 0d)));
                                                            fadeOut.play();
                                                        }
                                                    }, new KeyValue(opacity, 1d)));
                                    display.play();
                                }
                            }, new KeyValue(opacity, 1d)));
            fadeIn.play();
        }

        if (buttonSelected == -1) {
            /* If a different type of button is designated the "cancel button",
             * e.g. a MonologFXButton.Type.NO button, return that one;
             * otherwise, return a CANCEL button type.
             */
            return (buttonCancel == -1 ? MonologFXButton.Type.CANCEL : buttons.get(buttonCancel).getType());
        } else {
            return buttons.get(buttonSelected).getType();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
