package se.lu.ics.DAO;

import java.sql.SQLException;

public class UniversalError extends Exception {
    private int errorCode;

    public UniversalError(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public static UniversalError fromSQLException(SQLException e, int errorCode) {
        if (e.getMessage().contains("duplicate")) {
            return new UniversalError("Duplicate ID detected. Please try another ID.", e.getErrorCode());
        } else {
            return new UniversalError("An error occurred: "+e.getMessage(), e.getErrorCode());
        }
    }
}

