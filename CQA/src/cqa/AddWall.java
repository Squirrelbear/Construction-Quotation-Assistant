package cqa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

/**
 *  AddWall:
 * A dialog that allows the creation of a
 * wall with defined properties.
 *
 * @author BeN
 */
public class AddWall{
    private double length = 1;
    private double startHeight = 0;
    private double endHeight = 0;
    private double initRotation = 0.0;
    private JDialog addWallDialog;
    private boolean toggle = true;
    private JTextField fieldlength;
    private JTextField startWallHeight;
    private JTextField endWallHeight;
    private JTextField initialRotation;
    private JButton addWall;
    private JButton cancel;

    // pointer to the TopView
    private TopView ref;

    /**
     * AddWall(TopView ref, Main main):
     * Creates the dialog.
     *
     * @param ref - the pointer to use when creating the wall.
     * @param main - the pointer to tell the dialog which window it relates to.
     */
    public AddWall(TopView ref, Main main){
        this.ref = ref;

        addWallDialog = new JDialog(main, "Create Wall");
        addWallDialog.setPreferredSize(new Dimension(230, 170));
        addWallDialog.setMaximumSize(new Dimension(230, 170));
        addWallDialog.setMinimumSize(new Dimension(230, 170));
        addWallDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWallDialog.setResizable(false);
        addWallDialog.setLayout(new GridLayout(0,2));
        fieldlength = new JTextField(5);
        startWallHeight = new JTextField(5);
        endWallHeight = new JTextField(5);
        initialRotation = new JTextField(3);
        initialRotation.setText("0.0");
        addWallDialog.add(new JLabel("Wall Length: "));
        addWallDialog.add(fieldlength);
        addWallDialog.add(new JLabel("Wall Start Height: "));
        addWallDialog.add(startWallHeight);
        addWallDialog.add(new JLabel("Wall End Height: "));
        addWallDialog.add(endWallHeight);
        addWallDialog.add(new JLabel("Initial Rotation: "));
        addWallDialog.add(initialRotation);
        addWall = new JButton("Add Wall");
        cancel = new JButton("Cancel");
        addWallDialog.add(addWall);
        addWallDialog.add(cancel);
        addWallDialog.pack();

        myButtonHandler buttonListener = new myButtonHandler();
        addWall.addActionListener(buttonListener);
        cancel.addActionListener(buttonListener);
    }

    /**
     * viewDialog():
     * Displays the dialog with reset fields.
     */
    public void viewDialog(){
        clearFields();
        addWallDialog.setVisible(true);
    }

    /**
     * getEndHeight():
     * Gets the endHeight.
     *
     * @return endHeight
     */
    private double getEndHeight() {
        return endHeight;
    }

    /**
     * setEndHeight(double endHeight):
     * Changes the endHeight value.
     *
     * @param endHeight - The value to change to.
     */
    private void setEndHeight(double endHeight) {
        this.endHeight = endHeight;
    }

    /**
     * getInitRotation():
     * Gets the initial rotation.
     *
     * @return initial rotation.
     */
    private double getInitRotation() {
        return initRotation;
    }

    /**
     * setInitRotation(double initRotation):
     * Sets the initial rotation.
     *
     * @param initRotation - the rotation to set to.
     */
    private void setInitRotation(double initRotation) {
        this.initRotation = initRotation;
    }

    /**
     * getLength():
     * Gets the length.
     *
     * @return length.
     */
    private double getLength() {
        return length;
    }

    /**
     * setLength(double length):
     * Sets the length.
     *
     * @param length - the length to change to.
     */
    private void setLength(double length) {
        this.length = length;
    }

    /**
     * getStartHeight():
     * Gets the startHeight.
     *
     * @return startHeight.
     */
    private double getStartHeight() {
        return startHeight;
    }

    /**
     * setStartHeight(double startHeight):
     * Sets the startHeight.
     *
     * @param startHeight - The height to set to.
     */
    private void setStartHeight(double startHeight) {
        this.startHeight = startHeight;
    }

