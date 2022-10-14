package br.uniamerica.pwmanager.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "passwords")
public class Password extends AbstractEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @Getter @Setter
    private User user;

    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Column(name = "site", nullable = false)
    @Getter @Setter
    private String site;

    @Column(name = "password", nullable = false)
    @Getter @Setter
    private String password;
}
