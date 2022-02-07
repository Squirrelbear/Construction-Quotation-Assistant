package cqa;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * SideView:
 * Allows the viewing of a single wall from the side.
 * It displays a series of bay heights and allows the
 * modifying of these heights and the adding and removal
 * of bays.
 *
 * @author Mitchell
 */
public class SideView extends JPanel {

    // currently selected wall
    private Wall curWall;

    // Screen offsets and the maximum screen offsets
    private int offsetX;
    private int offsetY;
    private int maxOffsetX;
    private int maxOffsetY;

    // defined constants for the width and height of various parts
    private final int BAYWIDTH = 100;
    private final int SLEEPERHEIGHT = 15;
    private final int BOTTOMHEIGHT = 20; // area under wall for text

    /*
     * Mouse modes:
     * 0 - set bay height by click and hold (release leaves set)
     * 1 - set bay height by dialog
     * 2 - remove bay
     */
    private int mousemode;
    // The currently active bay
    private int targetBay;

    // pointer to the GraphicsPanel
    private GraphicsPanel ref;

    // The dialog to add bays with
    private AddBay addBay;

    /**
     * Constructor SideView():
     * COnfigures the vanilla view with no particular wall.
     *
     * @param ref The pointer to the GraphicsPanel.
     */
    public SideView(GraphicsPanel ref, Main main) {
        this.ref = ref;

        setPreferredSize(new Dimension(450,400));
        setBackground(Color.white);
        setLayout(null);

        MListener listener = new MListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);

