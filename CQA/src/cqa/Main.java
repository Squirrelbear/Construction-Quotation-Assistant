/*
 * Main.java
 *
 * @version 1.1.3
 * Created on 20/03/2011, 12:33:18 PM
 */

package cqa;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.StringTokenizer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Main:
 * Handles the displaying and creation of the upper level
 * JFrame for the application as well as the join between the
 * different panels.
 *
 * @author BeN
 */
public class Main extends javax.swing.JFrame {




    /**
     * initComponents():
     * Configure the components for each panel of the main application.
     * Then add then to the appropriate locations.
     */
    private void initComponents() {

        setTitle("Construction Quotation Assistant V1.1.3");
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        addButtons();
        initComponentsRight();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1366, 768));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        // Buttons panel
        jPanel1.setBackground(new java.awt.Color(204, 102, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(64, 76800));
        jPanel1.setMinimumSize(new java.awt.Dimension(64, 768));
        jPanel1.setPreferredSize(new java.awt.Dimension(64, 768));
        jPanel1.setLayout(new GridLayout(1,1));//jPanel1Layout);
        getContentPane().add(jPanel1);

        // Graphics Panel
        jPanel2.setBackground(new java.awt.Color(255, 0, 255));
        jPanel2.setLayout(new GridLayout(1,1));
        graphicsPanel = new GraphicsPanel(this);
        jPanel2.add(graphicsPanel);
        getContentPane().add(jPanel2);

        // Calculation Panel
        jPanel3.setMaximumSize(new java.awt.Dimension(210, 76800));
        jPanel3.setMinimumSize(new java.awt.Dimension(210, 768));
        jPanel3.setPreferredSize(new java.awt.Dimension(210, 768));
        jPanel3.setLayout(new GridLayout(15,1));
        getContentPane().add(jPanel3,  BorderLayout.CENTER);

        pack();

        updateTimer.start();
    }

    /**
     * initComponentsRight():
     * Create all the components for the calculation panel.
     * Then add them to the panel and set the defaults.
     */
    public void initComponentsRight(){
        buttonGroup = new ButtonGroup();
        //Cost = new JPanel();
        titleLabel = new JLabel();
        Normal = new JRadioButton();
        Sandy = new JRadioButton();
        Limestone = new JRadioButton();
        Bluestone = new JRadioButton();
	myEventHandler myActionListener = new myEventHandler();
        myChangeHandler myChangeListener = new myChangeHandler();
        Local = new JCheckBox();
        Access = new JCheckBox();
        Totals = new JPanel();
        wallCostLabel = new JLabel();
        wallCostLabel2 = new JLabel();
        wallCostLabel3 = new JLabel();
        multiCostLabel = new JLabel();
        multiCostLabel2 = new JLabel();
        multiCostLabel3 = new JLabel();
        totalCostLabel = new JLabel();
        totalCostLabel2 = new JLabel();
        totalCostLabel3 = new JLabel();
        sleeperCount = new JLabel();
        meterLength = new JLabel();

        setMaximumSize(new Dimension(210, 768));
        setMinimumSize(new Dimension(210, 768));
        setPreferredSize(new Dimension(210, 768));
        setLayout(new BorderLayout());

        titleLabel.setText("CURRENT ORDER");
        jPanel3.add(titleLabel);

        jPanel3.add(new JSeparator(SwingConstants.HORIZONTAL));

        buttonGroup.add(Normal);
        Normal.setLabel("Normal");
        Normal.addActionListener(myActionListener);
        Normal.setActionCommand("0");
        jPanel3.add(Normal);

        buttonGroup.add(Sandy);
        Sandy.setLabel("Sandy");
        Sandy.addActionListener(myActionListener);
	Sandy.setActionCommand("10");
        jPanel3.add(Sandy);

        buttonGroup.add(Limestone);
        Limestone.setLabel("Limestone");
        Limestone.addActionListener(myActionListener);
        Limestone.setActionCommand("15");
        jPanel3.add(Limestone);

        buttonGroup.add(Bluestone);
        Bluestone.setLabel("Bluestone");
        Bluestone.addActionListener(myActionListener);
        Bluestone.setActionCommand("30");
        jPanel3.add(Bluestone);

        jPanel3.add(new JSeparator(SwingConstants.HORIZONTAL));

        Local.setLabel("Not Built Locally");
        Local.addChangeListener(myChangeListener);
        Local.setRolloverEnabled(false);
        jPanel3.add(Local);

        Access.setLabel("Poor Site Access");
        Access.addChangeListener(myChangeListener);
        Access.setRolloverEnabled(false);
        jPanel3.add(Access);

        jPanel3.add(new JSeparator(SwingConstants.HORIZONTAL));

        jPanel3.add(sleeperCount);
        jPanel3.add(meterLength);

        jPanel3.add(new JSeparator(SwingConstants.HORIZONTAL));

        Totals.setLayout(new GridLayout(3, 3));

        wallCostLabel.setText("Wall Cost");
        Totals.add(wallCostLabel);
        Totals.add(wallCostLabel2);
        Totals.add(wallCostLabel3);

        multiCostLabel.setText("Multiplier");
        multiCostLabel.setPreferredSize(new Dimension(50,10));
        Totals.add(multiCostLabel);
        Totals.add(multiCostLabel2);
        Totals.add(multiCostLabel3);

        totalCostLabel.setText("Total Cost");
        Totals.add(totalCostLabel);
        Totals.add(totalCostLabel2);
        Totals.add(totalCostLabel3);

        jPanel3.add(Totals);

        // init the panel defaults
        Normal.setSelected(true);
        multiCostLabel2.setText("0%");
        notBuiltLocally = false;
        poorSiteAccess = false;
        difficulty = 0;
        updateTimer = new javax.swing.Timer(1000, new updateTimerListener());
    }

    /**
     * main():
     * Exists as the entry point for the program.
     *
    * @param args the command line arguments
    */
    public static void main(String args[]) throws AWTException, IOException  {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    /**
     * Main():
     * Starts the program by calling for
     * the generating of the frame.
     */
    public Main(){
        initComponents();
    }

    /**
     * addButtons():
     * Loads the images for and then adds the
     * various buttons that will populate the
     * button panel. These are used to interact
     * with the GraphicsPanel.
     */
    public void addButtons() {
        String picArr[] = new String[14];

        //System.out.println("Components Initialised");
        String imgDir = "\\images\\";
        picArr[1] = imgDir + "back.png";
        picArr[2] = imgDir + "add_bay.png";
        picArr[3] = imgDir + "add_wall.png";
        picArr[4] = imgDir + "remove_wall.png";
        picArr[5] = imgDir + "remove_bay.png";
        picArr[6] = imgDir + "rotate_wall.png";
        picArr[7] = imgDir + "set_bay_height.png";
        picArr[8] = imgDir + "edit_wall.png";
        picArr[9] = imgDir + "set_bay_height_drag.png";
        picArr[0] = imgDir + "move_wall.png";
        picArr[10] = imgDir + "save.png";
        picArr[11] = imgDir + "load.png";
        picArr[12] = imgDir + "clone.png";
        picArr[13] = imgDir + "new.png";

        backLabel = new JButton(new ImageIcon(getClass().getResource(picArr[1])));
        addBayLabel = new JButton(new ImageIcon(getClass().getResource(picArr[2])));
        addWallLabel = new JButton(new ImageIcon(getClass().getResource(picArr[3])));
        remWallLabel = new JButton(new ImageIcon(getClass().getResource(picArr[4])));
        remBayLabel = new JButton(new ImageIcon(getClass().getResource(picArr[5])));
        rotWallLabel = new JButton(new ImageIcon(getClass().getResource(picArr[6])));
        setBHeightLabel = new JButton(new ImageIcon(getClass().getResource(picArr[7])));
        editWallLabel = new JButton(new ImageIcon(getClass().getResource(picArr[8])));
        moveWallLabel = new JButton(new ImageIcon(getClass().getResource(picArr[0])));
        setBayHeightDrag = new JButton(new ImageIcon(getClass().getResource(picArr[9])));
        saveButton = new JButton(new ImageIcon(getClass().getResource(picArr[10])));
        loadButton = new JButton(new ImageIcon(getClass().getResource(picArr[11])));
        copyButton = new JButton(new ImageIcon(getClass().getResource(picArr[12])));
        newButton = new JButton(new ImageIcon(getClass().getResource(picArr[13])));

        myButtonHandler myButtonListener = new myButtonHandler();

        backLabel.addMouseListener(myButtonListener);
        addBayLabel.addMouseListener(myButtonListener);
        addWallLabel.addMouseListener(myButtonListener);
        remWallLabel.addMouseListener(myButtonListener);
        remBayLabel.addMouseListener(myButtonListener);
        rotWallLabel.addMouseListener(myButtonListener);
        setBHeightLabel.addMouseListener(myButtonListener);
        editWallLabel.addMouseListener(myButtonListener);
        moveWallLabel.addMouseListener(myButtonListener);
        setBayHeightDrag.addMouseListener(myButtonListener);
        saveButton.addMouseListener(myButtonListener);
        loadButton.addMouseListener(myButtonListener);
        copyButton.addMouseListener(myButtonListener);
        newButton.addMouseListener(myButtonListener);

        // Create the multi view
        buttonSwitcher = new JPanel(new CardLayout());
        topViewButtons = new JPanel();
        topViewButtons.setLayout(new GridLayout(10, 1));
        sideViewButtons = new JPanel();
        sideViewButtons.setLayout(new GridLayout(10, 1));

        // add the buttons to the respective panels
        sideViewButtons.add(saveButton);
        sideViewButtons.add(loadButton);
        sideViewButtons.add(backLabel);
        sideViewButtons.add(addBayLabel);
        sideViewButtons.add(setBayHeightDrag);
        sideViewButtons.add(setBHeightLabel);
        sideViewButtons.add(remBayLabel);
        topViewButtons.add(newButton);
        topViewButtons.add(saveButton);
        topViewButtons.add(loadButton);
        topViewButtons.add(addWallLabel);
        topViewButtons.add(editWallLabel);
        topViewButtons.add(moveWallLabel);
        topViewButtons.add(rotWallLabel);
        topViewButtons.add(copyButton);
        topViewButtons.add(remWallLabel);

        // combine the Jpanels into the one
        buttonSwitcher.add(topViewButtons, VIEWTOP);
        buttonSwitcher.add(sideViewButtons, VIEWSIDE);
        jPanel1.add(buttonSwitcher);
    }

    /**
     * swapButtons(boolean toTopView):
     * Switches the displayed buttons.
     * Depending on whether the view to switch to is topView or not.
     *
     * @param toTopView - is new view: top view.
     */
    public void swapButtons(boolean toTopView){
        CardLayout layout = (CardLayout)(buttonSwitcher.getLayout());

        if(toTopView){
            layout.show(buttonSwitcher, VIEWTOP);
        } else {
            layout.show(buttonSwitcher, VIEWSIDE);
        }
    }

    /**
     * updateMultiplier():
     * Calculates the cost multiplier and updates
     * the label. It also returns the value in case needed.
     *
     * @return The multiplier to value.
     */
    public int updateMultiplier(){
        multiplier = difficulty;
        if(notBuiltLocally) {
            multiplier += 5;
        }
        if(poorSiteAccess) {
            multiplier += 30;
        }
        multiCostLabel2.setText(multiplier + "%");
        return multiplier;
    }

    /**
     * updatePanel():
     * Updates the calculation panel.
     * Recalculates all the values based on the
     * current state of all elements.
     */
    public void updatePanel (){
        int sleepers = graphicsPanel.getSleepers();
        double meterTotal = (double)sleepers * 2.0 * 0.2;
        // sometimes cast between int and double gives strange numbers
        // so cast and cast back to make it always appear as desired
        double meterTotalFixed = ((double)((int)(meterTotal * 10.0)))/10.0;
        double subTotal = meterTotalFixed * 425.0;
        double multiTotal = (subTotal * multiplier) / 100.0;
        double grandTotal = subTotal + multiTotal;
        sleeperCount.setText("Sleeper Count: " + sleepers);
        meterLength.setText("Total in Meters: " + meterTotalFixed + "m2");
        wallCostLabel3.setText("$" + subTotal);
        multiCostLabel3.setText("+$" + multiTotal);
        totalCostLabel3.setText("$" + grandTotal);
    }

    /**
     * updateTimerListener:
     * Used to listen even 1 second and update
     * the cost information. It is cheaper than updating every single
     * time a tiny change is made.
     */
    private class updateTimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
                updatePanel();
        }
    }

    /**
     * myButtonHandler:
     * Used to handle the button presses in the Button Panel.
     */
     private class myButtonHandler implements MouseListener {
        public void mouseClicked (MouseEvent e){
            if(e.getSource() == backLabel){
                graphicsPanel.gotoTopView();
            }else if(e.getSource() == editWallLabel || e.getSource() == setBayHeightDrag){
                graphicsPanel.setMode(0);
            }else if(e.getSource() == addWallLabel || e.getSource() == setBHeightLabel){
                graphicsPanel.setMode(1);
            }else if(e.getSource() == remWallLabel || e.getSource() == remBayLabel){
                graphicsPanel.setMode(2);
            }else if(e.getSource() == addBayLabel || e.getSource() == moveWallLabel){
                graphicsPanel.setMode(3);
            }else if(e.getSource() == rotWallLabel){
                graphicsPanel.setMode(4);
            } else if(e.getSource() == saveButton) {
                save();
            } else if(e.getSource() == loadButton) {
                load();
            } else if(e.getSource() == copyButton) {
                graphicsPanel.setMode(5);
            } else if(e.getSource() == newButton) {
                newQuote();
            }

	}

        public void mousePressed(MouseEvent e) {
            //System.out.println("Button " + e.getSource() + " has been pressed");
        }

        public void mouseReleased(MouseEvent e) {
            //System.out.println("Button " + e.getSource() + " has been pressed");
        }

        public void mouseEntered(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void mouseExited(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }
    }

     /**
      * myEventHandler:
      * Handles the selection of and changing of the
      * radio buttons in the calculation panel.
      * Multipliers will be updated as needed.
      */
    private class myEventHandler implements ActionListener {
        public void actionPerformed (ActionEvent e){
            difficulty = Integer.parseInt(e.getActionCommand().trim());
            updateMultiplier();
            updatePanel();
	}
    }

    /**
     * myChangeHandler:
     * Handles the selecting and deselecting of
     * the checkboxes in the calculation panel.
     * Multipliers will be updated as needed.
     */
    private class myChangeHandler implements ChangeListener {
        public void stateChanged (ChangeEvent e){
            JCheckBox theCheckBox = (JCheckBox)e.getSource();
            if(e.getSource() == Local) {
                notBuiltLocally = theCheckBox.isSelected();
            } else {
                poorSiteAccess = theCheckBox.isSelected();
            }
            updateMultiplier();
            updatePanel();
        }
    }

    /**
     * save():
     * Allows the user to save a quote using a particular file name.
     * The save files are placed in the root of \\src\\cqa\\saves\\.
     * Each save file is give the extension ".quote".
     * If a failure to save the information occurs the method will stop
     * and display an error message stating that saving failed and explain
     * partly why.
     */
    private void save() {
        String fileName = "";
        fileName = JOptionPane.showInputDialog(this,
                                     "Enter a filename to save as:",
                                     "Save Quote", 1);
        if(fileName == null || fileName.length() == 0) {
            JOptionPane.showMessageDialog(this, "Filename not valid! File will not be saved.",
                                           "Warning!",
                                           JOptionPane.WARNING_MESSAGE);
            return;
        }

        String filePath = System.getProperty("user.dir") + "\\src\\cqa\\saves\\" + fileName + ".quote";
        PrintWriter out;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
        }  catch(IOException e) {
            JOptionPane.showMessageDialog(this, "Save failed! Unable to create connection to output file.",
                                           "ERROR!",
                                           JOptionPane.ERROR_MESSAGE);
            return;
        }

        // output all the data to the file
        String clientString = graphicsPanel.getClient()+"\n";
        out.write(clientString,0,clientString.length());
        String calcOptionsString = difficulty + " " + notBuiltLocally + " " + poorSiteAccess + "\n";
        out.write(calcOptionsString,0,calcOptionsString.length());
        String wallDataString = graphicsPanel.getWallsSaveData();
        out.write(wallDataString,0,wallDataString.length());

        // complete saving the file
        out.flush();
        out.close();

        JOptionPane.showMessageDialog(this, "Save successful! Output has been placed in " + fileName,
                                           "Sucess!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * load():
     * Allows the user to load an existing quote file.
     * The user will have to type in the name of the file (excluding extension).
     * It will attempt to read the information in and wipe the
     * existing information in the panel.
     * There are a number of issues that could occur with insufficient information
     * supplied from attempting to load a corrupt or tampered file. But that is
     * out of the scope of this current implementation.
     */
    private void load() {
        String fileName = "";
        fileName = JOptionPane.showInputDialog(null,
                                     "Enter a filename to load:",
                                     "Load Quote", 1);
        if(fileName == null || fileName.length() == 0) {
            JOptionPane.showMessageDialog(this, "Filename not valid! File path can't be created.",
                                           "Warning!",
                                           JOptionPane.WARNING_MESSAGE);
            return;
        }

        String filePath = System.getProperty("user.dir") + "\\src\\cqa\\saves\\" + fileName + ".quote";
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(filePath));
        } catch(FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Filename not valid! File does not exist.",
                                           "Warning!",
                                           JOptionPane.ERROR_MESSAGE);
            return;
        }

        // prepare to load by clearing existing data
        graphicsPanel.clearTopView();

        String quoteName;
        try {
            quoteName = in.readLine();
            graphicsPanel.setClient(quoteName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, fileName + " did not contain valid data. File load will not continue.",
                                           "Error!",
                                           JOptionPane.ERROR_MESSAGE);
            return;
        }

        String calcOptions;
        try {
            calcOptions = in.readLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, fileName + " did not contain valid data. File load will not continue.",
                                           "Error!",
                                           JOptionPane.ERROR_MESSAGE);
            return;
        }
        StringTokenizer calcTokens = new StringTokenizer(calcOptions);
        difficulty = Integer.parseInt((String)calcTokens.nextElement());
        notBuiltLocally = ((String)(true+"")).equals((String)calcTokens.nextElement());
        poorSiteAccess = ((String)(true+"")).equals((String)calcTokens.nextElement());
        switch(difficulty) {
            case 0:
                Normal.setSelected(true);
                break;
             case 10:
                Sandy.setSelected(true);
                break;
             case 15:
                Limestone.setSelected(true);
                break;
             case 30:
                Bluestone.setSelected(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, fileName + " did not contain valid data. File load will not continue.",
                                           "Error!",
                                           JOptionPane.ERROR_MESSAGE);
                return;
        }
        Access.setSelected(poorSiteAccess);
        Local.setSelected(notBuiltLocally);

        String wallData;
        String _bays;
        try {
            while((wallData = in.readLine()) != null) {
                _bays = in.readLine();
                graphicsPanel.loadWall(wallData, _bays);
            }
        } catch(IOException e) {
            JOptionPane.showMessageDialog(this, fileName + " did not contain valid data. File load will not continue.",
                                           "Error!",
                                           JOptionPane.ERROR_MESSAGE);
            return;
        }

        // load is complete
        graphicsPanel.gotoTopView();

    }

    /**
     * newQuote():
     * Clear the existing quote ready for a new one.
     */
    private void newQuote() {
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to continue? All current elements will be lost!",
                                                     "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if(response == JOptionPane.NO_OPTION) return;

        // configure the calculation panel
        Normal.setSelected(true);
        difficulty = 0;
        poorSiteAccess = false;
        notBuiltLocally = false;
        Access.setSelected(poorSiteAccess);
        Local.setSelected(notBuiltLocally);

        // clear and focus on top view
        graphicsPanel.clearTopView();
        graphicsPanel.gotoTopView();

        graphicsPanel.setInitialClientName();
    }

    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private JCheckBox Access;
    private JRadioButton Bluestone;
    private JRadioButton Limestone;
    private JCheckBox Local;
    private JRadioButton Normal;
    private JRadioButton Sandy;
    private JPanel Totals;
    private ButtonGroup buttonGroup;
    private JLabel titleLabel;
    private JLabel multiCostLabel3;
    private JLabel totalCostLabel2;
    private JLabel wallCostLabel;
    private JLabel multiCostLabel;
    private JLabel totalCostLabel;
    private JLabel wallCostLabel2;
    private JLabel wallCostLabel3;
    private JLabel totalCostLabel3;
    private JLabel multiCostLabel2;
    private JLabel sleeperCount;
    private JLabel meterLength;
    javax.swing.Timer updateTimer;
    private boolean poorSiteAccess;
    private boolean notBuiltLocally;
    private int multiplier;
    private int difficulty;
    private JButton backLabel;
    private JButton addBayLabel;
    private JButton addWallLabel;
    private JButton remWallLabel;
    private JButton remBayLabel;
    private JButton rotWallLabel;
    private JButton setBHeightLabel;
    private JButton editWallLabel;
    private JButton moveWallLabel;
    private JButton setBayHeightDrag;
    private JButton loadButton;
    private JButton saveButton;
    private JButton copyButton;
    private JButton newButton;

    private GraphicsPanel graphicsPanel;
    private JPanel topViewButtons;
    private JPanel sideViewButtons;
    private final String VIEWTOP = "VIEWTOP";
    private final String VIEWSIDE = "VIEWSIDE";
    private JPanel buttonSwitcher;
}
