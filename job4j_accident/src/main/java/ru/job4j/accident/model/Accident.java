package ru.job4j.accident.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Accident.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
public class Accident {

    private int id;
    private String name;
    private String desc;
    private String address;
    private Set<Participant> participants = new HashSet<>();

    public Accident() {
    }

    public Accident(int id, String name, String desc, String address) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.address = address;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Accident accident = (Accident) o;
        return id == accident.id
                && Objects.equals(name, accident.name)
                && Objects.equals(desc, accident.desc)
                && Objects.equals(address, accident.address)
                && Objects.equals(participants, accident.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, address, participants);
    }

    @Override
    public String toString() {
        return "Accident{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", text='" + desc + '\''
                + ", address='" + address + '\''
                + ", participants=" + participants
                + '}';
    }
}