package ru.job4j.accident.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
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
@Entity
@Table(name = "accident")
public class Accident implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (name = "name")
    private String name;

    @Column (name = "description", length = 10000)
    private String desc;

    @Column (name = "address")
    private String address;

    @Column (name = "creation_date", updatable = false)
    @Temporal(TemporalType.DATE)
    private Date created = new Date();

    @OneToMany(mappedBy = "accident", fetch = FetchType.EAGER)
    private Set<AccidentParticipant> accidentParticipants = new HashSet<>();

    @Transient
    private Set<Participant> participants = new HashSet<>();

    public Accident() {
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

    public Date getCreated() {
        return created;
    }

    public Set<AccidentParticipant> getAccidentParticipants() {
        return accidentParticipants;
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
                && Objects.equals(created, accident.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, address, created);
    }

    @Override
    public String toString() {
        return "Accident{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", desc='" + desc + '\''
                + ", address='" + address + '\''
                + ", created=" + created
                + ", accidentParticipants=" + accidentParticipants
                + ", participants=" + participants
                + '}';
    }
}