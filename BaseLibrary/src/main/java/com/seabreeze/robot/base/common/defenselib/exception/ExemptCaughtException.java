package com.seabreeze.robot.base.common.defenselib.exception;

public class ExemptCaughtException extends RuntimeException {
    public ExemptCaughtException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExemptCaughtException(Throwable cause) {
        super(cause);
    }
}
