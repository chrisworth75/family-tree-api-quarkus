package com.familytree.api.dto;

import com.familytree.api.entity.Member;

public class CreateTreeRequest {
    public String name;
    public String createdBy;
    public RootMemberRequest rootMember;

    public static class RootMemberRequest {
        public String firstName;
        public String lastName;
        public String birthDate;
        public String deathDate;
        public Member.Gender gender;
        public Boolean isAlive;
    }
}
