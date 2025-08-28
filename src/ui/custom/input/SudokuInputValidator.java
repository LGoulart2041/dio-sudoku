package ui.custom.input;

public class SudokuInputValidator implements InputValidator {
    @Override
    public boolean isValid(String input) {
        if(input == null || input.isEmpty()) return true;
        try {
            int value = Integer.parseInt(input);
            return value >= 1 && value <= 9;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
