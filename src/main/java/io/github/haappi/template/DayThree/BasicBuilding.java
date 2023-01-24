package io.github.haappi.template.DayThree;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class BasicBuilding {
    private final Button[][] buttons;
    private final GridPane gridPane;
    private final double rating = 1.00;
    private final int maxLevel;
    private final int topLeft;

    private final int x;
    private final int y;
    private final ArrayList<ProduceType> produceTypes = new ArrayList<>();
    private Color color;
    private int level = 1;

    public BasicBuilding(
            int x,
            int y,
            int maxLevel,
            Color color,
            Button[][] buttons,
            GridPane gridPane,
            String name) {
        this.buttons = buttons;
        this.gridPane = gridPane;
        this.maxLevel = maxLevel;
        this.color = color;
        this.topLeft = x;

        this.x = x;
        this.y = y;

        buttons[x][y].setText("\uD83C\uDFDF️"); // 🏟️
        buttons[x][y].setTextFill(color);

        switch (name) {
            case "Farm" -> {
                produceTypes.add(new ProduceType("Eggs"));
            }
            case "News" -> {
                produceTypes.add(new ProduceType("NewsPaper"));
            }
        }
    }

    public int getTopLeft() {
        return topLeft;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void upgrade() {
        if (level >= maxLevel) {
            return;
        }

        level++;

        for (int i = topLeft; i < topLeft + level; i++) {
            for (int j = topLeft; j < topLeft + level; j++) {
                buttons[i][j].setText("\uD83C\uDFDF️"); // 🏟️
                buttons[i][j].setTextFill(color);
            }
        }
    }

    public Button[][] getButtons() {
        return buttons;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getRating() {
        return rating;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public ArrayList<ProduceType> getProduceTypes() {
        return produceTypes;
    }

    public void produceItem() {
        if (Math.random() > 0.5) {
            return;
        }
        produceTypes.get(0).setCount(produceTypes.get(0).getCount() + 1);
        System.out.println(produceTypes.get(0));
    }

    @Override
    public String toString() {
        return "\uD83C\uDFDF️";
    }
}
