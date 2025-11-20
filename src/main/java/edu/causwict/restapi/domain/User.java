package edu.causwict.restapi.domain;

import edu.causwict.restapi.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    // 양방향 매핑 (User -> Post)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default // 빌더 패턴 사용 시 리스트 초기화 유지
    private List<Post> postList = new ArrayList<>();
}
