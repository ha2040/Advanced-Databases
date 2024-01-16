package se.lu.ics.data;

public class TextValidator {

    public static boolean isValidNumber(String text) {
        // Use a regular expression to check if the input consists of only numbers
        return text.matches("\\d+");
    }

    public static boolean isNotEmpty(String text) {
        return !text.isEmpty();
    }

    public static boolean isDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
