package server.api.termterm.domain.folder;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.termterm.domain.basetime.BaseTimeEntity;
import server.api.termterm.domain.member.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Folder extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOLDER_ID")
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public Folder(String name, String description, Member member) {
        this.name = name;
        this.description = description;
        this.member = member;
    }
}
