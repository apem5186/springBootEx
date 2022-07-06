package org.zerock.club.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;

import javax.persistence.Entity;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String > {

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM ClubMember m WHERE m.fromSocial = :social and m.email =:email")
    Optional<ClubMember> findByEmail(String email, boolean social);
}
