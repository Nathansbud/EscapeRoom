import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GroupFourProject extends PApplet {
    private enum ScreenState {
        LOCKOUT,
        BEGIN,
        CS_QUESTION,
        PASSWORD,
        HINT,
        SUCCESS,
        FAILURE
    }

    private static GroupFourProject app = new GroupFourProject();
    private static int maxTime = 1800;
    private static int currentTime = maxTime;

    private static String password = "7361";
    private static String input = "";
    private static String timeText = "";
    private static boolean quitOnEscape = false;
    private static boolean properQuit = true;

    private static ScreenState gameState = ScreenState.BEGIN;
    private static Button submitButton;
    private static Button nextButton;
    private static Button quitButton;

    private static List<String> scores;

    private static Timer timer = new Timer();

    private static String[] hints = {
            "CS: Only one of the outputs should be on…!",
            "Physics: Inertia is the correct name for this phenomenon...if you’re forcing the answer out of me!",
            "Chemistry: M = mRT/pV",
            "Biology: This structure will make sure you don’t go hungry...even though it sounds like a super villain!"
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
                if(gameState != ScreenState.BEGIN && gameState != ScreenState.SUCCESS) {
                    currentTime -= 1;
                    timeText = getTimeString(currentTime);
                }

                if(currentTime < 0) {
                    gameState = ScreenState.FAILURE;
                }
            }
        }, 0, 1000);

        Button.setup();
        submitButton = new Button("Submit", width/2.0f - width/10.0f, height - height/6.0f, 3*width/14.4f, height/10.0f);
        nextButton = new Button("Next", width/2.0f - width/10.0f, height - height/6.0f, 3*width/14.4f, height/10.0f);
        quitButton = new Button("Quit", width/2.0f - width/10.0f, height - height/6.0f, 3*width/14.4f, height/10.0f);
    }

    @Override public void draw() {
        switch(gameState) {
            case LOCKOUT:
                background(0);
                textSize(30);
                fill(255, 0, 0);
                text("You have been locked out for attempting to cheat. Begone, fools. Academic dishonesty is a crime!", width/48.0f, height/2.0f, width - width/48.0f, height - height/90.0f);
                break;
            case BEGIN:
                background(0);
                fill(PApplet.unhex(GConstants.HACKER_GREEN));
                textSize(30);
                text("Somebody, anybody—I need help. I spent the summer watching anime and programming, and I...well, I didn’t do jack on my college apps. Or my EE. And, as a plus side, I think I’m going to fail the IB. \n" +
                        "\n" +
                        "This is bad. Like, real bad. I can’t think of a way out of this...unless...no. It could never work...and I come from light...but could I? My EE is predicted an E, and I have a 2 in every science...if I could just convince my teachers to boost my predicteds, and hack into Ms. Pfantz’ ManageBac, all would be well. I’m pretty sure her password is only 4 numbers long…\n" +
                        "\n" +
                        "Ah, crud, why are there so many emails in my inbox? From all the science teachers? Failing...failing...failing—wait! All of them are offering me a second chance! Guess I have to put my Netflix on hold for a bit. What’s this? “The science department met, and you need to prove to us that you aren’t going to fail. Solve these questions—each answer leads to the next—and you’ll pass.” Seems a little...I dunno, contrived, but okay, fine! [Hints trigger at certain time intervals, time starts when you hit next, shift to tab between screens!]", 10, 10, width - width/48.0f, height - height/90.0f);
                nextButton.draw();
                break;
            case PASSWORD:
                background(0);
                fill(PApplet.unhex(GConstants.HACKER_GREEN));
                textSize(150);
                text(input, width / 2.0f - 0.5f * textWidth(input), height / 2.0f);
                text("Password:", width/2.0f - 0.5f*textWidth("Password"), height/8.5f);
                textSize(80);
                fill(255, 0, 0);
                text(timeText, width - textWidth(timeText), height / 10.0f);
                submitButton.draw();
                break;
            case HINT:
                background(25);
                fill(PApplet.unhex(GConstants.HACKER_GREEN));
                textSize(80);
                text("Hints [24, 18, 12, 6]", width/2.0f - 0.5f*textWidth("Hints [35, 25, 15, 5]"), height/9.0f);
                textSize(45);

                if(currentTime < 1440) text(hints[0], width/20.0f, 2*height/10.0f, width - width/48.0f, height);
                if(currentTime < 1080) text(hints[1], width/20.0f, 4*height/10.0f, width - width/48.0f, height);
                if(currentTime < 720) text(hints[2], width/20.0f, 6*height/10.0f, width - width/48.0f, height);
                if(currentTime < 360) text(hints[3], width/20.0f, 8*height/10.0f, width - width/48.0f, height);

                fill(255, 0, 0);
                textSize(80);
                text(timeText, width - textWidth(timeText), height / 10.0f);
                break;
            case SUCCESS:
                background(0);
                textSize(45);
                text("Holy crud! We did it! I hacked into Ms. Pfantz' ManageBac, my teachers are gonna change my grades...you're a lifesaver!\n\nYour time was: " + getTimeString(maxTime - currentTime) + "!", width/48.0f, height/90.0f, width - width/48.0f, height - height/90.0f);
                text("Scores: ", width/48.0f, height*0.35f);
                for(int i = 0; i < scores.size(); i++) {
                    text((i+1)+") " + getTimeString(maxTime - Integer.parseInt(scores.get(i))), width/48.0f, height*0.35f + height/10.0f*(i+1));
                }
                break;
            case FAILURE:
                background(0);
                textSize(45);
                fill(255, 0, 0);
                text("Crap! Predicteds are in...I'm doomed. I'm gonna fail the IB...I...I...guess I'll just have to bribe my way into an Ivy League...", width/48.0f, height/2.0f, width, height);
                quitButton.draw();
                break;
            case CS_QUESTION:
                background(0);
                textSize(28);
                fill(PApplet.unhex(GConstants.HACKER_GREEN));
                text("Alright, from John…“I know you’re really into logic circuits, but you can’t spend all class drawing them on the white board...you’re failing right now”? Ugh, John, just get to the good stuff—can’t I just supercorrect, or have my mom email or something? I know my failing grade was on the report card, but still!\n" +
                        "\n" +
                        "Ah, there it is…“but, since I know you’re capable, you can solve this truth table and I’ll let this one pass.” Sweet. Sweet! \n" +
                        "\n" +
                        "What is this dang truth table…(A XOR B) AND (C NOR B)? XOR is true when they’re both different...AND when they’re both true...and NOR when they’re both false? Right? Okay, fine, I can do this. A is 4 ones then 4 zeroes, B is 2, 2, 2, 2, C is every other...cool. \n" +
                        "\n" +
                        "I need to send him back the correct output converted into decimal, though? I’m gonna need your help for that one…hey, if you add the digits of that number, it’ll probably be the first digit to Ms. Pfantz’ ManageBac password!\n" +
                        "\n" +
                        "I also have a weird suspicion that subtracting factors of the decimal from itself will help me master physics too, so do that once you’re done and search the room for that number.", 10, 100, width - width/48.0f, height - height/90.0f);
                fill(255, 0, 0);
                textSize(80);
                text(timeText, width - textWidth(timeText), height / 10.0f);
                break;
        }

    }

    @Override public void keyPressed(KeyEvent event) {
        if(gameState == ScreenState.PASSWORD) {
            switch (event.getKeyCode()) {
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
                    if (input.length() != 4) input += (event.getKey());
                    break;
                case 27:
                    if (!quitOnEscape) {
                        key = 0;
                    }
                    break;
                case 8:
                    if (input.length() > 0) input = input.substring(0, input.length() - 1);
                    break;
            }
        }
        if(event.getKeyCode() == 16) {
            if (gameState == ScreenState.PASSWORD) {
                gameState = ScreenState.HINT;
            } else if (gameState == ScreenState.HINT) {
                gameState = ScreenState.CS_QUESTION;
            } else if (gameState == ScreenState.CS_QUESTION) {
                gameState = ScreenState.PASSWORD;
            }
        }
    }

    @Override public void mouseClicked(MouseEvent event) {
       switch(gameState) {
           case PASSWORD:
               if (submitButton.isSelected()) {
                   if (input.equals(password) && currentTime > 0) {
                       gameState = ScreenState.SUCCESS;
                       logScore();
                       scores = getScores();
                       timer.cancel();
                   }
               }
               break;
           case BEGIN:
               if (nextButton.isSelected()) {
                   gameState = ScreenState.CS_QUESTION;
               }
               break;
           case FAILURE:
               if(quitButton.isSelected()) {
                   properQuit = true;
                   System.exit(0);
               }
               break;
       }
    }

    public static void logScore() {
        try {
            PrintWriter p  = new PrintWriter(new BufferedWriter(new FileWriter(new File("data" + File.separator + "scores.txt"))));
            p.println(currentTime);
            p.close();
        } catch(IOException e) {
            System.out.println(":(((");
        }
    }

    public static List<String> getScores() {
        try {
            return Files.readAllLines(Paths.get("data" + File.separator + "scores.txt"));
        } catch(IOException e) {
            System.out.println(":(((");
        }
        return null;
    }

    public static void setupLockoutState() {
        System.out.println("LS");
        try {
            BufferedReader b = new BufferedReader(new FileReader(new File("data" + File.separator + "status.txt")));
            String l;
            while((l = b.readLine()) != null) {
                System.out.println(l);
                if(l.trim().equals("0")) {
                    properQuit = true;
                } else if(l.trim().equals("1")){
                    properQuit = false;
                } else {
                    properQuit = true;
                }
            }
            b.close();
        } catch(IOException e) {
            System.out.println(":(((");
        }
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            if (gameState != ScreenState.SUCCESS && gameState != ScreenState.FAILURE) {
                try {
                    BufferedWriter w = new BufferedWriter(new FileWriter(new File("data" + File.separator + "status.txt")));
                    w.write("1");
                    w.close();
                } catch (IOException e) {
                    System.out.println(":(((");
                }
            }
        }));

        setupLockoutState();
        if(!properQuit) {
            gameState = ScreenState.LOCKOUT;
        }
        PApplet.runSketch(new String[]{"Group Four"}, app);
    }
}
