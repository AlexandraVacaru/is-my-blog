package com.unibuc.ismyblog.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long blogId;

    @NotEmpty(message = "Title cannot be empty!")
    @NotNull
    private String title;

    @NotEmpty(message = "Blog content cannot be empty!")
    @NotNull
    private String content;

    @Column(columnDefinition = "enum('TRAVEL', 'FOOD', 'FASHION','OTHER')")
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @OneToMany(mappedBy = "blog")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Picture> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "blog")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public String getFormattedContent() {
        if (content.length() < 20) {
            return content;
        }
        return content.substring(0, 20) + "...";
    }
}
