package com.oops.wallsandwarriors.screens.game;

import com.oops.wallsandwarriors.ChallengeManager;
import com.oops.wallsandwarriors.Game;
import com.oops.wallsandwarriors.GameConstants;
import com.oops.wallsandwarriors.SolutionManager;
import com.oops.wallsandwarriors.definitions.WallDefinitions;
import com.oops.wallsandwarriors.model.ChallengeData;
import com.oops.wallsandwarriors.model.GridPiece;
import com.oops.wallsandwarriors.model.HighTowerData;
import com.oops.wallsandwarriors.model.KnightData;
import com.oops.wallsandwarriors.model.WallData;
import com.oops.wallsandwarriors.screens.challenges.CustomChallengesData;
import com.oops.wallsandwarriors.util.CopyUtils;
import com.oops.wallsandwarriors.util.EncodeUtils;
import com.oops.wallsandwarriors.view.BackgroundView;
import com.oops.wallsandwarriors.view.BoundedViewObject;
import com.oops.wallsandwarriors.view.EditorPaletteElementView;
import com.oops.wallsandwarriors.view.EditorPaletteView;
import com.oops.wallsandwarriors.view.GridPieceView;
import com.oops.wallsandwarriors.view.GridView;
import com.oops.wallsandwarriors.view.HighTowerView;
import com.oops.wallsandwarriors.view.KnightView;
import com.oops.wallsandwarriors.view.WallView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class defines the structure of the editor screen for Challenge Editor.
 * Extends BaseGameScreen
 * @author Emin Bahadir Tuluce
 * @author Cagla Sozen
 * @author Ali Babayev
 */
public class ChallengeEditorScreen extends BaseGameScreen {
    
    private EditorPaletteView paletteView;
    private List<EditorPaletteElementView> paletteElementViews;
    private TextField nameField;
    private TextField descriptionField;
    private TextField creatorField;
    
    List<ChallengeData> customChallenges;

    @Override
    protected void initViewObjects() {
        super.initViewObjects();
        gridView = new GridView(GameConstants.EDITOR_GRID_X, GameConstants.EDITOR_GRID_Y,
                GameConstants.EDITOR_GRID_MARGIN, GameConstants.EDITOR_GRID_B);
        Game.getInstance().challengeManager.initChallengeData();
        backgroundView = new BackgroundView(true);
        paletteView = new EditorPaletteView();
        initPaletteElements();
    }

    private void initPaletteElements() {
        paletteElementViews = new ArrayList<EditorPaletteElementView>();
        int index;
        for (index = 0; index < WallDefinitions.STANDARD.size(); index++) {
            WallData wall = WallDefinitions.STANDARD.get(index);
            paletteElementViews.add(new EditorPaletteElementView(index, new WallView(wall)));
        }
        paletteElementViews.add(index + 0, new EditorPaletteElementView(index + 0,
                new KnightView(new KnightData(null, true))));
        paletteElementViews.add(index + 1, new EditorPaletteElementView(index + 1,
                new KnightView(new KnightData(null, false))));
        paletteElementViews.add(index + 2, new EditorPaletteElementView(index + 2,
                new HighTowerView(new HighTowerData(null, null))));
        clickables.addAll(paletteElementViews);
    }
    
