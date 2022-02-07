package cqa;

/**
 * Bay:
 * Bays are the lowest level unit of this system.
 * They are part of walls and simply have a height
 * defined in the form of a number of sleepers.
 * 
 * @author Mitchell
 */
public class Bay {
        // The number of sleepers in this bay.
	private int sleepers;

        /**
         * Constructor - Bay(double height):
         * Defines a bay with preferred height "height".
         * Height will be rounded up in increments of 0.2m
         * (only if bay is half or more the size of a bay)
         *
         * @param height - height to round.
         */
	public Bay(double height) {
		// height must be greater than or equal to 0
                if(height < 0) return;

                setSleepers(height, true);
	}

        /**
         * setSleepers(double value, boolean isHeight):
         * Takes either a height or a specific number of sleepers and converts
         * the number if required, then storing int the sleepers variable.
         * Height will be rounded up in increments of 0.2m
         * (only if bay is half or more the size of a bay)
         *
         * @param value - either a height value or an integer number of sleepers.
         * @param isHeight - If true, "value" is a height, else "value" is a number of sleepers.
         */
	public void setSleepers(double value, boolean isHeight) {
		if(value < 0) {
                    sleepers = 0;
                    return;
                }

		if(isHeight) {
                    // Modify the height by time 10
                    // This allows altering of the first decimal place.
                    int modheight = (int)(value * 10);
                    if(modheight%2 != 0) {
                        value = (int)(modheight - modheight%2) + 2;
                    } else {
                        value = modheight;
                    }

                    // divide by 0.2 to get the number of sleepers
                    sleepers = (int)(value / 2);
		} else {
                    sleepers = (int)value;
		}
	}

        /**
         * getSleepers():
         * Accessor method to get the number of sleepers in this bay.
         *
         * @return The number of sleepers.
         */
	public int getSleepers() {
		return sleepers;
	}
}