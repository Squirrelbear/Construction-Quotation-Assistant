package cqa;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * AddBay:
 * Exists as a dialog for adding bays
 * to either the left or right.
 *
 * @author BeN
 */
public class AddBay {
    // 0 == left, 1 == right

    private int location = 1;
    private int numOfBays = 0;
    private double initBayHeight = 0;
    private JDialog addBayDialog;
    private boolean toggle = true;
    private JRadioButton left;
    private JRadioButton right;
    private JTextField numberOfBays;
    private JTextField initialBayHeight;
    private JButton addBays;
    private JButton cancel;
    private JPanel offSet;
    // pointer to the SideView
    private SideView ref;

    /**
     * AddBay(SideView ref, Main main):
     * Creates the dialog.
     *
     * @param ref - pointer to side view to add the bays.
     * @param main - pointer to main to tell frame what it is attached to.
     */
    public AddBay(SideView ref, Main main) {
        this.ref = ref;

        addBayDialog = new JDialog(main, "Create Bays");
        addBayDialog.setPreferredSize(new Dimension(230, 170));
        addBayDialog.setMaximumSize(new Dimension(230, 170));
        addBayDialog.setMinimumSize(new Dimension(230, 170));
        addBayDialog.setResizable(false);
        addBayDialog.setLayout(new GridLayout(0,2));
        offSet = new JPanel();
        left = new JRadioButton("Left", false);
        right = new JRadioButton("Right", false);
        ButtonGroup group = new ButtonGroup();
        numberOfBays = new JTextField(5);
        initialBayHeight = new JTextField(5);
        addBays = new JButton("Add Bays");
        cancel = new JButton("Cancel");
        addBayDialog.add(new JLabel("Bay location: "));
        addBayDialog.add(offSet);
        group.add(left);
        group.add(right);
        addBayDialog.add(left);
        addBayDialog.add(right);
        addBayDialog.add(new JLabel("Number of bays: "));
        addBayDialog.add(numberOfBays);
        addBayDialog.add(new JLabel("Initial height: "));
        addBayDialog.add(initialBayHeight);
        addBayDialog.add(addBays);
        addBayDialog.add(cancel);
        addBayDialog.pack();

        myButtonHandler buttonlistener = new myButtonHandler();
        myRadioButtonHandler radioButtonListener = new myRadioButtonHandler();
        addBays.addActionListener(buttonlistener);
        cancel.addActionListener(buttonlistener);
        left.addActionListener(radioButtonListener);
        right.addActionListener(radioButtonListener);
    }

    /**
     * viewDialog():
     * Clears the fields and displays the dialog.
     */
    public void viewDialog() {
        clearFields();
        addBayDialog.setVisible(true);
    }

    /**
     * getInitBayHeight():
     * Gets the initial height of the bays.
     *
     * @return initial bay height.
     */
    private double getInitBayHeight() {
        return initBayHeight;
    }

    /**
     * setInitBayHeight(int initBayHeight):
     * Sets the initial bay height.
     *
     * @param initBayHeight - the value to set to.
     */
    private void setInitBayHeight(double initBayHeight) {
        this.initBayHeight = initBayHeight;
    }

    /**
     * getLocation():
     * Gets the current location (left or right)
     *
     * @return location.
     */
    private int getLocation() {
        return location;
    }

    /**
     * setLocation(int location):
     * Sets the value of location. (0 or 1) - (left or right)
     *
     * @param location - the value to set to.
     */
    private void setLocation(int location) {
        this.location = location;
    }

    /**
     * getNumBays():
     * Gets the number of bays.
     *
     * @return number of bays to make.
     */
    private int getNumOfBays() {
        return numOfBays;
    }

    /**
     * setNumOfBays(int numOfBays):
     * Sets the number of bays.
     *
     * @param numOfBays - value to set to.
     */
    private void setNumOfBays(int numOfBays) {
        this.numOfBays = numOfBays;
    }

    /**
     * checkNumOfBays():
     * Checks that the specified number of bays
     * is a valid double type number and that it
     * is in the range 0 to 10000. A warning is
     * displayed if this is not the case.
     *
     * @return is number of bays valid.
     */
    private boolean checkNumOfBays() {
        String input = numberOfBays.getText();

        try {
            numOfBays = Integer.parseInt(input);
        } catch (NumberFormatException f) {
            JOptionPane.showMessageDialog(null, "Number of bays does not contain a valid number! (between 0 to 100)",
                    "Warning!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (numOfBays > 0 && numOfBays < 10000) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Number of bays does not contain a valid number! (between 0 to 100)",
                    "Warning!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * checkBayHeight():
     * Checks that the specified initial bay height
     * is a valid integer type number and that it
     * is in the range 0 to 10000. A warning is
     * displayed if this is not the case.
     *
     * @return is bay height valid.
     */
    private boolean checkBayHeight() {
        String input = initialBayHeight.getText();

        try {
            initBayHeight = Double.parseDouble(input);
        } catch (NumberFormatException f) {
            JOptionPane.showMessageDialog(null, "Initial Bay Height does not contain a valid number!",
                    "Warning!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (initBayHeight > 0.0 && initBayHeight < 10000.0) {
            return true;
        }
        return false;
    }

    /**
     * clearFields():
     * Resets the text fields
     * to their default state.
     */
    private void clearFields() {
        numberOfBays.setText("");
        initialBayHeight.setText("");
    }

    /**
     * myButtonHandler:
     * Handles the button events of the add and cancel
     * buttons. Clicking cancel will hide the dialog.
     * Clicking add will check that inputs are valid,
     * and then add the specified bays before also
     * hiding the dialog.
     */
    private class myButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancel) {
                addBayDialog.setVisible(false);
            } else if (e.getSource() == addBays) {
                if(checkNumOfBays() && checkBayHeight()) {
                    ref.addBays((location == 0), numOfBays, initBayHeight);
                    addBayDialog.setVisible(false);
                }
            }
        }
    }

    /**
     * myRadioButtonHandler:
     * Handles the changing of the radio buttons.
     * Stores the current state of which one is selected.
     */
    private class myRadioButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //System.out.println("Radio Button listener is called");
            if (e.getSource() == left) {
                location = 0;
                //System.out.println("Location set to 0");
            } else if (e.getSource() == right) {
                location = 1;
                //System.out.println("Location set to 1");
            } else {
                return;
            }
        }
    }
}
