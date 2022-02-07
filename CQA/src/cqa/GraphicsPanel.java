package cqa;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * GraphicsPanel:
 * Holds the core mechanics of the visual interface.
 * Allowing for the adding of walls and controlling of them.
 * This is done via two view interfaces.
 *
 * @author Mitchell
 */
public class GraphicsPanel extends JPanel {
    // Definitions of the views and the switching component
    // "views" allows the viewing of one view at a time.
    private TopView topView;
    private SideView sideView;
    private JPanel views;

    // VIEWTOP and VIEWSIDE are used to reference the views
    private static final String VIEWTOP = "TopView";
    private static final String VIEWSIDE = "SideView";
    // isTopView is a simple variable to determine which view is active
    private boolean isTopView;

    // the two sliders that control both panels
    // only applies to the currently visible view
    private JScrollBar xScroll;
    private JScrollBar yScroll;

    // The name of the client the quote is for.
    private String client;
    // upper bar variables
    private JLabel clientLabel;
    private JLabel hintLabel;

    // Holds hints showing what user should be doing at any time.
    private static final String [] TEXTHINTS = { "Click wall segments to edit their bays.",
                                        "Click where you want wall placed.",
                                        "Click walls to remove them.",
                                        "Push and hold while dragging wall to move.",
                                        "Click a wall to edit the rotation.",
                                        "Click a wall to clone it.",
                                        "Select and drag bays up/down to change heights.",
                                        "Click on bays to edit their height value.",
                                        "Click to remove a bay." };

    // pointer to upper class
    private Main ref;

