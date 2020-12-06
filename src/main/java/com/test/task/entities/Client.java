package com.test.task.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @NotNull
    @Column(name = "name_fld")
    private String name;

    @NotNull
    @Column(name = "shortname_fld")
    private String shortName;

    @NotNull
    @Column(name = "address_fld")
    private String address;

    @OneToMany
    @JoinTable(name = "deposits_clients",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "deposit_id"))
    private List<Deposit> deposits;

    @ManyToOne
    @JoinTable(name = "clients_forms",
    joinColumns = @JoinColumn(name = "client_id"),
    inverseJoinColumns = @JoinColumn(name = "form_id"))
    private Form form;
}
