package com.oops.wallsandwarriors.screens;

import com.oops.wallsandwarriors.Game;
import com.oops.wallsandwarriors.GameConstants;
import com.oops.wallsandwarriors.util.FileUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.scene.paint.Color;

/**
 * Class for adding the GUI components for this
 * screen including the information on How To Play in a form of tabs/pages
 * @author Merve Sagyatanlar
 */
public class HowToPlayScreen extends GeneralScreen {

    /**
     * An overriden getScene method to return the current Screen.
     * @return the current screen as a Screen object.
     */
    @Override
    public Scene getScene() {
        Group root = new Group();
        Scene scene = new Scene(root);
        GraphicsContext g = addBackgroundCanvas(root, "/com/oops/wallsandwarriors/resources/images/background2.png", "How To Play");
        g.setFill(Color.BEIGE);
        g.fillRoundRect(10, 80, 784, 450, 30, 30);
        Image content = getContentImage();
        g.drawImage(content, 20, 90, 784.0, 450.6);
        addTransitionButton(root, "Back", GameConstants.BACK_BUTTON_X,
                GameConstants.BACK_BUTTON_Y, Game.getInstance().screenManager.mainMenu);

        return scene;
    }
    
    private static Image contentImage = null;
    private static Image getContentImage() {
        if (contentImage == null) {
            contentImage = new Image(FileUtils.getInputStream("/com/oops/wallsandwarriors/resources/images/HowToPlay.png"));
        }
        return contentImage;
    }
    
}
