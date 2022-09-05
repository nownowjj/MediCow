package com.medicow.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HospitalFindDto {
    private String region;
    private String hospname;
    private String hospcate;
    private String user_addr;
    private Double user_x;
    private Double user_y;
    private int distKm;
    private int radius;
}
