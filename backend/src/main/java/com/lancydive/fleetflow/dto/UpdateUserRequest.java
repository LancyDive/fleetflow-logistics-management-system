package com.lancydive.fleetflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {

//	.*    → zero or more of any character
//	\\S   → one non-whitespace character
//	.*    → zero or more of any character
    @Pattern(
            regexp = ".*\\S.*",
            message = "First name cannot be blank"
        )
        private String firstName;

        @Pattern(
            regexp = ".*\\S.*",
            message = "Last name cannot be blank"
        )
        private String lastName;

        @Pattern(
            regexp = ".*\\S.*",
            message = "Email cannot be blank"
        )
        @Email(message = "Invalid email format")
        private String email;
    

}