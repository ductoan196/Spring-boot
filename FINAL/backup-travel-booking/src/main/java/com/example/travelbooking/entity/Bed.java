package com.example.travelbooking.entity;

import com.example.travelbooking.statics.BedType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "beds")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bed extends BaseEntity{

    String name;

    BedType bedType;

    int bed_nums;
}
