package com.splab.splabassignment.member.repository

import com.splab.splabassignment.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    @Query( "SELECT m " +
            "FROM Member m " +
            "WHERE 1 = 1 " +
            "AND m.memberId = :memberId " +
            "AND m.deletedAt IS NULL ")
    fun findByIdAndNotDeleted(@Param("memberId") memberId: Long): Member?

    @Query( "SELECT m " +
            "FROM Member m " +
            "WHERE 1 = 1 " +
            "AND m.memberEmail = :email " +
            "AND m.deletedAt IS NULL ")
    fun findByMemberEmail(@Param("email") email: String): Member?

    fun existsByMemberEmailAndMemberIdNot(memberEmail: String, memberIdNot: Long): Boolean
}