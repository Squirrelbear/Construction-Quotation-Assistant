package cqa;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * TopView:
 * Shows a top down view of the wall components.
 * Also allows for the modifying of the position or
 * rotation of the components and allow the adding
 * of more walls. Clicking on a wall using the edit
 * more will switch to the SideView.
 *
 * @author Mitchell
 */
public class TopView extends JPanel {

    // The collection of walls.
    private ArrayList<Wall> walls;

    /* mousemodes:
     * -1 - no mode (used while in add menu)
     * 0 - edit mode (click changes view to side view)
     * 1 - add mode (locks a new wall to mouse till a click occurs)
     * 2 - remove mode (clicking will delete a wall segment)
     * 3 - move mode (push selects, drag moves based on centre)
     * 4 - rotate mode (click selects opens dialog with value to change rotation)
     */
    private int mousemode;
    // The currently selected wall.
    private int selection;
    private int mouseover;

    // The current mouse coordinates
    // used by the show wall height on mouseover
    private int _MX = 0;
    private int _MY = 0;

    // The screen offset applied by the scrollbars.
    private int offsetX, offsetY;
    
    // pointer to the GraphicsPanel
    private GraphicsPanel ref;

    // the addWall dialog box
    private AddWall addWall;

    /**
     * Constructor - TopView()
     * Configures the basic layout and listeners for
     * the top view.
     *
     * @param ref The pointer to the GraphicsPanel.
     */
    public TopView(GraphicsPanel ref, Main main) {
        this.ref = ref;

        setPreferredSize(new Dimension(1100,700));
        setBackground(Color.white);
        setLayout(null);

        MListener listener = new MListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);

        walls = new ArrayList<Wall>();
        mousemode = 0;
        selection = -1;
        offsetX = offsetY = 0;

        addWall = new AddWall(this, main);

