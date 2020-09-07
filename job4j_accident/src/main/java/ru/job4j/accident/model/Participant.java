package ru.job4j.accident.model;

import java.util.Objects;

/**
 * Participant.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public class Participant {

    private int id;
    private String name;
    private String address;
    private String passportData;
    private String phone;
    private ParticipantStatus status;

    public Participant() {
    }

    public Participant(int id, String name, String address, String email, String phone, ParticipantStatus status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.passportData = email;
        this.phone = phone;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassportData() {
        return passportData;
    }

    public void setPassportData(String passportData) {
        this.passportData = passportData;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ParticipantStatus getStatus() {
        return status;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Participant that = (Participant) o;
        return id == that.id
                && Objects.equals(name, that.name)
                && Objects.equals(address, that.address)
                && Objects.equals(passportData, that.passportData)
                && Objects.equals(phone, that.phone)
                && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, passportData, phone, status);
    }

    @Override
    public String toString() {
        return "Participant{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", address='" + address + '\''
                + ", passportData='" + passportData + '\''
                + ", phone='" + phone + '\''
                + ", status=" + status
                + '}';
    }
}