    /**
     * checkStartHeight():
     * Checks that the entered startHeight is valid.
     * Needs to be a valid double type number, and fit
     * between 0 and 10000 in value. In the case of invalid
     * a dialog will be displayed stating the error.
     *
     * @return is startHeight valid.
     */
    private boolean checkStartHeight(){
        String input = startWallHeight.getText();

        try{
            startHeight = Double.parseDouble(input);
        }catch(NumberFormatException f){
               JOptionPane.showMessageDialog(null,
                       "Start Wall Height was not a valid number. (Between 0 and 10000)","Warning!", JOptionPane.ERROR_MESSAGE);
               return false;
        }

        if(startHeight >= 0.0 && startHeight < 10000.0){
            return true;
        }else{
            JOptionPane.showMessageDialog(null,
               "Start Wall Height was not a valid number. (Between 0 and 10000)","Warning!",  JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * checkEndHeight():
     * Checks that the entered endHeight is valid.
     * Needs to be a valid double type number, and fit
     * between 0 and 10000 in value. In the case of invalid
     * a dialog will be displayed stating the error.
     *
     * @return is endHeight valid.
     */
    private boolean checkEndHeight(){
        String input = endWallHeight.getText();

        try{
            endHeight = Double.parseDouble(input);
        }catch(NumberFormatException f){
               JOptionPane.showMessageDialog(null,
               "End Wall Height was not a valid number. (Between 0 and 10000)","Warning!", JOptionPane.ERROR_MESSAGE);
               return false;
        }

        if(endHeight >= 0.0 && endHeight < 10000.0){
            return true;
        }else{
               JOptionPane.showMessageDialog(null,
               "End Wall Height was not a valid number. (Between 0 and 10000)","Warning!", JOptionPane.ERROR_MESSAGE);
               return false;
        }
    }

    /**
     * checkLength():
     * Checks that the entered length is valid.
     * Needs to be a valid double type number, and fit
     * between 0 and 10000 in value. In the case of invalid
     * a dialog will be displayed stating the error.
     *
     * @return is startHeight valid.
     */
    private boolean checkLength(){
        String input = fieldlength.getText();
        try{
            length = Double.parseDouble(input);

        }catch(NumberFormatException f){
               JOptionPane.showMessageDialog(null,
               "Length supplied was not a valid number. (Between 0 and 10000)","Warning!", JOptionPane.ERROR_MESSAGE);
               return false;
        }

        if(length > 0.0 && length < 10000.0){
            return true;
        }else{
               JOptionPane.showMessageDialog(null,
               "Length supplied was not a valid number. (Between 0 and 10000)","Warning!", JOptionPane.ERROR_MESSAGE);
               return false;
        }
    }

    /**
     * checkRotation():
     * Checks that the entered rotation is valid.
     * Needs to be a valid double type number, and fit
     * between 0.0 and less than 360 in value. In the case of invalid
     * a dialog will be displayed stating the error.
     *
     * @return is startHeight valid.
     */
    private boolean checkRotation(){
        String input = initialRotation.getText();

        try{
            initRotation = Double.parseDouble(input);
        }catch(NumberFormatException f){
               JOptionPane.showMessageDialog(null,
               "Rotation supplied is not valid. (Between 0.0 and less than 360)","Warning!", JOptionPane.ERROR_MESSAGE);
               return false;
        }

        if(initRotation >= 0.0 && initRotation < 360.0){
            return true;
        }else{
               JOptionPane.showMessageDialog(null,
               "Rotation supplied is not valid. (Between 0.0 and less than 360)","Warning!", JOptionPane.ERROR_MESSAGE);
               return false;
        }
    }

    /**
     * clearFields():
     * Resets the values of the form making them empty.
     * Rotation is set to 0.0 to make it optional.
     */
    private void clearFields() {
        fieldlength.setText("");
        startWallHeight.setText("");
        endWallHeight.setText("");
        initialRotation.setText("0.0");
    }

    /**
     * myButtonHandler:
     * Handles the button presses of the
     * cancel and create buttons.
     * Cancel just hides the window, and
     * add checks if all the fields are valid
     * and then adds the wall to the program before
     * also hiding the dialog.
     */
    private class myButtonHandler implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == cancel){
                addWallDialog.setVisible(false);
            }else if(e.getSource() == addWall){
                if(checkLength() && checkStartHeight() && checkEndHeight() && checkRotation()) {
                    ref.addWall(length, startHeight, endHeight, initRotation);
                    addWallDialog.setVisible(false);
                }
            }
        }

    }
  }

