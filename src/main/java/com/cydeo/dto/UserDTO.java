package com.cydeo.dto;

import com.cydeo.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotBlank
    @Size(max = 15, min = 2)
    private String firstName;

    @NotBlank
    @Size(max = 15, min = 2)
    private String lastName;
    @NotBlank
    @Email
    private String userName;

   // @NotBlank
    //@Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passWord;

    //@NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassWord;

    private boolean enabled;

    @NotBlank
    @Pattern(regexp = "^\\d{10}$")
    private String phone;

    @NotNull
    private RoleDTO role;

    @NotNull
    private Gender gender;

    public void setPassWord(String passWord) {
        this.passWord = passWord;
        checkConfirmPassWord();
    }

    public void setConfirmPassWord(String confirmPassWord) {
        this.confirmPassWord = confirmPassWord;
        checkConfirmPassWord();
    }

    private void checkConfirmPassWord() {
        if(this.passWord == null || this.confirmPassWord == null){
            return;
        }else if(!this.passWord.equals(confirmPassWord)){
            this.confirmPassWord = null;
        }
    }

}


