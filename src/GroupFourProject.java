import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.Timer;
import java.util.TimerTask;

public class GroupFourProject extends PApplet {
    private enum ScreenState {
        BEGIN,
        PASSWORD,
        HINT,
        SUCCESS,
        FAILURE
    }

    private static GroupFourProject app = new GroupFourProject();
    private static int maxTime = 2700;
    private static int currentTime = maxTime;

    private static String password = "1234";
    private static String input = "";
    private static String timeText = "";
    private static boolean quitOnEscape = false;

    private static ScreenState gameState = ScreenState.PASSWORD;
    private static Button submitButton;
    private static Timer timer = new Timer();

    private static String[] hints = {
            "Hint 1",
            "Hint 2",
            "Hint 3",
            "Hint 4"
    };

    public static PApplet getGUI() {
        return app;
    }

    public String getTimeString(int time) {
        String minutes = String.valueOf((time / 60 < 10) ? ("0" + (time/60)) : (time / 60));
        String seconds = String.valueOf((time % 60 < 10) ? ("0" + (time%60)) : (time % 60));
        return minutes + ":" + seconds;
    }

    @Override public void settings() {
        fullScreen();
    }

    @Override public void setup() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentTime -= 1;
                timeText = getTimeString(currentTime);
            }
        }, 0, 1000);

        Button.setup();
        submitButton = new Button("Submit", width/2.0f - width/10.0f, height - height/6.0f, 3*width/14.4f, height/10.0f);
    }

    @Override public void draw() {
        switch(gameState) {
            case PASSWORD:
                background(0);
                fill(PApplet.unhex(GConstants.HACKER_GREEN));
                textSize(150);
                text(input, width / 2.0f - 0.5f * textWidth(input), height / 2.0f);
                fill(PApplet.unhex(GConstants.HACKER_GREEN));
                textSize(80);
                text(timeText, width - textWidth(timeText), height / 10);
                submitButton.draw();
                break;
            case HINT:
                System.out.println("DADDY");
                background(25);
                fill(PApplet.unhex(GConstants.HACKER_GREEN));
                textSize(80);
                text("Hints [35, 25, 15, 5]", width/2.0f - 0.5f*textWidth("Hints [35, 25, 15, 5]"), height/9.0f);
                textSize(45);

                if(currentTime < 2100) text(hints[0], width/20.0f, 2*height/10.0f);
                if(currentTime < 1500) text(hints[1], width/20.0f, 4*height/10.0f);
                if(currentTime < 900) text(hints[2], width/20.0f, 6*height/10.0f);
                if(currentTime < 300) text(hints[3], width/20.0f, 8*height/10.0f);

                fill(255, 0, 0);
                textSize(80);
                text(timeText, width - textWidth(timeText), height / 10);
                break;
            case SUCCESS:
                background(0);
                textSize(45);
                text("Congrats! You did it it! Your time was: " + getTimeString(maxTime - currentTime) + "!", width/10.0f, height/2.0f);
                break;
        }

    }

    @Override public void keyPressed(KeyEvent event) {
        switch(event.getKeyCode()) {
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                if(input.length() != 4) input += (event.getKey());
                break;
            case 27:
                if(quitOnEscape) {
                    key = 0;
                }
                break;
            case 8:
                if(input.length() > 0) input = input.substring(0, input.length() - 1);
                break;
            case 16:
                if(gameState == ScreenState.PASSWORD) {
                    gameState = ScreenState.HINT;
                } else if(gameState == ScreenState.HINT) {
                    gameState = ScreenState.PASSWORD;
                }
                break;
        }
    }

    @Override public void mouseClicked(MouseEvent event) {
       if(submitButton.isSelected()) {
           if(input.equals(password) && currentTime > 0) {
               gameState = ScreenState.SUCCESS;
               timer.cancel();
           }
       }
    }

    public static void main(String[] args) {
        PApplet.runSketch(new String[]{"Group Four"}, app);
    }
}
