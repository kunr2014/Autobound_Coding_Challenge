package com.neetika.challenge.autobound.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpreadSheet {

    @Id
    private String id;

}

