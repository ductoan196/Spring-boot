package com.example.bus_management_ajax.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Assignment {
    int id;
    Driver driver;
    Route route;
    String soLuotChay;
}
