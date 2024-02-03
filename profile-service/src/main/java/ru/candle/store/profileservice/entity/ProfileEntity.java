package ru.candle.store.profileservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class ProfileEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "city")
    private String city;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "address")
    private String address;
}
