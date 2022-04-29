package com.example.demo.categories.data.user;

import com.example.demo.categories.data.order.OrderDataMapper;
import com.example.demo.categories.data.rating.RatingDataMapper;
import com.example.demo.categories.data.refresh_token.RefreshTokenDataMapper;
import com.example.demo.categories.data.role.RoleDataMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "os_users",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "telephoneNumber")
        })
public class UserDataMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = ":id Пользователя", name = "id", required = true, example = "13")
    private Long id;

    @ApiModelProperty(notes = "Email пользователя", name = "telephoneNumber", required = true, example = "first@mail.ru")
    @NumberFormat
    @NotNull
    @Column(name = "telephoneNumber")
    private String telephoneNumber;


    @ApiModelProperty(notes = "Пароль пользователя", name = "password", required = true,
            example = "$2a$12$4kMSRkNGisnBLGs1l28ZVum9dzm.xKeBftE/Vr7MsbypMH9sPXyH.")
    @NotBlank(message = "Некорректный формат password")
    @Column(name = "password")
    private String password;


    @ApiModelProperty(notes = "Роли пользователя", name = "roles", required = true)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "os_roles_user_entities",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<RoleDataMapper> roleDataMappers;


    @ApiModelProperty(notes = "Заказы пользователя", name = "order")
    @OneToMany(mappedBy = "userDataMapper", cascade = CascadeType.ALL)
    private List<OrderDataMapper> orderDataMappers;


    @ApiModelProperty(notes = "Рейтинг (оценки для девайсов) пользователя", name = "ratings")
    @OneToMany(mappedBy = "userDataMapper")
    private List<RatingDataMapper> ratingDataMappers;


    @ApiModelProperty(notes = "RefreshToken пользователя", name = "refreshToken", required = true)
    @OneToOne(mappedBy = "userDataMapper")
    private RefreshTokenDataMapper refreshTokenDataMapper;


    @ApiModelProperty(notes = "ФИО пользователя", name = "fio")
    private String FIO;
    @ApiModelProperty(notes = "Верификация аккаунта (true - успешно верифицирован)", name = "verification")
    private Boolean verification;

    @ApiModelProperty(notes = "Пол (true - мужской)", name = "isMan")
    @Nullable
    private Boolean isMan;

    @ApiModelProperty(notes = "Время создания записи", name = "timeOfCreation")
    @Nullable
    private Long timeOfCreation;

    @ApiModelProperty(notes = "Код для верификации аккаунта", name = "code")
    @Nullable
    private Integer code;

}
