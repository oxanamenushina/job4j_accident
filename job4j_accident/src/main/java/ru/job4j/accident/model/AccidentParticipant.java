package ru.job4j.accident.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * AccidentParticipant.
 *
 * @author Oxana Menushina (oxsm@mail.ru).
 * @version $Id$
 * @since 0.1
 */
@Entity
@Table(name = "accident_participant")
public class AccidentParticipant implements Serializable {

    @EmbeddedId
    private APId id = new APId();

    @ManyToOne
    @JoinColumn(name = "accident_id", insertable = false, updatable = false)
    private Accident accident;

    @ManyToOne
    @JoinColumn(name = "participant_id", insertable = false, updatable = false)
    private Participant participant;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NotNull
    private ParticipantStatus status;

    public AccidentParticipant() {
    }

    public static AccidentParticipant of(Accident accident, Participant participant, ParticipantStatus status) {
        AccidentParticipant ap = new AccidentParticipant();
        ap.accident = accident;
        ap.participant = participant;
        ap.status = status;
        ap.id.accidentId = accident.getId();
        ap.id.participantId = participant.getId();
        ap.accident.getAccidentParticipants().add(ap);
        ap.participant.getAccidentParticipants().add(ap);
        return ap;
    }

    public APId getId() {
        return id;
    }

    public void setId(APId id) {
        this.id = id;
    }

    public Accident getAccident() {
        return accident;
    }

    public void setAccident(Accident accident) {
        this.accident = accident;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
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
        AccidentParticipant that = (AccidentParticipant) o;
        return Objects.equals(id, that.id)
                && Objects.equals(accident, that.accident)
                && Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accident, participant);
    }

    @Override
    public String toString() {
        return "AccidentParticipant{"
                + "id=" + id
                + ", status=" + status
                + '}';
    }

    /**
     * Class APId.
     */
    @Embeddable
    private static class APId implements Serializable {

        @Column(name = "accident_id")
        private long accidentId;

        @Column(name = "participant_id")
        private long participantId;

        public APId() {
        }

        public APId(long accidentId, long participantId) {
            this.accidentId = accidentId;
            this.participantId = participantId;
        }

        public long getAccidentId() {
            return accidentId;
        }

        public void setAccidentId(long accidentId) {
            this.accidentId = accidentId;
        }

        public long getParticipantId() {
            return participantId;
        }

        public void setParticipantId(long participantId) {
            this.participantId = participantId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            APId apId = (APId) o;
            return Objects.equals(accidentId, apId.accidentId) && Objects.equals(participantId, apId.participantId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accidentId, participantId);
        }
    }
}