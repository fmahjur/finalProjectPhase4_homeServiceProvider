package ir.maktab.finalprojectphase4.data.model;

import ir.maktab.finalprojectphase4.data.enums.ExpertStatus;
import ir.maktab.finalprojectphase4.data.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Expert extends Account {
    @Enumerated(EnumType.STRING)
    ExpertStatus expertStatus;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    byte[] personalPhoto;

    double rate;

    Long credit;

    @ManyToMany(mappedBy = "experts", cascade = CascadeType.ALL)
    Set<SubService> subServices = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "expert", cascade = {CascadeType.ALL})
    List<Comment> comments = new ArrayList<>();

    boolean isDeleted;

    public Expert() {
        this.expertStatus = ExpertStatus.NEW;
        this.rate = 0;
        this.isDeleted = false;
    }

    @Builder
    public Expert(String firstname, String lastname, String email, String username, String password, Boolean isActive, byte[] personalPhoto) {
        super(firstname, lastname, email, username, password, isActive, Role.ROLE_EXPERT);
        this.personalPhoto = personalPhoto;
    }

    public void addSubService(SubService subService) {
        this.subServices.add(subService);
        subService.getExperts().add(this);
    }

    public void deleteSubService(SubService subService) {
        this.subServices.remove(subService);
        subService.getExperts().remove(this);
    }

    public void setRate() {
        double sum = 0;
        for (Comment comment :
                comments) {
            sum += comment.getScore();
        }
        this.rate = sum / comments.size();
    }
}
