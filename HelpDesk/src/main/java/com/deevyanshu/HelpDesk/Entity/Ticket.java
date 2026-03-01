package com.deevyanshu.HelpDesk.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "help_desk_tickets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String summary;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(unique = true)
    private String email;

    @Column(length = 1000)
    private String description;

    private String category;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    @Enumerated(EnumType.STRING)
    private Status status;

    @PrePersist
    void preSave(){
        if(this.createdOn==null)
        {
            this.createdOn=LocalDateTime.now();
        }
        this.updatedOn=LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate(){
        this.updatedOn=LocalDateTime.now();
    }

}
