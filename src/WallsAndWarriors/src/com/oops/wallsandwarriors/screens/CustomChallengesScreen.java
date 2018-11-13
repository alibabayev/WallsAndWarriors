package com.oops.wallsandwarriors.screens;

import com.oops.wallsandwarriors.util.CopyUtils;
import com.oops.wallsandwarriors.util.DebugUtils;
import com.oops.wallsandwarriors.Game;
import com.oops.wallsandwarriors.game.model.ChallengeData;
import com.oops.wallsandwarriors.game.model.HighTowerData;
import com.oops.wallsandwarriors.game.model.KnightData;
import com.oops.wallsandwarriors.game.view.GridView;
import com.oops.wallsandwarriors.game.view.HighTowerView;
import com.oops.wallsandwarriors.game.view.KnightView;
import com.oops.wallsandwarriors.util.TestUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import com.oops.wallsandwarriors.util.EncodeUtils;

public class CustomChallengesScreen extends GeneralScreen {

    ObservableList<String> challengeNames = FXCollections.observableArrayList ();
    List<ChallengeData> challenges = new ArrayList<>();

    GridPane grid = new GridPane();

    @Override
    public Scene getScene(){
        Group root = new Group();
        Scene scene = new Scene(root);

        DebugUtils.initClickDebugger(scene);
        addBackgroundCanvas(root, "resources/images/background2.png", "Custom Challenges");
        renderButtons(root);

        Text title = new Text(50, 100, "Custom Mode - Choose a Challenge");
        Font theFont = Font.font("Arial", FontWeight.BOLD, 20);
        title.setFont(theFont);
        root.getChildren().add(title);


        showChallenges(root);

        Button importButton = new Button("Import");
        importButton.setLayoutX(50);
        importButton.setLayoutY(500);
        importButton.setPrefSize(100,50);

        importButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                TextInputDialog textInputDialog = new TextInputDialog(null);
                textInputDialog.setTitle("Add Challenge");
                textInputDialog.setHeaderText("Enter the code of the challenge");
                textInputDialog.setContentText("Code: ");
                textInputDialog.showAndWait();


                String code = textInputDialog.getEditor().getText();
                ChallengeData toImp = null;
                try {
                    toImp = EncodeUtils.decode(code);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                challenges.add(toImp);
            }
        });

        root.getChildren().add(importButton);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setLayoutX(450);
        grid.setLayoutY(150);
        root.getChildren().add(grid);

        return scene;
    }


    private void renderButtons(Group root)
    {
        addTransactionButton(root, "Back", 700, 500, Game.getInstance().screenManager.mainMenu);
    }

    private void showChallenges(Group root)
    {
        challengeNames.clear();

        challengeNames.add(TestUtils.CHALLENGE_45.getName());
        challengeNames.add(TestUtils.CHALLENGE_51.getName());
        challengeNames.add(TestUtils.CHALLENGE_51.getName());
        challengeNames.add(TestUtils.CHALLENGE_51.getName());
        challengeNames.add(TestUtils.CHALLENGE_51.getName());
        challengeNames.add(TestUtils.CHALLENGE_51.getName());
        challengeNames.add(TestUtils.CHALLENGE_51.getName());
        challengeNames.add(TestUtils.CHALLENGE_51.getName());
        challengeNames.add(TestUtils.CHALLENGE_51.getName());
        challengeNames.add(TestUtils.CHALLENGE_51.getName());
        challengeNames.add(TestUtils.CHALLENGE_51.getName());



        challenges.clear();

        challenges.add(TestUtils.CHALLENGE_45);
        challenges.add(TestUtils.CHALLENGE_51);
        challenges.add(TestUtils.CHALLENGE_51);
        challenges.add(TestUtils.CHALLENGE_51);
        challenges.add(TestUtils.CHALLENGE_51);
        challenges.add(TestUtils.CHALLENGE_51);
        challenges.add(TestUtils.CHALLENGE_51);
        challenges.add(TestUtils.CHALLENGE_51);
        challenges.add(TestUtils.CHALLENGE_51);
        challenges.add(TestUtils.CHALLENGE_51);
        challenges.add(TestUtils.CHALLENGE_51);


        ListView<String> list = new ListView<>();
        list.setLayoutX(50);
        list.setLayoutY(150);
        list.setOrientation(Orientation.VERTICAL);
        list.setPrefWidth(350);
        list.setPrefHeight(300);
        list.setItems(challengeNames);
        root.getChildren().add(list);


        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                grid.getChildren().clear();
                int challengeIndex = list.getSelectionModel().getSelectedIndex();
                try {
                    showChallengeInfo(challenges.get(challengeIndex), root);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void showChallengeInfo(ChallengeData challenge, Group root) throws FileNotFoundException
    {
        Game.getInstance().challengeManager.setChallengeData(challenge);
        
        //Image of the challenge
        dipslayChallengePreview(challenge);

        //created by info
        Label creatorLabel = new Label("Creator:  " + challenge.getCreator());

        //warriors info
        Label warriorLabel = new Label("Info:  " + challenge.knights.size() + " Knights.");

        //type info
//        String solved;
//        if(challenge.getSolved())
//        {
//            solved = " Yes";
//        }
//        else
//        {
//            solved = " No";
//        }
//
//        Label isSolved = new Label("Solved: " + solved);

        Button playButton = new Button("Play");
        playButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Screen gameScreen = Game.getInstance().screenManager.gameScreen;
                Game.getInstance().challengeManager.setChallengeData(challenge);
                Game.getInstance().setScreen(gameScreen);
            }
        });


        Button shareButton = new Button("Share");
        shareButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){

                try {
                    shareChallenge(challenge);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });


        grid.add(creatorLabel,0,1);
        grid.add(warriorLabel,0,2);
//        grid.add(isSolved,0,3);
        grid.add(shareButton, 0, 4);
        grid.add(playButton, 1,4);

    }
    
    private void dipslayChallengePreview(ChallengeData challenge) {
        Canvas previewCanvas = new Canvas();
        previewCanvas.setHeight(150);
        previewCanvas.setWidth(200);
        GraphicsContext graphics = previewCanvas.getGraphicsContext2D();
        
        GridView gridView = new GridView(5, 5, 5, 30);
        gridView.draw(graphics, 1);
        grid.add(previewCanvas, 0, 0);
        
        for (KnightData knight : challenge.knights) {
            new KnightView(knight, 5, 5, 30).draw(graphics, 1);
        }
        for (HighTowerData highTower : challenge.highTowers) {
            new HighTowerView(highTower, 5, 5, 30).draw(graphics, 1);
        }
    }
    private void shareChallenge(ChallengeData challenge ) throws FileNotFoundException,IOException{

        TextArea textArea = new TextArea(EncodeUtils.encode(challenge));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.add(textArea, 0, 0);
        ButtonType clipboard = new ButtonType("Copy To Clipboard!");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Copy the exported challenge code.");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(gridPane);
        alert.getButtonTypes().add(clipboard);

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == clipboard )
        {
            CopyUtils.copyToClipboard(textArea.getText());
        }

        alert.showAndWait();
    }
}
