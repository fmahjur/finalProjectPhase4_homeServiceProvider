package ir.maktab.finalprojectphase4.data.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Comment extends BaseEntity {
    String comment;
    double score;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="expert_id")
    Expert expert;

    boolean isDeleted;

    public Comment(Long id, String comment, double score, Expert expert) {
        super(id);
        this.comment = comment;
        this.score = score;
        this.expert = expert;
        this.isDeleted = false;
    }

    public Comment(Long id, String comment, double score) {
        super(id);
        this.comment = comment;
        this.score = score;
        this.isDeleted = false;
    }
}