    @Override
    protected void addComponents(Group root) {
        addTransitionButton(root, "Back", GameConstants.EDITOR_BACK_X,
                GameConstants.EDITOR_BACK_Y, Game.getInstance().screenManager.mainMenu);
        addButton(root, "Export", GameConstants.EDITOR_EXP_X,
                GameConstants.EDITOR_EXP_Y, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    exportChallenge();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        addButton(root, "Reset", GameConstants.EDITOR_RES_X,
                GameConstants.EDITOR_RES_Y, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetState();
            }
        });
        addTextComponents(root);
    }
    
    private void addTextComponents(Group root) {
        Font labelFont = new Font("Arial", GameConstants.EDITOR_FONT);
        Label nameLabel = new Label("Challenge Name:");
        Label descriptionLabel = new Label("Description:");
        Label creatorLabel = new Label("Creator:");
        nameLabel.setFont(labelFont);
        descriptionLabel.setFont(labelFont);
        creatorLabel.setFont(labelFont);
        nameLabel.setTextFill(Color.WHITE);
        descriptionLabel.setTextFill(Color.WHITE);
        creatorLabel.setTextFill(Color.WHITE);
        setLayoutPos(nameLabel, GameConstants.EDITOR_LABEL_X, GameConstants.EDITOR_LABEL_Y);
        setLayoutPos(descriptionLabel, GameConstants.EDITOR_LABEL_X,
                GameConstants.EDITOR_LABEL_Y + GameConstants.EDITOR_LABEL_SP);
        setLayoutPos(creatorLabel, GameConstants.EDITOR_LABEL_X,
                GameConstants.EDITOR_LABEL_Y + (GameConstants.EDITOR_LABEL_SP*2));
        
        nameField = new TextField();
        descriptionField = new TextField();
        creatorField = new TextField();
        
        nameField.setPrefWidth(GameConstants.EDITOR_PREF_WIDTH);
        descriptionField.setPrefWidth(GameConstants.EDITOR_PREF_WIDTH);
        creatorField.setPrefWidth(GameConstants.EDITOR_PREF_WIDTH);
        
        setLayoutPos(nameField, GameConstants.EDITOR_FIELD_X, GameConstants.EDITOR_FIELD_Y);
        setLayoutPos(descriptionField, GameConstants.EDITOR_FIELD_X,
                GameConstants.EDITOR_FIELD_Y + GameConstants.EDITOR_LABEL_SP);
        setLayoutPos(creatorField, GameConstants.EDITOR_FIELD_X,
                GameConstants.EDITOR_FIELD_Y + (GameConstants.EDITOR_LABEL_SP*2));
        
        root.getChildren().add(nameLabel);
        root.getChildren().add(descriptionLabel);
        root.getChildren().add(creatorLabel);
        
        root.getChildren().add(nameField);
        root.getChildren().add(descriptionField);
        root.getChildren().add(creatorField);
        
    }
    
    @Override
    protected boolean attemptPlacement() {
        if (hoveredBlock != null && selectedPiece != null) {
            Game.getInstance().soundManager.playPrimary();
        }
        if (hoveredBlock != null && selectedPiece != null) {
            boolean placed = Game.getInstance().gridManager.
                    attemptPlacement(hoveredBlock, selectedPiece);
            if (placed) {
                Game.getInstance().challengeManager.getChallengeData().addPiece(selectedPiece);
                updateViewList();
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected boolean handleViewClick(BoundedViewObject clickedView, MouseButton button) {
        if (selectedPiece == null) {
            Game.getInstance().soundManager.playSecondary();
            if (clickedView instanceof EditorPaletteElementView) {
                EditorPaletteElementView paletteElement = (EditorPaletteElementView) clickedView;
                GridPiece clickedPiece = paletteElement.generateModel();
                if (button == MouseButton.PRIMARY) {
                    if (selectedPiece == null) {
                        selectedPiece = clickedPiece;
                        boolean canPick = true;
                        if (selectedPiece instanceof WallData) {
                            canPick = checkWallCount();
                        }
                        if (canPick) {
                            refreshPreview();
                            previewView.setIndex(clickables.indexOf(clickedView));
                        } else {
                            selectedPiece = null;
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Wall Count");
                            alert.setContentText("There can be at most 4 walls in a challenge.");
                            alert.setHeaderText(null);
                            alert.showAndWait();
                        }
                    } else {
                        selectedPiece = null;
                        previewView = null;
                    }
                    return true;
                }
            } else if (clickedView instanceof GridPieceView) {
                GridPieceView clickedGridPieceView = (GridPieceView) clickedView;
                GridPiece clickedGridPiece = clickedGridPieceView.getModel();
                if (button == MouseButton.PRIMARY || button == MouseButton.SECONDARY) {
                    ChallengeManager challengeManager = Game.getInstance().challengeManager;
                    challengeManager.getChallengeData().removePiece(clickedGridPiece);
                    selectedPiece = null;
                    previewView = null;
                }
                if (button == MouseButton.PRIMARY) {
                    selectedPiece = clickedGridPiece.createCopy();
                    refreshPreview();
                    previewView.getModel().setPosition(null);
                    previewView.setIndex(-1);
                }
                if (button == MouseButton.PRIMARY || button == MouseButton.SECONDARY) {
                    updateViewList();
                    return true;
                }
            }
        }
        return false;
    }
    
    private void refreshPreview() {
        if (selectedPiece instanceof KnightData) {
            previewView = new KnightView((KnightData) selectedPiece, true);
        } else if (selectedPiece instanceof HighTowerData) {
            previewView = new HighTowerView((HighTowerData) selectedPiece, true);
        } else if (selectedPiece instanceof WallData) {
            previewView = new WallView((WallData) selectedPiece, true);
        }
    }
    
    private boolean checkWallCount() {
        int wallCount = Game.getInstance().challengeManager.getChallengeData().walls.size();
        return wallCount < GameConstants.EDITOR_WALL_NO;
    }
    
    @Override
    protected void resetState() {
        Game.getInstance().soundManager.playSecondary();
        selectedPiece = null;
        previewView = null;
        Game.getInstance().challengeManager.getChallengeData().resetAll();
        updateViewList();
    }
    
    @Override
    protected void step(double deltaTime) {
        backgroundView.draw(graphics, deltaTime);
        fpsDisplayView.draw(graphics, deltaTime);
        paletteView.draw(graphics, deltaTime);
        gridView.draw(graphics, deltaTime);
        
        drawPaletteElements(deltaTime);
        drawKnights(deltaTime);
        drawHighTowers(deltaTime);
        drawWalls(deltaTime);
        drawPreview(deltaTime);
    }
    
    private void drawPaletteElements(double deltaTime) {
        for (EditorPaletteElementView paletteElement : paletteElementViews) {
            paletteElement.draw(graphics, deltaTime);
        }
    }
    
    @Override
    protected void drawWalls(double deltaTime) {
        for (WallView wallView : wallViews) {
            wallView.update(false, false, -1, -1);
            wallView.draw(graphics, deltaTime);
        }
    }
    
    private void drawPreview(double deltaTime) {
        if (previewView != null) {
            double dragX;
            double dragY;
            boolean previewSuitable;
            if (hoveredBlock == null) {
                dragX = lastMouseX;
                dragY = lastMouseY;
                previewSuitable = true;
            } else {
                dragX = gridView.translateToScreenX(hoveredBlock.x + 0.5);
                dragY = gridView.translateToScreenY(hoveredBlock.y + 0.5);
                previewSuitable = placementIsSuitable;
            }
            previewView.update(true, previewSuitable, dragX, dragY);
            previewView.draw(graphics, deltaTime);
        }
    }
    
    private void updateViewList() {
        ChallengeData challenge = Game.getInstance().challengeManager.getChallengeData();
        wallViews.clear();
        knightViews.clear();
        highTowerViews.clear();
        for (WallData wall : challenge.walls) {
            wallViews.add(new WallView(wall, true));
        }
        for (KnightData knight : challenge.knights) {
            knightViews.add(new KnightView(knight, true));
        }
        for (HighTowerData highTower : challenge.highTowers) {
            highTowerViews.add(new HighTowerView(highTower, true));
        }
        
        clickables.clear();
        clickables.addAll(paletteElementViews);
        clickables.addAll(wallViews);
        clickables.addAll(knightViews);
        clickables.addAll(highTowerViews);
    }
    
    private void exportChallenge() throws IOException {
        ChallengeData exportedChallenge = Game.getInstance().challengeManager.getChallengeData().createCopy(false);
        SolutionManager solutionManager = Game.getInstance().solutionManager;
        ArrayList<KnightData> incorrectRedKnights = solutionManager.checkSolution(exportedChallenge);

        int max_LENGTH = GameConstants.MAX_LENGTH_OF_TEXT_FIELDS;
        boolean isValid = false;
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        
        if (nameField.getText().length() == 0) {
            alert.setContentText("Challenge name cannot be blank");
        }
        else if (descriptionField.getText().length() == 0) {
            alert.setContentText("Challenge description cannot be blank");
        }
        else if (creatorField.getText().length() == 0) {
            alert.setContentText("Challenge creator cannot be blank");
        }
        else if (max_LENGTH  <= descriptionField.getText().length() &&
                max_LENGTH  <= nameField.getText().length() &&
                max_LENGTH  <= creatorField.getText().length() ) {
            alert.setContentText("Length of each text cannot be more than " + max_LENGTH + "characters");
        }
        else if (!exportedChallenge.hasBlueKnights()) {
            alert.setContentText("There are no blue knights in the challenge");
        }
        else if (incorrectRedKnights == null) {
            alert.setContentText("Solution is incomplete");
        }
        else if (!incorrectRedKnights.isEmpty()) {
            alert.setContentText("Solution is not correct");
        }
        else
            isValid = true;

        if (isValid) {
            exportedChallenge.setDescription(descriptionField.getText());
            exportedChallenge.setName(nameField.getText());
            exportedChallenge.setCreator(creatorField.getText());
        } else {
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        TextArea textArea = new TextArea(EncodeUtils.encode(exportedChallenge));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.add(textArea, 0, 0);
        ButtonType clipboard = new ButtonType("Copy To Clipboard");
        ButtonType addToCustom = new ButtonType( "Add To Custom Challenges");
        ButtonType ok = new ButtonType("OK", ButtonData.CANCEL_CLOSE);

        Alert exportAlert = new Alert(Alert.AlertType.NONE);
        exportAlert.setTitle("Copy the exported challenge code.");
        exportAlert.setHeaderText(null);
        exportAlert.getDialogPane().setContent(gridPane);
        exportAlert.getButtonTypes().add(clipboard);
        exportAlert.getButtonTypes().add(addToCustom);
        exportAlert.getButtonTypes().add(ok);

        Optional<ButtonType> result = exportAlert.showAndWait();

        if (result.get() == clipboard) {
            CopyUtils.copyToClipboard(textArea.getText());
        }
        else if (result.get() == addToCustom) {
            addToCustomChallenges(textArea.getText());
        }
    }
    
    private void addToCustomChallenges(String challengeData) throws IOException {
        CustomChallengesData customChallengesData = new CustomChallengesData();
        customChallenges = customChallengesData.getCustomChallenges();

        ChallengeData toImp = null;
        try {
            toImp = EncodeUtils.decode(challengeData);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (customChallenges.add(toImp)) {
            customChallengesData.update(toImp);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setContentText("The new challenge added to your \"Custom Challenges\" list successfully!");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }
    
}