        repaint();
    }

    /**
     * getSleepers():
     * Counts the number of sleepers in each bay.
     *
     * @return The number of sleepers.
     */
    public int getSleepers() {
        int count = 0;

        for(Wall tmp : walls) {
            count += tmp.getSleepers();
        }

        return count;
    }

    /**
     * setMode(int mode):
     * Changes the mouse mode, dictating what the mouse action is.
     *
     * @param mode - The mode to change to.
     */
    public void setMode(int mode) {
        if(mode > 5 || mode < -1) return;
        if(mousemode == 1 && mode != -1) {
            JOptionPane.showMessageDialog(null, "Please complete placing the wall in the drawing space prior to using a different action!",
                                           "Warning!",
                                           JOptionPane.WARNING_MESSAGE);
            return;
        }

        resetSelection();
        if(mode == -1) {
            mousemode = 0;
        } else {
            mousemode = mode;
        }
        ref.setTextHint(mousemode);
    }

    /**
     * clearTopView():
     * Prepares the TopView for a load procedure.
     * Also allows for the "New Quote" function.
     */
    public void clearTopView() {
        setMode(-1);
        selection = -1;
        offsetX = offsetY = 0;
        for(Wall tmp : walls) {
            remove(tmp);
        }
        walls.clear();
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
        walls.add(new Wall(wallData,_bays));
        add(walls.get(walls.size()-1));
        walls.get(walls.size()-1).setOffsetX(offsetX);
        walls.get(walls.size()-1).setOffsetY(offsetY);
    }

    /**
     * getWallsSaveData():
     * Simply get the save data for all walls.
     *
     * @return
     */
    public String getWallsSaveData() {
        String saveData = "";
        for(Wall tmp : walls) {
            saveData += tmp.getSaveData();
        }
        return saveData;
    }

    /**
     * setOffsetX(int X):
     * Change the X screen offset to X.
     *
     * @param X - The new offsetX.
     */
    public void setOffsetX(int X) {
        offsetX = X;

        for(Wall wall : walls) {
            wall.setOffsetX(X);
        }

        repaint();
    }

    /**
     * setOffsetY(int Y):
     * Change the Y screen offset to Y.
     *
     * @param Y - The new offsetY.
     */
    public void setOffsetY(int Y) {
        offsetY = Y;

        for(Wall wall : walls) {
            wall.setOffsetY(Y);
        }

        repaint();
    }

    /**
     * getOffsetX():
     * Get the offsetX.
     *
     * @return The X screen offset.
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * getOffsetY():
     * Get the offsetY.
     *
     * @return The Y screen offset.
     */
    public int getOffsetY() {
        return offsetY;
    }

    /**
     *getBoundBox():
     * Gets the bounding edges of the box.
     * This is used for calculating the required maximum offsets.
     *
     * @return The left, right, top, and bottom most end points.
     */
    public int[] getBoundBox() {
        Object tmpWalls[] = walls.toArray();

        if(tmpWalls.length == 0) {
            // left, right, top, bottom
            int [] bounds = {0, 0, 0, 0};
            return bounds;
        }

        // left, right, top, bottom
        int [] bounds = ((Wall)tmpWalls[0]).getBoundBox();

        // find the furthest point in each direction
        for(int i = 1; i < tmpWalls.length; i++) {
            int [] tmpBounds = ((Wall)tmpWalls[i]).getBoundBox();
            if(tmpBounds[0] < bounds[0]) bounds[0] = tmpBounds[0];
            if(tmpBounds[1] > bounds[1]) bounds[1] = tmpBounds[1];
            if(tmpBounds[2] < bounds[2]) bounds[2] = tmpBounds[2];
            if(tmpBounds[3] > bounds[3]) bounds[3] = tmpBounds[3];
        }

        // Alter the points in each direction by 100
        for(int i = 0; i < 4; i++) {
            bounds[i] = bounds[i] + 100 * (int)Math.pow(-1, i+1);
        }

        return bounds;
    }

    /**
     * addWall():
     * Begins trying to add a new wall.
     * Simply opens the add wall dialog.
     */
    public void addWall() {
        // show dialog and get options
        addWall.viewDialog();
    }

    /**
     * addWall(double length, double startHeight, double endHeight, double startHeight):
     * Adds a new wall using the specified numbers. Also increases the size of the array as required.
     * This method will be called by the add wall dialog.
     *
     * @param length - length to use.
     * @param startHeight - startHeight to use.
     * @param endHeight - endHeight to use.
     * @param startAngle - startAngle to use.
     */
    public void addWall(double length, double startHeight, double endHeight, double startAngle) {
        walls.add(new Wall(length, startHeight, endHeight, startAngle));
        add(walls.get(walls.size()-1));
        walls.get(walls.size()-1).setOffsetX(offsetX);
        walls.get(walls.size()-1).setOffsetY(offsetY);

        // change mode to add
        setMode(1);
        selection = walls.size()-1;
    }

    /**
     * cloneWall(int id):
     * Creates a clone of a wall which is
     * then added to the window just like in the
     * addWall method.
     *
     * @param id - The wall selection to clone.
     */
    private void cloneWall(int id) {
        walls.add(new Wall(walls.get(id)));
        add(walls.get(walls.size()-1));
        walls.get(walls.size()-1).setOffsetX(offsetX);
        walls.get(walls.size()-1).setOffsetY(offsetY);

        // change mode to add
        setMode(1);
        selection = walls.size()-1;
    }

    /**
     * removeWall(int id):
     * Removes the wall at the specified id.
     *
     * @param id - Index at which to remove the wall.
     */
    private void removeWall(int id) {
        // remove the wall from rendering
        remove(walls.get(id));
        walls.remove(id);
        selection = -1;
    }

    /**
     * rotateWall(int id):
     * Rotates wall at the specified ID.
     * It displays an input window and if the number
     * is in the correct range and they have inputted
     * a number it will update the rotation.
     *
     * @param id - The id index to rotate.
     */
    private void rotateWall(int id) {
        String input = JOptionPane.showInputDialog(null,
                                     "Enter a rotation between 0 and less than 360:  (Current value is: " +
                                     walls.get(id).getRotation() + ")",
                                     "Set Rotation For This Wall", 1);
        if(input != null) {
            double angle;
            try {
                angle = Double.parseDouble(input);
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Input was not a valid number, wall was not changed!",
                                            "Warning!",
                                            JOptionPane.WARNING_MESSAGE);
                return;
            }

            if(angle < 0 || angle >= 360) {
                JOptionPane.showMessageDialog(null, "Input was not within the correct range (between 0 and 360), wall was not changed!",
                                            "Warning!",
                                            JOptionPane.WARNING_MESSAGE);
                return;
            }

            walls.get(id).setRotation(angle);
        }
    }

    /**
     * findClick(int X, int Y):
     * Finds if there is a selected wall. Wall selected is first one
     * appearing in the array. If none found result will be -1.
     *
     * @param X - The Mouse X coordinate.
     * @param Y - The Mouse Y coordinate.
     * @return Array index of selected OR -1 if none selected.
     */
    private int findClick(int X, int Y)
    {
        // search all walls attempting to find an intersect.
        for(int i = 0; i < walls.size(); i++) {
            if(walls.get(i).pointInWall(X, Y)) {
                return i;
            }
        }

        // no intersect was found.
        return -1;
    }

    /**
     * getSelection(int X, int Y):
     * Gets a selection if possible. Deselects old item first
     * then selects the new selection. Return is whether selection is
     * successful in finding an intersect.
     *
     * @param X - The Mouse X coordinate.
     * @param Y - The Mouse Y coordinate.
     * @return If selection was made.
     */
    private boolean getSelection(int X, int Y) {
        // deselect old item if there was one
        if(selection != -1) {
            walls.get(selection).changeSelection(false);
        }

        // find new selection and select it if one is found
        selection = findClick(X, Y);
        if(selection != -1) {
            walls.get(selection).changeSelection(true);
            return true;
        }

        // no selection was found
        return false;
    }

    /**
     * resetSelection():
     * Deselects any object that is selected
     */
    private void resetSelection() {
        if(selection != -1) {
            walls.get(selection).changeSelection(false);
        }
        selection = -1;
    }

     @Override
    public void paintComponent(Graphics page)
    {
        super.paintComponent(page);

        page.setColor( Color.black );
        //int sOffset = 20;
        if(mouseover != -1 && mouseover < walls.size()) {
            page.drawString("Wall length: " + walls.get(mouseover).getLength() + "m", 0, 20);
            // sOffset = 40;
        }
        //page.drawString("Mouse: (" + _MX + ", " + _MY + ")", 0, sOffset);

    }

    /**
     * MListener:
     * Exists as the interaction mechanism for the top view.
     */
    private class MListener implements MouseListener, MouseMotionListener
    {
        // Mouse Listener methods
        /**
         * mouseClicked(MouseEvent event):
         * Handles the actions: edit, add, remove, and rotate.
         *
         * @param event - The information about the mouse event.
         */
        public void mouseClicked(MouseEvent event)
        {
            if(mousemode != 3 && mousemode != -1) {
                int x = event.getX();
                int y = event.getY();

                switch(mousemode) {
                    case 0: // edit
                        // switch view mode
                        if(getSelection(x, y)) ref.gotoSideView(walls.get(selection));
                        break;
                    case 1: // add
                        // switch mousemode to edit
                        setMode(-1);
                        break;
                    case 2: // remove
                        mouseover = -1;
                        if(getSelection(x, y)) removeWall(selection);
                        break;
                    case 4: // rotate
                        if(getSelection(x, y)) rotateWall(selection);
                        break;
                    case 5: // clone
                        if(getSelection(x, y)) cloneWall(selection);
                        break;
                }
            }
        }

        /**
         * mousePressed(MouseEvent event):
         * Handles the Move action, by choosing the initial selection.
         *
         * @param event - The information about the mouse event.
         */
        public void mousePressed (MouseEvent event) {
            if(mousemode == 3) {
                int x = event.getX();
                int y = event.getY();

                getSelection(x, y);
            }
        }

        public void mouseEntered (MouseEvent event) {}
        public void mouseReleased (MouseEvent event) {}
        public void mouseExited (MouseEvent event) {}

        // Mouse Motion methods
        /**
         * mouseDragged(MouseEvent event):
         * Handles the move functionality.
         * It updates the position while dragging.
         *
         * @param event - The information about the mouseevent.
         */
        public void mouseDragged(MouseEvent event) {
            if(mousemode == 3 && selection != -1) {
                int x = event.getX();
                int y = event.getY();
                
                walls.get(selection).setPosition(x-offsetX, y-offsetY);
                ref.updateBounds();
                repaint();
            }
        }

        /**
         * mouseMoved(MouseEvent event):
         * Handles the add event. It allows free move until a click occurs.
         * The click will lock the location.
         *
         * @param event - The information about the mouse event.
         */
        public void mouseMoved(MouseEvent event) {
            if(mousemode == 1 && selection != -1) {
                int x = event.getX();
                int y = event.getY();

                walls.get(selection).setPosition(x-offsetX, y-offsetY);
                ref.updateBounds();
            }

            // update what the mouse is over at the moment
            _MX = event.getX();
            _MY = event.getY();
            if(mousemode != 1) {
                mouseover = findClick(_MX, _MY);
            }

            repaint();
        }
    }
}
