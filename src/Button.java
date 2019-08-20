import processing.core.PApplet;

public class Button {
    private static PApplet gui;
    private String text;

    private int textSize;

    private float x;
    private float y;
    private float width;
    private float height;


    public Button(String _text, float _x, float _y, float _width, float _height) {
        text = _text;
        x = _x;
        y = _y;
        width = _width;
        height = _height;
    }

    public static void setup() {
        gui = GroupFourProject.getGUI();
    }

    public void draw() {
        gui.fill(32, 194, 14);
        gui.rect(x, y, width, height);
        gui.fill(255);
        gui.text(text, x + width/2.0f - 0.5f*gui.textWidth(text), y + 0.8f*height);
    }

    public boolean isSelected() {
        return gui.mouseX > x && gui.mouseX < x + width && gui.mouseY > y && gui.mouseY < height + y;
    }

    public String getText() {
        return text;
    }

    public void setText(String _text) {
        text = _text;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int _textSize) {
        textSize = _textSize;
    }

    public float getX() {
        return x;
    }

    public void setX(float _x) {
        x = _x;
    }

    public float getY() {
        return y;
    }

    public void setY(float _y) {
        y = _y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float _width) {
        width = _width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float _height) {
        height = _height;
    }
}
