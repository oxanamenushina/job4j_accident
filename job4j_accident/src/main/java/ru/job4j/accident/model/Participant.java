package ru.job4j.accident.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Participant.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Entity
@Table(name = "participant")
public class Participant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (name = "name")
    private String name;

    @Column (name = "address")
    private String address;

    @Column (name = "passport_data")
    private String passportData;

    @Column (name = "phone")
    private String phone;

    @OneToMany(mappedBy = "participant", fetch = FetchType.EAGER)
    private Set<AccidentParticipant> accidentParticipants = new HashSet<>();

    @Transient
    private ParticipantStatus status;

    public Participant() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Set<AccidentParticipant> getAccidentParticipants() {
        return accidentParticipants;
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
                + ", phone='" + phone
                + '}';
    }
}