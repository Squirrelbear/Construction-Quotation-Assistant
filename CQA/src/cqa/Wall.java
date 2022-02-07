package cqa;

import javax.swing.*;
import java.awt.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * Wall:
 * Handles the definition of a basic wall type unit.
 * It forms one of the basic conceptual units of the program.
 * The wall is defined mostly by its X, Y, length, and angle variables.
 * In addition to the heights of its individual bays.
 *
 * @author Mitchell
 */
public class Wall extends JComponent {

        // Collection of bays that make up the wall
	private ArrayList<Bay> bays;

        // Core definition of wall position, dimension and rotation
	private double x, y;
        private int length;
	private double angle;

        // arrays to hold the Polygon that makes up the visible rectangle
	private double rx [];
	private double ry [];

        // selected just changes the colour of the "selected" wall.
	private boolean selected;

        // constant definitions
        private final static double _WIDTH = 0.2;
	// top left, top right, bottom right, bottom left
	private final static int [] XOFFSET = { -1, 1, 1, -1 };
	private final static int [] YOFFSET = { -1, -1, 1, 1 };

        // screen offsets
        private int scrollOffsetX;
        private int scrollOffsetY;

        /**
         * Constructor Wall(double length, double startHeight, double endHeight, double startAngle):
         * This method will take the specified values and prepare the new wall to be added.
         * It allows for walls slanting either way or starting with all blocks equal.
         * The initial rotation can also be set.
         *
         * @param length - The length for the walls. Will be rounded up in 2.0m increments.
         * @param startHeight - Defines the left side of the wall height.
         * @param endHeight - Defines the right side of the wall height.
         * @param startAngle - The initial angle rotation of the wall.
         */
	public Wall(double length, double startHeight, double endHeight, double startAngle) {
		super();
                setBounds(0, 0, 2000, 2000);
                setBackground(Color.WHITE);

		// this line should never be be true, but adds a redundant failsafe
		if(length <= 0 || startHeight < 0 || endHeight < 0 || startAngle < 0 || startAngle >= 360) return;

		// get the correct length
		if (length%2 != 0 || (length - (double)((int)length)) > 0) {
			this.length = ((int)length / 2 + 1) * 2;
		} else {
			this.length = (int)length;
		}

		// create array with number of bays
		bays = new ArrayList<Bay>();

		if(startHeight < endHeight) {
			double L = this.length-2;
			double theta = Math.atan((endHeight - startHeight) / L);

			bays.add(new Bay(endHeight));

			// populate each bay with correct number of sleepers
			for(int i = (int)this.length / 2 - 2; i >= 0; i--) {
				bays.add(0, new Bay((double)startHeight + Math.tan(theta)*(L-2)));
                                L -= 2;
			}
		} else if(startHeight > endHeight) {
			double L = this.length-2;
			double theta = Math.atan((startHeight - endHeight) / L);

			bays.add(new Bay(startHeight));

			// populate each bay with correct number of sleepers
			for(int i = 1; i < (int)this.length / 2; i++) {
				bays.add(new Bay(endHeight + Math.tan(theta)*(L-2)));
                                L -= 2;
			}
		} else {
			// populate each bay with equal height sleepers
			for(int i = 0; i < (int)this.length / 2; i++) {
				bays.add(new Bay(startHeight));
			}
		}

		// set the intial rotation and update the rectangle points
		angle = startAngle;
                rx = new double[4];
                ry = new double[4];
		updateRect();

		// default to not selected and default x and y to 0
		// x and y will not be set here, they will update with the mouse.
		selected = false;
		x = y = scrollOffsetX = scrollOffsetY = 0;
	}

