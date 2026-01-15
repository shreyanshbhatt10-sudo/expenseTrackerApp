package org.example.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.entities.UserInfo;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto extends UserInfo {

    @NonNull
    private String firstName; // first_name

    @NonNull
    private String lastName;  // last_name

    @NonNull
    private Long phoneNumber; // phone_number

    @NonNull
    private String email;
}