    /**
     * Constructor initiates the panels and the scroll bars.
     * Then switches to the default TopView.
     */
    public GraphicsPanel(Main ref) {
        this.ref = ref;
        setPreferredSize(new Dimension(450,400));
        setBackground(Color.lightGray);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1,2));
        clientLabel = new JLabel("Client name not set.");
        client = "";
        hintLabel = new JLabel("");
        hintLabel.setHorizontalTextPosition(JLabel.RIGHT);
        topPanel.add(clientLabel);
        topPanel.add(hintLabel);
        add(topPanel, BorderLayout.PAGE_START);

        // setup the two views
        views = new JPanel(new CardLayout());
        topView = new TopView(this, ref);
        sideView = new SideView(this, ref);
        views.add(topView, VIEWTOP);
        views.add(sideView, VIEWSIDE);
        add(views, BorderLayout.CENTER);

        // setup the X slider
        JPanel SliderPanelX = new JPanel();
        SliderPanelX.setPreferredSize(new Dimension(10, 20));
        SliderPanelX.setBackground(Color.lightGray);
        SliderPanelX.setLayout(new GridLayout(1,1));
        SliderListener listener = new SliderListener();
        xScroll = new JScrollBar(JScrollBar.HORIZONTAL);
        xScroll.addAdjustmentListener(listener);
        SliderPanelX.add(xScroll);
        add(SliderPanelX, BorderLayout.PAGE_END);

        // setup the Y slider
        JPanel SliderPanelY = new JPanel();
        SliderPanelY.setPreferredSize(new Dimension(20, 10));
        SliderPanelY.setBackground(Color.lightGray);
        SliderPanelY.setLayout(new GridLayout(1,1));
        yScroll = new JScrollBar(JScrollBar.VERTICAL);
        yScroll.addAdjustmentListener(listener);
        SliderPanelY.add(yScroll);
        add(SliderPanelY, BorderLayout.LINE_END);

        // default to the top view
        //updateBounds();
        gotoTopView();
        setTextHint(0);
        setInitialClientName();
    }


    /**
     * gotoTopView():
     * Switch the view to the top view.
     */
    public void gotoTopView() {
        CardLayout layout = (CardLayout)(views.getLayout());
        layout.show(views, VIEWTOP);
        isTopView = true;
        ref.swapButtons(isTopView);
        topView.repaint();
        xScroll.setMinimum(-20);
        xScroll.setValue(0);
        xScroll.setMaximum(20);
        yScroll.setMinimum(-20);
        yScroll.setValue(0);
        yScroll.setMaximum(20);
        updateBounds();
    }

    /**
     * gotoSideView(Wall wall):
     * Switch the view to the side view.
     *
     * @param wall - The wall to focus on.
     */
    public void gotoSideView(Wall wall) {
        sideView.setWallFocus(wall);
        xScroll.setMinimum(0);
        yScroll.setMinimum(0);

        CardLayout layout = (CardLayout)(views.getLayout());
        layout.show(views, VIEWSIDE);
        isTopView = false;
        ref.swapButtons(isTopView);
    }

    /**
     * clearTopView():
     * Empty existing structures.
     * Used to prepare for a load or a new quote.
     */
    public void clearTopView() {
        topView.clearTopView();
    }

    /**
     * loadWall(String wallData, String _bays):
     * Adds the specified wall to the collection of walls.
     * To be called when loading from a file.
     *
     * @param wallData - The main data about the wall (x,y,length, angle)
     * @param _bays - The bays all together.
     */
    public void loadWall(String wallData, String _bays) {
        topView.loadWall(wallData,_bays);
    }

    /**
     * getWallsSaveData():
     * Simply get the save data for all walls.
     *
     * @return
     */
    public String getWallsSaveData() {
        return topView.getWallsSaveData();
    }

    /**
     * setSliderXMax(int value):
     * Change the maximum value of the X slider to match
     * what it is told it should be by the panels.
     *
     * @param value - The value to set as maximum.
     */
    public void setSliderXMax(int value) {
        xScroll.setMaximum(value);
        xScroll.setValue(0);
    }

    /**
     * setSliderYMax(int value):
     * Change the maximum value of the Y slider to match
     * what it is told it should be by the panels.
     *
     * @param value - The value to set as maximum.
     */
    public void setSliderYMax(int value) {
        yScroll.setMaximum(value);
    }

    /**
     * void setSliderYValue(int value):
     * Changes the value on the Y Slider
     *
     * @param value - the value to set
     */
    public void setSliderYValue(int value) {
        yScroll.setValue(value);
    }

    /**
     * updateBounds():
     * This method is designed to be called specifically
     * by the TopView panel to update the sliders maximum and minimum values.
     */
    public void updateBounds() {
        // get the current bounding box
        int [] bounds = topView.getBoundBox();

        // get current slider values
        // need to make sure value still lies within
        // the new box
        int valueX = xScroll.getValue();
        int valueY = yScroll.getValue();

        // set X slider:
        int right = (bounds[1]/30 > 20) ? bounds[1]/30 : 20;
        int left = (bounds[0]/30 < -20) ? bounds[0]/30 : -20;
        xScroll.setMaximum(right);
        xScroll.setMinimum(left);
        /*if(valueX > bounds[1] || valueX < bounds[0]) {
            //updateOffsetX(0);
            xScroll.setValue(0);
        } else {
            //updateOffsetX(valueX);
            // TODO - check if this should actually happen
            // or whether should just be always reset to 0.
            xScroll.setValue(valueX);
        }*/

        // set Y slider:
        int top = (bounds[3]/30 > 20) ? bounds[3]/30 : 20;
        int bottom = (bounds[2]/30 < -20) ? bounds[2]/30 : -20;
        yScroll.setMaximum(top);
        yScroll.setMinimum(bottom);
        /*if(valueX > bounds[3] || valueX < bounds[2]) {
            //updateOffsetY(0);
            yScroll.setValue(0);
        } else {
            //updateOffsetY(valueY);
            yScroll.setValue(valueY);
        }*/
    }

    /**
     * setMode(int mode):
     * This method exists as the public interface for most
     * of the buttons to make their calls to. It enables the
     * functionality that is inside the panels.
     *
     * Modes match those in the panels as specified. Except for:
     * TopView has -1 remove, and 1 calls the add dialog instead.
     * SideView has 3 added which calls the add dialog for new bays.
     *
     * TopView Mouse modes:
     * 0 - edit mode
     * 1 - add mode
     * 2 - remove mode
     * 3 - move mode
     * 4 - rotate mode
     *
     * SideView Mouse modes:
     * 0 - set bay height by click and hold (release leaves set)
     * 1 - set bay height by dialog
     * 2 - remove bay
     * 3 - add mode
     *
     * @param mode - The mode to change to.
     */
    public void setMode(int mode) {
        if(isTopView) {
            if(mode < 0 || mode > 5) return;

            // start adding a new wall
            if(mode == 1) {
                topView.addWall();
                return;
            }
            topView.setMode(mode);
        } else {
            if(mode < 0 || mode > 3) return;

            // start adding new bay/s
            if(mode == 3) {
                sideView.addBays();
                return;
            }
            sideView.setMode(mode);
        }
    }

    /**
     * setTextHint():
     * Change the currently displayed text hint.
     * This will be called from TopView or SideView.
     *
     * @param mode - The mode to use to select the correct hint.
     */
    public void setTextHint(int mode) {
        if(isTopView) {
            // if mode is not valid
            if(mode < 0 || mode > 5) return;

            hintLabel.setText(TEXTHINTS[mode]);
        } else {
            // if mode is not valid
            if(mode < 0 || mode > 2) return;

            hintLabel.setText(TEXTHINTS[mode+6]);
        }
    }

    /**
     * setClient(String clientName):
     * Change the client name.
     *
     * @param clientName - The new name.
     */
    public void setClient(String clientName) {
        client = clientName;
        clientLabel.setText("Quotation for " + client);
    }

    /**
     * setInitialClientName():
     * Ask the user for a client name. It will not allow
     * continuation till a client name has been specified.
     */
    public void setInitialClientName() {
        String input = "";
        while(input == null || input.length() == 0) {
            input = JOptionPane.showInputDialog(null,
                                     "Enter a client name for this quotation:",
                                     "Enter Client Name", 1);
        }

        setClient(input);
    }

    /**
     * getClient():
     * Get the client name for this quotation.
     *
     * @return The client name.
     */
    public String getClient() {
        return client;
    }

    /**
     * getSleepers():
     * Get the number of sleepers that the total
     * set of walls include.
     *
     * @return The number of sleepers.
     */
    public int getSleepers() {
        return topView.getSleepers();
    }

    /**
     * updateOffsetX(int value):
     * Change the offset for the X coordinate
     * on the panel that is currently visible.
     *
     * @param value - The value to use as the offset.
     */
    private void updateOffsetX(int value) {
        if(isTopView) {
            topView.setOffsetX(value*-30);
        } else {
            sideView.setOffsetX(value);
        }
    }

    /**
     * updateOffsetY(int value):
     * Change the offset for the Y coordinate
     * on the panel that is currently visible.
     *
     * @param value - The value to use as the offset.
     */
    private void updateOffsetY(int value) {
        if(isTopView) {
            topView.setOffsetY(value*-30);
        } else {
            sideView.setOffsetY(yScroll.getMaximum()-value-10);
        }
    }

    /**
     * SliderListener:
     * Exists as the listener to handle the interactions with the scroll bars.
     */
    private class SliderListener implements AdjustmentListener  {

        /**
         * stateChanged(ChangeEvent e):
         * Handles the scroll event each time a move is made.
         *
         * @param e - The event information.
         */
        public void adjustmentValueChanged(AdjustmentEvent e) {
            if(e.getSource() == xScroll) {
                updateOffsetX(xScroll.getValue());
            } else {
                updateOffsetY(yScroll.getValue());
            }
        }
    }
}