        /*
         * Wall(String wallData, String _bays):
         * Creates a Wall at (x,y) with length, and angle.
         * This is to ONLY be used for LOAD operation!!
         * Bounds checking is not done.
         *
         * @param wallData - The main data about the wall (x,y,length, angle)
         * @param _bays - The sleeper value of all bays to use.
         */
        public Wall(String wallData, String _bays) {
            super();
            setBounds(0, 0, 2000, 2000);
            setBackground(Color.WHITE);

            StringTokenizer wallTokens = new StringTokenizer(wallData);
            this.x = Double.parseDouble((String)wallTokens.nextElement());
            this.y = Double.parseDouble((String)wallTokens.nextElement());

            int length = Integer.parseInt((String)wallTokens.nextElement());
            // get the correct length
            if (length%2 != 0) {
		this.length = ((int)length / 2 + 1) * 2;
            } else {
		this.length = (int)length;
            }

            this.angle = Double.parseDouble((String)wallTokens.nextElement());

            // create array with number of bays
            bays = new ArrayList<Bay>();
            
            StringTokenizer bayTokens = new StringTokenizer(_bays);
            while(bayTokens.hasMoreTokens()) {
                bays.add(new Bay(Double.parseDouble((String)bayTokens.nextElement())*0.2));
            }

            // default to not selected and default x and y to 0
            // x and y will not be set here, they will update with the mouse.
            selected = false;
            scrollOffsetX = scrollOffsetY = 0;

            rx = new double[4];
            ry = new double[4];
            updateRect();
        }

        /**
         * Wall(Wall wall):
         * Copy constructor. To be used
         * when doing a clone of a wall.
         * @param wall
         */
        public Wall(Wall wall) {
            super();
            setBounds(0, 0, 2000, 2000);
            setBackground(Color.WHITE);

            this.length = wall.getLength();
            this.angle = wall.getRotation();

            // default to not selected and default x and y to 0
            // x and y will not be set here, they will update with the mouse.
            selected = false;
            x = y = scrollOffsetX = scrollOffsetY = 0;

            // create array with number of bays
            bays = new ArrayList<Bay>();

            // get the bays
            for(Bay tmp : wall.getBays()) {
                bays.add(new Bay(tmp.getSleepers()*0.2));
            }

            rx = new double[4];
            ry = new double[4];
            updateRect();

        }

        /**
         * getSaveData():
         * Gets a copy of the required data to recreate the wall as it is.
         *
         * @return The core representation of the wall.
         */
        public String getSaveData() {
            String saveData = x + " " + y + " " + length + " " + angle + "\n";
            for(Bay tmp : bays) {
                saveData += tmp.getSleepers() + " ";
            }
            saveData = saveData.substring(0,saveData.length()-1) + "\n";
            return saveData;
        }

        /**
         * changeSelection(boolean selected):
         * This method just changes whether the wall is selected.
         *
         * @param selected - Changes whether the wall is selected or not.
         */
        public void changeSelection(boolean selected) {
            this.selected = selected;
        }

        /**
         * isSelected():
         * Gets whether this wall is selected.
         *
         * @return Whether selected or not.
         */
        public boolean isSelected() {
            return selected;
        }

        /**
         * setPosition(double x, double y):
         * Updates the centre point of this wall, and then updates the polygon.
         *
         * @param x - The x coordinate.
         * @param y - The y coordinate.
         */
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;

