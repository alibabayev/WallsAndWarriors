package com.oops.wallsandwarriors.view;

import com.oops.wallsandwarriors.Game;
import com.oops.wallsandwarriors.GameConstants;
import com.oops.wallsandwarriors.model.ChallengeData;
import com.oops.wallsandwarriors.util.DrawUtils;
import com.oops.wallsandwarriors.util.FileUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * A class to implement background view of the screens
 * @author Emin Bahadir Tuluce
 */
public class BackgroundView implements ViewObject {
    
    private final Image backgroundImage;
    private final Font titleFont;
    private final boolean inEditMode;

    /**
     * A constructor of the background view class
     * @param inEditMode boolean value to indicate edit mode
     */
    public BackgroundView(boolean inEditMode) {
        backgroundImage = new Image(FileUtils.getInputStream(
                "/com/oops/wallsandwarriors/resources/images/background2.png"));
        titleFont = Font.font("Arial", FontWeight.BOLD, 48);
        this.inEditMode = inEditMode;
    }
    
    /**
     * Draws the background view object on the screen
     * @param graphics the graphics object for rendering
     * @param deltaTime the time difference until last render
     */
    @Override
    public void draw(GraphicsContext graphics, double deltaTime) {
        String title = "Challenge Editor";
        if (!inEditMode) {
            ChallengeData challengeData = Game.getInstance().
                    challengeManager.getChallengeData();
            title = challengeData.getName();
        }
        graphics.drawImage(backgroundImage, 0, 0,
                GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        DrawUtils.setAttributes(graphics, Color.BLACK, Color.WHITE, 2);
        graphics.setFont(titleFont);
        graphics.fillText(title, 30, 50);
        graphics.strokeText(title, 30, 50);
    }

}
