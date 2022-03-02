package com.group15A.DataModel;

import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

/**
 *  Used to represent a notification within the system, will be used to pass and change
 *  information between the business logic and data access layer.
 *
 *  @author Wenbo Wu
 */
public class Notification {

    private Integer NotifID;
    private Integer doctorID;
    private Integer patientID;
    private String message;
    private LocalTime timestamp;

    public Notification(Integer notifID, Integer doctorID, Integer patientID, String message, LocalTime timestamp) {
        NotifID = notifID;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Integer getNotifID() {
        return NotifID;
    }

    public void setNotifID(Integer notifID) {
        NotifID = notifID;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(getNotifID(), that.getNotifID()) && Objects.equals(getDoctorID(), that.getDoctorID()) && Objects.equals(getPatientID(), that.getPatientID()) && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNotifID(), getDoctorID(), getPatientID(), getMessage(), getTimestamp());
    }

    @Override
    public String toString() {
        return "Notification{" +
                "NotifID=" + NotifID +
                ", doctorID=" + doctorID +
                ", patientID=" + patientID +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
