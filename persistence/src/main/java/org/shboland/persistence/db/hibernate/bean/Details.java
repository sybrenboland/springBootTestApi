package org.shboland.persistence.db.hibernate.bean;

import javax.persistence.MapsId;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DETAILS")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Details {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "NUMBER")
    private Long number;
    
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Student student;
    
    // @Input
    
}