        addBay = new AddBay(this, main);
    }

    /**
     * setWallFocus(Wall wall):
     * This method sets the focus on a particular wall.
     * It calculates all of the offsets that will be allowed for the sliders.
     * It will be called whenever switching to this SideView from TopView,
     * it will also be called when there are extra bays added.
     *
     * @param wall - The wall to set focus on and use in the side view.
     */
    public void setWallFocus(Wall wall) {
        curWall = wall;
        offsetX = offsetY = 0;
        setMode(0);
        targetBay = -1;

        // configure the X and Y sliders
        maxOffsetX = (curWall.getBays().size() - getWidth()/BAYWIDTH <= 0) ? 0 : (curWall.getBays().size() - getWidth()/BAYWIDTH);
        ref.setSliderXMax(maxOffsetX);
        updateMaxOffsetY();
        ref.setSliderYValue(maxOffsetY);

        repaint();
    }

    /**
     * getMethod getMaxOffsetX():
     * Gets the current maximum offset.
     *
     * @return The maximum offset on the X axis.
     */
    public int getMaxOffsetX() {
        return maxOffsetX;
    }

    /**
     * getMethod getMaxOffsetY():
     * Gets the current maximum offset.
     *
     * @return The maximum offset on the Y axis.
     */
    public int getMaxOffsetY() {
        return maxOffsetY;
    }

    /**
     * setMode(int mode):
     * Changes the mode of the mouse to the specified mode if it is valid.
     *
     * @param mode - mode to change to.
     */
    public void setMode(int mode) {
        if(mode < 0 || mode > 2) return;
        mousemode = mode;
        ref.setTextHint(mode);
    }

    /**
     * addBays():
     * Opens a dialog allowing for more bays to be added.
     */
    public void addBays()
    {
        // show a dialog
        addBay.viewDialog();
    }

    /**
     * addBays(boolean left, int count, int sleepers):
     * Adds the specified set of bays to the current wall,
     * then it resets the configuration of the offsets.
     *
     * @param left - left or right
     * @param count - the number of bays to add
     * @param sleepers - The number of sleepers to set the bays to.
     */
    public void addBays(boolean left, int count, double height) {
        curWall.addBays(left, count, height);

        // reset the dialog
        setWallFocus(curWall);
    }

    /**
     * setOffsetX(int X):
     * Changes the current X offset.
     *
     * @param X - The new X offset.
     */
    public void setOffsetX(int X) {
        offsetX = X;

        repaint();
    }

    /**
     * setOffsetY(int Y):
     * Changes the current Y offset.
     *
     * @param Y - The new Y offset.
     */
    public void setOffsetY(int Y) {
        offsetY = Y;

        repaint();
    }

    /**
     * paintComponent(Graphics page):
     * Displays the visible bays using given offset.
     *
     * @param page - The graphics surface to print to.
     */
    @Override
    public void paintComponent(Graphics page)
    {
        super.paintComponent(page);
        page.setColor( Color.black );

        for(int i = 0; i < Math.ceil(getWidth()*1.0/BAYWIDTH); i++) {
            if(i+offsetX > curWall.getBays().size()-1) break;
            int sleepers = curWall.getBays().get(i+offsetX).getSleepers();

            // display number of sleepers below bar
            double sheight = ((int)(sleepers*2))/10.0;
            page.drawString(sleepers+" (~" + sheight + "m)", i*BAYWIDTH, getHeight()-BOTTOMHEIGHT/2 + 5);

            // calculate and display bar bounds if offset allows
            if(sleepers == 0 || sleepers - offsetY <= 0) continue;
            int height = (sleepers - offsetY)*SLEEPERHEIGHT;

            if(height > 0) {
                page.setColor( Color.lightGray );
                page.fillRect (i*BAYWIDTH,getHeight()-BOTTOMHEIGHT-height,BAYWIDTH,height);
                page.setColor( Color.black );
                page.drawRect (i*BAYWIDTH,getHeight()-BOTTOMHEIGHT-height,BAYWIDTH,height);
            }
        }

    }

    /**
     * findBay(int X):
     * determines which bay the mouse is currently in given
     * an X coordinate. It returns the index of the bay in the array.
     */
    private int findBay(int X) {
        int index = X / BAYWIDTH;
        //System.out.println("Offset and Index: " + offsetX + " " + index);
        if(index+offsetX > curWall.getBays().size() - 1) return -1;
        return index + offsetX;
    }

    /**
     * updateMaxOffsetY():
     * Determines the Maximum allowed offset that can be used by the Y axis scrollbar.
     */
    private void updateMaxOffsetY() {
        // find largest current bay height
        int max = 0;
        for(Bay bay : curWall.getBays()) {
            if(bay.getSleepers() > max) {
                max = bay.getSleepers();
            }
        }

        // space on screen for number of sleepers
        int screenSpace = (getHeight()-BOTTOMHEIGHT)/SLEEPERHEIGHT;
        maxOffsetY = (screenSpace > max) ? screenSpace : max + 1;

        // update the Y slider
        ref.setSliderYMax(maxOffsetY);
    }

    /**
     * MListener to handle the mouse interactions on the screen.
     */
    private class MListener implements MouseListener, MouseMotionListener
    {
        // Mouse Listener methods
        /**
         * mouseClicked(MouseEvent event):
         * Handles the setHeight by value and remove bay actions.
         *
         * @param event - The mouse event information
         */
        public void mouseClicked(MouseEvent event)
        {
            // if mode is setHeight by click or remove bay
            if(mousemode == 1 || mousemode == 2) {
                // find target bay
                int x = event.getX();
                targetBay = findBay(x);
                if(targetBay == -1) return;

                // handle the set Height mode
                if(mousemode == 1) {
                    String input = JOptionPane.showInputDialog(null,
                                     "Enter height (any non-negative number):  (Current value is: " +
                                     curWall.getBays().get(targetBay).getSleepers()*0.2 + ")",
                                     "Set Height For Selected Bay", 1);
                    if(input != null) {
                        double height;
                        try {
                            height = Double.parseDouble(input);
                        } catch(NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Input was not a valid number, wall was not changed!",
                                           "Warning!",
                                           JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        curWall.getBays().get(targetBay).setSleepers(height, true);
                    }
                } else {
                    //System.out.println("Removing bay: " + targetBay);
                    // handle the removing of a bay
                    curWall.removeBay(targetBay);

                    // reset the dialog
                    setWallFocus(curWall);
                }

                repaint();
            }
        }

        /**
         * mousePressed(MouseEvent event):
         * Handles the selection of bay to edit by dragging.
         *
         * @param event - The mouse event information.
         */
        public void mousePressed (MouseEvent event) {
            if(mousemode == 0) {
                int x = event.getX();
                targetBay = findBay(x);
            }
        }

        public void mouseEntered (MouseEvent event) {}
        public void mouseReleased (MouseEvent event) {}
        public void mouseExited (MouseEvent event) {}

        // Mouse Motion methods
        /**
         * mouseDragged(MouseEvent event):
         * Handle the dragging of the mouse while using the edit by
         * dragging mode.
         *
         * @param event - The mouse event information.
         */
        public void mouseDragged(MouseEvent event) {
            if(mousemode == 0 && targetBay != -1) {
                double height = (getHeight() - event.getY() - BOTTOMHEIGHT)/SLEEPERHEIGHT + offsetY;
                curWall.getBays().get(targetBay).setSleepers(height, false);
                updateMaxOffsetY();

                repaint();
            }
        }

        public void mouseMoved(MouseEvent event) {}
    }
}
