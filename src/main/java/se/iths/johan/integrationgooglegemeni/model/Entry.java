package se.iths.johan.integrationgooglegemeni.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @Column(length = 1000)
    private String answer;

    public Entry() {
    }

    public Entry(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }


}
