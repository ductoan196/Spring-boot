package com.example.refreshtokenagain.entity;

import com.example.refreshtokenagain.statics.Roles;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity{
    @Enumerated(EnumType.STRING)
    Roles name;
}
