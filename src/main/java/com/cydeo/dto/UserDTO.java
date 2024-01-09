package com.cydeo.dto;

import com.cydeo.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Can not be blank")
    @Size(max = 15, min = 2)
    private String firstName;

    @NotBlank(message = "Can not be blank")
    @Size(max = 15, min = 2)
    private String lastName;

    //    @Email
    @NotBlank(message = "can not be blank")
    private String userName;

    @NotBlank(message = "can not be blank")
    //@Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passWord;

    //@NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassWord;

    private boolean enabled;


    @Pattern(regexp = "^\\d{10}$",message = "must have 10 digits")
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


