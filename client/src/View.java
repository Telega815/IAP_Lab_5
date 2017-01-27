import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class View extends JFrame{

    // left panel elements
    private static JPanel leftPanel;

    private static JTextArea xField, yField;

    private static JLabel xLabel, yLabel, rLabel;

    private static JComboBox<Integer> x;
    private static Integer[] xVars = new Integer[12];

    private static JPanel yPanel;
    private static JCheckBox[] yVars = new JCheckBox[12];

    private static JSlider rSlider;

    private static JButton setDot;
    public static JButton removeDots;
    
    private static JButton tryToConnect;
    
    private static JComboBox<String> language;
    private static String[] languages = {"English","Русский","Deutch"};

    // right panel elements
    private static Graph rightPanel;


    static Thread thread;


    public View(String s){
        super(s);
        setSize(1000,600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rightPanel = new Graph();
        add(rightPanel,BorderLayout.CENTER);
        rightPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                rightPanel.x = (int)e.getPoint().getX();
                rightPanel.y = (int)e.getPoint().getY();
                rightPanel.addDot();
                if (thread != null && thread.isAlive()) thread.stop();
                thread = new Thread(rightPanel);
                thread.start();
            }
        });

        drawLeftPart();
        add(leftPanel, BorderLayout.WEST);
    }

    private static void drawLeftPart (){
        // initialization ---------------------------------------------------------------
        new Localization();
        leftPanel = new JPanel();
        yPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(400,600));
        xField = new JTextArea("X = 0");
        yField  = new JTextArea("Y = 0");
        for (int i = 0; i<12; i++){
            xVars[i] = i - 6;
            yVars[i] = new JCheckBox(Integer.toString(i-6));
        }
        x = new JComboBox(xVars);
        xLabel = new JLabel("X = ");
        yLabel = new JLabel("Y = ");
        rLabel = new JLabel("R = ");
        rSlider = new JSlider(JSlider.HORIZONTAL, 1, 13, 10);
        setDot = new JButton("setDot");
        removeDots = new JButton("Remove all dots");
        tryToConnect = new JButton("Try to connect");
        
        language = new JComboBox(languages);

        // Setting ----------------------------------------------------------------------
        leftPanel.setLayout(new GridLayout(0,2, 4, 4));
        yPanel.setLayout(new GridLayout(0, 4, 4,4));

        yField.setEditable(false);
        xField.setEditable(false);

        x.setSelectedIndex(6);

        yVars[6].setSelected(true);
        yVars[6].disable();

        rSlider.setMajorTickSpacing(4);
        rSlider.setMinorTickSpacing(1);
        rSlider.setPaintTicks(true);
        rSlider.setPaintLabels(true);

        language.setSelectedIndex(0);
        // events ------------------------------------------------------------------------
        x.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xField.setText("X = " + Integer.toString((int)x.getSelectedItem()));
                rightPanel.setX(((double)(int)x.getSelectedItem()));
            }
        });

        for (int i = 0; i<12; i++){
            yVars[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JCheckBox box = (JCheckBox)e.getSource();
                    for(int i = 0; i < 12; i++){
                        yVars[i].enable();
                        yVars[i].setSelected(false);
                    }
                    box.disable();
                    box.setSelected(true);
                    yField.setText("Y = " + box.getText());
                    rightPanel.setY(Integer.parseInt(box.getText()));
                }
            });
        }

        rSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (thread != null && thread.isAlive()) thread.stop();
                rightPanel.setR(rSlider.getValue());
                rightPanel.removeDots();
                rightPanel.repaint();
            }
        });

        setDot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightPanel.setX((double) (int)x.getSelectedItem());
                for(int i = 0; i < 12; i++){
                    if(yVars[i].isSelected()) rightPanel.setY(Double.parseDouble(yVars[i].getText()));
                }
                rightPanel.addDot();
                if (thread != null && thread.isAlive()) thread.stop();
                thread = new Thread(rightPanel);
                thread.start();
            }
        });

        removeDots.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightPanel.removeDots();
            }
        });
        
        tryToConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightPanel.tryToConnect();
            }
        });
        
        language.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(language.getSelectedIndex());
                setDot.setText(Localization.Langs.get(language.getSelectedIndex()).setDot);
                removeDots.setText(Localization.Langs.get(language.getSelectedIndex()).removeDots);
                tryToConnect.setText(Localization.Langs.get(language.getSelectedIndex()).tryToConnect);
            }
        });

        // Attaching components ---------------------------------------------------------
        for (int i = 0; i<12; i++){
            yPanel.add(yVars[i]);
        }
        leftPanel.add(xField);
        leftPanel.add(yField);
        leftPanel.add(xLabel);
        leftPanel.add(x);
        leftPanel.add(yLabel);
        leftPanel.add(yPanel);
        leftPanel.add(rLabel);
        leftPanel.add(rSlider);
        leftPanel.add(setDot);
        leftPanel.add(removeDots);
        leftPanel.add(tryToConnect);
        leftPanel.add(language);
    }



}
