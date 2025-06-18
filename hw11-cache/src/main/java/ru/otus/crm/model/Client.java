package ru.otus.crm.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();

    public Client(String name) {
        this.id = null;
        this.address = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
        this.address = null;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.address = address.clone();
        this.id = id;
        this.name = name;
        List<Phone> clonedPhones = new ArrayList<>();
        for (Phone phone : phones) {
            Phone phone1 = phone.clone();
            phone1.setClient(this);
            clonedPhones.add(phone1);
        }
        this.phones = clonedPhones;
    }

    @Override
    public Client clone() {
        List<Phone> clonedPhones = new ArrayList<>();
        for (Phone phone : this.phones) {
            clonedPhones.add(phone.clone());
        }
        return new Client(this.id, this.name, this.address.clone(), clonedPhones);
    }

    @Override
    public String toString() {
        return "Client{"
                + "id=" + id
                + ", name='" + name
                + "', address ='" + address
                + "', phones = '" + phonesToString(phones) + '\'' + '}';
    }

    private String phonesToString(List<Phone> phones) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < phones.size(); i++) {
            sb.append(phones.get(i).toString());
            if (i < phones.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
