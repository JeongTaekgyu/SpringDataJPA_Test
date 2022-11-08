package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername")
//    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // dto로 반환한다.
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 컬렉션 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); // 단건 Optional
    List<Member> findByUsername(String username);

//    Page<Member> findByAge(int age, Pageable pageable);
    // totalCount는 데이터가 많아질 수 록 성능이 잘 안나올 수 있다. 그러므로 countQuery를 사용할 수 있다.
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    // @Modifying은 jpql의 executeUpdate()메서드와 같은 역할
    @Modifying(clearAutomatically = true) // EntityManager를 clear한다.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"}) // fetch join 하게 해준다.(위에 fetch join한 코드와 동일하다)
    List<Member> findAll();

    // 이처럼 JPQL + @EntityGraph 도 가능하다.
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    // readOnly true가 되어있으면 최적화를 시켜서 스냅샵을 안만든다.
    // 즉 set으로 데이터 변경해도 update문이 나가지 않는다.
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    // Lock이란 트랜잭션의 충돌이 발생한다고 가정하고 우선 락을 걸고 보는 방법이다.
    // 트랜잭션안에서 서비스로직이 진행되어야함.
    // 비관적 잠금 메커니즘은 데이터베이스 수준에서 엔티티 잠금을 포함한다.
    // 실시간 트래픽이 많으면 Lock을 걸기 부담스럽다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);


}
