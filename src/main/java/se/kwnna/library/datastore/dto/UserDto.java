package se.kwnna.library.datastore.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import se.kwnna.library.domain.user.User;

@Getter
@Setter
@Entity
public class UserDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;

    public static UserDto fromDomain(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setAddress(user.getAddress());
        return userDto;
    }

    public User toDomain() {
        return User.builder()
                .withId(id)
                .withName(name)
                .withAddress(address)
                .build();
    }
}
