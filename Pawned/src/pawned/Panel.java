package pawned;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Panel extends JPanel{
    private Image background;
    // Game types
    private Game       dGame;//for drawing only
    private playedGame pGame;
    private watchGame  wGame;
    
    // Watch parameters
    private Timer watchTimer;
    private float watchSpeed = 1.0f;
    
    // Listeners
    private ActionListener menuAction;
    private MouseListener  mouseAdapter;
    
    // Menu
    private JMenu[]      menu;
    private JFileChooser browser;

    
    public Panel(){
        setPreferredSize(new Dimension(641, 641));
        setFocusable(true);
        
        initListeners();
        
        menu = new JMenu[]{
            new JMenu("File"),
            new JMenu("Options"),
            new JMenu("PlayBack")
        };
        
        initFileMenu();
        initOptionMenu();
        initPlaybackMenu();
        
        pGame = null;
        wGame = null;
        dGame = null;
        
        background = new ImageIcon(this.getClass().getResource("icons/chessboard.gif")).getImage();
    }
    
    // Listeners
    private void initListeners(){
        browser      = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PGN FILE","pgn");
        browser.setFileFilter(filter);
        menuAction   = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                command(((JMenuItem)ae.getSource()).getText());
            }
        };
        mouseAdapter = new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                pGame.select(location.locationize(e.getX(), e.getY()));
                repaint();
            }
        };
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_RIGHT:
                        command("Forward");
                        break;
                    case KeyEvent.VK_LEFT:
                        command("Backward");
                        break;
                    case KeyEvent.VK_P:
                        command("Play/Pause");
                        break;
                    case KeyEvent.VK_S:
                        command("Stop");
                        break;

                    case KeyEvent.VK_UP:
                        increaseSpeed();
                        break;
                    case KeyEvent.VK_DOWN:
                        decreaseSpeed();
                        break;
                    case KeyEvent.VK_I:
                        command("Invert");
                        break;
                }
            }
        });
        
        watchTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(wGame == null || !wGame.forward())
                    watchTimer.stop();
                else
                    repaint();
            }
        });
        
    }
    
    // Menu methods
    public JMenu[] getMenu(){
        return menu;
    }
    private void initFileMenu(){
        JMenuItem[] mItems = {
            new JMenuItem("Play"),
            new JMenuItem("Open"),
            new JMenuItem("Exit")
        };
        for(JMenuItem mi : mItems){
            mi.addActionListener(menuAction);
            menu[0].add(mi);
        }
    }
    private void initOptionMenu(){
        JMenuItem[] mItems = {
            new JMenuItem("Invert                                          [i]"),
            new JMenuItem("Increase  PlayBackSpeed    [↑]"),
            new JMenuItem("Decrease PlayBackSpeed   [↓]")
        };
        for(JMenuItem mi : mItems){
            mi.addActionListener(menuAction);
            menu[1].add(mi);
        }
    }
    private void initPlaybackMenu(){
        JMenuItem[] mItems = {
            new JMenuItem("Play/Pause  [P]"),
            new JMenuItem("Stop              [S]"),
            new JMenuItem("Forward       [→]"),
            new JMenuItem("Backward   [←]")
        };
        for(JMenuItem mi : mItems){
            mi.addActionListener(menuAction);
            menu[2].add(mi);
        }
    }
    
    // Initialize specified game
    private void initPlay(){
        Object[] options = {"White", "Black"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose your color.",
                "Play Chess",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null);
        if(choice == -1)
            return;
        
        char c;
        boolean inversion;
        if(choice == 0){
            c = 'W';
            inversion = false;
        }else{
            c = 'B';
            inversion = true;
        }
        
        handleMouse(true);
        pGame = new playedGame(inversion, c);
        dGame = pGame;
        wGame = null;
        repaint();
    }
    private void initWatch(){
        if((browser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION ) )
            return;
        
        handleMouse(false);
        wGame = new watchGame(false, browser.getSelectedFile());
        dGame = wGame;
        pGame = null;
        repaint();
    }
    
    private void increaseSpeed(){
        if(watchSpeed - 0.25 >= 0.25 && wGame != null){
            watchSpeed -= 0.25f;
            watchTimer.setDelay((int)(watchSpeed * 1000));
        }
    }
    private void decreaseSpeed(){
        if(watchSpeed + 0.25 <= 5 && wGame != null){
            watchSpeed += 0.25f;
            watchTimer.setDelay((int)(watchSpeed * 1000));
        }
    }
    
    private void command(String str){
        String cut = str.split(" ")[0];
        switch(cut){
            case "Play":
                initPlay();
                break;
            case "Open":
                initWatch();
                break;
            case "Exit":
                System.exit(0);
                break;

            // Options
            case "Invert":
                invert();
                break;
            case "Increase":
                increaseSpeed();
                break;
            case "Decrease":
                decreaseSpeed();
                break;

            // Playback
            case "Play/Pause":
                if(watchTimer.isRunning())
                    watchTimer.stop();
                else
                    watchTimer.start();
                break;
            case "Stop":
                watchTimer.stop();
                if(wGame != null){
                   wGame.rewind();
                   repaint();
                }
                break;
            case "Forward":
                if(wGame != null){
                    wGame.forward();
                    repaint();
                }
                break;
            case "Backward":
                if(wGame != null){
                    wGame.backward();
                    repaint();
                }
                break;
        }
    }
    private void handleMouse(boolean set){
        int length = getMouseListeners().length;
        if(set){
            if(length == 0)
                addMouseListener(mouseAdapter);
        }else{
            if(length != 0)
                removeMouseListener(mouseAdapter);
        }
    }
    // Drawing components
    public void invert(){
        if(dGame != null)
            dGame.invert();
        repaint();
    }
    public void paint(Graphics g){
        super.paint(g);
        if(dGame != null)
            dGame.draw(g);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
    }
}