		// update the rectangle points
		updateRect();
	}

        /**
         * setOffsetX(int X):
         * changes the offset and then updates the rectangle.
         *
         * @param X - The X offset to use.
         */
        public void setOffsetX(int X) {
            scrollOffsetX = X;

            updateRect();
        }

        /**
         * setOffsetY(int Y):
         * changes the offset and then updates the rectangle.
         *
         * @param Y - The Y offset to use.
         */
        public void setOffsetY(int Y) {
            scrollOffsetY = Y;

            updateRect();
        }

        /**
         * getOffsetX():
         * Gets the current offset used by this wall.
         *
         * @return The x screen offset.
         */
        public int getOffsetX() {
            return scrollOffsetX;
        }

        /**
         * getOffsetY():
         * Gets the current offset used by this wall.
         *
         * @return The y screen offset.
         */
        public int getOffsetY() {
            return scrollOffsetY;
        }

        /**
         * get_X():
         * Gets the current X coordinate of the rectangles centre.
         *
         * @return Centre of rect X coordinate.
         */
	public double get_X() {
		return x;
	}

        /**
         * get_Y():
         * Gets the current Y coordinate of the rectangles centre.
         *
         * @return Centre of rect Y coordinate.
         */
	public double get_Y() {
		return y;
	}

        /**
         * setRotation(double angle):
         * Changes the rotation and updates the rectangle's polygon.
         *
         * @param angle - The desired angle to update to.
         */
	public void setRotation(double angle) {
		if(angle < 0 || angle >= 360) return;

		// check if bays need to be reversed in order
                if(slantIsPositive() != slantIsPositive(angle)) {
                    Collections.reverse(bays);
                }

		this.angle = angle;

		updateRect();
	}

        /**
         * getRotation():
         * Gets the current rotation of the rectangle.
         *
         * @return The current rotation.
         */
	public double getRotation() {
		return angle;
	}

        /**
         * getLength():
         * Gets the current length of the wall.
         *
         * @return The current length.
         */
        public int getLength() {
            return length;
        }

	/**
         * getSleepers():
         * Counts through each of the bays adding up the total number
         * of sleepers for the wall.
         *
         * @return The number of sleepers that make up this wall.
         */
	public int getSleepers() {
		int count = 0;

		// for each bay get the number of sleepers
		for(Bay bay : bays) {
			count += bay.getSleepers();
		}

		return count;
	}

        /**
         * addBays(boolean left, int count, int sleepers):
         * Adds 1 or more bays to the left or right of the current set of bays.
         * It uses a defined sleepers value to set the height of all the newly inserted
         * bays. It also updates the length respectively.
         *
         * @param left - If true, add to left; else add bays to the right.
         * @param count - The number of bays to add.
         * @param sleepers - The height of the sleepers to use initially.
         */
	public void addBays(boolean left, int count, double height) {
		// update length
		// don't do it if this is actually a reverse operation
		if(count <= 0) return;
		length = length + count*2;

		// determine which end is left and which is right using end points
		boolean startLeft = !slantIsPositive() && left;

		// if top is to left
		if(startLeft) {
			// insert new elements to the left
			for(int i = 0; i < count; i++) {
				bays.add(0,new Bay(height));
			}
		} else {
		// if top is to right
			// insert new elements to the right
			for(int i = 0; i < count; i++) {
				bays.add(new Bay(height));
			}
		}
	}

        /**
         * removeBay(int bayID):
         * Removes one bay at the specified index bayID.
         * Updates the information as required.
         *
         * @param bayID - The bay index to remove.
         */
	public void removeBay(int bayID) {
		// don't do it if it removes the wall (1 bay always required)
		if(length - 2 <= 0) return;
		length = length - 2;

		// remove the bay
                bays.remove(bayID);

		// update rectangle using new dimensions
		updateRect();
	}

        /**
         * getBays():
         * Gets the collection of bays to be able to access the bays directly.
         *
         * @return The bays that are in this wall.
         */
	public ArrayList<Bay> getBays() {
		return bays;
	}

        /**
         * pointInWall(double X, double Y):
         * Detects if the coordinates specified lie within the bounds of this
         * rectangle.
         *
         * @param X - The X coordinate of the point.
         * @param Y - The Y coordinate of the point.
         * @return True if the point lies inside the rectangle.
         */
	public boolean pointInWall(double X, double Y) {
		// check each of the 4 walls to see if the point is inside
		boolean a = pointMatches(X, Y, 0, 1, 2);
		boolean b = pointMatches(X, Y, 1, 2, 3);
		boolean c = pointMatches(X, Y, 2, 3, 0);
		boolean d = pointMatches(X, Y, 3, 0, 1);

		if(a && b && c && d) return true;
		else return false;
	}

        public int [] getBoundBox() {
            int [] bounds = new int[4];
            bounds[0] = bounds[1] = (int)rx[0];
            bounds[2] = bounds[3] = (int)ry[0];

            for(int i = 1; i < 4; i++) {
                if(rx[i] < bounds[0]) bounds[0] = (int)rx[i];
                if(rx[i] > bounds[1]) bounds[1] = (int)rx[i];
                if(ry[i] < bounds[2]) bounds[2] = (int)ry[i];
                if(ry[i] > bounds[3]) bounds[3] = (int)ry[i];
            }

            return bounds;
        }


        /**
         * updateRect():
         * This method is the core method in terms of graphical display.
         * It calculates the coordinates of each of the corner points of the rectangle.
         * It factors in the centre point and then finds the points using offsets
         * from the width and height. Then it applies a rotation to the points
         * which transforms them to the correct locations.
         */
	private void updateRect() {
		// scale makes object bigger
		double scaleL = 20.0;
                double scaleW = 100.0;

		// width and height offsets
		double W = (_WIDTH * scaleW) / 2;
		double H = (length * scaleL) / 2;

		double [] posMatX = new double[4];
		double [] posMatY = new double[4];

		// set X matrix without rotation
		for(int i = 0; i < 4; i++) {
			posMatX[i] = W * XOFFSET[i];//x + getOffsetX() + W * XOFFSET[i];
		}

		// set Y matrix without rotation
		for(int i = 0; i < 4; i++) {
			posMatY[i] = H * YOFFSET[i];//y + getOffsetY() + H * YOFFSET[i];
		}

                //System.out.print("RX: ");
		// transform X matrix
		for(int i = 0; i < 4; i++) {
			rx[i] = transformX(posMatX[i], posMatY[i]) + x + getOffsetX();
                        //System.out.print(rx[i] + ", ");
		}

                //System.out.print("\nRY: ");
		// transform Y matrix
		for(int i = 0; i < 4; i++) {
			ry[i] = transformY(posMatX[i], posMatY[i]) + y + getOffsetY();
                        //System.out.print(ry[i] + ", ");
		}


	}

        /**
         * transformX(double X, double Y):
         * Transforms an X coordinate using the current rotation.
         *
         * @param X - The X coordinate of the point to transform.
         * @param Y - The Y coordinate of the point to transform.
         * @return The transformed X coordinate.
         */
	private double transformX(double X, double Y) {
		return (X * Math.cos(angle*Math.PI/180) - Y * Math.sin(angle*Math.PI/180));
	}

        /**
         * transformY(double X, double Y):
         * Transforms an Y coordinate using the current rotation.
         *
         * @param X - The X coordinate of the point to transform.
         * @param Y - The Y coordinate of the point to transform.
         * @return The transformed Y coordinate.
         */
	private double transformY(double X, double Y) {
		return (X * Math.sin(angle*Math.PI/180) + Y * Math.cos(angle*Math.PI/180));
	}

        /**
         * slantIsPositive():
         * Determines whether the current slant is positive.
         *
         * @return Whether the slant is positive or not.
         */
	private boolean slantIsPositive() {
		return (angle > 0 && angle < 180);
	}

        /**
         * slantIsPositive(double angle):
         * Determines whether the current slant is positive.
         *
         * @param angle - angle to test for positive
         * @return Whether the slant is positive or not.
         */
	private boolean slantIsPositive(double angle) {
		return (angle > 0 && angle < 180);
	}

        /**
         * pointMatches(...):
         * Determines if the test point is on the same side of the line defined
         * as pa->pb, as the point MX,MY.
         *
	 * @param MX - Mouse X
	 * @param MY - Mouse Y
	 * @param pa - Point a
	 * @param pb - Point b
	 * @param tp - Test point
	 * @return Points both lie on the same side of the line pa->pb.
	*/
	private boolean pointMatches(double MX, double MY, int pa, int pb, int tp) {
		/* Equivalent to:
		*   (y2 - y1)
		*   ---------
		*   (x2 - x1)
		*/
		double q = (ry[pb] - ry[pa]) / (rx[pb] - rx[pa]);

		// function(tp) on line pa to pb
		double F_tp = (ry[tp] - ry[pa] - q * (rx[tp] - rx[pa]));

		// function(M) on line pa to pb
		double F_M = (MY - ry[pa] - q * (MX - rx[pa]));

		// if they are on the same side of the line pa to pb
		if( (F_M < 0 && F_tp < 0) || (F_M > 0 && F_tp > 0) ) return true;
		return false;
	}

        /**
         * paintComponent(Graphics g):
         * Draws the rectangle in the drawing space.
         *
         * @param g - The graphics interface.
         */
    @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor( Color.lightGray );

		// render rect
                int [] polyX = new int[4];
                int [] polyY = new int[4];

                for(int i = 0; i < 4; i++) {
                    polyX[i] = (int)rx[i];
                    polyY[i] = (int)ry[i];
                }
                g.fillPolygon(polyX, polyY, 4);

                if(selected) {
			g.setColor( Color.red );
		} else {
			g.setColor( Color.black );
		}
		g.drawPolygon(polyX, polyY, 4);

                //g.fillRect(0,0,getWidth(),getHeight());
		//paintChildren(g);
	}
}
