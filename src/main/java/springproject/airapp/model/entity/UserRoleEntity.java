package coherentsolutions.airapp.model.entity;

import coherentsolutions.airapp.model.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRoleEnum userRole;

    @OneToMany(mappedBy="userRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<UserEntity> userEntity;

}
