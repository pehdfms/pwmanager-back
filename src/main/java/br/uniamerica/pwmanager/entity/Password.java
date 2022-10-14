package br.uniamerica.pwmanager.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "passwords")
public class Password extends AbstractEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "site", nullable = false)
    private String site;

    @Column(name = "password", nullable = false)
    private String password;
}
