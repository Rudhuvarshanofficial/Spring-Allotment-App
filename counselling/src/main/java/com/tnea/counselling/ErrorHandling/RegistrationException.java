package com.tnea.counselling.ErrorHandling;

import java.time.LocalTime;

public class RegistrationException extends RuntimeException{
    private LocalTime now;
    private String s;

    public RegistrationException(LocalTime now, String message, String s) {
        super(message);
        this.now = now;
        this.s = s;
    }

    public LocalTime getNow() {
        return now;
    }

    public void setNow(LocalTime now) {
        this.now = now;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
